package com.desgreen.education.siapp.ui.utils.common;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.EnumRotate;
import com.desgreen.education.siapp.backend.model.ZLapTemplate2;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.vaadin.flow.server.StreamResource;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CommonJasperFactory {

    static public StreamResource getStreamResource_Jasper(List<ZLapTemplate2> listLapTempate, Map parameters,  String jasperFile, String outputFileName){
        StreamResource streamResource = null;
        String jasperFilePath = AppPublicService.REPORT_PATH + jasperFile;

        try{
            final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listLapTempate);
//            InputStream jasperFilePath = getClass().getResourceAsStream(jasperFilePath); //static path tidak bisa ditulisi ya
            InputStream stream = new FileInputStream(new File( jasperFilePath) );
            final JasperPrint jasperPrint = JasperFillManager.fillReport(stream, parameters, dataSource);

            byte[] bytes = null;
            try {
                bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            } catch (JRException ex) {
            }
            Objects.requireNonNull(bytes);
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

            streamResource = new StreamResource( outputFileName, () -> bis);
//            streamResource.setContentType("application/vnd.ms-excel");
            streamResource.setContentType("application/pdf");


        } catch(Exception ex){
            ex.printStackTrace();
        }

        return streamResource;
    }

    static public StreamResource getStreamResource_Jrxml(List<ZLapTemplate2> listLapTempate, Map parameters, String jrxmlFile, String outputFileName){
        StreamResource streamResource = null;
        String jasperFilePath = AppPublicService.REPORT_PATH + jrxmlFile;

        try{
            // InputStream jasperFilePath = getClass().getResourceAsStream(jasperFilePath); //static path tidak bisa ditulisi ya
            InputStream stream = new FileInputStream(new File( jasperFilePath) );

            // // Compile the Jasper report from .jrxml to .japser
            JasperReport compiledReport = JasperCompileManager.compileReport(stream);

            // // Fetching the employees from the data source.
            final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listLapTempate);


            // Filling the report with the employee data and additional parameters information.
            final JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, parameters, dataSource);

            byte[] bytes = null;
            try {
                bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            } catch (JRException ex) {
            }
            Objects.requireNonNull(bytes);
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

            streamResource = new StreamResource( outputFileName, () -> bis);
//            streamResource.setContentType("application/vnd.ms-excel");
            streamResource.setContentType("application/pdf");

        } catch(Exception ex){
            ex.printStackTrace();
        }

        return streamResource;

    }

}