# Specyfikacja projektu - Lektor Książek dla Słabowidzących

Program ma być aplikacją na Androida, zaprojektowaną specjalnie dla osób ze słabym wzrokiem, umożliwiającą skanowanie stron książek i czytanie ich na głos. Kluczowe cechy to prosty interfejs, możliwość pracy offline w części procesu (skanowanie i odczyt), a także wykorzystanie nowoczesnych technologii (w tym potencjalnie LLM) do przetwarzania tekstu i rozpoznawania elementów książki.

## 1. Przegląd Projektu
### Cel biznesowy
Umożliwienie osobom ze znacznymi problemami ze wzrokiem lub trudnościami w czytaniu fizycznych książek, powrotu do czerpania radości z literatury poprzez konwersję tekstu drukowanego na mowę syntetyczną.

### Problem do rozwiązania
Problemy z czytaniem tradycyjnego druku ze względu na wady wzroku. Brak łatwego w obsłudze narzędzia do samodzielnego skanowania i odsłuchiwania treści książek w formie audio, dostosowanego do specyficznych potrzeb użytkowników.

### Grupa docelowa
Główny użytkownik: Osoba dorosła ze znacznymi problemami ze wzrokiem, potencjalnie z ograniczoną biegłością techniczną, potrzebująca dużego i prostego interfejsu.
Wtórny użytkownik (opcjonalnie): Opiekun/członek rodziny pomagający w konfiguracji, zarządzaniu książkami czy rozwiązywaniu problemów.

## 2. Wymagania Funkcjonalne
### Funkcje podstawowe (MUST HAVE)
*   **Skanowanie offline:** Możliwość robienia zdjęć kolejnych stron książki za pomocą aparatu urządzenia z systemem Android, bez konieczności połączenia z internetem.
*   **Tworzenie "książki":** Grupowanie zeskanowanych zdjęć stron w ramach logicznej całości ("książki") w aplikacji.
*   **Przetwarzanie (online):** Wykonanie optycznego rozpoznawania znaków (OCR) na zdjęciach stron i konwersja rozpoznanego tekstu na dane audio lub tekstowe gotowe do syntezy mowy. Przetwarzanie wymaga połączenia z internetem.
*   **Czytanie na głos (offline):** Możliwość odczytania przetworzonej książki przez syntezator mowy bez konieczności połączenia z internetem. Program musi zapamiętywać ostatnie miejsce czytania i umożliwiać wznowienie.
*   **Podstawowa nawigacja czytania:** Kontrola nad odtwarzaniem (Play/Pause/Stop). Nawigacja do przodu/wstecz (min. zdanie, akapit, strona).
*   **Regulacja prędkości czytania:** Możliwość dostosowania tempa mowy lektora.
*   **Podstawowe zarządzanie książkami:** Wyświetlanie listy "aktualnych" książek (limitowanej do 4-8 ikon na ekranie głównym) oraz dostępu do "archiwum".
*   **Prosty interfejs dla słabowidzących:** Duże ikony (4-8 na ekranie), duże obszary do kliknięcia/dotknięcia.
*   **MVP:** Możliwość zeskanowania i odczytania od początku do końca co najmniej jednej standardowej książki beletrystycznej (tekst bez skomplikowanych tabel, schematów itp.).

