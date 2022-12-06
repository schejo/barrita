/*package ctrl_;

import Conexion.Conexion;
import DAL.VentasDal;
import MD.VentasMd;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

public class VentasCtrl extends GenericForwardComposer {

    private Connection conexion = null;
    private Conexion cnn = new Conexion();

    private Textbox venCod;
    private Textbox venCor;
    private Textbox venCan;
    private Doublebox venPre;
    private Textbox venUsu;
    private Datebox venFec;
    private Doublebox venDes;
    private Doublebox venTot;
    /*  private Textbox nitUsu;
    private Textbox nomUsu;

    List<VentasMd> allVentas = new ArrayList<VentasMd>();

    private Listbox lb2;
    VentasDal error = new VentasDal();
    private Include rootPagina;

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        //allVentas = error.RSelect();
        //lb2.setModel(new ListModelList(allVentas));
        venUsu.setText(desktop.getSession().getAttribute("USER").toString());
        venCod.focus();
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
        venCod.focus();

    }

    public void onChange$venCod(Event e) throws SQLException, ClassNotFoundException {

        Statement st = null;
        ResultSet rs = null;

        allVentas = error.Correlativo(venCod.getText());
        //allVentas = error.precio(venCod.getText());
        System.out.println("en el controlador CODIGO.. " + venCod.getText());

        for (VentasMd dt : allVentas) {
            venCor.setText(dt.getCorrelativo());
            String abc = "select pro_precio_venta from productos where pro_id  = '" + venCod.getText() + "' ";

            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);
            st = conexion.createStatement();
            System.out.println("en el controlador linea 88.. " + abc);
            rs = st.executeQuery(abc);
            System.out.println("en el controlador linea 91.. " + rs);

            /*String corrs = "";
            while (rs.next()) {
                corrs = rs.getString("corrs");
            }
            venPre.setText(corrs);
            //venPre.setText(" ");
        }

        venCan.setFocus(true);
    }

    public void onChange$venFec(Event e) {
//        venCan.getText();
//        venDes.getText();
        float subtotal = 0;
        float porcentaje = 0;

        porcentaje = (float) (Double.parseDouble(venPre.getText()) * (0.65));
        subtotal = (float) (Double.parseDouble(venPre.getText()) * Double.parseDouble(venCan.getText()));
        System.out.println("PORCENTAJE..: " + porcentaje);

        venDes.setText(String.valueOf(porcentaje));
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

        System.out.println("EXISTENCIA..: " + Integer.parseInt(existe));
        System.out.println("CANTIDAD..: " + Integer.parseInt(venCan.getText()));

        if (Integer.parseInt(existe) > Integer.parseInt(venCan.getText())) {

            error.REGinsert(venCod.getText(), venCor.getText(), venCan.getText(), venPre.getText(),
                    venUsu.getText(), venFec.getText(), venDes.getText(), venTot.getText()
            /*nitUsu.getText(), nomUsu.getText().toUpperCase());

            error.CalculaMov(venCod.getText(), venCor.getText(), venCan.getText());

            //venCod.setText("");
            venCor.setText("");
            venCan.setText("");
            venPre.setText("");
          //  venUsu.setText("");
            venFec.setText("");
            venDes.setText("");
            venTot.setText("");
            

            // allVentas = error.RSelect();
            //  lb2.setModel(new ListModelList(allVentas));
        } else {
            Clients.showNotification("No tiene suficientes <br/> <br/> Existencias! <br/> ",
                    "warning", null, "middle_center", 0);
        }
    }

    public void onClick$btnActualiza(Event e) throws SQLException {
        /*  venCod.setText("");
        venCor.setText("");
        venCan.setText("");
        venPre.setText("");
        venUsu.setText("");
        venFec.setText("");
        venDes.setText("");
        venTot.setText("");
        nitUsu.setText("");
        nomUsu.setText("");
        corUsu.setText("");
        dirUsu.setText("");
        venCan.focus();
    }

    public void onClick$btnDelete(Event e) throws SQLException {

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

    public void onClick$btnSalir() {
        rootPagina.setSrc("/Vistas/Principal.zul");
    }
}
*/