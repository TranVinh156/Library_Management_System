package com.ooops.lms.Settings;

public class Settings {
    private int volume;
    private String Mode;
    private String Password;
    private boolean haveFaceID;

    public Settings(String Mode, int volume, String Password, boolean haveFaceID) {
        this.Mode = Mode;
        this.volume = volume;
        this.Password = Password;
        this.haveFaceID = haveFaceID;
    }
    public int getVolume() {
        return volume;
    }
    public void setVolume(int volume) {
        this.volume = volume;
    }
    public String getMode() {
        return Mode;
    }
    public void setMode(String Mode) {
        this.Mode = Mode;
    }
    public String getPassword() {
        return Password;
    }
    public void setPassword(String Password) {
        this.Password = Password;
    }
    public boolean isHaveFaceID() {
        return haveFaceID;
    }
    public void setHaveFaceID(boolean haveFaceID) {
        this.haveFaceID = haveFaceID;
    }

    public String toText() {
        return String.format("%s\n%d\n%s\n%b\n", Mode, volume, Password, haveFaceID);
    }

    public static Settings fromText(String text) {
        String[] parts = text.split("\n");
        String mode = parts[0];
        int volume = Integer.parseInt(parts[1]);
        String Password = parts[2];
        boolean haveFaceID = Boolean.parseBoolean(parts[3]);
        return new Settings(mode, volume, Password, haveFaceID);
    }
}
