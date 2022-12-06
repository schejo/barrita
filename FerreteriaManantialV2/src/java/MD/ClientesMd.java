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
public class ClientesMd {
    private String usuario;
    private String CodigoCliente;
    private String NombreComercial;
    private String Nit;
    private String Direccion;
    
    private String Telefono;
    private String FechaAlta;
    private String correCleinte;
    //variables para mostrar el listado de clientes
    private String nombreMos;
    private String codigoClienteMos;
    //variables del reporte
   
    private String usuarioAlta;
    private String fechaModi;
    private String usuarioModi;
    //obtener fecha de la base de datos
    private String ObtenerFecha;
    private String credito;
    private String limite;
    private String disponible;
    private String montocredito;
    private String montopago;
    private String factura;
    private String obs;
    
//variables de mensajes
    private String resp;
    private String msg;
    private String dir;

    public String getMontocredito() {
        return montocredito;
    }

    public void setMontocredito(String montocredito) {
        this.montocredito = montocredito;
    }

    public String getMontopago() {
        return montopago;
    }

    public void setMontopago(String montopago) {
        this.montopago = montopago;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
    
    

    public String getLimite() {
        return limite;
    }

    public void setLimite(String limite) {
        this.limite = limite;
    }

    public String getDisponible() {
        return disponible;
    }

    public void setDisponible(String disponible) {
        this.disponible = disponible;
    }
    
    

    public String getCredito() {
        return credito;
    }

    public void setCredito(String credito) {
        this.credito = credito;
    }
    
    

    public String getObtenerFecha() {
        return ObtenerFecha;
    }

    public void setObtenerFecha(String ObtenerFecha) {
        this.ObtenerFecha = ObtenerFecha;
    }
    
    

    public String getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(String usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public String getFechaModi() {
        return fechaModi;
    }

    public void setFechaModi(String fechaModi) {
        this.fechaModi = fechaModi;
    }

    public String getUsuarioModi() {
        return usuarioModi;
    }

    public void setUsuarioModi(String usuarioModi) {
        this.usuarioModi = usuarioModi;
    }
    
    
    

    public String getNombreMos() {
        return nombreMos;
    }

    public void setNombreMos(String nombreMos) {
        this.nombreMos = nombreMos;
    }

    public String getCodigoClienteMos() {
        return codigoClienteMos;
    }

    public void setCodigoClienteMos(String codigoClienteMos) {
        this.codigoClienteMos = codigoClienteMos;
    }
    
    

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCorreCleinte() {
        return correCleinte;
    }

    public void setCorreCleinte(String correCleinte) {
        this.correCleinte = correCleinte;
    }

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
    
    

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getFechaAlta() {
        return FechaAlta;
    }

    public void setFechaAlta(String FechaAlta) {
        this.FechaAlta = FechaAlta;
    }

    public ClientesMd() {
    }

    public String getCodigoCliente() {
        return CodigoCliente;
    }

    public void setCodigoCliente(String CodigoCliente) {
        this.CodigoCliente = CodigoCliente;
    }

    public String getNombreComercial() {
        return NombreComercial;
    }

    public void setNombreComercial(String NombreComercial) {
        this.NombreComercial = NombreComercial;
    }

    public String getNit() {
        return Nit;
    }

    public void setNit(String Nit) {
        this.Nit = Nit;
    }

}
