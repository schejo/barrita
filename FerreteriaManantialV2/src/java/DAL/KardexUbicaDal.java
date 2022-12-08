package DAL;

import Conexion.Conexion;
import MD.KardexUbicaMd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.util.Clients;

public class KardexUbicaDal {

    private Connection conexion = null;
    private Conexion cnn = new Conexion();
    PreparedStatement ps = null;
    Statement st = null;
    ResultSet rs = null;

    public List<KardexUbicaMd> REGselect(String anio, String ubica) throws SQLException, ClassNotFoundException {
        List<KardexUbicaMd> alldata = new ArrayList<KardexUbicaMd>();

        System.out.println("que lleva tipo en DAL 1.: " + ubica);

        String query = "select a.pro_id, a.pro_descripcion,\n"
                + "                     a.pro_minimo, a.pro_precio_venta, a.pro_maximo,\n"
                + "                CASE a.pro_ubicacion\n"
                + "                    when 'S' then 'Sala de Ventas'\n"
                + "                    when 'P'then 'Patio'\n"
                + "                    when 'B1' then 'Bodega 1 Interior'\n"
                + "                    when 'B2' then 'Bodega 2'\n"
                + "                    when 'B3' then 'Bodega 3-Patio'\n"
                + "                    when 'B4' then 'Bodega 4-Carrizal '\n"
                + "                    when 'F2' then 'Ferreteria 2'\n"
     //           + "                    when 'P' then 'Pasillo Izquierdo'\n"
                + "                 END AS UBICACION,\n"
                + "                 a.pro_stock_barrita\n"
                + "                 from productos a\n"
                + "                 where a.pro_ubicacion = '" + ubica +"' ";
               
        System.out.println("que lleva tipo en DAL 2.: " + query);

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            KardexUbicaMd rg;
            int x = 0;
            //System.out.println("ACTIVIDAD...: "+actividad);
            while (rs.next()) {
                rg = new KardexUbicaMd();
                x++;
                rg.setCorrelativo(String.valueOf(x));
                rg.setCodigo(rs.getString(1));
                rg.setNombre(rs.getString(2));
                rg.setCantidad_ing(rs.getString(3));
                rg.setPrecio_ing(rs.getString(4));
                rg.setCantidad_sal(rs.getString(5));
                rg.setPrecio_sal(rs.getString(6));
                rg.setExistencia(rs.getString(7));

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
