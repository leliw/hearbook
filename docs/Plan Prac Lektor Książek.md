# Plan Prac - Lektor Książek dla Słabowidzących

## 1. Przegląd Projektu

### Cel

Stworzenie aplikacji Android umożliwiającej osobom ze słabym wzrokiem skanowanie książek i odsłuchiwanie ich treści za pomocą syntetyzatora mowy.

### Zakres Czasowy

- **MVP:** 10-14 tygodni
- **Pełna wersja:** 6-9 miesięcy
- **Start projektu:** Do ustalenia z klientem

### Zespół

- **Android Developer Senior (Kotlin):** 1-2 osoby
- **UI/UX Designer z doświadczeniem w Accessibility:** 1 osoba
- **Tech Lead/Architekt:** 1 osoba (może być rola dodatkowa Senior Dev)
- **Koordynator testów z użytkownikami:** 1 osoba

## 2. Faza Przygotowawcza (Tygodnie 0-1)

### 2.1 Analiza i Wybór Technologii

**Czas:** 3-5 dni  
**Odpowiedzialny:** Tech Lead + Senior Android Dev

**Zadania:**

- [x] **Analiza i wybór API OCR**
  - Przetestowanie: Google Cloud Vision AI, AWS Textract, Azure Computer Vision
  - Kryteria: dokładność rozpoznawania, czas odpowiedzi, koszty
  - Testy na próbkach różnych typów książek/czcionek
- [x] **Analiza i wybór API TTS**
  - Przetestowanie: Google Cloud Text-to-Speech, AWS Polly, Azure Speech
  - Kryteria: jakość głosu (szczególnie polska mowa), naturalność, koszty
- [x] **Konfiguracja środowiska deweloperskiego**
  - Konfiguracja Android Studio z Kotlin
  - Utworzenie kont u wybranych dostawców API
  - Konfiguracja kluczy API i środowiska testowego

**Deliverable:** Dokument wyboru technologii z uzasadnieniem i konfiguracją środowiska

### 2.2 Projektowanie Architektury Szczegółowej

**Czas:** 2-3 dni  
**Odpowiedzialny:** Tech Lead + Senior Android Dev

**Zadania:**

- [x] **Projektowanie struktury modułów aplikacji**
  - Camera Module (skanowanie)
  - Storage Module (zarządzanie danymi lokalnymi)
  - Processing Module (komunikacja z API)
  - Playback Module (odtwarzanie audio)
  - UI Module (interfejs użytkownika)
- [x] **Definiowanie interfejsów między modułami**
- [x] **Projektowanie struktury bazy danych SQLite**
- [x] **Planowanie zarządzania stanem aplikacji**

**Deliverable:** Diagram architektury szczegółowej i specyfikacja modułów

## 3. Faza 1 - MVP (Tygodnie 2-14)

### 3.1 Sprint 1: Podstawowy UI i Skanowanie (Tygodnie 2-3)

**Czas:** 2 tygodnie  
**Odpowiedzialny:** Android Dev + UI/UX Designer

**Zadania:**

- [x] **Projektowanie UI/UX dla dostępności**
  - Wireframes ekranów głównych (Główny, Skanowanie, Czytanie)
  - Projektowanie dużych elementów UI (4-8 ikon na ekranie)
  - Wybór palet kolorowych z wysokim kontrastem
- [x] **Implementacja podstawowego UI**
  - Ekran główny z listą książek
  - Ekran dodawania nowej książki
  - Nawigacja między ekranami
- [X] **Integracja z Android Camera API**
  - Implementacja robienia zdjęć
  - Zapisywanie zdjęć w pamięci lokalnej
  - Grupowanie zdjęć w "książki"
- [ ] **Podstawowe zarządzanie danymi lokalnych**
  - Konfiguracja SQLite
  - Modele danych (Book, Page)
  - CRUD operacje dla książek

**Kryteria akceptacji:**

- Aplikacja uruchamia się bez błędów
- Można robić zdjęcia kolejnych stron
- Zdjęcia zapisują się lokalnie i są pogrupowane w książki
- Lista książek wyświetla się poprawnie

**Deliverable:** Funkcjonalna aplikacja z podstawowym skanowaniem

### 3.2 Sprint 2: Integracja API - Upload/Download (Tygodnie 4-5)

