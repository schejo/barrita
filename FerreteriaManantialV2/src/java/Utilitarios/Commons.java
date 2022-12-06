package Utilitarios;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.GenericForwardComposer;




@SuppressWarnings("rawtypes")
public class Commons extends GenericForwardComposer {

    private static final long serialVersionUID = 1L;
    public static final String APPNAME = "Plantilla Base";
    public static final String No_Ticket = "";
    public static final String PATH_INICIAL = "menu.zul";
    public static final String USER = "";
    public static final String VARIABLE = "";
    
    Session TicketSession = Sessions.getCurrent();
    
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}
	
	public void salir(){
		desktop.getSession().invalidate();
		execution.sendRedirect("/login.zul");
	}
 	
	public boolean validarSesion(){
		if(TicketSession.getAttribute("USUARIO") != null){
			return true;
		}
		return false;
	}
        
        
        
    
        
}
