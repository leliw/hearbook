# Analiza Techniczna i Architektura Systemu

## Projekt: Lektor Książek dla Słabowidzących

## 1. Executive Summary

### Rekomendowana architektura (w skrócie)

Architektura typu **Client-Side Application with Cloud Processing**. Główna logika aplikacji rezyduje na urządzeniu mobilnym (Android), odpowiadając za interfejs użytkownika, skanowanie offline, zarządzanie lokalnymi danymi oraz odtwarzanie audio offline. Ciężkie operacje przetwarzania (OCR, konwersja na mowę, analiza struktury) realizowane są przez **zewnętrzne usługi chmurowe (API)** dostępne online, do których aplikacja wysyła dane (zdjęcia) i odbiera wyniki (tekst, audio).

### Główne technologie

* **Platforma:** Android OS
* **Język programowania:** Kotlin (preferowany) lub Java
* **UI Framework:** Android SDK (z naciskiem na Accessibility Features)
* **Lokalna Baza Danych:** SQLite (dla metadanych i struktury książek) oraz system plików (dla zdjęć, tekstu, audio)
* **Integracje zewnętrzne (online):** API dla OCR (np. Google Cloud Vision AI, AWS Textract, Azure Cognitive Services Computer Vision), API dla TTS (np. Google Cloud Text-to-Speech, AWS Polly, Azure Cognitive Services Speech), opcjonalnie API LLM (np. OpenAI API, Google AI Platform).

### Szacowany czas realizacji

MVP (Minimalna Wersja Użyteczna): **10-14 tygodni**
Pełna wersja (z częścią funkcji NICE TO HAVE): **~6-9 miesięcy** (w tym iteracyjne testy z użytkownikami)

### Zidentyfikowane ryzyka (top 3)

1. **Jakość danych wejściowych (zdjęć):** Niska jakość zdjęć wykonanych przez użytkownika (oświetlenie, ostrość, kąt) bezpośrednio wpłynie na dokładność OCR i TTS.
2. **Zależność i koszt zewnętrznych API:** Stabilność, dostępność, dokładność i koszty płatnych usług OCR/TTS/LLM. Nagłe zmiany w cennikach lub polityce dostawców mogą wpłynąć na projekt.
3. **Dopasowanie UI/UX do grupy docelowej:** Stworzenie interfejsu *naprawdę* intuicyjnego i użytecznego dla osób ze znacznymi problemami wzrokowymi wymaga wielu iteracji i testów z rzeczywistymi użytkownikami.

## 2. Analiza Wymagań Funkcjonalnych

### 2.1 Funkcje podstawowe - analiza złożoności

| Funkcja                     | Złożoność | Czas est. (dla MVP) | Zależności                 | Uwagi                                                                 |
|-----------------------------|-----------|---------------------|----------------------------|-----------------------------------------------------------------------|
| Skanowanie offline          | Niska     | 0.5 tyg             | Android Camera API         | Wykorzystanie standardowych możliwości urządzenia.                    |
| Tworzenie "książki"         | Niska     | 0.5 tyg             | Skanowanie                 | Proste grupowanie plików/danych w pamięci lokalnej.                   |
| Przetwarzanie (online)      | Wysoka    | 3-4 tyg             | Zeskanowane strony, API OCR/TTS | Wymaga obsługi asynchroniczności, błędów API, zarządzania danymi online. |
| Czytanie na głos (offline)  | Średnia   | 1.5-2 tyg           | Przetworzone audio/tekst   | Wykorzystanie Android TTS lub lokalnych plików audio. Zarządzanie stanem. |
| Podstawowa nawigacja czytania | Średnia   | 1 tyg               | Czytanie na głos           | Integracja z kontrolkami odtwarzania.                                |
| Regulacja prędkości czytania | Niska     | 0.5 tyg             | Czytanie na głos           | Standardowa opcja Android TTS.                                        |
| Podstawowe zarządzanie książkami | Średnia   | 1 tyg               | Tworzenie/Przetwarzanie    | Lista w UI, dostęp do archiwum/szczegółów (tytuł, okładka).           |
| Prosty interfejs dla słabowidzących | Średnia   | 2-3 tyg             | Wszystkie funkcje UI      | Kluczowe z perspektywy UX, ale technicznie wymaga standardowych narzędzi + dostosowania. |
| MVP                         | -         | 10-14 tyg           | Wszystkie powyższe         | Integracja i stabilizacja core loop.                                  |