**Czas:** 2 tygodnie  
**Odpowiedzialny:** Senior Android Dev

**Zadania:**

- [ ] **Implementacja komunikacji z API OCR**
  - Konfiguracja Retrofit/OkHttp
  - Implementacja wysyłania zdjęć do API
  - Obsługa odpowiedzi i błędów API
  - Parsowanie rozpoznanego tekstu
- [ ] **Implementacja komunikacji z API TTS**
  - Wysyłanie tekstu do API TTS
  - Pobieranie wygenerowanych plików audio
  - Zapisywanie audio w pamięci lokalnej
- [ ] **Background Processing**
  - Implementacja WorkManager dla zadań w tle
  - Obsługa stanów przetwarzania (w toku, ukończone, błąd)
  - Synchronizacja stanu z UI
- [ ] **Obsługa błędów i retry logic**
  - Mechanizmy ponownych prób przy błędach sieciowych
  - Informowanie użytkownika o statusie przetwarzania

**Kryteria akceptacji:**

- Możliwe wysłanie zdjęcia do API OCR i otrzymanie tekstu
- Możliwe wysłanie tekstu do API TTS i otrzymanie pliku audio
- Background processing działa stabilnie
- Użytkownik otrzymuje feedback o statusie przetwarzania

**Deliverable:** Działająca integracja z zewnętrznymi API

### 3.3 Sprint 3: Przetwarzanie End-to-End (Tygodnie 6-7)

**Czas:** 2 tygodnie  
**Odpowiedzialny:** Cały zespół deweloperski

**Zadania:**

- [ ] **Implementacja pełnego pipeline'u przetwarzania**
  - Sekwencyjne przetwarzanie wszystkich stron książki
  - Zarządzanie kolejnością stron
  - Łączenie wyników w spójną całość
- [ ] **Optymalizacja przepływu danych**
  - Kompresja zdjęć przed wysyłką do API
  - Parallel processing (jeśli API na to pozwala)
  - Zarządzanie pamięcią podczas przetwarzania
- [ ] **Implementacja statusów książek**
  - Zeskanowana (tylko zdjęcia)
  - W trakcie przetwarzania
  - Gotowa do czytania
- [ ] **UI dla monitorowania postępu**
  - Progress bar dla przetwarzania
  - Informacje o statusie każdej książki
  - Możliwość anulowania przetwarzania

**Kryteria akceptacji:**

- Możliwe przetworzenie całej prostej książki (kilka stron)
- Pipeline działa od zdjęć do gotowego audio
- Użytkownik widzi postęp przetwarzania

**Deliverable:** Funkcjonalny system przetwarzania end-to-end

### 3.4 Sprint 4: Odtwarzanie Audio (Tygodnie 8-9)

**Czas:** 2 tygodnie  
**Odpowiedzialny:** Android Dev

**Zadania:**

- [ ] **Implementacja Playback Module**
  - Wybór między Android TTS a MediaPlayer dla lokalnych plików
  - Implementacja podstawowych kontrolek (Play/Pause/Stop)
  - Zarządzanie stanem odtwarzania
- [ ] **Implementacja nawigacji w treści**
  - Przejście do następnej/poprzedniej strony
  - Przejście do następnego/poprzedniego akapitu (jeśli możliwe)
  - Regulacja prędkości czytania
- [ ] **Zapamiętywanie pozycji czytania**
  - Zapisywanie ostatniej pozycji w bazie danych
  - Wznowienie od ostatniej pozycji
  - UI pokazujący postęp czytania
- [ ] **UI dla odtwarzania**
  - Duże, łatwo dostępne kontrolki
  - Wyświetlanie tytułu książki i aktualnej strony
  - Slider do nawigacji (jeśli odpowiedni dla dostępności)

**Kryteria akceptacji:**

- Przetworzone książki można odtwarzać od początku do końca
- Kontrolki Play/Pause/Stop działają poprawnie
- Aplikacja pamięta ostatnią pozycję czytania
- Nawigacja między stronami działa

**Deliverable:** Funkcjonalny system odtwarzania z podstawową nawigacją

### 3.5 Sprint 5: Zarządzanie Książkami (Tygodnie 10-11)

**Czas:** 2 tygodnie  
**Odpowiedzialny:** Android Dev + UI/UX Designer

**Zadania:**

