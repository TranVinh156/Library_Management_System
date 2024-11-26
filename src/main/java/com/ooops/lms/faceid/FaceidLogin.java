package com.ooops.lms.faceid;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class FaceidLogin {
    private static final String PYTHON_PATH = "python";
    private static final String SCRIPT_PATH = "face/face_login.py";

    public static Integer loginFace() {
        try {
            File file = new File(SCRIPT_PATH);
            ProcessBuilder processBuilder = new ProcessBuilder(
                    PYTHON_PATH, file.getAbsolutePath()
            );

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                if (output.toString().contains("Login Success")) {
                    String[] parts = output.toString().split(": ");
                    if (parts.length > 1) {
                        return Integer.parseInt(parts[1]);
                    }
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("Lỗi khi đăng nhập: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        Integer label = loginFace();
        if (label != null) {
            System.out.println("Đăng nhập thành công với ID người dùng: " + label);
        } else {
            System.out.println("Đăng nhập thất bại hoặc xảy ra lỗi.");
        }
    }
}