*Czas estymowany dla MVP, w sumie zawiera integrację, testy i poprawki. Złożoność "Wysoka" oznacza znaczący nakład pracy deweloperskiej.*

### 2.2 Krytyczne ścieżki w systemie

1. **Użytkownik skanuje książkę offline:** Robienie zdjęć kolejnych stron, zapisywanie ich lokalnie.
2. **Użytkownik triggeruje przetwarzanie online:** Aplikacja wysyła zeskanowane zdjęcia do zewnętrznych API (OCR, TTS), odbiera wyniki i zapisuje je lokalnie.
3. **Użytkownik czyta książkę offline:** Wybiera przetworzoną książkę, aplikacja odtwarza audio, użytkownik steruje odtwarzaniem (Play/Pause, nawigacja), aplikacja zapamiętuje postęp.

### 2.3 Potencjalne wąskie gardła

* **Przetwarzanie online:** Czas potrzebny na wysłanie zdjęć, przetwarzanie przez API i odebranie danych. Zależny od jakości połączenia internetowego, obciążenia API i złożoności strony.
* **Pamięć lokalna urządzenia:** Skanowane zdjęcia i wygenerowane audio/tekst dla całych książek mogą zajmować znaczną ilość pamięci.
* **Wydajność odtwarzania audio:** Na starszych/słabszych urządzeniach odtwarzanie dużych plików audio lub dynamiczna synteza mowy przez Android TTS może być obciążająca.

## 3. Analiza Wymagań Niefunkcjonalnych

### 3.1 Wydajność

- Oczekiwana liczba użytkowników: Aplikacja jednouserowa na urządzeniu. Skalowalność dotyczy liczby książek/danych na urządzeniu i potencjalnie skali użycia API (co wpływa na koszty, nie na wydajność *per user*).
* Wielkość danych:
  * Zdjęcia: ~0.5 - 2 MB/strona (zależnie od rozdzielczości). Książka 300 stron: 150-600 MB.
  * Rozpoznany tekst: Znikome (kilkaset KB/MB na książkę).
  * Audio: ~1-5 MB/minutę (zależnie od formatu/jakości). Książka 300 stron czytana ~5-7h: 300-2100 MB (0.3 - 2.1 GB).
  * Całość na książkę: ~0.5 GB do 3 GB. Kilka książek może zająć kilkanaście GB.
* Wymagania czasowe:
  * Skanowanie: Natychmiastowe robienie zdjęć.
  * Odtwarzanie audio: Płynne, bez przerw w trybie offline.
  * Przetwarzanie: Cel - czas przetwarzania strony krótszy niż czas jej odczytania. Preferowane przetwarzanie w tle.

### 3.2 Bezpieczeństwo

- Poziom wymaganego bezpieczeństwa: Dane wrażliwe (treść książki) są przechowywane lokalnie na urządzeniu użytkownika. Brak wymogu zaawansowanego szyfrowania na poziomie pliku/bazy danych w MVP.
* Typy danych wrażliwych: Treść skanowanych książek, dane użytkownika (nie zbieramy ich poza lokalnym urządzeniem).
* Wymagania compliance: RODO - w przypadku integracji z zewnętrznymi API należy upewnić się, że przetwarzają one dane zgodnie z przepisami (szczególnie jeśli użytkownik skanuje książki zawierające dane osobowe). API powinny być zgodne z polityką prywatności (np. nie wykorzystywać treści do uczenia modeli bez zgody).

### 3.3 Skalowalność

- Przewidywany wzrost: Wzrost ilości danych (liczby zeskanowanych książek) na *jednym urządzeniu*. Liczba użytkowników aplikacji (choć nie wpływa na architekturę per-device, wpływa na koszty API).
* Strategie skalowania:
  * **Horyzontalne (dane):** Wymaga zarządzania pamięcią na urządzeniu (usuwanie starych książek/danych).
  * **Wertykalne (przetwarzanie):** Zależne od wydajności i limitów zewnętrznych API. Zazwyczaj API chmurowe skalują się automatycznie.

## 4. Rekomendowany Stos Technologiczny

### 4.1 Architektura ogólna

**Wybrana architektura:** Client-Side Application with Cloud Processing (Aplikacja mobilna z przetwarzaniem w chmurze)
**Uzasadnienie:** Ta architektura jest najlepiej dopasowana do wymagań projektowych:

