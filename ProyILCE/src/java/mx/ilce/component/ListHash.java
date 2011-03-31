/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import mx.ilce.bean.Campo;
import mx.ilce.bean.HashCampo;

/**
 *
 * @author ccatrilef
 */
public class ListHash {

    private HashMap lista;

    public boolean insertListHash(Object dato, BigDecimal clave){
        boolean bln=true;
        this.lista.put(clave, dato);
        return bln;
    }

    public boolean deleteListHash(BigDecimal clave){
        boolean bln=true;
        return bln;
    }

    public boolean updateListHash(Object dato, BigDecimal clave){
        boolean bln=true;
        return bln;
    }

    public Object getObjectHash(BigDecimal clave){
        String str="";
        return str;
    }

        /**
     * Tenemos un metodo donde en una clase "nombre", se introducen los
     * datos del objeto hsCmp, colocandolos en los campos que correspondan,
     * segun la definicion del Bean
     * @param nameClass    Clase a la que deben introducirse los datos
     * @param hsCmp     Objeto que contiene la data estructurada que debe ser
     *                  introducida en la clase señalada
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     */
    public Object getBean(Class nameClass, HashCampo hsCmp){
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
            e0.printStackTrace();
        }catch(ClassNotFoundException e1){
            e1.printStackTrace();
        }catch(NoSuchMethodException e2 ){
            e2.printStackTrace();
        }catch(InstantiationException e3){
            e3.printStackTrace();
        }catch(IllegalAccessException e4){
            e4.printStackTrace();
        }catch(IllegalArgumentException e5){
            e5.printStackTrace();
        }catch(InvocationTargetException e6){
            e6.printStackTrace();
        }
        return sld;
    }

    /**
     * Convierte un tipo String(obj) en el tipo entregado (type)
     * @param type  Tipo al cual debe ser convertirse un dato
     * @param obj   String que contiene el valor que debe ser convertido
     * @return
     * @throws ClassNotFoundException
     */
    private Object getTypeValueCampo(Class type, String obj) throws ClassNotFoundException{
        Object sld = Class.forName(type.getName()) ;
        if (type.getSimpleName().equals("String")){
            sld = String.valueOf(obj);
            if ("null".equals(sld)){
                sld = null;
            }
        }else if(type.getSimpleName().equals("Integer") ){
            sld = Integer.valueOf(obj);
        }else if(type.getSimpleName().equals("Date") ){
            sld = Date.valueOf(obj) ;
        }

        return sld;
    }

}
