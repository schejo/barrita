package DAL;

import Conexion.Conexion;
import MD.VentasServiciosMd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.util.Clients;

public class VentasServiciosDal {

    private Connection conexion = null;
    private Conexion cnn = new Conexion();
    PreparedStatement ps = null;
    //PreparedStatement st = null;
    //Statement st = null;
    // ResultSet rs = null;

    public List<VentasServiciosMd> REGselect(String codigo) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        List<VentasServiciosMd> allVentas = new ArrayList<VentasServiciosMd>();
        String query = "select trim(ven_pro_codigo),"
                + " trim(ven_correlativo),"
                + " trim(ven_cantidad,)"
                + " trim(ven_precio,)"
                + " trim(ven_usuario,)"
                + " trim(ven_fecha,)"
                + " trim(ven_descuento,)"
                + " trim(ven_total,)"
                + " trim(ven_tipo_pago)"
                + " from almacen.ventas where ven_pro_codigo = '" + codigo + "' ";
        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            VentasServiciosMd rg = new VentasServiciosMd();
            while (rs.next()) {
                rg.setCodigo(rs.getString(1));
                rg.setCorrelativo(rs.getString(2));
                rg.setCantidad(rs.getString(3));
                rg.setPrecio(rs.getString(4));
                rg.setUsuario(rs.getString(5));
                rg.setFecha(rs.getString(6));
                rg.setDescuento(rs.getString(7));
                rg.setTotal(rs.getString(8));
                rg.setTipo_pago(rs.getString(9));
                allVentas.add(rg);
            }

