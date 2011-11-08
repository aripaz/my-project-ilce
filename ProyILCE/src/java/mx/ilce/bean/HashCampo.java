package mx.ilce.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Clase implementada para el manejo de campos, el cual contendra las respuestas
 * obtenidas desde las operaciones de la Base de Datos
 * @author ccatrilef
 */
public class HashCampo implements Serializable  {

    private Integer lengthCampo;
    private Integer lengthData;
    private HashMap listData;
    private List listCampos;
    private HashMap listCampoByCod;
    private HashMap listCampoByName;
    private HashMap listAlias;
    private Object objData;
    private String pkData;
    private String strQuery;

    /**
     * Obtiene un texto con la query que se utiliza en la consulta a la 
     * Base de datos
     * @return
     */
    public String getStrQuery() {
        return strQuery;
    }

    /**
     * Obtiene un texto con la query que se utiliza en la consulta a la
     * Base de datos
     * @param strQuery
     */
    public void setStrQuery(String strQuery) {
        this.strQuery = strQuery;
    }

    /**
     * Obtiene el dato PK desde el objeto
     * @return
     */
    public String getPkData() {
        return pkData;
    }

    /**
     * Asigna el dato PK del objeto
     * @param pkData
     */
    public void setPkData(String pkData) {
        this.pkData = pkData;
    }

    /**
     * Agrega un registro (línea) al listado de Data
     * @param lst   Listado con data que se guardara en el contenedor de Data
     * @param codigo    Código con la posición que ocupara el listado
     */
    public void addListData(List lst, Integer codigo){
        this.listData.put(codigo, lst);
        setLengthData(this.listData.size());
    }

    /**
     * Agrega un conjunto de registros (varias líneas) al listado de Data
     * existente, proveniente de otro HashCampo. Si los campos no son 
     * equivalentes, se rechaza el ingreso. Si la lista es vacia, siendo los
     * campos correctos, se retorna TRUE.
     * Se asume que los registros vienen ordenados por código index de 0 a n.
     * @param hsCmp     HashCampo que contiene la nueva data a ingresar
     * @return
     */
    public boolean addListToListData(HashCampo hsCmp){
        boolean ingresado = false;
        if (equalsCampos(hsCmp.getListCampos(), this.getListCampos())){
            Integer lenData = this.getLengthData();
            //listado de datos a agregar
            HashMap hs = hsCmp.getListData();
            for (int i=0; i<hsCmp.getLengthData(); i++){
               //obtenemos la linea
               List lst = (List)hs.get(Integer.valueOf(i));
               //la agregamos al listado
               this.addListData(lst, lenData);
               lenData++;
            }
        }
        return ingresado;
    }

    /**
     * Obtenemos un Campo por medio de su código
     * @param codigo    Código del campo a buscar
     * @return
     */
    public Campo getCampoByCod(Integer codigo){
        HashMap hm = this.listCampoByCod;
        return (Campo) hm.get(codigo);
    }

    /**
     * Agrega un Campo, ordenandolo por su código
     * @param cmp   Campo a agregar
     */
    private void addCampoByCod(Campo cmp){
        this.listCampoByCod.put(cmp.getCodigo(), cmp);
    }

    /**
     * Obtenemos un Campo por medio de su alias
     * @param alias     Alias del Campo a buscar
     * @return
     */
    public Campo getCampoByAlias(String alias){
        HashMap hm = this.listAlias;
        Integer codigo = (Integer) hm.get(alias);
        return (Campo) listCampoByCod.get(codigo);
    }

    /**
     * Guarda el código de un Campo, ordenandolo por el alias
     * @param cmp   Campo a agregar
     */
    private void addCampoByAlias(Campo cmp){
        this.listAlias.put(cmp.getAlias(), cmp.getCodigo());
    }

    /**
     * Obtiene un Campo por su nombre
     * @param name      Nombre del Campo a buscar
     * @return
     */
    public Campo getCampoByName(String name){
        HashMap hm = this.listCampoByName;
        Integer codigo = (Integer) hm.get(name.toUpperCase());
        return (Campo) listCampoByCod.get(codigo);
    }

    /**
     * Guarda el código de un campo ordenandolo por su nombre
     * @param cmp   Campo a agregar
     */
    private void addCampoByName(Campo cmp){
        this.listCampoByName.put(cmp.getNombre(), cmp.getCodigo());
    }

    /**
     * Obtenemos un Campo por su nombre de Base de Datos
     * @param name      Nombre de Base de Datos del Campo a buscar
     * @return
     */
    public Campo getCampoByNameDB(String nameDB){
        HashMap hm = this.listCampoByName;
        String name = nameDB.replaceAll("_", "").toUpperCase();
        Integer codigo = (Integer) hm.get(name);
        return (Campo) listCampoByCod.get(codigo);
    }

    /**
     * Guarda un campo ordenado por alias, nombre y código.
     * @param cmp   Campo a agregar
     */
    public void addCampo(Campo cmp){
        this.listCampos.add(cmp);
        addCampoByCod(cmp);
        addCampoByName(cmp);
        if (cmp.getAlias()!=null){
            addCampoByAlias(cmp);
        }
        setLengthCampo(this.listCampos.size());
    }

    /**
     * Obtiene el número de campos que posee cada registro
     * @return
     */
    public Integer getLengthCampo() {
        return lengthCampo;
    }

