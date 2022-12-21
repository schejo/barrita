/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctrl_;

import DAL.ProductosNuevoDal;
import MD.ProductosNuevoMd;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;

/**
 *
 * @author chejo
 */
public class ProductosNuevoCtrl extends GenericForwardComposer {

    //para mostrar los prodcutos y sus codigos
    private Combobox busPro;
    List<ProductosNuevoMd> allProductos = new ArrayList<ProductosNuevoMd>();
    List<ProductosNuevoMd> allProductos1 = new ArrayList<ProductosNuevoMd>();
    List<ProductosNuevoMd> allPro2Tabla = new ArrayList<ProductosNuevoMd>();

    ProductosNuevoDal ProductoDal = new ProductosNuevoDal();//lo copie de usuarios
    //para mostrar
    ProductosNuevoMd ProductoModelo = new ProductosNuevoMd();
    private Textbox codPro;
    private Textbox nomPro;
    private Combobox tipPro;
    private Combobox tisPro;
    private Textbox marPro;
    private Combobox prsPro;
    private Doublebox preComp;
    private Doublebox prePro;
    private Doublebox desPro;
    private Doublebox stoPro;
    private Textbox covPro;
    private Combobox medPro;
    private Textbox minPro;
    private Textbox maxPro;
    private Combobox ubiPro;
    private Combobox ubiFerrete;
    //rediraccionar paginas 
    private Include rootPagina;

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        allProductos1 = ProductoDal.RSelect();
        allPro2Tabla = ProductoDal.RSelect2Tabla();

