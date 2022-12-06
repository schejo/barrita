package ctrl_;


import DAL.ReporteVenceDal;
import MD.ReporteVenceMd;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;

public class ReporteVenceCtrl extends GenericForwardComposer {

    private Datebox fechaG;

    public void doAfterComposer(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }

    public void onClick$btnDescargar(Event e) throws SQLException, ClassNotFoundException, ParseException {
        String usuario = (String) desktop.getSession().getAttribute("USER");
        GeneraPDF(fechaG.getText(),usuario);

    }

    public void GeneraPDF(String anio, String user) throws SQLException, ClassNotFoundException, ParseException {
        System.out.println("Generar PDF");
        Document document;
        Paragraph ParrafoHoja = new Paragraph();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        List<ReporteVenceMd> alldata = new ArrayList<ReporteVenceMd>();
        ReporteVenceDal rd = new ReporteVenceDal();
        alldata = rd.REGselect(anio);

        if (alldata.isEmpty()) {
            Clients.showNotification("NO TIENE DATOS..!");
        }
        try {

            document = new Document();
            document.setPageSize(PageSize.LETTER);
            PdfWriter.getInstance(document, baos);

            String dirImagen = desktop.getWebApp().getRealPath("bootstrap") + "/img/logoF.png";
            Image im = Image.getInstance(dirImagen);
            im.setAlignment(Image.ALIGN_RIGHT | Image.TEXTWRAP);
            im.setAbsolutePosition(25, 700);
            im.scalePercent(10);
            ParrafoHoja.add(im);

            ParrafoHoja.add(new Paragraph("              DISTRIBUIDORA"));
            ParrafoHoja.add(new Paragraph("              LA BARRITA, S.A"));
            ParrafoHoja.add(new Paragraph("              Reporte de Productos Vencidos"));
            ParrafoHoja.add(new Paragraph("              Fecha de Reporte.: "+anio));
            ParrafoHoja.add(new Paragraph("              USUARIO.: "+user));

            System.out.println("CREAR TABLA");
            float anchosFilas[] = {0.6f, 0.4f, 0.4f, 0.4f};//Anchos de las filas
            PdfPTable tabla = new PdfPTable(anchosFilas);
            String rotulosColumnas[] = {"NOMBRE", "PRECIO", "CANTIDAD", "FECHA_VENCE"};//Rotulos de las columnas 

            // Porcentaje que ocupa a lo ancho de la pagina del PDF
            tabla.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell();

            for (int i = 0; i < rotulosColumnas.length; i++) {
                cell = new PdfPCell(new Paragraph(rotulosColumnas[i]));
                tabla.addCell(cell);
            }

            int i = 0;
            for (ReporteVenceMd a : alldata) {
                i++;
                cell = new PdfPCell(new Paragraph(a.getNombre()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getPrecio()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getCantidad()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getVence()));
                tabla.addCell(cell);  

            }
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