### Funkcje dodatkowe (NICE TO HAVE)
*   **Rozpoznawanie numerów stron:** Próba rozpoznania numerów stron z zeskanowanych zdjęć.
*   **Weryfikacja sekwencji stron:** Sygnalizowanie użytkownikowi potencjalnego pominięcia strony na podstawie rozpoznanych numerów. Umożliwienie akceptacji wykrytych niezgodności przez użytkownika.
*   **Ocena jakości zdjęcia:** Wstępna ocena jakości zdjęcia (oświetlenie, kontrast, ostrość, kompletność strony) i prośba o ponowienie próby w przypadku słabej jakości.
*   **Automatyczne przetwarzanie:** Opcja automatycznego uruchamiania procesu OCR i generowania audio po wykryciu połączenia z internetem i nowych zeskanowanych stron.
*   **Wybór głosu lektora:** Możliwość wyboru silnika/głosu używanego do syntezy mowy.
*   **Rozpoznawanie tytułu z okładki:** Próba rozpoznania tytułu książki z zdjęcia okładki (z wykorzystaniem multimedialnego LLM). W przypadku niepowodzenia - nadanie domyślnej nazwy ("Książka X"). Możliwość ręcznej edycji tytułu (potencjalnie przez opiekuna).
*   **Rozpoznawanie typu treści:** Próba rozpoznania i wypowiedzenia typu treści (np. "Rozdział:", "Strona:", "Punkt:") na podstawie analizy układu strony (w MVP uproszczona analiza, np. po wielkości czcionki, pogrubieniu).
*   **Wizualizacje czytania:** Proste animacje (np. pulsująca kropka) synchronizowane z mową podczas czytania.
*   **Głosowe potwierdzenie wyboru:** Opcjonalne głosowe odczytywanie nazwy wybranej opcji/przycisku po kliknięciu.
*   **Wysoki kontrast interfejsu:** Opcjonalny tryb wyświetlania z wysokim kontrastem kolorów.
*   **Szczegółowe zarządzanie pamięcią:** Ekran pokazujący zajętość pamięci (zdjęcia, tekst, audio, z podziałem na książki) z możliwością usuwania danych dla poszczególnych typów lub książek.
*   **Wsparcie dla gestów:** Wykorzystanie prostych gestów nawigacyjnych specyficznych dla systemu Android lub aplikacji.

### Przepływ użytkownika (User Journey)
1.  Uruchomienie aplikacji -> Ekran główny z dużymi ikonami "aktualnych" książek i opcją "Nowa Książka".
2.  **Dodawanie nowej książki:** Wybór "Nowa Książka" -> Ekran skanowania -> Robienie zdjęcia okładki -> Robienie zdjęć kolejnych stron (offline) -> Zakończenie skanowania (offline) -> (Opcjonalnie, automatyczne przetwarzanie po połączeniu online LUB ręczne uruchomienie przetwarzania) -> Przetwarzanie (online: OCR, synteza głosu, analiza struktury) -> Książka staje się gotowa do czytania offline.
3.  **Czytanie książki:** Wybór gotowej książki z ekranu głównego/archiwum -> Ekran szczegółów książki (tytuł, okładka, postęp) -> Wybór "Czytaj" -> Uruchomienie lektora z możliwością pauzy/wznowienia/nawigacji.
4.  **Wznowienie skanowania/przetwarzania:** Wybór książki w trakcie skanowania/przetwarzania -> Ekran szczegółów -> Wybór "Kontynuuj skanowanie" lub "Przetwarzaj teraz".
5.  **Zarządzanie pamięcią:** Nawigacja do ekranu zarządzania pamięcią -> Przegląd zajętej pamięci według typu danych i książek -> Wybór danych do usunięcia.
6.  **Archiwizacja/Dostęp do Archiwum:** Możliwość przeniesienia książki do archiwum i dostępu do książek zarchiwizowanych.

## 3. Wymagania Techniczne
### Platforma i technologie
*   Platforma docelowa: System operacyjny Android.
*   Technologie do rozpoznawania tekstu (OCR): Wykorzystanie zewnętrznego API/silnika OCR (online, z potencjalnym użyciem LLM do poprawy wyników/struktury).
*   Technologie syntezy mowy (TTS): Wykorzystanie zewnętrznego API/silnika TTS (online do generowania danych do odczytu, offline do odtwarzania) lub wbudowanego silnika Android TTS.
*   Wykorzystanie LLM: Potencjalne użycie zaawansowanych modeli językowych (multimodalnych do analizy okładki, generalnych do analizy struktury tekstu, poprawy OCR, itp.) - opcjonalne, zależne od dostępnych API i budżetu.
*   Przechowywanie danych: Lokalna pamięć urządzenia z systemem Android (zdjęcia, rozpoznany tekst, pliki audio).

### Wymagania wydajnościowe
*   Skanowanie (robienie zdjęć) musi być płynne i szybkie.
*   Odczyt (odtwarzanie audio) musi być płynne i nieprzerwane w trybie offline.
*   Przetwarzanie pojedynczej strony (online: OCR, TTS) powinno trwać krócej niż jej odczytanie na głos.
*   Interfejs użytkownika musi być responsywny mimo dużych elementów i potencjalnego użycia dodatkowych funkcji dostępności.

