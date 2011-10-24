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

    public String getStrQuery() {
        return strQuery;
    }

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
     * Agregamos un registro (linea) al listado de Data
     * @param lst   Listado con data que se guardara en el contenedor de Data
     * @param codigo    Codigo con la posicion que ocupara el listado
     */
    public void addListData(List lst, Integer codigo){
        this.listData.put(codigo, lst);
        setLengthData(this.listData.size());
    }

    /**
     * Agregamos un conjunto de registros (varias lineas) al listado de Data 
     * existente, proveniente de otro HashCampo. Si los campos no son 
     * equivalentes, se rechaza el ingreso. Si la lista es vacia, siendo los
     * campos correctos, se retorna TRUE.
     * Se asume que los registros vienen ordenados con codigo index de 0 a n.
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
     * Obtenemos un campo por medio de su codigo
     * @param codigo    Codigo del campo a buscar
     * @return
     */
    public Campo getCampoByCod(Integer codigo){
        HashMap hm = this.listCampoByCod;
        return (Campo) hm.get(codigo);
    }

    /**
     * Agregamos un Campo, ordenandolo por su codigo
     * @param cmp   Campo a agregar
     */
    private void addCampoByCod(Campo cmp){
        this.listCampoByCod.put(cmp.getCodigo(), cmp);
    }

    /**
     * Obtenemos un Campo por medio de su alias
     * @param alias     Alias del campo a buscar
     * @return
     */
    public Campo getCampoByAlias(String alias){
        HashMap hm = this.listAlias;
        Integer codigo = (Integer) hm.get(alias);
        return (Campo) listCampoByCod.get(codigo);
    }

    /**
     * Guardamos el codigo de un campo, ordenandolo por el alias
     * @param cmp   Campo a agregar
     */
    private void addCampoByAlias(Campo cmp){
        this.listAlias.put(cmp.getAlias(), cmp.getCodigo());
    }

    /**
     * Obtenemos un campo por su nombre
     * @param name      Nombre del campo a buscar
     * @return
     */
    public Campo getCampoByName(String name){
        HashMap hm = this.listCampoByName;
        Integer codigo = (Integer) hm.get(name.toUpperCase());
        return (Campo) listCampoByCod.get(codigo);
    }

    /**
     * Guardamos el codigo de un campo ordenandolo por su nombre
     * @param cmp   Campo a agregar
     */
    private void addCampoByName(Campo cmp){
        this.listCampoByName.put(cmp.getNombre(), cmp.getCodigo());
    }

    /**
     * Obtenemos un campo por su nombre de Base de Datos
     * @param name      Nombre de Base de Datos del campo a buscar
     * @return
     */
    public Campo getCampoByNameDB(String nameDB){
        HashMap hm = this.listCampoByName;
        String name = nameDB.replaceAll("_", "").toUpperCase();
        Integer codigo = (Integer) hm.get(name);
        return (Campo) listCampoByCod.get(codigo);
    }

    /**
     * guardamos un campo ordenado por Alias, Nombre y Codigo.
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
     * Obtiene el numero de campos que posee cada registro
     * @return
     */
    public Integer getLengthCampo() {
        return lengthCampo;
    }

    /**
     * Asigna el numero de campos que posee cada registro
     * @param lengthCampo   Numero de campos existentes
     */
    public void setLengthCampo(Integer lengthCampo) {
        this.lengthCampo = lengthCampo;
    }

    /**
     * Obtiene el numero de registros de un listado
     * @return
     */
    public Integer getLengthData() {
        return lengthData;
    }

    /**
     * Asigna el numero de registros que posee un listado
     * @param lengthData    Numero de registros
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
     * Obtiene un dato del tipo Object, se usara para anexar algun dato de clase
     * indefinida, ademas del resultado obtenido desde la query que se invoco.
     * El metodo que la utilice debe indicar que clase es la que contiene en
     * su descripcion
     * @return
     */
    public Object getObjData() {
        return objData;
    }

    /**
     * Asigna un dato del tipo Object, se usara para anexar algun dato de clase
     * indefinida, ademas del resultado obtenido desde la query que se invoco.
     * El metodo que la utilice debe indicar que clase es la que contiene en
     * su descripcion
     * @param objData   Dato Object cualquiera que se desea asignar
     */
    public void setObjData(Object objData) {
        this.objData = objData;
    }

    /**
     * Constructor de la clase, donde se le entregan los distintos elementos
     * que contiene la clase, su contenido depende de la query que se ejecuto
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
     * Compara si dos listas de campos son iguales, retorna true si los son,
     * retorna false si no es asi
     * @param lst1
     * @param lst2
     * @return
     */
    private boolean equalsCampos(List lst1, List lst2){

        return lst1.equals(lst2);
    }

    private String ArrayListToString(ArrayList arr){
        StringBuffer str = new StringBuffer();
        if (arr!=null){
            Iterator it = arr.iterator();
            while (it.hasNext()){
                Campo cmp = (Campo) it.next();
                str.append(cmp.toString());
            }
        }
        return str.toString();
    }

    /**
     * Metodo que lleva a un string el contenido del objeto
     * @return
     */
    @Override
    public String toString() {
        return "HashCampo{"
                + ((lengthCampo!=null)?"\n|| lengthCampo=" + lengthCampo:"")
                + ((lengthData!=null)?"\n|| lengthData=" + lengthData:"")
                + ((objData!=null)?"\n|| objData=" + objData:"")
                + ((pkData!=null)?"\n|| pkData=" + pkData:"")
                + ((listData!=null)?"\n|| listData=" + listData.toString():"")
                + ((listCampoByCod!=null)?"\n|| listCampoByCod=" + listCampoByCod.toString():"")
                + ((listCampoByName!=null)?"\n|| listCampoByName=" + listCampoByName.toString():"")
                + ((listAlias!=null)?"\n|| listAlias=" + listAlias:"")
                + ((listCampos!=null)?"\n|| listCampos=" + listCampos.toString():"")
                + "\n}";
    }
}