        desPro.setFormat("###0.##");
        desPro.setLocale(Locale.US);
        prePro.setFormat("###0.##");
        prePro.setLocale(Locale.US);
        preComp.setFormat("###0.##");
        preComp.setLocale(Locale.US);
        bloquear();

    }

    public void onChange$ubiFerrete(Event e) {
        
       
        if (ubiFerrete.getSelectedItem().getValue().toString().equals("1")&&session.getAttribute("SUCURSAL").toString().equals("1")) {
            allProductos = ProductoDal.allCL();
            busPro.setModel(new ListModelList(allProductos));
             liberar();
        clear();
        codPro.setFocus(true);
        } else {
            if (ubiFerrete.getSelectedItem().getValue().toString().equals("2")&&session.getAttribute("SUCURSAL").toString().equals("2")) {
                allProductos = ProductoDal.MosProc2();
                busPro.setModel(new ListModelList(allProductos));
                 liberar();
        clear();
        codPro.setFocus(true);
            }else{
                 Clients.showNotification("Debe seleccionar su sucursal" + "<br/>",
                    Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3000);
                 bloquear();
                 
            }
        }
    }

    public void onChange$busPro(Event e) {
        ProductoModelo = new ProductosNuevoMd();

        if (ubiFerrete.getSelectedItem().getValue().toString().equals("1")) {
            ProductoModelo = ProductoDal.MostrarProducto(busPro.getSelectedItem().getValue().toString());

        } else {
            if (ubiFerrete.getSelectedItem().getValue().toString().equals("2")) {
                ProductoModelo = ProductoDal.MostrarCarrizal(busPro.getSelectedItem().getValue().toString());
            } else {
                ProductoModelo = ProductoDal.MostrarAngeles(busPro.getSelectedItem().getValue().toString());
            }
        }

        if (ProductoModelo.getResp().equals("1")) {
            codPro.setText(busPro.getSelectedItem().getValue().toString());
            nomPro.setText(ProductoModelo.getDescripcion());
            BuscaItem(ProductoModelo.getTipo_pro(), this.tipPro);
            // tipPro.setText(ProductoModelo.getTipo_pro());
            //tisPro.setText(ProductoModelo.getTipo_ser());
            BuscaItem(ProductoModelo.getTipo_ser(), this.tisPro);
            marPro.setText(ProductoModelo.getMarca());
            BuscaItem(ProductoModelo.getPresentacion(), this.prsPro);
            //prsPro.setText(ProductoModelo.getPresentacion());
            preComp.setText(ProductoModelo.getPre_compra());
            prePro.setText(ProductoModelo.getPre_venta());
            desPro.setText(ProductoModelo.getDescuento());
            stoPro.setText(ProductoModelo.getPro_stock());
            if (ubiFerrete.getSelectedItem().getValue().toString().equals("1")) {
                stoPro.setDisabled(true);
            } else {
                if (ubiFerrete.getSelectedItem().getValue().toString().equals("2")) {
                    stoPro.setDisabled(false);
                } else {
                    stoPro.setDisabled(false);
                }
            }
            covPro.setText(ProductoModelo.getPro_conver());
            //medPro.setText(ProductoModelo.getMedi_pro());
            BuscaItem(ProductoModelo.getMedi_pro(), this.medPro);
            minPro.setText(ProductoModelo.getMinimo());
            maxPro.setText(ProductoModelo.getMaximo());
            // ubiPro.setText(ProductoModelo.getUbicacion());
            BuscaItem(ProductoModelo.getUbicacion(), this.ubiPro);
            // BuscaItem(ProductoModelo.getUbicacionFerre(), this.ubiFerrete);

        } else {
            clear();
            codPro.setText("");
            codPro.setDisabled(false);
            codPro.setFocus(true);
            Clients.showNotification(ProductoModelo.getMsg() + "<br/>",
                    Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3000);
        }

    }

    public void onChange$nomPro(Event e) {
        ProductoModelo = new ProductosNuevoMd();
        ProductoModelo = ProductoDal.BusProducto(nomPro.getText());
        if (ProductoModelo.getResp().equals("1")) {

        } else {
            //clear();
            // nomPro.setText("");
            //curso.setDisabled(false);
            // nomPro.setFocus(true);
            Clients.showNotification(ProductoModelo.getMsg() + "<br/>",
                    Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000);
        }

    }

    public void onChange$codPro(Event e) {
        ProductoModelo = new ProductosNuevoMd();

        ProductoModelo = ProductoDal.MostrarProducto(codPro.getText());
        if (ProductoModelo.getResp().equals("1")) {
            nomPro.setText(ProductoModelo.getDescripcion());
            BuscaItem(ProductoModelo.getTipo_pro(), this.tipPro);
            // tipPro.setText(ProductoModelo.getTipo_pro());
            //tisPro.setText(ProductoModelo.getTipo_ser());
            BuscaItem(ProductoModelo.getTipo_ser(), this.tisPro);
            marPro.setText(ProductoModelo.getMarca());
            BuscaItem(ProductoModelo.getPresentacion(), this.prsPro);
            //prsPro.setText(ProductoModelo.getPresentacion());
            preComp.setText(ProductoModelo.getPre_compra());
            prePro.setText(ProductoModelo.getPre_venta());
            desPro.setText(ProductoModelo.getDescuento());

            if (ubiFerrete.getSelectedItem().getValue().toString().equals("1")) {
                stoPro.setDisabled(true);
                stoPro.setText(ProductoModelo.getPro_stock_Barrita());

            } else {
                if (ubiFerrete.getSelectedItem().getValue().toString().equals("2")) {
                    stoPro.setDisabled(false);
                    //carrizal
                    stoPro.setText(ProductoModelo.getPro_stock_Carrizal());
                } else {
                    stoPro.setDisabled(false);
                    //los angeles
                    stoPro.setText(ProductoModelo.getPro_stock_Angeles());
                }
            }
            covPro.setText(ProductoModelo.getPro_conver());
            //medPro.setText(ProductoModelo.getMedi_pro());
            BuscaItem(ProductoModelo.getMedi_pro(), this.medPro);
            minPro.setText(ProductoModelo.getMinimo());
            maxPro.setText(ProductoModelo.getMaximo());
            // ubiPro.setText(ProductoModelo.getUbicacion());
            BuscaItem(ProductoModelo.getUbicacion(), this.ubiPro);

        } else {
            clear();
            codPro.setText("");
            codPro.setDisabled(false);
            codPro.setFocus(true);
            Clients.showNotification(ProductoModelo.getMsg() + "<br/>",
                    Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3000);
        }

    }

    public void onClick$btnGuardar(Event e) throws SQLException, ClassNotFoundException, ParseException {
        int op = 0, op1 = 0;
        if (nomPro.getText().trim().equals("")) {
            op = 1;
        }
        if (tipPro.getSelectedIndex() == -1) {
            op = 1;
        }
        if (tisPro.getSelectedIndex() == -1) {
            op = 1;
        }
        if (marPro.getText().trim().equals("")) {
            op = 1;
        }
        if (prsPro.getSelectedIndex() == -1) {
            op = 1;
        }
        if (preComp.getText().trim().equals("")) {
            op = 1;
        }
        if (prePro.getText().trim().equals("")) {
            op = 1;
        }
        if (desPro.getText().trim().equals("")) {
            op = 1;
        }
        if (stoPro.getText().trim().equals("")) {
            op = 1;
        }
        if (covPro.getText().trim().equals("")) {
            op = 1;
        }
        if (medPro.getSelectedIndex() == -1) {
            op = 1;
        }
        if (minPro.getText().trim().equals("")) {
            op = 1;
        }
        if (maxPro.getText().trim().equals("")) {
            op = 1;
        }
        if (ubiPro.getText().trim().equals("")) {
            op = 1;
        }
        if (ubiFerrete.getSelectedIndex() == -1) {
            op = 1;
        }
        if (op == 0) {
            ProductoModelo = new ProductosNuevoMd();

            //para poder saber en que fereteria buscar los codigos
            if (ubiFerrete.getSelectedItem().getValue().toString().equals("1")) {
                for (ProductosNuevoMd dt : allProductos1) {
                    if (dt.getCodigo().equals(codPro.getText())) {
                        op1++;
                    }
                }

            } else {
                if (ubiFerrete.getSelectedItem().getValue().toString().equals("2")) {
                      for (ProductosNuevoMd dt : allPro2Tabla) {
                    if (dt.getCodigo().equals(codPro.getText())) {
                        op1++;
                    }
                }

                } else {

                }
            }

            if (op1 == 0) {

                //aqui se pone lo que se va a guardar
                ProductoModelo.setDescripcion(nomPro.getText().toUpperCase());
                ProductoModelo.setTipo_pro(tipPro.getSelectedItem().getValue().toString());
                ProductoModelo.setTipo_ser(tisPro.getSelectedItem().getValue().toString());
                ProductoModelo.setMarca(marPro.getText().toUpperCase());
                ProductoModelo.setPresentacion(prsPro.getSelectedItem().getValue().toString());
                ProductoModelo.setPre_compra(preComp.getText().toUpperCase());
                ProductoModelo.setPre_venta(prePro.getText().toUpperCase());
                ProductoModelo.setDescuento(desPro.getText().toUpperCase());
                ProductoModelo.setPro_stock_Barrita(stoPro.getText().toUpperCase());
                ProductoModelo.setPro_conver(covPro.getText().toUpperCase());
                ProductoModelo.setMedi_pro(medPro.getSelectedItem().getValue().toString());
                ProductoModelo.setMinimo(minPro.getText().toUpperCase());
                ProductoModelo.setMaximo(maxPro.getText().toUpperCase());
                ProductoModelo.setUbicacion(ubiPro.getSelectedItem().getValue().toString());
                ProductoModelo.setUsuario(desktop.getSession().getAttribute("USER").toString());
                if (ubiFerrete.getSelectedItem().getValue().toString().equals("1")) {
                    //barrita
                    ProductoModelo = ProductoDal.saveProBarrita(ProductoModelo);

                } else {
                    if (ubiFerrete.getSelectedItem().getValue().toString().equals("2")) {
                        //carrizal
                        ProductoModelo = ProductoDal.saveProCarrizal(ProductoModelo);
                    } else {
                        //angeles
                        ProductoModelo = ProductoDal.saveProAngeles(ProductoModelo);

                    }
                }

                allProductos = ProductoDal.allCL();
                allProductos1 = ProductoDal.RSelect();
                busPro.setModel(new ListModelList(allProductos));

            } else {
                ProductoModelo.setCodigo(codPro.getText().toUpperCase());
                ProductoModelo.setDescripcion(nomPro.getText().toUpperCase());
                ProductoModelo.setTipo_pro(tipPro.getSelectedItem().getValue().toString());
                ProductoModelo.setTipo_ser(tisPro.getSelectedItem().getValue().toString());
                ProductoModelo.setMarca(marPro.getText().toUpperCase());
                ProductoModelo.setPresentacion(prsPro.getSelectedItem().getValue().toString());
                ProductoModelo.setPre_compra(preComp.getText().toUpperCase());
                ProductoModelo.setPre_venta(prePro.getText().toUpperCase());
                ProductoModelo.setDescuento(desPro.getText().toUpperCase());
                ProductoModelo.setPro_stock_Barrita(stoPro.getText().toUpperCase());
                ProductoModelo.setPro_conver(covPro.getText().toUpperCase());
                ProductoModelo.setMedi_pro(medPro.getSelectedItem().getValue().toString());
                ProductoModelo.setMinimo(minPro.getText().toUpperCase());
                ProductoModelo.setMaximo(maxPro.getText().toUpperCase());
                ProductoModelo.setUbicacion(ubiPro.getSelectedItem().getValue().toString());
                ProductoModelo.setUsuario(desktop.getSession().getAttribute("USER").toString());

                if (ubiFerrete.getSelectedItem().getValue().toString().equals("1")) {
                    ProductoModelo = ProductoDal.updatePro(ProductoModelo);

                } else {
                    if (ubiFerrete.getSelectedItem().getValue().toString().equals("2")) {
                        ProductoModelo = ProductoDal.updateProCarrizal(ProductoModelo);

                    } else {

                        ProductoModelo = ProductoDal.updateProAngeles(ProductoModelo);

                    }
                }

                allProductos = ProductoDal.allCL();
                allProductos1 = ProductoDal.RSelect();
                busPro.setModel(new ListModelList(allProductos));

                //aqui se pone lo que se va a modificar
            }

            if (ProductoModelo.getResp().equals("1")) {
                clear();
                bloquear();
                ubiFerrete.setSelectedIndex(-1);
                Clients.showNotification(ProductoModelo.getMsg() + "<br/>",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000);
            } else {
                Clients.showNotification(ProductoModelo.getMsg() + "<br/>",
                        Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3000);
            }
        } else {
            Clients.showNotification("No puede dejar  <br/>  <br/>  Campos Vacios <br/> <br/>Intentelo de Nuevo",
                    Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 0);
        }

    }

