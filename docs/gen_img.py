import base64

from dotenv import load_dotenv
from openai import OpenAI

load_dotenv()

with open("/home/marcin/src/hearbook/docs/Projekt UI_UX Dostępność.md", "r") as f:
    text = f.read()

parts = text.split("\n---\n")
prompt = parts[0]
img_prompts = parts[2].split("\n**Generating Image: ")[1:]
img_names = [t.split("**\n")[0] for t in img_prompts]
img_idx = 0

client = OpenAI()

response = client.responses.create(
    model="gpt-4.1-mini",
    input=f"{prompt}\n\n**Generate image {img_prompts[img_idx]}",
    tools=[{"type": "image_generation"}],
)

image_data = [
    output.result
    for output in response.output
    if output.type == "image_generation_call"
]

if image_data and len(image_data) > 0:
    image_base64: str = image_data[0]

    with open(img_names[img_idx], "wb") as f:
        f.write(base64.b64decode(image_base64))

print(f"Response id: {response.id}")

response_id = response.id
while img_idx < len(img_prompts) - 1:
    img_idx = img_idx + 1

    # Follow up

    response_fwup = client.responses.create(
        model="gpt-4.1-mini",
        previous_response_id=response_id,
        input=f"**Generate image {img_prompts[img_idx]}",
        tools=[{"type": "image_generation"}],
    )

    image_data_fwup = [
        output.result
        for output in response_fwup.output
        if output.type == "image_generation_call"
    ]

    if image_data_fwup:
        image_base64 = image_data_fwup[0]
        with open(img_names[img_idx], "wb") as f:
            f.write(base64.b64decode(image_base64))

    print(f"Response id: {response_fwup.id}")
    response_id = response_fwup.id