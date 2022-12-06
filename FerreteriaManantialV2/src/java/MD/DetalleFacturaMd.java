/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MD;

/**
 *
 * @author kevin
 */
public class DetalleFacturaMd {

    private String DetProductoId;
    private String DetProductoDescripcion;
    private String DetProductoTipo;
    private String DetProductoPrecioVenta;
    private String ProductoStock;
        //mensajes del sistema
    private String resp;
    private String msg;
    private String dir;

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    
    
    
    public String getDetProductoMarca() {
        return DetProductoMarca;
    }

    public void setDetProductoMarca(String DetProductoMarca) {
        this.DetProductoMarca = DetProductoMarca;
    }

    public String getDetProductoConversion() {
        return DetProductoConversion;
    }

    public void setDetProductoConversion(String DetProductoConversion) {
        this.DetProductoConversion = DetProductoConversion;
    }
    private String ProductoDescuento;
    private String ProductoCantidad;
    private String Subtotal;
    private String DetProductoMarca;
    private String DetProductoConversion;
    private String DetProductoPresentacion;

    public String getDetProductoPresentacion() {
        return DetProductoPresentacion;
    }

    public void setDetProductoPresentacion(String DetProductoPresentacion) {
        this.DetProductoPresentacion = DetProductoPresentacion;
    }
    
    

    public String getDetProductoId() {
        return DetProductoId;
    }

    public void setDetProductoId(String DetProductoId) {
        this.DetProductoId = DetProductoId;
    }

    public String getDetProductoDescripcion() {
        return DetProductoDescripcion;
    }

    public void setDetProductoDescripcion(String DetProductoDescripcion) {
        this.DetProductoDescripcion = DetProductoDescripcion;
    }

    public String getDetProductoTipo() {
        return DetProductoTipo;
    }

    public void setDetProductoTipo(String DetProductoTipo) {
        this.DetProductoTipo = DetProductoTipo;
    }

    public String getDetProductoPrecioVenta() {
        return DetProductoPrecioVenta;
    }

    public void setDetProductoPrecioVenta(String DetProductoPrecioVenta) {
        this.DetProductoPrecioVenta = DetProductoPrecioVenta;
    }

    public String getProductoStock() {
        return ProductoStock;
    }

    public void setProductoStock(String ProductoStock) {
        this.ProductoStock = ProductoStock;
    }

    public String getProductoDescuento() {
        return ProductoDescuento;
    }

    public void setProductoDescuento(String ProductoDescuento) {
        this.ProductoDescuento = ProductoDescuento;
    }

    public String getProductoCantidad() {
        return ProductoCantidad;
    }

    public void setProductoCantidad(String ProductoCantidad) {
        this.ProductoCantidad = ProductoCantidad;
    }

    public String getSubtotal() {
        return Subtotal;
    }

    public void setSubtotal(String Subtotal) {
        this.Subtotal = Subtotal;
    }


}
