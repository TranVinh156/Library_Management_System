
# OOOPS - LIBRARY MANAGEMENT SYSTEM
Mục lục
1. [Tác giả](#author)
2. [Giới thiệu](#introduction)
3. [Tính năng nổi bật](#features)
4. [UML Diagram](#uml)
5. [Cách cài đặt](#user-guide)
6. [Demo](#demo)
# Tác giả <a name="author"></a>

Group Ooops:  <img src="src/main/resources/image/icon/Logo.png" alt="Image" width="30" align="right" />

<p align="center">
  <img src="src/main/resources/image/icon/Logo.png" alt="Centered Image"/>
</p>

- Trần Văn Vinh 23021749
- Phạm Việt Hà 23021541
- Nguyễn Đoàn Hoài Thương 23021733

# Giới thiệu <a name="introduction"></a>
Ứng dụng giúp độc giả có thể dễ dàng hơn trong việc đặt trước, mượn, trả sách và thủ thư dễ dàng quản lý sách, các đơn mượn, đặt, độc giả. Độc giả có thể dễ dàng báo cáo các vấn đề tới thủ thư và thủ thư cũng dễ dàng giải quyết các báo cáo đấy.

# Tính năng nổi bật <a name="features"></a>
User:
- Cung cấp cho người đọc những quyển sách cũng hay, đề xuất sách thịnh hành, được yêu thích, xếp hạng các sách theo rate cho bạn đọc thoả sức tìm sách.
- Tìm sách : bọn tớ có tìm kiếm nhanh qua api, tìm kiếm nâng cao, bạn có thể dễ dàng tra quyển sách dù quyển sách đấy không có bản cứng ở thư viện. Khi tìm thấy sách bạn có thể dễ dàng đọc trước qua bản pdf của GoogleBook (nếu có).
- Đặt trước sách: Bạn có thể dễ dàng đặt mượn sách trước, hẹn thời gian với thủ thư sắp xếp hợp lý thời gian để có thể đi nhận sách.
- BookMark: Cậu có thể lưu lại những quyển ưa thích của cậu vào bộ sưu tập của chính cậu nè.
- Comment: Cậu có thể góp ý cho những quyển sách nào cậu đã mượn cũng như có thể thấy được góp ý của người khác về quyển sách nhé.
- Thông tin người dùng :Bạn có thể sửa thông tin, thay đổi ảnh đại diện.
- Lịch sử mượn sách : hiển thị các sách đã mượn, đang mượn, đặt trước sách.
- Đánh giá : Cậu có thể đánh giá sách đã đọc, đánh giá app.
- Báo cáo sự cố : Cậu gặp sự cố gì, hãy báo cáo cho admin qua đây.
- Cài đặt màu sắc : chọn màu sắc yêu thích cho giao diện của bạn (có J97).
- Game : nếu chán các bạn có thể chơi TicTacToe.
- Âm nhạc : vừa nghe nhạc vừa xem sách cùng bọn mình nhé.

Admin:
- Quản lý độc giả: Dễ dàng đọc ,thêm, sửa, xóa độc giả mới vào CSDL. Và khi bạn thêm độc giả vào, độc giả đấy cũng đồng thời được cung cấp tài khoản và sẽ được bọn tớ gửi mail.
- Quản lý sách: Cũng với 4 chức năng thêm, sửa, xóa, đọc. Admin có thể dễ dàng thêm vào thư viện thông qua API hoặc cũng có thể thêm chay vào. Ngoài ra hỗ trợ chức năng quét mã vạch giúp cho công việc của bạn dễ dàng hơn.
- Quản lý đặt/mượn sách: Hai chức năng này cũng được tích hợp quét mã vạch, kết hợp bảng gợi ý giúp bạn hoàn thành công việc này một cách nhẹ nhàng.
- Các bảng quản lý cũng sẽ được tìm kiếm theo nhiều tiêu chí, lọc ra các đơn mượn/đặt sách, độc giả mà bạn muốn tìm một các nhanh chóng.
- Báo cáo theo tháng: Bạn có thể dễ dàng xem biểu đồ mượn sách của tất cả các tháng tùy theo bạn muốn.
- Report: Tổng hợp các vấn đề người dùng báo cáo lên hệ thống, qua đó bạn có thể thấy và quản lý các vấn đề một cách dễ dàng.
- Gmail: Bọn tớ tích hợp email vào Admin để bạn có thể nhanh chóng và dễ dàng gửi mail cho độc giả.

Ngoài ra, bọn tớ còn có một số chức năng nổi bật khác như:
- Đổi chủ đề giao diện: Với 4 màu Basic, Dark, Pink, Gold. Giúp người dùng dễ dàng thay đổi theo thẩm mỹ của mình.
- FaceID: Bọn tớ còn tích hợp quét ID để có thể giúp cậu đăng nhập một cách nhanh chóng và bảo mật.

# UML Diagram <a name="uml"></a>
![Demo](diagram/main.png)

# Cách sử dụng <a name="user-guide"></a>
* Cài đặt JDK 8 tại [đây](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html), có thể sử dụng Intellij để chạy chương trình viết bằng Java.
1. Clone project từ repository
2. Tải file database bọn tớ về và import đã nhé.
3. Mở project trong IDE Intellij 
4. Tìm đến file database thay đổi username, password và link đường dẫn đến database của cậu nhé. 
5. Tìm đến main/DictionaryApplication và run.

* Cài đặt OpenCV 
1. Cài đặt để scan FaceID :
- Bạn hãy cài các thư viện CV2, numpy của python.
- Bạn có thể xem hướng dẫn tại [đây](https://www.youtube.com/watch?v=KwUMAx_ipOk&t=491s).
2. Cài đặt để Scan BarCoed: 
- Link opencv tại [đây](https://opencv.org/releases/).
- Sau khi tải về , bạn hãy tự cấu hình hoặc làm theo hướng dẫn ở [đây](https://docs.google.com/document/d/1FxaHaLGKOYtVmnnE-fxTZHGzhJbVd94J2fnf_ncPfK4/edit?usp=sharing).

# DEMO <a name="demo"></a>

* Login

![login](https://drive.google.com/uc?id=1Ew0F191wNPKPuUvkakA3cB9HYHwk3e3u)

* User

![login](https://drive.google.com/uc?id=11BFTUEcqnS3--fssb-84D2L2FHDzYUcC)

* Admin

![login](https://drive.google.com/uc?id=1tDjWxD-jCFHYqnLBxzjhHY4WS9DXIVk_)

* Phát triển trong tương lai : nạp VIP

![login](https://drive.google.com/uc?id=1iLwfzJpJUzg7FxOyGZsPhZfqQURHMLhb)

* FaceID và book Scancer : Bạn hãy down về và trải nghiệm nhé!
