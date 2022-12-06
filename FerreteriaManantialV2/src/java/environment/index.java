package environment;

import DAL.VentasDal;
import MD.FacturaMd;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;

public class index extends GenericForwardComposer {

    private static final long serialVersionUID = 1L;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
       ////////// //  Redireccionando a la vista del Login
           execution.sendRedirect("login.zul");
//
////        VentasDal ven = new VentasDal();
////        ven.Anular("0208A476-EFDB-49DF-BCE3-EAA3EE579634","PRUEBAS");

    }
}