### Ograniczenia i zależności
*   Wymagane połączenie z internetem dla procesu przetwarzania (OCR, TTS, LLM).
*   Wymagana wystarczająca ilość wolnej pamięci na urządzeniu do przechowywania zeskanowanych i przetworzonych książek.
*   Zależność od zewnętrznych dostawców API dla OCR, TTS i LLM (dostępność, stabilność, koszty, ograniczenia API).
*   Dokładność rozpoznawania tekstu i struktury może się różnić w zależności od jakości zdjęć, układu strony i użytych technologii.

### Bezpieczeństwo
*   Dane książek (zdjęcia, tekst, audio) będą przechowywane lokalnie na urządzeniu.
*   Brak wymogu specjalnego szyfrowania danych lokalnych w pierwszej wersji (MVP).
*   Należy wziąć pod uwagę politykę prywatności zewnętrznych API używanych do przetwarzania danych tekstowych i graficznych.

## 4. Interfejs Użytkownika
### Wymagania dotyczące UI/UX
*   Minimalistyczny design z ograniczoną liczbą elementów na ekranie (4-8 dużych ikon na ekranie głównym).
*   Duże, łatwo klikalne obszary dla przycisków i opcji.
*   Wyraźne, czytelne oznaczenia ikon.
*   Opcjonalny tryb wysokiego kontrastu (np. biały tekst na czarnym tle, czarny tekst na żółtym tle).
*   Możliwość włączenia głosowego potwierdzenia wyboru opcji.
*   Proste wskaźniki postępu procesu skanowania i przetwarzania, czytelne dla osób ze słabym wzrokiem.

### Inspiracje i referencje
Brak konkretnych referencji, projekt ma być dostosowany do potrzeb użytkownika ze słabym wzrokiem, czerpiąc z ogólnych zasad projektowania dostępnych interfejsów (duże elementy, kontrast, głosowe wskazówki).

### Responsywność i dostępność
*   Interfejs musi być czytelny i funkcjonalny na różnych rozmiarach ekranów telefonów i tabletów z systemem Android.
*   Projekt powinien uwzględniać integrację z podstawowymi funkcjami dostępności systemu Android (np. gesty nawigacyjne, potencjalnie podstawowe wsparcie dla TalkBack - do ustalenia w zależności od złożoności UI).

## 5. Dane i Integracje
### Źródła danych
*   Wejście: Zdjęcia stron książek (pliki graficzne: JPEG, PNG).
*   Przetwarzanie: Dane tekstowe uzyskane z OCR, dane audio uzyskane z TTS.
*   Metadane: Tytuł książki, okładka (zdjęcie), postęp skanowania, postęp czytania, potencjalnie rozpoznane numery stron i struktura.

### Formaty plików
*   Zdjęcia: Standardowe formaty graficzne (np. JPEG).
*   Rozpoznany tekst: Pliki tekstowe (np. TXT) lub struktury danych (np. JSON/XML) przechowujące tekst i znaczniki struktury.
*   Audio: Pliki audio (np. MP3, WAV) wygenerowane przez TTS.

### API i integracje zewnętrzne
*   Android Camera API: Do robienia zdjęć.
*   Zewnętrzne API OCR: Do rozpoznawania tekstu ze zdjęć (online).
*   Zewnętrzne API TTS: Do generowania plików audio z tekstu (online).
*   Zewnętrzne API LLM: Do rozpoznawania okładki, analizy struktury, itp. (online, jeśli wybrane).
*   Android Media Player API: Do odtwarzania audio.
*   Android Storage API: Do zarządzania plikami lokalnymi.

### Baza danych
Lokalny mechanizm przechowywania danych książek, tekstu i audio na urządzeniu. Może to być system plików w połączeniu z plikiem konfiguracyjnym/indeksem lub prosta baza danych SQLite, w zależności od złożoności zarządzania danymi.

## 6. Kryteria Akceptacji
### Definicja "gotowego" produktu
Pierwsza wersja (MVP) będzie uznana za gotową, gdy będzie możliwe:
*   Zeskanowanie wszystkich stron (z pominięciem okładek i stron redakcyjnych) co najmniej jednej standardowej książki beletrystycznej (bez skomplikowanych układów).
*   Pomyślne przetworzenie zeskanowanych stron przez proces online (OCR, TTS) z akceptowalną jakością rozpoznania tekstu i mowy.
*   Odtworzenie całej przetworzonej książki na głos w trybie offline.
*   Zatrzymanie i wznowienie czytania w ostatnim miejscu.
*   Podstawowa nawigacja (np. przejście do następnej/poprzedniej strony/akapitu).
*   Interfejs graficzny (ekran główny z ikonami, ekran czytania z kontrolkami) jest funkcjonalny dla osoby ze słabym wzrokiem (duże elementy).

