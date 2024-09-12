from ultralytics import YOLO

model = YOLO('C:/Users/Usuario/Documents/TFG/backend/src/main/yolov8/yolov8n.pt')  

results = model.train(data='C:/Users/Usuario/Documents/TFG/backend/src/main/yolov8/dataset/mydataset.yaml', name='mymodel')