from PIL import Image

from ultralytics import YOLO

model = YOLO("C:/Users/Usuario/Documents/TFG/runs/detect/mymodel/weights/best.pt")

results = model("C:/Users/Usuario/Documents/TFG/backend/storefiles/Transformador144.jpg")

for i, r in enumerate(results):
    im_bgr = r.plot()  
    im_rgb = Image.fromarray(im_bgr[..., ::-1])  

    r.show()

    r.save(filename=f"C:/Users/Usuario/Documents/TFG/backend/storefiles/results{i}.jpg")