from ultralytics import YOLO

model = YOLO('C:/Users/Usuario/Documents/TFG/backend/src/main/yolov8/yolov8n.pt')  

results = model.train(data='C:/Users/Usuario/Documents/TFG/backend/src/main/yolov8/dataset/mydataset.yaml', 
                      name='mymodel',
                      epochs=300,
                      batch = 8,
                      lr0=0.0001,
                      lrf=0.2,
                      cls=0.6,
                      imgsz=832,
                      label_smoothing=0.1,
                      patience=70,
                      cos_lr=True)

