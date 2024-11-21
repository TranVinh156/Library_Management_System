package com.ooops.lms.barcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class BarcodeScanner extends JPanel {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Tải thư viện OpenCV
    }

    private Hashtable<DecodeHintType, Object> hints;

    public BarcodeScanner() {
        hints = new Hashtable<>();
        hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_128));
    }

    // camera
    private BufferedImage drawScanningFrame(BufferedImage image, int scanLineY) {
        Graphics2D g = image.createGraphics();

        // Tô màu nền tối bên ngoài vùng quét
        g.setColor(new Color(0, 0, 0, 150)); // Màu đen với độ trong suốt
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int rectX = imageWidth / 4;
        int rectY = imageHeight / 4;
        int rectWidth = imageWidth / 2;
        int rectHeight = imageHeight / 2;

        // Vẽ các phần bên ngoài khu vực quét
        g.fillRect(0, 0, imageWidth, rectY); // Phần trên vùng quét
        g.fillRect(0, rectY, rectX, rectHeight); // Phần bên trái vùng quét
        g.fillRect(rectX + rectWidth, rectY, rectX, rectHeight); // Phần bên phải vùng quét
        g.fillRect(0, rectY + rectHeight, imageWidth, rectY); // Phần dưới vùng quét

        // Vẽ các góc của khung viền cho khu vực quét
        g.setColor(new Color(0, 255, 255));
        int frameThickness = 5;
        int frameLength = 40;

        // Góc trên trái
        g.fillRect(rectX, rectY, frameLength, frameThickness);
        g.fillRect(rectX, rectY, frameThickness, frameLength);

        // Góc trên phải
        g.fillRect(rectX + rectWidth - frameLength, rectY, frameLength, frameThickness);
        g.fillRect(rectX + rectWidth - frameThickness, rectY, frameThickness, frameLength);

        // Góc dưới trái
        g.fillRect(rectX, rectY + rectHeight - frameThickness, frameLength, frameThickness);
        g.fillRect(rectX, rectY + rectHeight - frameLength, frameThickness, frameLength);

        // Góc dưới phải
        g.fillRect(rectX + rectWidth - frameLength, rectY + rectHeight - frameThickness, frameLength, frameThickness);
        g.fillRect(rectX + rectWidth - frameThickness, rectY + rectHeight - frameLength, frameThickness, frameLength);

        // Vẽ thanh quét màu xanh di chuyển
        g.setColor(new Color(0, 255, 0, 128)); // Màu xanh với độ trong suốt
        g.fillRect(rectX, scanLineY, rectWidth, 3); // Thanh mỏng đi từ trên xuống

        g.dispose();
        return image;
    }

    /**
     * Giải mã barcode.
     * @param image barcode
     * @return chuỗi kí tự ứng với barcode
     */
    private String decodeBarcode (BufferedImage image) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap, hints);
            return result.getText();
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Chuyển từ mat sang BufferedImage.
     * @param mat
     * @return
     */
    private BufferedImage matToBufferedImage(Mat mat) {
        if (mat.empty()) {
            System.out.println("Mat trống, không có dữ liệu từ camera.");
            return null;
        }

        // Chuyển đổi Mat từ BGR (định dạng của OpenCV) sang RGB
        Mat convertedMat = new Mat();
        Imgproc.cvtColor(mat, convertedMat, Imgproc.COLOR_BGR2RGB);

        // Chuyển đổi Mat thành BufferedImage
        int width = convertedMat.width();
        int height = convertedMat.height();
        int channels = convertedMat.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        convertedMat.get(0, 0, sourcePixels);

        // Tạo BufferedImage từ mảng byte
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

        return image;
    }

    public String scanBarcodeFromImage(String filePath) {
        try {
            BufferedImage image = ImageIO.read(new File(filePath));
            return decodeBarcode(image);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String scanBarcodeFromCamera() {
        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.out.println("Không thể mở camera!");
            return null;
        }

        final boolean[] flag = {true};

        JFrame frame = new JFrame("Barcode Scanner");
        frame.setContentPane(this);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Tại đây bạn có thể xử lý việc khi đóng cửa sổ mà không thoát chương trình
                // Ví dụ: thông báo, đóng camera, v.v.
                flag[0] = false;
                System.out.println("Cửa sổ đang được đóng, nhưng chương trình vẫn tiếp tục.");
                return;
            }
        });
        frame.setVisible(true);

        Mat frameMat = new Mat();
        int scanLineY = 150;
        boolean movingDown = true;
        int frameCount = 0;
        String barcode = "";

        while (flag[0]) {
            if (camera.read(frameMat)) {
                // Giảm kích thước khung hình để tăng tốc độ xử lý
                Mat resizedFrame = new Mat();
                Imgproc.resize(frameMat, resizedFrame, new Size(640, 480));
                BufferedImage image = matToBufferedImage(resizedFrame);

                if (image == null) {
                    return null;
                }

                frameCount++;
                if (movingDown) {
                    scanLineY += 5;
                    if (scanLineY >= image.getHeight() / 2 + image.getHeight() / 4 - 10) movingDown = false;
                } else {
                    scanLineY -= 5;
                    if (scanLineY <= image.getHeight() / 4) movingDown = true;
                }

                if (frameCount % 5 == 0) {
                    // Giảm số lần cập nhật giao diện
                    BufferedImage displayImage = drawScanningFrame(image, scanLineY);
                    this.getGraphics().drawImage(displayImage, 0, 0, this.getWidth(), this.getHeight(), null);

                    // Giới hạn giải mã vùng quét
                    BufferedImage croppedImage = image.getSubimage(image.getWidth() / 4, image.getHeight() / 4, image.getWidth() / 2, image.getHeight() / 2);
                    barcode = decodeBarcode(croppedImage);

                    if (barcode != null) {
                        JOptionPane.showMessageDialog(this, "Barcode: " + barcode);
                        break;
                    }
                }

                try {
                    Thread.sleep(10); // Giảm thời gian chờ
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Không đọc được khung hình.");
                break;
            }
        }

        camera.release();
        frame.dispose();
        return barcode;
    }
}
