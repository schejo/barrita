package DAL;

import Conexion.Conexion;
import MD.ProductosMd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;

public class ProductosDal {

    private Connection conexion = null;
    private Conexion cnn = new Conexion();
    PreparedStatement ps = null;
    Statement st = null;
    ResultSet rs = null;
    Conexion obtener = new Conexion();
    Connection conn;
    ProductosMd cl = new ProductosMd();

    Session session = Sessions.getCurrent();

    public List<ProductosMd> allCL() {
        List<ProductosMd> data = new ArrayList<ProductosMd>();
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;

        String query0 = " SELECT pro_id, concat(pro_descripcion,' Codigo: ',pro_id) FROM almacen.productos ";
        try {
            conn = obtener.Conexion();

            st = conn.createStatement();
            rs = st.executeQuery(query0);
            while (rs.next()) {
                cl = new ProductosMd();
                resp = 1;
                cl.setCodigo_pro(rs.getString(1));
                cl.setNombre_pro(rs.getString(2));
                // cl.setDir(rs.getString(3));
                // cl.setResp("1");
                //cl.setMsg("TODAS LAS PERSONAS.!");
                data.add(cl);
            }
            st.close();
            rs.close();

            if (resp == 0) {

                // cl.setResp("0");
                //cl.setMsg("CURSO NO EXISTEN");
            }
            conn.close();
            // obtener.desconectar();

        } catch (Exception e) {
            System.out.println("ERROR CATCH.: " + e.getMessage());
            // cl.setResp("0");
            //cl.setMsg(e.getMessage());

        }

        return data;
    }

