//ALMACEN
package ctrl_;

import DAL.ProductosDal;
import MD.ProductosMd;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

public class ProductosCtrl extends GenericForwardComposer {

    private Textbox codPro;
    private Textbox nomPro;
    private Combobox tipPro;
    private Combobox tisPro;
    private Textbox marPro;
    private Combobox prsPro;
    private Doublebox prePro;
    private Doublebox desPro;
    private Doublebox stoPro;
    private Textbox covPro;
    // private Datebox fecPro;
    // private Textbox usuPro;
    // private Datebox modPro;
    // private Textbox umoPro;
    private Combobox medPro;
    private Textbox minPro;
    private Textbox maxPro;
    private Combobox ubiPro;

    private Include rootPagina;
    private Combobox busPro;

    List<ProductosMd> allProductos = new ArrayList<ProductosMd>();
    ProductosDal error = new ProductosDal();//lo copie de usuarios

    /* private Listbox lb2;
    ProductosDal rg = new ProductosDal();
    private Include rootPagina;*/
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        allProductos = error.RSelect();
        
        allProductos = error.allCL();
        busPro.setModel(new ListModelList(allProductos));
        //lb2.setModel(new ListModelList(allProductos));
        // usuPro.setText(desktop.getSession().getAttribute("USER").toString());
        
        codPro.focus();
        desPro.setFormat("###0.##");
        desPro.setLocale(Locale.US);
        prePro.setFormat("###0.##");
        prePro.setLocale(Locale.US);

    }

    public void BuscaItem(String letra, Combobox cb) {
        for (int i = 0; i < cb.getItemCount(); i++) {
            if (letra.equals(cb.getItemAtIndex(i).getValue())) {
                cb.setSelectedIndex(i);
                break;
            }
        }
    }

    SimpleDateFormat p = new SimpleDateFormat("yyyy-MM-dd");
