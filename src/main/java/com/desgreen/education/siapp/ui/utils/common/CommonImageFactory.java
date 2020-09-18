package com.desgreen.education.siapp.ui.utils.common;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class CommonImageFactory {

    public static BufferedImage autoRotateImage(File imageFile) throws Exception{

        BufferedImage originalImage = ImageIO.read(imageFile);
        Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
        ExifIFD0Directory exifIFD0Directory = metadata.getDirectory(ExifIFD0Directory.class);
        JpegDirectory jpegDirectory = (JpegDirectory) metadata.getDirectory(JpegDirectory.class);

        int orientation = 1;
        try {
            orientation = exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        int width = jpegDirectory.getImageWidth();
        int height = jpegDirectory.getImageHeight();

        AffineTransform affineTransform = new AffineTransform();

        switch (orientation) {
            case 1:
                break;
            case 2: // Flip X
                affineTransform.scale(-1.0, 1.0);
                affineTransform.translate(-width, 0);
                break;
            case 3: // PI rotation
                affineTransform.translate(width, height);
                affineTransform.rotate(Math.PI);
                break;
            case 4: // Flip Y
                affineTransform.scale(1.0, -1.0);
                affineTransform.translate(0, -height);
                break;
            case 5: // - PI/2 and Flip X
                affineTransform.rotate(-Math.PI / 2);
                affineTransform.scale(-1.0, 1.0);
                break;
            case 6: // -PI/2 and -width
                affineTransform.translate(height, 0);
                affineTransform.rotate(Math.PI / 2);
                break;
            case 7: // PI/2 and Flip
                affineTransform.scale(-1.0, 1.0);
                affineTransform.translate(-height, 0);
                affineTransform.translate(0, width);
                affineTransform.rotate(3 * Math.PI / 2);
                break;
            case 8: // PI / 2
                affineTransform.translate(0, width);
                affineTransform.rotate(3 * Math.PI / 2);
                break;
            default:
                break;
        }

        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage destinationImage = new BufferedImage(originalImage.getHeight(), originalImage.getWidth(), originalImage.getType());
        destinationImage = affineTransformOp.filter(originalImage, destinationImage);
        return destinationImage;
    }
    public static BufferedImage resizeImageScaledInstant(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        java.awt.Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, java.awt.Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    public static BufferedImage resizeImageGraphics2D(BufferedImage originalImage, int targetWidth, int targetHeight, double rotationDegree) throws IOException {
//        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(45));
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(90));
        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);

        BufferedImage destinationImage = new BufferedImage(originalImage.getHeight(), originalImage.getWidth(), originalImage.getType());
        destinationImage = affineTransformOp.filter(originalImage, destinationImage);

//        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = destinationImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);

        graphics2D.dispose();

        return destinationImage;
    }

    public static double getRotationDegreeRecommended(InputStream inputStream) {
        double degree = 0;
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
            ExifIFD0Directory exifIFD0 = metadata.getDirectory(ExifIFD0Directory.class);
            int orientation = exifIFD0.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            System.out.println("Oriensasi nya: " + orientation);

//            int orientation =1;
            switch (orientation) {
                case 1: // [Exif IFD0] Orientation - Top, left side (Horizontal / normal)
                    return 0;
                case 6: // [Exif IFD0] Orientation - Right side, top (Rotate 90 CW)
                    return 90;
                case 3: // [Exif IFD0] Orientation - Bottom, right side (Rotate 180)
                    return 180;
                case 8: // [Exif IFD0] Orientation - Left side, bottom (Rotate 270 CW)
                    return 270;
            }

        }catch (Exception ex) {}

        return degree;
    }

}
