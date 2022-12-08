package DAL;

import Conexion.Conexion;
import MD.DetalleFacturaMd;
import MD.FacturaMd;
import MD.VentasMd;
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

public class VentasValesDal {

    private Connection conexion = null;
    private Conexion cnn = new Conexion();
    PreparedStatement ps = null;

        Session session = Sessions.getCurrent();

    
    public int Crear(FacturaMd modelo, List<DetalleFacturaMd> detalles) throws SQLException {
        {
            PreparedStatement smt = null, smt2 = null;
            Connection conn;
            ResultSet resulta = null;
            Conexion conex = new Conexion();
            conn = conex.Conexion();
            int result = 0;

            try {
                int total = detalles.size() + 2;   //3 sql + un update = 4 cambiando 2 a 3 

                String sql = "insert into prefactura values ((SELECT IFNULL(MAX(prefac_id), 0)+1 FROM prefactura u),"
                        + "?,?,?,?,?,?,now(),?,null,null,?)";
                conn.setAutoCommit(false);
                smt = conn.prepareStatement(sql);
                smt.setString(1, modelo.getFacturaClienteId());
                smt.setString(2, modelo.getFacturaEmpleadoId());
                smt.setString(3, modelo.getFacturaEstado());
                smt.setString(4, modelo.getFacturaSubtotal());
                smt.setString(5, modelo.getFacturaTotal());
                smt.setString(6, modelo.getFacturaDescuento());
                smt.setString(7, modelo.getFacturaUsuarioEmite().toUpperCase());
                smt.setString(8, session.getAttribute("SUCURSAL").toString());

                result += smt.executeUpdate();
                smt.close();

                for (int i = 0; i < detalles.size(); i++) {
                    sql = "insert into detalle_prefactura values ((SELECT MAX(prefac_id) FROM prefactura u),"
                            + "?,'" + detalles.get(i).getDetProductoPrecioVenta() + "','" + detalles.get(i).getProductoDescuento() + "',?)";
                    // conn.setAutoCommit(false);
                    smt = conn.prepareStatement(sql);
                    smt.setString(1, detalles.get(i).getDetProductoId());
                    smt.setString(2, detalles.get(i).getProductoCantidad());

                    result += smt.executeUpdate();
                    smt.close();

                                String stock = "",productos="";

            switch (session.getAttribute("SUCURSAL").toString()) {
                case "1":
                    stock = "pro_stock_barrita";
                    productos ="productos";
                    break;
                case "2":
                    stock = "pro_stock_carrizal";
                    productos ="productos2";
                    break;
                case "3":
                    stock = "pro_stock_angeles";
                    productos ="productos";
                    break;

            }

                    
                    
                    // esta es una prueba 22/05/2022 
                    sql = "update "+productos+" set " + stock + " = " + stock + "-" + detalles.get(i).getProductoCantidad() + " "
                            + "where pro_id = '" + detalles.get(i).getDetProductoId() + "'";
                    smt = conn.prepareStatement(sql);
                    smt.executeUpdate();
                    smt.close();
                    // fin de la prueba

                }

                sql = "insert into factura values (?,(SELECT IFNULL(MAX(fac_numero), 0)+1 FROM factura u where fac_serie ='" + modelo.getFacturaSerie() + "'),"
                        + "(SELECT MAX(prefac_id) FROM prefactura u),?,?," + modelo.getFacturaSubtotal() + "," + modelo.getFacturaTotal() + "," + modelo.getFacturaDescuento() + ","
                        + "now(),?,null," + (!modelo.getFacturaPagoEfectivo().equals("") ? modelo.getFacturaPagoEfectivo() : "0") + "," + (!modelo.getFacturaPagoTarjeta().equals("") ? modelo.getFacturaPagoTarjeta() : "0") + ","
                        + "'" + (!modelo.getFacturaReferenciaTarjeta().equals("") ? modelo.getFacturaReferenciaTarjeta() : "N/A") + "'," + (!modelo.getFacturaCredito().equals("") ? modelo.getFacturaCredito() : "0") + ","
                        + "?,?,?,?,?,?,"
                        + (!modelo.getFacturaCambio().equals("") ? modelo.getFacturaCambio() : "0") + "," + (!modelo.getFacturaEfectivoRecibido().equals("") ? modelo.getFacturaEfectivoRecibido() : "0") + ",?)";
                //conn.setAutoCommit(false);
                smt = conn.prepareStatement(sql);
                smt.setString(1, modelo.getFacturaSerie());
                smt.setString(2, modelo.getFacturaEstado());
                smt.setString(3, modelo.getFacturaTipoPago());
                smt.setString(4, modelo.getFacturaUsuarioEmite().toUpperCase());
                smt.setString(5, modelo.getFacturaSerieDTE());
                smt.setString(6, modelo.getFacturaNumeroD());
                smt.setString(7, modelo.getFacturaAutorizacion());
                smt.setString(8, modelo.getFacturaFechaCertificacion());
                smt.setString(9, modelo.getFacturaNitCertificador());
                smt.setString(10, modelo.getFacturaNombreCertificador());
                smt.setString(11, session.getAttribute("SUCURSAL").toString());

                result += smt.executeUpdate();
                smt.close();

//                total = 1;
//                result = 1;
                if (result == total) {
                    conn.commit();
                } else {
                    result = 0;
                    conn.rollback();
                    System.out.println("error resultado " + result);
                    System.out.println("error total " + total);

                }

            } catch (Exception e) {
                //      return 0;
                conn.rollback();
                System.out.println("error Update productos " + e);
            } finally {
                if (smt != null) {
                    smt.close();
                }
                if (conn != null) {
                    conex.cerrar();
                    conn.close();
                    conn = null;
                }
            }
            return result;
        }

    }

