package MD;

import java.util.List;

public class KardexMd {

    private String Correlativo;
    private String codigo;
    private String nombre;
    private String marca;
    private String cantidad_ing;
    private String pre_comp;
    private String precio_ing;
    private String cantidad_sal;
    private String precio_sal;
    private String stopbarrita;
    private String stopcarrizal;
    private String stopangeles;
    List<TotalesVentasXTurno> Totales;

    public String getStopcarrizal() {
        return stopcarrizal;
    }

    public void setStopcarrizal(String stopcarrizal) {
        this.stopcarrizal = stopcarrizal;
    }

    public String getStopangeles() {
        return stopangeles;
    }

    public void setStopangeles(String stopangeles) {
        this.stopangeles = stopangeles;
    }
    
    
    

    public List<TotalesVentasXTurno> getTotales() {
        return Totales;
    }

    public String getPre_comp() {
        return pre_comp;
    }

    public void setPre_comp(String pre_comp) {
        this.pre_comp = pre_comp;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setTotales(List<TotalesVentasXTurno> Totales) {
        this.Totales = Totales;
    }

    public String getCorrelativo() {
        return Correlativo;
    }

    public void setCorrelativo(String Correlativo) {
        this.Correlativo = Correlativo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCantidad_ing() {
        return cantidad_ing;
    }

    public void setCantidad_ing(String cantidad_ing) {
        this.cantidad_ing = cantidad_ing;
    }

    public String getPrecio_ing() {
        return precio_ing;
    }

    public void setPrecio_ing(String precio_ing) {
        this.precio_ing = precio_ing;
    }

    public String getCantidad_sal() {
        return cantidad_sal;
    }

    public void setCantidad_sal(String cantidad_sal) {
        this.cantidad_sal = cantidad_sal;
    }

    public String getPrecio_sal() {
        return precio_sal;
    }

    public void setPrecio_sal(String precio_sal) {
        this.precio_sal = precio_sal;
    }

    public String getStopbarrita() {
        return stopbarrita;
    }

    public void setStopbarrita(String stopbarrita) {
        this.stopbarrita = stopbarrita;
    }

}
