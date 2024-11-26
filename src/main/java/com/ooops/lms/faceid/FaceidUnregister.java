package com.ooops.lms.faceid;

import java.io.File;
import java.util.Scanner;

public class FaceidUnregister {
    private static final String PYTHON_PATH = "python";
    private static final String DELETE_SCRIPT_PATH = "face/delete_user.py";

    public static void unregisterUser(String userId) {
        try {
            File file = new File(DELETE_SCRIPT_PATH);
            ProcessBuilder deleteProcess = new ProcessBuilder(PYTHON_PATH, file.getAbsolutePath(), userId);
            Process process = deleteProcess.start();
            process.waitFor();
            System.out.println("Dữ liệu của người dùng " + userId + " đã bị xóa.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi xóa người dùng: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập user_id để hủy đăng ký: ");
        String userId = scanner.nextLine();
        unregisterUser(userId);
    }
}