    /**
     * Asigna el número de campos que posee cada registro
     * @param lengthCampo   Número de campos existentes
     */
    public void setLengthCampo(Integer lengthCampo) {
        this.lengthCampo = lengthCampo;
    }

    /**
     * Obtiene el número de registros de un listado
     * @return
     */
    public Integer getLengthData() {
        return lengthData;
    }

    /**
     * Asigna el número de registros que posee un listado
     * @param lengthData    Número de registros
     */
    public void setLengthData(Integer lengthData) {
        this.lengthData = lengthData;
    }

    /**
     * Obtiene el listado con los registros de la data recuperada en una query
     * @return
     */
    public HashMap getListData() {
        return listData;
    }

    /**
     * Asigna un listado con los registros recuperados por medio de una query
     * @param listData  Listado con los registros
     */
    public void setListData(HashMap listData) {
        this.listData = listData;
    }

    /**
     * Entrega el listado de nombres de campo que se usaron para obtener un
     * listado de registros de una query
     * @return
     */
    public List getListCampos() {
        return listCampos;
    }

    /**
     * Asigna un listado con los nombres de campo que se usaron para obtener un
     * listado de registros mediante una query
     * @param listCampos    Listado con los campos
     */
    public void setListCampos(List listCampos) {
        this.listCampos = listCampos;
        if (!listCampos.isEmpty()){
            Iterator it = listCampos.iterator();
            while (it.hasNext()){
                Campo cmp = (Campo) it.next();
                addCampoByCod(cmp);
                addCampoByName(cmp);
                if (cmp.getAlias()!=null){
                    addCampoByAlias(cmp);
                }
                setLengthCampo(this.listCampos.size());
            }
        }
    }

    /**
     * Obtiene un dato del tipo Object, se usara para anexar algún dato de clase
     * indefinida, además del resultado obtenido desde la query que se invocó.
     * El método que la utilice debe indicar que clase es la que contiene en
     * su descripción
     * @return
     */
    public Object getObjData() {
        return objData;
    }

    /**
     * Asigna un dato del tipo Object, se usara para anexar algún dato de clase
     * indefinida, además del resultado obtenido desde la query que se invocó.
     * El método que la utilice debe indicar que clase es la que contiene en
     * su descripción
     * @param objData   Dato Object cualquiera que se desea asignar
     */
    public void setObjData(Object objData) {
        this.objData = objData;
    }

    /**
     * Constructor de la clase, donde se le inicializan los distintos elementos
     * que contiene la clase, su contenido depende de la query que se ejecutó
     * para su llenado
     * @param lengthCampo   Cantidad de campos que posee la query
     * @param lengthData    Cantidad de registros obtenidos por la query
     * @param listData      Registros obtenidos mediante la query
     * @param listCampos    Campos que posee la query
     * @param listCampoByCod    Campos que posee la query, ordenados por codigo
     * @param listCampoByName   Campos que posee la query, ordenados por nombre
     * @param listAlias     Alias de los campos obtenidos por la query,
     * generalmente se usaran para mostrarlo como titulo o encabezado en las
     * paginas
     */
    public HashCampo(Integer lengthCampo, Integer lengthData, HashMap listData, 
            List listCampos, HashMap listCampoByCod, HashMap listCampoByName,
            HashMap listAlias) {
        this.lengthCampo = lengthCampo;
        this.lengthData = lengthData;
        this.listData = listData;
        this.listCampos = listCampos;
        this.listCampoByCod = listCampoByCod;
        this.listCampoByName = listCampoByName;
        this.listAlias = listAlias;
    }

    /**
     * Constructor de la clase el cual inicializa los valores de los campos
     * utilizados, para indicar que no se poseen datos.
     */
    public HashCampo() {
        this.lengthCampo = Integer.valueOf(0);
        this.lengthData = Integer.valueOf(0);
        this.listData = new HashMap();
        this.listCampos = new ArrayList();
        this.listCampoByCod = new HashMap();
        this.listCampoByName = new HashMap();
        this.listAlias = new HashMap();
    }

    /**
     * Compara si dos listas de campos son iguales, retorna TRUE si los son,
     * retorna FALSE si no es así
     * @param lst1
     * @param lst2
     * @return
     */
    private boolean equalsCampos(List lst1, List lst2){

        return lst1.equals(lst2);
    }

    /**
     * Método para convertir a String el contenido del Objeto. Los datos que
     * estén con valor NULL son ignorados
     * @return  String  Texto con los datos del objeto
     */
    @Override
    public String toString() {
        return "HashCampo{"
                + ((lengthCampo!=null)?"\n\tlengthCampo=" + lengthCampo:"")
                + ((lengthData!=null)?"\n\tlengthData=" + lengthData:"")
                + ((objData!=null)?"\n\tobjData=" + objData:"")
                + ((pkData!=null)?"\n\tpkData=" + pkData:"")
                + ((listData!=null)?"\n\tlistData=" + listData.toString():"")
                + ((listCampoByCod!=null)?"\n\tlistCampoByCod=" + listCampoByCod.toString():"")
                + ((listCampoByName!=null)?"\n\tlistCampoByName=" + listCampoByName.toString():"")
                + ((listAlias!=null)?"\n\tlistAlias=" + listAlias:"")
                + ((listCampos!=null)?"\n\tlistCampos=" + listCampos.toString():"")
                + "\n}";
    }
}
