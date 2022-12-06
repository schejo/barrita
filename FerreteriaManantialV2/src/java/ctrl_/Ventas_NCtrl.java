/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctrl_;

import DAL.VentasServiciosDal;
import MD.VentasServiciosMd;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

/**
 *
 * @author hp
 */
public class Ventas_NCtrl extends GenericForwardComposer{
     private Textbox venCod;
    private Textbox venCor;
    private Textbox venCan;
    private Doublebox venPre;
    private Textbox venUsu;
    private Datebox venFec;
    private Textbox venDes;
    private Textbox venTot;
    private Combobox venPag;

    List<VentasServiciosMd> allVentas = new ArrayList<VentasServiciosMd>();

    private Listbox lb2;
    VentasServiciosDal error = new VentasServiciosDal();
    private Include rootPagina;

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        //allVentas = error.RSelect();
        //lb2.setModel(new ListModelList(allVentas));
        venUsu.setText(desktop.getSession().getAttribute("USER").toString());
        venCod.focus();
        venPre.setFormat("###0.##");
        venPre.setLocale(Locale.US);
    }

    public void onClick$btnNuevo(Event e) throws SQLException {
        venCod.setText("");
        venCor.setText("");
        venCan.setText("");
        venPre.setText("");
        //venUsu.setText("");
        venFec.setText("");
        venDes.setText("");
        venTot.setText("");
        venPag.setText("");
        venCod.focus();

    }

    public void onChange$venCod(Event e) throws SQLException, ClassNotFoundException {

        allVentas = error.Correlativo(venCod.getText());
        for (VentasServiciosMd dt : allVentas) {
            venCor.setText(dt.getCorrelativo());
            venPre.setText(dt.getPrecio());
        }

        venCan.setFocus(true);
    }

 public void onChange$venDes(Event e) {
//        venCan.getText();
//        venDes.getText();
        float subtotal = 0;
        float subtotal2 = 0;
        //double descuento = Integer.parseInt(venDes.getText());
        //descuento = 0;
        subtotal = (float) (Double.parseDouble(venPre.getText()) * Double.parseDouble(venCan.getText()));
        if (venDes.getText().equals("") || (Double.parseDouble(venDes.getText())) == 0) {
            subtotal2 = (float) (Double.parseDouble(venPre.getText()) * Double.parseDouble(venCan.getText()));
            venTot.setText(String.valueOf(subtotal2));

            if (venDes.getText().equals("")) {
                venDes.setText("0");

            }

        } else {
            float total = (float) (subtotal - Double.parseDouble(venDes.getText()));
            venTot.setText(String.valueOf(total));
        }
    }
 
 
public void onChange$venFec(Event e) {
//        venCan.getText();
//        venDes.getText();
        float subtotal = 0;
        float porcentaje = 0;

        porcentaje = (float) (Double.parseDouble(venPre.getText()) * (0.65));
        subtotal = (float) (Double.parseDouble(venPre.getText()) * Double.parseDouble(venCan.getText()));
        System.out.println("PORCENTAJE..: " + porcentaje);

       // venDes.setText(String.valueOf(porcentaje));
        System.out.println("PORCENTAJE..: " + venDes);

        if (venDes.getText().equals("") || venDes.equals("0")) {
            venTot.setText(String.valueOf(subtotal));
        } else {
            float total = (float) (subtotal - Double.parseDouble(venDes.getText()));
            venTot.setText(String.valueOf(total));
        }
    }

    public void onClick$btnGuardar(Event e) throws SQLException, ClassNotFoundException {
        int op = 0;
        String existe = "";
        existe = error.Existencia(venCod.getText());
        //String usuario = (String) desktop.getSession().getAttribute("USER");


       // System.out.println("EXISTENCIA..: " + Integer.parseInt(existe));
   //     System.out.println("CANTIDAD..: " + Integer.parseInt(venCan.getText()));
        if (Integer.parseInt(existe) >= Integer.parseInt(venCan.getText())) {

            error.REGinsert(venCod.getText(), venCor.getText(), venCan.getText(), venPre.getText(),
                            venUsu.getText(), venFec.getText(), venDes.getText(), venTot.getText());

            error.CalculaMov(venCod.getText(), venCor.getText(), venCan.getText());

            venCod.setText("");
            venCor.setText("");
            venCan.setText("");
            venPre.setText("");
           // venUsu.setText("");
            venFec.setText("");
            venDes.setText("");
            venTot.setText("");
                //venPag.setText("");

            // allVentas = error.RSelect();
            //  lb2.setModel(new ListModelList(allVentas));
        } else {
            Clients.showNotification("No tiene suficientes <br/> <br/> Existencias! <br/> ",
                    "warning", null, "middle_center", 0);
        }
    }

    public void onClick$btnActualiza(Event e) throws SQLException {
        venCod.setText("");
        venCor.setText("");
        venCan.setText("");
        venPre.setText("");
       // venUsu.setText("");
        venFec.setText("");
        venDes.setText("");
        venTot.setText("");
        venPag.setText("");
        venCod.focus();
    }

    /*public void onClick$btnDelete(Event e) throws SQLException {

        if (!venCod.getText().equals("") && !venCod.getText().equals("")) {
            Messagebox.show("Estas seguro que Deseas Borrar este Registro?",
                    "Question", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event e) throws SQLException, ClassNotFoundException {
                    if (Messagebox.ON_OK.equals(e.getName())) {
                        error.REGdelete(venCod.getText());
                        venCod.setDisabled(false);
                        venCor.setText("");
                        venCan.setText("");
                        venPre.setText("");
                        venUsu.setText("");
                        venFec.setText("");
                        venDes.setText("");
                        venTot.setText("");
                        venCod.setText("");

                        allVentas = error.RSelect();
                        lb2.setModel(new ListModelList(allVentas));
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
    }
*/
    
    public void onClick$btnSalir() {
        rootPagina.setSrc("/Vistas/Principal.zul");
    }
    
}
