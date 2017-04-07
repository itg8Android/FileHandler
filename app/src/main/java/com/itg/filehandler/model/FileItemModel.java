package com.itg.filehandler.model;



public class FileItemModel {
    private String name;
    private String type;
    private String fId;
    private FileItemModel model;


    public FileItemModel getModel() {
        return model;
    }

    public void setModel(FileItemModel model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getfId() {
        return fId;
    }

    public void setfId(String fId) {
        this.fId = fId;
    }
}
