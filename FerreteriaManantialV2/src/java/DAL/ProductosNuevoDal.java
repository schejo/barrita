/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import Conexion.Conexion;
import MD.ProductosNuevoMd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Desktop;

import org.zkoss.zk.ui.util.Clients;

/**
 *
 * @author chejo
 */
public class ProductosNuevoDal {

    Conexion obtener = new Conexion();
    Connection conn;
    ProductosNuevoMd cl = new ProductosNuevoMd();
    private Connection conexion = null;
    private Conexion cnn = new Conexion();
    PreparedStatement ps = null;
    Statement st = null;
    ResultSet rs = null;
    
      public List<ProductosNuevoMd> RSelect2Tabla() throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;//copie de usuarios
        List<ProductosNuevoMd> allProductos = new ArrayList<ProductosNuevoMd>();
        String query = "select  pro_id\n"
                + " from productos2\n"
                + " order by  pro_id asc";

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            ProductosNuevoMd rg;
            while (rs.next()) {
                rg = new ProductosNuevoMd();
                rg.setCodigo(rs.getString(1));

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

    public List<ProductosNuevoMd> RSelect() throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;//copie de usuarios
        List<ProductosNuevoMd> allProductos = new ArrayList<ProductosNuevoMd>();
        String query = "select  pro_id\n"
                + " from almacen.productos\n"
                + " order by  pro_id asc";

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            ProductosNuevoMd rg;
            while (rs.next()) {
                rg = new ProductosNuevoMd();
                rg.setCodigo(rs.getString(1));

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

    public ProductosNuevoMd BusProducto(String curso) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ProductosNuevoMd();
        String query0 = " select pro_descripcion\n"
                + "from productos\n"
                + "where pro_descripcion='" + curso + "';";
        try {
            conn = obtener.Conexion();

            st = conn.createStatement();
            rs = st.executeQuery(query0);
            while (rs.next()) {
                resp = 1;
                cl.setNombre_pro(rs.getString(1));

                cl.setResp("1");
                cl.setMsg("PRODUCTO YA EXISTE");
            }
            st.close();
            rs.close();

            if (resp == 0) {

                cl.setResp("0");
                cl.setMsg("PRODUCTO NO EXISTE ");

            } else {
                cl.setResp("0");
                cl.setMsg("PRODUCTO YA EXISTE <br/>  <br/>  CONSULTAR REPORTE DE PRODUCTOS");
            }

        } catch (Exception e) {
            System.out.println("ERROR CATCH.: " + e.getMessage());
            cl.setResp("0");
            cl.setMsg(e.getMessage());

        }

        return cl;
    }

    public ProductosNuevoMd updateProAngeles(ProductosNuevoMd data) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ProductosNuevoMd();
        try {
            conn = obtener.Conexion();
            conn.setAutoCommit(false);
            int vl = 0;
            st = conn.createStatement();
            vl = st.executeUpdate("UPDATE productos SET "
                    + "pro_descripcion='" + data.getDescripcion() + "',"
                    + "pro_tipo='" + data.getTipo_pro() + "',pro_tipo_servicio='" + data.getTipo_ser() + "',pro_marca='" + data.getMarca()
                    + "',pro_presentacion='" + data.getPresentacion() + "',pro_precio_compra='" + data.getPre_compra() + "',pro_precio_venta='" + data.getPre_venta()
                    + "',pro_descuento='" + data.getDescuento() + "',pro_stock_angeles='" + data.getPro_stock_Barrita()
                    + "',pro_conversion='" + data.getPro_conver() + "',pro_medida='" + data.getMedi_pro()
                    + "',pro_minimo='" + data.getMinimo() + "',pro_maximo='" + data.getMaximo() + "',"
                    + "pro_ubicacion='" + data.getUbicacion() + "',pro_angeles='1',pro_fecha_modifica=NOW(),pro_usuario_modifica='" + data.getUsuario()+ "' "
                    + "where pro_id='" + data.getCodigo() + "' ");
            if (vl > 0) {
                cl.setResp("1");
                cl.setMsg("DATOS ACTUALIZADOS CORRECTAMENTE");
                System.out.println("Actualizacion Exitosa");
            } else {
                cl.setResp("0");
                cl.setMsg("DATOS NO ACTUALIZADOS");
                System.out.println("Actualizacion Fallida");
            }
            st.close();

            conn.commit();
            conn.close();
            obtener.desconectar();

        } catch (Exception e) {
            System.out.println("ERROR CATCH.: " + e.getMessage());
            cl.setResp("0");
            cl.setMsg(e.getMessage());

        }

        return cl;
    }

