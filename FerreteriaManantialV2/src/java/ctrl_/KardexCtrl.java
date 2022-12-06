package ctrl_;

import DAL.KardexDal;
import MD.KardexMd;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;

public class KardexCtrl extends GenericForwardComposer {

    private Datebox fechaG;
    private Combobox ubiFerrete;

    public void doAfterComposer(Component comp) throws Exception {
        super.doAfterCompose(comp);
        
    }
    
       public void onChange$ubiFerrete(Event e) {
        
    }

    public void onClick$btnDescargar(Event e) throws SQLException, ClassNotFoundException {
       String usuario = (String) desktop.getSession().getAttribute("USER");
       int op=0;
        if (ubiFerrete.getSelectedIndex() == -1) {
            op = 1;
        }
       
        if (!fechaG.getText().isEmpty() && op!=1 ) {
              if (ubiFerrete.getSelectedItem().getValue().toString().equals("1")) {
                 //barrita
                 GeneraPDFBarrita(fechaG.getText(), usuario);
            } else {
                if (ubiFerrete.getSelectedItem().getValue().toString().equals("2")) {
                  //carrizal
                  GeneraPDFCarrizal(fechaG.getText(), usuario);
                } else { if (ubiFerrete.getSelectedItem().getValue().toString().equals("0")) {
                    //todas
                    GeneraPDFTodas(fechaG.getText(), usuario, ubiFerrete.getSelectedItem().getValue().toString());
                }else{
                    //los angeles
                    GeneraPDFAngeles(fechaG.getText(), usuario);
                }
                    
                }
            }
            
            
        } else {
            Clients.showNotification("INGRESE UNA FECHA Y SELECIONE UNA FERRETERIA POR FAVOR PARA PODER GENERAR EL PDF");
        }
       
      
       
       
    }
    public void GeneraPDFAngeles(String anio, String user) throws SQLException, ClassNotFoundException {
        System.out.println("Generar PDF");
        Document document;
        Paragraph ParrafoHoja = new Paragraph();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        List<KardexMd> alldata = new ArrayList<KardexMd>();
        KardexDal rd = new KardexDal();
        alldata = rd.REGselectAngeles(anio);
        Float totalCompra =Float.parseFloat("0.00");
        Float totalEfectivo = Float.parseFloat("0.00");
        Float totalCantidad = Float.parseFloat("0.00");

//        for (int i = 0; i < alldata.size(); i++) {
//            //totalCompra +=Float.parseFloat(alldata.get(i).getPre_comp());
//            totalEfectivo += Float.parseFloat(alldata.get(i).getPrecio_ing());
//            totalCantidad += Float.parseFloat(alldata.get(i).getStopbarrita());
//        }

        if (alldata.isEmpty()) {
            Clients.showNotification("NO TIENE DATOS..!");
        }
        try {

            document = new Document();
            document.setPageSize(PageSize.LETTER);
            PdfWriter.getInstance(document, baos);
            System.out.println("Despues del PDFWriter" + anio);

            String dirImagen = desktop.getWebApp().getRealPath("bootstrap") + "/img/reportar.png";
            Image im = Image.getInstance(dirImagen);
            im.setAlignment(Image.ALIGN_RIGHT | Image.TEXTWRAP);
            im.setAbsolutePosition(25, 700);
            im.scalePercent(10);
            ParrafoHoja.add(im);

            ParrafoHoja.add(new Paragraph("              DISTRIBUIDORA"));
            ParrafoHoja.add(new Paragraph("              LOS ANGELES, S.A"));
            ParrafoHoja.add(new Paragraph("              Kardex de Productos"));
            ParrafoHoja.add(new Paragraph("              Fecha de Reporte.: " + anio));
            ParrafoHoja.add(new Paragraph("              USUARIO.: " + user));

            //System.out.println("CREAR TABLA");
            float anchosFilas[] = {0.2f, 0.8f, 0.3f, 0.4f,0.3f, 0.5f, 0.3f};//Anchos de las filas
            PdfPTable tabla = new PdfPTable(anchosFilas);
            String rotulosColumnas[] = {"COD", "NOMBRE","MARCA", "P.COMPRA","P.VENTA",
                "UBICACION", "SALDO"};//Rotulos de las columnas 

            tabla.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell();

            System.out.println("CONSTRUIR TABLA");
            for (int i = 0; i < rotulosColumnas.length; i++) {
                cell = new PdfPCell(new Paragraph(rotulosColumnas[i]));
                tabla.addCell(cell);
            }

            int i = 0;
            for (KardexMd a : alldata) {
                i++;
                cell = new PdfPCell(new Paragraph(a.getCodigo()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getNombre()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getMarca()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getPre_comp()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getPrecio_ing()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getPrecio_sal()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getStopbarrita()));
                tabla.addCell(cell);
            }

            //SUMATORIA DE COLUMNAS 
            cell = new PdfPCell(new Paragraph("TOTALES"));
            cell.setColspan(2);
            cell.setBorder(0);
            cell.setHorizontalAlignment(1);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(String.valueOf(totalEfectivo)));
            cell.setColspan(1);
            cell.setBorder(0);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(""));
            cell.setColspan(1);
            cell.setBorder(0);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(String.valueOf(totalCantidad)));
            cell.setColspan(1);
            cell.setBorder(0);
            tabla.addCell(cell);
            //FIN DE SUMA DE COLUMNAS

