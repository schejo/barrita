/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctrl_;

import DAL.ProveedoresDal;
import MD.ProveedoresMd;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;

/**
 *
 * @author chejo
 */
public class ProveedorUpCtrl extends GenericForwardComposer{
    private Combobox proveedor;//esto nos sirve para buscar
    //llamamos al dal y generamos una lista de tipo modelo
    ProveedoresDal proveedorDal = new ProveedoresDal();
    private List<ProveedoresMd> allProveedor = new ArrayList<ProveedoresMd>();
    ProveedoresMd proveedorMD = new ProveedoresMd();
    //variables usadas para mostrar
    private Textbox nomProve;
    private Textbox Nit;
    private Textbox Direccion;
    private Doublebox Telefono;
    private Textbox Correo;
    private Textbox Usu;
    //direccionar a las vistas
    private Include rootPagina;

    //esto se carga al inicial la vista
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        //mostrar siempre el usuario de la sesion
        Usu.setText(desktop.getSession().getAttribute("USER").toString());
        allProveedor = proveedorDal.allProve();
        proveedor.setModel(new ListModelList(allProveedor));

    }
    //metodo para actualizar
    public void onClick$btnGuardarUP(Event e) {
        int op = 0;
        if (nomProve.getText().trim().equals("")) {
            op = 1;
        }
        if (Nit.getText().trim().equals("")) {
            op = 1;
        }
        if (Direccion.getText().trim().equals("")) {
            op = 1;
        }
        if (Telefono.getText().trim().equals("")) {
            op = 1;
        }
        if (Correo.getText().trim().equals("")) {
            op = 1;
        }
        if (op == 0) {
            proveedorMD = new ProveedoresMd();
            proveedorMD.setNombreMos(proveedor.getText().toUpperCase());
            proveedorMD.setNombreComercial(nomProve.getText().toUpperCase());
            proveedorMD.setNit(Nit.getText().toUpperCase());
            proveedorMD.setDireccion(Direccion.getText().toUpperCase());
            proveedorMD.setTelefono(Telefono.getText().toUpperCase());
            proveedorMD.setCorreCleinte(Correo.getText().toUpperCase());
            proveedorMD.setUsuario(Usu.getText().toUpperCase());
            proveedorMD = proveedorDal.updateCur(proveedorMD);

            //volver actualizar la lista
            allProveedor = proveedorDal.allProve();
            proveedor.setModel(new ListModelList(allProveedor));

            if (proveedorMD.getResp().equals("1")) {
                clear();
                Clients.showNotification(proveedorMD.getMsg() + "<br/>",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 5000);
            } else {
                Clients.showNotification(proveedorMD.getMsg() + "<br/>",
                        Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 5000);
            }

        } else {
            {
                Clients.showNotification("Hay Campos <br/> Vacios <br/> <br/>Intentelo de Nuevo",
                        Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 5000);
            }

        }
    }

    //metodo se ejecuta al cambiar de valor proveedor
    public void onChange$proveedor(Event e) {
        proveedor.setDisabled(true);
        proveedorMD = new ProveedoresMd();
        proveedorMD = proveedorDal.findProve(proveedor.getText());
        if (proveedorMD.getResp().equals("1")) {
            nomProve.setText(proveedorMD.getNombreComercial());
            Nit.setText(proveedorMD.getNit());
            Direccion.setText(proveedorMD.getDireccion());
            Telefono.setText(proveedorMD.getTelefono());
            Correo.setText(proveedorMD.getCorreCleinte());
        } else {
            clear();
            proveedor.setText("");
            //curso.setDisabled(false);
            proveedor.setFocus(true);
            Clients.showNotification(proveedorMD.getMsg() + "<br/>",
                    Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3000);
        }
    }

    public void onClick$btnNuevo(Event e) throws SQLException, ClassNotFoundException {

        clear();

    }

    public void clear() {
        proveedor.setSelectedIndex(-1);
        proveedor.setText("");
        nomProve.setText("");
        Nit.setText("");
        Direccion.setText("");
        Telefono.setText("");
        Correo.setText("");
        proveedor.setDisabled(false);
    }

    public void onClick$btnSalir() {
        rootPagina.setSrc("/Vistas/Proveedores.zul");
    }
    
    
}