### Testy i weryfikacja
*   Testy funkcjonalne end-to-end (skanowanie -> przetwarzanie -> czytanie) z co najmniej jedną testową książką o prostym układzie.
*   Testy offline dla funkcji skanowania i czytania.
*   Testy użyteczności z docelowym użytkownikiem lub osobą o podobnych potrzebach wzrokowych, w celu oceny czytelności i intuicyjności interfejsu.
*   Weryfikacja jakości rozpoznanego tekstu i syntezy mowy dla testowej książki.

### Metryki sukcesu
*   Procent pomyślnie przetworzonych stron w testowej książce (np. >95%).
*   Subiektywna ocena użyteczności interfejsu przez docelowego użytkownika (czy jest w stanie samodzielnie uruchomić czytanie).
*   Stabilność działania aplikacji w trybach offline i online.

## 7. Ograniczenia Projektu
### Budżet i zasoby
*   Budżet na rozwój aplikacji i potencjalne koszty operacyjne związane z użyciem płatnych zewnętrznych API (OCR, TTS, LLM). Brak sztywnych ograniczeń budżetowych na tym etapie, ale konieczne będzie zarządzanie kosztami API.
*   Dostępne zasoby programistyczne do stworzenia aplikacji na Androida, integracji z API i zaprojektowania interfejsu użytkownika.

### Harmonogram
*   Nie określono sztywnych ram czasowych, projekt ma być realizowany iteracyjnie, zaczynając od MVP.

### Ograniczenia techniczne
*   Ograniczona dokładność OCR w przypadku zdjęć o słabej jakości, nietypowych czcionek, złożonych układów, obrazków w tekście.
*   Złożoność niezawodnego rozpoznawania numerów stron i struktury tekstu z różnorodnych książek.
*   Potencjalnie wysokie koszty użycia zaawansowanych API (zwłaszcza LLM) przy dużej ilości skanowanego tekstu.
*   Wydajność przetwarzania online zależna od szybkości internetu i czasu odpowiedzi API.
*   Ograniczenia lokalnej pamięci urządzenia.

### Znane ryzyka
*   Niska jakość zdjęć wykonywanych przez użytkownika może uniemożliwić prawidłowe rozpoznanie tekstu.
*   Zmiany w dostępności, cenach lub warunkach korzystania z zewnętrznych API.
*   Trudności w zaprojektowaniu interfejsu naprawdę intuicyjnego i łatwego w obsłudze dla osoby ze specyficznymi problemami wzrokowymi.
*   Niewystarczająca jakość syntetyzowanej mowy w trybie offline (jeśli używany jest wbudowany silnik TTS o niskiej jakości).

## 8. Dodatkowe Uwagi
### Specjalne wymagania
*   Aplikacja musi być maksymalnie prosta i intuicyjna w obsłudze dla osoby ze słabym wzrokiem, nawet kosztem pominięcia niektórych zaawansowanych funkcji w pierwszej wersji.
*   Funkcjonalności offline (skanowanie zdjęć, czytanie audio) są krytyczne.

### Przyszłe rozszerzenia
*   Możliwość ręcznej korekty rozpoznanego tekstu przez opiekuna.
*   Bardziej zaawansowana analiza struktury książki (tabel, list, przypisów).
*   Lepsza obsługa skomplikowanych formatowań tekstu.
*   Publikacja aplikacji w sklepie Google Play Store.
*   Integracja z innymi formatami dokumentów (np. PDF, EPUB - wymagałoby to innego podejścia niż skanowanie zdjęć).
*   Możliwość eksportu przetworzonego tekstu/audio.

### Uwagi od klienta
*   Projekt ma charakter osobisty, tworzony z myślą o konkretnym użytkowniku.
*   Akceptowalne jest podejście iteracyjne, z naciskiem na szybkie dostarczenie działającego MVP.
*   Gotowość do wykorzystania (i opłacania) zewnętrznych, zaawansowanych usług online (API, LLM), jeśli znacząco poprawią jakość i funkcjonalność.
*   W MVP wystarczy funkcjonalność dla książek beletrystycznych.
*   Głosowy przewodnik wprowadzający do aplikacji będzie przydatny.

