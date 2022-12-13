/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctrl_;

import DAL.CatalogoDal;
import DAL.ClientesDal;
import DAL.ProductosDal;
import DAL.UsuarioDal;
import DAL.VentasValesDal;
import MD.CatalogosMd;
import MD.ClientesMd;
import MD.DetalleFacturaMd;
import MD.FacturaMd;
import MD.ProductosMd;
import MD.UsuariosMd;
import Utilitarios.Commons;
import Utilitarios.Util;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author HP 15
 */
public class CotizacionCtrl extends Commons {

    UsuarioDal user = new UsuarioDal();
    private Textbox txtEmpleadoId;
    private Textbox txtEmpleadoNombre;
    private Textbox txtEmpleadoUsuario;
    private Textbox txtFacturaFecha;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    Util util = new Util();
    private Combobox cbxDetalleBusqueda;
    private Textbox txtClienteNit;
    ClientesDal cliente = new ClientesDal();

    private Textbox txtClienteNombre;
    private Textbox txtClienteId;
    private Textbox txtClienteDireccion;
    private Textbox txtClienteTelefono;
    private Textbox txtClienteAlta;
    private Textbox txtProductoId;
    private Textbox txtProductoTipo;
    private Textbox txtProductoDescripcion;
    private Textbox txtProductoPrecio;
    private Textbox txtProductoDescuento;
    private Textbox txtProductoStock;
    private Intbox txtProductoCantidad;
    public List<DetalleFacturaMd> datos = new ArrayList<DetalleFacturaMd>();
    DecimalFormat formato = new DecimalFormat("#.00");
    private int banderaGrid = 0;
    private Rows rows;
    private Row row;
    Float subtotal = Float.parseFloat("0.0"), descuentos = Float.parseFloat("0.0"), total = Float.parseFloat("0.0");
    private Textbox txtSubtotal;
    private Textbox txtDescuentos;
    private Textbox txtTotal;
    VentasValesDal ven = new VentasValesDal();
    List<CatalogosMd> lista = new ArrayList<CatalogosMd>();
    CatalogoDal ctd = new CatalogoDal();
    File f;

    public void onClick$btnGuardar(Event evt) throws SQLException, ParseException {

        PDF(ven.BuscarEncabezadoCotiza(txtClienteId.getText(), txtEmpleadoId.getText()));

    }
      public void onClick$btnLimpiar(Event e) {
        Limpiartodo();
        
    }
    private void Limpiartodo() {
        cbxDetalleBusqueda.setText("");
        txtProductoId.setText("");
        txtProductoTipo.setText("");
        txtProductoDescripcion.setText("");
        txtProductoPrecio.setText("");
        txtProductoDescuento.setText("");
        txtProductoStock.setText("");
        txtProductoCantidad.setText("");
        txtClienteNit.setText("");
        
        txtClienteNit.setText("");
        txtClienteNombre.setText("");
        txtClienteId.setText("");
        txtClienteDireccion.setText("");
        txtClienteTelefono.setText("");
        txtClienteAlta.setText("");
        
        txtSubtotal.setText("");
        txtDescuentos.setText("");
        txtTotal.setText("");      
         rows.getChildren().clear();   
         
         datos.removeAll(datos);
    }  
    
        public void onClick$btnClienteAct(Event e) {
//        EventQueues.lookup("myEventQueue", EventQueues.DESKTOP, true)
//                .publish(new Event("onChangeNickname", null, lista));

        //INVOCAR MODAL
        Window window = (Window) Executions.createComponents(
                "/Vistas/RegisClientes.zul", null, null);
        window.doModal();
    }
            public void onClick$btnBusca1(Event e) {
                 txtProductoTipo.setValue("");
        txtProductoDescripcion.setValue("");
        txtProductoPrecio.setValue("");
        txtProductoDescuento.setValue("");
        txtProductoStock.setValue("");
        EventQueues.lookup("myEventQueue", EventQueues.DESKTOP, true)
                .publish(new Event("onChangeNickname", null, lista));

        //INVOCAR MODAL
        Window window = (Window) Executions.createComponents(
                "/Vistas/BuscaPro.zul", null, null);
        window.doModal();
    }

