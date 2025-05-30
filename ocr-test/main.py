import os
import io
import glob # Import the glob module
import time
from PIL import Image
from google.cloud import vision
import boto3
from azure.cognitiveservices.vision.computervision import ComputerVisionClient
from azure.cognitiveservices.vision.computervision.models import OperationStatusCodes
from msrest.authentication import CognitiveServicesCredentials
from dotenv import load_dotenv


load_dotenv()

# --- KONFIGURACJA API ---

# 1. Google Cloud Vision API
# Zainstaluj: pip install google-cloud-vision
# Ustaw zmienną środowiskową: GOOGLE_APPLICATION_CREDENTIALS wskazuje na plik JSON klucza serwisowego
# Więcej info: https://cloud.google.com/docs/authentication/getting-started
# Jeśli nie ustawiasz zmiennej, podaj ścieżkę do pliku klucza:
# os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = "path/to/your/key.json"

# Inicjalizacja klienta Google Cloud Vision (automatycznie szuka GOOGLE_APPLICATION_CREDENTIALS)
try:
    google_vision_client = vision.ImageAnnotatorClient()
    print("Google Cloud Vision Client initialized.")
except Exception as e:
    google_vision_client = None
    print(f"Failed to initialize Google Cloud Vision Client. Check credentials: {e}")

# 2. AWS Textract
# Zainstaluj: pip install boto3
# Skonfiguruj AWS CLI lub zmienne środowiskowe (AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, AWS_REGION_NAME)
# Więcej info: https://boto3.amazonaws.com/v1/documentation/api/latest/guide/credentials.html
# Jeśli nie konfigurujesz przez CLI/env, podaj klucze bezpośrednio (NIEZALECANE):
# aws_access_key_id = "YOUR_AWS_ACCESS_KEY_ID"
# aws_secret_access_key = "YOUR_AWS_SECRET_ACCESS_KEY"
# aws_region_name = "YOUR_AWS_REGION" # np. 'us-east-1'

try:
    aws_textract_client = boto3.client('textract') # Domyślnie szuka w standardowych miejscach
    # Jeśli podajesz jawnie (NIEZALECANE):
    # aws_textract_client = boto3.client('textract',
    #                                    aws_access_key_id=aws_access_key_id,
    #                                    aws_secret_access_key=aws_secret_access_key,
    #                                    region_name=aws_region_name)
    print("AWS Textract Client initialized.")
except Exception as e:
    aws_textract_client = None
    print(f"Failed to initialize AWS Textract Client. Check credentials/region: {e}")


# 3. Azure Computer Vision (Read API)
# Zainstaluj: pip install azure-cognitiveservices-vision-computervision azure-core
# Pobierz klucz i endpoint z Azure Portal
# Więcej info: https://learn.microsoft.com/en-us/azure/cognitive-services/computer-vision/quickstarts-sdk/python-sdk
azure_endpoint = os.environ.get("AZURE_VISION_ENDPOINT", "YOUR_AZURE_VISION_ENDPOINT")
azure_key = os.environ.get("AZURE_VISION_KEY", "YOUR_AZURE_VISION_KEY")

if azure_endpoint == "YOUR_AZURE_VISION_ENDPOINT" or azure_key == "YOUR_AZURE_VISION_KEY":
    azure_vision_client = None
    print("Azure Computer Vision configuration missing. Set AZURE_VISION_ENDPOINT and AZURE_VISION_KEY env vars or update script.")
else:
    try:
        azure_vision_client = ComputerVisionClient(azure_endpoint, CognitiveServicesCredentials(azure_key))
        print("Azure Computer Vision Client initialized.")
    except Exception as e:
        azure_vision_client = None
        print(f"Failed to initialize Azure Computer Vision Client. Check endpoint and key: {e}")


# --- FUNKCJE OCR DLA KAŻDEGO API ---

def ocr_google_cloud(image_path):
    if not google_vision_client:
        return "Google Cloud Vision API not configured."
    try:
        with io.open(image_path, 'rb') as image_file:
            content = image_file.read()
        image = vision.Image(content=content)

        # Używamy document_text_detection, które jest zoptymalizowane dla gęstego tekstu (książek)
        response = google_vision_client.document_text_detection(image=image)

        # Pełny tekst strony
        full_text = response.full_text_annotation.text
        return full_text

    except Exception as e:
        return f"Google Cloud Vision Error: {e}"

