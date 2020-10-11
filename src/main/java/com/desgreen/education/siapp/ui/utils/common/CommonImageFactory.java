package com.desgreen.education.siapp.ui.utils.common;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.desgreen.education.siapp.backend.model.EnumRotate;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class CommonImageFactory {

    public static BufferedImage autoRotateImage_OraBerhasil(File imageFile) throws Exception {

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

    public static BufferedImage resizeImageGraphics2D_MaxWidth(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage destinationImage = new BufferedImage(originalImage.getHeight(), originalImage.getWidth(), originalImage.getType());

//        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = destinationImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);

        graphics2D.dispose();

        return destinationImage;
    }

    public static BufferedImage autoRotateBufferedImage_BelumBerhasil(final InputStream inputStream) throws Exception{
        int orientation = 1;
        BufferedImage bufferedImageOri = ImageIO.read(inputStream);
        Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
        ExifIFD0Directory exifIFD0 = metadata.getDirectory(ExifIFD0Directory.class);
        orientation = exifIFD0.getInt(ExifIFD0Directory.TAG_ORIENTATION);


        switch (orientation) {
            case 1: // [Exif IFD0] Orientation - Top, left side (Horizontal / normal)
                return null;
            case 6: // [Exif IFD0] Orientation - Right side, top (Rotate 90 CW)
                return rotateImage_CW90(bufferedImageOri);
            case 3: // [Exif IFD0] Orientation - Bottom, right side (Rotate 180)
                return rotateImage_CW180(bufferedImageOri);
//                case 8: // [Exif IFD0] Orientation - Left side, bottom (Rotate 270 CW)
//                    return 270;
            default:
                return null;
        }

    }


    public static BufferedImage autoRotateImage(BufferedImage buffImage, EnumRotate rotate){
//        EnumRotate rotate = getImageRotationSuggestion(inputStream);
        if (rotate.equals(EnumRotate.CW_90)) {
            buffImage = CommonImageFactory.rotateImage_CW90(buffImage);
        }else if (rotate.equals(EnumRotate.CW_180)) {
            buffImage = CommonImageFactory.rotateImage_CW180(buffImage);
        }else if (rotate.equals(EnumRotate.CW_270)) {
            buffImage = CommonImageFactory.rotateImage_CW270(buffImage);
        }

        return buffImage;
    }

    public static EnumRotate getImageRotationSuggestion(InputStream inputStream) {
        int orientation = 1;
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
            ExifIFD0Directory exifIFD0 = metadata.getDirectory(ExifIFD0Directory.class);
            orientation = exifIFD0.getInt(ExifIFD0Directory.TAG_ORIENTATION);
        } catch (Exception ex) {
//            ex.printStackTrace();
        }

//        System.out.println("Oriensasi nya: " + orientation);

        switch (orientation) {
            case 1: // [Exif IFD0] Orientation - Top, left side (Horizontal / normal)
                return EnumRotate.NORMAL;
            case 6: // [Exif IFD0] Orientation - Right side, top (Rotate 90 CW)
                return EnumRotate.CW_90;
            case 3: // [Exif IFD0] Orientation - Bottom, right side (Rotate 180)
                return EnumRotate.CW_180;
            case 8: // [Exif IFD0] Orientation - Left side, bottom (Rotate 270 CW)
                return EnumRotate.CW_270;
            default:
                return EnumRotate.NORMAL;
        }

    }

    public static BufferedImage rotateImage_CW90(BufferedImage bufferedImageOri) {
        BufferedImage bfBlankCanvas = null;
        try {
//            BufferedImage bufferedImageOri = ImageIO.read(inputStream);
            final int width = bufferedImageOri.getWidth();
            final int height = bufferedImageOri.getHeight();

            //Dibalik Width->Height and Height->Width karena akan diputar
            bfBlankCanvas = new BufferedImage(height, width, bufferedImageOri.getType());
            Graphics2D graphics2D = bfBlankCanvas.createGraphics();
            final int newWidth = bfBlankCanvas.getWidth();
            final int newHeight = bfBlankCanvas.getHeight();
            graphics2D.rotate(Math.toRadians(90), 0, 0);
            graphics2D.translate(0, -newWidth);//dibalik ya
            graphics2D.drawImage(bufferedImageOri, 0, 0, null);
            graphics2D.dispose();

        } catch (Exception ex) {
        }

        return bfBlankCanvas;
    }

    public static BufferedImage rotateImage_CW180(BufferedImage bufferedImageOri) {
        BufferedImage bfBlankCanvas = null;
        try {
//            BufferedImage bufferedImageOri = ImageIO.read(inputStream);
            final int width = bufferedImageOri.getWidth();
            final int height = bufferedImageOri.getHeight();

            bfBlankCanvas = new BufferedImage(width, height, bufferedImageOri.getType());
            Graphics2D graphics2D = bfBlankCanvas.createGraphics();
            final int newWidth = bfBlankCanvas.getWidth();
            final int newHeight = bfBlankCanvas.getHeight();
            graphics2D.rotate(Math.toRadians(180), 0, 0);
            graphics2D.translate(-newWidth, -newHeight);//dibalik ya
            graphics2D.drawImage(bufferedImageOri, 0, 0, null);
            graphics2D.dispose();

        } catch (Exception ex) {
        }

        return bfBlankCanvas;
    }

    public static BufferedImage rotateImage_CW270(BufferedImage bufferedImageOri) {
        BufferedImage bfBlankCanvas = null;
        try {
//            BufferedImage bufferedImageOri = ImageIO.read(inputStream);
            final int width = bufferedImageOri.getWidth();
            final int height = bufferedImageOri.getHeight();

            //Dibalik Width->Height and Height->Width karena akan diputar
            bfBlankCanvas = new BufferedImage(height, width, bufferedImageOri.getType());
            Graphics2D graphics2D = bfBlankCanvas.createGraphics();
            final int newWidth = bfBlankCanvas.getWidth();
            final int newHeight = bfBlankCanvas.getHeight();
            graphics2D.rotate(Math.toRadians(270), 0, 0);
            graphics2D.translate(-newHeight, 0);//dibalik ya
            graphics2D.drawImage(bufferedImageOri, 0, 0, null);
            graphics2D.dispose();

        } catch (Exception ex) {
        }

        return bfBlankCanvas;
    }


    public static BufferedImage resizeImageGraphics2D_MaxWidth(final BufferedImage bufferedImageOri, final int maxWidth) {

        int newWidth = bufferedImageOri.getWidth();
        int newHeight = bufferedImageOri.getHeight();
        if (bufferedImageOri.getWidth() >maxWidth) {
            Double reduceScale = (double) (maxWidth) / (double) bufferedImageOri.getWidth();
            newWidth =  (int) (reduceScale * bufferedImageOri.getWidth());
            newHeight = (int) (reduceScale * bufferedImageOri.getHeight());

//            System.out.println("Ukuran: " + bufferedImageOri.getWidth() + " : " + bufferedImageOri.getWidth() +
//                    " >> " + reduceScale + " >> " + newWidth + " : " + newHeight);
        }else {
            return bufferedImageOri;
        }

        BufferedImage bfBlankCanvas = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bfBlankCanvas.createGraphics();
        graphics2D.drawImage(bufferedImageOri, 0, 0, newWidth, newHeight, null);
        graphics2D.dispose();

        return bfBlankCanvas;
    }

    public static BufferedImage resizeImageGraphics2D_MaxHeight(final BufferedImage bufferedImageOri, final int maxHeight) {

        int newWidth = bufferedImageOri.getWidth();
        int newHeight = bufferedImageOri.getHeight();
        if (bufferedImageOri.getHeight() >maxHeight) {
            Double reduceScale = (double) (maxHeight) / (double) bufferedImageOri.getHeight();
            newWidth = (int) (reduceScale * bufferedImageOri.getWidth());
            newHeight = (int) (reduceScale * bufferedImageOri.getHeight());
        }else {
            return bufferedImageOri;
        }
        BufferedImage bfBlankCanvas = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bfBlankCanvas.createGraphics();
        graphics2D.drawImage(bufferedImageOri, 0, 0, newWidth, newHeight, null);
        graphics2D.dispose();

        return bfBlankCanvas;
    }

    public static int getMaxScaleHeight(BufferedImage bufferedImage, int targetWidth){
        return getMaxScaleHeight(bufferedImage.getWidth(), bufferedImage.getHeight(), targetWidth);
    }
    public static int getMaxScaleHeight(int width, int height, int targetWidth){
        Double reduceScale = (double) (targetWidth) / (double) width;
        return (int) (reduceScale * height);
    }

}