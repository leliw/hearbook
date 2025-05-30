# Analiza i Wybór API OCR/TTS - Rekomendacje dla Lektora Książek

## Executive Summary - Rekomendowane API

### **Rekomendacja główna: Google Cloud Platform**
- **OCR**: Google Cloud Vision API (Document Text Detection)
- **TTS**: Google Cloud Text-to-Speech API
- **Uzasadnienie**: Najlepszy balans jakości, ceny i integracji dla MVP

---

## 1. Szczegółowa Analiza API OCR

### 1.1 Google Cloud Vision API
**Funkcja**: Document Text Detection (najlepsza dla książek)

**Cennik (2025)**:
- Pierwsze 1000 stron/miesiąc: **DARMOWE**
- Strony 1001-5,000,000: **$1.50 za 1000 stron**
- Powyżej 5M stron: **$0.60 za 1000 stron**

**Zalety**:
- Wysokiej jakości OCR dla dokumentów
- Darmowy tier na start
- Wykrywanie układu strony i struktury
- Doskonała obsługa polskich znaków diakrytycznych
- Rozpoznawanie różnych czcionek

**Wady**:
- Wymaga połączenia internetowego
- Wyższy koszt przy dużej skali

### 1.2 AWS Textract
**Funkcja**: DetectDocumentText

**Cennik (2025)**:
- Pierwsze 1000 stron/miesiąc: **DARMOWE** (przez 3 miesiące)
- Następne strony: **$1.50 za 1000 stron**
- W wybranych regionach: **$1.02 za 1000 stron** (po redukcji cen)

**Zalety**:
- Bardzo dobra jakość OCR
- Specjalne funkcje dla formularzy i tabel
- Infrastruktura AWS

**Wady**:
- Brak długoterminowego darmowego tieru
- Mniej optymalizowany dla ciągłego tekstu książek

### 1.3 Azure Computer Vision (Read API)
**Cennik**:
- Pierwsze 5000 transakcji/miesiąc: **DARMOWE**
- Następne: **$1.00 za 1000 transakcji**

**Zalety**:
- Konkurencyjny cennik
- Dobra jakość OCR
- Większy darmowy tier

**Wady**:
- Mniejsza społeczność deweloperska w Polsce
- Mniej dokumentacji w języku polskim

---

## 2. Szczegółowa Analiza API TTS

### 2.1 Google Cloud Text-to-Speech
**Cennik (2025)**:
- Standard głosy: **$4.00 za 1M znaków**
- WaveNet głosy (wyższa jakość): **$16.00 za 1M znaków**
- Neural2 głosy (najwyższa jakość): **$20.00 za 1M znaków**

**Polskie głosy dostępne**:
- Standard: pl-PL-Wavenet-A (żeński), pl-PL-Wavenet-B (męski)
- Neural2: pl-PL-Neural2-A, pl-PL-Neural2-B

**Zalety**:
- Naturalne polskie głosy
- Różne poziomy jakości do wyboru
- SSML wsparcie dla kontroli wymowy
- Dobra intonacja dla długich tekstów

### 2.2 AWS Polly
**Cennik**:
- Standard głosy: **$4.00 za 1M znaków**
- Neural głosy: **$16.00 za 1M znaków**

**Polskie głosy**:
- Standard: Maja (żeński), Jan (męski)
- Neural: Ola (żeński)

**Zalety**:
- Konkurencyjne ceny
- Dobra jakość neural voices

**Wady**:
- Mniej polskich głosów do wyboru
- Słabsza intonacja dla długich tekstów

### 2.3 Azure Cognitive Services Speech
**Cennik**:
- Standard: **$4.00 za 1M znaków**
- Neural: **$16.00 za 1M znaków**

**Polskie głosy**:
- Neural: pl-PL-AgnieszkaNeural (żeński), pl-PL-MarekNeural (męski)

---

## 3. Kalkulacja Kosztów - Książka 300 Stron

### Założenia dla typowej książki:
- **300 stron**
- **Średnio 2000 znaków na stronę** (ok. 300-400 słów)
- **Łącznie: 600,000 znaków tekstu**

### 3.1 Koszt OCR (na książkę)