            st.close();
            rs.close();
            conexion.close();
            conexion = null;
        } catch (SQLException e) {
            conexion.close();
            conexion = null;
            Clients.showNotification("ERROR AL CONSULTAR (REGselect) <br/> <br/> REGISTROS! <br/> " + e.getMessage().toString(),
                    "warning", null, "middle_center", 0);
        }
        return allVentas;
    }

    public List<VentasServiciosMd> RSelect() throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        List<VentasServiciosMd> allVentas = new ArrayList<VentasServiciosMd>();
        String query = "select trim(ven_pro_codigo),"
                + " trim(ven_correlativo),"
                + " trim(ven_cantidad),"
                + " trim(ven_precio),"
                + " trim(ven_usuario),"
                + " trim(DATE_FORMAT(ven_fecha,'%d/%m/%Y')),"
                + " trim(ven_descuento),"
                + " trim(ven_total),"
                + " trim(ven_tipo_pago)"
                + " from almacen.ventas order by  ven_pro_codigo asc";

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            VentasServiciosMd rg;
            while (rs.next()) {
                rg = new VentasServiciosMd();
                rg.setCodigo(rs.getString(1));
                rg.setCorrelativo(rs.getString(2));
                rg.setCantidad(rs.getString(3));
                rg.setPrecio(rs.getString(4));
                rg.setUsuario(rs.getString(5));
                rg.setFecha(rs.getString(6));
                rg.setDescuento(rs.getString(7));
                rg.setTotal(rs.getString(8));
                rg.setTipo_pago(rs.getString(9));

                allVentas.add(rg);
            }

            st.close();
            rs.close();
            conexion.close();
            conexion = null;
        } catch (SQLException e) {

            conexion.close();
            conexion = null;
            Clients.showNotification("ERROR AL CONSULTAR (Rselect) <br/> <br/> REGISTROS! <br/> " + e.getMessage().toString(),
                    "warning", null, "middle_center", 0);
        }
        return allVentas;
    }

    public void REGinsert(String codigo, String correlativo, String cantidad, String precio,
            String usuario, String fecha, String descuento, String total/*, String pago*/) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        String sql = "insert into almacen.ventas"
                + "(ven_pro_codigo,"
                + " ven_correlativo,"
                + " ven_cantidad,"
                + " ven_precio,"
                + " ven_usuario,"
                + " ven_fecha,"
                + " ven_descuento,"
                + " ven_total)"
                //+ " ven_tipo_pago)"
                + " VALUES(?,?,?,?,?,STR_TO_DATE(?,'%d/%m/%Y'),?,?)";
        System.out.println("Query ventas" + sql);
        // String sql0 = "select max(ven_prod_codigo)+1 as codigo from ventas";
        try {
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);
            st = conexion.createStatement();

            /*  rs = st.executeQuery(sql0);
            String codigo = "";
            while (rs.next()) {
                codigo = rs.getString("codigo");
            }*/
            ps = conexion.prepareStatement(sql);

            ps.setString(1, codigo);
            ps.setString(2, correlativo);
            ps.setString(3, cantidad);
            ps.setString(4, precio);
            ps.setString(5, usuario);
            ps.setString(6, fecha);
            ps.setString(7, descuento);
            ps.setString(8, total);
            //ps.setString(9, pago);
            System.out.println("Query ventas datos" + ps);
            ps.executeUpdate();
            ps.close();

            conexion.commit();
            Clients.showNotification("REGISTRO CREADO <br/> CON EXITO  <br/>");
            conexion.close();
            conexion = null;
            System.out.println("Conexion Cerrada" + conexion);

        } catch (SQLException e) {
            conexion.rollback();
            conexion.close();
            conexion = null;
            Clients.showNotification("ERROR AL INSERTAR <br/> <br/> REGISTROS! <br/> " + e.getMessage().toString(),
                    "warning", null, "middle_center", 0);
        }
    }

    public void REGupdate(String codigo, String correlativo, String cantidad, String precio,
            String usuario, String fecha, String descuento, String total, String pago) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        try {
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);
            System.out.println("ACTUALIZAR DATOS..!");
            System.out.println("Actualizar " + codigo);
            st = conexion.createStatement();

            st.executeUpdate("update almacen.ventas set ven_correlativo= '" + correlativo + "'"
                    + ",ven_cantidad = '" + cantidad + "'"
                    + ",ven_precio = '" + precio + "'"
                    + ",ven_usuario = '" + usuario + "'"
                    + ",ven_fecha = STR_TO_DATE('" + fecha + "','%d/%m/%Y')"
                    + ",ven_descuento = '" + descuento + "'"
                    + ",ven_total = '" + total + "'"
                    + ",ven_tipo_pago = '" + pago + "'"
                    + " where ven_pro_codigo = '" + codigo + "' ");

            Clients.showNotification("REGISTRO ACTUALIZADO <br/> CON EXITO  <br/>");
            System.out.println("Actualizacion Exitosa.! ");

            st.close();
            conexion.commit();
            conexion.close();
        } catch (SQLException e) {

            String mensaje = e.getMessage();
            Clients.showNotification("ERROR AL ACTUALIZAR <br/>'" + mensaje + "' <br/> REGISTROS! <br/> ",
                    "warning", null, "middle_center", 0);
            System.out.println("Actualizacion EXCEPTION.: " + mensaje);
            conexion.rollback();
            conexion.close();
        }

    }

    public void REGdelete(String codigo) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        try {
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);
            System.out.println("ELIMINAR DATOS..!");
            System.out.println("Eliminar " + codigo);
            st = conexion.createStatement();

            st.executeUpdate("DELETE almacen.ventas where ven_pro_codigo = '" + codigo + "' ");
            Clients.showNotification("REGISTRO ELIMINADO <br/> CON EXITO  <br/>");
            System.out.println("Eliminacion Exitosa.! ");
            st.close();
            conexion.commit();
            conexion.close();
        } catch (SQLException e) {
            conexion.rollback();
            conexion.close();
            String mensaje = e.getMessage();
            Clients.showNotification("ERROR AL ELIMINAR <br/>'" + mensaje + "' <br/> REGISTROS! <br/> ",
                    "warning", null, "middle_center", 0);
            System.out.println("Eliminacion EXCEPTION.: " + mensaje);
        }

    }

    public List<VentasServiciosMd> Correlativo(String codigo) throws ClassNotFoundException, SQLException {
        Statement st = null;
        ResultSet rs = null;
        String query = "select count(a.ven_correlativo)+1 as correlativo, pro_precio_venta \n"
                + "from almacen.ventas a,\n"
                + "almacen.productos b\n"
                + "where b.pro_id='" + codigo + "' and a.ven_pro_codigo='" + codigo + "'; ";
        List<VentasServiciosMd> allVentas = new ArrayList<VentasServiciosMd>();
        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            VentasServiciosMd rg;
            while (rs.next()) {
                rg = new VentasServiciosMd();
                rg.setCorrelativo(rs.getString(1));
                rg.setPrecio(rs.getString(2));
                allVentas.add(rg);
            }

            st.close();
            rs.close();
            conexion.close();
            conexion = null;
        } catch (SQLException e) {
            conexion.close();
            conexion = null;
            Clients.showNotification("ERROR AL CONSULTAR (Rselect) <br/> <br/> REGISTROS! <br/> " + e.getMessage().toString(),
                    "warning", null, "middle_center", 0);
        }
        return allVentas;
    }

    public void CalculaMov(String codigo, String correla, String cantidad) throws ClassNotFoundException, SQLException {
        Statement st = null;
        ResultSet rs = null;
        int valor = 0;
        valor = Integer.parseInt(cantidad);
        try {
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);
            System.out.println("ACTUALIZAR DATOS..!");
            System.out.println("Actualizar " + codigo);
            st = conexion.createStatement();

            st.executeUpdate("update almacen.productos "
                    + "set pro_stock_barrita = pro_stock_barrita -" + valor + " "
                    + " where pro_id = '" + codigo + "'  ");

            Clients.showNotification("REGISTRO ACTUALIZADO <br/> CON EXITO  <br/>");
            System.out.println("Actualizacion Exitosa.! ");

            st.close();
            conexion.commit();
            conexion.close();
        } catch (SQLException e) {

            String mensaje = e.getMessage();
            Clients.showNotification("ERROR AL ACTUALIZAR <br/>'" + mensaje + "' <br/> REGISTROS! <br/> ",
                    "warning", null, "middle_center", 0);
            System.out.println("Actualizacion EXCEPTION.: " + mensaje);
            conexion.rollback();
            conexion.close();
        }
    }

    public String Existencia(String codigo) throws ClassNotFoundException, SQLException {
        Statement st = null;
        ResultSet rs = null;
        String query = "select pro_stock_barrita from almacen.productos where pro_id = '" + codigo + "' ";
        String resp = "";
        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                resp = rs.getString("pro_stock_barrita");
            }

            st.close();
            rs.close();
            conexion.close();
            conexion = null;
        } catch (SQLException e) {
            conexion.close();
            conexion = null;
            Clients.showNotification("ERROR AL CONSULTAR (Rselect) <br/> <br/> REGISTROS! <br/> " + e.getMessage().toString(),
                    "warning", null, "middle_center", 0);
        }
        return resp;
    }

}