* **Offline Capability:** Kluczowe funkcje (skanowanie, czytanie) działają offline, co jest możliwe dzięki umieszczeniu logiki UI, kamery, lokalnego storage i odtwarzacza audio w aplikacji klienckiej.
* **Heavy Processing Offloading:** Obciążające obliczeniowo zadania (OCR, zaawansowana synteza mowy, analiza LLM) są przeniesione do zewnętrznych usług w chmurze, wykorzystujących potężną infrastrukturę. Pozwala to utrzymać aplikację na urządzeniu stosunkowo lekką i responsywną.
* **Accessibility Focus:** Natywna aplikacja mobilna (Android) pozwala na pełne wykorzystanie wbudowanych funkcji dostępności systemu operacyjnego i tworzenie niestandardowego interfejsu (duże elementy, gesty) w sposób najbardziej efektywny.

### 4.2 Frontend

**Technologia:** Kotlin + Android SDK
**Uzasadnienie:** Kotlin to nowoczesny, bezpieczniejszy i bardziej produktywny język dla rozwoju na Androida niż Java. Pełne wsparcie Google, duża społeczność i interoperacyjność z Javą. Android SDK zapewnia natywny dostęp do wszystkich potrzebnych komponentów systemowych (Kamera, Storage, Accessibility Services, TTS Engine). Pozwala to na tworzenie interfejsu w pełni dostosowanego do potrzeb grupy docelowej.
**Dokumentacja:** [https://developer.android.com/docs](https://developer.android.com/docs), [https://kotlinlang.org/docs/](https://kotlinlang.org/docs/)
**Alternatywy:**

* Java + Android SDK: Również opcja, ale Kotlin jest bardziej rekomendowany dla nowych projektów.
* Frameworki cross-platformowe (Flutter, React Native): Mogą przyspieszyć rozwój UI, ale integracja z niskopoziomowymi API Androida (Kamera, Storage, Accessibility) i specyficzne dostosowania UI/UX dla słabowidzących mogą być bardziej skomplikowane lub wymagać natywnych modułów, co niweczy część zalet cross-platformowości. Dla projektu z tak silnym naciskiem na Accessibility i integrację z natywnymi funkcjami, podejście natywne (Kotlin) wydaje się bezpieczniejsze.

### 4.3 Backend

**Technologia:** Zewnętrzne API usług chmurowych (OCR, TTS, opcjonalnie LLM)
**Framework:** Brak własnego backend frameworku po stronie aplikacji. Komunikacja z API będzie realizowana przy użyciu standardowych bibliotek sieciowych dla Androida (np. Retrofit z OkHttp).
**Uzasadnienie:** Outsourcing ciężkich obliczeniowo zadań do wyspecjalizowanych usług chmurowych pozwala skupić się na rozwoju aplikacji klienckiej i wykorzystać najnowsze osiągnięcia w dziedzinie AI/ML bez konieczności budowania i utrzymywania własnej infrastruktury do tych celów. Wybór konkretnego dostawcy API (Google, AWS, Azure, inni) będzie kluczowy i powinien zależeć od dokładności, wydajności, kosztów i łatwości integracji.
**Dokumentacja:**

* Przykładowe API:
  * Google Cloud Vision AI (OCR): [https://cloud.google.com/vision/docs](https://cloud.google.com/vision/docs)
  * Google Cloud Text-to-Speech: [https://cloud.google.com/text-to-speech/docs](https://cloud.google.com/text-to-speech/docs)
  * AWS Textract (OCR): [https://aws.amazon.com/textract/](https://aws.amazon.com/textract/)
  * AWS Polly (TTS): [https://aws.amazon.com/polly/](https://aws.amazon.com/polly/)
  * Azure Cognitive Services (Vision, Speech): [https://azure.microsoft.com/en-us/services/cognitive-services/](https://azure.microsoft.com/en-us/services/cognitive-services/)

### 4.4 Baza danych

**Główna baza:** SQLite
**Cache:** Nie dotyczy w tym kontekście (dane są pobierane jednorazowo i przechowywane lokalnie).
**Uzasadnienie:** SQLite jest standardową, lekką bazą danych na Androidzie, idealną do przechowywania ustrukturyzowanych metadanych: listy książek, ich tytuły, okładki, statusy (zeskanowana, przetworzona, archiwalna), listę stron w każdej książce, numery stron (jeśli rozpoznane), postęp czytania (ostatnia pozycja). Duże pliki (zdjęcia, audio, tekst) będą przechowywane bezpośrednio w systemie plików, a ścieżki do nich będą zapisane w SQLite. To proste i efektywne rozwiązanie dla lokalnego zarządzania danymi.
**Dokumentacja:**

* SQLite na Androidzie: [https://developer.android.com/training/data-storage/sqlite](https://developer.android.com/training/data-storage/sqlite)
* Oficjalna dokumentacja SQLite: [https://www.sqlite.org/docs.html](https://www.sqlite.org/docs.html)

### 4.5 Infrastruktura i hosting

**Platforma:** Urządzenia użytkowników końcowych (Android smartphones/tablets). Przetwarzanie online na infrastrukturze dostawców API (np. Google Cloud, AWS, Azure).
**Uzasadnienie:** Aplikacja instalowana jest bezpośrednio na urządzeniu użytkownika. Koszty infrastruktury po stronie dostawców API są zmienne i zależą od wolumenu użycia (liczby skanowanych/przetwarzanych stron przez wszystkich użytkowników).
**Dokumentacja:** Dokumentacja wybranych API (patrz sekcja 4.3) będzie zawierać informacje o ich infrastrukturze i wydajności.
**Szacowane koszty:** Głównie koszty API (OCR, TTS). Trudne do oszacowania bez wiedzy o przewidywanej liczbie użytkowników i ich aktywności (liczbie skanowanych książek/stron). Przykładowo, 1000 stron OCR + TTS może kosztować od kilku do kilkunastu USD/EUR w zależności od dostawcy i wybranych opcji (np. standard vs. premium głos TTS). Przy dużej skali może to być znaczący koszt operacyjny.

## 5. Architektura Systemu

### 5.1 Diagram architektury

```
+--------------------------+          +--------------------------+
|  ANDROID DEVICE          |          |  EXTERNAL CLOUD SERVICES |
| +----------------------+ |          | +----------------------+ |
| | User Interface (UI)  | |          | |      OCR API         | |
| | - Large Elements     | |          | |                      | |
| | - Accessibility Feat | |          | +----------------------+ |
| +----------------------+ |          | +----------------------+ |
| | Camera Module        | |<------->| |      TTS API         | |
| +----------------------+ |   HTTPS  | |                      | |
| | Local Storage Mgmt   | |   (Online| +----------------------+ |
| | (SQLite + Files)     | |    Data  | +----------------------+ |
| +----------------------+ |    for   | |      LLM API         | |
| | Playback Module      | |   Process| |    (Optional)        | |
| | (Android TTS/MediaP) | |          | +----------------------+ |
| +----------------------+ |          |                          |
| | Background Process.  | |          |                          |
| | - Upload/Download    | |          |                          |
| | - API Communication  | |          |                          |
| +----------------------+ |          |                          |
+--------------------------+          +--------------------------+

(Offline operations within the Android Device boundary)
(Online operations involve communication over HTTPS to External Services)
```

### 5.2 Główne komponenty

| Komponent             | Odpowiedzialność                                                                 | Technologia           | Interfejsy               | Dokumentacja                               |
|-----------------------|----------------------------------------------------------------------------------|-----------------------|--------------------------|--------------------------------------------|
| **User Interface**    | Wyświetlanie danych, obsługa interakcji użytkownika, nawigacja. Dostosowanie UI.   | Kotlin + Android SDK  | UI Events, Android APIs  | developer.android.com/docs                 |
| **Camera Module**     | Robienie zdjęć stron, zapisywanie ich do pamięci lokalnej.                       | Android Camera API    | System Camera API        | developer.android.com/docs/reference/android/hardware/camera2 |
| **Local Storage Mgmt**| Zapis/odczyt zdjęć, tekstu, audio i metadanych (SQLite, system plików).          | SQLite, Java.io/Kotlin.io | File System, SQLite DB API | developer.android.com/training/data-storage |
| **Playback Module**   | Odtwarzanie audio (syntetyzowanej mowy), kontrola (Play/Pause/Stop, nawigacja). | Android TTS, MediaPlayer API | Audio APIs               | developer.android.com/docs/reference/android/speech/tts/TextToSpeech, developer.android.com/guide/topics/media/mediaplayer |
| **Background Process.**| Zarządzanie komunikacją z API (wysyłanie zdjęć, odbiór danych), obsługa statusów.| Kotlin Coroutines/WorkManager, Retrofit | REST API Calls (HTTPS) | developer.android.com/topic/libraries/architecture/workmanager, squareup.github.io/retrofit/ |
| **OCR API (External)**| Optyczne rozpoznawanie znaków na obrazach, zwracanie tekstu.                     | Usługa chmurowa       | REST API                 | Docs dostawcy API (np. Google, AWS, Azure) |
| **TTS API (External)**| Konwersja tekstu na pliki audio lub strumienie audio.                            | Usługa chmurowa       | REST API                 | Docs dostawcy API                          |
| **LLM API (Optional)**| Analiza okładek, struktury tekstu, poprawa OCR.                                  | Usługa chmurowa       | REST API                 | Docs dostawcy API                          |

### 5.3 Przepływ danych

1. **Skanowanie:** Użytkownik w UI wybiera "Nowa Książka". UI aktywuje Camera Module. User robi zdjęcia stron. Camera Module zapisuje zdjęcia do Local Storage (system plików). UI aktualizuje listę zeskanowanych stron dla bieżącej książki w Local Storage (SQLite). *Całość offline.*
2. **Przetwarzanie:** Użytkownik w UI wybiera "Przetwarzaj" lub Background Processor wykrywa online i nowe strony. Background Processor czyta zdjęcia z Local Storage, wysyła je (przez sieć) do OCR API. OCR API zwraca tekst. Background Processor wysyła tekst do TTS API. TTS API zwraca pliki audio (lub linki/strumienie). Background Processor zapisuje tekst i audio do Local Storage (system plików). Aktualizuje status książki w Local Storage (SQLite) na "Gotowa do czytania".
3. **Czytanie:** Użytkownik w UI wybiera "Czytaj". UI czyta metadane (w tym postęp czytania) z Local Storage (SQLite). UI aktywuje Playback Module, wskazując pliki audio do odtwarzania z Local Storage. Playback Module odtwarza audio. UI wyświetla kontrolki nawigacji/odtwarzania. Po pauzie/stopie UI/Playback Module zapisuje ostatnią pozycję czytania w Local Storage (SQLite). *Całość offline.*

### 5.4 Bezpieczeństwo

- Uwierzytelnianie: Brak wymaganego uwierzytelniania użytkowników końcowych w aplikacji. API mogą wymagać uwierzytelniania (np. API Keys), które powinny być zarządzane bezpiecznie w aplikacji (np. nie hardcodowane, użycie zmiennych środowiskowych/systemu zarządzania kluczami na platformie produkcyjnej API, choć w przypadku aplikacji klienckiej na Androida jest to wyzwanie - często API Keys są osadzane w kodzie/konfiguracji aplikacji, co stanowi ryzyko ich wycieku).
* Autoryzacja: Brak wymaganego modelu autoryzacji użytkowników w aplikacji. Dostęp do danych lokalnych jest zarządzany przez uprawnienia systemu Android. API używają własnych modeli autoryzacji dostępu do usług.
* Szyfrowanie: Dane wrażliwe (treść książki) są przechowywane w pamięci lokalnej urządzenia. Szyfrowanie "at rest" nie jest wymagane w MVP. Dane przesyłane do/z API powinny być przesyłane szyfrowanym protokołem (HTTPS jest standardem dla REST API).
* Backup: Brak wbudowanej strategii backupu danych książek poza urządzenie w MVP. Backup może być opcjonalnie realizowany przez standardowe funkcje backupu systemu Android (jeśli aplikacja to wspiera).

## 6. Plan Implementacji

### 6.1 Fazy rozwoju

**Faza 1 - MVP (10-14 tygodni):**
* Implementacja podstawowego UI z dużymi elementami (Ekran Główny, Ekran Skanowania, Ekran Czytania).
* Integracja z Android Camera API i Local Storage do zapisywania zdjęć i grupowania w "książki".
* Implementacja Background Processor do komunikacji z wybranymi API OCR i TTS.
* Integracja z API OCR: wysyłanie zdjęć, odbiór tekstu.
* Integracja z API TTS: wysyłanie tekstu, odbiór plików audio.
* Zapisywanie tekstu i audio w Local Storage.
* Implementacja Playback Module (wykorzystanie Android TTS lub MediaPlayer dla lokalnych plików audio).
* Podstawowa nawigacja (Play/Pause/Stop, Następna/Poprzednia Strona).
* Zapamiętywanie ostatniego miejsca czytania (na poziomie strony).
* Podstawowe zarządzanie książkami (lista aktualnych, dostęp do archiwum, usuwanie).
* Pierwsza runda testów użyteczności z docelowymi użytkownikami (minimum 2-3 osoby).

**Faza 2 - Rozszerzenie (6-10 tygodni po MVP):**
* Implementacja funkcji NICE TO HAVE:
  * Rozpoznawanie numerów stron i weryfikacja sekwencji.
  * Wstępna ocena jakości zdjęcia.
  * Opcja automatycznego przetwarzania po połączeniu online.
  * Wybór głosu lektora (jeśli API lub Android TTS na to pozwala).
  * Wysoki kontrast interfejsu.
  * Szczegółowe zarządzanie pamięcią (ekran zajętości, usuwanie danych).
* Poprawa UI/UX na podstawie testów z Fazy 1.
* Usprawnienie zarządzania danymi lokalnymi.

**Faza 3 - Zaawansowane funkcje (8-12 tygodni po Fazie 2):**
* Integracja z LLM API (opcjonalnie) do:
  * Rozpoznawania tytułu z okładki.
  * Rozpoznawania typu treści i prostej struktury (rozdziały, punkty).
* Potencjalna implementacja prostych wizualizacji czytania.
* Wsparcia dla gestów nawigacyjnych.
* Możliwość ręcznej edycji tytułu książki.
* Dalsze optymalizacje wydajnościowe i zarządzania pamięcią.
* Rozważenie wsparcia dla TalkBack lub innych zaawansowanych funkcji dostępności (jeśli wczesne testy z użytkownikami wykażą taką potrzebę i wykonalność).
* Przygotowanie do publikacji w Google Play Store.

### 6.2 Kamienie milowe

| Data Est. | Milestone                       | Deliverable                                      |
|-----------|---------------------------------|--------------------------------------------------|
| Tydzień 2 | Bazowy UI i Skanowanie          | Aplikacja uruchamia się, można robić zdjęcia, zapisują się lokalnie. Książki na liście. |
| Tydzień 4 | Integracja API - Upload/Download | Możliwe wysłanie zdjęcia do API, odbiór surowej odpowiedzi (testowe). |
| Tydzień 7 | Procesowanie End-to-End (Proto) | Możliwe zeskanowanie 1 strony, wysłanie do OCR/TTS, odebranie audio, odtworzenie audio. |
| Tydzień 10| Core MVP Functionality          | Możliwe zeskanowanie całej prostej książki (kilka stron), przetworzenie, odtworzenie od początku do końca z kontrolami. |
| Tydzień 12| MVP Stabilizacja & Testy        | MVP gotowe do testów z użytkownikami. Pierwsze testy rozpoczęte. |
| Tydzień 14| MVP Delivered                   | MVP stabilne, spełniające minimalne kryteria akceptacji. |

## 7. Analiza Ryzyk

### 7.1 Ryzyka wysokie

| Ryzyko                                   | Prawdopodobieństwo | Wpływ | Mitigation                                                                 |
|------------------------------------------|-------------------|--------|----------------------------------------------------------------------------|
| Niska jakość zdjęć wejściowych           | Wysokie           | Wysoki | Wdrożenie funkcji oceny jakości zdjęcia (NICE TO HAVE). Wyraźne wskazówki w UI jak robić zdjęcia. Testy z różnymi warunkami oświetleniowymi. |
| Wysokie koszty użycia API                | Średnie           | Wysoki | Monitorowanie zużycia API. Wybór dostawców z konkurencyjnymi cenami. Optymalizacja procesu (np. kompresja zdjęć przed wysyłką). W budżecie przewidzieć koszty operacyjne. |
| Trudność w stworzeniu użytecznego UI/UX  | Wysokie           | Wysoki | Iteracyjne projektowanie i testowanie z docelową grupą użytkowników od samego początku. Zaangażowanie ekspertów od dostępności. |

### 7.2 Ryzyka średnie

| Ryzyko                                   | Prawdopodobieństwo | Wpływ | Mitigation                                                                 |
|------------------------------------------|-------------------|--------|----------------------------------------------------------------------------|
| Zależność od stabilności/dostępności API | Średnie           | Średni | Wybór renomowanych dostawców API (Google, AWS, Azure). Wdrożenie obsługi błędów i ponownych prób w komunikacji z API. Powiadomienia użytkownika o problemach. |
| Ograniczenia pamięci lokalnej urządzenia | Średnie           | Średni | Implementacja ekranu zarządzania pamięcią. Możliwość usuwania danych (zdjęć, audio) per książka lub typ danych. Wskazówki dla użytkownika. |
| Niska jakość syntetyzowanej mowy         | Średnie           | Średni | Wybór wysokiej jakości API TTS. Opcja wyboru głosu (jeśli dostępna). |
| Niska dokładność OCR dla skomplikowanych układów | Wysokie           | Średni | W MVP skupić się na prostych książkach beletrystycznych. W przyszłości rozważenie bardziej zaawansowanych API lub modeli (LLM) do analizy struktury. |

### 7.3 Plan zarządzania ryzykami

* **Regularne testy z użytkownikami:** Cykliczne sesje testowe z osobami ze słabym wzrokiem, począwszy od wczesnych etapów rozwoju, skupiające się na UI/UX i przepływie użytkownika.
* **Monitoring kosztów i wydajności API:** Aktywne śledzenie zużycia i kosztów API, testowanie alternatywnych dostawców, negocjowanie warunków przy większej skali (jeśli projekt urośnie).
* **Wdrożenie funkcji oceny jakości:** Priorytetowe potraktowanie funkcji oceny jakości zdjęcia, aby minimalizować problemy z OCR u źródła.
* **Solidna obsługa błędów:** Implementacja mechanizmów obsługi błędów sieciowych, błędów API, brakującej pamięci, z czytelnymi komunikatami dla użytkownika (potencjalnie głosowymi).
* **Iteracyjny rozwój:** Realizacja projektu w krótkich iteracjach, pozwalająca na szybkie reagowanie na wykryte problemy i zebrany feedback.

## 8. Szacunki i Zasoby

### 8.1 Szacowany czas realizacji

- **MVP:** 10-14 tygodni
* **Faza 2 (Rozszerzenie):** 6-10 tygodni
* **Faza 3 (Zaawansowane funkcje):** 8-12 tygodni
* **Całość do wersji z częścią NICE TO HAVE i stabilizacją:** ~6-9 miesięcy

### 8.2 Wymagane kompetencje

- **Programista Android (Kotlin):** Poziom Senior (doświadczenie w pracy z natywnymi API, asynchronicznością, zarządzaniem pamięcią, integracją z API REST). Wymagana min. 1 osoba, optymalnie 2 dla przyspieszenia.
* **Projektant UI/UX z doświadczeniem w Dostępności (Accessibility):** Poziom Mid/Senior (kluczowy do zaprojektowania interfejsu spełniającego specyficzne potrzeby). Wymagana min. 1 osoba (może być konsultant zewnętrzny).
* **Osoba do testów z użytkownikami:** Koordynator/tester posiadający kontakt z docelową grupą lub opiekunami, zdolny do przeprowadzenia i udokumentowania testów użyteczności.
* **Tech Lead/Architekt:** Doświadczenie w projektowaniu systemów, wyborze technologii, zarządzaniu ryzykiem. Wymagany nadzór architektoniczny (może być rola pełniona przez Senior Android Deva lub konsultanta).

### 8.3 Szacowane koszty

- Licencje: Brak kosztów licencyjnych dla podstawowego stosu (Kotlin, Android SDK, SQLite). Potencjalne koszty licencji na użycie płatnych API (OCR, TTS, LLM) - patrz koszty infrastruktury.
* Infrastruktura: Koszty zużycia zewnętrznych API. Zmienne, zależne od skali. Przy założeniu testów i wczesnego użycia przez kilku użytkowników: dziesiątki do setek USD/miesiąc. Przy skali komercyjnej/publicznej: potencjalnie setki do tysięcy USD/miesiąc, wymagające precyzyjnych szacunków w miarę rozwoju.
* Rozwój: Główny koszt. Man-hours zespołu deweloperskiego i UX/konsultantów. Przy zespole 2 developerów + 0.5 UX + Tech Lead nadzór, przez 6 miesięcy: znaczny koszt, zależny od stawek godzinowych/dziennych.

## 9. Rekomendacje i Następne Kroki

### 9.1 Kluczowe rekomendacje

1. **Skupić się na MVP:** Zrealizować podstawowy przepływ (skanowanie offline -> przetwarzanie online -> czytanie offline) jako pierwszy, stabilny krok.
2. **Priorytet dla UI/UX Dostępności:** Rozpocząć projektowanie UI/UX z silnym naciskiem na potrzeby słabowidzących i włączyć testy z użytkownikami od wczesnych faz.
3. **Wybór API:** Dokonać analizy i wyboru konkretnych dostawców API (OCR, TTS) na podstawie dokładności (dla różnych typów druku), wydajności i kosztów. Warto przetestować kilku.
4. **Zarządzanie pamięcią:** Zaplanować implementację podstawowego zarządzania pamięcią lokalną już w Fazie 1 lub 2, ponieważ duże pliki mogą szybko zapełnić urządzenie.
5. **Kotlin jako język wiodący:** Rekomendowane użycie Kotlin dla nowego kodu Android, zapewniając lepszą produktywność i bezpieczeństwo.

### 9.2 Następne kroki

1. Szczegółowe doprecyzowanie zakresu MVP i kryteriów akceptacji.
2. Wybór konkretnych dostawców API OCR i TTS (po krótkim badaniu/testach porównawczych).
3. Zaprojektowanie architektury modułów aplikacji (Camera, Storage, Network/Processing, Playback, UI) i ich interfejsów.
4. Wstępne prototypowanie kluczowych elementów UI/UX z testami na grupie docelowej.
5. Uruchomienie środowiska deweloperskiego i integracja z pierwszym API (np. OCR).

### 9.3 Decyzje wymagające konsultacji z klientem

- Wybór konkretnych dostawców płatnych API (koszt vs. jakość rozpoznawania/syntezy).
* Akceptowalny poziom niedokładności OCR/TTS w MVP (szczególnie dla nietypowych czcionek/układów).
* Priorytetyzacja funkcji NICE TO HAVE w Fazie 2 i 3.
* Decyzja o potencjalnym wykorzystaniu LLM (koszt vs. korzyści, np. lepsze rozpoznawanie struktury/tytułu).
* Strategia zarządzania kosztami API przy potencjalnym wzroście liczby użytkowników.

## 10. Załączniki i Zasoby

### 10.1 Linki do dokumentacji

**Główne technologie:**
* Kotlin: [https://kotlinlang.org/docs/](https://kotlinlang.org/docs/)
* Android Developer Documentation: [https://developer.android.com/docs](https://developer.android.com/docs)
* SQLite Official Documentation: [https://www.sqlite.org/docs.html](https://www.sqlite.org/docs.html)
* Android Storage Documentation: [https://developer.android.com/training/data-storage](https://developer.android.com/training/data-storage)
* Android Camera Documentation: [https://developer.android.com/training/camera](https://developer.android.com/training/camera)
* Android Text-to-Speech Documentation: [https://developer.android.com/reference/android/speech/tts/TextToSpeech](https://developer.android.com/reference/android/speech/tts/TextToSpeech)

**Przykładowe API:**
* Google Cloud Vision AI (OCR): [https://cloud.google.com/vision/docs](https://cloud.google.com/vision/docs)
* Google Cloud Text-to-Speech: [https://cloud.google.com/text-to-speech/docs](https://cloud.google.com/text-to-speech/docs)
* AWS Textract (OCR): [https://aws.amazon.com/textract/](https://aws.amazon.com/textract/)
* AWS Polly (TTS): [https://aws.amazon.com/polly/](https://aws.amazon.com/polly/)

**Narzędzia dodatkowe:**
* Retrofit (HTTP Client for Android): [https://square.github.io/retrofit/](https://square.github.io/retrofit/)
* Android Architecture Components (np. WorkManager for background tasks): [https://developer.android.com/topic/libraries/architecture](https://developer.android.com/topic/libraries/architecture)

**Tutoriale i przewodniki:**
* Oficjalne tutoriale Android (Kotlin): [https://developer.android.com/courses/android-basics-kotlin/overview](https://developer.android.com/courses/android-basics-kotlin/overview)
* Android Accessibility Documentation: [https://developer.android.com/guide/topics/ui/accessibility](https://developer.android.com/guide/topics/ui/accessibility)

### 10.2 Materiały referencyjne

- Zasady projektowania dla dostępności (General Accessibility Design Principles).
* Porównania jakości i kosztów różnych dostawców API OCR/TTS.
* Case studies aplikacji mobilnych z silnym naciskiem na dostępność.
* Benchmarki wydajnościowe przetwarzania obrazów i tekstu przez różne API.
