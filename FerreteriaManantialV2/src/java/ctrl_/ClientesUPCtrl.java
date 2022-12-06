/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctrl_;

import DAL.ClientesDal;
import MD.ClientesMd;
import Util.EstilosReporte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;

/**
 *
 * @author chejo
 */
public class ClientesUPCtrl extends GenericForwardComposer {

    private Combobox cliente;//esto nos sirve para buscar
    //llamamos al dal y generamos una lista de tipo modelo
    ClientesDal clienteDal = new ClientesDal();
    private List<ClientesMd> allCliente = new ArrayList<ClientesMd>();
    ClientesMd clienteMD = new ClientesMd();
    //variables usadas para mostrar
    private Textbox Ncliente;
    private Textbox NitCliente;
    private Textbox Direccion;
    private Doublebox Telefono;
    private Textbox Correo;
    private Textbox Usu;
    private Doublebox credito;
    //direccionar a las vistas
    private Include rootPagina;
    private Doublebox disponible;
    private Doublebox limite;

    //esto se carga al inicial la vista
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        //mostrar siempre el usuario de la sesion
        Usu.setText(desktop.getSession().getAttribute("USER").toString());
        allCliente = clienteDal.allCL();
        cliente.setModel(new ListModelList(allCliente));
        credito.setDisabled(true);

    }

    //metodo para actualizar
    public void onClick$btnGuardarUP(Event e) {
        int op = 0;
        if (Ncliente.getText().trim().equals("")) {
            op = 1;
        }
        if (NitCliente.getText().trim().equals("")) {
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

            clienteMD = new ClientesMd();
            clienteMD.setNombreMos(cliente.getText().toUpperCase());
            clienteMD.setNombreComercial(Ncliente.getText().toUpperCase());
            clienteMD.setNit(NitCliente.getText().toUpperCase());
            clienteMD.setDireccion(Direccion.getText().toUpperCase());
            clienteMD.setTelefono(Telefono.getText().toUpperCase());
            clienteMD.setCorreCleinte(Correo.getText().toUpperCase());
            clienteMD.setUsuario(Usu.getText().toUpperCase());
            
             float limite1 = Float.parseFloat(limite.getText().replace(",", ""));
              float disponible1 = Float.parseFloat(disponible.getText().replace(",", ""));
            clienteMD.setCredito(String.valueOf(limite1));
            clienteMD.setDisponible(String.valueOf(disponible1));

            clienteMD = clienteDal.updateCur(clienteMD);

            //volver actualizar la lista
            allCliente = clienteDal.allCL();
            cliente.setModel(new ListModelList(allCliente));

            if (clienteMD.getResp().equals("1")) {
                clear();
                Clients.showNotification(clienteMD.getMsg() + "<br/>",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 5000);
            } else {
                Clients.showNotification(clienteMD.getMsg() + "<br/>",
                        Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 5000);
            }
            credito.setDisabled(false);

        } else {
            {
                credito.setDisabled(false);
                Clients.showNotification("Hay Campos <br/> Vacios <br/> <br/>Intentelo de Nuevo",
                        Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 5000);
            }

        }
    }

    public void onChange$credito(Event e) {
        if (credito.getText().equals("")) {
            Clients.showNotification("<br/>" + "OC NO PUEDE ESTAR VACIO",
                    Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3000);
            credito.setText("0");
            float montoactual = Float.parseFloat(credito.getText().replace(",", ""));
            float nuevoDisponi = Float.parseFloat(disponible.getText().replace(",", ""));
            float nuevoOtorgado = Float.parseFloat(limite.getText().replace(",", ""));

            float total = montoactual + nuevoOtorgado;
            float total2 = montoactual + nuevoDisponi;
            limite.setText(String.valueOf(total));

            disponible.setText(String.valueOf(total2));
            credito.focus();
            credito.setDisabled(true);
        } else {
            float montoactual = Float.parseFloat(credito.getText().replace(",", ""));
            float nuevoDisponi = Float.parseFloat(disponible.getText().replace(",", ""));
            float nuevoOtorgado = Float.parseFloat(limite.getText().replace(",", ""));
            float total = montoactual + nuevoOtorgado;
            float total2 = montoactual + nuevoDisponi;
            limite.setText(String.valueOf(total));

            disponible.setText(String.valueOf(total2));
            credito.setDisabled(true);

        }

    }

    //metodo se ejecuta al cambiar de valor cliente
    public void onChange$cliente(Event e) {
        credito.setDisabled(false);
        cliente.setDisabled(true);
        clienteMD = new ClientesMd();
        clienteMD = clienteDal.findClientes(cliente.getText());
        if (clienteMD.getResp().equals("1")) {
            Ncliente.setText(clienteMD.getNombreComercial());
            NitCliente.setText(clienteMD.getNit());
            Direccion.setText(clienteMD.getDireccion());
            Telefono.setText(clienteMD.getTelefono());
            Correo.setText(clienteMD.getCorreCleinte());
            credito.setText(clienteMD.getCredito());
            limite.setText(clienteMD.getCredito());
            disponible.setText(clienteMD.getDisponible());
        } else {
            clear();
            cliente.setText("");
            //curso.setDisabled(false);
            cliente.setFocus(true);
            Clients.showNotification(clienteMD.getMsg() + "<br/>",
                    Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3000);
        }
    }

    public void onClick$btnNuevo(Event e) throws SQLException, ClassNotFoundException {

        clear();

    }

    public void clear() {
        cliente.setSelectedIndex(-1);
        cliente.setText("");
        Ncliente.setText("");
        NitCliente.setText("");
        Direccion.setText("");
        Telefono.setText("");
        Correo.setText("");
        credito.setText("");
        limite.setText("");
        disponible.setText("");
        cliente.setDisabled(false);
        credito.setDisabled(false);

    }

    public void onClick$btnSalir() {
        rootPagina.setSrc("/Vistas/Clientes.zul");
    }

}
