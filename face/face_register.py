import cv2
import sys
import numpy as np
import os

# Đường dẫn tới file mô hình
MODEL_PATH = "face/face_recognizer.yml"
TRAINING_DATA_PATH = "face/training_data"

def collect_faces(user_id, save_path=TRAINING_DATA_PATH, num_samples=300):
    """
    Thu thập ảnh từ camera để đăng ký khuôn mặt.
    Args:
        user_id (str): ID của người dùng.
        save_path (str): Đường dẫn lưu ảnh.
        num_samples (int): Số lượng ảnh cần thu thập.
    """
    if not os.path.exists(save_path):
        os.makedirs(save_path)

    user_path = os.path.join(save_path, user_id)
    if not os.path.exists(user_path):
        os.makedirs(user_path)

    cap = cv2.VideoCapture(0)
    if not cap.isOpened():
        return

    count = 0
    face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + "haarcascade_frontalface_default.xml")

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        faces = face_cascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(100, 100))

        for (x, y, w, h) in faces:
            face = gray[y:y + h, x:x + w]
            face_resized = cv2.resize(face, (200, 200))
            file_path = os.path.join(user_path, f"{count}.jpg")
            cv2.imwrite(file_path, face_resized)
            count += 1

            # Vẽ khung trên camera
            cv2.rectangle(frame, (x, y), (x + w, y + h), (255, 0, 0), 2)

        cv2.imshow("Camera - Thu thập khuôn mặt", frame)

        if cv2.waitKey(1) & 0xFF == ord('q') or count >= num_samples:
            break

    cap.release()
    cv2.destroyAllWindows()

def train_and_update_model(user_id, save_path=TRAINING_DATA_PATH, model_path=MODEL_PATH):
    """
    Đào tạo và cập nhật mô hình nhận diện khuôn mặt.
    Args:
        user_id (str): ID của người dùng.
        save_path (str): Đường dẫn lưu ảnh training.
        model_path (str): Đường dẫn tới mô hình đã tồn tại.
    """
    recognizer = cv2.face.LBPHFaceRecognizer_create()

    # Nạp mô hình hiện có nếu tồn tại
    if os.path.exists(model_path):
        try:
            recognizer.read(model_path)
        except cv2.error as e:
            pass  # Nếu gặp lỗi khi nạp mô hình, không làm gì

    faces = []
    labels = []

    # Tải dữ liệu hiện có
    user_path = os.path.join(save_path, user_id)
    if not os.path.exists(user_path):
        return

    for file in os.listdir(user_path):
        filepath = os.path.join(user_path, file)
        img = cv2.imread(filepath, cv2.IMREAD_GRAYSCALE)
        if img is not None:
            faces.append(img)
            labels.append(int(user_id))  # Sử dụng user_id làm nhãn (label)

    # Kiểm tra dữ liệu đã nạp
    if len(faces) == 0:
        return

    faces = np.array(faces)
    labels = np.array(labels)

    recognizer.update(faces, labels)  # Cập nhật mô hình
    recognizer.save(model_path)

def register_user(user_id):
    """
    Đăng ký một user mới.
    Args:
        user_id (str): ID của người dùng.
    """
    collect_faces(user_id)
    train_and_update_model(user_id)

# Đăng ký khuôn mặt cho người dùng với user_id
if __name__ == "__main__":
    user_id = sys.argv[1]
    register_user(user_id)
