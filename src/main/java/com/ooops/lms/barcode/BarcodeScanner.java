package com.ooops.lms.barcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

public class BarcodeScanner {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Tải thư viện OpenCV
    }

    /**
     * Giải mã barcode.
     * @param image barcode
     * @return chuỗi kí tự ứng với barcode
     */
    private String decodeBarcode (BufferedImage image) {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
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
        String barcode = "";
        if (!camera.isOpened()) {
            // show alert không thể mở camera
            System.out.println("Camera not opened!");
            return null;
        }

        Mat frame = new Mat();
        while (true) {
            if (camera.read(frame)) {
                BufferedImage image = matToBufferedImage(frame);
                if (image == null) {
                    System.out.println("image is null");
                }
                barcode = decodeBarcode(image);
                if (barcode != null) {
                    break;
                }
            } else {
                // show alert không đọc được ảnh
                break;
            }
        }

        camera.release();
        return barcode;
    }
}