    public ProductosNuevoMd saveProBarrita(ProductosNuevoMd data) throws ClassNotFoundException, SQLException {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;

        cl = new ProductosNuevoMd();
        String query1 = " SELECT max(pro_id)+1 as id FROM productos ";
        String sql = " INSERT INTO productos "
                + "(pro_id,pro_descripcion,pro_tipo,pro_tipo_servicio,pro_marca,pro_presentacion,pro_precio_compra,pro_precio_venta"
                + ",pro_descuento,pro_stock_barrita,pro_conversion,pro_medida,pro_minimo,pro_maximo,pro_ubicacion,pro_ferreteria,pro_fecha_alta,pro_usuario_alta)"
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'1',NOW(),?);";
        try {
            conn = obtener.Conexion();
            conn.setAutoCommit(false);

            st = conn.createStatement();
            rs = st.executeQuery(query1);
            while (rs.next()) {
                id = rs.getString("id");
            }
            st.close();
            rs.close();

            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            //ps.setString(1, data.getCodigo());
            // ps.setString(2, data.getCodigo_curso());
            ps.setString(2, data.getDescripcion());
            ps.setString(3, data.getTipo_pro());
            ps.setString(4, data.getTipo_ser());
            ps.setString(5, data.getMarca());
            ps.setString(6, data.getPresentacion());
            ps.setString(7, data.getPre_compra());
            ps.setString(8, data.getPre_venta());
            ps.setString(9, data.getDescuento());
            ps.setString(10, data.getPro_stock_Barrita());
            ps.setString(11, data.getPro_conver());
            ps.setString(12, data.getMedi_pro());
            ps.setString(13, data.getMinimo());
            ps.setString(14, data.getMaximo());
            ps.setString(15, data.getUbicacion());
            ps.setString(16, data.getUsuario());

            ps.executeUpdate();
            ps.close();
            conn.commit();
            cl.setCodigo(id);
            cl.setResp("1");
            cl.setMsg("REGISTRO GUARDADO CORRECTAMENTE");

            conn.close();
            obtener.desconectar();

        } catch (SQLException e) {
            System.out.println("EXCEPTION..: " + e.getMessage());
            cl.setResp("0");
            cl.setMsg(e.getMessage());
        }

