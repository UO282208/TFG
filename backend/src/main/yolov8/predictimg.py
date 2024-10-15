import json
from PIL import Image

from ultralytics import YOLO

import sys

filename = sys.argv[1] 

object_classes = ['Transformador Elevador Subestación', 'Tanque de Expansión', 'Radiador', 'Punto de Conexión', 'Muro Cortafuegos']

model = YOLO("C:/Users/Usuario/Documents/TFG/runs/detect/mymodel5/weights/best.pt")

results = model("C:/Users/Usuario/Documents/TFG/backend/storefiles/" + filename)

object_counts = {class_name: 0 for class_name in object_classes}

for i, r in enumerate(results):
    im_bgr = r.plot()  
    im_rgb = Image.fromarray(im_bgr[..., ::-1])  

    (r.show())

    r.save(filename=f"C:/Users/Usuario/Documents/TFG/backend/storefiles/results/Processed" + filename)

    for box in r.boxes:
        cls_id = box.cls.item() 
        class_name = model.names[cls_id]
        if class_name in object_counts:
            object_counts[class_name] += 1


object_counts_array = [object_counts[class_name] for class_name in object_classes]

output_filename = f"C:/Users/Usuario/Documents/TFG/backend/storefiles/results/ObjectCounts_{filename}.json"
with open(output_filename, 'w') as outfile:
    json.dump(object_counts_array, outfile)