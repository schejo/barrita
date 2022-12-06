package DAL;

import Conexion.Conexion;
import MD.ActVentasMd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.util.Clients;

public class ActVentasDal {

    private Connection conexion = null;
    private Conexion cnn = new Conexion();
    PreparedStatement ps = null;

    public List<ActVentasMd> REGselect(String codigo, String correlativo) throws SQLException, ClassNotFoundException {
        Statement st = null;
        ResultSet rs = null;
        List<ActVentasMd> allActVentas = new ArrayList<ActVentasMd>();
        String query = "SELECT "
                + "      trim( ven_prod_codigo),"
                + "	 trim(  ven_correlativo),"
                + "      trim( ven_cantidad),"
                + "      trim( ven_precio),"
                + "      trim( ven_usuario),"
                + "      trim( ven_descuento),"
                + "      trim( ven_total)"
                + " FROM             farmacia_2021.ventas "
                + " WHERE            ven_prod_codigo =  '" + codigo + "'\n "
                + " AND		     ven_correlativo = '" + correlativo + "'\n";
        
        

        System.out.println("datos....  " + query);
        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            ActVentasMd rg = new ActVentasMd();
            while (rs.next()) {
                rg.setCodigo(rs.getString(1));
                rg.setCorrelativo(rs.getString(2));
                rg.setCantidad(rs.getString(3));
                rg.setPrecio(rs.getString(4));
                rg.setUsuario(rs.getString(5));
                rg.setDescuento(rs.getString(6));
                rg.setTotal(rs.getString(7));
                allActVentas.add(rg);

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
        return allActVentas;
    }

    public void REGupdate(String codigo, String correlativo, String cantidad,
            String precio, String descuento, String total) throws SQLException, ClassNotFoundException {

        Statement st = null;
        ResultSet rs = null;
        try {
            conexion = cnn.Conexion();
            conexion.setAutoCommit(false);
            System.out.println("ACTUALIZAR DATOS..!");
            System.out.println("Actualizar " + codigo);
            st = conexion.createStatement();

            st.executeUpdate("update farmacia_2021.ventas set"
                    + " ven_cantidad = '" + cantidad + "'"
                    + ",ven_precio = '" + precio + "'"
                    + ",ven_descuento = '" + descuento + "'"
                    + ", ven_total = '" + total + "'"
                    + " where ven_prod_codigo = '" + codigo + "'"
                    + " and ven_correlativo = '" + correlativo + "'");

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

}
