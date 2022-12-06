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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;

/**
 *
 * @author HP 15
 */
public class ReporteDeudoresCtrl extends GenericForwardComposer{
     private Datebox fechaG;
     ClientesMd clienteMD = new ClientesMd();
    ClientesDal clienteDal = new ClientesDal();
     
       public void doAfterComposer(Component comp) throws Exception {
        super.doAfterCompose(comp);
        
    }
       
        public void onClick$btnDescargar(Event e) throws SQLException, ClassNotFoundException, IOException {
             String usuario = (String) desktop.getSession().getAttribute("USER");
              ReporteExcel(fechaG.getText(), usuario);
        }
         public void ReporteExcel(String anio, String user) throws SQLException, IOException, ClassNotFoundException {
          ClientesDal dataBase = new ClientesDal();
        
        List<ClientesMd> allReporteCur = new ArrayList<ClientesMd>();
        
        ClientesDal rd = new ClientesDal();
        allReporteCur = rd.REGselectDeudores(anio);

        Workbook workbook = new HSSFWorkbook();

        EstilosReporte estilo = new EstilosReporte();
        CellStyle style = estilo.Estilo1(workbook);
        CellStyle style2 = estilo.Estilo2(workbook);
        CellStyle styleEntero = estilo.EstiloEnteros2(workbook);
        //hoja
       // String dirImagen = desktop.getWebApp().getRealPath("bootstrap") + "/img/logo.png";
        Sheet listSheet = workbook.createSheet("Reporte Deudores");
       // estilo.insertImage(workbook, listSheet, dirImagen);
        //filas
        Row titulo1 = listSheet.createRow(0);
        Row titulo2 = listSheet.createRow(1);
        Row titulo3 = listSheet.createRow(2);
        Row encabezado = listSheet.createRow(3);
        int index = 4;
        
         Cell c1 = titulo1.createCell(1);
        c1.setCellValue("DISTRIBUIDORA");
        c1.setCellStyle(style);

        Cell c2 = titulo2.createCell(1);
        c2.setCellValue("LOS ANGELES, S.A");
        c2.setCellStyle(style);

        Cell c3 = titulo3.createCell(1);
        c3.setCellValue("DEL.: " + anio + " usuario: " + user);
        c3.setCellStyle(style);
        
        Cell cE1 = encabezado.createCell(0);
        cE1.setCellValue("CODIGO");
        cE1.setCellStyle(style2);

        Cell cE2 = encabezado.createCell(1);
        cE2.setCellValue("CLIENTE");
        cE2.setCellStyle(style2);

        Cell cE3 = encabezado.createCell(2);
        cE3.setCellValue("LIMITE");
        cE3.setCellStyle(style2);

        Cell cE4 = encabezado.createCell(3);
        cE4.setCellValue("CREDITO OTORGADO");
        cE4.setCellStyle(style2);

        Cell cE5 = encabezado.createCell(4);
        cE5.setCellValue("CREDITO PAGADO");
        cE5.setCellStyle(style2);

        Cell cE6 = encabezado.createCell(5);
        cE6.setCellValue("SALDO");
        cE6.setCellStyle(style2);
        
        Cell cE7 = encabezado.createCell(6);
        cE7.setCellValue("FECHA DE CREDITO");
        cE7.setCellStyle(style2);
        
        Cell cE8 = encabezado.createCell(7);
        cE8.setCellValue("FECHA DE PAGO");
        cE8.setCellStyle(style2);
        
        Cell cE9 = encabezado.createCell(8);
        cE9.setCellValue("FACTURA O VALE");
        cE9.setCellStyle(style2);
        
        Cell cE10 = encabezado.createCell(9);
        cE10.setCellValue("OBSERVACIONES");
        cE10.setCellStyle(style2);
        
        Cell cE11 = encabezado.createCell(10);
        cE11.setCellValue("USUARIO");
        cE11.setCellStyle(style2);
        
         for (ClientesMd item : allReporteCur) {
             Row contenido = listSheet.createRow(index++);
            

            Cell cC1 = contenido.createCell(0);
            cC1.setCellValue(item.getCodigoCliente());
            cC1.setCellStyle(styleEntero);

            Cell cC2 = contenido.createCell(1);
            cC2.setCellValue(item.getNombreMos());
            cC2.setCellStyle(styleEntero);

            Cell cC3 = contenido.createCell(2);
            cC3.setCellValue(item.getLimite());
            cC3.setCellStyle(style2);

            Cell cC4 = contenido.createCell(3);
            cC4.setCellValue(item.getCredito());
            cC4.setCellStyle(style2);

            Cell cC5 = contenido.createCell(4);
            cC5.setCellValue(item.getMontopago());
            cC5.setCellStyle(style2);

            Cell cC6 = contenido.createCell(5);
            cC6.setCellValue(item.getDisponible());
            cC6.setCellStyle(styleEntero);
            
            Cell cC7 = contenido.createCell(6);
            cC7.setCellValue(item.getFechaAlta());
            cC7.setCellStyle(styleEntero);
            
            Cell cC8 = contenido.createCell(7);
            cC8.setCellValue(item.getFechaModi());
            cC8.setCellStyle(styleEntero);
            
            Cell cC9 = contenido.createCell(8);
            cC9.setCellValue(item.getFactura());
            cC9.setCellStyle(styleEntero);
            
            Cell cC10 = contenido.createCell(9);
            cC10.setCellValue(item.getObs());
            cC10.setCellStyle(styleEntero);
            
            Cell cC11 = contenido.createCell(10);
            cC11.setCellValue(item.getUsuario());
            cC11.setCellStyle(styleEntero);
            
           
            
         }
         try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //  estilo.autoSizeColumns(workbook, 6);
            workbook.write(baos);
            AMedia amedia = new AMedia("REPORTE DE CREDITOS.xls", "xls", "application/file", baos.toByteArray());
            Filedownload.save(amedia);
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
         
        
    }
       
    
}
