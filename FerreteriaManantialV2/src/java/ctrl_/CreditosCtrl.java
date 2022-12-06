/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctrl_;

import DAL.ClientesDal;
import MD.ClientesMd;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;

/**
 *
 * @author HP 15
 */
public class CreditosCtrl extends GenericForwardComposer {

    private Combobox cliente;//esto nos sirve para buscar
    //llamamos al dal y generamos una lista de tipo modelo
    ClientesDal clienteDal = new ClientesDal();
    private List<ClientesMd> allCliente = new ArrayList<ClientesMd>();
    private Textbox Usu;
    ClientesMd clienteMD = new ClientesMd();
    ClientesMd clienteMDUpdate = new ClientesMd();
     ClientesMd clienteMDUmostrar = new ClientesMd();
    private Doublebox disponible;
    private Doublebox limite;
    private Label titulocredito;
    private Combobox selec;
    private Label tituloMontocre;
    private Doublebox monCredit;
    private Label tituPago;
    private Doublebox montoPago;
    private Textbox factura;
    private Label titulofac;
    private Textbox obs;
    private Label tituloobs;

    public void clear() {

        selec.setSelectedIndex(-1);
        cliente.setSelectedIndex(-1);
        disponible.setText("");
        limite.setText("");
        monCredit.setText("");
        montoPago.setText("");
        factura.setText("");
        obs.setText("");
        titulocredito.setVisible(false);
        selec.setVisible(false);
        tituloMontocre.setVisible(false);
        monCredit.setVisible(false);
        tituPago.setVisible(false);
        montoPago.setVisible(false);
        titulofac.setVisible(false);
        factura.setVisible(false);
        tituloobs.setVisible(false);
        obs.setVisible(false);
        selec.setDisabled(false);

    }

