package com.example.taskmanager.model.taskdata.attachment;

public class SketchAttachment extends FileAttachment {

    public SketchAttachment(String path) {
        super(path);
        this.attachmentType = AttachmentType.SKETCH;
    }
}