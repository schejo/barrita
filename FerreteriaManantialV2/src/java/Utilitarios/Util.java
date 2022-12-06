package Utilitarios;

import Conexion.Conexion;
import MD.ProductosMd;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import org.json.JSONObject;
import org.zkoss.zul.Button;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

public class Util {

    public void cargaCombox(String paquete, Combobox co) throws SQLException {
        PreparedStatement smt = null;
        Connection conn;
        Conexion conex = new Conexion();
        conn = conex.Conexion();
        ResultSet rst = null;

        co.getItems().clear();

        try {

            Comboitem item = new Comboitem();
            smt = conn.prepareStatement(paquete);
            rst = smt.executeQuery();

            while (rst.next()) {
                item = new Comboitem();
                item.setLabel(rst.getString(2));
                item.setValue(rst.getString(1));
                item.setParent(co);
            }

            co.setValue(null);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (smt != null) {
                smt.close();
            }
            if (rst != null) {
                smt.close();
            }
            if (conn != null) {
                conn.close();
                conex.cerrar();
            }
        }
    }

    public void cargaComboxLista(List<ProductosMd> paquete, Combobox co) throws SQLException {

        co.getItems().clear();
        Comboitem item = new Comboitem();

        /*for (int i = 0; i < paquete.size(); i++) {
         //   if (comparar(paquete.get(i).getProductoDescripcion().toUpperCase(), co.getText().toUpperCase())) {
                item = new Comboitem();
                item.setLabel(paquete.get(i).getProductoDescripcion());
                item.setValue(paquete.get(i).getProductoId());
                item.setParent(co);

            }
        }*/
        co.setValue(item.getValue().toString());
    }

    public boolean comparar(String bd, String busqueda) {
        int searchMeLength = bd.length();
        int findMeLength = busqueda.length();
        boolean foundIt = false;
        for (int i = 0; i <= (searchMeLength - findMeLength); i++) {
            if (bd.regionMatches(i, busqueda, 0, findMeLength)) {
                foundIt = true;
                break;
            }
        }
        return foundIt;
    }

    public String ip = "67.205.143.90", ip4 = "45.55.47.25";

    public String EnvioFactura(int ambiente, String Xml, String Funcion) {
        String tokenRecibido = ApiToken(ambiente);

        String[] dividir = tokenRecibido.split("-");

        if (dividir[0].equals("01")) {

            String Direccion = "";

            Direccion = "http://" + ip + ":8080/FEL/api/" + Funcion;

            String salida = EnvioCertificador(Xml, Direccion, dividir[2]);

            if (!salida.equals("ERROR")) {

                JSONObject myJson = new JSONObject(salida);
                JSONObject myJson2 = new JSONObject(myJson.get("Mensaje").toString());

                return myJson2.get("Codigo") + "|API FACTURA," + myJson2.get("Descripcion") + "|" + (myJson2.get("Codigo").equals("01") ? myJson2.get("Certificados") : "");

            } else {
                return "200|API FACTURA, Error de conexión con API";
            }
        } else {
            return "100|API TOKEN, " + dividir[1] + " -NO VALIDADO";
        }

    }

    public String ApiToken(int ambiente) {
        String Direccion = "", Xml = "{\"Usuario\": \"FELBD\", \"Clave\": \"SmVkaS0xMTQ5NjA=\"}";

        Direccion = "http://" + ip + ":8080/FEL/api/Token";

        String salida = EnvioCertificador(Xml, Direccion, "");

        if (!salida.equals("ERROR")) {

            JSONObject myJson = new JSONObject(salida);
            JSONObject myJson2 = new JSONObject(myJson.get("Mensaje").toString());

            return myJson2.get("Codigo") + "-" + myJson2.get("Descripcion") + "-" + myJson2.get("Token");

        } else {
            return "100-Error de conexión con API-NO VALIDADO";
        }
    }

    public static String EnvioCertificador(String input, String Direccion, String token) {
        String output;

        try {

            URL url = new URL(Direccion);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/xml");

            if (!token.equals("")) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            //	if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) { throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());}
            if (conn.getResponseCode() != 200) {
                return "ERROR";
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            output = br.readLine();

            conn.disconnect();

            return output;

        } catch (IOException e) {
            return "ERROR";
        } catch (Exception e) {
            return "ERROR";
        }
    }

    public static String Pars(String Entrada) {
        String REPLACEMENT = "AaEeIiOoUuNnUu ", ORIGINAL = "ÁáÉéÍíÓóÚúÑñÜü&";
        char[] array = Entrada.toCharArray();
        for (int index = 0; index < array.length; index++) {
            int pos = ORIGINAL.indexOf(array[index]);
            if (pos > -1) {
                array[index] = REPLACEMENT.charAt(pos);
            }
        }
        return String.valueOf(array);
    }

    public static String Validar_JSON(String JSON) {
        try {
            new JSONObject(JSON);
            return "ok";
        } catch (Exception jse) {
            return "ERROR";
        }
    }

}