// el que formatea
    SimpleDateFormat f = new SimpleDateFormat("dd/MM/YYYY");

    public void onClick$btnNuevo(Event e) throws SQLException {

        codPro.setText("");
        nomPro.setText("");
        tipPro.setText("");
        tisPro.setText("");
        marPro.setText("");
        prsPro.setText("");
        prePro.setText("");
        desPro.setText("");
        stoPro.setText("");
        covPro.setText("");
        //  fecPro.setText("");
        //  usuPro.setText("");
        //  modPro.setText("");
        // umoPro.setText("");
        medPro.setText("");
        minPro.setText("");
        maxPro.setText("");
        ubiPro.setText("");
        codPro.focus();
        nomPro.focus();

    }

    public void onChange$codPro(Event e) throws SQLException, ParseException, ClassNotFoundException {
        allProductos = error.RSelect();

        codPro.focus();
        desPro.setFormat("###0.##");
        desPro.setLocale(Locale.US);
        prePro.setFormat("###0.##");
        prePro.setLocale(Locale.US);

        if (codPro.getText().isEmpty()) {

            codPro.setText("");
            nomPro.setText("");
            tipPro.setText("");
            tisPro.setText("");
            marPro.setText("");
            prsPro.setText("");
            prePro.setText("");
            desPro.setText("");
            stoPro.setText("");
            covPro.setText("");
            // fecPro.setText("");
            //  usuPro.setText("");
            // modPro.setText("");
            //  umoPro.setText("");
            medPro.setText("");
            minPro.setText("");
            maxPro.setText("");
            ubiPro.setText("");

        } else {

            for (ProductosMd dt : allProductos) {

                if (dt.getCodigo().equals(codPro.getText())) {
                    allProductos = error.RSelect();
                    codPro.setText(dt.getCodigo());
                    nomPro.setText(dt.getDescripcion());
                    BuscaItem(dt.getTipo(), this.tipPro);
                    BuscaItem(dt.getTipo_servicio(), this.tisPro);
                    marPro.setText(dt.getMarca());
                    BuscaItem(dt.getPresentacion(), this.prsPro);
                    prePro.setText(dt.getPrecio_venta());
                    desPro.setText(dt.getDescuento());
                    stoPro.setText(dt.getStock());
                    covPro.setText(dt.getConversion());
                    //  Date date = p.parse(dt.getFecha_alta());
                    //fecPro.setText(f.format(date));
                    //  usuPro.setText(desktop.getSession().getAttribute("USER").toString());
                    /*    if (dt.getFecha_modifica() != null) {
                        date = p.parse(dt.getFecha_modifica());
                        modPro.setText(f.format(date));
                    }*/
                    //     umoPro.setText(dt.getUsuario_modifica());
                    BuscaItem(dt.getMedida(), this.medPro);
                    minPro.setText(dt.getMinimo());
                    maxPro.setText(dt.getMaximo());
                    BuscaItem(dt.getUbicacion(), this.ubiPro);

                }
            }
        }
    }

    public void onClick$btnGuardar(Event e) throws SQLException, ClassNotFoundException, ParseException {
        int op = 0;
        tipPro.getSelectedItem().getValue().toString();

        for (ProductosMd dt : allProductos) {
            if (dt.getCodigo().equals(codPro.getText())) {
                op++;
            }
        }
        if (op == 0) {
            tipPro.getSelectedItem().getValue().toString();
            //  Date date = f.parse(fecPro.getText());

            //  String fechaAlta = p.format(date);
            error.REGinsert(nomPro.getText().toUpperCase(), tipPro.getSelectedItem().getValue().toString(),
                    tisPro.getSelectedItem().getValue().toString(), marPro.getText().toUpperCase(), prsPro.getSelectedItem().getValue().toString(),
                    prePro.getText(), desPro.getText(), stoPro.getText(), covPro.getText(),// fecPro.getText(),
                    /* usuPro.getText(), modPro.getText(), umoPro.getText(),*/ medPro.getSelectedItem().getValue().toString(),
                    minPro.getText(), maxPro.getText(), ubiPro.getSelectedItem().getValue().toString());

            allProductos = error.allCL();
            busPro.setModel(new ListModelList(allProductos));
            codPro.setText("");
            nomPro.setText("");
            tipPro.setText("");
            tisPro.setText("");
            marPro.setText("");
            prsPro.setText("");
            prePro.setText("");
            desPro.setText("");
            stoPro.setText("");
            covPro.setText("");
            // fecPro.setText("");
            //  usuPro.setText("");
            // modPro.setText("");
            // umoPro.setText("");
            medPro.setText("");
            minPro.setText("");
            maxPro.setText("");
            ubiPro.setText("");

            allProductos = error.RSelect();

            //lb2.setModel(new ListModelList(allProductos));
        } else {
            error.REGupdate(codPro.getText(), nomPro.getText().toUpperCase(), tipPro.getSelectedItem().getValue().toString(),
                    tisPro.getSelectedItem().getValue().toString(), marPro.getText().toUpperCase(), prsPro.getSelectedItem().getValue().toString(),
                    prePro.getText(), desPro.getText(), stoPro.getText(), covPro.getText(),// fecPro.getText(),
                    /* usuPro.getText(), modPro.getText(), umoPro.getText(),*/ medPro.getSelectedItem().getValue().toString(),
                    minPro.getText(), maxPro.getText(), ubiPro.getSelectedItem().getValue().toString());

            codPro.setDisabled(false);
            nomPro.setText("");
            tipPro.setText("");
            tisPro.setText("");
            marPro.setText("");
            prsPro.setText("");
            prePro.setText("");
            desPro.setText("");
            stoPro.setText("");
            covPro.setText("");
            //  fecPro.setText("");
            //  usuPro.setText("");
            //  modPro.setText("");
            //  umoPro.setText("");
            medPro.setText("");
            minPro.setText("");
            maxPro.setText("");
            ubiPro.setText("");
            codPro.setText("");
            //  codPro.focus();
            // allProductos = error.RSelect();
            //lb2.setModel(new ListModelList(allProductos));
            allProductos = error.RSelect();
        }
    }

    /*  public void onClick$btnActualiza(Event e) throws SQLException {
         codPro.setText("");
        nomPro.setText("");
        tipPro.setText("");
        medPro.setText("");
        fecPro.setText("");
        usuPro.setText("");
        prePro.setText("");
        ubiPro.setText("");
        salPro.setText("");
        tisPro.setText("");
        nomPro.focus();
    }*/
    public void onClick$btnDelete(Event e) throws SQLException {

        if (!codPro.getText().equals("") && !codPro.getText().equals("")) {
            Messagebox.show("Estas seguro que Deseas Borrar este Registro?",
                    "Question", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event e) throws SQLException, ClassNotFoundException {
                    if (Messagebox.ON_OK.equals(e.getName())) {
                        error.REGdelete(codPro.getText());
                        codPro.setDisabled(false);
                        codPro.setText("");
                        nomPro.setText("");
                        tipPro.setText("");
                        tisPro.setText("");
                        marPro.setText("");
                        prsPro.setText("");
                        prePro.setText("");
                        desPro.setText("");
                        stoPro.setText("");
                        covPro.setText("");
                        //  fecPro.setText("");
                        //  usuPro.setText("");
                        //  modPro.setText("");
                        //  umoPro.setText("");
                        medPro.setText("");
                        minPro.setText("");
                        maxPro.setText("");
                        ubiPro.setText("");

                        allProductos = error.RSelect();
                        //  lb2.setModel(new ListModelList(allProductos));
                    } else if (Messagebox.ON_CANCEL.equals(e.getName())) {
                        Clients.showNotification("REGISTRO NO SE HA <br/> BORRADO <br/>");
                    }
                }
            }
            );

        } else {
            Clients.showNotification("DEBE SELECCIONAR <br/> UN REGISTRO! <br/>",
                    "warning", null, "middle_center", 50);
        }
    }//fi boton eliminar

    public void onClick$btnSalir() {
        rootPagina.setSrc("/Vistas/Principal.zul");
    }

}
