package MD;

import java.util.List;

public class KardexTipoMd {

    private String Correlativo;
    private String codigo;
    private String nombre;
    private String cantidad_ing;
    private String precio_ing;
    private String cantidad_sal;
    private String precio_sal;
    private String existencia;
    private String tipo;
    List<TotalesVentasXTurno> Totales;

    public List<TotalesVentasXTurno> getTotales() {
        return Totales;
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

    public String getExistencia() {
        return existencia;
    }

    public void setExistencia(String existencia) {
        this.existencia = existencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