// ParrafoHoja.add(new Paragraph("               Kardex: "));
            ParrafoHoja.add(tabla);
            document.open();
            document.add(ParrafoHoja);
            document.close();

            AMedia amedia = new AMedia("Kardex.PDF", "PDF", "application/file", baos.toByteArray());
            Filedownload.save(amedia);
            baos.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
     public void GeneraPDFCarrizal(String anio, String user) throws SQLException, ClassNotFoundException {
        System.out.println("Generar PDF");
        Document document;
        Paragraph ParrafoHoja = new Paragraph();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        List<KardexMd> alldata = new ArrayList<KardexMd>();
        KardexDal rd = new KardexDal();
        alldata = rd.REGselectCarrizal(anio);
        Float totalCompra =Float.parseFloat("0.00");
        Float totalEfectivo = Float.parseFloat("0.00");
        Float totalCantidad = Float.parseFloat("0.00");

//        for (int i = 0; i < alldata.size(); i++) {
//            //totalCompra +=Float.parseFloat(alldata.get(i).getPre_comp());
//            totalEfectivo += Float.parseFloat(alldata.get(i).getPrecio_ing());
//            totalCantidad += Float.parseFloat(alldata.get(i).getStopbarrita());
//        }

        if (alldata.isEmpty()) {
            Clients.showNotification("NO TIENE DATOS..!");
        }
        try {

            document = new Document();
            document.setPageSize(PageSize.LETTER);
            PdfWriter.getInstance(document, baos);
            System.out.println("Despues del PDFWriter" + anio);

            String dirImagen = desktop.getWebApp().getRealPath("bootstrap") + "/img/reportar.png";
            Image im = Image.getInstance(dirImagen);
            im.setAlignment(Image.ALIGN_RIGHT | Image.TEXTWRAP);
            im.setAbsolutePosition(25, 700);
            im.scalePercent(10);
            ParrafoHoja.add(im);

            ParrafoHoja.add(new Paragraph("              DISTRIBUIDORA"));
            ParrafoHoja.add(new Paragraph("              EL CARRIZAL, S.A"));
            ParrafoHoja.add(new Paragraph("              Kardex de Productos"));
            ParrafoHoja.add(new Paragraph("              Fecha de Reporte.: " + anio));
            ParrafoHoja.add(new Paragraph("              USUARIO.: " + user));

            //System.out.println("CREAR TABLA");
            float anchosFilas[] = {0.2f, 0.8f, 0.3f, 0.4f,0.3f, 0.5f, 0.3f};//Anchos de las filas
            PdfPTable tabla = new PdfPTable(anchosFilas);
            String rotulosColumnas[] = {"COD", "NOMBRE","MARCA", "P.COMPRA","P.VENTA",
                "UBICACION", "SALDO"};//Rotulos de las columnas 

            tabla.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell();

            System.out.println("CONSTRUIR TABLA");
            for (int i = 0; i < rotulosColumnas.length; i++) {
                cell = new PdfPCell(new Paragraph(rotulosColumnas[i]));
                tabla.addCell(cell);
            }

            int i = 0;
            for (KardexMd a : alldata) {
                i++;
                cell = new PdfPCell(new Paragraph(a.getCodigo()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getNombre()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getMarca()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getPre_comp()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getPrecio_ing()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getPrecio_sal()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getStopbarrita()));
                tabla.addCell(cell);
            }

            //SUMATORIA DE COLUMNAS 
            cell = new PdfPCell(new Paragraph("TOTALES"));
            cell.setColspan(2);
            cell.setBorder(0);
            cell.setHorizontalAlignment(1);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(String.valueOf(totalEfectivo)));
            cell.setColspan(1);
            cell.setBorder(0);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(""));
            cell.setColspan(1);
            cell.setBorder(0);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(String.valueOf(totalCantidad)));
            cell.setColspan(1);
            cell.setBorder(0);
            tabla.addCell(cell);
            //FIN DE SUMA DE COLUMNAS

