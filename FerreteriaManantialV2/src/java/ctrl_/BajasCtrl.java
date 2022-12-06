package ctrl_;

import DAL.BajasDal;
import MD.BajasMd;
import java.sql.SQLException;
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

public class BajasCtrl extends GenericForwardComposer {

    private Textbox bajCod;
    private Textbox bajCor;
    private Textbox bajCan;
    private Doublebox bajPre;
    private Textbox bajUsu;
    private Datebox bajFec;
    private Doublebox bajTot;

    List<BajasMd> allBajas = new ArrayList<BajasMd>();

    private Listbox lb2;
    BajasDal error = new BajasDal();
    private Include rootPagina;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        allBajas = error.RSelect();
        bajUsu.setText(desktop.getSession().getAttribute("USER").toString());
        bajCod.focus();
    }

    public void onClick$btnNuevo(Event e) throws SQLException {
        bajCod.setText("");
        bajCor.setText("");
        bajCan.setText("");
        bajPre.setText("");
      //  bajUsu.setText("");
        bajFec.setText("");
        bajTot.setText("");
        bajCod.focus();

    }

    public void onChange$bajCod(Event e) throws SQLException, ClassNotFoundException {

          allBajas = error.Correlativo(bajCod.getText());
        for (BajasMd dt : allBajas) {
            bajCor.setText(dt.getCorrelativo());
            bajPre.setText(dt.getPrecio());
        }

        bajCan.setFocus(true);
    }

       public void onChange$bajFec(Event e) {
        float subtotal = 0;
        subtotal = (float) (Double.parseDouble(bajPre.getText()) * Double.parseDouble(bajCan.getText()));
        if (bajFec.getText().equals("") || bajFec.equals("0")) {
            bajTot.setText(String.valueOf(subtotal));
        } else {
         //   float total = (float) (subtotal - Double.parseDouble(bajPre.getText()));
            //subtotal = (float) (Double.parseDouble(bajPre.getText()) * Double.parseDouble(bajCan.getText()));
            bajTot.setText(String.valueOf(subtotal));
        }
    }

   public void onClick$btnGuardar(Event e) throws SQLException, ClassNotFoundException {
        int op = 0;
        String existe = "";
        existe = error.Existencia(bajCod.getText());

        System.out.println("EXISTENCIA..: " + Integer.parseInt(existe));
        System.out.println("CANTIDAD..: " + Integer.parseInt(bajCan.getText()));
        if (Integer.parseInt(existe) >= Integer.parseInt(bajCan.getText())) {

            error.REGinsert(bajCod.getText(), bajCor.getText(), bajCan.getText(), bajPre.getText(),
                            bajUsu.getText(), bajFec.getText(), bajTot.getText());

            error.CalculaMov(bajCod.getText(), bajCor.getText(), bajCan.getText());

            bajCod.setText("");
            bajCor.setText("");
            bajCan.setText("");
            bajPre.setText("");
            bajUsu.setText("");
            bajFec.setText("");
            bajTot.setText("");

            // allVentas = error.RSelect();
            //  lb2.setModel(new ListModelList(allVentas));
        } else {
            Clients.showNotification("No tiene suficientes <br/> <br/> Existencias! <br/> ",
                    "warning", null, "middle_center", 0);
        }
    }
   
    public void onClick$btnActualiza(Event e) throws SQLException {
       /* bajCod.setText("");
        bajCor.setText("");
        bajCan.setText("");
        bajPre.setText("");
        bajUsu.setText("");
        bajFec.setText("");
        bajDes.setText("");
        bajTot.setText("");*/
        bajCan.focus();
    }

    public void onClick$btnDelete(Event e) throws SQLException {

        if (!bajCod.getText().equals("") && !bajCod.getText().equals("")) {
            Messagebox.show("Estas seguro que Deseas Borrar este Registro?",
                    "Question", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event e) throws SQLException, ClassNotFoundException {
                    if (Messagebox.ON_OK.equals(e.getName())) {
                        error.REGdelete(bajCod.getText());
                        bajCod.setDisabled(false);
                        bajCor.setText("");
                        bajCan.setText("");
                        bajPre.setText("");
                        bajUsu.setText("");
                        bajFec.setText("");
                     //   bajDes.setText("");
                        bajTot.setText("");

                        allBajas = error.RSelect();
                        lb2.setModel(new ListModelList(allBajas));
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
