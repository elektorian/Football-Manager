# Domain Entities and Repository Layer Specification

## Domain Entities

### 1. Person

**Location**: `src/main/kotlin/com/footballmanager/domain/person/Person.kt`

```kotlin
package com.footballmanager.domain.person

import java.util.UUID
import java.time.LocalDate

class Person(
    val id: UUID,
    val name: String,
    val surname: String,
    val birthDate: LocalDate?,
    val clubId: UUID?
)
```

**Rationale**:
- `id`, `name`, `surname` are non-nullable (required for identification)
- `birthDate`, `clubId` are nullable (optional)
- Uses `val` (immutable properties)
- UUID for DB compatibility (future JPA support)

### 2. Club

**Location**: `src/main/kotlin/com/footballmanager/domain/club/Club.kt`

```kotlin
package com.footballmanager.domain.club

import java.util.UUID
import java.util.concurrent.CopyOnWriteArraySet

class Club(
    val id: UUID,
    val name: String,
    val tournamentId: UUID?,
    val managerId: UUID?,
    val playerIds: CopyOnWriteArraySet<UUID>
)
```

**Rationale**:
- `id`, `name` are non-nullable
- `tournamentId`, `managerId` are nullable (optional)
- `playerIds` is non-nullable `MutableSet` (required but can be empty)
- Uses `CopyOnWriteArraySet` for thread-safe mutable collection

### 3. Tournament

**Location**: `src/main/kotlin/com/footballmanager/domain/tournament/Tournament.kt`

```kotlin
package com.footballmanager.domain.tournament

import java.util.UUID
import java.util.concurrent.CopyOnWriteArraySet

class Tournament(
    val id: UUID,
    val name: String,
    val clubIds: CopyOnWriteArraySet<UUID>,
    val rounds: Int
)
```

**Rationale**:
- All fields non-nullable
- `clubIds` is non-nullable `MutableSet` (required but can be empty)
- Uses `CopyOnWriteArraySet` for thread-safe mutable collection

---

## Repository Layer

### Repository Interfaces

**Location**: `src/main/kotlin/com/footballmanager/domain/repository/`

Each repository interface defines minimal operations: `save()` and `get()` only.

#### PersonRepository

```kotlin
package com.footballmanager.domain.repository

import com.footballmanager.domain.person.Person
import java.util.UUID

interface PersonRepository {
    fun save(person: Person): Person
    fun get(id: UUID): Person
}
```

**Rationale**:
- `save(person)` — stores entity, returns same object
- `get(id)` — retrieves by UUID, throws if not found
- No `getAll()` — not required
- No `delete()` — never needed (per requirements)

#### ClubRepository

```kotlin
package com.footballmanager.domain.repository

import com.footballmanager.domain.club.Club
import java.util.UUID

interface ClubRepository {
    fun save(club: Club): Club
    fun get(id: UUID): Club
}
```

#### TournamentRepository

```kotlin
package com.footballmanager.domain.repository

import com.footballmanager.domain.tournament.Tournament
import java.util.UUID

interface TournamentRepository {
    fun save(tournament: Tournament): Tournament
    fun get(id: UUID): Tournament
}
```

---

## Infrastructure Implementation

**Location**: `src/main/kotlin/com/footballmanager/infrastructure/storage/`

### InMemoryPersonRepository

```kotlin
package com.footballmanager.infrastructure.storage

import com.footballmanager.domain.person.Person
import com.footballmanager.domain.repository.PersonRepository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryPersonRepository : PersonRepository {
    private val store = ConcurrentHashMap<UUID, Person>()

    override fun save(person: Person): Person {
        store[person.id] = person
        return person
    }

    override fun get(id: UUID): Person {
        return store[id]!!  // Asserts existence
    }
}
```

**Rationale**:
- `ConcurrentHashMap` for thread-safety (standard Java, no external deps)
- Empty store on creation (per requirements)
- No `getAll()` or `delete()` (not needed)
- `get()` uses `!!` assertion (throws if not found)
- `save()` returns entity as-is (immutable contract)

### InMemoryClubRepository

```kotlin
package com.footballmanager.infrastructure.storage

import com.footballmanager.domain.club.Club
import com.footballmanager.domain.repository.ClubRepository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryClubRepository : ClubRepository {
    private val store = ConcurrentHashMap<UUID, Club>()

    override fun save(club: Club): Club {
        store[club.id] = club
        return club
    }

    override fun get(id: UUID): Club {
        return store[id]!!
    }
}
```

### InMemoryTournamentRepository

```kotlin
package com.footballmanager.infrastructure.storage

import com.footballmanager.domain.tournament.Tournament
import com.footballmanager.domain.repository.TournamentRepository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryTournamentRepository : TournamentRepository {
    private val store = ConcurrentHashMap<UUID, Tournament>()

    override fun save(tournament: Tournament): Tournament {
        store[tournament.id] = tournament
        return tournament
    }

    override fun get(id: UUID): Tournament {
        return store[id]!!
    }
}
```

---

## Summary

### Files Created

| File | Location | Lines |
|------|----------|-------|
| Person.kt | domain/person/ | 14 |
| Club.kt | domain/club/ | 15 |
| Tournament.kt | domain/tournament/ | 14 |
| PersonRepository.kt | domain/repository/ | 6 |
| ClubRepository.kt | domain/repository/ | 6 |
| TournamentRepository.kt | domain/repository/ | 6 |
| InMemoryPersonRepository.kt | infrastructure/storage/ | 20 |
| InMemoryClubRepository.kt | infrastructure/storage/ | 20 |
| InMemoryTournamentRepository.kt | infrastructure/storage/ | 20 |

**Total**: 9 files, ~121 lines

### Technical Decisions

| Decision | Reason |
|----------|--------|
| UUID for IDs | Standard, future JPA compatible |
| `val` (immutable) | Kotlin idiom, clear contract |
| `CopyOnWriteArraySet` for collections | Thread-safe mutable set |
| `ConcurrentHashMap` for storage | Standard thread-safe HashMap |
| `!!` in `get()` | Simpler than nullable returns |
| No `getAll()` | Not required |
| No `delete()` | Never needed |
| No factory methods | Simple use case |

### Collection Types

- `playerIds: CopyOnWriteArraySet<UUID>` — Club's player collection
- `clubIds: CopyOnWriteArraySet<UUID>` — Tournament's club collection

**Why**: Thread-safe mutable sets. Writes are fast enough for in-memory use case.

### Thread-Safety

- All repository stores use `ConcurrentHashMap`
- All entity collections use `CopyOnWriteArraySet`
- Multiple threads can save/get concurrently without explicit locking
