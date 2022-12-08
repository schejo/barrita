package DAL;

import Conexion.Conexion;
import MD.ReporteBajasMd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.util.Clients;

public class ReporteBajasDal {

    private Connection conexion = null;
    private Conexion cnn = new Conexion();
    PreparedStatement ps = null;
    Statement st = null;
    ResultSet rs = null;

    public List<ReporteBajasMd> REGselect(String FechaI) throws SQLException, ClassNotFoundException {
        List<ReporteBajasMd> alldata = new ArrayList<ReporteBajasMd>();

        String query = "select a.pro_descripcion,b.mov_precio_unitario,c.baj_cantidad,\n"
                + "       c.baj_fecha,\n"
                + "       ( (b.mov_precio_unitario * c.baj_cantidad )) as BAJA_TOTAL\n"
                + "from   productos a,\n"
                + "       mov_productos b,\n"
                + "       bajas c \n"
                + "where  a.pro_id = b. mov_pro_codigo\n"
                + "and    a.pro_id = c. baj_pro_codigo";
        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            ReporteBajasMd rg;
            int x = 0;
            while (rs.next()) {
                rg = new ReporteBajasMd();
                x++;
                rg.setCorrelativo(String.valueOf(x));
                rg.setNombre(rs.getString(1));
                rg.setPrecio(rs.getString(2));
                rg.setCantidad(rs.getString(3));
                rg.setFecha(rs.getString(4));
                rg.setBaja(rs.getString(5));

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
