package com.desgreen.education.siapp.ui.views.master_division;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.backend.model.FDivision;
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.desgreen.education.siapp.ui.utils.common.CommonFileFactory;
import com.desgreen.education.siapp.ui.utils.common.CommonImageFactory;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;

public class DivisionController implements DivisionListener {

    protected DivisionModel model;
    protected DivisionView view;

    @Autowired
    public DivisionController(DivisionModel model, DivisionView view){
//        this.model = new DivisionModel();
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
        model.currentDomain = new FDivision();
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
//            viewLogic.deleteProduct(currentProduct);
        model.fDivisionJPARepository.delete(model.currentDomain);
        model.mapHeader.remove(model.currentDomain.getId());

        view.dataProvider = DataProvider.ofCollection( model.mapHeader.values() );
        view.dataProvider.refreshAll();
        view.grid.setDataProvider(view.dataProvider);

        /**
         * REfresh Public Variabel
         */
        model.appPublicService.reloadMapDivision();
        UIUtils.showNotification("DATA DELETED!! ");

    }

    @Override
    public void aksiBtnSaveForm() {

        final boolean newDomain = model.currentDomain.isNewDomain();
        if (model.currentDomain != null
                && view.binder.writeBeanIfValid(model.currentDomain)) {

            if ( view.isImageChange && ! newDomain) {
                //Hapus dahulu file Lama
                File oldFile = new File(AppPublicService.FILE_PATH + model.currentDomain.getLogoImage());
                if (oldFile.exists()) oldFile.delete();

                //Set Nama Image
                model.currentDomain.setLogoImage(model.currentDomain.getFcompanyBean().getId()
                        + "_" + System.currentTimeMillis() + "_" + view.imageOuput.getTitle() );
            }

            model.currentDomain = model.fDivisionJPARepository.save(model.currentDomain);
            model.mapHeader.put(model.currentDomain.getId(), model.currentDomain);

            //Lakukan Upload Image to Disk
            if ( ! model.currentDomain.getLogoImage().equals("") && view.isImageChange) {
//                CommonFileFactory.writeStreamToFile(view.buffer.getInputStream(), model.currentDomain.getLogoImage());
                File targetFile = new File(AppPublicService.FILE_PATH + model.currentDomain.getLogoImage());
                String extention = CommonFileFactory.getExtensionByStringHandling(model.currentDomain.getLogoImage()).get();
                try {
                    BufferedImage buffImage = ImageIO.read(view.buffer.getInputStream());
                    buffImage = CommonImageFactory.autoRotateImage(buffImage,
                            CommonImageFactory.getImageRotationSuggestion(view.buffer.getInputStream()));

                    buffImage = CommonImageFactory.resizeImageGraphics2D_MaxWidth(buffImage, 1028);
                    RenderedImage renderedImage = (RenderedImage) buffImage;

                    if (renderedImage !=null) {
                        ImageIO.write( renderedImage,  extention,  targetFile);
                    }else {
                        System.out.println("Image Division Null Bos...");
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }

            if (newDomain) {
                view.dataProvider = DataProvider.ofCollection( model.mapHeader.values() );
                view.dataProvider.refreshAll();
                view.grid.setDataProvider(view.dataProvider);
            } else {
                view.dataProvider.refreshItem(model.currentDomain);
            }
            /**
             * REfresh Public Variabel
             */
            try {
                model.appPublicService.reloadMapDivision();
                model.appPublicService.mapDivisions.put(model.currentDomain.getId(), model.currentDomain);
            }catch (Exception ex){
            }

            view.detailsDrawer.hide();
            UIUtils.showNotification("DATA SAVED!! ");

        }



    }

    @Override
    public void aksiBtnCancelForm() {

    }
}
