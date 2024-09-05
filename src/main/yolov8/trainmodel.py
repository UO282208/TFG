from ultralytics import YOLO

# Loading my custom model
model = YOLO('yolov8n.yaml')  # or load a pretrained model: YOLO('yolov8n.pt')

# Training the model
model.train(
    data='dataset/mydataset.yaml',  # path to the dataset.yaml file
    epochs=100,                   # number of epochs to train
    batch=16,                     # batch size
    imgsz=640,                    # image size for training
    name='mymodel',               # name for the run
    workers=2,                    # number of data loading workers
)