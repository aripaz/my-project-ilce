package mx.ilce.component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mx.ilce.bean.Campo;
import mx.ilce.bean.HashCampo;
import mx.ilce.handler.ExceptionHandler;

/**
 *  Clase implementada para guardar datos en un listado Hash
 * @author ccatrilef
 */
public class ListHash {

    private HashMap lista;

    /**
     * Inserta un dato a una lista Hash
     * @param dato  Dato a insertar
     * @param clave Clave que poseera en el Hash
     * @return
     */
    private boolean insertListHash(Object dato, BigDecimal clave){
        boolean bln=true;
        this.lista.put(clave, dato);
        return bln;
    }

    /**
     * Elimina un dato de una lista Hash, ubicandolo por su clave
     * @param clave Clave del objeto a eliminar
     * @return
     */
    private boolean deleteListHash(BigDecimal clave){
        this.lista.remove(clave);
        return true;
    }

    /**
     * Actualiza el contenido de un listado Hash, reemplazando el objeto ubicado
     * en la clave entregada por el nuevo objeto entregado
     * @param dato      Objeto que sera ingresado
     * @param clave     Clave del objeto a reemplazar
     * @return
     */
    private boolean updateListHash(Object dato, BigDecimal clave){
        boolean bln=true;
        return bln;
    }

    /**
     * Obtiene un objeto desde el listado hash, mediante una clave
     * @param clave     Clave del objeto a buscar
     * @return
     */
    private Object getObjectHash(BigDecimal clave){
        return this.lista.get(clave);
    }

     /**
     * Tenemos un metodo donde en una clase "nameClass", se introducen los
     * datos del objeto "hsCmp", colocandolos en los campos que correspondan,
     * segun la definicion de la clase entregada. Se necesita que la clase
     * poseea los metodos getter y setter para asociar los campos.
     * @param nameClass    Clase a la que deben introducirse los datos
     * @param hsCmp     Objeto que contiene la data estructurada que debe ser
     *                  introducida en la clase señalada
     * @return
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
            throw new ExceptionHandler(e0,this.getClass(),"Problemas para obtener el Bean individual");
        }catch(ClassNotFoundException e1){
            throw new ExceptionHandler(e1,this.getClass(),"Problemas para obtener el Bean individual");
        }catch(NoSuchMethodException e2 ){
            throw new ExceptionHandler(e2,this.getClass(),"Problemas para obtener el Bean individual");
        }catch(InstantiationException e3){
            throw new ExceptionHandler(e3,this.getClass(),"Problemas para obtener el Bean individual");
        }catch(IllegalAccessException e4){
            throw new ExceptionHandler(e4,this.getClass(),"Problemas para obtener el Bean individual");
        }catch(IllegalArgumentException e5){
            throw new ExceptionHandler(e5,this.getClass(),"Problemas para obtener el Bean individual");
        }catch(InvocationTargetException e6){
            throw new ExceptionHandler(e6,this.getClass(),"Problemas para obtener el Bean individual");
        }
        return sld;
    }

    /**
     * Tenemos un metodo donde se entrega un ArrayList, compuesta de elementos
     * que son de la clase "nameClass", en ella se introducen los datos de los
     * registros contenidos en el objeto "hsCmp", colocandolos en los campos
     * que correspondan, segun la definicion de la clase entregada. Se necesita
     * que la clase poseea los metodos getter y setter para asociar los campos.
     * @param nameClass    Clase a la que deben introducirse los datos
     * @param hsCmp     Objeto que contiene la data estructurada que debe ser
     *                  introducida en la clase señalada
     * @return
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
            throw new ExceptionHandler(e0,this.getClass(),"Problemas para obtener el Listado de Bean");
        }catch(ClassNotFoundException e1){
            throw new ExceptionHandler(e1,this.getClass(),"Problemas para obtener el Listado de Bean");
        }catch(NoSuchMethodException e2 ){
            throw new ExceptionHandler(e2,this.getClass(),"Problemas para obtener el Listado de Bean");
        }catch(InstantiationException e3){
            throw new ExceptionHandler(e3,this.getClass(),"Problemas para obtener el Listado de Bean");
        }catch(IllegalAccessException e4){
            throw new ExceptionHandler(e4,this.getClass(),"Problemas para obtener el Listado de Bean");
        }catch(IllegalArgumentException e5){
            throw new ExceptionHandler(e5,this.getClass(),"Problemas para obtener el Listado de Bean");
        }catch(InvocationTargetException e6){
            throw new ExceptionHandler(e6,this.getClass(),"Problemas para obtener el Listado de Bean");
        }
        return arr;
    }

    /**
     * Convierte un tipo String(obj) en el tipo entregado (type)
     * @param type  Tipo al cual debe ser convertirse un dato
     * @param obj   String que contiene el valor que debe ser convertido
     * @return
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
            }
        }catch (ClassNotFoundException e1){
            throw new ExceptionHandler(e1,this.getClass(),"Problemas para converti el Tipo de Campo");
        }catch (Exception e2){
            throw new ExceptionHandler(e2,this.getClass(),"Problemas para converti el Tipo de Campo");
        }
        return sld;
    }
}
