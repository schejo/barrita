package DAL;

import Conexion.Conexion;
import MD.ReporteVentasEfectivoMd;
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

public class ReporteVentasEfectivoDal {

    private Connection conexion = null;
    private Conexion cnn = new Conexion();
    PreparedStatement ps = null;
    Statement st = null;
    ResultSet rs = null;

    public List<ReporteVentasEfectivoMd> REGselect(String anio, String fechaF) throws SQLException, ClassNotFoundException {
        List<ReporteVentasEfectivoMd> alldata = new ArrayList<ReporteVentasEfectivoMd>();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = "";

        try {
            Date date = formatter.parse(anio);
            formatter.applyPattern("yyyy/MM/dd");
            dateInString = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString2 = "";

        try {
            Date date2 = formatter2.parse(fechaF);
            formatter2.applyPattern("yyyy/MM/dd");
            dateInString2 = formatter2.format(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String query = "select a.pro_descripcion, \n"
                + "       b.det_cantidad, B.det_precio_venta,   \n"
                + "      (b.det_precio_venta * b.det_cantidad) as TOTAL_VENTA,\n"
                + "	  d.cl_nombre, d.cl_nit\n"
                + "from   productos a,\n"
                + "       detalle_prefactura b,\n"
                + "	  prefactura c,\n"
                + "       cliente d\n"
                + "where  c.prefac_estado = 'E' \n"
                + "and    a.pro_id = b.det_pro_id \n"
                + "and    b.det_prefac_id = c.prefac_id\n"
                + "and    c.prefac_cl_id = d.cl_id\n"
                + "and    c.prefac_fecha_alta >=  '" + dateInString + "'\n "
                + "and    c.prefac_fecha_alta <=  '" + dateInString2 + "'";

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            ReporteVentasEfectivoMd rg;
            int x = 0;

            while (rs.next()) {
                rg = new ReporteVentasEfectivoMd();
                x++;
                rg.setCorrelativo(String.valueOf(x));
                rg.setNombre(rs.getString(1));
                rg.setPrecio(rs.getString(2));
                rg.setCantidad(rs.getString(3));
                rg.setTotal(rs.getString(4));
                rg.setCliente(rs.getString(5));
                rg.setNit(rs.getString(6));

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
