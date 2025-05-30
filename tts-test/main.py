import os
import time
from google.cloud import texttospeech
import boto3
import azure.cognitiveservices.speech as speechsdk
from dotenv import load_dotenv


# --- Konfiguracja ---

load_dotenv()

# Tekst do przetestowania
TEST_TEXT = """
Witajcie w naszym eksperymentalnym laboratorium testowym! 
Dzisiaj będziemy porównywać jakość syntetycznej mowy generowanej przez wiodące API. 
Zwróćcie uwagę na płynność, naturalność intonacji i poprawność wymowy polskich słów. 
Mam nadzieję, że te testy pomogą w wyborze najlepszego rozwiązania dla naszego projektu Lektora Książek dla Słabowidzących. Dziękuję za uwagę!
"""

# Pliki wyjściowe
OUTPUT_DIR = "tts_tests"
if not os.path.exists(OUTPUT_DIR):
    os.makedirs(OUTPUT_DIR)

# --- Funkcje dla poszczególnych API ---


def synthesize_google(text, voice_name, filename):
    """Generuje mowę używając Google Cloud Text-to-Speech."""
    try:
        start_time = time.time()
        client = texttospeech.TextToSpeechClient()

        synthesis_input = texttospeech.SynthesisInput(text=text)

        voice = texttospeech.VoiceSelectionParams(
            language_code="pl-PL",
            name=voice_name
        )

        audio_config = texttospeech.AudioConfig(
            audio_encoding=texttospeech.AudioEncoding.MP3
        )

        response = client.synthesize_speech(
            input=synthesis_input, voice=voice, audio_config=audio_config
        )

        with open(os.path.join(OUTPUT_DIR, filename), "wb") as out:
            out.write(response.audio_content)

        end_time = time.time()
        print(
            f"Google Cloud TTS ({voice_name}): Sukces! Zapisano do {filename}. Czas: {end_time - start_time:.2f}s"
        )
        return True
    except Exception as e:
        end_time = time.time()
        print(
            f"Google Cloud TTS ({voice_name}): Błąd! {e}. Czas: {end_time - start_time:.2f}s"
        )
        print(
            "Upewnij się, że zmienna środowiskowa GOOGLE_APPLICATION_CREDENTIALS jest poprawnie ustawiona i klucz ma uprawnienia."
        )
        return False


def synthesize_aws_polly(text, voice_id, filename):
    """Generuje mowę używając AWS Polly."""
    try:
        start_time = time.time()
        region = os.environ.get("AWS_REGION")
        if not region:
            region = os.environ.get(
                "AWS_DEFAULT_REGION", "eu-west-2"
            )  # Użyj domyślnego, jeśli brak

        polly_client = boto3.client("polly", region_name=region)

        response = polly_client.synthesize_speech(
            Text=text,
            OutputFormat="mp3",
            VoiceId=voice_id,
            Engine = 'neural' if voice_id=="Ola" else 'standard'
        )

        if "AudioStream" in response:
            with open(os.path.join(OUTPUT_DIR, filename), "wb") as out:
                # AudioStream is a file-like object. Read its contents.
                out.write(response["AudioStream"].read())

            end_time = time.time()
            print(
                f"AWS Polly ({voice_id}): Sukces! Zapisano do {filename}. Czas: {end_time - start_time:.2f}s"
            )
            return True
        else:
            end_time = time.time()
            print(
                f"AWS Polly ({voice_id}): Błąd! Brak AudioStream w odpowiedzi. Czas: {end_time - start_time:.2f}s"
            )
            return False

    except Exception as e:
        end_time = time.time()
        print(f"AWS Polly ({voice_id}): Błąd! {e}. Czas: {end_time - start_time:.2f}s")
        print(
            "Upewnij się, że zmienne środowiskowe AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY i AWS_REGION są poprawnie ustawione."
        )
        return False


