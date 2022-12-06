package DAL;

import Conexion.Conexion;
import MD.ActComprasMd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.util.Clients;

public class ActComprasDal {

    private Connection conexion = null;
    private Conexion cnn = new Conexion();
    PreparedStatement ps = null;
    //PreparedStatement st = null;
    //Statement st = null;
    // ResultSet rs = null;

    public List<ActComprasMd> REGselect(String codigo, String correlativo) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        List<ActComprasMd> allActCompras = new ArrayList<ActComprasMd>();
        String query = "select trim(mov_prod_codigo),"
                + " trim(mov_correlativo),"
                + " trim(mov_precio_unitario),"
                //+ " trim(mov_fecha_ingreso,)"
                //+ " trim(mov_fecha_vence,)"
                + " trim(mov_vendedor),"
                + " trim(mov_cantidad)"
                + " from farmacia_2021.mov_productos "
                + " where mov_prod_codigo = '" + codigo + "'\n "
                + " and mov_correlativo = '" + correlativo + "'\n";

        System.out.println("datos....  " + query);
        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            ActComprasMd rg = new ActComprasMd();
            while (rs.next()) {
            //System.out.println("despues que pasa el query....  ");
                rg.setCodigo(rs.getString(1));
                rg.setCorrelativo(rs.getString(2));
                rg.setPrecio(rs.getString(3));
                //rg.setIngreso(rs.getString(4));
                //rg.setVencimiento(rs.getString(5));
                rg.setVendedor(rs.getString(4));
                rg.setCantidad(rs.getString(5));
                //System.out.println("while Codigo:....   " + rs.getString(1));
                allActCompras.add(rg);
                
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
        return allActCompras;
    }

    /*   public List<ActComprasMd> RSelect() throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        List<ActComprasMd> allActCompras = new ArrayList<ActComprasMd>();
        String query = "select trim(mov_prod_codigo),"
                + " trim(mov_correlativo),"
                + " trim(mov_precio_unitario),"
                + " trim(DATE_FORMAT(mov_fecha_ingreso,'%d/%m/%Y')),"
                + " trim(DATE_FORMAT(mov_fecha_vence,'%d/%m/%Y')),"
                + " trim(mov_vendedor),"
                + " trim(mov_cantidad)"
                + " from farmacia_2021.mov_productos order by  mov_prod_codigo asc";

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            ActComprasMd rg;
            while (rs.next()) {
                rg = new ActComprasMd();
                rg.setCodigo(rs.getString(1));
                rg.setCorrelativo(rs.getString(2));
                rg.setPrecio(rs.getString(3));
                rg.setIngreso(rs.getString(4));
                rg.setVencimiento(rs.getString(5));
                rg.setVendedor(rs.getString(6));
                rg.setCantidad(rs.getString(7));

                allActCompras.add(rg);
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
        return allActCompras;
    }*/

 /*   public void REGinsert(String codigo, String correlativo, String precio,
            String ingreso, String vencimiento, String vendedor,
            String cantidad) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        String sql = "insert into farmacia_2021.mov_productos"
                + "(mov_prod_codigo,"
                + " mov_correlativo,"
                + " mov_precio_unitario,"
                + " mov_fecha_ingreso,"
                + " mov_fecha_vence,"
                + " mov_vendedor,"
                + " mov_cantidad)"
                + " VALUES(?,?,?,STR_TO_DATE(?,'%d/%m/%Y'),STR_TO_DATE(?,'%d/%m/%Y'),?,?)";
        //  String sql0 = "select max(mov_prod_codigo)+1 as codigo from mov_productos";
        try {
            System.out.println("precio linea 125...: " +sql);
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
            ps.setString(2, correlativo);
            ps.setString(3, precio);
            ps.setString(4, ingreso);
            ps.setString(5, vencimiento);
            ps.setString(6, vendedor);
            ps.setString(7, cantidad);
            
            System.out.println("precio linea 144...: " +ps);
           
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
     */
    public void REGupdate(String codigo, String correlativo, String precio,
           /* String ingreso, String vencimiento,*/ String vendedor,
            String cantidad) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        try {
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);
            System.out.println("ACTUALIZAR DATOS..!");
            System.out.println("Actualizar " + codigo);
            st = conexion.createStatement();

            st.executeUpdate("update farmacia_2021.mov_productos set"
                    + " mov_precio_unitario = '" + precio + "'"
                   // + ",mov_fecha_ingreso = STR_TO_DATE('" + ingreso + "','%d/%m/%Y') "
                  //  + ",mov_fecha_vence = STR_TO_DATE('" + vencimiento + "','%d/%m/%Y') "
                    + ",mov_vendedor = '" + vendedor + "'"
                    + ",mov_cantidad = '" + cantidad + "'"
                    + " where mov_prod_codigo = '" + codigo + "'"
                    + " and mov_correlativo = '" + correlativo + "'");

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

    /*public void REGdelete(String codigo, String correlativo) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        try {
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);
            System.out.println("ELIMINAR DATOS..!");
            System.out.println("Eliminar " + codigo);
            System.out.println("Eliminar " + correlativo);

            st = conexion.createStatement();

            st.executeUpdate("delete farmacia_2021.mov_productos where mov_prod_codigo = '" + codigo + "'"
                    + "and mov_correlativo");
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

    }*/

 /*    public void CalculaMov(String codigo, String correla, String cantidad) throws ClassNotFoundException, SQLException {
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

            st.executeUpdate("update farmacia_2021.productos "
                    + "set prod_saldo = prod_saldo +" + valor + " "
                    + " where prod_codigo = '" + codigo + "'  ");

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
    }*/

 /*  public String Correlativo(String codigo) throws ClassNotFoundException, SQLException {
        Statement st = null;
        ResultSet rs = null;
        String query = "select count(mov_correlativo)+1 as correlativo from farmacia_2021.mov_productos where mov_prod_codigo='" + codigo + "' ";
        String resp = "";
        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                resp = rs.getString("correlativo");
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
    }*/
}