- [ ] **Implementacja ekranu głównego**
  - Lista aktualnych książek (4-8 na ekranie)
  - Duże ikony/kafelki dla każdej książki
  - Dostęp do archiwum książek
- [ ] **Funkcje zarządzania**
  - Usuwanie książek
  - Archiwizacja/przywracanie książek
  - Edycja tytułów książek (podstawowa)
- [ ] **Wyświetlanie metadanych**
  - Okładka książki (pierwsze zdjęcie)
  - Tytuł (domyślnie "Książka X" lub rozpoznany)
  - Status (zeskanowana/w trakcie/gotowa)
  - Postęp czytania
- [ ] **Zarządzanie pamięcią**
  - Wyświetlanie zajętości pamięci
  - Możliwość usuwania różnych typów danych (zdjęcia/audio/tekst)

**Kryteria akceptacji:**

- Użytkownik może łatwo nawigować między książkami
- Możliwe usuwanie i archiwizowanie książek
- Wyświetlane są odpowiednie metadane każdej książki
- Funkcje zarządzania pamięcią działają

**Deliverable:** Kompletny system zarządzania biblioteką książek

### 3.6 Sprint 6: Stabilizacja MVP i Testy (Tygodnie 12-14)

**Czas:** 3 tygodnie  
**Odpowiedzialny:** Cały zespół

**Zadania:**

- [ ] **Optymalizacja wydajności**
  - Profiling aplikacji pod kątem zużycia pamięci
  - Optymalizacja zapytań do bazy danych
  - Optymalizacja UI dla responsywności
- [ ] **Obsługa błędów i edge cases**
  - Brak połączenia internetowego podczas przetwarzania
  - Przepełnienie pamięci
  - Błędy API (rate limiting, timeouts)
  - Przerwanie aplikacji podczas przetwarzania
- [ ] **Pierwsze testy z użytkownikami**
  - Rekrutacja 3-5 osób ze słabym wzrokiem
  - Sesje testowe przepływu end-to-end
  - Zbieranie feedbacku o UI/UX
  - Dokumentacja problemów i sugestii
- [ ] **Poprawki na podstawie testów**
  - Implementacja krytycznych poprawek UI/UX
  - Poprawki błędów wykrytych podczas testów
- [ ] **Finalizacja MVP**
  - Code review całego projektu
  - Testowanie na różnych urządzeniach Android
  - Przygotowanie dokumentacji użytkownika

**Kryteria akceptacji MVP:**

- Możliwe zeskanowanie i odczytanie pełnej książki beletrystycznej
- Aplikacja działa stabilnie offline (skanowanie, czytanie)
- UI jest użyteczne dla osób ze słabym wzrokiem
- Podstawowe funkcje zarządzania książkami działają
- Aplikacja przechodzi testy z rzeczywistymi użytkownikami

**Deliverable:** Gotowe MVP spełniające wszystkie kryteria akceptacji

## 4. Faza 2 - Rozszerzenia (Tygodnie 15-24)

### 4.1 Sprint 7: Jakość i Walidacja (Tygodnie 15-16)

**Czas:** 2 tygodnie  
**Odpowiedzialny:** Senior Android Dev

**Zadania:**

- [ ] **Ocena jakości zdjęć**
  - Implementacja algorytmów oceny ostrości
  - Sprawdzanie oświetlenia i kontrastu
  - Walidacja kompletności strony w kadrze
  - UI z sugestiami poprawy jakości zdjęcia
- [ ] **Rozpoznawanie numerów stron**
  - Analiza pozycji numerów na stronie
  - Rozpoznawanie numerów przez OCR
  - Walidacja sekwencji stron
- [ ] **Weryfikacja ciągłości**
  - Wykrywanie pominiętych stron
  - UI informujący o potencjalnych brakach
  - Możliwość akceptacji przez użytkownika

**Deliverable:** System walidacji jakości i ciągłości

### 4.2 Sprint 8: Automatyzacja i Personalizacja (Tygodnie 17-18)

**Czas:** 2 tygodnie  
**Odpowiedzialny:** Android Dev

**Zadania:**

- [ ] **Automatyczne przetwarzanie**
  - Detekcja połączenia z internetem
  - Automatyczne uruchamianie przetwarzania nowych stron
  - Ustawienia preferencji użytkownika
