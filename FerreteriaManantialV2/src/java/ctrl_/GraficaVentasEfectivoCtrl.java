package ctrl_;

import DAL.GraficaVentasEfectivoDal;
import MD.GraficaVentasEfectivoMd;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.ngi.zhighcharts.SimpleExtXYModel;
import org.ngi.zhighcharts.ZHighCharts;

 
  public class GraficaVentasEfectivoCtrl extends GenericForwardComposer {
    private ZHighCharts grafica2;
    private ZHighCharts grafica1;
    private Datebox Fecha_Inicial;
   // private Datebox Fecha_Final;
    private Session Session = Sessions.getCurrent();

    //pantallas de dashboard
    private Div dashboardJefe;
    private Div dashboardUsuario;
    
    //private CorrespondenciaJefaturaDal dataBaseJefatura = new CorrespondenciaJefaturaDal();
    private Div divContenedor;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Date date = new Date();
        int month = date.getMonth();
      //  Grafica1(Fecha_Inicial.getText(), Fecha_Final.getText());
    }

      public void onChange$Fecha_Inicial(Event evn) throws SQLException, ClassNotFoundException {
        //Bitacora bt = new Bitacora();
        //String rps = bt.login(Login.usuario, "Estadistica-Web", "Grafica", "null", 0, 0, "Grafica Tipo de Buque"); 
      Grafica1(Fecha_Inicial.getText());
      Grafica2(Fecha_Inicial.getText());       
    }

        //GRAFICA DE BARRAS #1 en DAL
    public void Grafica1(String Fecha_inicial) throws SQLException{
        GraficaVentasEfectivoDal dataBase = new GraficaVentasEfectivoDal();

        grafica1.setType("column");
        grafica1.setTitle("Grafica de Consumo");
        grafica1.setSubTitle("Fechas "+Fecha_inicial );

        grafica1.setyAxisOptions("{ "
                + "min:0"
                + "}");
        grafica1.setYAxisTitle("Total");
        grafica1.setTooltipFormatter("function formatTooltip(obj){ "
                + "return ''+obj.x +': '+ obj.y + '' ;"
                + "}");
        grafica1.setLegend("{"
                + "layout: 'vertical',"
                + "backgroundColor: '#FFFFFF',"
                + "align: 'left',"
                + "verticalAlign: 'top',"
                + "x: 100,"
                + "y: 70,"
                + "floating: true,"
                + "shadow: true"
                + "}");
        grafica1.setPlotOptions("{"
                + "column: {"
                + "colorByPoint: true," //Para cambiar de color en las barras
                + "pointPadding: 0.2,"
                + "borderWidth: 0,"
                + "dataLabels: {"
                + " enabled: true,"
                + " format: '{point.y:.1f}'"
                + " }"
                + "}"
                + "}");
        SimpleExtXYModel model = new SimpleExtXYModel();
        List<GraficaVentasEfectivoMd> data = dataBase.Grafica1(Fecha_inicial);
        String categoria = "";

        int i = 0;    
        
        for (GraficaVentasEfectivoMd valor : data) {
            if (categoria.equals("")) {
                categoria = "'" + valor.getTipo()+ "'";
            } else {
                categoria = categoria + "," + "'" + valor.getTipo()+ "'";
            }
            model.addValue("Total de ", i, Integer.parseInt(valor.getSumatipo()));
              i++;
        }
        grafica1.setxAxisOptions("{"
                + "categories: ["
                + categoria
                + "]"
                //+ "colors: ['#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4']"
                + "}");

        grafica1.setModel(model);

    }

   
    //GRAFICA DE PAI #2 en DAL
    public void Grafica2(String fecha_inicial) throws SQLException {
        GraficaVentasEfectivoDal dataBase = new GraficaVentasEfectivoDal();

        grafica2.setTitle("PORCENTAJE DE PRODUCTOS CONSUMIDOS");
        grafica2.setSubTitle("");
        grafica2.setType("pie");
        grafica2.setTooltipFormatter("function formatTooltip(obj){"
                + "return obj.key + '<br />Total: <b>'+obj.y+'</b>'"
                + "}");

        grafica2.setPlotOptions("{"
                + "pie:{"
                + "allowPointSelect: true,"
                + "cursor: 'pointer',"
                + "dataLabels: {"
                + "enabled: true, "
                + "color: '#000000',"
                + "connectorColor: '#000000',"
                + "formatter: function() {"
                + "return '<b>'+ this.point.name +'</b>: '+ Highcharts.numberFormat(this.percentage,2) +' % ';"
                + "}"
                + "}"
                + "}"
                + "}");
        grafica2.setModel(dataBase.Grafica2(fecha_inicial));
    }
    
    }



