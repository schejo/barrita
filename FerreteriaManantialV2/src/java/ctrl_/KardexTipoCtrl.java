package ctrl_;

import DAL.KardexTipoDal;
import MD.KardexTipoMd;
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

public class KardexTipoCtrl extends GenericForwardComposer {

    private Datebox fechaG;
    private Combobox tipPro;

    List<KardexTipoMd> allKardex = new ArrayList<KardexTipoMd>();

    public void doAfterComposer(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }

    public void onClick$btnDescargar(Event e) throws SQLException, ClassNotFoundException {

        System.out.println("que lleva en tipo CTRL 1.... " + tipPro.getSelectedItem().getValue().toString());
        if (!fechaG.getText().isEmpty()) {
            String usuario = (String) desktop.getSession().getAttribute("USER");
            GeneraPDF(fechaG.getText(), tipPro.getSelectedItem().getValue().toString(), usuario);
            System.out.println("que lleva en tipo CTRL 2.... " + tipPro);
        } else {
            Clients.showNotification("INGRESE UNA FECHA POR FAVOR PARA PODER GENERAR EL PDF");
        }
    }

    public void GeneraPDF(String anio, String tipo, String user) throws SQLException, ClassNotFoundException {
        System.out.println("Generar PDF");
        Document document;
        Paragraph ParrafoHoja = new Paragraph();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        List<KardexTipoMd> alldata = new ArrayList<KardexTipoMd>();
        KardexTipoDal rd = new KardexTipoDal();
        alldata = rd.REGselect(anio, tipo);

        Float totalPrecio = Float.parseFloat("0.00");
        Float totalSaldo = Float.parseFloat("0.00");

        for (int i = 0; i < alldata.size(); i++) {
            totalPrecio += Float.parseFloat(alldata.get(i).getPrecio_ing());
            totalSaldo += Float.parseFloat(alldata.get(i).getExistencia());
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
            ParrafoHoja.add(new Paragraph("              Kardex de Productos"));
            ParrafoHoja.add(new Paragraph("              Fecha de Reporte.: " + anio));
            ParrafoHoja.add(new Paragraph("              USUARIO.: " + user));

            //System.out.println("CREAR TABLA");
            float anchosFilas[] = {0.2f, 0.8f, 0.3f, 0.5f, 0.2f};//Anchos de las filas
            PdfPTable tabla = new PdfPTable(anchosFilas);
            String rotulosColumnas[] = {"CODIGO", "NOMBRE", "PRECIO",
                "TIPO", "SALDO"};//Rotulos de las columnas 
            // Porcentaje que ocupa a lo ancho de la pagina del PDF
            tabla.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell();

            //System.out.println("CONSTRUIR TABLA");
            for (int i = 0; i < rotulosColumnas.length; i++) {
                cell = new PdfPCell(new Paragraph(rotulosColumnas[i]));
                tabla.addCell(cell);
            }

            int i = 0;
            for (KardexTipoMd a : alldata) {
                i++;
                cell = new PdfPCell(new Paragraph(a.getCodigo()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getNombre()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getPrecio_ing()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getTipo()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getExistencia()));
                tabla.addCell(cell);

            }

            //SUMATORIA DE COLUMNAS 
            cell = new PdfPCell(new Paragraph("TOTALES"));
            cell.setColspan(2);
            cell.setBorder(0);
            cell.setHorizontalAlignment(1);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(String.valueOf(totalPrecio)));
            cell.setColspan(1);
            cell.setBorder(0);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(""));
            cell.setColspan(1);
            cell.setBorder(0);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(String.valueOf(totalSaldo)));
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