        return cl;
    }

    public ProductosNuevoMd saveProCarrizal(ProductosNuevoMd data) throws ClassNotFoundException, SQLException {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ProductosNuevoMd();
        String query1 = " SELECT max(pro_id)+1 as id FROM productos2 ";
        String sql = " INSERT INTO productos2 "
                + "(pro_id,pro_descripcion,pro_tipo,pro_tipo_servicio,pro_marca,pro_presentacion,pro_precio_compra,pro_precio_venta"
                + ",pro_descuento,pro_stock_carrizal,pro_conversion,pro_medida,pro_minimo,pro_maximo,pro_ubicacion,pro_carrizal,pro_fecha_alta,pro_usuario_alta)"
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'1',now(),?);";
        try {
            conn = obtener.Conexion();
            conn.setAutoCommit(false);

            st = conn.createStatement();
            rs = st.executeQuery(query1);
            while (rs.next()) {
                id = rs.getString("id");
            }
            st.close();
            rs.close();

            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            //ps.setString(1, data.getCodigo());
            // ps.setString(2, data.getCodigo_curso());
            ps.setString(2, data.getDescripcion());
            ps.setString(3, data.getTipo_pro());
            ps.setString(4, data.getTipo_ser());
            ps.setString(5, data.getMarca());
            ps.setString(6, data.getPresentacion());
            ps.setString(7, data.getPre_compra());
            ps.setString(8, data.getPre_venta());
            ps.setString(9, data.getDescuento());
            ps.setString(10, data.getPro_stock_Barrita());
            ps.setString(11, data.getPro_conver());
            ps.setString(12, data.getMedi_pro());
            ps.setString(13, data.getMinimo());
            ps.setString(14, data.getMaximo());
            ps.setString(15, data.getUbicacion());
            ps.setString(16, data.getUsuario());

            ps.executeUpdate();
            ps.close();
            conn.commit();
            cl.setCodigo(id);
            cl.setResp("1");
            cl.setMsg("REGISTRO GUARDADO CORRECTAMENTE");

            conn.close();
            obtener.desconectar();

        } catch (SQLException e) {
            System.out.println("EXCEPTION..: " + e.getMessage());
            cl.setResp("0");
            cl.setMsg(e.getMessage());
        }

        return cl;
    }

    public ProductosNuevoMd saveProAngeles(ProductosNuevoMd data) throws ClassNotFoundException, SQLException {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ProductosNuevoMd();
        String query1 = " SELECT max(pro_id)+1 as id FROM productos ";
        String sql = " INSERT INTO productos "
                + "(pro_id,pro_descripcion,pro_tipo,pro_tipo_servicio,pro_marca,pro_presentacion,pro_precio_compra,pro_precio_venta"
                + ",pro_descuento,pro_stock_angeles,pro_conversion,pro_medida,pro_minimo,pro_maximo,pro_ubicacion,pro_angeles,pro_fecha_alta,pro_usuario_alta)"
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'1',NOW(),?);";
        try {
            conn = obtener.Conexion();
            conn.setAutoCommit(false);

            st = conn.createStatement();
            rs = st.executeQuery(query1);
            while (rs.next()) {
                id = rs.getString("id");
            }
            st.close();
            rs.close();

            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            //ps.setString(1, data.getCodigo());
            // ps.setString(2, data.getCodigo_curso());
            ps.setString(2, data.getDescripcion());
            ps.setString(3, data.getTipo_pro());
            ps.setString(4, data.getTipo_ser());
            ps.setString(5, data.getMarca());
            ps.setString(6, data.getPresentacion());
            ps.setString(7, data.getPre_compra());
            ps.setString(8, data.getPre_venta());
            ps.setString(9, data.getDescuento());
            ps.setString(10, data.getPro_stock_Barrita());
            ps.setString(11, data.getPro_conver());
            ps.setString(12, data.getMedi_pro());
            ps.setString(13, data.getMinimo());
            ps.setString(14, data.getMaximo());
            ps.setString(15, data.getUbicacion());
            ps.setString(16, data.getUsuario());

            ps.executeUpdate();
            ps.close();
            conn.commit();
            cl.setCodigo(id);
            cl.setResp("1");
            cl.setMsg("REGISTRO GUARDADO CORRECTAMENTE");

            conn.close();
            obtener.desconectar();

        } catch (SQLException e) {
            System.out.println("EXCEPTION..: " + e.getMessage());
            cl.setResp("0");
            cl.setMsg(e.getMessage());
        }

        return cl;
    }

    public ProductosNuevoMd updateProCarrizal(ProductosNuevoMd data) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ProductosNuevoMd();
        try {
            conn = obtener.Conexion();
            conn.setAutoCommit(false);
            int vl = 0;
            st = conn.createStatement();
            vl = st.executeUpdate("UPDATE productos2 SET "
                    + "pro_descripcion='" + data.getDescripcion() + "',"
                    + "pro_tipo='" + data.getTipo_pro() + "',pro_tipo_servicio='" + data.getTipo_ser() + "',pro_marca='" + data.getMarca()
                    + "',pro_presentacion='" + data.getPresentacion() + "',pro_precio_compra='" + data.getPre_compra() + "',pro_precio_venta='" + data.getPre_venta()
                    + "',pro_descuento='" + data.getDescuento() + "',pro_stock_carrizal='" + data.getPro_stock_Barrita()
                    + "',pro_conversion='" + data.getPro_conver() + "',pro_medida='" + data.getMedi_pro()
                    + "',pro_minimo='" + data.getMinimo() + "',pro_maximo='" + data.getMaximo() + "',"
                    + "pro_ubicacion='" + data.getUbicacion() + "',pro_carrizal=1,pro_fecha_modifica=NOW(),pro_usuario_modifica='" + data.getUsuario() + "'"
                    + "where pro_id='" + data.getCodigo() + "' ");
            if (vl > 0) {
                cl.setResp("1");
                cl.setMsg("DATOS ACTUALIZADOS CORRECTAMENTE");
                System.out.println("Actualizacion Exitosa");
            } else {
                cl.setResp("0");
                cl.setMsg("DATOS NO ACTUALIZADOS");
                System.out.println("Actualizacion Fallida");
            }
            st.close();

            conn.commit();
            conn.close();
            obtener.desconectar();

        } catch (Exception e) {
            System.out.println("ERROR CATCH.: " + e.getMessage());
            cl.setResp("0");
            cl.setMsg(e.getMessage());

        }

        return cl;
    }

    public ProductosNuevoMd updatePro(ProductosNuevoMd data) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ProductosNuevoMd();
        try {
            conn = obtener.Conexion();
            conn.setAutoCommit(false);
            int vl = 0;
            st = conn.createStatement();
            vl = st.executeUpdate("UPDATE productos SET "
                    + "pro_descripcion='" + data.getDescripcion() + "',"
                    + "pro_tipo='" + data.getTipo_pro() + "',pro_tipo_servicio='" + data.getTipo_ser() + "',pro_marca='" + data.getMarca()
                    + "',pro_presentacion='" + data.getPresentacion() + "',pro_precio_compra='" + data.getPre_compra() + "',pro_precio_venta='" + data.getPre_venta()
                    + "',pro_descuento='" + data.getDescuento() + "',pro_stock_barrita='" + data.getPro_stock_Barrita()
                    + "',pro_conversion='" + data.getPro_conver() + "',pro_medida='" + data.getMedi_pro()
                    + "',pro_minimo='" + data.getMinimo() + "',pro_maximo='" + data.getMaximo() + "'"
                    + ",pro_ubicacion='" + data.getUbicacion() + "'"
                    + ",pro_ferreteria='1',pro_fecha_modifica=NOW(),pro_usuario_modifica='" + data.getUsuario() + "' "
                    + "where pro_id='" + data.getCodigo() + "' ");
            if (vl > 0) {
                cl.setResp("1");
                cl.setMsg("DATOS ACTUALIZADOS CORRECTAMENTE");
                System.out.println("Actualizacion Exitosa");
            } else {
                cl.setResp("0");
                cl.setMsg("DATOS NO ACTUALIZADOS");
                System.out.println("Actualizacion Fallida");
            }
            st.close();

            conn.commit();
            conn.close();
            obtener.desconectar();

        } catch (Exception e) {
            System.out.println("ERROR CATCH.: " + e.getMessage());
            cl.setResp("0");
            cl.setMsg(e.getMessage());

        }

        return cl;
    }

    public ProductosNuevoMd savePro(ProductosNuevoMd data) throws ClassNotFoundException, SQLException {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ProductosNuevoMd();
        String query1 = " SELECT max(pro_id)+1 as id FROM productos ";
        String sql = " INSERT INTO productos "
                + "(pro_id,pro_descripcion,pro_tipo,pro_tipo_servicio,pro_marca,pro_presentacion,pro_precio_compra,pro_precio_venta"
                + ",pro_descuento,pro_stock_barrita,pro_conversion,pro_medida,pro_minimo,pro_maximo,pro_ubicacion,pro_ferreteria)"
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try {
            conn = obtener.Conexion();
            conn.setAutoCommit(false);

            st = conn.createStatement();
            rs = st.executeQuery(query1);
            while (rs.next()) {
                id = rs.getString("id");
            }
            st.close();
            rs.close();

            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            //ps.setString(1, data.getCodigo());
            // ps.setString(2, data.getCodigo_curso());
            ps.setString(2, data.getDescripcion());
            ps.setString(3, data.getTipo_pro());
            ps.setString(4, data.getTipo_ser());
            ps.setString(5, data.getMarca());
            ps.setString(6, data.getPresentacion());
            ps.setString(7, data.getPre_compra());
            ps.setString(8, data.getPre_venta());
            ps.setString(9, data.getDescuento());
            ps.setString(10, data.getPro_stock());
            ps.setString(11, data.getPro_conver());
            ps.setString(12, data.getMedi_pro());
            ps.setString(13, data.getMinimo());
            ps.setString(14, data.getMaximo());
            ps.setString(15, data.getUbicacion());
            ps.setString(16, data.getUbicacionFerre());

            ps.executeUpdate();
            ps.close();
            conn.commit();
            cl.setCodigo(id);
            cl.setResp("1");
            cl.setMsg("REGISTRO GUARDADO CORRECTAMENTE");

            conn.close();
            obtener.desconectar();

        } catch (SQLException e) {
            System.out.println("EXCEPTION..: " + e.getMessage());
            cl.setResp("0");
            cl.setMsg(e.getMessage());
        }

        return cl;
    }

    public ProductosNuevoMd MostrarAngeles(String producto) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ProductosNuevoMd();
        String query0 = "SELECT pro_descripcion,pro_tipo,pro_tipo_servicio,pro_marca,pro_presentacion,pro_precio_compra,pro_precio_venta,\n"
                + "pro_descuento,pro_stock_angeles,pro_conversion,pro_medida,pro_minimo,pro_maximo,pro_ubicacion,pro_ferreteria\n"
                + "from  almacen.productos\n"
                + " where pro_id='" + producto + "';";
        try {
            conn = obtener.Conexion();

            st = conn.createStatement();
            rs = st.executeQuery(query0);
            while (rs.next()) {
                resp = 1;
                cl.setDescripcion(rs.getString(1));
                cl.setTipo_pro(rs.getString(2));

                cl.setTipo_ser(rs.getString(3));
                cl.setMarca(rs.getString(4));
                cl.setPresentacion(rs.getString(5));
                cl.setPre_compra(rs.getString(6));
                cl.setPre_venta(rs.getString(7));
                cl.setDescuento(rs.getString(8));
                cl.setPro_stock(rs.getString(9));
                cl.setPro_conver(rs.getString(10));
                cl.setMedi_pro(rs.getString(11));
                cl.setMinimo(rs.getString(12));
                cl.setMaximo(rs.getString(13));
                cl.setUbicacion(rs.getString(14));
                cl.setUbicacionFerre(rs.getString(15));
                cl.setResp("1");
                cl.setMsg("ACTUALIZAR DATOS DE CATEDRATICO.!");
            }
            st.close();
            rs.close();

            if (resp == 0) {

                cl.setResp("0");
                cl.setMsg("PRODCUTO NO EXISTE <br/>  <br/>  CONSULTAR REPORTE DE PRODUCTOS");

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

    public ProductosNuevoMd MostrarCarrizal(String producto) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ProductosNuevoMd();
        String query0 = "SELECT pro_descripcion,pro_tipo,pro_tipo_servicio,pro_marca,pro_presentacion,pro_precio_compra,pro_precio_venta,\n"
                + "pro_descuento,pro_stock_carrizal,pro_conversion,pro_medida,pro_minimo,pro_maximo,pro_ubicacion,pro_ferreteria\n"
                + "from  almacen.productos2\n"
                + " where pro_id='" + producto + "';";
        try {
            conn = obtener.Conexion();

            st = conn.createStatement();
            rs = st.executeQuery(query0);
            while (rs.next()) {
                resp = 1;
                cl.setDescripcion(rs.getString(1));
                cl.setTipo_pro(rs.getString(2));

                cl.setTipo_ser(rs.getString(3));
                cl.setMarca(rs.getString(4));
                cl.setPresentacion(rs.getString(5));
                cl.setPre_compra(rs.getString(6));
                cl.setPre_venta(rs.getString(7));
                cl.setDescuento(rs.getString(8));
                cl.setPro_stock(rs.getString(9));
                cl.setPro_conver(rs.getString(10));
                cl.setMedi_pro(rs.getString(11));
                cl.setMinimo(rs.getString(12));
                cl.setMaximo(rs.getString(13));
                cl.setUbicacion(rs.getString(14));
                cl.setUbicacionFerre(rs.getString(15));
                cl.setResp("1");
                cl.setMsg("ACTUALIZAR DATOS DE CATEDRATICO.!");
            }
            st.close();
            rs.close();

            if (resp == 0) {

                cl.setResp("0");
                cl.setMsg("PRODCUTO NO EXISTE <br/>  <br/>  CONSULTAR REPORTE DE PRODUCTOS");

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

    public ProductosNuevoMd MostrarCorreVale() {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ProductosNuevoMd();
        String query0 = "select max(fac_numero_dte)+1 from factura where fac_nombre_certificador='VALE';";
//                + "SELECT pro_descripcion,pro_tipo,pro_tipo_servicio,pro_marca,pro_presentacion,pro_precio_compra,pro_precio_venta,\n"
//                + "pro_descuento,pro_stock_barrita,pro_stock_Carrizal,pro_stock_angeles,pro_conversion,pro_medida,pro_minimo,pro_maximo,pro_ubicacion,pro_ferreteria\n"
//                + "from  almacen.productos\n"
//                + " where pro_id='" + producto + "';";
        try {
            conn = obtener.Conexion();

            st = conn.createStatement();
            rs = st.executeQuery(query0);
            while (rs.next()) {
                resp = 1;
                cl.setCorrelaVale(rs.getString(1));

                cl.setResp("1");
                cl.setMsg("ACTUALIZAR DATOS DE CATEDRATICO.!");
            }
            st.close();
            rs.close();

            if (resp == 0) {

                cl.setResp("0");
                cl.setMsg("PRODCUTO NO EXISTE <br/>  <br/>  CONSULTAR REPORTE DE PRODUCTOS");

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

    public ProductosNuevoMd MostrarProducto(String producto) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ProductosNuevoMd();
        String query0 = "SELECT pro_descripcion,pro_tipo,pro_tipo_servicio,pro_marca,pro_presentacion,pro_precio_compra,pro_precio_venta,\n"
                + "pro_descuento,pro_stock_barrita,pro_stock_Carrizal,pro_stock_angeles,pro_conversion,pro_medida,pro_minimo,pro_maximo,pro_ubicacion,pro_ferreteria\n"
                + "from  almacen.productos\n"
                + " where pro_id='" + producto + "';";
        try {
            conn = obtener.Conexion();

            st = conn.createStatement();
            rs = st.executeQuery(query0);
            while (rs.next()) {
                resp = 1;
                cl.setDescripcion(rs.getString(1));
                cl.setTipo_pro(rs.getString(2));

                cl.setTipo_ser(rs.getString(3));
                cl.setMarca(rs.getString(4));
                cl.setPresentacion(rs.getString(5));
                cl.setPre_compra(rs.getString(6));
                cl.setPre_venta(rs.getString(7));
                cl.setDescuento(rs.getString(8));
                cl.setPro_stock(rs.getString(9));
                cl.setPro_stock_Barrita(rs.getString(9));
                cl.setPro_stock_Carrizal(rs.getString(10));
                cl.setPro_stock_Angeles(rs.getString(11));
                cl.setPro_conver(rs.getString(12));
                cl.setMedi_pro(rs.getString(13));
                cl.setMinimo(rs.getString(14));
                cl.setMaximo(rs.getString(15));
                cl.setUbicacion(rs.getString(16));
                cl.setUbicacionFerre(rs.getString(17));
                cl.setResp("1");
                cl.setMsg("ACTUALIZAR DATOS DE CATEDRATICO.!");
            }
            st.close();
            rs.close();

            if (resp == 0) {

                cl.setResp("0");
                cl.setMsg("PRODCUTO NO EXISTE <br/>  <br/>  CONSULTAR REPORTE DE PRODUCTOS");

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

    public List<ProductosNuevoMd> allCL() {
        List<ProductosNuevoMd> data = new ArrayList<ProductosNuevoMd>();
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;

        String query0 = " SELECT pro_id, concat(pro_descripcion,' Codigo: ',pro_id) FROM productos ";
        try {
            conn = obtener.Conexion();

            st = conn.createStatement();
            rs = st.executeQuery(query0);
            while (rs.next()) {
                cl = new ProductosNuevoMd();
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

                cl.setResp("0");
                cl.setMsg("error comunicarse con sistemas");
            }
            conn.close();
            obtener.desconectar();

        } catch (Exception e) {
            System.out.println("ERROR CATCH.: " + e.getMessage());
            cl.setResp("0");
            cl.setMsg(e.getMessage());

        }

        return data;
    }
    
       public List<ProductosNuevoMd> MosProc2() {
        List<ProductosNuevoMd> data = new ArrayList<ProductosNuevoMd>();
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;

        String query0 = " SELECT pro_id, concat(pro_descripcion,' Codigo: ',pro_id) FROM productos2 ";
        try {
            conn = obtener.Conexion();

            st = conn.createStatement();
            rs = st.executeQuery(query0);
            while (rs.next()) {
                cl = new ProductosNuevoMd();
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

                cl.setResp("0");
                cl.setMsg("error comunicarse con sistemas");
            }
            conn.close();
            obtener.desconectar();

        } catch (Exception e) {
            System.out.println("ERROR CATCH.: " + e.getMessage());
            cl.setResp("0");
            cl.setMsg(e.getMessage());

        }

        return data;
    }

}
