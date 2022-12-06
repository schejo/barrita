package ctrl_;

import DAL.VentasDal;
import MD.DetalleFacturaMd;
import MD.FacturaMd;

//import Util.Reportes;
//import Util.Utilitarios;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.InetAddress;

import org.zkoss.zul.Textbox;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.Component;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.WrongValueException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Composition;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;

public class ReporteFacturasCtrl extends GenericForwardComposer {

    private Intbox txtId;
    private Textbox txtNombre;
    private Textbox txtDireccion;
    private Intbox txtTelefono;
    private Textbox txtNit;
    private Button btnGuardar;
    private Button btnImprimir;

    Div formulario;

    private static final long serialVersionUID = 1L;

//    Utilitarios util = new Utilitarios();
    Session Session = Sessions.getCurrent();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        LlenarGrid();

    }
  
    private Rows rows;
    private Row row;
    public List<FacturaMd> datos;
    private int banderaGrid = 0;
    VentasDal ven = new VentasDal();

    public void LlenarGrid() throws SQLException, ParseException, ClassNotFoundException {

        datos = ven.buscaGrid();
        if (banderaGrid == 1) {
            row.getChildren().clear();
            rows.getChildren().clear();
            banderaGrid = 0;
        }

        for (FacturaMd mov : datos) {
            banderaGrid = 1;
            Label Autorizacion = new Label();
            ValoresLabel(Autorizacion, mov.getFacturaAutorizacion(), "");
            Label Cliente = new Label();
            ValoresLabel(Cliente, mov.getFacturaClienteNombre(), "");
            
SimpleDateFormat formatoE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatoS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            

            String fecha = formatoS.format(formatoE.parse(mov.getFacturaFechaEmision()));

            Label Fecha = new Label();
            ValoresLabel(Fecha, fecha, "");
            Label Total = new Label();
            ValoresLabel(Total, mov.getFacturaTotal(), "");
            Label Estado = new Label();
            ValoresLabel(Estado, Estado(mov.getFacturaEstado()), "");
            Label TipoPago = new Label();
            ValoresLabel(TipoPago, TipoPago(mov.getFacturaTipoPago()), "");

            Div acciones = new Div();
            acciones.setClass("text-center");

            Button Reimprimir = new Button();
            CreateButton(Reimprimir, "btn btn-primary btn-md", "z-icon-print", "margin-left:3px;", "", true);
            acciones.appendChild(Reimprimir);

            Button Anular = new Button();
            if (!mov.getFacturaEstado().equals("A")) {

                CreateButton(Anular, "btn btn-primary btn-md", "z-icon-trash-o", "margin-left:3px;", "", true);
                acciones.appendChild(Anular);
            }

            row = new Row();
            row.setStyle("border-style:solid;border-width:1px");
            row.appendChild(Autorizacion);
            row.appendChild(Cliente);
            row.appendChild(Fecha);
            row.appendChild(Total);
            row.appendChild(Estado);
            row.appendChild(TipoPago);

            row.appendChild(acciones);

            row.setParent(rows);
            row.setValue(mov);

            Reimprimir.addEventListener("onClick", reimprimir);
            Anular.addEventListener("onClick", anular);
        }
    }

    private String TipoPago(String tipo) {
        String resp = "";

        switch (tipo) {
            case "E":
                resp = "EFECTIVO";
                break;
            case "T":
                resp = "TARJETA";
                break;
            case "M":
                resp = "MULTIPLE";
                break;
        }
        return resp;
    }

    private String Estado(String estado) {
        String resp = "";

        switch (estado) {
            case "E":
                resp = "EMITIDA";
                break;
            case "A":
                resp = "ANULADA";
                break;

        }
        return resp;
    }

    EventListener reimprimir = new EventListener<Event>() {
        @Override
        public void onEvent(Event event) throws SQLException, IOException, DocumentException, ParseException {
            FacturaMd modelo = new FacturaMd();

            Button button = (Button) event.getTarget();
            Div div = (Div) button.getParent();
            Row row = (Row) div.getParent();

            modelo = row.getValue();

            PDF(ven.BuscarEncabezadoFactura("", modelo.getFacturaAutorizacion()));
        }
    };

    EventListener anular = new EventListener<Event>() {
        @Override
        public void onEvent(Event event) throws SQLException, IOException, DocumentException, ParseException, ClassNotFoundException, InterruptedException {
            FacturaMd modelo = new FacturaMd();

            Button button = (Button) event.getTarget();
            Div div = (Div) button.getParent();
            Row row = (Row) div.getParent();

            modelo = row.getValue();

            session.setAttribute("AUTORIZACION", modelo.getFacturaAutorizacion());
            session.setAttribute("CLIENTE", modelo.getFacturaClienteNombre());
            session.setAttribute("EMISION", modelo.getFacturaFechaEmision());

            // ven.Anular(modelo.getFacturaAutorizacion(), "PRUEBAS");
            showPopup(new EventListener<Event>() {
                @Override
                public void onEvent(final Event event) throws Exception {
                    LlenarGrid();

                    //Messagebox.show("FACTURA ANULADA CORRECTAMENTE");
                }
            });

        }
    };

    public static void showPopup(final EventListener<Event> eventListener)
            throws InterruptedException {
        //you can give more params with the method to add them as arguments.
        final Map<String, Object> args = new HashMap<String, Object>();
        args.put("modus", "modal");
        openModal("/Vistas/AnulacionFacturas.zul", null, args, eventListener);
    }

    public static void openModal(final String page, final Component parent,
            final Map<String, Object> obMap,
            final EventListener<Event> onCloseListener)
            throws InterruptedException {
        for (final Map.Entry<String, Object> entry : obMap.entrySet()) {
            Executions.getCurrent().setAttribute(entry.getKey(),
                    entry.getValue());
        }
        Executions.getCurrent().setAttribute(Composition.PARENT, null);
        final Component createComponents = Executions.createComponents(page,
                parent, obMap);
        Component parent1 = createComponents;
        parent1 = getWindow(parent1);
        if (parent1 instanceof Window) {
            final Window window = (Window) parent1;
            if (onCloseListener != null) {
                //attach the listener so when popup is closed the listener is called.
                window.addEventListener(Events.ON_CLOSE, onCloseListener);
                window.addEventListener(Events.ON_CANCEL, onCloseListener);
            }
            window.doModal();
        }
    }

    public static Component getWindow(Component comp) {
        if (comp != null && !(comp instanceof Window)) {
            return getWindow(comp.getParent());
        }
        return comp;
    }

    public void ValoresLabel(Label label, String valor, String clase) {
        label.setValue(valor);
        label.setClass(clase);
    }

    public void CreateButton(Button button, String clase, String icon, String style, String label, boolean visible) {
        button.setClass(clase);
        button.setIconSclass(icon);
        button.setStyle(style);
        button.setVisible(visible);
        button.setLabel(label);
    }

