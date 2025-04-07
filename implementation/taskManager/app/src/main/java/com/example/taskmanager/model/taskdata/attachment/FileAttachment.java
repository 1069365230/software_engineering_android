package com.example.taskmanager.model.taskdata.attachment;

public class FileAttachment {

    private String path;
    private String name;
    protected AttachmentType attachmentType;

    public FileAttachment(String path) {
        this.path = path;
        this.name = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
        this.attachmentType = AttachmentType.FILE;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}