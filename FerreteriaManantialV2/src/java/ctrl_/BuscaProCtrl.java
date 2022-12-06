/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctrl_;

import MD.CatalogosMd;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author HP 15
 */
public class BuscaProCtrl extends GenericForwardComposer{
    Window modalDialog;
    private Textbox bemp1;
     private Listbox lb2;
    List<CatalogosMd> lista = new ArrayList<CatalogosMd>();
    List<CatalogosMd> data = new ArrayList<CatalogosMd>();
    private Textbox cod1;
    private Textbox nom1;
    
    
    public void doAfterCompose (Component comp) throws Exception {
         super.doAfterCompose(comp);
           bemp1.setFocus(true);
        EventQueues.lookup("myEventQueue", EventQueues.DESKTOP, true)
                .subscribe(new EventListener() {
                    public void onEvent(Event event) throws Exception {
                       
                        lista = (List<CatalogosMd>) event.getData();
                      
                    }
                });
        
        
    }
    
      public void onClick$btnAgregar(Event e) {
        List<CatalogosMd> items = new ArrayList<CatalogosMd>();
        CatalogosMd data = new CatalogosMd();
        data.setCodemp(cod1.getText());
        data.setNomemp(nom1.getText());
        
        items.add(data);
        EventQueues.lookup("myEventQueue", EventQueues.DESKTOP, true)
                .publish(new Event("onChangeNickname", null, items));
        modalDialog.detach();
    }
      
      public void onChanging$bemp1(InputEvent event) {
        data.clear();
        String byEvent = event.getValue(); //This will give you the new value
        System.out.println("TECLEADO.: "+byEvent);
        for(CatalogosMd item : lista){
            if (!byEvent.equals("") && item.getNomemp().contains(byEvent.toUpperCase())) {
                CatalogosMd da = new CatalogosMd();
                da.setCodemp(item.getCodemp());
                da.setNomemp(item.getNomemp());
                
                data.add(da);
            }
        }
        lb2.setModel(new ListModelList(data));
    }
    
    
    
     public void onClick$btnSalir(Event e) {
//        List<CatalogosMd> items = new ArrayList<CatalogosMd>();
//        items.clear();
//        EventQueues.lookup("myEventQueue", EventQueues.DESKTOP, true)
//                .publish(new Event("onChangeNickname", null, items));
        modalDialog.detach();
    }
    
    
    
}
