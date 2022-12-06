package ctrl_;

import DAL.MovimientosDal;
import DAL.ProductosNuevoDal;
import DAL.ProveedoresDal;
import MD.MovimientosMd;
import MD.ProductosNuevoMd;
import MD.ProveedoresMd;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

public class MovimientosCtrl extends GenericForwardComposer {

    private Textbox codMov;
    private Textbox corMov;
    private Textbox preMov;
    private Datebox ingMov;
    private Textbox usuMov;
    private Combobox proveedor;
    private Doublebox canMov;
    private Combobox ubiFerrete;

    List<MovimientosMd> allMovimientos = new ArrayList<MovimientosMd>();
    MovimientosMd moviModel = new MovimientosMd();
    private Listbox lb2;
    // MovimientosDal rg = new MovimientosDal();
    ProveedoresDal proveedorDal = new ProveedoresDal();
    private List<ProveedoresMd> allProveedor = new ArrayList<ProveedoresMd>();
    MovimientosDal error = new MovimientosDal();
    private Include rootPagina;
    List<ProductosNuevoMd> allProductos1 = new ArrayList<ProductosNuevoMd>();
    List<ProductosNuevoMd> allPro2Tabla = new ArrayList<ProductosNuevoMd>();
    ProductosNuevoDal ProductoDal = new ProductosNuevoDal();//lo copie de usuarios

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        allProductos1 = ProductoDal.RSelect();
        allPro2Tabla = ProductoDal.RSelect2Tabla();
        usuMov.setText(desktop.getSession().getAttribute("USER").toString());
        codMov.focus();
        allProveedor = proveedorDal.allProve();
        proveedor.setModel(new ListModelList(allProveedor));
        bloquear();
    }

    public void onChange$ubiFerrete(Event e) {
        liberar();

    }

    public void liberar() {

        codMov.setDisabled(false);
        corMov.setDisabled(false);
        preMov.setDisabled(false);
        ingMov.setDisabled(false);
        proveedor.setDisabled(false);
        canMov.setDisabled(false);

    }

    public void bloquear() {

        codMov.setDisabled(true);
        corMov.setDisabled(true);
        preMov.setDisabled(true);
        ingMov.setDisabled(true);
        proveedor.setDisabled(true);
        canMov.setDisabled(true);

    }

    public void onClick$btnNuevo(Event e) throws SQLException {
        codMov.setText("");
        corMov.setText("");
        preMov.setText("");
        ingMov.setText("");
        proveedor.setText("");
        canMov.setText("");
        corMov.focus();

    }

    public void onChange$codMov(Event e) throws SQLException, ClassNotFoundException {
        String correla = "";

        correla = error.Correlativo(codMov.getText());
        corMov.setText(correla);
        preMov.setFocus(true);
    }

    public void onClick$btnGuardar(Event e) throws SQLException, ClassNotFoundException {

        int op = 0, op1 = 0;

        if (codMov.getText().trim().equals("")) {
            op1 = 1;
        }
        if (corMov.getText().trim().equals("")) {
            op1 = 1;
        }
        if (preMov.getText().trim().equals("")) {
            op1 = 1;
        }
        if (ingMov.getText().trim().equals("")) {
            op1 = 1;
        }
        if (canMov.getText().trim().equals("")) {
            op1 = 1;
        }
        if (proveedor.getSelectedIndex() == -1) {
            op1 = 1;
        }

        if (ubiFerrete.getSelectedItem().getValue().toString().equals("1")) {
            for (ProductosNuevoMd dt : allProductos1) {
                if (dt.getCodigo().equals(codMov.getText())) {
                    op++;
                }
            }

        } else {
            if (ubiFerrete.getSelectedItem().getValue().toString().equals("2")) {
                for (ProductosNuevoMd dt : allPro2Tabla) {
                    if (dt.getCodigo().equals(codMov.getText())) {
                        op++;
                    }
                }

            } else {

            }
        }

        if (op1 == 0) {

            if (op == 1) {
                error.REGinsert(codMov.getText(), corMov.getText(), preMov.getText(), ingMov.getText(),
                        usuMov.getText(), proveedor.getText().toUpperCase(), canMov.getText());
                //aqui tiene que ir el codigo para validar el precio
                moviModel = new MovimientosMd();

                //para optener el valor actual de las dos ferreterias
                if (ubiFerrete.getSelectedItem().getValue().toString().equals("1")) {
                   moviModel = error.obtenervaloractual(codMov.getText());

                } else {
                    if (ubiFerrete.getSelectedItem().getValue().toString().equals("2")) {
                      moviModel = error.valorActual2Tabla(codMov.getText());

                    } else {

                    }
                }

               
                moviModel.getPrecio();
                float valorCompra = Float.valueOf(preMov.getText());
                float valorActual = Float.valueOf(moviModel.getPrecio());

                if (ubiFerrete.getSelectedItem().getValue().toString().equals("1")) {
                    //barrita
                    if (valorCompra > valorActual) {
                        //si el valor de la compra es es mayor actualizar precio
                        error.CalculaMovBarrita(codMov.getText(), corMov.getText(), canMov.getText(), preMov.getText());
                    } else {// si el valor de la compra es menor dejar el precio mayor ya actual
                        error.CalculaMov2Barrita(codMov.getText(), corMov.getText(), canMov.getText());

                    }

                } else {
                    if (ubiFerrete.getSelectedItem().getValue().toString().equals("2")) {
                        //carrizal
                        if (valorCompra > valorActual) {
                            //si el valor de la compra es es mayor actualizar precio
                            error.CalculaMovCarrizal(codMov.getText(), corMov.getText(), canMov.getText(), preMov.getText());
                        } else {// si el valor de la compra es menor dejar el precio mayor ya actual
                            error.CalculaMov2Carrizal(codMov.getText(), corMov.getText(), canMov.getText());

                        }

                    } else {
                        //angeles
                        if (valorCompra > valorActual) {
                            //si el valor de la compra es es mayor actualizar precio
                            error.CalculaMovAngeles(codMov.getText(), corMov.getText(), canMov.getText(), preMov.getText());
                        } else {// si el valor de la compra es menor dejar el precio mayor ya actual
                            error.CalculaMov2Angeles(codMov.getText(), corMov.getText(), canMov.getText());

                        }
                    }
                }

                System.out.println("Entro al if : " + op);
                codMov.setText("");
                corMov.setText("");
                preMov.setText("");
                ingMov.setText("");
                usuMov.setText("");
                proveedor.setText("");
                canMov.setText("");

//            allMovimientos = error.RSelect();
                // lb2.setModel(new ListModelList(allMovimientos));
            } else {//este else no se cumple sergio perez
//            error.REGupdate(codMov.getText(), corMov.getText(), preMov.getText(), ingMov.getText(),
//                    usuMov.getText(), proveedor.getText(), canMov.getText());
//
//            codMov.setDisabled(false);
//            corMov.setText("");
//            preMov.setText("");
//            ingMov.setText("");
//            usuMov.setText("");
//            proveedor.setText("");
//            canMov.setText("");
//            codMov.focus();
//            allMovimientos = error.RSelect();
//            //  lb2.setModel(new ListModelList(allMovimientos));

                Clients.showNotification("El Codigo <br/>  <br/> " + codMov.getText() + " No Existe <br/> <br/>Intentelo de Nuevo",
                        Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 0);
            }
        } else {
            Clients.showNotification("No puede dejar  <br/>  <br/>  Campos Vacios <br/> <br/>Intentelo de Nuevo",
                    Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 0);

        }
        usuMov.setText(desktop.getSession().getAttribute("USER").toString());
    }

    public void onClick$btnActualiza(Event e) throws SQLException {
        codMov.setText("");
        corMov.setText("");
        preMov.setText("");
        ingMov.setText("");
        usuMov.setText("");
        proveedor.setText("");
        canMov.setText("");
        codMov.focus();
    }

    public void onClick$btnDelete(Event e) throws SQLException {

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
                        usuMov.setText("");
                        proveedor.setText("");
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

    public void onClick$btnSalir() {
        rootPagina.setSrc("/Vistas/Principal.zul");
    }
}
