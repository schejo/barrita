package DAL;

import Conexion.Conexion;
import MD.ReporteRestriccionMd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.zkoss.zk.ui.util.Clients;

public class ReporteRestriccionDal {

    private Connection conexion = null;
    private Conexion cnn = new Conexion();
    PreparedStatement ps = null;
    Statement st = null;
    ResultSet rs = null;

    public List<ReporteRestriccionMd> REGselect(String fecha) throws SQLException, ClassNotFoundException {
        List<ReporteRestriccionMd> alldata = new ArrayList<ReporteRestriccionMd>();
        System.out.println("Antes del query   " + fecha);
        
        SimpleDateFormat formatter = new SimpleDateFormat ("dd/MM/yyyy");
        String dateInString = "";
        
        try {
             Date date = formatter.parse(fecha);
             formatter.applyPattern("yyyy/MM/dd");
             dateInString = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String query = " select a.prod_nombre,b.mov_precio_unitario,\n"
                + "             c.ven_cantidad,c.ven_precio,\n"
                + "             ( (c.ven_precio - b.mov_precio_unitario) * c.ven_cantidad )as GANACIA               \n"
                + "      from   productos a,\n"
                + "             mov_productos b,\n"
                + "             ventas c \n"
                + "     where   a.prod_codigo = b. mov_prod_codigo\n"
                + "     and     a.prod_codigo = c. ven_prod_codigo\n" 
                + "     and     a.prod_restriccion = 'S'\n"
                +"      and     c.ven_fecha < '"+dateInString+"' " ; 
        
                System.out.println("Antes del query   " + query);

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            ReporteRestriccionMd rg;
            int x = 0;
            //System.out.println("ACTIVIDAD...: "+actividad);
            while (rs.next()) {
                rg = new ReporteRestriccionMd();
                x++;
                rg.setCorrelativo(String.valueOf(x));
                rg.setNombre(rs.getString(1));
                rg.setPrecio_uni(rs.getString(2));
                rg.setCantidad(rs.getString(3));
                rg.setPrecio_ven(rs.getString(4));
                rg.setTotal(rs.getString(5));
                
                System.out.println("En el while del query   " +alldata );
                
                alldata.add(rg);
            }

            st.close();
            rs.close();
            conexion.close();
            conexion = null;
        } catch (SQLException e) {
            st.close();
            rs.close();
            conexion.close();
            conexion = null;
            Clients.showNotification("ERROR AL CONSULTAR <br/> <br/> REGISTROS! <br/> " + e.getMessage().toString(),
                    "warning", null, "middle_center", 0);
        }
        return alldata;
    }

}
