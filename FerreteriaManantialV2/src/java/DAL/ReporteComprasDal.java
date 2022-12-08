package DAL;

import Conexion.Conexion;
import MD.ReporteComprasMd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.zkoss.zk.ui.util.Clients;

public class ReporteComprasDal {

    private Connection conexion = null;
    private Conexion cnn = new Conexion();
    PreparedStatement ps = null;
    Statement st = null;
    ResultSet rs = null;

    public List<ReporteComprasMd> REGselectBarrita(String anio, String fechaF) throws SQLException, ClassNotFoundException {
        List<ReporteComprasMd> alldata = new ArrayList<ReporteComprasMd>();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = "";

//        try {
//            Date date = formatter.parse(anio);
//            formatter.applyPattern("yyyy/MM/dd");
//            dateInString = formatter.format(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString2 = "";

//        try {
//            Date date2 = formatter2.parse(fechaF);
//            formatter2.applyPattern("yyyy/MM/dd");
//            dateInString2 = formatter2.format(date2);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        String query = "select a.pro_descripcion,b.mov_precio_unitario, \n" +
"                          b.mov_cantidad,\n" +
"                         (b.mov_precio_unitario * b.mov_cantidad)as TOTAL\n" +
"                  from   productos a,\n" +
"                         mov_productos b\n" +
"                  where  a.pro_id = b. mov_pro_codigo\n" +
"                  and a.pro_ferreteria=1\n" +
"                   and   date_format(b.mov_fecha_ingreso, '%d/%m/%Y') >= '" + anio + "' \n" +
"                   and   date_format(b.mov_fecha_ingreso, '%d/%m/%Y') <=  '" + fechaF + "';";
//                + " select a.pro_descripcion,b.mov_precio_unitario, \n"
//                + "             b.mov_cantidad,\n"
//                + "            (b.mov_precio_unitario * b.mov_cantidad)as TOTAL\n"
//                + "     from   almacen.productos a,\n"
//                + "            almacen.mov_productos b\n"
//                + "     where  a.pro_id = b. mov_pro_codigo\n"
//                + "      and   b.mov_fecha_ingreso >= '" + dateInString + "'\n "
//                + "      and   b.mov_fecha_ingreso <=  '" + dateInString2 + "'";

        String query2 = " select a.pro_descripcion,\n"
                + "         SUM( b.mov_precio_unitario) as PRECIO,\n"
                + "	     SUM(a.pro_stock_barrita) as CANTIDAD,\n"
                + "          SUM(b.mov_cantidad) as TOTAL\n"//b.mov_saldo
                + "     from productos a,\n"
                + "          mov_productos b ";

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            ReporteComprasMd rg;
            int x = 0;
            
            while (rs.next()) {
                rg = new ReporteComprasMd();
                x++;
                rg.setCorrelativo(String.valueOf(x));
                rg.setNombre(rs.getString(1));
                rg.setPrecio(rs.getString(2));
                rg.setCantidad(rs.getString(3));
                rg.setTotal(rs.getString(4));

                alldata.add(rg);
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
        return alldata;
    }
    
      public List<ReporteComprasMd> REGselectCarrizal(String anio, String fechaF) throws SQLException, ClassNotFoundException {
        List<ReporteComprasMd> alldata = new ArrayList<ReporteComprasMd>();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = "";



        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString2 = "";



        String query = "select a.pro_descripcion,b.mov_precio_unitario, \n" +
"                          b.mov_cantidad,\n" +
"                         (b.mov_precio_unitario * b.mov_cantidad)as TOTAL\n" +
"                  from   productos a,\n" +
"                         mov_productos b\n" +
"                  where  a.pro_id = b. mov_pro_codigo\n" +
"                  and a.pro_carrizal=1\n" +
"                   and   date_format(b.mov_fecha_ingreso, '%d/%m/%Y') >= '" + anio + "' \n" +
"                   and   date_format(b.mov_fecha_ingreso, '%d/%m/%Y') <=  '" + fechaF + "';";


        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            ReporteComprasMd rg;
            int x = 0;
            
            while (rs.next()) {
                rg = new ReporteComprasMd();
                x++;
                rg.setCorrelativo(String.valueOf(x));
                rg.setNombre(rs.getString(1));
                rg.setPrecio(rs.getString(2));
                rg.setCantidad(rs.getString(3));
                rg.setTotal(rs.getString(4));

                alldata.add(rg);
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
        return alldata;
    }
      
        public List<ReporteComprasMd> REGselectAngeles(String anio, String fechaF) throws SQLException, ClassNotFoundException {
        List<ReporteComprasMd> alldata = new ArrayList<ReporteComprasMd>();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = "";



        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString2 = "";



        String query = "select a.pro_descripcion,b.mov_precio_unitario, \n" +
"                          b.mov_cantidad,\n" +
"                         (b.mov_precio_unitario * b.mov_cantidad)as TOTAL\n" +
"                  from   productos a,\n" +
"                         mov_productos b\n" +
"                  where  a.pro_id = b. mov_pro_codigo\n" +
"                  and a.pro_angeles=1\n" +
"                   and   date_format(b.mov_fecha_ingreso, '%d/%m/%Y') >= '" + anio + "' \n" +
"                   and   date_format(b.mov_fecha_ingreso, '%d/%m/%Y') <=  '" + fechaF + "';";


        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            ReporteComprasMd rg;
            int x = 0;
            
            while (rs.next()) {
                rg = new ReporteComprasMd();
                x++;
                rg.setCorrelativo(String.valueOf(x));
                rg.setNombre(rs.getString(1));
                rg.setPrecio(rs.getString(2));
                rg.setCantidad(rs.getString(3));
                rg.setTotal(rs.getString(4));

                alldata.add(rg);
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
        return alldata;
    }

}
