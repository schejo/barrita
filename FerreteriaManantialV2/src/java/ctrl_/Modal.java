package ctrl_;

import DAL.VentasDal;
import java.io.ByteArrayInputStream;
import java.sql.SQLException;

import org.zkoss.zul.Textbox;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.Component;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.WrongValueException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

public class Modal extends GenericForwardComposer {

    private Window wind;
    private Textbox txtMotivo;

    private Label lblAutorizacion;
    private Label lblCliente;
    private Label lblFechaEmision;

    Session Session = Sessions.getCurrent();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        SimpleDateFormat formatoE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatoS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        String fecha = formatoS.format(formatoE.parse(Session.getAttribute("EMISION").toString()));

        lblAutorizacion.setValue(Session.getAttribute("AUTORIZACION").toString());
        lblCliente.setValue(Session.getAttribute("CLIENTE").toString());
        lblFechaEmision.setValue(fecha);

    }

    public void onClick$btnCancelar(Event evt) {
        Events.postEvent(Events.ON_CLOSE, wind, "");
    }

    VentasDal ven = new VentasDal();

    public void onClick$btnGuardar(Event evt) throws ParseException, SQLException {
        if (!txtMotivo.getText().equals("")) {

            if (ven.Anular(Session.getAttribute("AUTORIZACION").toString(), txtMotivo.getText()) > 0) {
                Messagebox.show("FACTURA ANULADA DE FORMA EXITOSA", "Informacion", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                Messagebox.show("ERROR AL ANULAR FACTURA", "Informacion", Messagebox.OK, Messagebox.ERROR);

            }
            Events.postEvent(Events.ON_CLOSE, wind, "");
        } else {
            Messagebox.show("INGRESE EL MOTIVO DE ANLACION", "Informacion", Messagebox.OK, Messagebox.EXCLAMATION);
        }

    }

}