// ParrafoHoja.add(new Paragraph("               Kardex: "));
            ParrafoHoja.add(tabla);
            document.open();
            document.add(ParrafoHoja);
            document.close();

            AMedia amedia = new AMedia("Kardex.PDF", "PDF", "application/file", baos.toByteArray());
            Filedownload.save(amedia);
            baos.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void GeneraPDFBarrita(String anio, String user) throws SQLException, ClassNotFoundException {
        System.out.println("Generar PDF");
        Document document;
        Paragraph ParrafoHoja = new Paragraph();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        List<KardexMd> alldata = new ArrayList<KardexMd>();
        KardexDal rd = new KardexDal();
        alldata = rd.REGselectBarrita(anio);
        Float totalCompra =Float.parseFloat("0.00");
        Float totalEfectivo = Float.parseFloat("0.00");
        Float totalCantidad = Float.parseFloat("0.00");

        for (int i = 0; i < alldata.size(); i++) {
            //totalCompra +=Float.parseFloat(alldata.get(i).getPre_comp());
            totalEfectivo += Float.parseFloat(alldata.get(i).getPrecio_ing());
            totalCantidad += Float.parseFloat(alldata.get(i).getStopbarrita());
        }

        if (alldata.isEmpty()) {
            Clients.showNotification("NO TIENE DATOS..!");
        }
        try {

            document = new Document();
            document.setPageSize(PageSize.LETTER);
            PdfWriter.getInstance(document, baos);
            System.out.println("Despues del PDFWriter" + anio);

            String dirImagen = desktop.getWebApp().getRealPath("bootstrap") + "/img/reportar.png";
            Image im = Image.getInstance(dirImagen);
            im.setAlignment(Image.ALIGN_RIGHT | Image.TEXTWRAP);
            im.setAbsolutePosition(25, 700);
            im.scalePercent(10);
            ParrafoHoja.add(im);

            ParrafoHoja.add(new Paragraph("              DISTRIBUIDORA"));
            ParrafoHoja.add(new Paragraph("              LA BARRITA, S.A"));
            ParrafoHoja.add(new Paragraph("              Kardex de Productos"));
            ParrafoHoja.add(new Paragraph("              Fecha de Reporte.: " + anio));
            ParrafoHoja.add(new Paragraph("              USUARIO.: " + user));

            //System.out.println("CREAR TABLA");
            float anchosFilas[] = {0.2f, 0.8f, 0.3f, 0.4f,0.3f, 0.5f, 0.3f};//Anchos de las filas
            PdfPTable tabla = new PdfPTable(anchosFilas);
            String rotulosColumnas[] = {"COD", "NOMBRE","MARCA", "P.COMPRA","P.VENTA",
                "UBICACION", "SALDO"};//Rotulos de las columnas 

            tabla.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell();

            System.out.println("CONSTRUIR TABLA");
            for (int i = 0; i < rotulosColumnas.length; i++) {
                cell = new PdfPCell(new Paragraph(rotulosColumnas[i]));
                tabla.addCell(cell);
            }

            int i = 0;
            for (KardexMd a : alldata) {
                i++;
                cell = new PdfPCell(new Paragraph(a.getCodigo()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getNombre()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getMarca()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getPre_comp()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getPrecio_ing()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getPrecio_sal()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getStopbarrita()));
                tabla.addCell(cell);
            }

            //SUMATORIA DE COLUMNAS 
            cell = new PdfPCell(new Paragraph("TOTALES"));
            cell.setColspan(2);
            cell.setBorder(0);
            cell.setHorizontalAlignment(1);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(String.valueOf(totalEfectivo)));
            cell.setColspan(1);
            cell.setBorder(0);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(""));
            cell.setColspan(1);
            cell.setBorder(0);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(String.valueOf(totalCantidad)));
            cell.setColspan(1);
            cell.setBorder(0);
            tabla.addCell(cell);
            //FIN DE SUMA DE COLUMNAS

