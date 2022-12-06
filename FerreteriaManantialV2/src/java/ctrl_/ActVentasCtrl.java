package ctrl_;

import DAL.ActVentasDal;
import MD.ActVentasMd;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

public class ActVentasCtrl extends GenericForwardComposer {

    private Textbox venCod;
    private Textbox venCor;
    private Textbox venCan;
    private Doublebox venPre;
    private Textbox venUsu;
    private Doublebox venDes;
    private Doublebox venTot;

    List<ActVentasMd> allActVentas = new ArrayList<ActVentasMd>();

    private Listbox lb2;
    ActVentasDal error = new ActVentasDal();
    private Include rootPagina;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        //   allActCompras = error.RSelect();
        //lb2.setModel(new ListModelList(allMovimientos));
        // nomMov.setText(desktop.getSession().getAttribute("USER").toString());
        venCod.focus();
    }

    public void onClick$btnNuevo(Event e) throws SQLException {
        venCod.setText("");
        venCor.setText("");
        venCan.setText("");
        venPre.setText("");
        venUsu.setText("");
        venDes.setText("");
        venTot.setText("");
        venCod.focus();

    }

    public void onChange$venCor(Event e) throws SQLException, ClassNotFoundException {
        System.out.println("CODIGO.:" + venCod.getText());
        System.out.println("CORRELATIVO.:" + venCor.getText());
        allActVentas = error.REGselect(venCod.getText(), venCor.getText());

        if (allActVentas.isEmpty()) {
            venCod.setText("");
            venCor.setText("");
            venCan.setText("");
            venPre.setText("");
            venUsu.setText("");
            venDes.setText("");
            venTot.setText("");

        } else {
            for (ActVentasMd dt : allActVentas) {
                venCod.setText(dt.getCodigo());
                venCor.setText(dt.getCorrelativo());
                venCan.setText(dt.getCantidad());
                venPre.setText(dt.getPrecio());
                venUsu.setText(dt.getUsuario());
                venDes.setText(dt.getDescuento());
                venTot.setText(dt.getTotal());
            }
        }
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

    public void onClick$btnGuardar(Event e) throws SQLException, ClassNotFoundException {
        int op = 0;

        for (ActVentasMd dt : allActVentas) {
            if (dt.getCodigo().equals(venCod.getText())) {
                op++;
            }
        }

        if (op == 0) {

        } else {
            error.REGupdate(venCod.getText(), venCor.getText(), venCan.getText(), venPre.getText(),
                    /* venUsu.getText(),*/ venDes.getText(), venTot.getText());

            venCod.setDisabled(false);
            venCor.setText("");
            venCan.setText("");
            venPre.setText("");
            //  venUsu.setText("");
            venDes.setText("");
            venTot.setText("");
            venCod.focus();
        }
    }

    public void onClick$btnActualiza(Event e) throws SQLException {
        venCod.setText("");
        venCor.setText("");
        venCan.setText("");
        venPre.setText("");
        //  venUsu.setText("");
        venDes.setText("");
        venTot.setText("");
        venCod.focus();
        venCod.focus();
    }

    public void onClick$btnSalir() {
        rootPagina.setSrc("/Vistas/Principal.zul");
    }
}