    //esto se carga al inicial la vista
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        //mostrar siempre el usuario de la sesion
        Usu.setText(desktop.getSession().getAttribute("USER").toString());
        allCliente = clienteDal.allCL();
        cliente.setModel(new ListModelList(allCliente));
        titulocredito.setVisible(false);
        selec.setVisible(false);
        tituloMontocre.setVisible(false);
        monCredit.setVisible(false);
        tituPago.setVisible(false);
        montoPago.setVisible(false);
        titulofac.setVisible(false);
        factura.setVisible(false);
        tituloobs.setVisible(false);
        obs.setVisible(false);

    }

    public void onChange$montoPago(Event e) {
        float disponibleInicial = Float.parseFloat(disponible.getText().replace(",", ""));
        if (montoPago.getText().equals("")) {
            Clients.showNotification("<br/>" + "MONTO CREDITO NO PUEDE ESTAR VACIO",
                    Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3000);
            montoPago.setText("0");
            float vdisponible = Float.parseFloat(disponible.getText().replace(",", ""));
            float vmontocredito = Float.parseFloat(montoPago.getText().replace(",", ""));
            disponible.setText(String.valueOf(disponibleInicial));
            montoPago.setDisabled(true);

            montoPago.focus();
            clear();

        } else {
            float vdisponible = Float.parseFloat(disponible.getText().replace(",", ""));
            float vmontocredito = Float.parseFloat(montoPago.getText().replace(",", ""));
            float total = vdisponible + vmontocredito;
            disponible.setText(String.valueOf(total));
            montoPago.setDisabled(true);
        }

    }

    public void onChange$monCredit(Event e) {
        float disponibleInicial = Float.parseFloat(disponible.getText().replace(",", ""));
        if (monCredit.getText().equals("")) {
            Clients.showNotification("<br/>" + "MONTO CREDITO NO PUEDE ESTAR VACIO",
                    Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3000);
            monCredit.setText("0");
            float vlimite = Float.parseFloat(limite.getText().replace(",", ""));
            float vdisponible = Float.parseFloat(disponible.getText().replace(",", ""));
            float vmontocredito = Float.parseFloat(monCredit.getText().replace(",", ""));

            disponible.setText(String.valueOf(disponibleInicial));
            monCredit.setDisabled(true);

            monCredit.focus();
        } else {
            float vlimite = Float.parseFloat(limite.getText().replace(",", ""));
            float vdisponible = Float.parseFloat(disponible.getText().replace(",", ""));
            float vmontocredito = Float.parseFloat(monCredit.getText().replace(",", ""));
            if (vdisponible < vmontocredito) {
                Clients.showNotification("<br/>" + "MONTO CREDITO NO PUEDE SER MAYOR AL DISPONIBLE",
                        Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3000);
                monCredit.setText("0");
                monCredit.focus();
                disponible.setText(String.valueOf(disponibleInicial));
                monCredit.setDisabled(true);
            } else {
                float total = vdisponible - vmontocredito;
                disponible.setText(String.valueOf(total));
                monCredit.setDisabled(true);
            }

        }

    }

    public void onChange$selec(Event e) {
        if (selec.getSelectedItem().getValue().toString().equals("1")) {
            //credito
            tituloMontocre.setVisible(true);
            monCredit.setVisible(true);
            titulofac.setVisible(true);
            factura.setVisible(true);
            tituloobs.setVisible(true);
            obs.setVisible(true);
            tituPago.setVisible(false);
            montoPago.setVisible(false);

        } else {
            //pago
            tituPago.setVisible(true);
            montoPago.setVisible(true);
            titulofac.setVisible(true);
            factura.setVisible(true);
            tituloobs.setVisible(true);
            obs.setVisible(true);
            tituloMontocre.setVisible(false);
            monCredit.setVisible(false);
        }
        selec.setDisabled(true);

    }

    public void onClick$btnNuevo(Event e) {
        clear();

    }

    public void onClick$btnGuardarUP(Event e) {
        int op = 0;

        if (factura.getText().trim().equals("")) {
            op = 1;
        }

        if (obs.getText().trim().equals("")) {
            op = 1;
        }
        if (op == 0) {
            clienteMDUpdate = new ClientesMd();
            clienteMDUpdate.setCodigoClienteMos(cliente.getSelectedItem().getValue().toString());
            clienteMDUpdate.setFactura(factura.getText().toUpperCase());
            clienteMDUpdate.setObs(obs.getText().toUpperCase());
            clienteMDUpdate.setUsuario(Usu.getText().toUpperCase());
            float limite3 = Float.parseFloat(limite.getText().replace(",", ""));
            clienteMDUpdate.setCredito(String.valueOf(limite3));

            //aqui le tengo que quitar las comillas
            float disponible1 = Float.parseFloat(disponible.getText().replace(",", ""));
            clienteMDUpdate.setDisponible(String.valueOf(disponible1));

            if (selec.getSelectedItem().getValue().toString().equals("1")) {
                //creditos
                float montoC = Float.parseFloat(monCredit.getText().replace(",", ""));
                clienteMDUpdate.setMontocredito(String.valueOf(montoC));
                clienteMDUpdate = clienteDal.saveCredito(clienteMDUpdate);
            } else {
                //pagos
                float montoPag = Float.parseFloat(montoPago.getText().replace(",", ""));
                clienteMDUpdate.setMontopago(String.valueOf(montoPag));
                clienteMDUpdate = clienteDal.savePago(clienteMDUpdate);

            }

            //credito
//                clienteMD.setMontocredito(monCredit.getText().toUpperCase());
            clienteMDUmostrar = new ClientesMd();
            clienteMDUmostrar.setCodigoClienteMos(cliente.getSelectedItem().getValue().toString());
            float disponible12 = Float.parseFloat(disponible.getText().replace(",", ""));
            clienteMDUmostrar.setDisponible(String.valueOf(disponible12));
            clienteMDUmostrar = clienteDal.updateOtorgarCredito(clienteMDUmostrar);

            if (clienteMDUpdate.getResp().equals("1")) {
                clear();
                Clients.showNotification(clienteMDUpdate.getMsg() + "<br/>",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 5000);
            } else {
                Clients.showNotification(clienteMDUpdate.getMsg() + "<br/>",
                        Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 5000);
            }
            montoPago.setDisabled(false);
            monCredit.setDisabled(false);

        } else {

            Clients.showNotification("Hay Campos Vacios<br/> O <br/>Telefono,Nit tiene valor 1   <br/> <br/>Intentelo de Nuevo",
                    Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 5000);

        }
    }

    public void onChange$cliente(Event e) {

        clienteMD = new ClientesMd();
        clienteMD = clienteDal.mostrarDispo(cliente.getSelectedItem().getValue().toString());
        if (clienteMD.getResp().equals("1")) {
            disponible.setText(clienteMD.getDisponible());
            limite.setText(clienteMD.getLimite());

        } else {
            //clear();
            cliente.setText("");

            cliente.setFocus(true);
            Clients.showNotification(clienteMD.getMsg() + "<br/>",
                    Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3000);
        }
        if (limite.getText().equals("0")) {
            Clients.showNotification("CLIENTE NO CUENTA CON CREDITO" + "<br/>",
                    Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3000);
            titulocredito.setVisible(false);
            selec.setVisible(false);
            tituloMontocre.setVisible(false);
            monCredit.setVisible(false);
            tituPago.setVisible(false);
            montoPago.setVisible(false);
            titulofac.setVisible(false);
            factura.setVisible(false);
            tituloobs.setVisible(false);
            obs.setVisible(false);
        } else {
            titulocredito.setVisible(true);
            selec.setVisible(true);
        }
    }

}
