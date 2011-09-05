package mx.ilce.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Clase implementada para entregar las configuraciones de las secciones fijas
 * que forman un reporte: LAYOUT, PAGEMASTER, REGIONBODY, PAGESEQUENCE,
 * TITLE, PAGETITLE
 * @author ccatrilef
 */
class SectionReport {

    private HashMap configReport;

    /**
     * Obtiene la configuracion general que posee un reporte
     * @return
     */
    public HashMap getConfigReport() {
        return configReport;
    }

    /**
     * Asigna la configuracion general que posee un reporte
     * @param configReport
     */
    public void setConfigReport(HashMap configReport) {
        this.configReport = configReport;
    }

    /**
     * Entrega la configuracion de la seccion PAGEMASTER de un reporte
     * @return
     */
    public ArrayList getPageMaster(){
        ArrayList sld = new ArrayList();
        Config conf = (Config) this.getConfigReport().get("pageMaster");
        if(conf !=null){
            List lst = conf.getListConfig();
            if (lst!=null){
                Iterator it = lst.iterator();
                while (it.hasNext()){
                    Config confLst = (Config) it.next();
                    String[] str = new String[2];
                    str[0]=confLst.getName();
                    str[1]=confLst.getConfig();
                    sld.add(str);
                }
            }
        }
        return sld;
    }

    /**
     * Entrega la configuracion de la seccion PAGESECUENCE de un reporte
     * @return
     */
    public ArrayList getPageSecuence(){
        ArrayList sld = new ArrayList();
        Config conf = (Config) this.getConfigReport().get("pageSequence");
        if(conf !=null){
            List lst = conf.getListConfig();
            if (lst!=null){
                Iterator it = lst.iterator();
                while (it.hasNext()){
                    Config confLst = (Config) it.next();
                    String[] str = new String[2];
                    str[0]=confLst.getName();
                    str[1]=confLst.getConfig();
                    sld.add(str);
                }
            }
        }
        return sld;
    }

    /**
     * Entrega la configuracion de la seccion FLOW de un reporte
     * @return
     */
    public ArrayList getFlow(){
        ArrayList sld = new ArrayList();
        Config conf = (Config) this.getConfigReport().get("flow");
        if(conf !=null){
            List lst = conf.getListConfig();
            if (lst!=null){
                Iterator it = lst.iterator();
                while (it.hasNext()){
                    Config confLst = (Config) it.next();
                    String[] str = new String[2];
                    str[0]=confLst.getName();
                    str[1]=confLst.getConfig();
                    sld.add(str);
                }
            }
        }
        return sld;
    }

    /**
     * * Entrega la configuracion de la seccion TITLE de un reporte
     * @return
     */
    public ArrayList getTitle(){
        ArrayList sld = new ArrayList();
        Config conf = (Config) this.getConfigReport().get("title");
        if(conf !=null){
            List lst = conf.getListConfig();
            if (lst!=null){
                Iterator it = lst.iterator();
                while (it.hasNext()){
                    Config confLst = (Config) it.next();
                    String[] str = new String[2];
                    str[0]=confLst.getName();
                    str[1]=confLst.getConfig();
                    sld.add(str);
                }
            }
        }
        return sld;
    }

    /**
     * Entrega la configuracion de la seccion LAYOUT de un reporte
     * @return
     */
    public ArrayList getLayout(){
        ArrayList sld = new ArrayList();
        Config conf = (Config) this.getConfigReport().get("layout");
        if(conf !=null){
            List lst = conf.getListConfig();
            if (lst!=null){
                Iterator it = lst.iterator();
                while (it.hasNext()){
                    Config confLst = (Config) it.next();
                    String[] str = new String[2];
                    str[0]=confLst.getName();
                    str[1]=confLst.getConfig();
                    sld.add(str);
                }
            }
        }
        return sld;
    }

    /**
     * Entrega la configuracion de la seccion REGIONBODY de un reporte
     * @return
     */
    public ArrayList getRegionBody(){
        ArrayList sld = new ArrayList();
        Config conf = (Config) this.getConfigReport().get("regionBody");
        if(conf !=null){
            List lst = conf.getListConfig();
            if (lst!=null){
                Iterator it = lst.iterator();
                while (it.hasNext()){
                    Config confLst = (Config) it.next();
                    String[] str = new String[2];
                    str[0]=confLst.getName();
                    str[1]=confLst.getConfig();
                    sld.add(str);
                }
            }
        }
        return sld;
    }

    /**
     * Entrega la configuracion de la seccion PAGETITLE de un reporte
     * @return
     */
    public String getStrPageTitle(HashMap hsSection){
        String sld = "TITULO PAGINA DEFECTO";
        boolean seguir = true;
        for (int i=1;i<hsSection.size() && seguir;i++){
            Section sec = (Section) hsSection.get(Integer.valueOf(i));
            if ("PAGETITLE".equals(sec.getSequenceType())){
                sld = sec.getTextValue();
                seguir = false;
            }
        }
        return sld;
    }

    public ArrayList getPageTitle(){
        ArrayList sld = new ArrayList();
        Config conf = (Config) this.getConfigReport().get("pageTitle");
        if(conf !=null){
            List lst = conf.getListConfig();
            if (lst!=null){
                Iterator it = lst.iterator();
                while (it.hasNext()){
                    Config confLst = (Config) it.next();
                    String[] str = new String[2];
                    str[0]=confLst.getName();
                    str[1]=confLst.getConfig();
                    sld.add(str);
                }
            }
        }
        return sld;
    }
}
