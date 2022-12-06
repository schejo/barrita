package DAL;

import Conexion.Conexion;
import MD.GraficaVentasEfectivoMd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zul.SimplePieModel;

public class GraficaVentasEfectivoDal {

    public List<GraficaVentasEfectivoMd> Grafica1(String sFecha_inicial) throws SQLException {
        List<GraficaVentasEfectivoMd> lstDatos = new ArrayList<GraficaVentasEfectivoMd>();

        PreparedStatement smt = null;
        Connection conn = null;
        Conexion conex = new Conexion();
        conn = conex.Conexion();
        String sql = null;
        ResultSet rs = null;

        try {

            sql = "select CASE fac_tipo_pago\n"
                    + "                      when 'E' then 'EFECTIVO'\n"
                    + "                      when 'T' then 'TARJETA'\n"
                    + "                      when 'C' then 'CREDITO'\n"
                    + "                      END AS TIPO_PAGO,\n"
                    + "                      count(fac_numero) as CANTIDAD\n"
                    + "                      from  almacen.factura \n"
                    + "                      group by fac_tipo_pago";

            smt = conn.prepareStatement(sql);
            rs = smt.executeQuery();

            while (rs.next()) {
                GraficaVentasEfectivoMd temp = new GraficaVentasEfectivoMd();
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
        List<GraficaVentasEfectivoMd> lstDatos = new ArrayList<GraficaVentasEfectivoMd>();

        SimplePieModel model = new SimplePieModel();
        PreparedStatement smt = null;
        Connection conn;
        Conexion conex = new Conexion();
        conn = conex.Conexion();
        String sql = null;
        ResultSet result2 = null;
        try {

            sql = "select CASE fac_tipo_pago\n"
                    + "                      when 'E' then 'EFECTIVO'\n"
                    + "                      when 'T' then 'TARJETA'\n"
                    + "                      when 'C' then 'CREDITO'\n"
                    + "                      END AS TIPO_PAGO,\n"
                    + "                      count(fac_numero) as CANTIDAD\n"
                    + "                      from  almacen.factura \n"
                    + "                      group by fac_tipo_pago";

            smt = conn.prepareStatement(sql);
            result2 = smt.executeQuery();

            while (result2.next()) {

                GraficaVentasEfectivoMd temp = new GraficaVentasEfectivoMd();

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
