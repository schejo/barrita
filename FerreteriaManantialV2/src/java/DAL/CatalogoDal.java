/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import Conexion.Conexion;
import MD.CatalogosMd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP 15
 */
public class CatalogoDal {

    Conexion obtener = new Conexion();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
     public List<CatalogosMd> consulta2Tabla() {
        List<CatalogosMd> lista = new ArrayList<CatalogosMd>();

        CatalogosMd data;
        String sql = "select pro_id, CONCAT(pro_descripcion,' MARCA: ',pro_marca,' PRECIO Q',pro_precio_venta) from productos2;";
                
//                
        try {
            conn = obtener.Conexion();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                data = new CatalogosMd();

                data.setCodemp(rs.getString(1));
                data.setNomemp(rs.getString(2));

                lista.add(data);
            }
            rs.close();
            pst.close();

            obtener.desconectar();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Exception..: " + e.getMessage());
        }

        return lista;
    }


    public List<CatalogosMd> consulta() {
        List<CatalogosMd> lista = new ArrayList<CatalogosMd>();

        CatalogosMd data;
        String sql = "select pro_id, CONCAT(pro_descripcion,' MARCA: ',pro_marca,' PRECIO Q',pro_precio_venta) from productos;";
                
//                
        try {
            conn = obtener.Conexion();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                data = new CatalogosMd();

                data.setCodemp(rs.getString(1));
                data.setNomemp(rs.getString(2));

                lista.add(data);
            }
            rs.close();
            pst.close();

            obtener.desconectar();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Exception..: " + e.getMessage());
        }

        return lista;
    }

}