    public List<ProductosMd> REGselect(String codigo) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;//las agg de usuarios
        List<ProductosMd> allProductos = new ArrayList<ProductosMd>();
        String query = "select pro_id,\n"
                + "pro_descripcion,\n"
                + "pro_tipo,\n"
                + "pro_tipo_servicio,\n"
                + "pro_marca,\n"
                + "pro_presentacion,\n"
                + "pro_precio_venta,\n"
                + "pro_descuento,\n"
                + "pro_stock_barrita,\n"
                + "pro_conversion,\n"
                //  + "pro_fecha_alta,\n"
                //  + "pro_usuario_alta,\n"
                //  + "pro_fecha_modifica,\n"
                //   + "pro_usuario_modifica,\n"
                + "pro_medida,\n"
                + "pro_minimo,\n"
                + "pro_maximo,\n"
                + "pro_ubicacion\n"
                + " from almacen.productos\n"
                + "where pro_id = '" + codigo + "' ";
        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            ProductosMd rg = new ProductosMd();
            while (rs.next()) {
                rg.setCodigo(rs.getString(1));
                rg.setDescripcion(rs.getString(2));
                rg.setTipo(rs.getString(3));
                rg.setTipo_servicio(rs.getString(4));
                rg.setMarca(rs.getString(5));
                rg.setPresentacion(rs.getString(6));
                rg.setPrecio_venta(rs.getString(7));
                rg.setDescuento(rs.getString(8));
                rg.setStock(rs.getString(9));
                rg.setConversion(rs.getString(10));
                // rg.setFecha_alta(rs.getString(11));
                //  rg.setUsuario_alta(rs.getString(12));
                //  rg.setFecha_modifica(rs.getString(13));
                // rg.setUsuario_alta(rs.getString(12));
                rg.setMedida(rs.getString(11));
                rg.setMinimo(rs.getString(12));
                rg.setMaximo(rs.getString(13));
                rg.setUbicacion(rs.getString(14));
                allProductos.add(rg);
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
            Clients.showNotification("ERROR AL CONSULTAR (REGselect) <br/> <br/> REGISTROS! <br/> " + e.getMessage().toString(),
                    "warning", null, "middle_center", 0);
        }
        return allProductos;
    }

    public List<ProductosMd> RSelect() throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;//copie de usuarios
        List<ProductosMd> allProductos = new ArrayList<ProductosMd>();
        String query = "select  pro_id,\n"
                + "pro_descripcion,\n"
                + "pro_tipo,\n"
                + "pro_tipo_servicio,\n"
                + "pro_marca,\n"
                + "pro_presentacion,\n"
                + "pro_precio_venta,\n"
                + "pro_descuento,\n"
                + "pro_stock_barrita,\n"
                + "pro_conversion,\n"
                //  + "pro_fecha_alta,\n"
                //  + "pro_usuario_alta,\n"
                //  + "pro_fecha_modifica,\n"
                //  + "pro_usuario_modifica,\n"
                + "pro_medida,\n"
                + "pro_minimo,\n"
                + "pro_maximo,\n"
                + "pro_ubicacion\n"
                + " from almacen.productos\n"
                + " order by  pro_id asc";

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            ProductosMd rg;
            while (rs.next()) {
                rg = new ProductosMd();
                rg.setCodigo(rs.getString(1));
                rg.setDescripcion(rs.getString(2));
                rg.setTipo(rs.getString(3));
                rg.setTipo_servicio(rs.getString(4));
                rg.setMarca(rs.getString(5));
                rg.setPresentacion(rs.getString(6));
                rg.setPrecio_venta(rs.getString(7));
                rg.setDescuento(rs.getString(8));
                rg.setStock(rs.getString(9));
                rg.setConversion(rs.getString(10));
                //rg.setFecha_alta(rs.getString(11));
                //  rg.setUsuario_alta(rs.getString(12));
                //  rg.setFecha_modifica(rs.getString(13));
                //  rg.setUsuario_alta(rs.getString(13));
                rg.setMedida(rs.getString(11));
                rg.setMinimo(rs.getString(12));
                rg.setMaximo(rs.getString(13));
                rg.setUbicacion(rs.getString(14));

                allProductos.add(rg);
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
        return allProductos;
    }

    public void REGinsert(String descripcion, String tipo, String tipo_servicio,
            String marca, String presentacion, String precio_venta, String descuento,
            String stock, String conversion,/* String usuario_alta, String fecha_modifica,
            String usuario_modifica,*/ String medida, String minimo, String maximo, String ubicacion) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;

        String sql = "INSERT INTO almacen.productos"
                + "(pro_id,\n"
                + "pro_descripcion,\n"
                + "pro_tipo,\n"
                + "pro_tipo_servicio,\n"
                + "pro_marca,\n"
                + "pro_presentacion,\n"
                + "pro_precio_venta,\n"
                + "pro_descuento,\n"
                + "pro_stock_barrita,\n"
                + "pro_conversion,\n"
                //+ "pro_fecha_alta,\n"
                // + "pro_usuario_alta,\n"
                + "pro_medida,\n"
                + "pro_minimo,\n"
                + "pro_maximo,\n"
                + "pro_ubicacion)\n"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";//now()

        String sql0 = "select max(pro_id)+1 as codigo from productos";

        try {
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);
            st = conexion.createStatement();

            rs = st.executeQuery(sql0);
            String codigo = "";
            while (rs.next()) {
                codigo = rs.getString("codigo");
            }

            ps = conexion.prepareStatement(sql);

            ps.setString(1, codigo);
            ps.setString(2, descripcion);
            ps.setString(3, tipo);
            ps.setString(4, tipo_servicio);
            ps.setString(5, marca);
            ps.setString(6, presentacion);
            ps.setString(7, precio_venta);
            ps.setString(8, descuento);
            ps.setString(9, stock);
            ps.setString(10, conversion);
            //  ps.setString(11, usuario_alta);
            ps.setString(11, medida);
            ps.setString(12, minimo);
            ps.setString(13, maximo);
            ps.setString(14, ubicacion);
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
            Clients.showNotification("ERROR INSERTAR  <br/> <br/> REGISTROS! <br/> " + e.getMessage().toString(),
                    "warning", null, "middle_center", 0);
        }
    }

    public void REGupdate(String codigo, String descripcion, String tipo, String tipo_servicio,
            String marca, String presentacion, String precio_venta, String descuento,
            String stock, String conversion,/* String usuario_alta, String fecha_modifica,
            String usuario_modifica,*/ String medida, String minimo, String maximo, String ubicacion) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;//copie de usuarios
        try {
            System.out.println("Conexion..: " + cnn);
            conexion = cnn.Conexion();//copie de usuarios
            conexion.setAutoCommit(false);
            System.out.println("ACTUALIZAR DATOS....................................!");
            System.out.println("Actualizar " + codigo);
            System.out.println("Actualizar " + tipo_servicio);
            st = conexion.createStatement();

            st.executeUpdate("update almacen.productos"
                    + " set pro_descripcion = '" + descripcion + "'"
                    + ",pro_tipo = '" + tipo + "'"
                    + ",pro_tipo_servicio = '" + tipo_servicio + "'"
                    + ",pro_marca = '" + marca + "'"
                    + ",pro_presentacion = '" + presentacion + "'"
                    + ",pro_precio_venta = '" + precio_venta + "'"
                    + ",pro_descuento = '" + descuento + "'"
                    + ",pro_stock_barrita = '" + stock + "'"
                    + ",pro_conversion = '" + conversion + "'"
                    //  + ",pro_fecha_alta = now()"
                    //  + ",pro_usuario_alta = '" + usuario_alta + "'"
                    //  + ",pro_fecha_modifica = STR_TO_DATE('" + fecha_modifica + "','%d/%m/%Y') "
                    //+ ",pro_usuario_modifica = '" + usuario_modifica + "'"
                    + ",pro_medida = '" + medida + "'"
                    + ",pro_minimo = '" + minimo + "'"
                    + ",pro_maximo = '" + maximo + "'"
                    + ",pro_ubicacion = '" + ubicacion + "'"
                    + " where pro_id = '" + codigo + "' ");
            conexion.commit();
            Clients.showNotification("REGISTRO ACTUALIZADO <br/> CON EXITO  <br/>");
            System.out.println("Actualizacion Exitosa.! ");
            //%u     + ",prod_precio_venta = '" + precio_venta + "'"

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
        ResultSet rs = null;//lo copie de usuarios
        try {
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);
            System.out.println("ELIMINAR DATOS..!");
            System.out.println("Eliminar " + codigo);
            st = conexion.createStatement();

            st.executeUpdate("delete almacen.productos where pro_id = " + codigo + " ");
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

    public ProductosMd BuscarPro2Tabla(String codigo) throws SQLException {
        PreparedStatement smt = null;
        Connection conn;
        Conexion conex = new Conexion();
        conexion = cnn.Conexion();
        //conn = conex.getConnection();
        ResultSet result = null;

        ProductosMd Buscar = null;

        String stock = "";

        switch (session.getAttribute("SUCURSAL").toString()) {
            case "1":
                stock = "pro_stock_barrita";
                break;
            case "2":
                stock = "pro_stock_carrizal";
                break;
            case "3":
                stock = "pro_stock_angeles";
                break;

        }

        String sql = "SELECT P.PRO_ID,\n"
                + "       CONCAT( IFNULL(P.PRO_DESCRIPCION,' '),' ', IFNULL(P.PRO_MARCA,' ') ,' ',IFNULL((CASE P.PRO_PRESENTACION\n"
                + "                   WHEN 'U' THEN 'UNIDAD'\n"
                + "                    WHEN 'C' THEN 'CAJA'\n"
                + "                    WHEN 'B' THEN 'BOLSA'\n"
                + "                    WHEN 'L' THEN 'LITRO' ELSE ' ' END),' '),' ', case \n"
                + "  when P.PRO_CONVERSION IS NULL OR P.PRO_CONVERSION = 0 then ' '  \n"
                + "  else  CONCAT('X ',P.PRO_CONVERSION)\n"
                + "end) DESCRIPCION,\n"
                + "       P.PRO_TIPO_SERVICIO,\n"
                + "       IFNULL(FORMAT(P.PRO_PRECIO_VENTA,2),'-'),\n"
                + "       IFNULL(FORMAT(IFNULL(P.PRO_DESCUENTO,0),2),'-'),\n"
                + "       IFNULL(P." + stock + ",0)\n"
                + "FROM productos2 P  "
                + "   where P.PRO_ID = ?";

        try {
            smt = conexion.prepareStatement(sql);
            smt.setString(1, codigo);

            result = smt.executeQuery();

            while (result.next()) {
                Buscar = new ProductosMd();

                Buscar.setCodigo(result.getString(1));
                Buscar.setDescripcion(result.getString(2));
                Buscar.setTipo_servicio(result.getString(3));
                Buscar.setPrecio_venta(result.getString(4));
                Buscar.setDescuento(result.getString(5));
                Buscar.setStock(result.getString(6));

            }
        } catch (Exception e) {
        } finally {
            if (smt != null) {
                smt.close();
            }
            if (result != null) {
                smt.close();
            }
            if (conexion != null) {
                conex.cerrar();
                conexion.close();
                conn = null;
            }
        }
        return Buscar;
    }

    public ProductosMd BuscarProducto(String codigo) throws SQLException {
        PreparedStatement smt = null;
        Connection conn;
        Conexion conex = new Conexion();
        conexion = cnn.Conexion();
        //conn = conex.getConnection();
        ResultSet result = null;
        int resp = 0;

        ProductosMd Buscar = new ProductosMd();

        String stock = "", productos = "";

        switch (session.getAttribute("SUCURSAL").toString()) {
            case "1":
                stock = "pro_stock_barrita";
                productos = "productos";
                break;
            case "2":
                stock = "pro_stock_carrizal";
                productos = "productos2";
                break;
            case "3":
                stock = "pro_stock_angeles";
                productos = "productos";
                break;

        }

        String sql = "SELECT P.PRO_ID,\n"
                + "       CONCAT( IFNULL(P.PRO_DESCRIPCION,' '),' ', IFNULL(P.PRO_MARCA,' ') ,' ',IFNULL((CASE P.PRO_PRESENTACION\n"
                + "                   WHEN 'U' THEN 'UNIDAD'\n"
                + "                    WHEN 'C' THEN 'CAJA'\n"
                + "                    WHEN 'B' THEN 'BOLSA'\n"
                + "                    WHEN 'L' THEN 'LITRO' ELSE ' ' END),' '),' ', case \n"
                + "  when P.PRO_CONVERSION IS NULL OR P.PRO_CONVERSION = 0 then ' '  \n"
                + "  else  CONCAT('X ',P.PRO_CONVERSION)\n"
                + "end) DESCRIPCION,\n"
                + "       P.PRO_TIPO_SERVICIO,\n"
                + "       IFNULL(FORMAT(P.PRO_PRECIO_VENTA,2),'-'),\n"
                + "       IFNULL(FORMAT(IFNULL(P.PRO_DESCUENTO,0),2),'-'),\n"
                + "       IFNULL(P." + stock + ",0)\n"
                + "FROM " + productos + " P  "
                + "   where P.PRO_ID = ?";

        try {
            smt = conexion.prepareStatement(sql);
            smt.setString(1, codigo);

            result = smt.executeQuery();

            while (result.next()) {
                Buscar = new ProductosMd();
                resp = 1;
                Buscar.setCodigo(result.getString(1));
                Buscar.setDescripcion(result.getString(2));
                Buscar.setTipo_servicio(result.getString(3));
                Buscar.setPrecio_venta(result.getString(4));
                Buscar.setDescuento(result.getString(5));
                Buscar.setStock(result.getString(6));
                Buscar.setResp("1");

            }
            if (resp == 0) {

                Buscar.setResp("0");

            }
        } catch (Exception e) {
        } finally {
            if (smt != null) {
                smt.close();
            }
            if (result != null) {
                smt.close();
            }
            if (conexion != null) {
                conex.cerrar();
                conexion.close();
                conn = null;
            }
        }
        return Buscar;
    }

    public List<ProductosMd> BuscarProductos() throws SQLException {
        PreparedStatement smt = null;
        Connection conn;
        Conexion conex = new Conexion();
        //conn = conex.();
        conexion = cnn.Conexion();

        ResultSet result = null;

        ProductosMd Buscar = null;

        List<ProductosMd> lista = new ArrayList<ProductosMd>();

        String sql = "SELECT P.PRO_ID, TRIM(NVL(P.PRO_DESCRIPCION,' ')||' '|| NVL(P.PRO_MARCA,' ') ||' '|| NVL(P.PRO_PRESENTACION,' ')||' '|| DECODE(P.PRO_CONVERSION,NULL,' ','X '||P.PRO_CONVERSION)) FROM productos P ";

        try {
            smt = conexion.prepareStatement(sql);

            result = smt.executeQuery();

            while (result.next()) {
                Buscar = new ProductosMd();

                Buscar.setCodigo(result.getString(1));
                Buscar.setDescripcion(result.getString(2));
                lista.add(Buscar);

            }
        } catch (Exception e) {
        } finally {
            if (smt != null) {
                smt.close();
            }
            if (result != null) {
                smt.close();
            }
            if (conexion != null) {
                conex.cerrar();
                conexion.close();
                conn = null;
            }
        }
        return lista;
    }

}
