package ctrl_;

import DAL.ActComprasDal;
import MD.ActComprasMd;
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

public class ActComprasCtrl extends GenericForwardComposer {

    private Textbox codMov;
    private Textbox corMov;
    private Doublebox preMov;
    private Textbox nomMov;
    private Textbox canMov;

    List<ActComprasMd> allActCompras = new ArrayList<ActComprasMd>();

    private Listbox lb2;
    ActComprasDal error = new ActComprasDal();
    private Include rootPagina;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
     //   allActCompras = error.RSelect();
        //lb2.setModel(new ListModelList(allMovimientos));
        // nomMov.setText(desktop.getSession().getAttribute("USER").toString());
        codMov.focus();
    }

    public void onClick$btnNuevo(Event e) throws SQLException {
        codMov.setText("");
        corMov.setText("");
        preMov.setText("");
      //  ingMov.setText("");
      //  venMov.setText("");
        nomMov.setText("");
        canMov.setText("");
        codMov.focus();

    }

    public void onChange$corMov(Event e) throws SQLException, ClassNotFoundException {
      System.out.println("CODIGO.:" +codMov.getText());
      System.out.println("CORRELATIVO.:" +corMov.getText());
      allActCompras = error.REGselect (codMov.getText(),corMov.getText());
      //preMov.setFocus(true);
      if (allActCompras.isEmpty()) {
        codMov.setText("");
        corMov.setText("");
        preMov.setText("");
//        ingMov.setText("");
//        venMov.setText("");
        nomMov.setText("");
        canMov.setText("");
        
       } else {
           for (ActComprasMd dt : allActCompras) {
               codMov.setText(dt.getCodigo());
               corMov.setText(dt.getCorrelativo());
               preMov.setText(dt.getPrecio());
//               ingMov.setText(dt.getIngreso());
//               venMov.setText(dt.getVencimiento());
               nomMov.setText(dt.getVendedor());
               canMov.setText(dt.getCantidad());
               
           }  
        }
      }

    public void onClick$btnGuardar(Event e) throws SQLException, ClassNotFoundException {
        int op = 0;

        for (ActComprasMd dt : allActCompras) {
            if (dt.getCodigo().equals(codMov.getText())) {
                op++;
            }
        }

        if (op == 0) {
            /*error.REGinsert(codMov.getText(),corMov.getText(), preMov.getText(), ingMov.getText(),
                            venMov.getText(), nomMov.getText(), canMov.getText());
            
            error.CalculaMov(codMov.getText(), corMov.getText(), canMov.getText());
            
            System.out.println("Entro al if : "+op);
            codMov.setText("");
            corMov.setText("");
            preMov.setText("");
            ingMov.setText("");
            venMov.setText("");
            nomMov.setText("");
            canMov.setText("");
            
            

            allMovimientos = error.RSelect();
           // lb2.setModel(new ListModelList(allMovimientos));*/
        } else {
            error.REGupdate(codMov.getText(), corMov.getText(), preMov.getText(),/* ingMov.getText(),
                    venMov.getText(),*/ nomMov.getText(), canMov.getText());

            codMov.setDisabled(false);
            corMov.setText("");
            preMov.setText("");
//            ingMov.setText("");
//            venMov.setText("");
            nomMov.setText("");
            canMov.setText("");
            codMov.focus();
         //   allActCompras = error.RSelect();
            //  lb2.setModel(new ListModelList(allMovimientos));
        }
    }

    public void onClick$btnActualiza(Event e) throws SQLException {
        codMov.setText("");
        corMov.setText("");
        preMov.setText("");
//        ingMov.setText("");
//        venMov.setText("");
        nomMov.setText("");
        canMov.setText("");
        codMov.focus();
    }

    /* public void onClick$btnDelete(Event e) throws SQLException {

        if (!codMov.getText().equals("") && !codMov.getText().equals("")) {
            Messagebox.show("Estas seguro que Deseas Borrar este Registro?",
                    "Question", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event e) throws SQLException, ClassNotFoundException {
                    if (Messagebox.ON_OK.equals(e.getName())) {
                        error.REGdelete(codMov.getText());
                        codMov.setDisabled(false);
                        corMov.setText("");
                        preMov.setText("");
                        ingMov.setText("");
                        venMov.setText("");
                        nomMov.setText("");
                        canMov.setText("");

                        allMovimientos = error.RSelect();
                        lb2.setModel(new ListModelList(allMovimientos));
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