//    public void onClick$btnImprimir(Event evt) throws SQLException, DocumentException {
//        Reportes rep = new Reportes();
//        
//        rep.ExtraccionBoletas("0");
//    }
    File f;

    public void PDF(FacturaMd enc/*, java.util.List<DetalleFacturaMd> lista*/) throws SQLException {
        //  String ano_arribo="", num_Arribo="";

        String buque = "";
        DecimalFormat formato = new DecimalFormat("#.00");

        List<DetalleFacturaMd> lista = ven.BuscaDetallesFactura("");

        try {
            com.itextpdf.text.Document detalle = new com.itextpdf.text.Document(PageSize.LEGAL, 0, 390, 0, 0);
            ByteArrayOutputStream badetalle = new ByteArrayOutputStream();
            PdfWriter escritura;
            // Se crea el OutputStream para el fichero donde queremos dejar el pdf.
            FileOutputStream ficheroPdf = new FileOutputStream(desktop.getWebApp().getRealPath("rpt") + File.separator + "archivo.pdf");

// Se asocia el documento al OutputStream y se indica que el espaciado entre
// lineas sera de 20. Esta llamada debe hacerse antes de abrir el documento
            PdfWriter.getInstance(detalle, ficheroPdf).setInitialLeading(20);

            //escritura = PdfWriter.getInstance(detalle, badetalle);
            detalle.open();

            Paragraph predetalle = new Paragraph();
            predetalle.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////// ENCABEZADO DE BOLETA ///////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            PdfPTable table2 = new PdfPTable(10);
            table2.setWidthPercentage(90);

            PdfPCell cell2;

            String direccion = InetAddress.getLocalHost().getHostAddress();
            Image imagen = Image.getInstance("http://" + direccion + ":8080/FerreteriaManantial/bootstrap/img/logo_1.png");
            imagen.scalePercent(70f);

            cell2 = new PdfPCell(imagen);
            cell2.setColspan(10);
            cell2.setRowspan(3);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);

            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("Servicios de ventas Facturadas", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("\"Distribuidora La Barrita, S.A.\"", FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("NIT: 11237058-6", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("Campamento la Barrita, Puerto San Jose, Escuintla", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("Telefonos", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("4954-2983, 5379-0884 y 4218-9304", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(10);
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

            cell2 = new PdfPCell(new Phrase("FACTURA REGIMEN FEL", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("DOCUMENTO TRIBUTARIO ELECRONICO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("SERIE\n" + enc.getFacturaSerieDTE(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("NUMERO DE DTE " + enc.getFacturaNumeroD(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////// DATOS FACTURA FACTURA/////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("INTERNO: " + enc.getFacturaSerie() + " - " + enc.getFacturaNumero(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
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

            cell2 = new PdfPCell(new Phrase("CLIENTE: " + enc.getFacturaClienteId() + "  " + enc.getFacturaClienteNombre(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("DIRECCION: " + enc.getFacturaClienteDireccion(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
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

            cell2 = new PdfPCell(new Phrase("__________________________________________________________"));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("PRODUCTO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(7);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("IMPORTE", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(3);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("__________________________________________________________"));
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

            for (int i = 0; i < lista.size(); i++) {
                cell2 = new PdfPCell(new Phrase(lista.get(i).getDetProductoId() + " " + lista.get(i).getDetProductoDescripcion() + " " + lista.get(i).getDetProductoMarca() + " "
                        + lista.get(i).getDetProductoPresentacion() + " " + lista.get(i).getDetProductoConversion(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
                cell2.setColspan(7);
                cell2.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell2);

                cell2 = new PdfPCell(new Phrase(""));
                cell2.setColspan(3);
                cell2.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell2);

                cell2 = new PdfPCell(new Phrase(lista.get(i).getProductoCantidad() + " x Q." + lista.get(i).getDetProductoPrecioVenta(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
                cell2.setColspan(7);
                cell2.setBorder(Rectangle.NO_BORDER);
                cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                table2.addCell(cell2);

                cell2 = new PdfPCell(new Phrase(String.valueOf(formato.format(Float.parseFloat(lista.get(i).getDetProductoPrecioVenta()) * Float.parseFloat(lista.get(i).getProductoCantidad()))), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
                cell2.setColspan(3);
                cell2.setBorder(Rectangle.NO_BORDER);
                cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                table2.addCell(cell2);
            }

            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("__________________________________________________________"));
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

            cell2 = new PdfPCell(new Phrase("SUBTOTAL: Q." + enc.getFacturaSubtotal(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12)));
            cell2.setColspan(7);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(3);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("(-) DESCUENTO: Q." + enc.getFacturaDescuento(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12)));
            cell2.setColspan(7);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(3);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("TOTAL: Q." + enc.getFacturaTotal(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12)));
            cell2.setColspan(7);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("MEDIO DE PAGO: " + enc.getFacturaTipoPago(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            if (enc.getFacturaTipoPago().equals("EFECTIVO") || enc.getFacturaTipoPago().equals("MULTIPLE")) {
                cell2 = new PdfPCell(new Phrase("EFECTIVO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
                cell2.setColspan(7);
                cell2.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell2);

                cell2 = new PdfPCell(new Phrase("Q." + enc.getFacturaPagoEfectivo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11)));
                cell2.setColspan(3);
                cell2.setBorder(Rectangle.NO_BORDER);
                cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                table2.addCell(cell2);

            }

            if (enc.getFacturaTipoPago().equals("TARJETA") || enc.getFacturaTipoPago().equals("MULTIPLE")) {

                cell2 = new PdfPCell(new Phrase("TARJETA D/C", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
                cell2.setColspan(7);
                cell2.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell2);

                cell2 = new PdfPCell(new Phrase("Q." + enc.getFacturaPagoTarjeta(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11)));
                cell2.setColspan(3);
                cell2.setBorder(Rectangle.NO_BORDER);
                cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                table2.addCell(cell2);
            }

            if (enc.getFacturaTipoPago().equals("CREDITO") || enc.getFacturaTipoPago().equals("MULTIPLE")) {

                cell2 = new PdfPCell(new Phrase("CREDITO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
                cell2.setColspan(7);
                cell2.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell2);

                cell2 = new PdfPCell(new Phrase("Q." + enc.getFacturaCredito(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11)));
                cell2.setColspan(3);
                cell2.setBorder(Rectangle.NO_BORDER);
                cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                table2.addCell(cell2);

            }

            if (enc.getFacturaTipoPago().equals("EFECTIVO") || enc.getFacturaTipoPago().equals("MULTIPLE")) {
                cell2 = new PdfPCell(new Phrase("PAGO RECIBIDO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
                cell2.setColspan(7);
                cell2.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell2);

                cell2 = new PdfPCell(new Phrase("Q." + enc.getFacturaEfectivoRecibido(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11)));
                cell2.setColspan(3);
                cell2.setBorder(Rectangle.NO_BORDER);
                cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                table2.addCell(cell2);

                cell2 = new PdfPCell(new Phrase("CAMBIO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
                cell2.setColspan(7);
                cell2.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell2);

                cell2 = new PdfPCell(new Phrase("Q." + enc.getFacturaCambio(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11)));
                cell2.setColspan(3);
                cell2.setBorder(Rectangle.NO_BORDER);
                cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                table2.addCell(cell2);

            }

            cell2 = new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("Es un placer el Servirle, Vuelva Pronto!!!", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("__________________________________________________________"));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("DATOS DE CERTIFICADOR", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("NIT CERTIFICADOR: ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(enc.getFacturaNitCertificador(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("NOMBRE CERTIFICADOR: ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(enc.getFacturaNombreCertificador(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("AUTORIZACION", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(enc.getFacturaAutorizacion(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("FECHA CERTIFICACION ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(enc.getFacturaFechaCertificacion(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            BarcodeQRCode qrcode = new BarcodeQRCode(enc.getFacturaAutorizacion(), 1, 1, null);
            Image qrcodeImage = qrcode.getImage();
            qrcodeImage.scalePercent(300f);

            cell2 = new PdfPCell(qrcodeImage);
            cell2.setColspan(10);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);

            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(".", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11)));
            cell2.setColspan(10);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table2.addCell(cell2);

            predetalle.add(table2);

            detalle.add(predetalle);

            detalle.close();
            //escritura.close();

            f = new File(desktop.getWebApp().getRealPath("rpt") + File.separator + "archivo.pdf");
            //Messagebox.show("CREA ARCHIVO");
            byte[] buffer = new byte[(int) f.length()];
            FileInputStream fs = new FileInputStream(f);

            fs.read(buffer);
            fs.close();

            //ByteArrayInputStream is = new ByteArrayInputStream(buffer);
            InputStream is = new ByteArrayInputStream(buffer);
            Filedownload.save(is, "application/pdf", "archivo.pdf");
        } catch (Exception ex) {
            ex.getMessage();
        }
    }
}