- [ ] **Wybór głosu lektora**
  - Integracja z dostępnymi głosami TTS
  - UI wyboru głosu z próbkami
  - Zapisywanie preferencji
- [ ] **Tryb wysokiego kontrastu**
  - Implementacja alternatywnych schematów kolorów
  - Przełączanie między trybami
  - Zwiększone rozmiary tekstów

**Deliverable:** Ulepszone funkcje automatyzacji i personalizacji

### 4.3 Sprint 9: Zarządzanie Pamięci i UX (Tygodnie 19-20)

**Czas:** 2 tygodnie  
**Odpowiedzialny:** Android Dev + UI/UX Designer

**Zadania:**

- [ ] **Szczegółowe zarządzanie pamięcią**
  - Ekran analizy zajętości (breakdown per książka i typ danych)
  - Selektywne usuwanie danych (tylko zdjęcia, tylko audio, itp.)
  - Ostrzeżenia o niskiej pamięci
- [ ] **Poprawa UX na podstawie testów**
  - Implementacja sugestii z pierwszych testów
  - Głosowe potwierdzenia wyboru (opcjonalne)
  - Proste wizualizacje czytania (pulsująca kropka)
- [ ] **Wsparcie dla gestów**
  - Podstawowe gesty nawigacyjne
  - Integracja z systemowymi gestami Android
  - Możliwość wyłączenia dla użytkowników którzy nie chcą

**Deliverable:** Ulepszone zarządzanie pamięcią i UX

### 4.4 Sprint 10: Testy i Stabilizacja Fazy 2 (Tygodnie 21-24)

**Czas:** 4 tygodnie  
**Odpowiedzialny:** Cały zespół

**Zadania:**

- [ ] **Drugie testy z użytkownikami**
  - Testy z większą grupą użytkowników (5-8 osób)
  - Fokus na nowych funkcjach
  - Testy długoterminowego użytkowania
- [ ] **Optymalizacje wydajnościowe**
  - Profilowanie i optymalizacja zużycia baterii
  - Optymalizacja operacji na plikach
  - Caching strategii dla lepszej responsywności
- [ ] **Stabilizacja i bug fixing**
  - Naprawy błędów znalezionych w testach
  - Testy regresji
  - Dokumentacja zmian i ulepszeń

**Deliverable:** Stabilna wersja z funkcjami NICE TO HAVE

## 5. Faza 3 - Funkcje Zaawansowane (Tygodnie 25-36)

### 5.1 Sprint 11-12: Integracja LLM (Tygodnie 25-28)

**Czas:** 4 tygodnie  
**Odpowiedzialny:** Senior Android Dev + Tech Lead

**Zadania:**

- [ ] **Rozpoznawanie tytułów z okładek**
  - Integracja z multimodalnym LLM API
  - Analiza okładek książek
  - Ekstrakcja tytułów i autorów
  - Fallback do domyślnych nazw
- [ ] **Analiza struktury tekstu**
  - Rozpoznawanie rozdziałów
  - Identyfikacja różnych sekcji
  - Poprawka błędów OCR przez LLM
  - Ustrukturyzowanie nawigacji
- [ ] **Rozpoznawanie typu treści**
  - Identyfikacja nagłówków, punktów, przypisów
  - Różnicowanie stylu czytania dla różnych typów
  - UI pokazujący strukturę dokumentu

**Deliverable:** System inteligentnej analizy treści

### 5.2 Sprint 13-14: Finalizacja i Publikacja (Tygodnie 29-36)

**Czas:** 8 tygodni  
**Odpowiedzialny:** Cały zespół

**Zadania:**

- [ ] **Przygotowanie do publikacji**
  - Tworzenie kont Google Play Developer
  - Przygotowanie opisów i grafik promocyjnych
  - Testowanie na szerokim spektrum urządzeń
- [ ] **Dokumentacja końcowa**
  - Instrukcja obsługi dla użytkowników
  - Dokumentacja techniczna
  - Przewodnik rozwiązywania problemów
- [ ] **Finalne testy**
  - Beta testing z większą grupą
  - Testy bezpieczeństwa i prywatności
  - Performance testing
- [ ] **Publikacja i wsparcie**
  - Publikacja w Google Play Store
  - Monitoring pierwszych instalacji
  - Szybkie reagowanie na feedback użytkowników

