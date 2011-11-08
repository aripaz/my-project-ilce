package mx.ilce.component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mx.ilce.bean.Campo;
import mx.ilce.bean.HashCampo;
import mx.ilce.handler.ExceptionHandler;

/**
 * Clase implementada para guardar datos en un listado Hash
 * @author ccatrilef
 */
public class ListHash {

    private HashMap lista;

     /**
     * Método donde en una clase "nameClass", se introducen los
     * datos del objeto "hsCmp", colocandolos en los campos que correspondan,
     * según la definición de la clase entregada. Se necesita que la clase
     * poseea los métodos getter y setter para asociar los campos.
     * @param nameClass    Clase a la que deben introducirse los datos
     * @param hsCmp     Objeto que contiene la data estructurada que debe ser
     *                  introducida en la clase señalada
     * @return  Object  Objeto convertido a la clase que le corresponde
     * @throws ExceptionHandler
     */
    public Object getBean(Class nameClass, HashCampo hsCmp)throws ExceptionHandler{
        Object sld = null;
        try{
            Class clase = Class.forName(nameClass.getName());
            Object objeto=null;
            Campo cmp;
            Method[] met = clase.getDeclaredMethods();
            //Vemos cuantos datos tenemos
            if (met.length>0){
                objeto = clase.newInstance();
                for(int i=0;i<met.length;i++){  //get
                    //buscamos solo los metodos set
                    String strIni = met[i].getName().substring(0,3);
                    if ("set".equals(strIni)){
                        // obtenemos el campo por el nombre del metodo a aplicar
                        cmp = hsCmp.getCampoByName(met[i].getName().substring(3).toUpperCase());
                        if (cmp != null){
                            //entregamos el tipo de datos Java que le corresponde al dato
                            //asociado al metodo
                            Class tipoDato = Class.forName(cmp.getTypeDataAPL());
                            //obtenemos el metodo en un objeto
                            Method mtd = clase.getMethod(met[i].getName(), tipoDato);
                            //obtenemos el listado de datos
                            HashMap data = hsCmp.getListData();
                            Object[] paramDato = new Object[1];
                            if (hsCmp.getLengthData()>0){
                                //obtenemos el listado de datos del registro
                                List lstData =(List) data.get(0);
                                boolean bool = true;
                                for (int j=0;j<lstData.size() && bool;j++){
                                    Campo obj = (Campo)lstData.get(j);
                                    if (obj.getNombre().equals(mtd.getName().substring(3).toUpperCase()) ){
                                        paramDato[0] = getTypeValueCampo(tipoDato, obj.getValor());
                                        mtd.invoke(objeto, paramDato);
                                        bool = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            sld = objeto;
        }catch(NullPointerException e0){
            ExceptionHandler eh = new ExceptionHandler(e0,this.getClass(),
                             "Problemas para obtener el Bean individual");
            eh.setDataToXML("NOMBRE DE LA CLASE",nameClass.getName());
            eh.setDataToXML(hsCmp);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(ClassNotFoundException e1){
            ExceptionHandler eh = new ExceptionHandler(e1,this.getClass(),
                             "Problemas para obtener el Bean individual");
            eh.setDataToXML("NOMBRE DE LA CLASE",nameClass.getName());
            eh.setDataToXML(hsCmp);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(NoSuchMethodException e2 ){
            ExceptionHandler eh = new ExceptionHandler(e2,this.getClass(),
                             "Problemas para obtener el Bean individual");
            eh.setDataToXML("NOMBRE DE LA CLASE",nameClass.getName());
            eh.setDataToXML(hsCmp);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(InstantiationException e3){
            ExceptionHandler eh = new ExceptionHandler(e3,this.getClass(),
                             "Problemas para obtener el Bean individual");
            eh.setDataToXML("NOMBRE DE LA CLASE",nameClass.getName());
            eh.setDataToXML(hsCmp);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(IllegalAccessException e4){
            ExceptionHandler eh = new ExceptionHandler(e4,this.getClass(),
                             "Problemas para obtener el Bean individual");
            eh.setDataToXML("NOMBRE DE LA CLASE",nameClass.getName());
            eh.setDataToXML(hsCmp);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(IllegalArgumentException e5){
            ExceptionHandler eh = new ExceptionHandler(e5,this.getClass(),
                             "Problemas para obtener el Bean individual");
            eh.setDataToXML("NOMBRE DE LA CLASE",nameClass.getName());
            eh.setDataToXML(hsCmp);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(InvocationTargetException e6){
            ExceptionHandler eh = new ExceptionHandler(e6,this.getClass(),
                             "Problemas para obtener el Bean individual");
            eh.setDataToXML("NOMBRE DE LA CLASE",nameClass.getName());
            eh.setDataToXML(hsCmp);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return sld;
    }

    /**
     * Método donde se entrega un ArrayList, compuesta de elementos
     * que son de la clase "nameClass", en ella se introducen los datos de los
     * registros contenidos en el objeto "hsCmp", colocandolos en los campos
     * que correspondan, según la definición de la clase entregada. Se necesita
     * que la clase poseea los métodos getter y setter para asociar los campos.
     * @param nameClass    Clase a la que deben introducirse los datos
     * @param hsCmp     Objeto que contiene la data estructurada que debe ser
     *                  introducida en la clase señalada
     * @return  ArrayList   Listado con los objetos convertidos a la clase que
     * le corresponde
     * @throws ExceptionHandler
     */
    public ArrayList getListBean(Class nameClass, HashCampo hsCmp) throws ExceptionHandler{
        ArrayList arr = new ArrayList();
        try{
            Class clase = Class.forName(nameClass.getName());
            Object objeto=null;
            Campo cmp;
            Method[] met = clase.getDeclaredMethods();
            //Vemos cuantos datos tenemos
            if (met.length>0){
                //obtenemos el listado de datos
                HashMap data = hsCmp.getListData();
                for (int lData=0;lData<hsCmp.getLengthData();lData++){
                    objeto = clase.newInstance();
                    for(int i=0;i<met.length;i++){  //get
                        //buscamos solo los metodos set
                        String strIni = met[i].getName().substring(0,3);
                        if ("set".equals(strIni)){
                            // obtenemos el campo por el nombre del metodo a aplicar
                            cmp = hsCmp.getCampoByName(met[i].getName().substring(3).toUpperCase());
                            if (cmp != null){
                                //entregamos el tipo de datos Java que le corresponde al dato
                                //asociado al metodo
                                Class tipoDato = Class.forName(cmp.getTypeDataAPL());
                                if (tipoDato.getSimpleName().equals("Text")){
                                    tipoDato = String.class;
                                }
                                if (tipoDato.getSimpleName().equals("BIT")){
                                    tipoDato = Integer.class;
                                }
                                //obtenemos el metodo en un objeto
                                Method mtd = clase.getMethod(met[i].getName(), tipoDato);
                                Object[] paramDato = new Object[1];
                                if (hsCmp.getLengthData()>0){
                                    //obtenemos el listado de datos del registro
                                    List lstData =(List) data.get(lData);
                                    boolean bool = true;
                                    for (int j=0;j<lstData.size() && bool;j++){
                                        Campo obj = (Campo)lstData.get(j);
                                        if (obj.getNombre().equals(mtd.getName().substring(3).toUpperCase()) ){
                                            paramDato[0] = getTypeValueCampo(tipoDato, obj.getValor());
                                            mtd.invoke(objeto, paramDato);
                                            bool = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    arr.add(objeto);
                }
            }
        }catch(NullPointerException e0){
            ExceptionHandler eh = new ExceptionHandler(e0,this.getClass(),
                             "Problemas para obtener el Listado de Bean");
            eh.setDataToXML("NOMBRE DE LA CLASE",nameClass.getName());
            eh.setDataToXML(hsCmp);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(ClassNotFoundException e1){
            ExceptionHandler eh = new ExceptionHandler(e1,this.getClass(),
                             "Problemas para obtener el Listado de Bean");
            eh.setDataToXML("NOMBRE DE LA CLASE",nameClass.getName());
            eh.setDataToXML(hsCmp);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(NoSuchMethodException e2 ){
            ExceptionHandler eh = new ExceptionHandler(e2,this.getClass(),
                             "Problemas para obtener el Listado de Bean");
            eh.setDataToXML("NOMBRE DE LA CLASE",nameClass.getName());
            eh.setDataToXML(hsCmp);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(InstantiationException e3){
            ExceptionHandler eh = new ExceptionHandler(e3,this.getClass(),
                             "Problemas para obtener el Listado de Bean");
            eh.setDataToXML("NOMBRE DE LA CLASE",nameClass.getName());
            eh.setDataToXML(hsCmp);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(IllegalAccessException e4){
            ExceptionHandler eh = new ExceptionHandler(e4,this.getClass(),
                             "Problemas para obtener el Listado de Bean");
            eh.setDataToXML("NOMBRE DE LA CLASE",nameClass.getName());
            eh.setDataToXML(hsCmp);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(IllegalArgumentException e5){
            ExceptionHandler eh = new ExceptionHandler(e5,this.getClass(),
                             "Problemas para obtener el Listado de Bean");
            eh.setDataToXML("NOMBRE DE LA CLASE",nameClass.getName());
            eh.setDataToXML(hsCmp);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(InvocationTargetException e6){
            ExceptionHandler eh = new ExceptionHandler(e6,this.getClass(),
                             "Problemas para obtener el Listado de Bean");
            eh.setDataToXML("NOMBRE DE LA CLASE",nameClass.getName());
            eh.setDataToXML(hsCmp);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return arr;
    }

    /**
     * Método que convierte un tipo String(obj) en el tipo entregado (type)
     * @param type  Tipo al cual debe ser convertirse un dato
     * @param obj   String que contiene el valor que debe ser convertido
     * @return  Object  Objeto convertido a la clase que le corresponde
     * @throws ExceptionHandler
     */
    private Object getTypeValueCampo(Class type, String obj) throws ExceptionHandler{
        Object sld = null;
        try {
            sld = Class.forName(type.getName()) ;
            if (type.getSimpleName().equals("String")){
                sld = String.valueOf(obj);
                if ("null".equals(sld)){
                    sld = null;
                }
            }else if(type.getSimpleName().equals("Integer") ){
                if ((obj != null) && (!"null".equals(obj))){
                    sld = Integer.valueOf(obj);
                }else{
                    sld = null;
                }
            }else if(type.getSimpleName().equals("Date") ){
                if ((obj != null) && (!"null".equals(obj))){
                    sld = Date.valueOf(obj) ;
                }
            }else {
                sld = String.valueOf(obj);
                if ("null".equals(sld)){
                    sld = null;
                }
            }
        }catch (ClassNotFoundException e1){
            ExceptionHandler eh = new ExceptionHandler(e1,this.getClass(),
                             "Problemas para convertir el Tipo de Campo");
            eh.setDataToXML("TIPO", type.getName());
            eh.setDataToXML("OBJETO", obj);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch (Exception e2){
            ExceptionHandler eh = new ExceptionHandler(e2,this.getClass(),
                             "Problemas para convertir el Tipo de Campo");
            eh.setDataToXML("TIPO", type.getName());
            eh.setDataToXML("OBJETO", obj);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return sld;
    }
}
