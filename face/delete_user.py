import cv2
import numpy as np
import os
import sys
import shutil

MODEL_PATH = "face/face.recognizer.yml"
TRAINING_DATA_PATH = "face/training_data"

def remove_user_from_model(user_id, model_path, save_path = TRAINING_DATA_PATH):
    """
    Xóa dữ liệu của user_id khỏi mô hình nhận diện khuôn mặt.
    Args:
        user_id (str): ID của người dùng cần xóa.
        model_path (str): Đường dẫn tới file mô hình.
        save_path (str): Đường dẫn thư mục chứa dữ liệu huấn luyện.
    """
    if not os.path.exists(model_path):
        return

    # Nạp mô hình hiện tại
    recognizer = cv2.face.LBPHFaceRecognizer_create()
    recognizer.read(model_path)

    # Đọc dữ liệu từ thư mục huấn luyện
    faces = []
    labels = []
    for folder in os.listdir(save_path):
        folder_path = os.path.join(save_path, folder)
        if not os.path.isdir(folder_path):
            continue

        for file in os.listdir(folder_path):
            file_path = os.path.join(folder_path, file)
            img = cv2.imread(file_path, cv2.IMREAD_GRAYSCALE)
            if img is not None:
                if folder != user_id:  # Chỉ giữ lại dữ liệu không thuộc user_id cần xóa
                    faces.append(img)
                    labels.append(int(folder))

    user_path = os.path.join(save_path, user_id)
    if os.path.exists(user_path):
        try:
            shutil.rmtree(user_path)  # Xóa thư mục và toàn bộ nội dung bên trong
        except Exception as e:
            print(f"Failed to delete folder {user_path}: {e}")

    if len(faces) == 0:
        os.remove(model_path)
        return

    # Huấn luyện lại mô hình với dữ liệu còn lại
    recognizer.train(np.array(faces), np.array(labels))
    recognizer.save(model_path)

    # Xóa thư mục chứa ảnh của user_id


# Xóa dữ liệu của một user trong mô hình
if __name__ == "__main__":
    user_id = sys.argv[1]
    modelPath = sys.argv[2]
    remove_user_from_model(user_id, modelPath, TRAINING_DATA_PATH)
