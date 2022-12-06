/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import Conexion.Conexion;
import MD.ProveedoresMd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.util.Clients;

/**
 *
 * @author chejo
 */
public class ProveedoresDal {
     Connection conexion;
    Conexion cnn = new Conexion();
    ProveedoresMd cl = new ProveedoresMd();
    Statement st = null;
    ResultSet rs = null;
    
    
     public ProveedoresMd findProve(String cliente) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ProveedoresMd();
        String query0 = " select pr_nombre,pr_nit,pr_direccion,pr_telefono,pr_correo from proveedores "
                + "where pr_nombre='" + cliente + "';";
        try {
            conexion = cnn.Conexion();

            st = conexion.createStatement();
            rs = st.executeQuery(query0);
            while (rs.next()) {
                resp = 1;
                cl.setNombreComercial(rs.getString(1));
                cl.setNit(rs.getString(2));
                cl.setDireccion(rs.getString(3));
                cl.setTelefono(rs.getString(4));
                cl.setCorreCleinte(rs.getString(5));
                cl.setResp("1");
                cl.setMsg("ACTUALIZAR DATOS DE Cliente.!");
            }
            st.close();
            rs.close();

            if (resp == 0) {

                cl.setResp("0");
                cl.setMsg("cliente NO EXISTE <br/>  <br/>  CONSULTAR REPORTE DE cliente");

            }
            conexion.close();
            cnn.desconectar();

        } catch (Exception e) {
            System.out.println("ERROR CATCH.: " + e.getMessage());
            cl.setResp("0");
            cl.setMsg(e.getMessage());

        }

        return cl;
    }
    
     public ProveedoresMd updateCur(ProveedoresMd data) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ProveedoresMd();
        try {
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);
            int vl = 0;
            st = conexion.createStatement();
            vl = st.executeUpdate("UPDATE proveedores SET pr_NOMBRE='" + data.getNombreComercial()+ "',pr_NIT='" 
                    + data.getNit()+ "',"
                    + "pr_DIRECCION='" + data.getDireccion()+ "',pr_TELEFONO='" + data.getTelefono()+ "',"
                    + "pr_FECHA_MODIFICA=  now()  ,pr_USUARIO_MODIFICA='" + data.getUsuario()
                    + "',pr_CORREO='" + data.getCorreCleinte()
                    + "'where pr_NOMBRE='" + data.getNombreMos()+ "' ");
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

            conexion.commit();
            conexion.close();
            cnn.desconectar();

        } catch (Exception e) {
            System.out.println("ERROR CATCH.: " + e.getMessage());
            cl.setResp("0");
            cl.setMsg(e.getMessage());

        }

        return cl;
    }
    
      public List<ProveedoresMd> allProve() {
        List<ProveedoresMd> data = new ArrayList<ProveedoresMd>();
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;

        String query0 = " select pr_id, pr_nombre from proveedores; ";
        try {
            conexion = cnn.Conexion();

            st = conexion.createStatement();
            rs = st.executeQuery(query0);
            while (rs.next()) {
                cl = new ProveedoresMd();
                resp = 1;
                cl.setCodigoProveedorMos(rs.getString(1));
                cl.setNombreMos(rs.getString(2));
                //cl.setDir(rs.getString(3));
                // cl.setResp("1");
                cl.setMsg("TODOS LOS Proveedores.!");
                data.add(cl);
            }
            st.close();
            rs.close();

            if (resp == 0) {

                cl.setResp("0");
                cl.setMsg("cliente NO EXISTEN");
            }
            conexion.close();
            cnn.desconectar();

        } catch (Exception e) {
            System.out.println("ERROR CATCH.: " + e.getMessage());
            cl.setResp("0");
            cl.setMsg(e.getMessage());

        }

        return data;
    }
    
    
     public List<ProveedoresMd> REGselect() throws SQLException, ClassNotFoundException {
        List<ProveedoresMd> allReporteCli = new ArrayList<ProveedoresMd>();

        String query = "SELECT * FROM almacen.proveedores;";

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            ProveedoresMd rg;
          
            while (rs.next()) {
                rg = new ProveedoresMd();
               
                rg.setCodigoCliente(rs.getString(1));
                rg.setNombreComercial(rs.getString(2));
                rg.setNit(rs.getString(3));
                rg.setDireccion(rs.getString(4));
                rg.setTelefono(rs.getString(5));
                rg.setFechaAlta(rs.getString(6));
                rg.setUsuarioAlta(rs.getString(7));
                rg.setFechaModi(rs.getString(8));
                rg.setUsuarioModi(rs.getString(9));
                rg.setCorreCleinte(rs.getString(10));
                

                allReporteCli.add(rg);
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
        return allReporteCli;
    }
     
      //obtener fecha de la base de datos
     public ProveedoresMd obtenerFecha() {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ProveedoresMd();
        String query0 = " SELECT trim(DATE_FORMAT(NOW(),'%d-%m-%Y'));";
        try {
            conexion = cnn.Conexion();

            st = conexion.createStatement();
            rs = st.executeQuery(query0);
            while (rs.next()) {
                resp = 1;
                cl.setObtenerFecha(rs.getString(1));
                
                cl.setResp("1");
                cl.setMsg("fecha obtenida!");
            }
            st.close();
            rs.close();

            if (resp == 0) {

                cl.setResp("0");
                cl.setMsg("error con la base <br/>  <br/>  de datos");

            }
            conexion.close();
            cnn.desconectar();

        } catch (Exception e) {
            System.out.println("ERROR CATCH.: " + e.getMessage());
            cl.setResp("0");
            cl.setMsg(e.getMessage());

        }

        return cl;
    }
     
      public ProveedoresMd savePro(ProveedoresMd data) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ProveedoresMd();
        String query1 = " SELECT max(pr_ID)+1 as id FROM proveedores; ";
        String sql = " INSERT INTO almacen.proveedores (pr_ID, pr_NOMBRE, pr_NIT, pr_DIRECCION, pr_TELEFONO, pr_FECHA_ALTA, pr_USUARIO_ALTA, pr_CORREO)\n"
                + "VALUES (?,?,?,?,?,NOW(),?,?);";

        try {
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);

            st = conexion.createStatement();
            rs = st.executeQuery(query1);
            while (rs.next()) {
                id = rs.getString("id");
            }
            st.close();
            rs.close();

            ps = conexion.prepareStatement(sql);

            ps.setString(1, id);
            ps.setString(2, data.getNombreComercial());
            ps.setString(3, data.getNit());
            ps.setString(4, data.getDireccion());
            ps.setString(5, data.getTelefono());
            ps.setString(6, data.getUsuario());
            ps.setString(7, data.getCorreCleinte());

            ps.executeUpdate();
            ps.close();
            conexion.commit();
            cl.setResp("1");
            cl.setMsg("REGISTRO GUARDADO CORRECTAMENTE");

            conexion.close();
            cnn.desconectar();

        } catch (Exception e) {
            System.out.println("ERROR CATCH.: " + e.getMessage());
            cl.setResp("0");
            cl.setMsg(e.getMessage());
        }

        return cl;
    }
    
}
