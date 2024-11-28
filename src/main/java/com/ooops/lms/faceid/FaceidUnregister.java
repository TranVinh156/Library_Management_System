package com.ooops.lms.faceid;

import java.io.File;
import java.util.Scanner;

public class FaceidUnregister {
    private static final String PYTHON_PATH = "python";
    private static final String DELETE_SCRIPT_PATH = "face/delete_user.py";
    public static final int ADMIN = 1;
    private static final int USER = 2;

    public static void unregisterUser(String userId, int type) {
        Thread unsignThread = new Thread(() -> {
            try {
                File file = new File(DELETE_SCRIPT_PATH);
                String modelPath = "";
                if (type == ADMIN) {
                    modelPath = "face/admin.yml";
                } else if (type == USER) {
                    modelPath = "face/user.yml";
                }
                ProcessBuilder deleteProcess = new ProcessBuilder(PYTHON_PATH, file.getAbsolutePath(), userId, modelPath);
                Process process = deleteProcess.start();
                process.waitFor();
                System.out.println("Dữ liệu của người dùng " + userId + " đã bị xóa.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Lỗi khi xóa người dùng: " + e.getMessage());
            }
        });
        unsignThread.start();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập user_id để hủy đăng ký: ");
        String userId = scanner.nextLine();
        unregisterUser(userId, 1);
    }
}