**Deliverable:** Aplikacja dostępna w Google Play Store

## 6. Zarządzanie Ryzykiem

### 6.1 Ryzyka Wysokie i Mitigation

| Ryzyko | Plan Mitigation | Odpowiedzialny |
|--------|-----------------|----------------|
| **Niska jakość zdjęć użytkowników** | Wczesne testy z użytkownikami, implementacja oceny jakości w Sprint 7, szczegółowe instrukcje | Cały zespół |
| **Wysokie koszty API** | Monitoring kosztów od Sprint 2, budżet na API, optymalizacja zapytań | Tech Lead |
| **UI nieodpowiednie dla słabowidzących** | Testy z użytkownikami od Sprint 6, konsultacje z ekspertami, iteracyjne poprawki | UI/UX Designer |

### 6.2 Monitoring i Kontrola

- **Tygodniowe standupy** zespołu deweloperskiego
- **Bi-weekly review** postępów z klientem
- **Miesięczne przeglądy** kosztów API i budżetu
- **Ciągły monitoring** jakości kodu i wydajności

## 7. Kryteria Sukcesu

### 7.1 MVP (Koniec Fazy 1)

- [ ] Zeskanowanie i odtwarzanie pełnej książki beletrystycznej
- [ ] Stabilna praca offline (skanowanie + czytanie)
- [ ] UI użyteczne dla osób ze słabym wzrokiem
- [ ] Pozytywny feedback od testowych użytkowników
- [ ] Błędy krytyczne <= 2 na wersję

### 7.2 Wersja Finalna (Koniec Fazy 3)

- [ ] Wszystkie funkcje NICE TO HAVE zaimplementowane
- [ ] Publikacja w Google Play Store
- [ ] Rating >= 4.0/5.0 w ciągu pierwszego miesiąca
- [ ] Zużycie pamięci <= 100MB dla typowej książki
- [ ] Czas przetwarzania strony <= czas czytania strony

## 8. Komunikacja i Raportowanie

### 8.1 Harmonogram Spotkań

- **Daily standups:** Codziennie o 9:00 (zespół deweloperski)
- **Sprint reviews:** Koniec każdego sprintu (cały zespół + klient)
- **User testing sessions:** Co 3-4 tygodnie
- **Technical reviews:** Co 2 tygodnie (architektura, bezpieczeństwo)

### 8.2 Dokumentacja Postępów

- **Sprint reports:** Po każdym sprincie
- **User testing reports:** Po każdej sesji testowej
- **Risk assessment updates:** Miesięcznie
- **Budget tracking reports:** Miesięcznie

## 9. Zasoby i Budżet

### 9.1 Zespół

- **Android Senior Dev:** 1.0 FTE przez 36 tygodni
- **Android Dev:** 1.0 FTE przez 28 tygodni (od tygodnia 2)
- **UI/UX Designer:** 0.5 FTE przez 20 tygodni
- **Tech Lead:** 0.3 FTE przez 36 tygodni
- **Test Coordinator:** 0.2 FTE przez 36 tygodni

### 9.2 Infrastruktura i Narzędzia

- **API Costs:** $200-500/miesiąc (zmienne, zależne od użycia)
- **Google Play Developer:** $25 jednorazowo
- **Development tools:** Android Studio (darmowe), pozostałe narzędzia (do 100$/miesiąc)
- **Testing devices:** Budżet na różne urządzenia Android (500-1000$)

## 10. Następne Kroki

### Natychmiastowe (Tydzień 0)

1. [ ] **Ustalenie zespołu** - potwierdzenie dostępności osób
2. [ ] **Konfiguracja środowiska** - repozytoria, narzędzia deweloperskie  
3. [ ] **Kick-off meeting** - omówienie planu z zespołem
4. [ ] **Pierwsza analiza API** - rejestracja w serwisach, podstawowe testy

### Do końca tygodnia 1

1. [ ] **Wybór konkretnych API** (OCR + TTS)
2. [ ] **Prototyp architektury** - szczegółowy diagram
3. [ ] **UI mockups** - pierwsze szkice głównych ekranów
4. [ ] **Plan testów z użytkownikami** - rekrutacja pierwszej grupy

---

**Plan przygotował:** [Nazwa]  
**Data:** [Data]  
**Wersja:** 1.0  
**Status:** Do akceptacji przez klienta
