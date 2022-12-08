package DAL;

import Conexion.Conexion;
import MD.ReporteVenceMd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.util.Clients;
import java.util.Date;
import java.text.SimpleDateFormat;


public class ReporteVenceDal {

    private Connection conexion = null;
    private Conexion cnn = new Conexion();
    PreparedStatement ps = null;
    Statement st = null;
    ResultSet rs = null;

    public List<ReporteVenceMd> REGselect(String fecha) throws SQLException, ClassNotFoundException, ParseException {
        List<ReporteVenceMd> alldata = new ArrayList<ReporteVenceMd>();
               
        SimpleDateFormat formatter = new SimpleDateFormat ("dd/MM/yyyy");
        String dateInString = "";
        
        try {
             Date date = formatter.parse(fecha);
             formatter.applyPattern("yyyy/MM/dd");
             dateInString = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
       
        String query = "  select a.prod_nombre,b.mov_precio_unitario, \n"
                + "              b.mov_cantidad,\n"
                + "              b.mov_fecha_vence\n"
                + "       from   productos a,              \n"
                + "              mov_productos b              \n"
                + "       where  a.prod_codigo = b. mov_prod_codigo\n" 
                +"        and    b.mov_fecha_vence < '"+dateInString+"' " ;    
                  
        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            ReporteVenceMd rg;
            int x = 0;
            while (rs.next()) {
                rg = new ReporteVenceMd();
                x++;
                rg.setCorrelativo(String.valueOf(x));
                rg.setNombre(rs.getString(1));
                rg.setPrecio(rs.getString(2));
                rg.setCantidad(rs.getString(3));
                rg.setVence(rs.getString(4));

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
