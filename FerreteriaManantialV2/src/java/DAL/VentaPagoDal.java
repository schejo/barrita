package DAL;

import Conexion.Conexion;
import MD.VentaPagoMd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.util.Clients;

public class VentaPagoDal {

    private Connection conexion = null;
    private Conexion cnn = new Conexion();
    PreparedStatement ps = null;
    Statement st = null;
    ResultSet rs = null;

    public List<VentaPagoMd> REGselect(String inicio, String pago, String fin, String sucursal) throws SQLException, ClassNotFoundException {
        List<VentaPagoMd> alldata = new ArrayList<VentaPagoMd>();

        System.out.println("que lleva tipo en DAL 1.: " + pago);

        String query
                = "select a.fac_autorizacion, c.cl_nombre,date_format(a.fac_fecha_alta, '%d/%m/%Y'),a.fac_total,\n"
                + "             CASE   a.fac_estado\n"
                + "              WHEN 'A' THEN 'ANULADO'\n"
                + "              WHEN 'E'THEN 'EMITIDA'\n"
                + "              END   AS ESTADO_FAC ,\n"
                + "              CASE   a.fac_tipo_pago\n"
                + "              WHEN 'E' THEN 'EFECTIVO'\n"
                + "              WHEN 'T'THEN 'TARJETA'\n"
                + "              WHEN 'C'THEN 'CHEQUE'\n"
                + "              WHEN 'TS'THEN 'TRANSFERENCIA'\n"
                + "              WHEN 'CR'THEN 'CREDITO'\n"
                + "              END   AS TIPO_PAGO ,\n"
                + "              c.cl_nombre, c.cl_nit,\n"
                + "             CASE    a.fac_sucursal\n"
                + "              WHEN '1' THEN 'LA BARRITA'\n"
                + "              WHEN '2' THEN 'CARRIZAL'\n"
                + "              WHEN '3' THEN 'LOS ANGELES'\n"
                + "              END   AS SUCURSAL \n"
                + "	from almacen.factura a, almacen.prefactura p, almacen.cliente c\n"
                + "	where a.fac_pre_numero = p.prefac_id and p.prefac_cl_id = c.CL_ID \n"
                + "    and a.fac_tipo_pago='" + pago + "' and date_format(a.fac_fecha_alta, '%d/%m/%Y')>='" + inicio + "' \n"
                + "    and date_format(a.fac_fecha_alta, '%d/%m/%Y')<='" + fin + "' and a.fac_sucursal='" + sucursal + "';";
//                + "select a.fac_autorizacion, a.fac_fecha_alta,a.fac_usuario_alta ,a.fac_total,\n"
//                + "             CASE   a.fac_estado\n"
//                + "              WHEN 'A' THEN 'ANULADO'\n"
//                + "              WHEN 'E'THEN 'EMITIDA'\n"
//                + "              END   AS TIPO_PAGO ,\n"
//                + "              CASE   a.fac_tipo_pago\n"
//                + "              WHEN 'E' THEN 'EFECTIVO'\n"
//                + "              WHEN 'T'THEN 'TARJETA'\n"
//                + "              WHEN 'C'THEN 'CHEQUE'\n"
//                + "              END   AS TIPO_PAGO ,\n"
//                + "              c.cl_nombre, c.cl_nit\n"
//                + "	from almacen.factura a, almacen.prefactura p, almacen.cliente c\n"
//                + "	where a.fac_pre_numero = p.prefac_id and p.prefac_cl_id = c.CL_ID \n"
//                + "    and a.fac_tipo_pago='" + pago + "' and date_format(a.fac_fecha_alta,"
//                + " '%d/%m/%Y')>='" + inicio + "' \n"
//                + "    and date_format(a.fac_fecha_alta, '%d/%m/%Y')<='" + fin + "';";
//                + "select a.fac_autorizacion, a.fac_fecha_alta,a.fac_usuario_alta ,a.fac_total,a.fac_estado,a.fac_tipo_pago,c.cl_nombre, c.cl_nit\n"
//                + "	from almacen.factura a, almacen.prefactura p, almacen.cliente c\n"
//                + "	where a.fac_pre_numero = p.prefac_id and p.prefac_cl_id = c.CL_ID \n"
//                + "    and a.fac_tipo_pago='" + pago + "' and date_format(a.fac_fecha_alta, '%d/%m/%Y')>='" + inicio + "' \n"
//                + "    and date_format(a.fac_fecha_alta, '%d/%m/%Y')<='" + fin + "';";

        System.out.println("que lleva tipo en DAL 2.: " + query);

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            VentaPagoMd rg;
            int x = 0;
            while (rs.next()) {
                rg = new VentaPagoMd();
                x++;
                rg.setCorrelativo(String.valueOf(x));
                rg.setAutorizacion(rs.getString(1));
                rg.setCliente(rs.getString(2));
                rg.setFecha(rs.getString(3));
                rg.setTotal(rs.getString(4));
                rg.setEstado(rs.getString(5));
                rg.setPago(rs.getString(6));
                rg.setNombre_sucursal(rs.getString(9));

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
