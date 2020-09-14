package com.desgreen.education.siapp.ui.views.master_komp_biaya;

import com.desgreen.education.siapp.backend.model.FKompBiaya;
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;

public class KompBiayaController implements KompBiayaListener {

    protected KompBiayaModel model;
    protected KompBiayaView view;

    @Autowired
    public KompBiayaController(KompBiayaModel model, KompBiayaView view){
//        this.model = new MatPelModel();
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
        view.currentDomain = new FKompBiaya();
        view.showDetails(view.currentDomain);
    }

    @Override
    public void aksiBtnDeleteForm() {

        if (view.currentDomain != null) {
            VerticalLayout messageLayout = new VerticalLayout();
            messageLayout.add(new Label("Apakah akan Menghapus Data"));
            messageLayout.add(new Label(view.currentDomain.getDescription()));
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
        view.grid.deselectAll();
//            viewLogic.deleteProduct(currentProduct);
        model.fKompBiayaJPARepository.delete(view.currentDomain);
        model.mapHeader.remove(view.currentDomain.getId());

        view.dataProvider = DataProvider.ofCollection( model.mapHeader.values() );
        view.dataProvider.refreshAll();
        view.grid.setDataProvider(view.dataProvider);

        UIUtils.showNotification("DATA DELETED!! ");

    }

    @Override
    public void aksiBtnSaveForm() {

        final boolean newDomain = view.currentDomain.isNewDomain();
        if (view.currentDomain != null
                && view.binder.writeBeanIfValid(view.currentDomain)) {

            view.currentDomain = model.fKompBiayaJPARepository.save(view.currentDomain);
            model.mapHeader.put(view.currentDomain.getId(), view.currentDomain);

            if (newDomain) {
                view.dataProvider = DataProvider.ofCollection( model.mapHeader.values() );
                view.dataProvider.refreshAll();
                view.grid.setDataProvider(view.dataProvider);
            } else {
                view.dataProvider.refreshItem(view.currentDomain);
            }

            UIUtils.showNotification("DATA SAVED!! ");
            view.detailsDrawer.hide();
        }



    }

    @Override
    public void aksiBtnCancelForm() {

    }
}