def ocr_aws_textract(image_path):
    if not aws_textract_client:
        return "AWS Textract API not configured."
    try:
        with io.open(image_path, 'rb') as image_file:
            content = image_file.read()

        # Używamy detect_document_text dla prostego rozpoznawania tekstu
        response = aws_textract_client.detect_document_text(Document={'Bytes': content})

        # Zbieramy tekst z bloków typu 'LINE'
        text = ""
        for block in response['Blocks']:
            if block['BlockType'] == 'LINE':
                text += block['Text'] + "\n"
        return text.strip() # Usuń ostatnią pustą linię

    except Exception as e:
        return f"AWS Textract Error: {e}"

def ocr_azure_cv(image_path):
    if not azure_vision_client:
        return "Azure Computer Vision API not configured."
    try:
        # Azure Read API wymaga streamu pliku i jest asynchroniczne, wymaga pollingu
        with open(image_path, "rb") as image_stream:
            # Uruchom asynchroniczne zadanie czytania
            read_response = azure_vision_client.read_in_stream(image_stream, raw=True)

        # Pobierz ID operacji z nagłówka 'Operation-Location'
        operation_location = read_response.headers["Operation-Location"]
        operation_id = operation_location.split("/")[-1]

        # Oczekiwanie na zakończenie operacji
        print(f"Azure: Reading '{os.path.basename(image_path)}', operation ID: {operation_id}. Waiting...")
        while True:
            read_result = azure_vision_client.get_read_result(operation_id)
            if read_result.status not in ['notStarted', 'running']:
                break
            time.sleep(1) # Czekaj 1 sekundę

        # Wyodrębnij tekst z wyniku
        text = ""
        if read_result.status == OperationStatusCodes.succeeded:
            for text_result in read_result.analyze_result.read_results:
                for line in text_result.lines:
                    text += line.text + "\n"
        return text.strip()

    except Exception as e:
        return f"Azure Computer Vision Error: {e}"


# --- GŁÓWNY BLOK TESTOWY ---

if __name__ == "__main__":
    # --- LISTA TESTOWYCH OBRAZÓW ---
    # Zmień ścieżki na rzeczywiste ścieżki do Twoich testowych plików graficznych.
    # Użyj kilku zdjęć z różnymi czcionkami, układami, oświetleniem itp.
    
    # Define the directory containing the images
    image_dir = "pages/"
    # Define allowed image extensions
    allowed_extensions = ("*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp", "*.tiff")
    
    test_images = []
    # Check if the directory exists
    if os.path.isdir(image_dir):
        # Populate the list with images of allowed extensions
        for ext in allowed_extensions:
            test_images.extend(glob.glob(os.path.join(image_dir, ext)))
    else:
        print(f"Warning: Directory '{image_dir}' not found. No images will be processed.")

    # Sort the images for consistent order (optional)
    test_images.sort()


    print("--- ROZPOCZĘCIE TESTÓW OCR ---")
    print("Upewnij się, że skonfigurowano dostęp do API i podano poprawne ścieżki obrazów.")
    print("-----------------------------\n")
    if not test_images:
        print(f"No images found in '{image_dir}' with allowed extensions: {', '.join(allowed_extensions)}")
    else:
        for image_path in test_images:
            if not os.path.exists(image_path): # This check might be redundant if glob already ensures existence
                print(f"ERROR: File not found: {image_path}. Skipping.")
                continue

            print(f"Przetwarzanie pliku: {image_path}")
            print("--- Google Cloud Vision ---")
            google_text = ocr_google_cloud(image_path)
            print(google_text)
            print("\n")

            print("--- AWS Textract ---")
            aws_text = ocr_aws_textract(image_path)
            print(aws_text)
            print("\n")

            print("--- Azure Computer Vision (Read API) ---")
            azure_text = ocr_azure_cv(image_path)
            print(azure_text)
            print("\n")

            print("-" * 50) # Separator między plikami

    print("--- TESTY OCR ZAKOŃCZONE ---")

    print("\n--- WSKAZÓWKI DOTYCZĄCE ANALIZY WYNIKÓW ---")
    print("Porównaj tekst uzyskany z każdego API dla każdej strony.")
    print("- Sprawdź dokładność rozpoznawania (literówki, błędy w słowach).")
    print("- Zwróć uwagę na poprawne rozpoznawanie polskich znaków diakrytycznych.")
    print("- Oceń, jak API radzi sobie z różnymi czcionkami, rozmiarami tekstu i układami strony.")
    print("- Zaobserwuj czas odpowiedzi (szczególnie dla Azure ze względu na polling).")
    print("- Weź pod uwagę cenę za 1000 stron dla każdego dostawcy przy masowym przetwarzaniu.")
    print("\nNa podstawie tych obserwacji i kalkulacji kosztów wybierz najlepsze API dla Twojego projektu.")
    print("Pamiętaj o testach na większej liczbie różnorodnych stron, aby wyniki były bardziej miarodajne.")
