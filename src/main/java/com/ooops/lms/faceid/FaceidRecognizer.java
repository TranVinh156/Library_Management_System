package com.ooops.lms.faceid;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class FaceidRecognizer {
    private static final String PYTHON_PATH = "python";  // Đảm bảo rằng Python đã được cài và đường dẫn chính xác
    private static final String REGISTER_SCRIPT_PATH = "face/face_register.py";  // Đường dẫn đến script Python của bạn

    public static final int ADMIN = 1;
    public static final int USER = 2;
    public static void registerUser(String userId, int type) {
        Thread trainThread = new Thread(() -> {
            try {
                String modelPath = "";
                if (type == ADMIN) {
                    modelPath = "face/admin.yml";
                } else if (type == USER) {
                    modelPath = "face/user.yml";
                }
                File file = new File(REGISTER_SCRIPT_PATH);
                ProcessBuilder processBuilder = new ProcessBuilder(PYTHON_PATH, file.getAbsolutePath(), userId, modelPath);
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();
                printProcessOutput(process);
            } catch (IOException e) {
                System.err.println("Lỗi khi gọi script Python: " + e.getMessage());
            }
        });
        trainThread.start();
    }

    private static void printProcessOutput(Process process) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Lỗi khi đọc output từ script Python: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        //FaceidRecognizer faceRecognition = new FaceidRecognizer();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập user_id cần đăng ký: ");
        String userId = scanner.nextLine();

        FaceidRecognizer.registerUser(userId, 1);
    }
}
