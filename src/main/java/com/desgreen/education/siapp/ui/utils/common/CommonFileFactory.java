package com.desgreen.education.siapp.ui.utils.common;

import com.desgreen.education.siapp.AppPublicService;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;

public class CommonFileFactory {

    /**
     * FILE OPERATION
     * IMAGE FORM EXTERNAL
     */
    public static File writeStreamToFile(InputStream streamSource, String outputFileName) {
        File file = null;
        try {
            byte[] buffer = new byte[streamSource.available()];
            streamSource.read(buffer);

            File targetFile = new File(AppPublicService.FILE_PATH + outputFileName);
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);

        }catch (Exception ex){}

        return file;
    }

    public static Image generateImage(String sourceImagePath) throws IOException {
        return generateImage(getBytesFromFile(sourceImagePath));
    }

    public static byte[] getBytesFromFile(String sourceImagePath) throws IOException {
        File file = new File(sourceImagePath);
        return Files.readAllBytes(file.toPath());
    }

    public static Image generateImage(byte[] bytes) {
        Objects.requireNonNull(bytes);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        StreamResource sr = new StreamResource("user", () -> bis);
        Image image = new Image(sr, "image");
        return image;
    }

}
