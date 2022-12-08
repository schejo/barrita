package DAL;

import Conexion.Conexion;
import MD.GraficaPorTipoMd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zul.SimplePieModel;

public class GraficaPorTipoDal {

    public List<GraficaPorTipoMd> Grafica1(String sFecha_inicial) throws SQLException {
        List<GraficaPorTipoMd> lstDatos = new ArrayList<GraficaPorTipoMd>();
        System.out.println("Linea 21 Dal");

        PreparedStatement smt = null;
        Connection conn = null;
        Conexion conex = new Conexion();
        conn = conex.Conexion();
        String sql = null;
        ResultSet rs = null;

        try {

            sql = "  select CASE pro_tipo\n"
                    + "  when 'A' then 'ACCESORIOS'\n"
                    + "  when 'H' then 'Herramientas'\n"
                    + "  when 'C' then 'Construccion'\n"
                    + "  when 'P' then 'Pvc'\n"
                    + "  when 'I' then 'Hierro'\n"
                    + "  when 'E' then 'Electricos'\n"
                    + "  when 'T' then 'Tornillos'\n"
                    + "  when 'M' then 'Madera'\n"
                    + "  when 'S' then 'Ceramicos'\n"
                    + "  when 'G' then 'Griferia'\n"
                    + "  when 'O' then 'Otro'\n"
                    + "  END AS TIPO,\n"
                    + "  count(pro_id) as CANTIDAD\n"
                    + "  from  productos\n"
                    + "  group by tipo";

            smt = conn.prepareStatement(sql);
            rs = smt.executeQuery();

            while (rs.next()) {
                GraficaPorTipoMd temp = new GraficaPorTipoMd();
                temp.setTipo(rs.getString(1));
                temp.setSumatipo(rs.getString(2));

                lstDatos.add(temp);
            }

        } finally {
            if (smt != null) {
                smt.close();
                smt = null;
            }
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (conn != null) {
                conex.cerrar();
            }
        }
        return lstDatos;
    }

    public SimplePieModel Grafica2(String sFecha_inicial) throws SQLException {
        List<GraficaPorTipoMd> lstDatos = new ArrayList<GraficaPorTipoMd>();

        SimplePieModel model = new SimplePieModel();
        PreparedStatement smt = null;
        Connection conn;
        Conexion conex = new Conexion();
        conn = conex.Conexion();
        String sql = null;
        ResultSet result2 = null;
        try {

            sql = " select CASE pro_tipo\n"
                    + "  when 'A' then 'ACCESORIOS'\n"
                    + "  when 'H' then 'Herramientas'\n"
                    + "  when 'C' then 'Construccion'\n"
                    + "  when 'P' then 'Pvc'\n"
                    + "  when 'I' then 'Hierro'\n"
                    + "  when 'E' then 'Electricos'\n"
                    + "  when 'T' then 'Tornillos'\n"
                    + "  when 'M' then 'Madera'\n"
                    + "  when 'S' then 'Ceramicos'\n"
                    + "  when 'G' then 'Griferia'\n"
                    + "  when 'O' then 'Otro'\n"
                    + "  END AS TIPO,\n"
                    + "  count(pro_stock_barrita) as CANTIDAD\n"
                    + "  from  productos\n"
                    + "  group by tipo";

            smt = conn.prepareStatement(sql);
            result2 = smt.executeQuery();

            while (result2.next()) {

                GraficaPorTipoMd temp = new GraficaPorTipoMd();

                temp.setTipo(result2.getString(1));
                temp.setSumatipo(result2.getString(2));

                lstDatos.add(temp);
            }

            for (int i = 0; i < lstDatos.size(); i++) {
                model.setValue(lstDatos.get(i).getTipo(), Integer.parseInt(lstDatos.get(i).getSumatipo()));
            }

        } finally {
            if (smt != null) {
                smt.close();
                smt = null;
            }
            if (result2 != null) {
                result2.close();
                result2 = null;
            }
            if (conn != null) {
                conex.cerrar();
            }
        }
        return model;
    }
}
