# Rick and Morty

An Android app built with Jetpack Compose that lets you browse characters from the Rick and Morty universe, search through them, view details, and save your favorites.

## What it does

When you open the app you land on a scrollable list of characters fetched from the Rick and Morty API. The list is paginated so it loads more as you scroll. There's a search bar at the top that filters characters by name in real time, hitting the remote API for results.

Tapping a character opens a detail screen showing their image, status, species, gender, and location. The location section shows a small avatar pulled from the first resident of that location. You can mark any character as a favorite by tapping the heart icon.

A floating action button on the list screen takes you to a dedicated favorites screen. It shows only the characters you've saved and has its own search bar that filters within your favorites locally, without making any network requests.

## How it's built

The app follows a clean architecture split across three layers — data, domain, and presentation — with a unidirectional data flow in the UI layer using ViewModels, StateFlow for UI state, and SharedFlow for one-shot events like navigation and error messages.

**Networking** is handled by Retrofit with a Moshi converter. OkHttp sits underneath with a logging interceptor attached for debugging.

**Local storage** uses Room. Characters are cached in a single table after being fetched from the network. The favorites flag lives on the same entity, so toggling a favorite is just a local update that Room propagates reactively through a Flow.

**Pagination** is done with Paging 3. A RemoteMediator coordinates between the API and Room — it fetches pages from the network, writes them to the database, and the UI always reads from Room as the single source of truth. This means the list works offline with whatever was last cached.

**Dependency injection** is handled by Hilt throughout. ViewModels, repositories, the Room database, and Retrofit are all provided via Hilt modules.

**Navigation** uses the type-safe Navigation Compose API with kotlinx.serialization for route definitions, so routes are plain Kotlin objects rather than strings.

**Image loading** is handled by Coil with its OkHttp network fetcher.

## Tech stack

- Kotlin
- Jetpack Compose + Material 3
- Hilt
- Room
- Paging 3 with RemoteMediator
- Retrofit + Moshi + OkHttp
- Coil
- Navigation Compose (type-safe)
- Kotlin Coroutines and Flow
- ViewModel + StateFlow + SharedFlow

## Minimum requirements

Android 9 (API 28) or higher.