//    public void onClick$btnGuardar(Event e) throws SQLException, ClassNotFoundException, ParseException {
//        int op = 0, op1 = 0;
//        if (nomPro.getText().trim().equals("")) {
//            op = 1;
//        }
//        if (tipPro.getSelectedIndex() == -1) {
//            op = 1;
//        }
//        if (tisPro.getSelectedIndex() == -1) {
//            op = 1;
//        }
//        if (marPro.getText().trim().equals("")) {
//            op = 1;
//        }
//        if (prsPro.getSelectedIndex() == -1) {
//            op = 1;
//        }
//        if (preComp.getText().trim().equals("")) {
//            op = 1;
//        }
//        if (prePro.getText().trim().equals("")) {
//            op = 1;
//        }
//        if (desPro.getText().trim().equals("")) {
//            op = 1;
//        }
//        if (stoPro.getText().trim().equals("")) {
//            op = 1;
//        }
//        if (covPro.getText().trim().equals("")) {
//            op = 1;
//        }
//        if (medPro.getSelectedIndex() == -1) {
//            op = 1;
//        }
//        if (minPro.getText().trim().equals("")) {
//            op = 1;
//        }
//        if (maxPro.getText().trim().equals("")) {
//            op = 1;
//        }
//        if (ubiPro.getText().trim().equals("")) {
//            op = 1;
//        }
//        if (ubiFerrete.getSelectedIndex() == -1) {
//            op = 1;
//        }
//        if (op == 0) {
//            ProductoModelo = new ProductosNuevoMd();
//
//            for (ProductosNuevoMd dt : allProductos1) {
//                if (dt.getCodigo().equals(codPro.getText())) {
//                    op1++;
//                }
//            }
//
//            if (op1 == 0) {
//
//                //aqui se pone lo que se va a guardar
//                ProductoModelo.setDescripcion(nomPro.getText().toUpperCase());
//                ProductoModelo.setTipo_pro(tipPro.getSelectedItem().getValue().toString());
//                ProductoModelo.setTipo_ser(tisPro.getSelectedItem().getValue().toString());
//                ProductoModelo.setMarca(marPro.getText().toUpperCase());
//                ProductoModelo.setPresentacion(prsPro.getSelectedItem().getValue().toString());
//                ProductoModelo.setPre_compra(preComp.getText().toUpperCase());
//                ProductoModelo.setPre_venta(prePro.getText().toUpperCase());
//                ProductoModelo.setDescuento(desPro.getText().toUpperCase());
//                ProductoModelo.setPro_stock(stoPro.getText().toUpperCase());
//                ProductoModelo.setPro_conver(covPro.getText().toUpperCase());
//                ProductoModelo.setMedi_pro(medPro.getSelectedItem().getValue().toString());
//                ProductoModelo.setMinimo(minPro.getText().toUpperCase());
//                ProductoModelo.setMaximo(maxPro.getText().toUpperCase());
//                ProductoModelo.setUbicacion(ubiPro.getSelectedItem().getValue().toString());
//                ProductoModelo.setUbicacionFerre(ubiFerrete.getSelectedItem().getValue().toString());
//                ProductoModelo = ProductoDal.savePro(ProductoModelo);
//                allProductos = ProductoDal.allCL();
//                allProductos1 = ProductoDal.RSelect();
//                busPro.setModel(new ListModelList(allProductos));
//
//            } else {
//                ProductoModelo.setCodigo(codPro.getText().toUpperCase());
//                ProductoModelo.setDescripcion(nomPro.getText().toUpperCase());
//                ProductoModelo.setTipo_pro(tipPro.getSelectedItem().getValue().toString());
//                ProductoModelo.setTipo_ser(tisPro.getSelectedItem().getValue().toString());
//                ProductoModelo.setMarca(marPro.getText().toUpperCase());
//                ProductoModelo.setPresentacion(prsPro.getSelectedItem().getValue().toString());
//                ProductoModelo.setPre_compra(preComp.getText().toUpperCase());
//                ProductoModelo.setPre_venta(prePro.getText().toUpperCase());
//                ProductoModelo.setDescuento(desPro.getText().toUpperCase());
//                ProductoModelo.setPro_stock(stoPro.getText().toUpperCase());
//                ProductoModelo.setPro_conver(covPro.getText().toUpperCase());
//                ProductoModelo.setMedi_pro(medPro.getSelectedItem().getValue().toString());
//                ProductoModelo.setMinimo(minPro.getText().toUpperCase());
//                ProductoModelo.setMaximo(maxPro.getText().toUpperCase());
//                ProductoModelo.setUbicacion(ubiPro.getSelectedItem().getValue().toString());
//                if (ubiFerrete.getSelectedItem().getValue().toString().equals("1")) {
//                    ProductoModelo = ProductoDal.updatePro(ProductoModelo);
//
//                } else {
//                    if (ubiFerrete.getSelectedItem().getValue().toString().equals("2")) {
//                        ProductoModelo = ProductoDal.updateProCarrizal(ProductoModelo);
//
//                    } else {
//
//                        ProductoModelo = ProductoDal.updateProAngeles(ProductoModelo);
//
//                    }
//                }
//
//                allProductos = ProductoDal.allCL();
//                allProductos1 = ProductoDal.RSelect();
//                busPro.setModel(new ListModelList(allProductos));
//
//                //aqui se pone lo que se va a modificar
//            }
//
//            if (ProductoModelo.getResp().equals("1")) {
//                clear();
//                bloquear();
//                ubiFerrete.setSelectedIndex(-1);
//                Clients.showNotification(ProductoModelo.getMsg() + "<br/>",
//                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000);
//            } else {
//                Clients.showNotification(ProductoModelo.getMsg() + "<br/>",
//                        Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3000);
//            }
//        } else {
//            Clients.showNotification("No puede dejar  <br/>  <br/>  Campos Vacios <br/> <br/>Intentelo de Nuevo",
//                    Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 0);
//        }
//
//    }
    public void BuscaItem(String letra, Combobox cb) {
        for (int i = 0; i < cb.getItemCount(); i++) {
            if (letra.equals(cb.getItemAtIndex(i).getValue())) {
                cb.setSelectedIndex(i);
                break;
            }
        }
    }

    public void onClick$btnNuevo(Event e) throws SQLException {
        bloquear();
        clear();
        ubiFerrete.setSelectedIndex(-1);

    }

    public void bloquear() {
        codPro.setDisabled(true);
        nomPro.setDisabled(true);
        tipPro.setDisabled(true);
        tisPro.setDisabled(true);
        marPro.setDisabled(true);
        prsPro.setDisabled(true);
        preComp.setDisabled(true);
        prePro.setDisabled(true);
        desPro.setDisabled(true);
        stoPro.setDisabled(true);
        covPro.setDisabled(true);
        medPro.setDisabled(true);
        minPro.setDisabled(true);
        maxPro.setDisabled(true);
        ubiPro.setDisabled(true);
        busPro.setDisabled(true);

    }

    public void liberar() {
        codPro.setDisabled(false);
        nomPro.setDisabled(false);
        tipPro.setDisabled(false);
        tisPro.setDisabled(false);
        marPro.setDisabled(false);
        prsPro.setDisabled(false);
        preComp.setDisabled(false);
        prePro.setDisabled(false);
        desPro.setDisabled(false);
        stoPro.setDisabled(false);
        covPro.setDisabled(false);
        medPro.setDisabled(false);
        minPro.setDisabled(false);
        maxPro.setDisabled(false);
        ubiPro.setDisabled(false);
        busPro.setDisabled(false);
    }

    public void clear() {
        busPro.setText("");
        busPro.setSelectedIndex(-1);
        codPro.setText("");
        nomPro.setText("");
        tipPro.setText("");
        tipPro.setSelectedIndex(-1);
        tisPro.setText("");
        tisPro.setSelectedIndex(-1);
        marPro.setText("");
        prsPro.setText("");
        prsPro.setSelectedIndex(-1);
        preComp.setText("");
        prePro.setText("");
        desPro.setText("");
        stoPro.setText("");
        stoPro.setDisabled(false);
        covPro.setText("");
        medPro.setText("");
        medPro.setSelectedIndex(-1);
        minPro.setText("");
        maxPro.setText("");
        ubiPro.setText("");
        ubiPro.setSelectedIndex(-1);

    }

    public void onClick$btnSalir() {
        rootPagina.setSrc("/Vistas/Principal.zul");
    }

}
