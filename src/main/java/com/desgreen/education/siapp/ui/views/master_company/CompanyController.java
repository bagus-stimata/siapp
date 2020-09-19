package com.desgreen.education.siapp.ui.views.master_company;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.EnumRotate;
import com.desgreen.education.siapp.backend.model.FCompany;
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.desgreen.education.siapp.ui.utils.common.CommonFileFactory;
import com.desgreen.education.siapp.ui.utils.common.CommonImageFactory;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;

@SpringComponent
@UIScope
public class CompanyController implements CompanyListener{

    protected CompanyModel model;
    protected CompanyView view;

    @Autowired
    public CompanyController(CompanyModel model, CompanyView view){
//        this.model = new CompanyModel();
        this.model = model;
        this.view = view;

    }

    @Override
    public void valueChangeListenerSearch(HasValue.ValueChangeEvent e) {
        if (e !=null) {
            view.setFilter(e.getValue().toString());
        }
    }

    @Override
    public void aksiBtnReloadFromDb() {
        view.grid.deselectAll();
        model.initVariableData();
        view.dataProvider = DataProvider.ofCollection(model.mapHeader.values());
        view.dataProvider.refreshAll();
        view.grid.setDataProvider(view.dataProvider);
    }

    @Override
    public void aksiBtnNewForm() {
        view.grid.deselectAll();
        model.currentDomain = new FCompany();
        view.showDetails(model.currentDomain);
    }

    @Override
    public void aksiBtnDeleteForm() {

        if (model.currentDomain != null) {
            VerticalLayout messageLayout = new VerticalLayout();
            messageLayout.add(new Label("Apakah akan Menghapus Data"));
            messageLayout.add(new Label(model.currentDomain.getDescription()));
             ConfirmDialog.createQuestion()
                 .withCaption("KONFIRMASI HAPUS")

                 .withMessage(messageLayout)
                 // .withMessage("Do you want to continue?, Do you want to continue?, \nDo you want to continue?")

                 .withOkButton(() -> {
                        aksiBtnDeleteProcess();

                     }, ButtonOption.caption("YES").icon(VaadinIcon.TRASH))
                 // .withCancelButton(ButtonOption.caption("NO"))
                 .withCancelButton(() -> {
    //                     System.out.println("No. Implement logic here.");

                      }, ButtonOption.focus(), ButtonOption.caption("No").icon(VaadinIcon.EXIT))
                 .withWidthForAllButtons("150px")
                 .open();

        }

    }
    public void aksiBtnDeleteProcess(){

        try {
            File oldFile = new File(AppPublicService.FILE_PATH + model.currentDomain.getLogoImage());
            if (oldFile.exists()) oldFile.delete();
        }catch (Exception ex){}


        view.grid.deselectAll();
//      viewLogic.deleteProduct(currentProduct);
        model.fCompanyJPARepository.delete(model.currentDomain);
        model.mapHeader.remove(model.currentDomain.getId());

        view.dataProvider = DataProvider.ofCollection( model.mapHeader.values() );
        view.dataProvider.refreshAll();
        view.grid.setDataProvider(view.dataProvider);

        /**
         * Komunikasi dan Reload kepada Public Variabel
         */
        model.appPublicService.reloadMapCompany();
        UIUtils.showNotification("DATA DELETED!! ");

    }

    @Override
    public void aksiBtnSaveForm() {
        /**
         * KETIKA SAVE -> MAKA GMBAR HARUSNYA DILIHAT. APAKAH GAMBAR ADA PERUBAHAN ATAU TIDAK
         * JIKA IYA MAKA
         * HAPUS DAN GANTI BARU
         * JIKA TIDAK. DIBIARKAN
         */
        final boolean newDomain = model.currentDomain.isNewDomain();
        if (model.currentDomain != null
                && view.binder.writeBeanIfValid(model.currentDomain)) {

            if ( view.isImageChange && ! newDomain) {
                //Hapus dahulu file Lama
                File oldFile = new File(AppPublicService.FILE_PATH + model.currentDomain.getLogoImage());
                if (oldFile.exists()) oldFile.delete();

                //Set Nama Image::
                model.currentDomain.setLogoImage("_" + System.currentTimeMillis() + "_" + view.imageOuput.getTitle().get());
            }

            model.currentDomain = model.fCompanyJPARepository.save(model.currentDomain);
            model.mapHeader.put(model.currentDomain.getId(), model.currentDomain);

            //Lakukan Upload Image to Disk
            if ( ! model.currentDomain.getLogoImage().equals("") && view.isImageChange && view.buffer.getInputStream() !=null) {
//                CommonFileFactory.writeStreamToFile(view.buffer.getInputStream(), model.currentDomain.getLogoImage());
//                CommonFileFactory.writeStreamToFile(view.imageOuput., model.currentDomain.getLogoImage());

                File targetFile = new File(AppPublicService.FILE_PATH + model.currentDomain.getLogoImage());
                String extention = CommonFileFactory.getExtensionByStringHandling(model.currentDomain.getLogoImage()).get();
                try {
                    BufferedImage buffImage = ImageIO.read(view.buffer.getInputStream());
//                    if (CommonImageFactory.getImageRotationSuggestion(view.buffer.getInputStream()).equals(EnumRotate.CW_90)) {
//                        buffImage = CommonImageFactory.rotateImage_CW90(buffImage);
//                    }else if (CommonImageFactory.getImageRotationSuggestion(view.buffer.getInputStream()).equals(EnumRotate.CW_180)) {
//                        buffImage = CommonImageFactory.rotateImage_CW180(buffImage);
//                    }else if (CommonImageFactory.getImageRotationSuggestion(view.buffer.getInputStream()).equals(EnumRotate.CW_270)) {
//                        buffImage = CommonImageFactory.rotateImage_CW270(buffImage);
//                    }
                    buffImage = CommonImageFactory.autoRotateImage(buffImage,
                            CommonImageFactory.getImageRotationSuggestion(view.buffer.getInputStream()));

                    buffImage = CommonImageFactory.resizeImageGraphics2D_MaxWidth(buffImage, 1028);
                    RenderedImage renderedImage = (RenderedImage) buffImage;

                    if (renderedImage !=null) {
                        ImageIO.write( renderedImage,  extention,  targetFile);
                        System.out.println("Selesai Write");
                    }else {
                        System.out.println("Null Bos...");
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }

            if (newDomain) {
                view.dataProvider = DataProvider.ofCollection( model.mapHeader.values() );
                view.dataProvider.refreshAll();
                view.grid.setDataProvider( view.dataProvider);
            } else {
                view.dataProvider.refreshItem( model.currentDomain);
            }

            /**
             * Komunikasi dan Reload kepada Public Variabel
             */
            model.appPublicService.reloadMapCompany();

            view.detailsDrawer.hide();
            UIUtils.showNotification("DATA SAVED!! ");

        }

    }

    @Override
    public void aksiBtnCancelForm() {

    }
}
