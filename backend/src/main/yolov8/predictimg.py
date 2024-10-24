import json
import os
from PIL import Image

from ultralytics import YOLO

import sys

filename = sys.argv[1] 

object_classes = ['Transformador Elevador Subestación', 'Tanque de Expansión', 'Radiador', 'Punto de Conexión', 'Muro Cortafuegos']

try:
    model = YOLO(os.path.join(os.path.dirname(__file__), 'best.pt'))
except Exception as e:
    print(f"An error occurred while loading the model: {e}")

try:
    results = model(os.path.join(os.path.dirname(__file__), "../../../storefiles", filename))
except Exception as e:
    print(f"An error occurred while loading the file: {e}")

object_counts = {class_name: 0 for class_name in object_classes}

for i, r in enumerate(results):
    im_bgr = r.plot()  
    im_rgb = Image.fromarray(im_bgr[..., ::-1])  

    r.save(os.path.join(os.path.join(os.path.dirname(__file__), "../../../storefiles/results/"), f"Processed{filename}"))

    for box in r.boxes:
        cls_id = box.cls.item() 
        class_name = model.names[cls_id]
        if class_name in object_counts:
            object_counts[class_name] += 1


object_counts_array = [object_counts[class_name] for class_name in object_classes]

output_filename = os.path.join(os.path.join(os.path.dirname(__file__), "../../../storefiles/results/"), f"ObjectCounts_{filename}.json")
with open(output_filename, 'w') as outfile:
    json.dump(object_counts_array, outfile)