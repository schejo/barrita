package DAL;

import Conexion.Conexion;
import MD.BajasMd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.util.Clients;

public class BajasDal {

    Connection conexion = null;
    Conexion cnn = new Conexion();
    PreparedStatement ps = null;

    public List<BajasMd> REGselect(String codigo) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        List<BajasMd> allBajas = new ArrayList<BajasMd>();
        String query = "select trim(baj_pro_codigo),"
                + " trim(baj_correlativo),"
                + " trim(baj_cantidad,)"
                + " trim(baj_precio,)"
                + " trim(baj_usuario,)"
                + " trim(baj_fecha,)"
                + " trim(baj_total)"
                + " from almacen.bajas where baj_pro_codigo = '" + codigo + "' ";
        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            BajasMd rg = new BajasMd();
            while (rs.next()) {
                rg.setCodigo(rs.getString(1));
                rg.setCorrelativo(rs.getString(2));
                rg.setCantidad(rs.getString(3));
                rg.setPrecio(rs.getString(4));
                rg.setUsuario(rs.getString(5));
                rg.setFecha(rs.getString(6));
                rg.setTotal(rs.getString(7));

                allBajas.add(rg);
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
        return allBajas;
    }

    public List<BajasMd> RSelect() throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        List<BajasMd> allBajas = new ArrayList<BajasMd>();
        String query = "select trim(baj_pro_codigo),"
                + " trim(baj_correlativo),"
                + " trim(baj_cantidad),"
                + " trim(baj_precio),"
                + " trim(baj_usuario),"
                + " trim(DATE_FORMAT(baj_fecha,'%d/%m/%Y')),"
                + " trim(baj_total)"
                + " from almacen.bajas order by  baj_pro_codigo asc";

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            BajasMd rg;
            while (rs.next()) {
                rg = new BajasMd();
                rg.setCodigo(rs.getString(1));
                rg.setCorrelativo(rs.getString(2));
                rg.setCantidad(rs.getString(3));
                rg.setPrecio(rs.getString(4));
                rg.setUsuario(rs.getString(5));
                rg.setFecha(rs.getString(6));
                rg.setTotal(rs.getString(7));

                allBajas.add(rg);
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
        return allBajas;
    }

    public void REGinsert(String codigo, String correlativo, String cantidad, String precio,
            String usuario, String fecha,/* String descuento,*/ String total) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        String sql = "insert into almacen.bajas"
                + "(baj_pro_codigo,"
                + " baj_correlativo,"
                + " baj_cantidad,"
                + " baj_precio,"
                + " baj_usuario,"
                + " baj_fecha,"
                + " baj_total)"
                + " VALUES(?,?,?,?,?,STR_TO_DATE(?,'%d/%m/%Y'),?)";
        
           String sql0 = "select max(baj_pro_codigo)+1 as codigo from bajas";
        try {
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);
            st = conexion.createStatement();

             rs = st.executeQuery(sql0);
            String codigos = "";
            while (rs.next()) {
                codigos = rs.getString("codigo");
            }
            ps = conexion.prepareStatement(sql);
            ps.setString(1, codigo);
            ps.setString(2, correlativo);
            ps.setString(3, cantidad);
            ps.setString(4, precio);
            ps.setString(5, usuario);
            ps.setString(6, fecha);
            ps.setString(7, total);
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
            String usuario, String fecha, String total) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        try {
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);
            System.out.println("ACTUALIZAR DATOS..!");
            System.out.println("Actualizar " + codigo);
            st = conexion.createStatement();

            st.executeUpdate("update almacen.bajas set"
                    + ",baj_correlativo = '" + correlativo + "'"
                    + ",baj_cantidad = '" + cantidad + "'"
                    + ",baj_precio = '" + precio + "'"
                    + ",baj_usuario = '" + usuario + "'"
                    + ",baj_fecha = STR_TO_DATE('" + fecha + "','%d/%m/%Y') "
                    + ",baj_total = '" + total + "'"
                    + " where baj_pro_codigo = '" + codigo + "' ");

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

            st.executeUpdate("delete almacen.ventas where ven_pro_codigo = '" + codigo + "' ");
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

    public List<BajasMd> Correlativo(String codigo) throws ClassNotFoundException, SQLException {
        Statement st = null;
        ResultSet rs = null;
        String query = "select count(baj_correlativo)+1 as correlativo, baj_precio from almacen.bajas where baj_pro_codigo='" + codigo + "' ";
        List<BajasMd> allBajas = new ArrayList<BajasMd>();
        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            BajasMd rg;
            while (rs.next()) {
                rg = new BajasMd();
                rg.setCorrelativo(rs.getString(1));
                rg.setPrecio(rs.getString(2));
                allBajas.add(rg);
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
        return allBajas;
    }
    
        public void CalculaMov(String codigo, String correla, String cantidad) throws ClassNotFoundException, SQLException{
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
                    + "set pro_stock_barrita = pro_stock_barrita-"+valor+" "
                    + " where pro_id = '"+codigo+"'  ");

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
        
  public String Existencia(String codigo) throws ClassNotFoundException, SQLException{
        Statement st = null;
        ResultSet rs = null;
        String query = "select pro_stock_barrita from almacen.productos where pro_id ='"+codigo+"' ";
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
      

