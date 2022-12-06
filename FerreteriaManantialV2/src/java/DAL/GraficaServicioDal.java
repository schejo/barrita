package DAL;

import Conexion.Conexion;
import MD.GraficaServicioMd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zul.SimplePieModel;

public class GraficaServicioDal {

    public List<GraficaServicioMd> Grafica1(String sFecha_inicial) throws SQLException {
        List<GraficaServicioMd> lstDatos = new ArrayList<GraficaServicioMd>();

        PreparedStatement smt = null;
        Connection conn = null;
        Conexion conex = new Conexion();
        conn = conex.Conexion();
        String sql = null;
        ResultSet rs = null;

        try {

            sql = "  select CASE pro_tipo_servicio\n"
                    + "  when 'S' then 'SERVICIO'\n"
                    + "  when 'B' then 'BIEN'\n"
                    + "  END AS TIPO,\n"
                    + "  count(pro_id) as CANTIDAD\n"
                    + "  from  almacen.productos\n"
                    + "  group by tipo";
           

            smt = conn.prepareStatement(sql);
            rs = smt.executeQuery();

            while (rs.next()) {
                GraficaServicioMd temp = new GraficaServicioMd();
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
        List<GraficaServicioMd> lstDatos = new ArrayList<GraficaServicioMd>();

        SimplePieModel model = new SimplePieModel();
        PreparedStatement smt = null;
        Connection conn;
        Conexion conex = new Conexion();
        conn = conex.Conexion();
        String sql = null;
        ResultSet result2 = null;
        try {

            sql = " select CASE pro_tipo_servicio\n"
                    + "  when 'S' then 'SERVICIO'\n"
                    + "  when 'B' then 'BIEN'\n"
                    + "  END AS TIPO,\n"
                    + "  count(pro_id) as CANTIDAD\n"
                    + "  from  almacen.productos\n"
                    + "  group by tipo";

            smt = conn.prepareStatement(sql);
            result2 = smt.executeQuery();

            while (result2.next()) {

                GraficaServicioMd temp = new GraficaServicioMd();

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