| Dostawca | Koszt za książkę (300 stron) | Uwagi |
|----------|-------------------------------|-------|
| **Google Cloud Vision** | **$0.45** | Po wykorzystaniu darmowego tieru |
| AWS Textract | $0.45 | Standardowa cena |
| Azure Computer Vision | $0.30 | Najniższa cena |

### 3.2 Koszt TTS (na książkę)

| Dostawca & Jakość | Koszt za książkę (600k znaków) | Jakość głosu |
|-------------------|--------------------------------|--------------|
| **Google Neural2** | **$12.00** | **Najwyższa jakość** ⭐ |
| Google WaveNet | $9.60 | Wysoka jakość |
| Google Standard | $2.40 | Podstawowa jakość |
| AWS Neural | $9.60 | Wysoka jakość |
| Azure Neural | $9.60 | Wysoka jakość |

### 3.3 Łączny Koszt na Książkę

| Scenariusz | OCR + TTS | Łączny koszt |
|------------|-----------|--------------|
| **Rekomendowany (Google)** | $0.45 + $12.00 | **$12.45** |
| Ekonomiczny (Google) | $0.45 + $2.40 | $2.85 |
| Konkurencyjny (Azure) | $0.30 + $9.60 | $9.90 |

---

## 4. Analiza Miesięcznych Kosztów Operacyjnych

### Scenariusz A: Projekt Pilotażowy (5 użytkowników)
- **5 książek/miesiąc** (średnio 1 książka/użytkownik)
- **Koszt miesięczny**: $62.25 (Google rekomendowany)
- **Koszt roczny**: $747

### Scenariusz B: Wdrożenie Lokalne (25 użytkowników)
- **25 książek/miesiąc**
- **Koszt miesięczny**: $311.25
- **Koszt roczny**: $3,735

### Scenariusz C: Skala Komercyjna (100 użytkowników)
- **100 książek/miesiąc**
- **Koszt miesięczny**: $1,245
- **Koszt roczny**: $14,940

### Optymalizacje kosztów:
1. **Wykorzystanie darmowych tierów**: Pierwsze 1000 stron OCR miesięcznie za darmo
2. **Wybór jakości TTS**: Standard vs Neural (różnica $9.60 na książkę)
3. **Batch processing**: Grupowanie żądań dla efektywności
4. **Caching**: Unikanie ponownego przetwarzania tych samych stron

---

## 5. Finalne Rekomendacje

### Dla MVP (Rekomendacja A):
- **OCR**: Google Cloud Vision API (Document Text Detection)
- **TTS**: Google Cloud Text-to-Speech (Neural2 - najwyższa jakość)
- **Uzasadnienie**: Najlepsza jakość, jeden dostawca, łatwa integracja
- **Budżet na MVP**: $200-500/miesiąc na testy

### Dla Wersji Produkcyjnej (Rekomendacja B):
- **OCR**: Google Cloud Vision API 
- **TTS**: Mieszane podejście - Neural2 dla nowych książek, Standard dla testów
- **Backup API**: Azure jako secondary dla redundancji
- **Budżet**: $300-1500/miesiąc zależnie od adoption

### Dodatkowe Rekomendacje:
1. **Implementuj monitoring kosztów** - alerty przy przekroczeniu budżetu
2. **Testuj jakość** na różnych typach książek przed wyborem
3. **Przygotuj fallback** na lokalne Android TTS dla sytuacji offline
4. **Rozważ kompresję obrazów** przed wysłaniem do OCR (oszczędność ~20-30%)

---

## 6. Plan Testów Porównawczych

### Faza testowa (2 tygodnie):
1. **Przygotuj 20 testowych stron** różnych typów (czcionki, układy)
2. **Przetestuj wszystkie 3 API OCR** na tych samych stronach
3. **Oceń jakość rozpoznawania** (dokładność, błędy polskich znaków)
4. **Przetestuj 3 różne głosy TTS** na tym samym tekście
5. **Zbierz feedback** od potencjalnych użytkowników

### Kryteria oceny:
- **Dokładność OCR**: % poprawnie rozpoznanych słów
- **Jakość TTS**: Naturalność, zrozumiałość, przyjemność słuchania
- **Niezawodność API**: Czas odpowiedzi, dostępność
- **Łatwość integracji**: Jakość dokumentacji, SDK

**Budżet na testy**: $50-100 (pokryje testy wszystkich API)