    public void PDF(FacturaMd enc) throws SQLException {

        String buque = "";
        DecimalFormat formato = new DecimalFormat("#.00");
        Paragraph ParrafoHoja = new Paragraph();
        // List<DetalleFacturaMd> lista = ven.BuscaDetallesFactura();
        try {
            com.itextpdf.text.Document detalle = new com.itextpdf.text.Document(PageSize.LETTER.rotate());
            ByteArrayOutputStream badetalle = new ByteArrayOutputStream();
            PdfWriter escritura;
            // Se crea el OutputStream para el fichero donde queremos dejar el pdf.
            FileOutputStream ficheroPdf = new FileOutputStream(desktop.getWebApp().getRealPath("rpt") + File.separator + "vale.pdf");

// Se asocia el documento al OutputStream y se indica que el espaciado entre
// lineas sera de 20. Esta llamada debe hacerse antes de abrir el documento
            PdfWriter.getInstance(detalle, ficheroPdf).setInitialLeading(20);
//            String dirImagen = desktop.getWebApp().getRealPath("bootstrap") + "/img/reportar.png";
//            Image im = Image.getInstance(dirImagen);
//            im.setAlignment(Image.ALIGN_RIGHT | Image.TEXTWRAP);
//            im.setAbsolutePosition(25, 700);
//            im.scalePercent(10);
//            ParrafoHoja.add(im);
            //escritura = PdfWriter.getInstance(detalle, badetalle);
            detalle.open();

            Paragraph predetalle = new Paragraph();
            predetalle.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////// ENCABEZADO DE BOLETA ///////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            PdfPTable table2 = new PdfPTable(10);   //tenia 10,90
            table2.setWidthPercentage(100);

            PdfPCell cell2;
            
            String direccion = InetAddress.getLocalHost().getHostAddress();
            Image imagen = Image.getInstance("http://" + direccion + ":8080/FerreteriaManantial/bootstrap/img/logo_1.png");
            imagen.scalePercent(70f);

            cell2 = new PdfPCell(imagen);
            cell2.setColspan(2);
            cell2.setRowspan(5);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);

            table2.addCell(cell2);

            /*cell2 = new PdfPCell(new Phrase("Servicios de ventas Facturadas", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);*/
            cell2 = new PdfPCell(new Phrase("\"Distribuidora La Barrita, S.A.\"", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD)));
            cell2.setColspan(8);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(8);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("NIT: 11237058-6", FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD)));
            cell2.setColspan(8);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(8);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);
            cell2 = new PdfPCell(new Phrase("Calle Principal Parcela 1,Lote 1, Parcelamiento Los Angeles ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD)));
            cell2.setColspan(8);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("COTIZACION DE MATERIAL ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////// DATOS FACTURA FACTURA/////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("ATENDIO: " + enc.getFacturaEmpleadoId() + "  " + enc.getFacturaEmpleadoNombre(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("NIT: " + enc.getFacturaClienteNit(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("CLIENTE: " + "  " + enc.getFacturaClienteNombre(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("DIRECCION: " + txtClienteDireccion.getText(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());

            cell2 = new PdfPCell(new Phrase("FECHA IMP: " + timeStamp, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////// DATOS DETALLE FACTURA/////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("___________________________________________________________________________________________________________"));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("CODIGO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(0);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("PRODUCTO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(6);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("PRECIO UNITARIO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(1);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("TOTAL", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(3);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("___________________________________________________________________________________________________________"));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);
            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            for (int i = 0; i < datos.size(); i++) {
                if (datos.get(i).getDetProductoId().equals(null)) {

                } else {
                    cell2 = new PdfPCell(new Phrase(datos.get(i).getDetProductoId() + " " + datos.get(i).getDetProductoDescripcion() + " " + " ",
                            FontFactory.getFont(FontFactory.TIMES_ROMAN, 13)));
                    cell2.setColspan(9);
                    cell2.setBorder(Rectangle.NO_BORDER);
                    table2.addCell(cell2);

                    cell2 = new PdfPCell(new Phrase(""));
                    cell2.setColspan(1);
                    cell2.setBorder(Rectangle.NO_BORDER);
                    table2.addCell(cell2);

                    cell2 = new PdfPCell(new Phrase(datos.get(i).getProductoCantidad() + " x Q." + datos.get(i).getDetProductoPrecioVenta(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 13)));
                    cell2.setColspan(9);
                    cell2.setBorder(Rectangle.NO_BORDER);
                    cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    table2.addCell(cell2);

                    cell2 = new PdfPCell(new Phrase(String.valueOf(formato.format(Float.parseFloat(datos.get(i).getDetProductoPrecioVenta()) * Float.parseFloat(datos.get(i).getProductoCantidad()))), FontFactory.getFont(FontFactory.TIMES_ROMAN, 13)));
                    cell2.setColspan(1);
                    cell2.setBorder(Rectangle.NO_BORDER);
                    cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                    table2.addCell(cell2);
                }
            }

            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("___________________________________________________________________________________________________________"));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////// DATOS TOTALES FACTURA/////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(3);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            float totalcoti = ActualizaTotales();
            float totalDescu=ActualizaDescuento();
            float subtotal=ActualizaSubtotal();
              cell2 = new PdfPCell(new Phrase("SUBTOTAL: Q." + subtotal, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12)));
            cell2.setColspan(7);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(3);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("(-) DESCUENTO: Q." + totalDescu, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12)));
            cell2.setColspan(7);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(3);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("TOTAL: Q." + totalcoti, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12)));
            cell2.setColspan(7);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("ESTA COTIZACION TIENE UNA VIGENCIA DE 15 DIAS!!!", FontFactory.getFont(FontFactory.TIMES_ROMAN, 15)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("Es un placer el Servirle, Vuelva Pronto!!!", FontFactory.getFont(FontFactory.TIMES_ROMAN, 15)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("___________________________________________________________________________________________________________"));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            predetalle.add(table2);

            
                    
            detalle.add(ParrafoHoja);
            detalle.add(predetalle);

            detalle.close();
            f = new File(desktop.getWebApp().getRealPath("rpt") + File.separator + "vale.pdf");
            //f= new File(desktop.getWebApp().getRealPath("rpt")+ File.separator+ "cotizacion.pdf");

//f = new File(desktop.getWebApp().getRealPath("rpt") + File.separator + "Vale.pdf");
            //Messagebox.show("CREA ARCHIVO");
            byte[] buffer = new byte[(int) f.length()];

            FileInputStream fs = new FileInputStream(f);

            fs.read(buffer);
            fs.close();

            //ByteArrayInputStream is = new ByteArrayInputStream(buffer);
            InputStream is = new ByteArrayInputStream(buffer);
            Filedownload.save(is, "application/pdf", "cotizacion.pdf");

//            AMedia amedia = new AMedia("archivo.pdf", "pdf", "application/pdf", is);
//            is.close();
//            // f.delete();
//
//            desktop.setAttribute("reporte", amedia);
//            Window w = (Window) execution.createComponents("Vistas/Rpt_Boleta.zul", ventana, null);
//            w.setMaximized(true);
//            w.doHighlighted();
            // Creamos un PDDocument con el arreglo de entrada que creamos        
//////////////////////////            PDDocument document = PDDocument.load(is);
//////////////////////////            PrinterJob job = PrinterJob.getPrinterJob();
//////////////////////////
//////////////////////////            String parm = Executions.getCurrent().getRemoteAddr();
//////////////////////////
//////////////////////////            PrintService myPrintService = this.findPrintService("POS");
//////////////////////////            //PrintService myPrintService = this.findPrintService("\\\\"+parm+"\\EPSON L3110 Series");
//////////////////////////
//////////////////////////            //   Messagebox.show(parm);
//////////////////////////            job.setPageable(new PDFPageable(document));
//////////////////////////
//////////////////////////            job.setPrintService(myPrintService);
//////////////////////////
//////////////////////////            job.print();
        } catch (Exception ex) {
            // ex.getMessage();

        }
    }
        public void onChange$txtProductoId(Event e) throws SQLException{
        onOK$txtProductoId(e);
    }

    public void onOK$txtProductoId(Event evt) throws SQLException {
        ProductosDal pro = new ProductosDal();
        ProductosMd proMd = pro.BuscarProducto(txtProductoId.getText());

        this.BuscaItem(txtProductoId.getText(), cbxDetalleBusqueda);

        txtProductoId.setText(proMd.getCodigo());
        txtProductoTipo.setText(proMd.getTipo_servicio());
        txtProductoDescripcion.setText(proMd.getDescripcion());
        txtProductoPrecio.setText(proMd.getPrecio_venta());
        txtProductoDescuento.setText(proMd.getDescuento());
        txtProductoStock.setText(proMd.getStock());

        //cbxDetalleBusqueda.getItems().clear();
    }
    public void BuscaItem(String letra, Combobox cb) {
        for (int i = 0; i < cb.getItemCount(); i++) {
            if (letra.equals(cb.getItemAtIndex(i).getValue().toString())) {
                cb.setSelectedIndex(i);
                break;
            }
        }
    }

    public void onClick$btnDetalleAgregar(Event evt) throws SQLException, ParseException {
        if (!txtProductoId.getText().equals("")) {
            if (txtProductoCantidad.getValue() != null) {
                if (txtProductoCantidad.getValue() != 0) {
                    if (txtProductoCantidad.getValue() > 0) {

                        if (!ProductoAgregado(txtProductoId.getText())) {
                            DetalleFacturaMd detMod = new DetalleFacturaMd();

//
                            detMod.setDetProductoId(txtProductoId.getText());
                            detMod.setDetProductoDescripcion(txtProductoDescripcion.getText());
                            detMod.setProductoCantidad(String.valueOf(txtProductoCantidad.getValue()));
                            detMod.setDetProductoPrecioVenta(txtProductoPrecio.getText());
                            detMod.setProductoDescuento(txtProductoDescuento.getText());

                            Float PrecioReal = (Float.parseFloat(txtProductoPrecio.getText()) - Float.parseFloat(txtProductoDescuento.getText()));
                            Float SubtotalDetalle = PrecioReal * Float.parseFloat(String.valueOf(txtProductoCantidad.getValue()));
                            detMod.setSubtotal(String.valueOf(formato.format(SubtotalDetalle)));
                            detMod.setDetProductoTipo(txtProductoTipo.getText());
                            detMod.setProductoStock(txtProductoStock.getText());
                            datos.add(detMod);
                            LimpiarProducto();
                        } else {
                            Messagebox.show("EL PRODUCTO YA FUE AGREGADO A LA FACTURA", "Informacion", Messagebox.OK, Messagebox.ERROR);
                        }

                    } else {
                        Messagebox.show("NO DEBE INGRESAR UNA CANTIDAD NEGATIVA", "Informacion", Messagebox.OK, Messagebox.ERROR);
                    }
                } else {
                    Messagebox.show("DEBE AGREGAR UNA CANTIDAD DIFERENTE DE 0", "Informacion", Messagebox.OK, Messagebox.ERROR);
                }
            } else {
                Messagebox.show("DEBE AGREGAR LA CANTIDAD DE PRODUCTO QUE DESEA ADQUIRIR", "Informacion", Messagebox.OK, Messagebox.ERROR);
            }
        } else {
            Messagebox.show("DEBE SELECCIONAR UN PRODUCTO PARA AGREGARLO", "Informacion", Messagebox.OK, Messagebox.ERROR);
        }
        ActualizaTotales();
        LlenarGrid();
    }

    public void ValoresLabel(Label label, String valor, String clase) {
        label.setValue(valor);
        label.setClass(clase);
    }

    public void ValoresIntbox(Intbox text, String valor, String clase, boolean read) {
        text.setValue(Integer.parseInt(valor));
        text.setReadonly(read);
        text.setWidth("100%");
        text.setClass(clase);
    }

    public void CreateButton(Button button, String clase, String icon, String style, String label, boolean visible) {
        button.setClass(clase);
        button.setIconSclass(icon);
        button.setStyle(style);
        button.setVisible(visible);
        button.setLabel(label);
    }

    public void ValoresDoublebox(Doublebox text, String valor, String clase, boolean read) {
        text.setValue(Float.parseFloat(valor));
        text.setReadonly(read);
        text.setFormat("###0.##");
        text.setLocale(Locale.US);
        text.setWidth("100%");
        text.setClass(clase);
    }

    public void LlenarGrid() throws ParseException {

        if (banderaGrid == 1) {
            row.getChildren().clear();
            rows.getChildren().clear();
            banderaGrid = 0;
        }
        int i = 1;
        for (DetalleFacturaMd mov : datos) {
            banderaGrid = 1;
            Label No = new Label();
            ValoresLabel(No, String.valueOf(i), "");
            Label Codigo = new Label();
            ValoresLabel(Codigo, mov.getDetProductoId(), "");
            Label Descripcion = new Label();
            ValoresLabel(Descripcion, mov.getDetProductoDescripcion(), "");
            Label Stock = new Label();
            ValoresLabel(Stock, mov.getProductoStock(), "");
            Intbox Cantidad = new Intbox();
            ValoresIntbox(Cantidad, mov.getProductoCantidad(), "", false);
            Label PrecioUnidad = new Label();
            ValoresLabel(PrecioUnidad, mov.getDetProductoPrecioVenta(), "");
            Doublebox Descuento = new Doublebox();
            ValoresDoublebox(Descuento, mov.getProductoDescuento(), "", false);
            Label Subtotal = new Label();
            ValoresLabel(Subtotal, mov.getSubtotal(), "");

            Div acciones = new Div();
            acciones.setClass("text-center");

            Button Eliminar = new Button();
            CreateButton(Eliminar, "btn btn-primary btn-md", "z-icon-print", "margin-left:3px;", "", true);
            acciones.appendChild(Eliminar);

            row = new Row();
            row.setStyle("border-style:solid;border-width:1px");
            row.appendChild(No);
            row.appendChild(Codigo);
            row.appendChild(Descripcion);
            row.appendChild(Stock);
            row.appendChild(Cantidad);
            row.appendChild(PrecioUnidad);
            row.appendChild(Descuento);
            row.appendChild(Subtotal);
            row.appendChild(acciones);

            row.setParent(rows);
            row.setValue(mov);
            Eliminar.addEventListener("onClick", EliminarProducto);
            Cantidad.addEventListener("onChange", ModificarCantidad);
            Descuento.addEventListener("onChange", ModificarDescuento);
            i++;
        }
    }
    EventListener EliminarProducto = new EventListener<Event>() {
        @Override
        public void onEvent(Event event) throws SQLException, IOException, ParseException {
            DetalleFacturaMd modelo = new DetalleFacturaMd();

            Button button = (Button) event.getTarget();
            Div div = (Div) button.getParent();
            Row row = (Row) div.getParent();

            modelo = row.getValue();

            for (int i = 0; i < datos.size(); i++) {
                if (modelo.getDetProductoId().equals(datos.get(i).getDetProductoId())) {
                    datos.remove(i);
                }
            }
            ActualizaTotales();

            LlenarGrid();
        }
    };

    EventListener ModificarCantidad = new EventListener<Event>() {
        @Override
        public void onEvent(Event event) throws SQLException, IOException, ParseException {
            DetalleFacturaMd modelo = new DetalleFacturaMd();

            Intbox text = (Intbox) event.getTarget();
            Row row = (Row) text.getParent();

            modelo = row.getValue();

            if (text.getValue() != null) {
                if (text.getValue() != 0) {
                    if (text.getValue() > 0) {
                        if (Integer.parseInt(text.getText()) <= Integer.parseInt(modelo.getProductoStock())) {
                            for (int i = 0; i < datos.size(); i++) {
                                if (modelo.getDetProductoId().equals(datos.get(i).getDetProductoId())) {
                                    datos.get(i).setProductoCantidad(text.getValue().toString());
                                    Float PrecioReal = (Float.parseFloat(datos.get(i).getDetProductoPrecioVenta()) - Float.parseFloat(datos.get(i).getProductoDescuento()));
                                    Float SubtotalDetalle = PrecioReal * Float.parseFloat(String.valueOf(datos.get(i).getProductoCantidad()));

                                    datos.get(i).setSubtotal(String.valueOf(formato.format(SubtotalDetalle)));
                                }
                            }

                        } else {
                            Messagebox.show("LA EXISTENCIA ES MENOR A LA CANTIDAD INGRESADA", "Informacion", Messagebox.OK, Messagebox.ERROR);
                        }
                    } else {
                        Messagebox.show("NO DEBE INGRESAR UNA CANTIDAD NEGATIVA", "Informacion", Messagebox.OK, Messagebox.ERROR);
                    }
                } else {
                    Messagebox.show("DEBE AGREGAR UNA CANTIDAD DIFERENTE DE 0", "Informacion", Messagebox.OK, Messagebox.ERROR);
                }
            } else {
                Messagebox.show("DEBE AGREGAR LA CANTIDAD DE PRODUCTO QUE DESEA ADQUIRIR", "Informacion", Messagebox.OK, Messagebox.ERROR);
            }

            ActualizaTotales();
            LlenarGrid();

        }
    };

    EventListener ModificarDescuento = new EventListener<Event>() {
        @Override
        public void onEvent(Event event) throws SQLException, IOException, ParseException {
            DetalleFacturaMd modelo = new DetalleFacturaMd();

            Doublebox text = (Doublebox) event.getTarget();
            Row row = (Row) text.getParent();

            modelo = row.getValue();

            if (text.getValue() != null) {
                if (text.getValue() >= 0) {
                    if (Float.parseFloat(text.getText()) <= Float.parseFloat(modelo.getDetProductoPrecioVenta())) {
                        for (int i = 0; i < datos.size(); i++) {
                            if (modelo.getDetProductoId().equals(datos.get(i).getDetProductoId())) {
                                datos.get(i).setProductoDescuento(text.getText());
                                Float PrecioReal = (Float.parseFloat(datos.get(i).getDetProductoPrecioVenta()) - Float.parseFloat(datos.get(i).getProductoDescuento()));
                                Float SubtotalDetalle = PrecioReal * Float.parseFloat(String.valueOf(datos.get(i).getProductoCantidad()));

                                datos.get(i).setSubtotal(String.valueOf(formato.format(SubtotalDetalle)));
                            }
                        }

                    } else {
                        Messagebox.show("EL DESCUENTO ES MAYOR AL PRECIO DEL PRODUCTO", "Informacion", Messagebox.OK, Messagebox.ERROR);
                    }
                } else {
                    Messagebox.show("NO DEBE INGRESAR UNA CANTIDAD NEGATIVA", "Informacion", Messagebox.OK, Messagebox.ERROR);
                }

            } else {
                Messagebox.show("DEBE AGREGAR LA CANTIDAD DE PRODUCTO QUE DESEA ADQUIRIR", "Informacion", Messagebox.OK, Messagebox.ERROR);
            }

            ActualizaTotales();
            LlenarGrid();

        }
    };

    public float ActualizaTotales() {

        subtotal = Float.parseFloat("0.00");
        descuentos = Float.parseFloat("0.00");

        for (int i = 0; i < datos.size(); i++) {
            subtotal += Float.parseFloat(datos.get(i).getDetProductoPrecioVenta()) * Float.parseFloat(datos.get(i).getProductoCantidad());
            descuentos += Float.parseFloat(datos.get(i).getProductoDescuento()) * Float.parseFloat(datos.get(i).getProductoCantidad());
        }

        total = (subtotal - descuentos);

        txtSubtotal.setText(String.valueOf(formato.format(subtotal)));
        txtDescuentos.setText(String.valueOf(formato.format(descuentos)));
        txtTotal.setText(String.valueOf(formato.format(total)));

        return total;
    }
     public float ActualizaDescuento() {

        subtotal = Float.parseFloat("0.00");
        descuentos = Float.parseFloat("0.00");

        for (int i = 0; i < datos.size(); i++) {
            subtotal += Float.parseFloat(datos.get(i).getDetProductoPrecioVenta()) * Float.parseFloat(datos.get(i).getProductoCantidad());
            descuentos += Float.parseFloat(datos.get(i).getProductoDescuento()) * Float.parseFloat(datos.get(i).getProductoCantidad());
        }

        //total = (subtotal - descuentos);

      

        return descuentos;
    }
      public float ActualizaSubtotal() {

        subtotal = Float.parseFloat("0.00");
        descuentos = Float.parseFloat("0.00");

        for (int i = 0; i < datos.size(); i++) {
            subtotal += Float.parseFloat(datos.get(i).getDetProductoPrecioVenta()) * Float.parseFloat(datos.get(i).getProductoCantidad());
            descuentos += Float.parseFloat(datos.get(i).getProductoDescuento()) * Float.parseFloat(datos.get(i).getProductoCantidad());
        }

        //total = (subtotal - descuentos);

      

        return subtotal;
    }

    private boolean ProductoAgregado(String ProductoId) {
        boolean resp = false;

        for (int i = 0; i < datos.size(); i++) {
            if (ProductoId.equals(datos.get(i).getDetProductoId())) {
                resp = true;
                break;
            }
        }
        return resp;
    }

    private void LimpiarProducto() {
        cbxDetalleBusqueda.setText("");
        txtProductoId.setText("");
        txtProductoTipo.setText("");
        txtProductoDescripcion.setText("");
        txtProductoPrecio.setText("");
        txtProductoDescuento.setText("");
        txtProductoStock.setText("");
        txtProductoCantidad.setText("");

    }

    public void onSelect$cbxDetalleBusqueda(Event evt) throws SQLException {
        ProductosDal pro = new ProductosDal();
        ProductosMd proMd = pro.BuscarProducto(cbxDetalleBusqueda.getSelectedItem().getValue().toString());

        txtProductoId.setText(proMd.getCodigo());
        txtProductoTipo.setText(proMd.getTipo_servicio());
        txtProductoDescripcion.setText(proMd.getDescripcion());
        txtProductoPrecio.setText(proMd.getPrecio_venta());
        txtProductoDescuento.setText(proMd.getDescuento());
        txtProductoStock.setText(proMd.getStock());

    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        UsuariosMd mod = user.REGselectUsuario(desktop.getSession().getAttribute("USER").toString());

        txtEmpleadoId.setText(mod.getCodigo());
        txtEmpleadoNombre.setText(mod.getNombre());
        txtEmpleadoUsuario.setText(desktop.getSession().getAttribute("USER").toString());

        txtFacturaFecha.setText(String.valueOf(formatter.format(new Date())));

        util.cargaCombox("SELECT P.PRO_ID,\n"
                + "       CONCAT( IFNULL(P.PRO_DESCRIPCION,' '),\" \", IFNULL(P.PRO_MARCA,' ') ,\" \",IFNULL((CASE P.PRO_PRESENTACION\n"
                + "                   WHEN 'U' THEN 'UNIDAD'\n"
                + "                    WHEN 'C' THEN 'CAJA'\n"
                + "                    WHEN 'B' THEN 'BOLSA'\n"
                + "                    WHEN 'L' THEN 'LITRO' ELSE ' ' END),' '),\" \", case \n"
                + "  when P.PRO_CONVERSION IS NULL OR P.PRO_CONVERSION = 0 then ' '  \n"
                + "  else  CONCAT(\"X \",P.PRO_CONVERSION)\n"
                + "end) DESCRIPCION,\n"
                + "       P.PRO_TIPO_SERVICIO,\n"
                + "       IFNULL(FORMAT(P.PRO_PRECIO_VENTA,2),'-'),\n"
                + "       IFNULL(FORMAT(IFNULL(P.PRO_DESCUENTO,0),2),'-'),\n"
                + "       IFNULL(P.pro_stock_barrita,0)\n"
                + "FROM productos P  ORDER BY P.PRO_DESCRIPCION ASC", cbxDetalleBusqueda);
        
        //para obtener la lista
         lista = ctd.consulta();
        
           EventQueues.lookup("myEventQueue", EventQueues.DESKTOP, true)
                .subscribe(new EventListener() {
                    public void onEvent(Event event) throws Exception {
                        List<CatalogosMd> data = new ArrayList<CatalogosMd>();
                        data.clear();
                        data = (List<CatalogosMd>) event.getData();
                        if (data.isEmpty()) {
                            txtProductoId.setText("");
                           
                        } else {
                            for (CatalogosMd item : data) {
                                if(data.size()==1){
                                txtProductoId.setText(item.getCodemp());
                                 txtProductoId.setFocus(true);
                                }
                            }
                        }
                    }
                });

    }

    public void onChange$txtClienteNit(Event evt) throws SQLException, SAXException, IOException, ParserConfigurationException {
        ClientesMd cl = cliente.BuscarClientes(txtClienteNit.getText());

        if (cl != null) {
            txtClienteNombre.setText(cl.getNombreComercial());
            txtClienteId.setText(cl.getCodigoCliente());
            txtClienteDireccion.setText(cl.getDireccion());
            txtClienteTelefono.setText(cl.getTelefono());
            txtClienteAlta.setText(cl.getFechaAlta());
        } else {
            limpiarCliente();
            Messagebox.show("NO EXISTE EL CLIENTE INGRESADO, INTENTE DE NUEVO O ", "Informacion", Messagebox.OK, Messagebox.ERROR);
        }

    }

    private void limpiarCliente() {
        txtClienteNombre.setText("");
        txtClienteId.setText("");
        txtClienteDireccion.setText("");
        txtClienteTelefono.setText("");
        txtClienteAlta.setText("");
    }

}
