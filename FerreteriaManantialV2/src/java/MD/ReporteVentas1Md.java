
package MD;

import java.util.List;

public class ReporteVentas1Md {
    
    private String correlativo;
    private String factura;
    private String autorizacion;
    private String descripcion;
    private String precio_ing;
    private String cantidad_ing;
    private String total;
    private String fecha;
    
    List<TotalesVentasXTurno> Totales;

    public List<TotalesVentasXTurno> getTotales() {
        return Totales;
    }

    public void setTotales(List<TotalesVentasXTurno> Totales) {
        this.Totales = Totales;
    }

    public String getFactura() {
        return factura;
    }

    public String getCorrelativo() {
        return correlativo;
    }

    public String getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }
    
    

    public void setCorrelativo(String correlativo) {
        this.correlativo = correlativo;
    }

    
    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio_ing() {
        return precio_ing;
    }

    public void setPrecio_ing(String precio_ing) {
        this.precio_ing = precio_ing;
    }

    public String getCantidad_ing() {
        return cantidad_ing;
    }

    public void setCantidad_ing(String cantidad_ing) {
        this.cantidad_ing = cantidad_ing;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    
    
}
