package com.itg.filehandler.category;

import java.io.File;



public class FolderModel {
    private String label;
    private File path;

    String getLabel() {
        return label;
    }

    void setLabel(String label) {
        this.label = label;
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }
}
