package ctrl_;

import DAL.VentaPagoDal;
import MD.VentaPagoMd;
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
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;

public class VentaPagoCtrl extends GenericForwardComposer {

    private Datebox fechaG;
    private Datebox fechaFin;
    private Combobox venPag;
    Session session = Sessions.getCurrent();

    List<VentaPagoMd> allVentaPago = new ArrayList<VentaPagoMd>();

    public void doAfterComposer(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }

    public void onClick$btnDescargar(Event e) throws SQLException, ClassNotFoundException {

        System.out.println("que lleva en tipo CTRL 1.... " + venPag.getSelectedItem().getValue().toString());
        if (!fechaG.getText().isEmpty()) {
            String usuario = (String) desktop.getSession().getAttribute("USER");
            GeneraPDF(fechaG.getText(), venPag.getSelectedItem().getValue().toString(), usuario, fechaFin.getText(), session.getAttribute("SUCURSAL").toString());
            System.out.println("que lleva en tipo CTRL 2.... " + venPag);
        } else {
            Clients.showNotification("INGRESE UNA FECHA POR FAVOR PARA PODER GENERAR EL PDF");
        }
    }

    public void GeneraPDF(String inicio, String pago, String user, String fin, String sucursal) throws SQLException, ClassNotFoundException {
        System.out.println("Generar PDF");
        Document document;
        Paragraph ParrafoHoja = new Paragraph();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        List<VentaPagoMd> alldata = new ArrayList<VentaPagoMd>();
        VentaPagoDal rd = new VentaPagoDal();
        alldata = rd.REGselect(inicio, pago, fin, sucursal);

        Float totalPrecio = Float.parseFloat("0.00");
        Float totalCantidad = Float.parseFloat("0.00");
        Float totalDescuento = Float.parseFloat("0.00");
        Float totalGanancia = Float.parseFloat("0.00");
        DecimalFormat formato = new DecimalFormat("Q#,###.###");


        for (int i = 0; i < alldata.size(); i++) {
            totalPrecio += Float.parseFloat(alldata.get(i).getTotal());
//            totalCantidad += Float.parseFloat(alldata.get(i).getCantidad());
//            totalDescuento += Float.parseFloat(alldata.get(i).getDescuento());
//            totalGanancia += Float.parseFloat(alldata.get(i).getGanancia());

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

            ParrafoHoja.add(new Paragraph("              FERRETERIA"));
            ParrafoHoja.add(new Paragraph("              Sistema Control de Inventario y Facturacion"));
            ParrafoHoja.add(new Paragraph("              Ventas por Tipo Pago"));
            ParrafoHoja.add(new Paragraph("              Fecha de Reporte.: " + inicio + " al " + fin));
            ParrafoHoja.add(new Paragraph("              USUARIO.: " + user + " SUCURSAL " + alldata.get(0).getNombre_sucursal()));

            //System.out.println("CREAR TABLA");
            float anchosFilas[] = {0.6f, 0.2f, 0.2f, 0.2f, 0.2f, 0.2f};//Anchos de las filas
            PdfPTable tabla = new PdfPTable(anchosFilas);
            String rotulosColumnas[] = {"AUTORIZACION", "CLIENTE", "FECHA",
                "TOTAL", "ESTADO", "PAGO"};//Rotulos de las columnas 
            // Porcentaje que ocupa a lo ancho de la pagina del PDF
            tabla.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell();

            //System.out.println("CONSTRUIR TABLA");
            for (int i = 0; i < rotulosColumnas.length; i++) {
                cell = new PdfPCell(new Paragraph(rotulosColumnas[i]));
                tabla.addCell(cell);
            }

            int i = 0;
            for (VentaPagoMd a : alldata) {
                i++;
                cell = new PdfPCell(new Paragraph(a.getAutorizacion()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getCliente()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getFecha()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getTotal()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getEstado()));
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph(a.getPago()));
                tabla.addCell(cell);

            }

            //SUMATORIA DE COLUMNAS 
            cell = new PdfPCell(new Paragraph("TOTALES"));
            cell.setColspan(3);
            cell.setBorder(0);
            cell.setHorizontalAlignment(1);
            tabla.addCell(cell);
            
            String valorFormateado = formato.format(totalPrecio);

            cell = new PdfPCell(new Paragraph(valorFormateado));
            cell.setColspan(1);
            cell.setBorder(0);
            cell.setHorizontalAlignment(1);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph(""));
            cell.setColspan(2);
            cell.setBorder(0);
            tabla.addCell(cell);
//            
//            cell = new PdfPCell(new Paragraph(String.valueOf(totalCantidad)));
//            cell.setColspan(1);
//            cell.setBorder(0);
//            cell.setHorizontalAlignment(1);
//            tabla.addCell(cell);
//
//            cell = new PdfPCell(new Paragraph(String.valueOf(totalDescuento)));
//            cell.setColspan(1);
//            cell.setBorder(0);
//            cell.setHorizontalAlignment(1);
//            tabla.addCell(cell);
//
//            cell = newspan(1);
//            cell.setBorder(0);
//            cell.setHorizontalAlignment(1);
//            tabla.addCell(cell); PdfPCell(new Paragraph(String.valueOf(totalGanancia)));
//            cell.setColspan(1);
//            cell.setBorder(0);
//            cell.setHorizontalAlignment(1);
//            tabla.addCell(cell);
            //FIN DE SUMA DE COLUMNAS

            // ParrafoHoja.add(new Paragraph("               Kardex: "));
            ParrafoHoja.add(tabla);
            document.open();
            document.add(ParrafoHoja);
            document.close();

            AMedia amedia = new AMedia("Venta Tipo Pago.PDF", "PDF", "application/file", baos.toByteArray());
            Filedownload.save(amedia);
            baos.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
