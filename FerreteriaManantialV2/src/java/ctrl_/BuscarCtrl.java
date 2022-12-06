//CALUCO
package ctrl_;

import DAL.BuscarDal;
import MD.BuscarMd;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

public class BuscarCtrl extends GenericForwardComposer {

    private Textbox busca;
    private Listbox lb2;
    private List<BuscarMd> allData = new ArrayList<BuscarMd>();
    private List<BuscarMd> allItem = new ArrayList<BuscarMd>();
    BuscarDal bp = new BuscarDal();
    private Include rootPagina;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        allData = bp.BuscarProd();
        
    }

    public void onChange$busca() {

        allItem.clear();
        BuscarMd bsp;
        for (BuscarMd item : allData) {
            if ((!busca.getText().equals("") && item.getNombre().contains(busca.getText().toUpperCase()))||(!busca.getText().equals("") && item.getCodigo().contains(busca.getText().toUpperCase()))) {
                bsp = new BuscarMd();
                bsp.setCodigo(item.getCodigo());
                bsp.setNombre(item.getNombre());
                bsp.setMarca(item.getMarca());
                bsp.setPrecio(item.getPrecio());
                bsp.setUbicacion(item.getUbicacion());
                bsp.setSaldo(item.getSaldo());
                allItem.add(bsp);

            }
        }
        lb2.setModel(new ListModelList(allItem));
        busca.setFocus(true);
    }

    public void onClick$btnSalir() {
        rootPagina.setSrc("/Vistas/Principal.zul");
    }
}
