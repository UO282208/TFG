from ultralytics import YOLO

# Loading my custom model
model = YOLO('yolov8n.yaml')  # or load a pretrained model: YOLO('yolov8n.pt')

# Training the model
model.train(
    data='dataset/dataset.yaml',  # path to the dataset.yaml file
    epochs=100,                   # number of epochs to train
    batch=16,                     # batch size
    imgsz=640,                    # image size for training
    name='custom_yolov8_model',   # name for the run
    workers=2,                    # number of data loading workers
)