// ParrafoHoja.add(new Paragraph("               Kardex: "));
            ParrafoHoja.add(tabla);
            document.open();
            document.add(ParrafoHoja);
            document.close();

            AMedia amedia = new AMedia("Kardex.PDF", "PDF", "application/file", baos.toByteArray());
            Filedownload.save(amedia);
            baos.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
     

    public void GeneraPDFTodas(String anio, String user,String ferre) throws SQLException, ClassNotFoundException {
        System.out.println("Generar PDF");
        Document document;
        Paragraph ParrafoHoja = new Paragraph();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        List<KardexMd> alldata = new ArrayList<KardexMd>();
        KardexDal rd = new KardexDal();
        alldata = rd.REGselectTodas(anio);
        Float totalCompra =Float.parseFloat("0.00");
        Float totalEfectivo = Float.parseFloat("0.00");
        Float totalCantidad = Float.parseFloat("0.00");

        for (int i = 0; i < alldata.size(); i++) {
            //totalCompra +=Float.parseFloat(alldata.get(i).getPre_comp());
            totalEfectivo += Float.parseFloat(alldata.get(i).getPrecio_ing());
            totalCantidad += Float.parseFloat(alldata.get(i).getStopbarrita());
        }

        if (alldata.isEmpty()) {
            Clients.showNotification("NO TIENE DATOS..!");
        }
        try {

            document = new Document();
            document.setPageSize(PageSize.A4.rotate());
            //document.setPageSize(PageSize.LETTER);
            PdfWriter.getInstance(document, baos);
            System.out.println("Despues del PDFWriter" + anio);

            String dirImagen = desktop.getWebApp().getRealPath("bootstrap") + "/img/reportar.png";
            Image im = Image.getInstance(dirImagen);
            im.setAlignment(Image.ALIGN_RIGHT | Image.TEXTWRAP);
            im.setAbsolutePosition(25, 700);
            im.scalePercent(10);
            ParrafoHoja.add(im);

            ParrafoHoja.add(new Paragraph("              DISTRIBUIDORA"));
            ParrafoHoja.add(new Paragraph("              TODAS LAS FERRETERIAS, S.A"));
            ParrafoHoja.add(new Paragraph("              Kardex de Productos"));
            ParrafoHoja.add(new Paragraph("              Fecha de Reporte.: " + anio));
            ParrafoHoja.add(new Paragraph("              USUARIO.: " + user));

            //System.out.println("CREAR TABLA");
            float anchosFilas[] = {0.2f, 0.8f, 0.3f, 0.4f,0.3f, 0.3f, 0.3f, 0.3f};//Anchos de las filas
            PdfPTable tabla = new PdfPTable(anchosFilas);
            String rotulosColumnas[] = {"COD", "NOMBRE","MARCA", "P.COMPRA","P.VENTA", "SALDO BARRITA", "SALDO CARRIZAL", "SALDO ANGELES"};//Rotulos de las columnas 

            tabla.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell();

            System.out.println("CONSTRUIR TABLA");
            for (int i = 0; i < rotulosColumnas.length; i++) {
                cell = new PdfPCell(new Paragraph(rotulosColumnas[i]));
                tabla.addCell(cell);
            }

            int i = 0;
            for (KardexMd a : alldata) {
                i++;
                cell = new PdfPCell(new Paragraph(a.getCodigo()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getNombre()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getMarca()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getPre_comp()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getPrecio_ing()));
                tabla.addCell(cell);
//                cell = new PdfPCell(new Paragraph(a.getPrecio_sal()));
//                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getStopbarrita()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getStopcarrizal()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getStopangeles()));
                tabla.addCell(cell);
            }

            //SUMATORIA DE COLUMNAS 
            cell = new PdfPCell(new Paragraph("TOTALES"));
            cell.setColspan(2);
            cell.setBorder(0);
            cell.setHorizontalAlignment(1);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(String.valueOf(totalEfectivo)));
            cell.setColspan(1);
            cell.setBorder(0);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(""));
            cell.setColspan(1);
            cell.setBorder(0);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(String.valueOf(totalCantidad)));
            cell.setColspan(1);
            cell.setBorder(0);
            tabla.addCell(cell);
            //FIN DE SUMA DE COLUMNAS

// ParrafoHoja.add(new Paragraph("               Kardex: "));
            ParrafoHoja.add(tabla);
            document.open();
            document.add(ParrafoHoja);
            document.close();

            AMedia amedia = new AMedia("Kardex.PDF", "PDF", "application/file", baos.toByteArray());
            Filedownload.save(amedia);
            baos.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
