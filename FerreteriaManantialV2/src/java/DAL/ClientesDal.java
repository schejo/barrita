package DAL;

import Conexion.Conexion;
import MD.ClientesMd;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.imageio.ImageIO;
import org.zkoss.zk.ui.util.Clients;

public class ClientesDal {

    Connection conexion;
    Conexion cnn = new Conexion();
    ClientesMd cl = new ClientesMd();
    Statement st = null;
    ResultSet rs = null;

    public List<ClientesMd> REGselectDeudores(String anio) throws SQLException, ClassNotFoundException {
        List<ClientesMd> allReporteCur = new ArrayList<ClientesMd>();

        String query = "select a.id,b.cl_nombre,a.cre_capital,a.cre_otorgado,a.cre_pagado,a.cre_saldo,a.cre_fecha_otorga,a.cre_fecha_paga,a.cre_factura,a.cre_obser,a.cre_usu_alta\n"
                + "from cliente b,\n"
                + "     creditos a\n"
                + "     where a.cl_id=b.cl_id;";
//                + "SELECT * FROM almacen.creditos;"
//                + "select a.cur_id,a.cur_nombre,\n"
//                + "a.cur_duracion, a.cur_sala, a.cur_empresa,\n"
//                + "a.cur_observaciones\n"
//                + "from control_cursos.cursos a";

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            ClientesMd rg;
            //int x = 0;
            //System.out.println("ACTIVIDAD...: "+actividad);
            while (rs.next()) {
                rg = new ClientesMd();

                rg.setCodigoCliente(rs.getString(1));
                rg.setNombreMos(rs.getString(2));
                rg.setLimite(rs.getString(3));
                rg.setCredito(rs.getString(4));
                rg.setMontopago(rs.getString(5));
                rg.setDisponible(rs.getString(6));
                rg.setFechaAlta(rs.getString(7));
                rg.setFechaModi(rs.getString(8));
                rg.setFactura(rs.getString(9));
                rg.setObs(rs.getString(10));
                rg.setUsuario(rs.getString(11));

                allReporteCur.add(rg);
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
        return allReporteCur;
    }

    public List<ClientesMd> REGselect() throws SQLException, ClassNotFoundException {
        List<ClientesMd> allReporteCli = new ArrayList<ClientesMd>();

        String query = "SELECT * FROM cliente;";

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            ClientesMd rg;

            while (rs.next()) {
                rg = new ClientesMd();

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

    public ClientesMd updateOtorgarCredito(ClientesMd data) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ClientesMd();
        try {
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);
            int vl = 0;
            st = conexion.createStatement();
            vl = st.executeUpdate("UPDATE cliente SET CL_DISPONIBLE = " + data.getDisponible() + " WHERE CL_ID =" + data.getCodigoClienteMos() + "");
//                    + "UPDATE almacen.cliente SET CL_DISPONIBLE = " + data.getDisponible()+ " WHERE CL_ID = " + data.getCodigoClienteMos()+ ";");
//                    + "UPDATE cliente SET CL_NOMBRE='" + data.getDisponible()+ "',CL_NIT='"
//                    + data.getNit() + "',"
//                    + "CL_DIRECCION='" + data.getDireccion() + "',CL_TELEFONO='" + data.getTelefono() + "',"
//                    + "CL_FECHA_MODIFICA=  now()  ,CL_USUARIO_MODIFICA='" + data.getUsuario()
//                    + "',CL_CORREO='" + data.getCorreCleinte()
//                    + "',CL_CREDITO=" + data.getCredito()
//                    + ",CL_DISPONIBLE=" + data.getDisponible()
//                    + "where CL_NOMBRE='" + data.getNombreMos() + "' ");
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

    public ClientesMd updateCur(ClientesMd data) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ClientesMd();
        try {
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);
            int vl = 0;
            st = conexion.createStatement();
            vl = st.executeUpdate("UPDATE cliente SET CL_NOMBRE='" + data.getNombreComercial() + "',CL_NIT='"
                    + data.getNit() + "',"
                    + "CL_DIRECCION='" + data.getDireccion() + "',CL_TELEFONO='" + data.getTelefono() + "',"
                    + "CL_FECHA_MODIFICA=  now()  ,CL_USUARIO_MODIFICA='" + data.getUsuario()
                    + "',CL_CORREO='" + data.getCorreCleinte()
                    + "',CL_CREDITO=" + data.getCredito()
                    + ",CL_DISPONIBLE=" + data.getDisponible()
                    + "where CL_NOMBRE='" + data.getNombreMos() + "' ");
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

    //obtener fecha de la base de datos
    public ClientesMd obtenerFecha() {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ClientesMd();
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

    public ClientesMd mostrarDispo(String cliente) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ClientesMd();
        String query0 = "select a.cl_credito,a.cl_disponible \n"
                + "from cliente a \n"
                + "where a.cl_id='" + cliente + "';";

        try {
            conexion = cnn.Conexion();

            st = conexion.createStatement();
            rs = st.executeQuery(query0);
            while (rs.next()) {
                resp = 1;
                cl.setLimite(rs.getString(1));
                cl.setDisponible(rs.getString(2));

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

    public ClientesMd findClientes(String cliente) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ClientesMd();
        String query0 = " select cl_nombre,cl_nit,cl_direccion,cl_telefono,cl_correo,cl_credito,cl_disponible from cliente "
                + "where cl_nombre='" + cliente + "';";
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
                cl.setCredito(rs.getString(6));
                cl.setDisponible(rs.getString(7));
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

    public List<ClientesMd> allCL() {
        List<ClientesMd> data = new ArrayList<ClientesMd>();
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;

        String query0 = " select cl_id, cl_nombre from cliente; ";
        try {
            conexion = cnn.Conexion();

            st = conexion.createStatement();
            rs = st.executeQuery(query0);
            while (rs.next()) {
                cl = new ClientesMd();
                resp = 1;
                cl.setCodigoClienteMos(rs.getString(1));
                cl.setNombreMos(rs.getString(2));
                //cl.setDir(rs.getString(3));
                // cl.setResp("1");
                cl.setMsg("TODOS LOS clientes.!");
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

    public ClientesMd BuscarClientes(String nit) throws SQLException {
        PreparedStatement smt = null;

        conexion = cnn.Conexion();
        //conn = cnn.getConnection();
        ResultSet result = null;

        ClientesMd Buscar = null;

        String sql = "Select CL_ID, "
                + "          CL_NOMBRE, "
                + "          CL_NIT, "
                + "          CL_DIRECCION, "
                + "          CL_TELEFONO, "
                + "          DATE_FORMAT(CL_FECHA_ALTA, '%d/%m/%Y') "
                + "   from cliente U"
                + "   where TRIM(CL_NIT) = ?";

        try {
            smt = conexion.prepareStatement(sql);
            smt.setString(1, nit.replace("-", ""));

            result = smt.executeQuery();

            while (result.next()) {
                Buscar = new ClientesMd();
                Buscar.setCodigoCliente(result.getString(1));
                Buscar.setNombreComercial(result.getString(2));
                Buscar.setNit(result.getString(3));
                Buscar.setDireccion(result.getString(4));
                Buscar.setTelefono(result.getString(5));
                Buscar.setFechaAlta(result.getString(6));

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
                cnn.cerrar();
                conexion.close();
                conexion = null;
            }
        }
        return Buscar;
    }

    public ClientesMd savePago(ClientesMd data) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ClientesMd();
        //  String query1 = "";
        String sql = "INSERT INTO creditos (`cl_id`, `cre_capital`, `cre_pagado`, `cre_saldo`, `cre_fecha_paga`, `cre_factura`, `cre_obser`, `cre_usu_alta`) "
                + "VALUES (?, ?, ?, ?,NOW(), ?, ?, ?)";
//                + " INSERT INTO almacen.cliente (CL_ID, CL_NOMBRE, CL_NIT, CL_DIRECCION, CL_TELEFONO, CL_FECHA_ALTA, CL_USUARIO_ALTA, CL_CORREO,CL_CREDITO,CL_DISPONIBLE)\n"
//                + "VALUES (?,?,?,?,?,NOW(),?,?,?,?);";

        try {
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);

            st = conexion.createStatement();
            // rs = st.executeQuery(query1);
//            while (rs.next()) {
//                id = rs.getString("id");
//            }
            st.close();
//            rs.close();

            ps = conexion.prepareStatement(sql);

            ps.setString(1, data.getCodigoClienteMos());
            ps.setString(2, data.getCredito());
            ps.setString(3, data.getMontopago());
            ps.setString(4, data.getDisponible());
            ps.setString(5, data.getFactura());
            ps.setString(6, data.getObs());
            ps.setString(7, data.getUsuario());

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

    public ClientesMd saveCredito(ClientesMd data) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ClientesMd();
        //  String query1 = "";
        String sql = "INSERT INTO `creditos` (`cl_id`, `cre_capital`, `cre_otorgado`, `cre_saldo`, `cre_fecha_otorga`, `cre_factura`, `cre_obser`, `cre_usu_alta`) "
                + "VALUES (?, ?, ?, ?,NOW(), ?, ?, ?)";
//                + " INSERT INTO almacen.cliente (CL_ID, CL_NOMBRE, CL_NIT, CL_DIRECCION, CL_TELEFONO, CL_FECHA_ALTA, CL_USUARIO_ALTA, CL_CORREO,CL_CREDITO,CL_DISPONIBLE)\n"
//                + "VALUES (?,?,?,?,?,NOW(),?,?,?,?);";

        try {
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);

            st = conexion.createStatement();
            // rs = st.executeQuery(query1);
//            while (rs.next()) {
//                id = rs.getString("id");
//            }
            st.close();
//            rs.close();

            ps = conexion.prepareStatement(sql);

            ps.setString(1, data.getCodigoClienteMos());
            ps.setString(2, data.getCredito());
            ps.setString(3, data.getMontocredito());
            ps.setString(4, data.getDisponible());
            ps.setString(5, data.getFactura());
            ps.setString(6, data.getObs());
            ps.setString(7, data.getUsuario());

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
    
      public ClientesMd saveCLModal(ClientesMd data) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ClientesMd();
        String query1 = " SELECT max(CL_ID)+1 as id FROM cliente; ";
        String sql = " INSERT INTO cliente (CL_ID, CL_NOMBRE, CL_NIT, CL_DIRECCION, CL_TELEFONO, CL_FECHA_ALTA, CL_USUARIO_ALTA, CL_CORREO)\n"
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

    public ClientesMd saveCL(ClientesMd data) {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String id = "";
        int resp = 0;
        cl = new ClientesMd();
        String query1 = " SELECT max(CL_ID)+1 as id FROM cliente; ";
        String sql = " INSERT INTO cliente (CL_ID, CL_NOMBRE, CL_NIT, CL_DIRECCION, CL_TELEFONO, CL_FECHA_ALTA, CL_USUARIO_ALTA, CL_CORREO,CL_CREDITO,CL_DISPONIBLE)\n"
                + "VALUES (?,?,?,?,?,NOW(),?,?,?,?);";

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
            ps.setString(8, data.getCredito());
            ps.setString(9, data.getCredito());

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
