
package MD;

public class VentasMd {
    
    private String codigo;
    private String correlativo;
    private String cantidad;
    private String precio;
    private String usuario;
    private String fecha;
    private String descuento;
    private String total;
    private String ClienteId;
    private String Estado;
    private String Subtotal;
    private String EmpleadoId;

    public String getEmpleadoId() {
        return EmpleadoId;
    }

    public void setEmpleadoId(String EmpleadoId) {
        this.EmpleadoId = EmpleadoId;
    }

    public String getSubtotal() {
        return Subtotal;
    }

    public void setSubtotal(String Subtotal) {
        this.Subtotal = Subtotal;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public String getClienteId() {
        return ClienteId;
    }

    public void setClienteId(String ClienteId) {
        this.ClienteId = ClienteId;
    }
    
   /* private String nit;
    private String nombre;*/

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(String correlativo) {
        this.correlativo = correlativo;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

  /*  public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
  */
}
