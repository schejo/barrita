package DAL;

import Conexion.Conexion;
import MD.ReporteVentas1Md;
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

public class ReporteVentas1Dal {

    private Connection conexion = null;
    private Conexion cnn = new Conexion();
    PreparedStatement ps = null;
    Statement st = null;
    ResultSet rs = null;

    public List<ReporteVentas1Md> REGselect(String anio,String ferreteria) throws SQLException, ClassNotFoundException {
        List<ReporteVentas1Md> alldata = new ArrayList<ReporteVentas1Md>();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = "";

//        try {
//            Date date = formatter.parse(anio);
//            formatter.applyPattern("yyyy/MM/dd");
//            dateInString = formatter.format(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString2 = "";

        String query = "select d.fac_numero as NUMEROFACT,d.fac_autorizacion,\n" +
"                             c.pro_descripcion AS DESCRIPCION,\n" +
"                                 b.det_cantidad as CANTIDAD, b.det_precio_venta as PRECIO, \n" +
"                                 (b.det_precio_venta * b.det_cantidad) as TOTAL,d.fac_fecha_alta\n" +
"                                  from almacen.prefactura a,\n" +
"                                    almacen.detalle_prefactura b, \n" +
"                                    almacen.productos c,\n" +
"                                    factura d\n" +
"                               where a.prefac_id = b.det_prefac_id \n" +
"                               and b.det_pro_id = c.pro_id\n" +
"                               and d.fac_numero = a.prefac_id\n" +
"                               and d.fac_numero =b.det_prefac_id\n" +
"                               and date_format(d.fac_fecha_alta, '%d/%m/%Y')= '" + anio + "'\n" +
"                               and fac_sucursal='" + ferreteria + "'\n" +
"                               order by a.prefac_id;";

//                + "select a.prefac_id as NUMEROFACT,\n"
//                + "              c.pro_descripcion AS DESCRIPCION,\n"
//                + "                  b.det_cantidad as CANTIDAD, b.det_precio_venta as PRECIO, \n"
//                + "                  (b.det_precio_venta * b.det_cantidad) as TOTAL\n"
//                + "                   from almacen.prefactura a,\n"
//                + "                     almacen.detalle_prefactura b, \n"
//                + "                     almacen.productos c\n"
//                + "                where a.prefac_id = b.det_prefac_id \n"
//                + "                and b.det_pro_id = c.pro_id\n"
//                + "                and c.pro_tipo_servicio = 'B'"
//                + "                order by a.prefac_id";

        
        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            ReporteVentas1Md rg;
            int x = 0;

            while (rs.next()) {
                rg = new ReporteVentas1Md();
                x++;
                rg.setCorrelativo(String.valueOf(x));
                rg.setFactura(rs.getString(1));
                rg.setAutorizacion(rs.getString(2));
                rg.setDescripcion(rs.getString(3));
                rg.setCantidad_ing(rs.getString(4));
                rg.setPrecio_ing(rs.getString(5));
                rg.setTotal(rs.getString(6));
                rg.setFecha(rs.getString(7));

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
