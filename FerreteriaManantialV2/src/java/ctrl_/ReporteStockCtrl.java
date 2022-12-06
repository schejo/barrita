package ctrl_;

import DAL.ReporteStockDal;
import MD.ReporteStockMd;
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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;

public class ReporteStockCtrl extends GenericForwardComposer {

    private Datebox anio;
    private Datebox fechaF;

    public void doAfterComposer(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }

    public void onClick$btnDescargar(Event e) throws SQLException, ClassNotFoundException {
        String usuario = (String) desktop.getSession().getAttribute("USER");
        GeneraPDF(anio.getText(), fechaF.getText(), usuario);

    }

    public void GeneraPDF(String anio, String fechaF, String user) throws SQLException, ClassNotFoundException {
        System.out.println("Generar PDF");
        Document document;
        Paragraph ParrafoHoja = new Paragraph();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        List<ReporteStockMd> alldata = new ArrayList<ReporteStockMd>();
        ReporteStockDal rd = new ReporteStockDal();
        alldata = rd.REGselect(anio, fechaF);

        Float precio = Float.parseFloat("0.00");
        Float stock = Float.parseFloat("0.00");

        for (int i = 0; i < alldata.size(); i++) {
            precio += Float.parseFloat(alldata.get(i).getPrecio());
            stock += Float.parseFloat(alldata.get(i).getStock());

        }

        if (alldata.isEmpty()) {
            Clients.showNotification("NO TIENE DATOS..!");
        }
        try {

            document = new Document();
            document.setPageSize(PageSize.LETTER);
            PdfWriter.getInstance(document, baos);

            String dirImagen = desktop.getWebApp().getRealPath("bootstrap") + "/img/reportar.png";
            Image im = Image.getInstance(dirImagen);
            im.setAlignment(Image.ALIGN_RIGHT | Image.TEXTWRAP);
            im.setAbsolutePosition(25, 700);
            im.scalePercent(10);
            ParrafoHoja.add(im);

            ParrafoHoja.add(new Paragraph("              DISTRIBUIDORA"));
            ParrafoHoja.add(new Paragraph("              LA BARRITA, S.A"));
            ParrafoHoja.add(new Paragraph("              Reporte Stock de Productos menores de 5"));
            ParrafoHoja.add(new Paragraph("              Fecha del.: " + anio + " al.: " + fechaF));
            ParrafoHoja.add(new Paragraph("              USUARIO.: " + user));

            System.out.println("CREAR TABLA");
            float anchosFilas[] = {0.2f, 0.5f, 0.2f, 0.2f,/* 0.5f,*/};//Anchos de las filas
            PdfPTable tabla = new PdfPTable(anchosFilas);
            String rotulosColumnas[] = {"CODIGO", "NOMBRE", "PRECIO", "STOCK"/*, "UBICACION"*/};//Rotulos de las columnas
            System.out.println("SE CREARON ENCABZADOS");
            // Porcentaje que ocupa a lo ancho de la pagina del PDF
            tabla.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell();

            System.out.println("CONSTRUIR TABLA");
            for (int i = 0; i < rotulosColumnas.length; i++) {
                cell = new PdfPCell(new Paragraph(rotulosColumnas[i]));
                tabla.addCell(cell);
            }

            int i = 0;
            for (ReporteStockMd a : alldata) {
                i++;
                cell = new PdfPCell(new Paragraph(a.getCodigo()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getNombre()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getPrecio()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getStock()));
                tabla.addCell(cell);
//                cell = new PdfPCell(new Paragraph(a.getUbicacion()));
//                tabla.addCell(cell);
            }
            //SUMATORIA DE COLUMNAS 
            cell = new PdfPCell(new Paragraph("TOTALES"));
            cell.setColspan(1);
            cell.setBorder(0);
            cell.setHorizontalAlignment(1);
            tabla.addCell(cell);
            
            cell = new PdfPCell(new Paragraph(""));
            cell.setColspan(1);
            cell.setBorder(0);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(String.valueOf(precio)));
            cell.setColspan(1);
            cell.setBorder(0);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(String.valueOf(stock)));
            cell.setColspan(1);
            cell.setBorder(0);
            tabla.addCell(cell);


            //FIN DE SUMA DE COLUMNAS
            ParrafoHoja.add(tabla);
            document.open();
            document.add(ParrafoHoja);
            document.close();

            AMedia amedia = new AMedia("Reporte de Stock.PDF", "PDF", "application/file", baos.toByteArray());
            Filedownload.save(amedia);
            baos.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
