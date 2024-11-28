package com.ooops.lms.userInfo;

import java.io.*;

public class UserInfoManagement {
    private static UserInfoManagement instance;
    private String filePath = "accountSetting.txt";

    public static UserInfoManagement getInstance() {
        if(instance==null) {
            instance = new UserInfoManagement();
        }
        return instance;
    }

    public void writeUserInfoToFile(UserInfo userInfo) {
        UserInfo user = new UserInfo(userInfo.getId(), userInfo.getColor(), userInfo.isFaceIdSet());

        String filePath = "accountSetting.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Ghi thông tin user vào file
            writer.write(user.toFileString());
            writer.newLine(); // Thêm dòng mới
            System.out.println("Dữ liệu đã được ghi vào file!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UserInfo findUserById(String searchId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String id = parts[0];
                String color = parts[1];
                boolean isFaceIdSet = Boolean.parseBoolean(parts[2]);

                UserInfo user = new UserInfo(id, color, isFaceIdSet);

                if (user.matchesId(searchId)) {
                    return user;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading from the file.");
            e.printStackTrace();
        }

        return null;
    }

    public void updateUserInfo(UserInfo updatedUser) {
        try {
            // Đọc tất cả thông tin cũ vào bộ nhớ
            StringBuilder fileContent = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String id = parts[0];

                // Nếu ID trùng với người dùng cần cập nhật, thay đổi thông tin
                if (id.equals(updatedUser.getId())) {
                    fileContent.append(updatedUser.toFileString()).append("\n");
                } else {
                    fileContent.append(line).append("\n");
                }
            }
            reader.close();

            // Ghi lại tất cả nội dung đã cập nhật vào file
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(fileContent.toString());
            writer.close();

        } catch (IOException e) {
            System.out.println("An error occurred while updating the file.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //UserInfoManagement.getInstance().writeUserInfoToFile("123","hehe",true);
        System.out.println(UserInfoManagement.getInstance().findUserById("123").getColor());
        UserInfoManagement.getInstance().updateUserInfo(new UserInfo("123","rea",false));
        System.out.println(UserInfoManagement.getInstance().findUserById("123").getColor());

    }
}
