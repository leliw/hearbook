# HearBook - Aplikacja Android

## Informacje o projekcie

- **Nazwa pakietu**: `eu.haintech.hearbook`
- **Minimalna wersja SDK**: 26 (Android 8.0)
- **Docelowa wersja SDK**: 34 (Android 14)
- **Wersja aplikacji**: 1.0
- **Historia zmian**: Zobacz [CHANGELOG.md](CHANGELOG.md)

## Technologie i biblioteki

### Główne komponenty

- **Kotlin**: Wersja 1.9.22
- **Jetpack Compose**: Wersja 1.5.8 - główny framework UI
- **Java**: Wersja 17

### Ważne zależności

- **CameraX**: Wersja 1.3.1 - obsługa aparatu
- **Compose Material 3** - nowoczesny system projektowania UI
- **Navigation Compose** - nawigacja w aplikacji

## Struktura projektu

```text
android/
├── app/                    # Główny moduł aplikacji
│   ├── src/               # Kod źródłowy
│   └── build.gradle.kts   # Konfiguracja modułu
├── build.gradle.kts       # Główna konfiguracja projektu
└── gradle/                # Pliki konfiguracyjne Gradle
```

## Konfiguracja środowiska deweloperskiego

1. **Wymagania**:
   - Android Studio (najnowsza wersja)
   - JDK 17
   - Android SDK z API level 34

2. **Pierwsze uruchomienie**:
   - Sklonuj repozytorium
   - Otwórz projekt w Android Studio
   - Poczekaj na synchronizację Gradle
   - Uruchom aplikację na emulatorze lub urządzeniu fizycznym

## Główne funkcje

- Interfejs użytkownika oparty na Jetpack Compose
- Integracja z aparatem poprzez CameraX
- Material Design 3 jako system projektowania
- Efekty podczas robienia zdjęć:
  - Dźwięk migawki aparatu
  - Animacja błysku ekranu
  - Automatyczne zapisywanie zdjęć z timestampem

## Testowanie

Projekt zawiera konfigurację dla:

- Testów jednostkowych (katalog `test/`)
- Testów instrumentalnych (katalog `androidTest/`)

## Uwagi dla deweloperów

1. Projekt używa Kotlin DSL dla plików Gradle
2. Compose jest głównym narzędziem do tworzenia UI
3. Minifikacja kodu jest wyłączona w buildType 'release'
4. Projekt wykorzystuje najnowsze wersje bibliotek AndroidX
5. Wszystkie istotne zmiany są dokumentowane w pliku [CHANGELOG.md](CHANGELOG.md)
