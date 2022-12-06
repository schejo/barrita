package ctrl_;

import DAL.ReporteBajasDal;
import MD.ReporteBajasMd;
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

public class ReporteBajasCtrl extends GenericForwardComposer {

    private Datebox FechaI;
    private Datebox fechaF;

    public void doAfterComposer(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }

    public void onClick$btnDescargar(Event e) throws SQLException, ClassNotFoundException {
        String usuario = (String) desktop.getSession().getAttribute("USER");
        GeneraPDF(FechaI.getText(), fechaF.getText(), usuario);

    }

    public void GeneraPDF(String FechaI, String fechaF, String user) throws SQLException, ClassNotFoundException {
        System.out.println("Generar PDF");
        Document document;
        Paragraph ParrafoHoja = new Paragraph();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        List<ReporteBajasMd> alldata = new ArrayList<ReporteBajasMd>();
        ReporteBajasDal rd = new ReporteBajasDal();
        alldata = rd.REGselect(FechaI);

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
            ParrafoHoja.add(new Paragraph("              Ventas del Dia"));
            ParrafoHoja.add(new Paragraph("              Fecha del.: " + FechaI + " al.: " + fechaF));
            ParrafoHoja.add(new Paragraph("              USUARIO.: " + user));

            System.out.println("CREAR TABLA");
            float anchosFilas[] = {0.6f, 0.4f, 0.4f, 0.4f, 0.4f};//Anchos de las filas
            PdfPTable tabla = new PdfPTable(anchosFilas);
            String rotulosColumnas[] = {"NOMBRE", "PRECIO_UNI", "CANTIDAD", "FECHA", "BAJA"};//Rotulos de las columnas
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
            for (ReporteBajasMd a : alldata) {
                i++;
                cell = new PdfPCell(new Paragraph(a.getNombre()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getPrecio()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getCantidad()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getFecha()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getBaja()));
                tabla.addCell(cell);
            }

            ParrafoHoja.add(tabla);
            document.open();
            document.add(ParrafoHoja);
            document.close();

            AMedia amedia = new AMedia("Ventas.PDF", "PDF", "application/file", baos.toByteArray());
            Filedownload.save(amedia);
            baos.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
