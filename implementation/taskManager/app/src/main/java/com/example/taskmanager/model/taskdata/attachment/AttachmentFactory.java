package com.example.taskmanager.model.taskdata.attachment;

import java.util.Arrays;
import java.util.List;

public abstract class AttachmentFactory {
    private static List<String> typesImages = Arrays.asList("png", "jpg", "gif", "bmp");
    private static List<String> typesText = Arrays.asList("txt");
    private static List<String> typesPDF = Arrays.asList("pdf");

    public static FileAttachment createAttachment(AttachmentType type, String path) {

        String fileType = path.substring(path.lastIndexOf('.'));
        if (type.equals(AttachmentType.SKETCH)) {
            return new SketchAttachment(path);
        } else {
            return new FileAttachment(path);
        }
    }
}