package ctrl_;

import DAL.UsuarioDal;
import MD.UsuariosMd;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

public class UserCtrl extends GenericForwardComposer {

    private Textbox usuCod;
    private Textbox usuNom;
    private Textbox usuCel;
    private Textbox usuUsu;
    private Textbox usuPas;
    private Combobox usuRol;
    private Datebox usuCre;
    private Datebox usuVen;
    private Include rootPagina;

    List<UsuariosMd> allUsuarios = new ArrayList<UsuariosMd>();
    UsuarioDal error = new UsuarioDal();

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        allUsuarios = error.Rselect();
        //lb2.setModel(new ListModelList(allUsuarios));
        //usuCod.focus();
    }

    public void BuscaItem(String letra, Combobox cb) {
        for (int i = 0; i < cb.getItemCount(); i++) {
            if (letra.equals(cb.getItemAtIndex(i).getValue())) {
                cb.setSelectedIndex(i);
                break;
            }
        }
    }

    public void onClick$btnNuevo(Event e) throws SQLException {
        usuCod.setText("");
        usuNom.setText("");
        usuCel.setText("");
        usuUsu.setText("");
        usuPas.setText("");
        usuRol.setText("");
        usuCre.setText("");
        usuVen.setText("");
        usuNom.focus();

    }

    public void onChange$usuCod(Event e) throws SQLException {

        if (usuCod.getText().isEmpty()) {

            usuCod.setText("");
            usuNom.setText("");
            usuCel.setText("");
            usuUsu.setText("");
            usuPas.setText("");
            usuRol.setText("");
            usuCre.setText("");
            usuVen.setText("");

        } else {
            for (UsuariosMd dt : allUsuarios) {
                if (dt.getCodigo().equals(usuCod.getText())) {
                    //System.out.println("FECHA CREA..: " + dt.getCrea());
                    //System.out.println("FECHA VENCE..: " + dt.getVence());
                    usuCod.setText(dt.getCodigo());
                    usuNom.setText(dt.getNombre());
                    usuCel.setText(dt.getCelular());
                    usuUsu.setText(dt.getUsuario());
                    usuPas.setText(dt.getPassword());
                    BuscaItem(dt.getRol(), this.usuRol);
                    usuCre.setText(dt.getCrea());
                    usuVen.setText(dt.getVence());
                }
            }
        }
    }

    public void onClick$btnGuardar(Event e) throws SQLException, ClassNotFoundException {
        int op = 0;

        for (UsuariosMd dt : allUsuarios) {
            if (dt.getCodigo().equals(usuCod.getText())) {
                op++;
            }
        }

        if (op == 0) {
            error.Rinsert(usuNom.getText(), usuCel.getText(), usuUsu.getText(),
                    usuPas.getText(), usuRol.getSelectedItem().getValue().toString(), usuCre.getText(),
                    usuVen.getText());

            usuCod.setText("");
            usuNom.setText("");
            usuCel.setText("");
            usuUsu.setText("");
            usuPas.setText("");
            usuRol.setText("");
            usuCre.setText("");
            usuVen.setText("");

            allUsuarios = error.Rselect();
            //lb2.setModel(new ListModelList(allUsuarios));
        } else {
            error.Rupdate(usuCod.getText(), usuNom.getText(), usuCel.getText(), usuUsu.getText(),
                    usuPas.getText(), usuRol.getSelectedItem().getValue().toString(), usuCre.getText(),
                    usuVen.getText());

            usuCod.setDisabled(false);
            usuNom.setText("");
            usuCel.setText("");
            usuUsu.setText("");
            usuPas.setText("");
            usuRol.setText("");
            usuCre.setText("");
            usuVen.setText("");
            usuCod.focus();
            allUsuarios = error.Rselect();
            //lb2.setModel(new ListModelList(allUsuarios));
        }
    }

    public void onClick$btnDelete(Event e) throws SQLException {

        if (!usuCod.getText().equals("") && !usuCod.getText().equals("")) {
            Messagebox.show("Estas seguro que Deseas Borrar este Registro?",
                    "Question", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener() {
                public void onEvent(Event e) throws SQLException, ClassNotFoundException {
                    if (Messagebox.ON_OK.equals(e.getName())) {
                        error.REGdelete(usuCod.getText());
                        usuCod.setDisabled(false);
                        usuNom.setText("");
                        usuCel.setText("");
                        usuUsu.setText("");
                        usuPas.setText("");
                        usuRol.setText("");
                        usuCre.setText("");
                        usuVen.setText("");

                        allUsuarios = error.Rselect();
                        //lb2.setModel(new ListModelList(allUsuarios));
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
