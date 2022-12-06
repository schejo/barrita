package DAL;

import Conexion.Conexion;
import MD.MovimientosMd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.util.Clients;

public class MovimientosDal {

    private Connection conn = null;//con
    private Conexion obtener = new Conexion();//obtener
    PreparedStatement ps = null;

    //PreparedStatement st = null;
    //Statement st = null;
    // ResultSet rs = null;
    MovimientosMd cl = new MovimientosMd();

    public List<MovimientosMd> REGselect(String codigo) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        List<MovimientosMd> allMovimientos = new ArrayList<MovimientosMd>();
        String query = "select trim(mov_pro_codigo),"
                + " trim(mov_correlativo),"
                + " trim(mov_precio_unitario,)"
                + " trim(mov_fecha_ingreso,)"
                + " trim(mov_usuario,)"
                + " trim(mov_vendedor,)"
                + " trim(mov_cantidad)"
                + " from almacen.mov_productos where mov_pro_codigo = '" + codigo + "' ";
        try {
            conn = obtener.Conexion();
            st = conn.createStatement();
            rs = st.executeQuery(query);
            MovimientosMd rg = new MovimientosMd();
            while (rs.next()) {
                rg.setCodigo(rs.getString(1));
                rg.setCorrelativo(rs.getString(2));
                rg.setPrecio(rs.getString(3));
                rg.setIngreso(rs.getString(4));
                rg.setUsuario(rs.getString(5));
                rg.setVendedor(rs.getString(6));
                rg.setCantidad(rs.getString(7));

                allMovimientos.add(rg);
            }

            st.close();
            rs.close();
            conn.close();
            conn = null;
        } catch (SQLException e) {
            conn.close();
            conn = null;
            Clients.showNotification("ERROR AL CONSULTAR (REGselect) <br/> <br/> REGISTROS! <br/> " + e.getMessage().toString(),
                    "warning", null, "middle_center", 0);
        }
        return allMovimientos;
    }

    public List<MovimientosMd> RSelect() throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        List<MovimientosMd> allMovimientos = new ArrayList<MovimientosMd>();
        String query = "select"
                + " trim(mov_pro_codigo),"
                + " trim(mov_correlativo),"
                + " trim(mov_precio_unitario),"
                + " trim(DATE_FORMAT(mov_fecha_ingreso,'%d/%m/%Y')),"
                + " trim(mov_usuario),"
                + " trim(mov_vendedor),"
                + " trim(mov_cantidad)"
                + " from almacen.mov_productos order by  mov_pro_codigo asc";

        try {
            conn = obtener.Conexion();
            st = conn.createStatement();
            rs = st.executeQuery(query);
            MovimientosMd rg;
            while (rs.next()) {
                rg = new MovimientosMd();
                rg.setCodigo(rs.getString(1));
                rg.setCorrelativo(rs.getString(2));
                rg.setPrecio(rs.getString(3));
                rg.setIngreso(rs.getString(4));
                rg.setUsuario(rs.getString(5));
                rg.setVendedor(rs.getString(6));
                rg.setCantidad(rs.getString(7));

                allMovimientos.add(rg);
            }

            st.close();
            rs.close();
            conn.close();
            conn = null;
        } catch (SQLException e) {
            conn.close();
            conn = null;
            Clients.showNotification("ERROR AL CONSULTAR (Rselect) <br/> <br/> REGISTROS! <br/> " + e.getMessage().toString(),
                    "warning", null, "middle_center", 0);
        }
        return allMovimientos;
    }

    public void REGinsert(String codigo, String correlativo, String precio,
            String ingreso, String usuario, String vendedor,
            String cantidad) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        String sql = "insert into almacen.mov_productos"
                + "(mov_pro_codigo,"
                + " mov_correlativo,"
                + " mov_precio_unitario,"
                + " mov_fecha_ingreso,"
                + " mov_usuario,"
                + " mov_vendedor,"
                + " mov_cantidad)"
                + " VALUES(?,?,?,STR_TO_DATE(?,'%d/%m/%Y'),?,?,?)";
        //  String sql0 = "select max(mov_prod_codigo)+1 as codigo from mov_productos";
        try {
            conn = obtener.Conexion();
            conn.setAutoCommit(false);
            st = conn.createStatement();

            /*  rs = st.executeQuery(sql0);
            String codigo = "";
            while (rs.next()) {
                codigo = rs.getString("codigo");
            }*/
            ps = conn.prepareStatement(sql);
            ps.setString(1, codigo);
            ps.setString(2, correlativo);
            ps.setString(3, precio);
            ps.setString(4, ingreso);
            ps.setString(5, usuario);
            ps.setString(6, vendedor);
            ps.setString(7, cantidad);
            ps.executeUpdate();
            ps.close();

            conn.commit();
            Clients.showNotification("REGISTRO CREADO <br/> CON EXITO  <br/>");
            conn.close();
            conn = null;
            System.out.println("Conexion Cerrada" + conn);

        } catch (SQLException e) {
            conn.rollback();
            conn.close();
            conn = null;
            Clients.showNotification("ERROR AL INSERTAR <br/> <br/> REGISTROS! <br/> " + e.getMessage().toString(),
                    "warning", null, "middle_center", 0);
        }
    }
    
      public MovimientosMd valorActual2Tabla(String codigo) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new MovimientosMd();
        String query0 = "select pro_precio_compra from productos2 where pro_id=" + codigo + ";";
        try {
            conn = obtener.Conexion();

            st = conn.createStatement();
            rs = st.executeQuery(query0);
            while (rs.next()) {
                resp = 1;
                cl.setPrecio(rs.getString(1));
                cl.setResp("1");
                cl.setMsg("ACTUALIZAR DATOS DE CATEDRATICO.!");
            }
            st.close();
            rs.close();

            if (resp == 0) {

                cl.setResp("0");
                cl.setMsg("CURSO NO EXISTE <br/>  <br/>  CONSULTAR REPORTE DE CURSOS");

            }
            conn.close();
            obtener.desconectar();

        } catch (Exception e) {
            System.out.println("ERROR CATCH.: " + e.getMessage());
            cl.setResp("0");
            cl.setMsg(e.getMessage());

        }

        return cl;
    }

    public MovimientosMd obtenervaloractual(String codigo) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new MovimientosMd();
        String query0 = "select pro_precio_compra from productos where pro_id=" + codigo + ";";
        try {
            conn = obtener.Conexion();

            st = conn.createStatement();
            rs = st.executeQuery(query0);
            while (rs.next()) {
                resp = 1;
                cl.setPrecio(rs.getString(1));
                cl.setResp("1");
                cl.setMsg("ACTUALIZAR DATOS DE CATEDRATICO.!");
            }
            st.close();
            rs.close();

            if (resp == 0) {

                cl.setResp("0");
                cl.setMsg("CURSO NO EXISTE <br/>  <br/>  CONSULTAR REPORTE DE CURSOS");

            }
            conn.close();
            obtener.desconectar();

        } catch (Exception e) {
            System.out.println("ERROR CATCH.: " + e.getMessage());
            cl.setResp("0");
            cl.setMsg(e.getMessage());

        }

        return cl;
    }

    public void REGupdate(String codigo, String correlativo, String precio,
            String ingreso, String usuario, String vendedor,
            String cantidad) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = obtener.Conexion();
            conn.setAutoCommit(false);
            System.out.println("ACTUALIZAR DATOS..!");
            System.out.println("Actualizar " + codigo);
            st = conn.createStatement();

            st.executeUpdate("update almacen.mov_productos set "
                    + ",mov_correlativo = '" + correlativo + "'"
                    + ",mov_precio_unitario = '" + precio + "'"
                    + ",mov_fecha_ingreso = STR_TO_DATE('" + ingreso + "','%d/%m/%Y') "
                    + ",mov_usuario = '" + usuario + "'"
                    + ",mov_vendedor = '" + vendedor + "'"
                    + ",mov_cantidad = '" + cantidad + "'"
                    + " where mov_pro_codigo = '" + codigo + "' ");

            Clients.showNotification("REGISTRO ACTUALIZADO <br/> CON EXITO  <br/>");
            System.out.println("Actualizacion Exitosa.! ");

            st.close();
            conn.commit();
            conn.close();
        } catch (SQLException e) {

            String mensaje = e.getMessage();
            Clients.showNotification("ERROR AL ACTUALIZAR <br/>'" + mensaje + "' <br/> REGISTROS! <br/> ",
                    "warning", null, "middle_center", 0);
            System.out.println("Actualizacion EXCEPTION.: " + mensaje);
            conn.rollback();
            conn.close();
        }

    }

    public void REGdelete(String codigo) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = obtener.Conexion();
            conn.setAutoCommit(false);
            System.out.println("ELIMINAR DATOS..!");
            System.out.println("Eliminar " + codigo);
            st = conn.createStatement();

            st.executeUpdate("delete almacen.mov_productos where mov_pro_codigo = '" + codigo + "' ");
            Clients.showNotification("REGISTRO ELIMINADO <br/> CON EXITO  <br/>");
            System.out.println("Eliminacion Exitosa.! ");
            st.close();
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            conn.rollback();
            conn.close();
            String mensaje = e.getMessage();
            Clients.showNotification("ERROR AL ELIMINAR <br/>'" + mensaje + "' <br/> REGISTROS! <br/> ",
                    "warning", null, "middle_center", 0);
            System.out.println("Eliminacion EXCEPTION.: " + mensaje);
        }

    }

    public void CalculaMovAngeles(String codigo, String correla, String cantidad, String precio) throws ClassNotFoundException, SQLException {
        Statement st = null;
        ResultSet rs = null;
        int valor = 0;
        double precios = 0.00;
        valor = Integer.parseInt(cantidad);
        precios = Double.parseDouble(precio);
        try {
            conn = obtener.Conexion();
            conn.setAutoCommit(false);
            System.out.println("ACTUALIZAR DATOS..!");
            System.out.println("Actualizar " + codigo);
            st = conn.createStatement();

            st.executeUpdate("update almacen.productos "
                    + "set pro_stock_angeles = pro_stock_angeles +" + valor + " ,"
                    + " pro_precio_compra = +" + precios + " ,"
                    + " pro_angeles = 1"
                    + " where pro_id = '" + codigo + "'  ");

            Clients.showNotification("REGISTRO ACTUALIZADO <br/> CON EXITO  <br/>");
            System.out.println("Actualizacion Exitosa.! ");

            st.close();
            conn.commit();
            conn.close();
        } catch (SQLException e) {

            String mensaje = e.getMessage();
            Clients.showNotification("ERROR AL ACTUALIZAR <br/>'" + mensaje + "' <br/> REGISTROS! <br/> ",
                    "warning", null, "middle_center", 0);
            System.out.println("Actualizacion EXCEPTION.: " + mensaje);
            conn.rollback();
            conn.close();
        }
    }

    public void CalculaMovCarrizal(String codigo, String correla, String cantidad, String precio) throws ClassNotFoundException, SQLException {
        Statement st = null;
        ResultSet rs = null;
        int valor = 0;
        double precios = 0.00;
        valor = Integer.parseInt(cantidad);
        precios = Double.parseDouble(precio);
        try {
            conn = obtener.Conexion();
            conn.setAutoCommit(false);
            System.out.println("ACTUALIZAR DATOS..!");
            System.out.println("Actualizar " + codigo);
            st = conn.createStatement();

            st.executeUpdate("update productos2 "
                    + "set pro_stock_carrizal = pro_stock_carrizal +" + valor + " ,"
                    + " pro_precio_compra = +" + precios + ", "
                    + " pro_carrizal = 1"
                    + " where pro_id = '" + codigo + "'  ");

            Clients.showNotification("REGISTRO ACTUALIZADO <br/> CON EXITO  <br/>");
            System.out.println("Actualizacion Exitosa.! ");

            st.close();
            conn.commit();
            conn.close();
        } catch (SQLException e) {

            String mensaje = e.getMessage();
            Clients.showNotification("ERROR AL ACTUALIZAR <br/>'" + mensaje + "' <br/> REGISTROS! <br/> ",
                    "warning", null, "middle_center", 0);
            System.out.println("Actualizacion EXCEPTION.: " + mensaje);
            conn.rollback();
            conn.close();
        }
    }

    public void CalculaMovBarrita(String codigo, String correla, String cantidad, String precio) throws ClassNotFoundException, SQLException {
        Statement st = null;
        ResultSet rs = null;
        int valor = 0;
        double precios = 0.00;
        valor = Integer.parseInt(cantidad);
        precios = Double.parseDouble(precio);
        try {
            conn = obtener.Conexion();
            conn.setAutoCommit(false);
            System.out.println("ACTUALIZAR DATOS..!");
            System.out.println("Actualizar " + codigo);
            st = conn.createStatement();

            st.executeUpdate("update almacen.productos "
                    + "set pro_stock_barrita = pro_stock_barrita +" + valor + " ,"
                    + " pro_precio_compra = +" + precios + ", "
                    + " pro_ferreteria = 1"
                    + " where pro_id = '" + codigo + "'  ");

            Clients.showNotification("REGISTRO ACTUALIZADO <br/> CON EXITO  <br/>");
            System.out.println("Actualizacion Exitosa.! ");

            st.close();
            conn.commit();
            conn.close();
        } catch (SQLException e) {

            String mensaje = e.getMessage();
            Clients.showNotification("ERROR AL ACTUALIZAR <br/>'" + mensaje + "' <br/> REGISTROS! <br/> ",
                    "warning", null, "middle_center", 0);
            System.out.println("Actualizacion EXCEPTION.: " + mensaje);
            conn.rollback();
            conn.close();
        }
    }

    public void CalculaMov2angeles(String codigo, String correla, String cantidad) throws ClassNotFoundException, SQLException {
        Statement st = null;
        ResultSet rs = null;
        int valor = 0;
        double precios = 0.00;
        valor = Integer.parseInt(cantidad);

        try {
            conn = obtener.Conexion();
            conn.setAutoCommit(false);
            System.out.println("ACTUALIZAR DATOS..!");
            System.out.println("Actualizar " + codigo);
            st = conn.createStatement();

            st.executeUpdate("update almacen.productos "
                    + "set pro_stock_angeles = pro_stock_angeles +" + valor + " "
                    + " where pro_id = '" + codigo + "'  ");

            Clients.showNotification("REGISTRO ACTUALIZADO <br/> CON EXITO  <br/>");
            System.out.println("Actualizacion Exitosa.! ");

            st.close();
            conn.commit();
            conn.close();
        } catch (SQLException e) {

            String mensaje = e.getMessage();
            Clients.showNotification("ERROR AL ACTUALIZAR <br/>'" + mensaje + "' <br/> REGISTROS! <br/> ",
                    "warning", null, "middle_center", 0);
            System.out.println("Actualizacion EXCEPTION.: " + mensaje);
            conn.rollback();
            conn.close();
        }
    }
 public void CalculaMov2Angeles(String codigo, String correla, String cantidad) throws ClassNotFoundException, SQLException {
        Statement st = null;
        ResultSet rs = null;
        int valor = 0;
        double precios = 0.00;
        valor = Integer.parseInt(cantidad);

        try {
            conn = obtener.Conexion();
            conn.setAutoCommit(false);
            System.out.println("ACTUALIZAR DATOS..!");
            System.out.println("Actualizar " + codigo);
            st = conn.createStatement();

            st.executeUpdate("update almacen.productos "
                    + "set pro_stock_angeles = pro_stock_angeles +" + valor + ", "
                    + " pro_angeles = 1"
                    + " where pro_id = '" + codigo + "'  ");

            Clients.showNotification("REGISTRO ACTUALIZADO <br/> CON EXITO  <br/>");
            System.out.println("Actualizacion Exitosa.! ");

            st.close();
            conn.commit();
            conn.close();
        } catch (SQLException e) {

            String mensaje = e.getMessage();
            Clients.showNotification("ERROR AL ACTUALIZAR <br/>'" + mensaje + "' <br/> REGISTROS! <br/> ",
                    "warning", null, "middle_center", 0);
            System.out.println("Actualizacion EXCEPTION.: " + mensaje);
            conn.rollback();
            conn.close();
        }
    }
    public void CalculaMov2Carrizal(String codigo, String correla, String cantidad) throws ClassNotFoundException, SQLException {
        Statement st = null;
        ResultSet rs = null;
        int valor = 0;
        double precios = 0.00;
        valor = Integer.parseInt(cantidad);

        try {
            conn = obtener.Conexion();
            conn.setAutoCommit(false);
            System.out.println("ACTUALIZAR DATOS..!");
            System.out.println("Actualizar " + codigo);
            st = conn.createStatement();

            st.executeUpdate("update productos2 "
                    + "set pro_stock_carrizal = pro_stock_carrizal +" + valor + ", "
                    + " pro_carrizal = 1"
                    + " where pro_id = '" + codigo + "'  ");

            Clients.showNotification("REGISTRO ACTUALIZADO <br/> CON EXITO  <br/>");
            System.out.println("Actualizacion Exitosa.! ");

            st.close();
            conn.commit();
            conn.close();
        } catch (SQLException e) {

            String mensaje = e.getMessage();
            Clients.showNotification("ERROR AL ACTUALIZAR <br/>'" + mensaje + "' <br/> REGISTROS! <br/> ",
                    "warning", null, "middle_center", 0);
            System.out.println("Actualizacion EXCEPTION.: " + mensaje);
            conn.rollback();
            conn.close();
        }
    }

    public void CalculaMov2Barrita(String codigo, String correla, String cantidad) throws ClassNotFoundException, SQLException {
        Statement st = null;
        ResultSet rs = null;
        int valor = 0;
        double precios = 0.00;
        valor = Integer.parseInt(cantidad);

        try {
            conn = obtener.Conexion();
            conn.setAutoCommit(false);
            System.out.println("ACTUALIZAR DATOS..!");
            System.out.println("Actualizar " + codigo);
            st = conn.createStatement();

            st.executeUpdate("update almacen.productos "
                    + "set pro_stock_barrita = pro_stock_barrita +" + valor + ", "
                    + " pro_ferreteria = 1"
                    + " where pro_id = '" + codigo + "'  ");

            Clients.showNotification("REGISTRO ACTUALIZADO <br/> CON EXITO  <br/>");
            System.out.println("Actualizacion Exitosa.! ");

            st.close();
            conn.commit();
            conn.close();
        } catch (SQLException e) {

            String mensaje = e.getMessage();
            Clients.showNotification("ERROR AL ACTUALIZAR <br/>'" + mensaje + "' <br/> REGISTROS! <br/> ",
                    "warning", null, "middle_center", 0);
            System.out.println("Actualizacion EXCEPTION.: " + mensaje);
            conn.rollback();
            conn.close();
        }
    }

    public String Correlativo(String codigo) throws ClassNotFoundException, SQLException {
        Statement st = null;
        ResultSet rs = null;
        String query = "select count(mov_correlativo)+1 as correlativo from almacen.mov_productos where mov_pro_codigo='" + codigo + "' ";
        String resp = "";
        try {
            conn = obtener.Conexion();
            st = conn.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                resp = rs.getString("correlativo");
            }

            st.close();
            rs.close();
            conn.close();
            conn = null;
        } catch (SQLException e) {
            conn.close();
            conn = null;
            Clients.showNotification("ERROR AL CONSULTAR (Rselect) <br/> <br/> REGISTROS! <br/> " + e.getMessage().toString(),
                    "warning", null, "middle_center", 0);
        }
        return resp;
    }

}