    public List<VentasMd> REGselect(String codigo) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        List<VentasMd> allVentas = new ArrayList<VentasMd>();
        String query = "select trim(ven_prod_codigo),"
                + " trim(ven_correlativo),"
                + " trim(ven_cantidad,)"
                + " trim(ven_precio,)"
                + " trim(ven_usuario,)"
                + " trim(ven_fecha,)"
                + " trim(ven_descuento,)"
                + " trim(ven_total)"
                //   + " trim(ven_nit),"
                //   + " trim(ven_nombre)"
                + " from ventas where ven_prod_codigo = '" + codigo + "' ";
        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            VentasMd rg = new VentasMd();
            while (rs.next()) {
                rg.setCodigo(rs.getString(1));
                rg.setCorrelativo(rs.getString(2));
                rg.setCantidad(rs.getString(3));
                rg.setPrecio(rs.getString(4));
                rg.setUsuario(rs.getString(5));
                rg.setFecha(rs.getString(6));
                rg.setDescuento(rs.getString(7));
                rg.setTotal(rs.getString(8));
                //  rg.setNit(rs.getString(9));
                //  rg.setNombre(rs.getString(10));

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

    public List<VentasMd> RSelect() throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        List<VentasMd> allVentas = new ArrayList<VentasMd>();
        String query = "select trim(ven_prod_codigo),"
                + " trim(ven_correlativo),"
                + " trim(ven_cantidad),"
                + " trim(ven_precio),"
                + " trim(ven_usuario),"
                + " trim(DATE_FORMAT(ven_fecha,'%d/%m/%Y')),"
                + " trim(ven_descuento),"
                + " trim(ven_total)"
                //  + " trim(ven_nit),"
                //   + " trim(ven_nombre)"
                + " from ventas order by  ven_prod_codigo asc";

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            VentasMd rg;
            while (rs.next()) {
                rg = new VentasMd();
                rg.setCodigo(rs.getString(1));
                rg.setCorrelativo(rs.getString(2));
                rg.setCantidad(rs.getString(3));
                rg.setPrecio(rs.getString(4));
                rg.setUsuario(rs.getString(5));
                rg.setFecha(rs.getString(6));
                rg.setDescuento(rs.getString(7));
                rg.setTotal(rs.getString(8));
                //  rg.setNit(rs.getString(9));
                //  rg.setNombre(rs.getString(10));

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
            String usuario, String fecha, String descuento, String total // String nit,
    /* String nombre*/) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        String sql = "insert into ventas"
                + "(ven_prod_codigo,"
                + " ven_correlativo,"
                + " ven_cantidad,"
                + " ven_precio,"
                + " ven_usuario,"
                + " ven_fecha,"
                + " ven_descuento,"
                + " ven_total)"
                /*  + " ven_nit,"
                + " ven_nombre)"*/
                + " VALUES(?,?,?,?,?,STR_TO_DATE(?,'%d/%m/%Y'),?,?)";

        String sql0 = "select max(ven_prod_codigo)+1 as codigo from ventas";

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
            ps.setString(7, descuento);
            ps.setString(8, total);
            /*ps.setString(9, nit);
            ps.setString(10, nombre);*/
            ps.executeUpdate();
            ps.close();

            conexion.commit();
            Clients.showNotification("REGISTRO CREADO <br/> CON EXITO  <br/>");
            conexion.close();
            conexion = null;

        } catch (SQLException e) {
            conexion.rollback();
            conexion.close();
            conexion = null;
            Clients.showNotification("ERROR AL INSERTAR <br/> <br/> REGISTROS! <br/> " + e.getMessage().toString(),
                    "warning", null, "middle_center", 0);
        }
    }

    public void REGupdate(String codigo, String correlativo, String cantidad, String precio,
            String usuario, String fecha, String descuento, String total /* String nit,
            String nombre*/) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        try {
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);
            st = conexion.createStatement();

            st.executeUpdate("update ventas set ven_correlativo= '" + correlativo + "'"
                    + ",ven_cantidad = '" + cantidad + "'"
                    + ",ven_precio = '" + precio + "'"
                    + ",ven_usuario = '" + usuario + "'"
                    + ",ven_fecha = STR_TO_DATE('" + fecha + "','%d/%m/%Y')"
                    + ",ven_descuento = '" + descuento + "'"
                    + ",ven_total = '" + total + "'"
                    /*   + ",ven_nit = '" + nit + "'"
                    + ",ven_nombre = '" + nombre + "'"*/
                    + " where ven_prod_codigo = '" + codigo + "' ");

            Clients.showNotification("REGISTRO ACTUALIZADO <br/> CON EXITO  <br/>");

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

            st.executeUpdate("delete ventas where ven_prod_codigo = '" + codigo + "' ");
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
        }

    }

    public List<VentasMd> Correlativo(String codigo) throws ClassNotFoundException, SQLException {
        Statement st = null;
        ResultSet rs = null;
        String query = "select count(ven_correlativo)+1 as correlativo, ven_precio from ventas where ven_prod_codigo='" + codigo + "' ";
        List<VentasMd> allVentas = new ArrayList<VentasMd>();
        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            VentasMd rg;
            while (rs.next()) {
                rg = new VentasMd();
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

            st.executeUpdate("update productos "
                    + "set pro_stock_barrita = pro_stock_barrita -" + valor + " "
                    + " where pro_id = '" + codigo + "'  ");

            Clients.showNotification("REGISTRO ACTUALIZADO <br/> CON EXITO  <br/>");
            System.out.println("Actualizacion Exitosa EL PRODUCTO.! ");

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
        String query = "select prod_stock from productos where prod_id='" + codigo + "' ";
        String resp = "";
        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                resp = rs.getString("prod_saldo");
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

    public FacturaMd BuscarEncabezadoCotiza(String cliente,String usuario) throws SQLException {
        PreparedStatement smt = null;
        Connection conn;
        Conexion conex = new Conexion();
        conn = conex.Conexion();
        String sql = null;
        ResultSet result = null;
        FacturaMd Busqueda = null;

        try {
            sql = " SELECT a.usu_codigo,a.usu_nombre, b.Cl_NIT,b.CL_NOMBRE,b.CL_DIRECCION \n"
                    + " from usuarios a,\n"
                    + " cliente b\n"
                    + " WHERE b.CL_ID=? AND a.USU_CODIGO=?;";

            smt = conn.prepareStatement(sql);
            smt.setString(1, cliente);
            smt.setString(2,usuario);

            result = smt.executeQuery();

            while (result.next()) {
                Busqueda = new FacturaMd();
                Busqueda.setFacturaEmpleadoId(result.getString(1));
                Busqueda.setFacturaEmpleadoNombre(result.getString(2));
                
                Busqueda.setFacturaClienteNit(result.getString(3));
                Busqueda.setFacturaClienteNombre(result.getString(4));
                Busqueda.setFacturaClienteDireccion(result.getString(5));
                
              

            }

        } catch (Exception e) {
        } finally {
            if (smt != null) {
                smt.close();
                smt = null;
            }
            if (result != null) {
                result.close();
                result = null;
            }
            if (conn != null) {
                conn.close();
                conex.cerrar();
            }
        }
        return Busqueda;
    }

    public FacturaMd BuscarEncabezadoFactura(String serie) throws SQLException {
        PreparedStatement smt = null;
        Connection conn;
        Conexion conex = new Conexion();
        conn = conex.Conexion();
        String sql = null;
        ResultSet result = null;
        FacturaMd Busqueda = null;

        try {
            sql = "SELECT F.FAC_SERIE,\n"
                    + "                           F.FAC_NUMERO,\n"
                    + "                           IFNULL(F.FAC_SERIE_DTE,'-'),\n"
                    + "                           IFNULL(F.FAC_NUMERO_DTE,'-'),      \n"
                    + "                           IFNULL(F.FAC_AUTORIZACION,'-'),\n"
                    + "                           IFNULL(F.FAC_NIT_CERTIFICADOR,'-'),\n"
                    + "                           IFNULL(F.FAC_NOMBRE_CERTIFICADOR,'-'),\n"
                    + "                           IFNULL(DATE_FORMAT(F.FAC_FECHA_CERTIFICACION,'%d/%m/%Y'),'-'),\n"
                    + "                           DATE_FORMAT(F.FAC_FECHA_ALTA,'%d/%m/%Y'),\n"
                    + "                           F.FAC_ESTADO,\n"
                    + "                           (CASE F.FAC_TIPO_PAGO WHEN 'E' THEN 'EFECTIVO' WHEN 'T' THEN 'TARJETA' WHEN 'C' THEN 'CREDITO' WHEN 'M' THEN 'MULTIPLE' 	ELSE ' ' END),\n"
                    + "						   IFNULL(FORMAT(F.FAC_REFERENCIA,2),'-'),\n"
                    + "                           IFNULL(FORMAT(F.FAC_SUBTOTAL,2),0),\n"
                    + "                           IFNULL(FORMAT(F.FAC_DESCUENTO,2),0),\n"
                    + "                           IFNULL(FORMAT(F.FAC_TOTAL,2),0),\n"
                    + "                           IFNULL(FORMAT(F.FAC_PAGO_EFECTIVO,2),0),\n"
                    + "                           IFNULL(FORMAT(F.FAC_PAGO_TARJETA,2),0),\n"
                    + "                           IFNULL(FORMAT(F.FAC_PAGO_CREDITO,2),0),\n"
                    + "                           IFNULL(FORMAT(F.FAC_RECIBIDO,2),0),\n"
                    + "                           IFNULL(FORMAT(F.FAC_CAMBIO,2),0),\n"
                    + "                           p.prefac_usu_ID,\n"
                    + "                           upper(u.usu_nombre),\n"
                    + "                           F.FAC_USUARIO_ALTA,\n"
                    + "                           \n"
                    + "                           p.PREFAC_CL_ID,\n"
                    + "						   upper(c.CL_NOMBRE),\n"
                    + "                           c.CL_NIT,\n"
                    + "                           c.CL_DIRECCION\n"
                    + "                    FROM factura F, prefactura p, cliente c ,usuarios u\n"
                    + "                    WHERE F.fac_pre_numero = p.prefac_id  \n"
                    + "                    AND p.prefac_cl_id = c.cl_id\n"
                    + "                    AND p.prefac_usu_id = u.usu_codigo \n"
                    + "                    and F.FAC_NUMERO = (SELECT MAX(FAC_NUMERO) FROM factura WHERE FAC_SERIE =?);";

            smt = conn.prepareStatement(sql);
            smt.setString(1, serie);
            //smt.setString(2, Correlativo);
            //smt.setString(3, Fecha);
            result = smt.executeQuery();

            while (result.next()) {
                Busqueda = new FacturaMd();
                Busqueda.setFacturaSerie(result.getString(1));
                Busqueda.setFacturaNumero(result.getString(2));
                Busqueda.setFacturaSerieDTE(result.getString(3));
                Busqueda.setFacturaNumeroD(result.getString(4));
                Busqueda.setFacturaAutorizacion(result.getString(5));
                Busqueda.setFacturaNitCertificador(result.getString(6));
                Busqueda.setFacturaNombreCertificador(result.getString(7));
                Busqueda.setFacturaFechaCertificacion(result.getString(8));
                Busqueda.setFacturaFechaEmision(result.getString(9));
                Busqueda.setFacturaEstado(result.getString(10));
                Busqueda.setFacturaTipoPago(result.getString(11));
                Busqueda.setFacturaReferenciaTarjeta(result.getString(12));
                Busqueda.setFacturaSubtotal(result.getString(13));
                Busqueda.setFacturaDescuento(result.getString(14));
                Busqueda.setFacturaTotal(result.getString(15));
                Busqueda.setFacturaPagoEfectivo(result.getString(16));
                Busqueda.setFacturaPagoTarjeta(result.getString(17));
                Busqueda.setFacturaCredito(result.getString(18));
                Busqueda.setFacturaEfectivoRecibido(result.getString(19));
                Busqueda.setFacturaCambio(result.getString(20));

                Busqueda.setFacturaEmpleadoId(result.getString(21));
                Busqueda.setFacturaEmpleadoNombre(result.getString(22));
                Busqueda.setFacturaUsuarioEmite(result.getString(23));

                Busqueda.setFacturaClienteId(result.getString(24));
                Busqueda.setFacturaClienteNombre(result.getString(25));
                Busqueda.setFacturaClienteNit(result.getString(26));
                Busqueda.setFacturaClienteDireccion(result.getString(27));

            }

        } catch (Exception e) {
        } finally {
            if (smt != null) {
                smt.close();
                smt = null;
            }
            if (result != null) {
                result.close();
                result = null;
            }
            if (conn != null) {
                conn.close();
                conex.cerrar();
            }
        }
        return Busqueda;
    }

    public List<DetalleFacturaMd> BuscaDetallesFactura() throws SQLException {
        PreparedStatement smt = null;
        Connection conn;
        Conexion conex = new Conexion();
        conn = conex.Conexion();
        ResultSet result = null;

        List<DetalleFacturaMd> lista = new ArrayList<DetalleFacturaMd>();

        DetalleFacturaMd Buscar = null;

        String sql = "SELECT D.DET_PRO_ID,\n"
                + "                       IFNULL(P.PRO_DESCRIPCION,' '),\n"
                + "                       IFNULL(P.PRO_TIPO,' '),\n"
                + "                       IFNULL(P.PRO_MARCA,' '),\n"
                + "                       IFNULL(P.PRO_PRESENTACION,' '),\n"
                + "                       (CASE P.PRO_CONVERSION WHEN null THEN ' ' WHEN 0 THEN ' ' ELSE 'X '||P.PRO_CONVERSION END),\n"
                + "                       IFNULL(FORMAT(D.DET_PRECIO_VENTA,2),0),\n"
                + "                       IFNULL(FORMAT(D.DET_DESCUENTO,2),0),\n"
                + "                       IFNULL(D.DET_CANTIDAD,0)\n"
                + "                FROM detalle_prefactura D,productos P\n"
                + "                WHERE D.DET_PRO_ID = P.PRO_ID\n"
                + "                AND D.DET_PREFAC_ID =(select IFNULL(MAX(PREFAC_ID),0) FROM prefactura J)";

        try {
            smt = conn.prepareStatement(sql);
            result = smt.executeQuery();

            while (result.next()) {
                Buscar = new DetalleFacturaMd();

                Buscar.setDetProductoId(result.getString(1));
                Buscar.setDetProductoDescripcion(result.getString(2));
                Buscar.setDetProductoTipo(result.getString(3));
                Buscar.setDetProductoMarca(result.getString(4));
                Buscar.setDetProductoPresentacion(result.getString(5));
                Buscar.setDetProductoConversion(result.getString(6));
                Buscar.setDetProductoPrecioVenta(result.getString(7));
                Buscar.setProductoDescuento(result.getString(8));
                Buscar.setProductoCantidad(result.getString(9));

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
            if (conn != null) {
                conex.cerrar();
                conn.close();
                conn = null;
            }
        }
        return lista;
    }
    
       public List<DetalleFacturaMd> BusDetaFac2Tabla() throws SQLException {
        PreparedStatement smt = null;
        Connection conn;
        Conexion conex = new Conexion();
        conn = conex.Conexion();
        ResultSet result = null;

        List<DetalleFacturaMd> lista = new ArrayList<DetalleFacturaMd>();

        DetalleFacturaMd Buscar = null;

        String sql = "SELECT D.DET_PRO_ID,\n"
                + "                       IFNULL(P.PRO_DESCRIPCION,' '),\n"
                + "                       IFNULL(P.PRO_TIPO,' '),\n"
                + "                       IFNULL(P.PRO_MARCA,' '),\n"
                + "                       IFNULL(P.PRO_PRESENTACION,' '),\n"
                + "                       (CASE P.PRO_CONVERSION WHEN null THEN ' ' WHEN 0 THEN ' ' ELSE 'X '||P.PRO_CONVERSION END),\n"
                + "                       IFNULL(FORMAT(D.DET_PRECIO_VENTA,2),0),\n"
                + "                       IFNULL(FORMAT(D.DET_DESCUENTO,2),0),\n"
                + "                       IFNULL(D.DET_CANTIDAD,0)\n"
                + "                FROM detalle_prefactura D,productos2 P\n"
                + "                WHERE D.DET_PRO_ID = P.PRO_ID\n"
                + "                AND D.DET_PREFAC_ID =(select IFNULL(MAX(PREFAC_ID),0) FROM prefactura J)";

        try {
            smt = conn.prepareStatement(sql);
            result = smt.executeQuery();

            while (result.next()) {
                Buscar = new DetalleFacturaMd();

                Buscar.setDetProductoId(result.getString(1));
                Buscar.setDetProductoDescripcion(result.getString(2));
                Buscar.setDetProductoTipo(result.getString(3));
                Buscar.setDetProductoMarca(result.getString(4));
                Buscar.setDetProductoPresentacion(result.getString(5));
                Buscar.setDetProductoConversion(result.getString(6));
                Buscar.setDetProductoPrecioVenta(result.getString(7));
                Buscar.setProductoDescuento(result.getString(8));
                Buscar.setProductoCantidad(result.getString(9));

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
            if (conn != null) {
                conex.cerrar();
                conn.close();
                conn = null;
            }
        }
        return lista;
    }

}
