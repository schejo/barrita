package DAL;

import Conexion.Conexion;
import MD.KardexMd;
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

public class KardexDal {

    private Connection conexion = null;
    private Conexion cnn = new Conexion();
    PreparedStatement ps = null;
    Statement st = null;
    ResultSet rs = null;
    
    
    public List<KardexMd> REGselectAngeles(String anio) throws SQLException, ClassNotFoundException {
        List<KardexMd> alldata = new ArrayList<KardexMd>();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = "";

        try {
            Date date = formatter.parse(anio);
            formatter.applyPattern("yyyy/MM/dd");
            dateInString = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString2 = "";

        String query = "select a.pro_id,a.pro_descripcion,pro_marca,\n"
                + "a.pro_minimo, a.pro_precio_compra,a.pro_precio_venta, a.pro_maximo,\n" 
                +" CASE a.pro_ubicacion \n"
                +" when 'S' then 'SALA DE VENTAS' \n"
                +" when 'P' then 'PATIO' \n"
                +" when 'BI' then 'BODEGA 1 INTERIOR' \n"
                +" when 'B2' then 'BODEGA 2' \n"
                +" when 'B3' then 'BODEGA 3-PATIOS' \n"
                +" when 'B4' then 'BODEGA 4-CARRIZAL' \n"
                +" END AS UBICACION, \n"
                + "a.pro_stock_angeles\n"
                + "from productos a where pro_angeles=1";

        String query2 = "select a.pro_id,a.pro_descripcion,\n"
                + "                a.pro_minimo, SUM( a.pro_precio_venta) as TOTAL1, a.pro_maximo,\n"
                + "                a.pro_ubicacion, SUM(a.pro_stock) as TOTAL2\n"
                + "                from productos a ";

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            KardexMd rg;
            int x = 0;
            
            while (rs.next()) {
                rg = new KardexMd();
                x++;
                rg.setCorrelativo(String.valueOf(x));
                rg.setCodigo(rs.getString(1));
                rg.setNombre(rs.getString(2));
                rg.setMarca(rs.getString(3));
               
                rg.setCantidad_ing(rs.getString(4));
                rg.setPre_comp(rs.getString(5));
                rg.setPrecio_ing(rs.getString(6));
                rg.setCantidad_sal(rs.getString(7));
                rg.setPrecio_sal(rs.getString(8));
                rg.setStopbarrita(rs.getString(9));

            
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
    
     public List<KardexMd> REGselectCarrizal(String anio) throws SQLException, ClassNotFoundException {
        List<KardexMd> alldata = new ArrayList<KardexMd>();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = "";

        try {
            Date date = formatter.parse(anio);
            formatter.applyPattern("yyyy/MM/dd");
            dateInString = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString2 = "";

        String query = "select a.pro_id,a.pro_descripcion,pro_marca,\n"
                + "a.pro_minimo, a.pro_precio_compra,a.pro_precio_venta, a.pro_maximo,\n" 
                +" CASE a.pro_ubicacion \n"
                +" when 'S' then 'SALA DE VENTAS' \n"
                +" when 'P' then 'PATIO' \n"
                +" when 'BI' then 'BODEGA 1 INTERIOR' \n"
                +" when 'B2' then 'BODEGA 2' \n"
                +" when 'B3' then 'BODEGA 3-PATIOS' \n"
                +" when 'B4' then 'BODEGA 4-CARRIZAL' \n"
                +" END AS UBICACION, \n"
                + "a.pro_stock_carrizal\n"
                + "from productos2 a where pro_carrizal=1";

//        String query2 = "select a.pro_id,a.pro_descripcion,\n"
//                + "                a.pro_minimo, SUM( a.pro_precio_venta) as TOTAL1, a.pro_maximo,\n"
//                + "                a.pro_ubicacion, SUM(a.pro_stock) as TOTAL2\n"
//                + "                from productos a ";

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            KardexMd rg;
            int x = 0;
            
            while (rs.next()) {
                rg = new KardexMd();
                x++;
                rg.setCorrelativo(String.valueOf(x));
                rg.setCodigo(rs.getString(1));
                rg.setNombre(rs.getString(2));
                rg.setMarca(rs.getString(3));
               
                rg.setCantidad_ing(rs.getString(4));
                rg.setPre_comp(rs.getString(5));
                rg.setPrecio_ing(rs.getString(6));
                rg.setCantidad_sal(rs.getString(7));
                rg.setPrecio_sal(rs.getString(8));
                rg.setStopbarrita(rs.getString(9));

            
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
     
     public List<KardexMd> REGselectBarrita(String anio) throws SQLException, ClassNotFoundException {
        List<KardexMd> alldata = new ArrayList<KardexMd>();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = "";

        try {
            Date date = formatter.parse(anio);
            formatter.applyPattern("yyyy/MM/dd");
            dateInString = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString2 = "";

        String query = "select a.pro_id,a.pro_descripcion,pro_marca,\n"
                + "a.pro_minimo, a.pro_precio_compra,a.pro_precio_venta, a.pro_maximo,\n" 
                +" CASE a.pro_ubicacion \n"
                +" when 'S' then 'SALA DE VENTAS' \n"
                +" when 'P' then 'PATIO' \n"
                +" when 'BI' then 'BODEGA 1 INTERIOR' \n"
                +" when 'B2' then 'BODEGA 2' \n"
                +" when 'B3' then 'BODEGA 3-PATIOS' \n"
                +" when 'B4' then 'BODEGA 4-CARRIZAL' \n"
                +" END AS UBICACION, \n"
                + "a.pro_stock_barrita\n"
                + "from productos a where pro_ferreteria=1";

        String query2 = "select a.pro_id,a.pro_descripcion,\n"
                + "                a.pro_minimo, SUM( a.pro_precio_venta) as TOTAL1, a.pro_maximo,\n"
                + "                a.pro_ubicacion, SUM(a.pro_stock) as TOTAL2\n"
                + "                from productos a ";

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            KardexMd rg;
            int x = 0;
            
            while (rs.next()) {
                rg = new KardexMd();
                x++;
                rg.setCorrelativo(String.valueOf(x));
                rg.setCodigo(rs.getString(1));
                rg.setNombre(rs.getString(2));
                rg.setMarca(rs.getString(3));
               
                rg.setCantidad_ing(rs.getString(4));
                rg.setPre_comp(rs.getString(5));
                rg.setPrecio_ing(rs.getString(6));
                rg.setCantidad_sal(rs.getString(7));
                rg.setPrecio_sal(rs.getString(8));
                rg.setStopbarrita(rs.getString(9));

            
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

    public List<KardexMd> REGselectTodas(String anio) throws SQLException, ClassNotFoundException {
        List<KardexMd> alldata = new ArrayList<KardexMd>();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = "";

        try {
            Date date = formatter.parse(anio);
            formatter.applyPattern("yyyy/MM/dd");
            dateInString = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString2 = "";

        String query = "select a.pro_id,a.pro_descripcion,pro_marca,   \n" +
"                  a.pro_minimo, a.pro_precio_compra,a.pro_precio_venta, a.pro_maximo,    \n" +
"                  CASE a.pro_ubicacion    \n" +
"                  when 'S' then 'SALA DE VENTAS '    \n" +
"                  when 'P' then 'PATIO '    \n" +
"                  when 'BI' then 'BODEGA 1 INTERIOR '    \n" +
"                  when 'B2' then 'BODEGA 2 '    \n" +
"                  when 'B3' then 'BODEGA 3-PATIOS '    \n" +
"                  when 'B4' then 'BODEGA 4-CARRIZAL '    \n" +
"                  END as ubicacion, \n" +
"                  a.pro_stock_barrita,a.pro_stock_carrizal,a.pro_stock_angeles   \n" +
"                  from productos a;";
//                + "select a.pro_id,a.pro_descripcion,pro_marca,  \n" +
//"                 a.pro_minimo, a.pro_precio_compra,a.pro_precio_venta, a.pro_maximo,   \n" +
//"                 concat(CASE a.pro_ubicacion   \n" +
//"                 when 'S' then 'SALA DE VENTAS '   \n" +
//"                 when 'P' then 'PATIO '   \n" +
//"                 when 'BI' then 'BODEGA 1 INTERIOR '   \n" +
//"                 when 'B2' then 'BODEGA 2 '   \n" +
//"                 when 'B3' then 'BODEGA 3-PATIOS '   \n" +
//"                 when 'B4' then 'BODEGA 4-CARRIZAL '   \n" +
//"                 END , \n" +
//"                  CASE a.pro_ferreteria   \n" +
//"                 when '1' then 'BARRITA'   \n" +
//"                 when '2' then 'MANANTIAL'   \n" +
//"                    \n" +
//"                 END )AS UBICACION,  \n" +
//"                 a.pro_stock_barrita  \n" +
//"                 from almacen.productos a where a.pro_ferreteria='" + ferre+ "';"


        String query2 = "select a.pro_id,a.pro_descripcion,\n"
                + "                a.pro_minimo, SUM( a.pro_precio_venta) as TOTAL1, a.pro_maximo,\n"
                + "                a.pro_ubicacion, SUM(a.pro_stock_barrita) as TOTAL2\n"
                + "                from productos a ";

        try {
            conexion = cnn.Conexion();
            st = conexion.createStatement();
            rs = st.executeQuery(query);
            KardexMd rg;
            int x = 0;
            
            while (rs.next()) {
                rg = new KardexMd();
                x++;
                rg.setCorrelativo(String.valueOf(x));
                rg.setCodigo(rs.getString(1));
                rg.setNombre(rs.getString(2));
                rg.setMarca(rs.getString(3));
               
                rg.setCantidad_ing(rs.getString(4));
                rg.setPre_comp(rs.getString(5));
                rg.setPrecio_ing(rs.getString(6));
                rg.setCantidad_sal(rs.getString(7));
                rg.setPrecio_sal(rs.getString(8));
                rg.setStopbarrita(rs.getString(9));
                rg.setStopcarrizal(rs.getString(10));
                rg.setStopangeles(rs.getString(11));

            
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
