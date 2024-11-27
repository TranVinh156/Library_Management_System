import cv2

# Đường dẫn tới mô hình đã đào tạo
model_path = "face/face_recognizer.yml"

# Tải mô hình nhận diện khuôn mặt
recognizer = cv2.face.LBPHFaceRecognizer_create()
recognizer.read(model_path)

# Đường dẫn tới Haar Cascade
cascade_path = cv2.data.haarcascades + "haarcascade_frontalface_default.xml"
face_cascade = cv2.CascadeClassifier(cascade_path)

def face_login():
    # Mở camera
    cap = cv2.VideoCapture(0)
    if not cap.isOpened():
        return False

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        # Chuyển frame sang grayscale
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

        # Phát hiện khuôn mặt
        faces = face_cascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(100, 100))

        for (x, y, w, h) in faces:
            roi_gray = gray[y:y + h, x:x + w]  # Vùng chứa khuôn mặt
            label, confidence = recognizer.predict(roi_gray)  # So sánh khuôn mặt

            # Kiểm tra độ tin cậy
            if confidence < 50:
                color = (0, 255, 0)  # Màu xanh lá
                cv2.rectangle(frame, (x, y), (x + w, y + h), color, 2)
                cv2.putText(frame, f"Login Success: ID {label}", (x, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.8, color, 2)
                cv2.imshow("Face Recognition", frame)
                cv2.waitKey(2000)  # Hiển thị thông báo trong 2 giây
                cap.release()
                cv2.destroyAllWindows()
                print(f"Login Success: {label}")  # In kết quả với định dạng mong muốn
                return label  # Trả về label của người dùng khi đăng nhập thành công
            else:
                color = (0, 0, 255)  # Màu đỏ
                cv2.rectangle(frame, (x, y), (x + w, y + h), color, 2)
                cv2.putText(frame, f"Unknown: Confidence {confidence:.2f}", (x, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.8, color, 2)

        # Hiển thị frame
        cv2.imshow("Face Recognition", frame)

        # Nhấn 'q' để thoát
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    # Giải phóng tài nguyên và trả về None nếu người dùng thoát
    cap.release()
    cv2.destroyAllWindows()
    return None  # Trả về None nếu đăng nhập không thành công

# Gọi hàm để thực thi
if __name__ == "__main__":
    face_login()