def synthesize_azure_speech(text, voice_name, filename):
    """Generuje mowę używając Azure Cognitive Services Speech."""
    try:
        start_time = time.time()
        speech_key = os.environ.get("AZURE_SPEECH_KEY")
        speech_endpoint = os.environ.get("AZURE_SPEECH_ENDPOINT")
        speech_region = os.environ.get("AZURE_SPEECH_REGION")

        if not speech_key or not speech_region:
            raise ValueError(
                "AZURE_SPEECH_KEY and AZURE_SPEECH_REGION environment variables must be set."
            )

        speech_config = speechsdk.SpeechConfig(
            subscription=speech_key, endpoint=speech_endpoint
        )
        speech_config.speech_synthesis_voice_name = voice_name
        # Output in MP3 format
        speech_config.set_speech_synthesis_output_format(
            speechsdk.SpeechSynthesisOutputFormat.Audio16Khz64KBitRateMonoMp3
        )

        # Direct output to a file
        file_output_config = speechsdk.audio.AudioOutputConfig(
            filename=os.path.join(OUTPUT_DIR, filename)
        )
        speech_synthesizer = speechsdk.SpeechSynthesizer(
            speech_config=speech_config, audio_config=file_output_config
        )

        # Synthesize the text
        result = speech_synthesizer.speak_text_async(text).get()

        if result.reason == speechsdk.ResultReason.SynthesizingAudioCompleted:
            end_time = time.time()
            print(
                f"Azure Speech ({voice_name}): Sukces! Zapisano do {filename}. Czas: {end_time - start_time:.2f}s"
            )
            return True
        elif result.reason == speechsdk.ResultReason.Canceled:
            cancellation_details = result.cancellation_details
            end_time = time.time()
            print(
                f"Azure Speech ({voice_name}): Synteza anulowana: {cancellation_details.reason}. Czas: {end_time - start_time:.2f}s"
            )
            if cancellation_details.reason == speechsdk.CancellationReason.Error:
                print(f"Azure Error Details: {cancellation_details.error_details}")
            print(
                "Upewnij się, że zmienne środowiskowe AZURE_SPEECH_KEY i AZURE_SPEECH_REGION są poprawnie ustawione."
            )
            return False
        else:
            end_time = time.time()
            print(
                f"Azure Speech ({voice_name}): Nieznany błąd podczas syntezy. Czas: {end_time - start_time:.2f}s"
            )
            return False

    except Exception as e:
        end_time = time.time()
        print(
            f"Azure Speech ({voice_name}): Błąd! {e}. Czas: {end_time - start_time:.2f}s"
        )
        print(
            "Upewnij się, że zmienne środowiskowe AZURE_SPEECH_KEY i AZURE_SPEECH_REGION są poprawnie ustawione i klucz ma uprawnienia."
        )
        return False


# --- Główna część skryptu ---

if __name__ == "__main__":
    print("--- Rozpoczęto testowanie API TTS ---")
    print(f"Tekst do syntezy:\n---\n{TEST_TEXT}\n---")

    tests = [
        {
            "api": "google",
            "voice": "pl-PL-Standard-A",
            "filename": "google_standard_A_pl.mp3",
        },
        {
            "api": "google",
            "voice": "pl-PL-Wavenet-B",
            "filename": "google_wavenet_B_pl.mp3",
        },
        {
            "api": "aws",
            "voice": "Ola",
            "filename": "aws_neural_Ola_pl.mp3",
        },  # Neural voice
        {
            "api": "aws",
            "voice": "Jan",
            "filename": "aws_standard_Jan_pl.mp3",
        },  # Standard voice
        {
            "api": "azure",
            "voice": "pl-PL-AgnieszkaNeural",
            "filename": "azure_neural_Agnieszka_pl.mp3",
        },
        {
            "api": "azure",
            "voice": "pl-PL-MarekNeural",
            "filename": "azure_neural_Marek_pl.mp3",
        },
    ]

    for test in tests:
        api = test["api"]
        voice = test["voice"]
        filename = test["filename"]

        if api == "google":
            synthesize_google(TEST_TEXT, voice, filename)
        elif api == "aws":
            synthesize_aws_polly(TEST_TEXT, voice, filename)
        elif api == "azure":
            synthesize_azure_speech(TEST_TEXT, voice, filename)
        else:
            print(f"Nieznane API: {api}")

        print("-" * 20)  # Separator między testami

    print(f"--- Testowanie API TTS zakończone ---")
    print(f"Pliki audio powinny znajdować się w katalogu: {OUTPUT_DIR}")
    print("Odsłuchaj pliki, aby porównać jakość głosów.")
