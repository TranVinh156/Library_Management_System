package com.ooops.lms.Settings;

import java.io.*;

public class SettingManger {
    private static final String SETTINGS_FILE = "settings.txt";

    public void saveSettings( Settings settings) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SETTINGS_FILE))) {
            writer.write(settings.toText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Settings loadSettings() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SETTINGS_FILE))) {
            String text = reader.readLine() + "\n" + reader.readLine() + "\n" + reader.readLine() + "\n" + reader.readLine();
            return Settings.fromText(text);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Trả về null khi không tìm thấy file
        }
    }

}
