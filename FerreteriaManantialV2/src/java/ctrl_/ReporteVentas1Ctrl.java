package ctrl_;

import DAL.ReporteVentas1Dal;
import MD.ReporteVentas1Md;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;

public class ReporteVentas1Ctrl extends GenericForwardComposer {

    private Datebox fechaG;

    public void doAfterComposer(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }

    public void onClick$btnDescargar(Event e) throws SQLException, ClassNotFoundException {
        String usuario = (String) desktop.getSession().getAttribute("USER");
        if (!fechaG.getText().isEmpty()) {
            GeneraPDF(fechaG.getText(), usuario);
        } else {
            Clients.showNotification("INGRESE UNA FECHA POR FAVOR <BR> PARA PODER GENERAR EL PDF");
        }
    }

    public void GeneraPDF(String anio, String user) throws SQLException, ClassNotFoundException {
        System.out.println("Generar PDF");
        Document document;
        Paragraph ParrafoHoja = new Paragraph();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        List<ReporteVentas1Md> alldata = new ArrayList<ReporteVentas1Md>();
        ReporteVentas1Dal rd = new ReporteVentas1Dal();
        alldata = rd.REGselect(anio,session.getAttribute("SUCURSAL").toString());

        Float totalEfectivo = Float.parseFloat("0.00");
        Float totalCantidad = Float.parseFloat("0.00");
        DecimalFormat formato = new DecimalFormat("Q#,###.###");

        for (int i = 0; i < alldata.size(); i++) {
//            totalEfectivo += Float.parseFloat(alldata.get(i).getPrecio_ing());
            totalCantidad += Float.parseFloat(alldata.get(i).getTotal());
        }

        if (alldata.isEmpty()) {
            Clients.showNotification("NO TIENE DATOS..!");
        }
        try {

            document = new Document();
            document.setPageSize(PageSize.A4.rotate());
            PdfWriter.getInstance(document, baos);
            System.out.println("Despues del PDFWriter" + anio);

            String dirImagen = desktop.getWebApp().getRealPath("bootstrap") + "/img/reportar.png";
            Image im = Image.getInstance(dirImagen);
            im.setAlignment(Image.ALIGN_RIGHT | Image.TEXTWRAP);
            im.setAbsolutePosition(25, 700);
            im.scalePercent(10);
            ParrafoHoja.add(im);

            ParrafoHoja.add(new Paragraph("              DISTRIBUIDORA"));
             switch (session.getAttribute("SUCURSAL").toString()) {
                case "1":
                     ParrafoHoja.add(new Paragraph("              LA BARRITA, S.A"));
                    break;
                case "2":
                    ParrafoHoja.add(new Paragraph("              CARRIZAL, S.A"));
                    break;
                case "3":
                    ParrafoHoja.add(new Paragraph("              LOS ANGELES, S.A"));
                    break;

            }
            ParrafoHoja.add(new Paragraph("              Ventas del DIA"));
            ParrafoHoja.add(new Paragraph("              Fecha de Reporte.: " + anio));
            ParrafoHoja.add(new Paragraph("              USUARIO.: " + user));

            //System.out.println("CREAR TABLA");
            float anchosFilas[] = {0.1f, 0.2f, 0.6f, 0.7f, 0.3f, 0.2f, 0.2f, 0.3f};//Anchos de las filas
            PdfPTable tabla = new PdfPTable(anchosFilas);
            String rotulosColumnas[] = {"No.", "FACT.", "AUTORIZACION", "DESCRIPCION", "CANTIDAD", "PRECIO", "TOTAL", "FECHA"};//Rotulos de las columnas 

            tabla.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell();

            System.out.println("CONSTRUIR TABLA");
            for (int i = 0; i < rotulosColumnas.length; i++) {
                cell = new PdfPCell(new Paragraph(rotulosColumnas[i]));
                tabla.addCell(cell);
            }

            int i = 0;
            for (ReporteVentas1Md a : alldata) {
                i++;
                cell = new PdfPCell(new Paragraph(a.getCorrelativo()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getFactura()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getAutorizacion()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getDescripcion()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getCantidad_ing()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getPrecio_ing()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getTotal()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getFecha()));
                tabla.addCell(cell);
            }

            //SUMATORIA DE COLUMNAS 
            cell = new PdfPCell(new Paragraph("VENTA TOTAL DEL DIA"));
            cell.setColspan(3);
            cell.setBorder(0);
            cell.setHorizontalAlignment(1);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(""));
            cell.setColspan(4);
            cell.setBorder(0);
            tabla.addCell(cell);

//            cell = new PdfPCell(new Paragraph(String.valueOf(totalEfectivo)));
//            cell.setColspan(1);
//            cell.setBorder(0);
//            tabla.addCell(cell);
            String valorFormateado = formato.format(totalCantidad);

            cell = new PdfPCell(new Paragraph(String.valueOf(valorFormateado)));
            cell.setColspan(1);
            cell.setBorder(0);
            tabla.addCell(cell);
            //FIN DE SUMA DE COLUMNAS

// ParrafoHoja.add(new Paragraph("               Kardex: "));
            ParrafoHoja.add(tabla);
            document.open();
            document.add(ParrafoHoja);
            document.close();

            AMedia amedia = new AMedia("ReporteVentasDia.PDF", "PDF", "application/file", baos.toByteArray());
            Filedownload.save(amedia);
            baos.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
