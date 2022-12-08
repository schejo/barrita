package environment;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;

public class menu extends GenericForwardComposer {

    @Wire
    private Include rootPagina;
    private Label lblUser;

    @SuppressWarnings("unchecked")
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        String User = getUsuario();
        System.out.println("Session.: " + User);
        String sucursal="";
         switch (session.getAttribute("SUCURSAL").toString()) {
            case "1":
                sucursal = "Barrita";
                break;
            case "2":
                sucursal = "Carrizal";
                break;
            case "3":
                sucursal = "Los Angeles";
                break;

        }
        lblUser.setValue(User+" sucursal "+sucursal);
        rootPagina.setSrc("/Vistas/Principal.zul");
    }

    //LINK MENUS
    public void onClick$ItemCOT(Event evt) {
        rootPagina.setSrc("/Vistas/Cotizacion.zul");
    }

    public void onClick$menuHome(Event evt) {
        rootPagina.setSrc("/Vistas/Principal.zul");
    }

    //CATALOGOS
    public void onClick$ItemA(Event evt) {
        rootPagina.setSrc("/Vistas/Usuarios.zul");
    }

    public void onClick$ItemAB(Event evt) {
        rootPagina.setSrc("/Vistas/Productos.zul");
    }
      public void onClick$ItemDUE(Event evt) {
        rootPagina.setSrc("/Vistas/ReporteDeudores.zul");
    }

    public void onClick$ItemABN(Event evt) {
        rootPagina.setSrc("/Vistas/ProductosNuevo.zul");
    }

    //PROCESOS
    public void onClick$ItemB(Event evt) {
        rootPagina.setSrc("/Vistas/Compras.zul");
    }
      public void onClick$ItemCREDI(Event evt) {
        rootPagina.setSrc("/Vistas/Creditos.zul");
    }

    public void onClick$ItemBA(Event evt) {
        rootPagina.setSrc("/Vistas/Ventas1.zul");
    }

    public void onClick$ItemBV(Event evt) {
        rootPagina.setSrc("/Vistas/Ventasvales.zul");
    }

    public void onClick$ItemBB(Event evt) {
        rootPagina.setSrc("/Vistas/Bajas.zul");
    }

    public void onClick$ItemBC(Event evt) {
        rootPagina.setSrc("/Vistas/Buscar.zul");
    }

    public void onClick$ItemBD(Event evt) {
        rootPagina.setSrc("/Vistas/Venta_N.zul");
    }

    public void onClick$ItemBF(Event evt) {
        rootPagina.setSrc("/Vistas/ActCompras.zul");
    }

    public void onClick$ItemBG(Event evt) {
        rootPagina.setSrc("/Vistas/ActVentas.zul");
    }

    //REPORTES
    public void onClick$ItemC(Event evt) {
        rootPagina.setSrc("/Vistas/ReporteKardex.zul");
    }

    public void onClick$ItemCA(Event evt) {
        rootPagina.setSrc("/Vistas/ReporteVentaPago.zul");
    }

    public void onClick$ItemCB(Event evt) {
        rootPagina.setSrc("/Vistas/ReporteCompras.zul");
    }

    public void onClick$ItemCC(Event evt) {
        rootPagina.setSrc("/Vistas/ReporteKardexTipo.zul");
    }

    public void onClick$ItemCD(Event evt) {
        rootPagina.setSrc("/Vistas/ReporteKardexUbica.zul");
    }

    public void onClick$ItemCE(Event evt) {
        rootPagina.setSrc("/Vistas/ReporteVentasdia.zul");
    }

    public void onClick$ItemCF(Event evt) {
        rootPagina.setSrc("/Vistas/ReporteVentasEfectivo.zul");
    }

    public void onClick$ItemCG(Event evt) {
        rootPagina.setSrc("/Vistas/ReporteStock.zul");
    }

        public void onClick$ItemCH(Event evt) {
        rootPagina.setSrc("/Vistas/ReporteFacturas.zul");
    }



    //GRAFICAS
    public void onClick$ItemD(Event evt) {
        rootPagina.setSrc("/Vistas/GraficaPorTipo.zul");
    }

    public void onClick$ItemDA(Event evt) {
        rootPagina.setSrc("/Vistas/GraficaServicio.zul");
    }

    public void onClick$ItemDB(Event evt) {
        rootPagina.setSrc("/Vistas/GraficaPorUbicacion.zul");
    }

    public void onClick$ItemDC(Event evt) {
        rootPagina.setSrc("/Vistas/GraficaVentasEfectivo.zul");
    }

    public void onClick$ItemCli(Event evt) {
        rootPagina.setSrc("/Vistas/Clientes.zul");
    }

    public void onClick$ItemPro(Event evt) {
        rootPagina.setSrc("/Vistas/Proveedores.zul");
    }

    // CERRAR SESSION
    public void onClick$ItemSalir(Event evt) {
        desktop.getSession().invalidate();
        execution.sendRedirect("/login.zul");
    }

    //VALIDAR SESSION EN ZK FRAMEWORK
    public void salir() {
        desktop.getSession().invalidate();
        execution.sendRedirect("login.zul");
    }

    public String getUsuario() {
        System.out.println("Session getUsuario.: " + desktop.getSession().getAttribute("USUARIO"));
        if (desktop.getSession().getAttribute("USER") == null) {
            salir();
            return "";
        }
        Clients.showNotification("BIENVENIDO AL SISTEMA DE INVENTARIO Y FACTURACION <br/>" + login.USER + " <br/>  <br/>");
        return desktop.getSession().getAttribute("USER").toString();
    }

}
