package com.footballmanager.seasons

import com.footballmanager.entities.Club
import com.footballmanager.entities.match.Match
import com.footballmanager.entities.season.Season
import com.footballmanager.entities.season.schedule.LeagueSchedule
import com.footballmanager.entities.season.schedule.Round
import kotlin.random.Random
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.UUID

@Service
class ScheduleService {
    /**
     * Генерирует расписание матчей лиги (double round-robin).
     *
     * Алгоритм — круговой метод (circle method):
     * - Фиксируем первую команду, остальные вращаем по кругу
     * - В каждом туре пары: первый с последним, второй с предпоследним и т.д.
     * - Первый круг: каждый с каждым один раз
     * - Второй круг: ответные матчи (home/away меняются)
     * - Чётность тура определяет, какая команда в паре дома
     *
     * @param season сезон с клубами, в который добавляются матчи
     * @return расписание с турами
     */
    fun generateLeagueSchedule(season: Season): LeagueSchedule {
        // Случайно перемешиваем клубы для рандомизации итогового расписания
        val clubs = season.clubs.shuffled()
        val n = clubs.size

        // Для турнира нужно минимум 2 клуба, иначе возвращаем пустое расписание
        if (n < 2) return LeagueSchedule(emptyList())

        // Если клубов нечётное количество, добавляем фиктивный null ("выходной")
        // Это нужно чтобы в каждом туре у всех были пары
        val effectiveN = if (n % 2 == 1) n + 1 else n
        val teams: List<Club?> = clubs + List(effectiveN - n) { null }

        // Базовая дата — 1 июля года сезона (начало футбольного сезона)
        val baseDate = Date.from(
            LocalDate.of(season.year, 7, 1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )
        // Константа: количество миллисекунд в одной неделе
        val weekMs = 7 * 24 * 60 * 60 * 1000L

        // Первая команда фиксирована — она всегда остаётся на своей позиции
        val fixed = teams[0]
        // Остальные команды будут вращаться по кругу (cyclic shift)
        var rest = teams.drop(1)

        /**
         * Вращает список rest: последний элемент становится вторым
         * (после fixed). Остальные сдвигаются вправо.
         *
         * Пример: [A, B, C, D] -> [D, A, B, C]
         * Когда paired с fixed: fixed + [D, A, B, C] даёт пары (fixed,C), (D,B), (A,?)
         */
        fun rotate() {
            rest = listOf(rest.last()) + rest.dropLast(1)
        }

        /**
         * Собирает матчи одного тура по текущему расположению команд.
         *
         * Круговой метод: берём команды с обоих концов списка и складываем в пары.
         * Например, для списка [0, 1, 2, 3, 4, 5] пары: (0,5), (1,4), (2,3).
         *
         * Проблема курицы и яйца: Match требует Round в конструктор, но Round
         * собирается из списка Match. Решение: создаём заглушку Round (без матчей),
         * конструируем Match с ней, затем собираем настоящий Round с матчами.
         * Match.round будет ссылаться на заглушку (пустой список матчей),
         * но номер тура (match.round.number) будет корректным.
         *
         * @param arranged текущий порядок команд (фиксированная + rest)
         * @param roundNumber номер тура (1-based), используется для расчёта даты
         * @param isLeftHome true — левая команда в паре играет дома, false — правая
         * @return Round с матчами тура
         */
        fun buildRound(arranged: List<Club?>, roundNumber: Int, isLeftHome: Boolean): Round {
            // Создаём заглушку Round (без матчей) — нужна для конструктора Match
            val placeholderRound = Round(emptyList(), roundNumber, passed = false, season)

            // Формируем матчи тура — каждый Match ссылается на placeholderRound
            val matches = buildList {
                // Проходим половину списка, паруя i-й элемент с (N-1-i)-м
                for (i in 0 until effectiveN / 2) {
                    val left = arranged[i]
                    val right = arranged[effectiveN - 1 - i]

                    // Пропускаем пары с null (фиктивная команда — выходной)
                    if (left != null && right != null) {
                        // Чередование home/away: в зависимости от isLeftHome
                        // определяем кто дома, кто на выезде
                        val (home, away) = if (isLeftHome) left to right else right to left

                        // Дата матча = базовая дата + (номер тура - 1) недель
                        val match = Match(
                            id = UUID.randomUUID(),
                            date = Date(baseDate.time + (roundNumber - 1) * weekMs),
                            homeTeam = home,
                            awayTeam = away,
                            round = placeholderRound,
                        )
                        add(match)
                        // Сохраняем матч в сезон (позже пересохраним с правильным round)
                    }
                }
            }

            // Добавляем матчи в сезон
            season.matches.addAll(matches)

            // Возвращаем Round уже с заполненным списком матчей
            return Round(matches, roundNumber, passed = false, season)
        }

        // Количество туров в одном круге (N-1 для N команд)
        val halfRounds = effectiveN - 1

        // ─── ПЕРВЫЙ КРУГ ──────────────────────────────────────────────────
        // Каждая пара встречается один раз.
        // Правило: в чётных турах (0, 2, 4...) левая команда дома;
        //          в нечётных (1, 3, 5...) — правая команда дома.
        // После каждого тура rest вращается для новых пар.
        val firstHalf = buildList {
            for (roundIndex in 0 until halfRounds) {
                add(buildRound(listOf(fixed) + rest, roundIndex + 1, roundIndex % 2 == 0))
                rotate()
            }
        }

        // ─── ВТОРОЙ КРУГ ──────────────────────────────────────────────────
        // Ответные матчи: те же пары, но home/away инвертированы.
        // Правило наоборот: в чётных турах правая команда дома;
        //                   в нечётных — левая команда дома.
        // Нумерация туров продолжается после первого круга.
        // rest продолжает вращение с того же места (после первого круга
        // он вернулся в исходное положение).
        val secondHalf = buildList {
            for (roundIndex in 0 until halfRounds) {
                add(buildRound(listOf(fixed) + rest, halfRounds + roundIndex + 1, roundIndex % 2 != 0))
                rotate()
            }
        }

        // Перемешиваем туры внутри каждого круга для непредсказуемости
        val rng = Random(System.currentTimeMillis())
        val allRounds = (firstHalf.shuffled(rng) + secondHalf.shuffled(rng))
            // Перенумеровываем туры последовательно 1, 2, 3...
            .mapIndexed { index, round -> round.copy(number = index + 1) }

        return LeagueSchedule(rounds = allRounds)
    }
}