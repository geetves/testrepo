package com.example.geet.myapplication;

public class StorageInfo {

    public final String path;
    public final boolean internal;
    public final boolean readonly;
    public final int display_number;

    StorageInfo(String path, boolean internal, boolean readonly, int display_number) {
        this.path = path;
        this.internal = internal;
        this.readonly = readonly;
        this.display_number = display_number;
    }

    public String getDisplayName() {
        StringBuilder res = new StringBuilder();
        if (internal) {
            res.append("Internal SD card");
        } else if (display_number > 1) {
            res.append("SD card " + display_number);
        } else {
            res.append("SD card");
        }
        if (readonly) {
            res.append(" (Read only)");
        }
        return res.toString();
    }

}
