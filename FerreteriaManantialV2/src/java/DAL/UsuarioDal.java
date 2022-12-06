package DAL;

import Conexion.Conexion;
import MD.UsuariosMd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.util.Clients;

public class UsuarioDal {

    Connection conexion = null;
    Conexion cnn = new Conexion();
    PreparedStatement ps = null;
    //PreparedStatement st = null;
//    Statement st = null;
//    ResultSet rs = null;

    public int UsuarioExiste(String user, String pass) throws ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        int resp = 0;
        String sql = "select * from almacen.usuarios where usu_usuario = '" + user + "' AND usu_password= '" + pass + "'   ";
        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                resp = 1;
            }
            conexion.close();
          //  cnn.cerrar();
        } catch (SQLException e) {
        }
        return resp;
    }

        public UsuariosMd REGselectUsuario(String usuario) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;

        UsuariosMd rg = new UsuariosMd();
        String query = "select"
                + " trim(usu_codigo),"
                + " trim(upper(usu_nombre)),"
                + " trim(usu_telefono),"
                + " trim(Upper(usu_usuario)),"
                + " trim(usu_password),"
                + " trim(usu_rol),"
                + " trim(usu_fecha_crea),"
                + " trim(usu_fecha_vence)"
                + " from almacen.usuarios u where upper(usu_usuario) = '" + usuario.toUpperCase() + "' ";
        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                rg.setCodigo(rs.getString(1));
                rg.setNombre(rs.getString(2));
                rg.setCelular(rs.getString(3));
                rg.setUsuario(rs.getString(4));
                rg.setPassword(rs.getString(5));
                rg.setRol(rs.getString(6));
                rg.setCrea(rs.getString(7));
                rg.setVence(rs.getString(8));
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
        return rg;
    }

    public List<UsuariosMd> REGselect(String cod) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        List<UsuariosMd> allUsuarios = new ArrayList<UsuariosMd>();
        String query = "select"
                + " trim(usu_codigo),"
                + " trim(usu_nombre),"
                + " trim(usu_telefono,)"
                + " trim(usu_usuario,)"
                + " trim(usu_password,)"
                + " trim(usu_rol,)"
                + " trim(usu_fecha_crea,)"
                + " trim(usu_fecha_vence)"
                + " from almacen.usuarios where usu_codigo = '" + cod + "' ";
        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            UsuariosMd rg = new UsuariosMd();
            while (rs.next()) {
                rg.setCodigo(rs.getString(1));
                rg.setNombre(rs.getString(2));
                rg.setCelular(rs.getString(3));
                rg.setUsuario(rs.getString(4));
                rg.setPassword(rs.getString(5));
                rg.setRol(rs.getString(6));
                rg.setCrea(rs.getString(7));
                rg.setVence(rs.getString(8));

                allUsuarios.add(rg);
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
        return allUsuarios;
    }

    public List<UsuariosMd> Rselect() throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        List<UsuariosMd> allUsuarios = new ArrayList<UsuariosMd>();
        String query = "select trim(usu_codigo),"
                + " trim(usu_nombre), "
                + " trim(usu_telefono), "
                + " trim(usu_usuario), "
                + " trim(usu_password), "
                + " trim(usu_rol), "
                + " trim(DATE_FORMAT(usu_fecha_crea, '%d/%m/%Y')),"
                + " trim(DATE_FORMAT(usu_fecha_vence, '%d/%m/%Y'))"
                + " from almacen.usuarios order by  usu_codigo asc";

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            UsuariosMd rg;
            while (rs.next()) {
                rg = new UsuariosMd();
                rg.setCodigo(rs.getString(1));
                rg.setNombre(rs.getString(2));
                rg.setCelular(rs.getString(3));
                rg.setUsuario(rs.getString(4));
                rg.setPassword(rs.getString(5));
                rg.setRol(rs.getString(6));
                rg.setCrea(rs.getString(7));
                rg.setVence(rs.getString(8));

                allUsuarios.add(rg);
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
        return allUsuarios;
    }

    public void Rinsert(String nombre, String celular, String usuario, String password,
                        String rol, String crea, String vence) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        String sql = "insert into almacen.usuarios"
                + "(usu_codigo,"
                + " usu_nombre,"
                + "usu_telefono,"
                + " usu_usuario,"
                + " usu_password,"
                + " usu_rol,"
                + " usu_fecha_crea,"
                + " usu_fecha_vence)"
                + " VALUES(?,?,?,?,?,?,STR_TO_DATE(?,'%d/%m/%Y'),STR_TO_DATE(?,'%d/%m/%Y'))";
        String sql0 = "select max(usu_codigo)+1 as codigo from almacen.usuarios";
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
            ps.setString(2, nombre);
            ps.setString(3, celular);
            ps.setString(4, usuario);
            ps.setString(5, password);
            ps.setString(6, rol);
            ps.setString(7, crea);
            ps.setString(8, vence);
            ps.executeUpdate();
            ps.close();

            conexion.commit();
            Clients.showNotification("REGISTRO CREADO <br/> CON EXITO  <br/>");
            conexion.close();
            conexion = null;
            cnn.cerrar();
            System.out.println("Conexion Cerrada" + conexion);

        } catch (SQLException e) {
            conexion.rollback();
            conexion.close();
            conexion = null;
            Clients.showNotification("ERROR AL INSERTAR <br/> <br/> REGISTROS! <br/> " + e.getMessage().toString(),
                    "warning", null, "middle_center", 0);
        }
    }

    public void Rupdate(String codigo, String nombre, String celular, String usuario, String password,
                        String rol, String crea, String vence) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        try {
            System.out.println("Conexion..: " + cnn);
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);
            System.out.println("ACTUALIZAR DATOS..!");
            System.out.println("Actualizar " + codigo);
            st = conexion.createStatement();

            st.executeUpdate("update almacen.usuarios set usu_nombre = '" + nombre + "'"
                    + ",usu_telefono = '" + celular + "'"
                    + ",usu_usuario = '" + usuario + "'"
                    + ",usu_password = '" + password + "'"
                    + ",usu_rol = '" + rol + "'"
                    + ",usu_fecha_crea = STR_TO_DATE('" + crea + "','%d/%m/%Y') "
                    + ",usu_fecha_vence = STR_TO_DATE('" + vence + "','%d/%m/%Y') "
                    + " where usu_codigo= '" + codigo + "' ");
            
            conexion.commit();
            Clients.showNotification("REGISTRO ACTUALIZADO <br/> CON EXITO  <br/>");
            System.out.println("Actualizacion Exitosa.! ");

            st.close();
            conexion.close();
            cnn.cerrar();
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

            st.executeUpdate("delete from almacen.usuarios where usu_codigo = " + codigo + " ");
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
}
