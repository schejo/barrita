/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctrl_;

import DAL.ClientesDal;
import MD.ClientesMd;
import Util.EstilosReporte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zul.Combobox;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.zkoss.util.media.AMedia;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

/**
 *
 * @author chejo
 */
public class ClientesCtrl extends GenericForwardComposer {

    private Textbox Ncliente;
    private Textbox NitCliente;
    private Textbox Direccion;
    private Doublebox Telefono;
    private Textbox Correo;
    private Textbox Usu;
    private Doublebox credito;
    private Combobox selec;
    private Label titulo;

    private Include rootPagina;

    ClientesMd clienteMD = new ClientesMd();
    ClientesDal clienteDal = new ClientesDal();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Usu.setText(desktop.getSession().getAttribute("USER").toString());
        credito.setVisible(false);
        titulo.setVisible(false);
    }

    //boton descargar reporte
    public void onClick$btnDescargar(Event e) throws SQLException, ClassNotFoundException, IOException {

        ReporteExcel(Usu.getText());
    }

    public void onChange$selec(Event e) {

        String op = selec.getSelectedItem().getValue().toString();
        if (op.equals("1")) {
            //credito
            credito.setVisible(true);
            titulo.setVisible(true);

        }

        if (op.equals("2")) {
            credito.setVisible(false);
            titulo.setVisible(false);
        }
    }

    //metodo para hacer reporte en excel
    public void ReporteExcel(String usuario) throws SQLException, IOException, ClassNotFoundException {
        // ReporteCliDal dataBase = new ReporteCliDal();

        List<ClientesMd> allReporteCli = new ArrayList<ClientesMd>();
        // List<ReporteViajesMd> lista = dataBase.GetByFecha(cambio_fecha(fechaInicial), cambio_fecha(fechaFinal));
        ClientesDal repDal = new ClientesDal();
        allReporteCli = repDal.REGselect();
        //PARA OBTENER LA FECHA
        clienteMD = new ClientesMd();
        clienteMD = repDal.obtenerFecha();
        String fecha = clienteMD.getObtenerFecha();

        Workbook workbook = new HSSFWorkbook();

        EstilosReporte estilo = new EstilosReporte();
        CellStyle style = estilo.Estilo1(workbook);
        CellStyle style2 = estilo.Estilo2(workbook);
        CellStyle styleEntero = estilo.EstiloEnteros2(workbook);
        //hoja
        String dirImagen = desktop.getWebApp().getRealPath("bootstrap") + "/img/reportar.png";

        Sheet listSheet = workbook.createSheet("Reporte Clientes");
        //estilo.insertImage(workbook, listSheet, dirImagen);
        //filas
        Row titulo1 = listSheet.createRow(0);
        Row titulo2 = listSheet.createRow(1);
        Row titulo3 = listSheet.createRow(2);
        Row encabezado = listSheet.createRow(3);
        int index = 4;

        Cell c1 = titulo1.createCell(1);
        c1.setCellValue("DISTRIBUIDORA LA BARRITA, S.A");
        c1.setCellStyle(style);

        Cell c2 = titulo2.createCell(1);
        c2.setCellValue("REPORTE DE CLIENTES");
        c2.setCellStyle(style);

        Cell c3 = titulo3.createCell(1);
        c3.setCellValue("FECHA: " + fecha + ", USUARIO: " + usuario);
        c3.setCellStyle(style);

        Cell cE1 = encabezado.createCell(0);
        cE1.setCellValue("CODIGO");
        cE1.setCellStyle(style2);

        Cell cE2 = encabezado.createCell(1);
        cE2.setCellValue("NOMBRE");
        cE2.setCellStyle(style2);

        Cell cE3 = encabezado.createCell(2);
        cE3.setCellValue("NIT");
        cE3.setCellStyle(style2);

        Cell cE4 = encabezado.createCell(3);
        cE4.setCellValue("DIRECCION");
        cE4.setCellStyle(style2);

        Cell cE5 = encabezado.createCell(4);
        cE5.setCellValue("TELEFONO");
        cE5.setCellStyle(style2);

        Cell cE6 = encabezado.createCell(5);
        cE6.setCellValue("FECHA DE CREACION");
        cE6.setCellStyle(style2);

        Cell cE7 = encabezado.createCell(6);
        cE7.setCellValue("USUARIO QUE LO CREO");
        cE7.setCellStyle(style2);

        Cell cE8 = encabezado.createCell(7);
        cE8.setCellValue("FECHA DE MODIFICACION*");
        cE8.setCellStyle(style2);

        Cell cE9 = encabezado.createCell(8);
        cE9.setCellValue("USUARIO QUE MODIFICO*");
        cE9.setCellStyle(style2);

        Cell cE10 = encabezado.createCell(9);
        cE10.setCellValue("CORREO");
        cE10.setCellStyle(style2);

        for (ClientesMd item : allReporteCli) {
            Row contenido = listSheet.createRow(index++);

            Cell cC1 = contenido.createCell(0);
            cC1.setCellValue(item.getCodigoCliente());
            cC1.setCellStyle(styleEntero);

            Cell cC2 = contenido.createCell(1);
            cC2.setCellValue(item.getNombreComercial());
            cC2.setCellStyle(style2);

            Cell cC3 = contenido.createCell(2);
            cC3.setCellValue(item.getNit());
            cC3.setCellStyle(style2);

            Cell cC4 = contenido.createCell(3);
            cC4.setCellValue(item.getDireccion());
            cC4.setCellStyle(style2);

            Cell cC5 = contenido.createCell(4);
            cC5.setCellValue(item.getTelefono());
            cC5.setCellStyle(style2);

            Cell cC6 = contenido.createCell(5);
            cC6.setCellValue(item.getFechaAlta());
            cC6.setCellStyle(styleEntero);

            Cell cC7 = contenido.createCell(6);
            cC7.setCellValue(item.getUsuarioAlta());
            cC7.setCellStyle(styleEntero);

            Cell cC8 = contenido.createCell(7);
            cC8.setCellValue(item.getFechaModi());
            cC8.setCellStyle(style2);

            Cell cC9 = contenido.createCell(8);
            cC9.setCellValue(item.getUsuarioModi());
            cC9.setCellStyle(style2);

            Cell cC10 = contenido.createCell(9);
            cC10.setCellValue(item.getCorreCleinte());
            cC10.setCellStyle(style2);

        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            estilo.autoSizeColumns(workbook, 4);
            workbook.write(baos);
            AMedia amedia = new AMedia("REPORTE DE CLIENTES.xls", "xls", "application/file", baos.toByteArray());
            Filedownload.save(amedia);
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //boton guardar
    public void onClick$btnGuardar(Event e) {
        int op = 0;
        if (Ncliente.getText().trim().equals("")) {
            op = 1;
        }
        if (NitCliente.getText().trim().equals("") || NitCliente.getText().trim().equals("1")) {
            op = 1;
        }
        if (Direccion.getText().trim().equals("")) {
            op = 1;
        }
        if (Telefono.getText().trim().equals("") || Telefono.getText().trim().equals("1")) {
            op = 1;
        }
        if (Correo.getText().trim().equals("")) {
            op = 1;
        }
        if (op == 0) {
            clienteMD = new ClientesMd();
            clienteMD.setNombreComercial(Ncliente.getText().toUpperCase());
            clienteMD.setNit(NitCliente.getText().toUpperCase());
            clienteMD.setDireccion(Direccion.getText().toUpperCase());
            clienteMD.setTelefono(Telefono.getText().toUpperCase());
            clienteMD.setCorreCleinte(Correo.getText().toUpperCase());
            clienteMD.setUsuario(Usu.getText().toUpperCase());
            if (selec.getSelectedItem().getValue().toString().equals("1")) {
                //credito
                clienteMD.setCredito(credito.getText().toUpperCase());

            } else {
                //sin cretido
                  clienteMD.setCredito("0");
            }

            clienteMD = clienteDal.saveCL(clienteMD);

            if (clienteMD.getResp().equals("1")) {
                clear();
                Clients.showNotification(clienteMD.getMsg() + "<br/>",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 5000);
            } else {
                Clients.showNotification(clienteMD.getMsg() + "<br/>",
                        Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 5000);
            }

        } else {
            {
                Clients.showNotification("Hay Campos Vacios<br/> O <br/>Telefono,Nit tiene valor 1   <br/> <br/>Intentelo de Nuevo",
                        Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 5000);
            }

        }
    }

    public void onClick$btnSalir() {
        rootPagina.setSrc("/Vistas/Principal.zul");
    }

    public void onClick$btnModificar() {
        rootPagina.setSrc("/Vistas/ClientesUP.zul");
    }

    public void onClick$btnNuevo(Event e) throws SQLException {

        clear();

    }

    public void clear() {

        Ncliente.setText("");
        NitCliente.setText("");
        Direccion.setText("");
        Telefono.setText("1");
        Correo.setText("");

    }

}
