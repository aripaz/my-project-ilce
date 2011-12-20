/**
 * Desarrollado para ILCE (Instituto Latinoamericano de la Comunicación
 * Educativa) bajo el contexto del Proyecto de Migración de la Aplicación SAEP,
 * desde un esquema .NET a Java.
 * Marzo-Diciembre 2011
 * Autor: Carlos Leonel Catrilef Cea
 * Version: 1.0
 *
 * - Las licencias de los componentes y librerías utilizadas, están adjuntas en
 * el(los) archivo(s) LICENCE que corresponda(n), junto al código fuente de la
 * aplicación, tal como establecen para el uso no comercial de las mismas.
 * - Todos los elementos de la aplicación: Componentes, Módulos, Bean, Clases, etc,
 * se entienden revisadas y aprobadas solamente para esta aplicación.
 * - Sobre condiciones de uso, reproducción y distribución referirse al archivo
 * LICENCE-ILCE incluido en la raiz del proyecto.
 */
package mx.ilce.conection;

import java.util.List;
import java.util.Properties;
import mx.ilce.bean.CampoForma;
import mx.ilce.bean.DataTransfer;
import mx.ilce.bean.HashCampo;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.component.AdminFile;
import mx.ilce.component.ListHash;
import mx.ilce.handler.ExceptionHandler;

/**
 * Clase utilizada para realizar las consultas a la base de datos desde
 * la aplicación
 * @author ccatrilef
 */
public class ConEntidad {

    private Properties prop = null;
    private String query = "";
    private String queryDel = "";
    private HashCampo hashCampo = new HashCampo();
    private CampoForma campoForma = new CampoForma();
    private Bitacora bitacora;

    /**
     * Constructor básico de la clase, al crearse se cargan los datos del
     * properties
     * @throws ExceptionHandler
     */
    public ConEntidad() throws ExceptionHandler{
        try{
            this.prop = AdminFile.leerIdQuery();
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),
                                "Problemas para abrir conexion ConSession");
        }
    }

    /**
     * Obtiene el IDQUERY desde el properties de queries
     * @param key           Clave que se esta buscando
     * @return  Integer     ID de la query asociada a la clave entregada
     * @throws ExceptionHandler
     */
    public Integer getIdQuery(String key) throws ExceptionHandler{
        Integer intSld = new Integer(0);
        try{
            if (prop == null){
                prop = AdminFile.leerIdQuery();
            }
            intSld = AdminFile.getIdQuery(prop,key);
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para obtener ID QUERY desde properties");
            eh.setDataToXML("KEY", key);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return intSld;
    }

    /**
     * Método que ejecuta la inserción de los datos configurados en el objeto.
     * El resultado se deja en el objeto HashCampo del Controller.
     * El objeto DataTransfer contiene los siguientes datos:
     * (-) CampoForma: contiene los datos para la operación.
     * (-) Query: contiene la query que se ejecutara.
     * @param dataTransfer  Objeto para transferencia de datos entre capas
     * @throws ExceptionHandler
     */
    public void ingresaEntidad(DataTransfer dataTransfer) throws ExceptionHandler{
        try{
            ConQuery con = new ConQuery();
            con.setBitacora(this.getBitacora());
            HashCampo hs = con.executeInsert(dataTransfer);
            this.hashCampo = hs;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para Ingresar la Entidad");
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
    }

    /**
     * Método que ejecuta la edición de los datos configurados en el objeto.
     * El resultado se deja en el objeto HashCampo del Controller.
     * El objeto DataTransfer contiene los siguientes datos:
     * (-) CampoForma: contiene los datos para la operación.
     * (-) Query: contiene la query que se ejecutara.
     * @param dataTransfer  Objeto para transferencia de datos entre capas
     * @throws ExceptionHandler
     */
    public void editarEntidad(DataTransfer dataTransfer) throws ExceptionHandler{
        try {
            ConQuery con = new ConQuery();
            con.setBitacora(this.getBitacora());

            HashCampo hs = con.executeUpdate(dataTransfer);
            this.hashCampo = hs;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para Editar la Entidad");
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
    }

    /**
     * Método que ejecuta la eliminación de los datos configurados en el objeto.
     * El resultado se deja en el objeto HashCampo del Controller.
     * El objeto DataTransfer contiene los siguientes datos:
     * (-) CampoForma: contiene los datos para la operación.
     * (-) Query: contiene la query que se ejecutara.
     * @param dataTransfer  Objeto para transferencia de datos entre capas
     * @throws ExceptionHandler
     */
    public void eliminaEntidad(DataTransfer dataTransfer) throws ExceptionHandler{
        try {
            ConQuery con = new ConQuery();
            con.setBitacora(this.getBitacora());

            HashCampo hs = con.executeDelete(dataTransfer);
            
            this.hashCampo = hs;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para Eliminar la Entidad");
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
    }

    /**
     * Método implementado pensando en la posibilidad de ingresar una data desde
     * un formulario y los permisos asociados a los nuevos datos. NO PROBADA
     * @param dataTransfer  Objeto para transferencia de datos entre capas
     * @throws ExceptionHandler
     */
    public void ingresarDataPermisos(DataTransfer dataTransfer) throws ExceptionHandler{
        try {
            ConQuery con = new ConQuery();
            con.setBitacora(this.getBitacora());

            HashCampo hs = con.executeDeleteInsert(dataTransfer);

            this.hashCampo = hs;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para Borrar y Agregar Permisos de la Entidad");
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
    }

    /**
     * Método que obtiene la configuración de la formas a partir del ID de la
     * misma y los campos que se están buscando.
     * El objeto de DataTransfer contiene los siguientes datos:
     * (-) strData: Debe contener dos parámetros, el ID de la Forma y un
     * listado de String con los nombres de los campos que se quieren obtener. 
     * Con esto se evita traer la forma completa.
     * (-) arrVariable: Arreglo con variables que no necesariamente son obligatorias,
     * pero pueden estar definidas en el User o el ambiente de la aplicación
     * @param dataTransfer  Objeto para transferencia de datos entre capas
     * @return  List        Listado con el resultado obtenido
     * @throws ExceptionHandler
     */
    public List getListFormaByIdAndCampos(DataTransfer dataTransfer) throws ExceptionHandler{
        List lstSld=null;
        try{
            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());
            Integer idQuery = getIdQuery(AdminFile.FORMACAMPOS);
            dataTransfer.setIdQuery(idQuery);
            HashCampo hsCmp = connQ.getData(dataTransfer);
            
            if (!hsCmp.getListData().isEmpty()){
                //introducimos en el Bean los datos obtenidos
                ListHash lst = new ListHash();
                lstSld = lst.getListBean(CampoForma.class, hsCmp);
            }
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para obtener el listado de Formas por ID y Campos");
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return lstSld;
    }

    /**
     * Método que obtiene la configuración de una forma a partir del ID de la misma,
     * entregando un Listado de Bean del tipo CampoForma.
     * El objeto de DataTransfer contiene los siguientes datos: 
     * (-) strData: Arreglo con el ID de la forma a buscar.
     * (-) strWhere: Condiciones adicionales para agregar a la query.
     * (-) arrData: Arreglo de data con los parámetros de entrada que se
     * utilizara en la query.
     * (-) arrVariables: Arreglo con variables que no necesariamente son
     * obligatorias, pero pueden estar definidas en el User o el ambiente
     * de la aplicación.
     * @param   dataTransfer    Objeto para transferencia de datos entre capas
     * @return  List            Listado con el resultado obtenido
     * @throws ExceptionHandler
     */
    public List getListFormaById(DataTransfer dataTransfer) throws ExceptionHandler{
        List lstSld=null;
        try{
            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());

            Integer idQuery = getIdQuery(AdminFile.FORMA);
            dataTransfer.setIdQuery(idQuery);
            HashCampo hsCmp = connQ.getDataWithWhereAndData(dataTransfer);

            if (!hsCmp.getListData().isEmpty()){
                //introducimos en el Bean los datos obtenidos
                ListHash lst = new ListHash();
                lstSld = lst.getListBean(CampoForma.class, hsCmp);
            }
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para obtener el listado de Formas por ID");
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return lstSld;
    }

    /**
     * Método que obtiene la data generada a partir de la Query entregada y los
     * parámetros de entrada que deben utilizarse con la Query.
     * El objeto de DataTransfer contiene los siguientes datos:
     * (-) strQuery: Query que se desea ejecutar.
     * (-) strData: Arreglo con los datos de entrada para la query. 
     * (-) arrVariables: Arreglo con variables que no necesariamente son obligatorias,
     * pero pueden estar definidas en el User o el ambiente de la aplicación.
     * @param   dataTransfer    Objeto para transferencia de datos entre capas
     * @return  HashCampo       Objeto con el resultado obtenido
     * @throws ExceptionHandler
     */
    public HashCampo getDataByQuery(DataTransfer dataTransfer) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            ConQuery con = new ConQuery();
            con.setBitacora(this.getBitacora());

            hsCmp = con.getDataByQuery(dataTransfer);
            
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para obtener Datos por una QUERY");
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return hsCmp;
    }

    /**
     * Método que entrega el campo PK de una Tabla
     * @param tabla     Nombre de la tabla donde se buscara el PK
     * @return  String  Nombre del campo PK
     * @throws ExceptionHandler
     */
    public String getCampoPK(String tabla) throws ExceptionHandler{
        String campo = "";
        try{
            ConQuery con = new ConQuery();
            con.setBitacora(this.getBitacora());

            campo = con.getCampoPK(tabla);
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para obtener el Campo PK de una tabla");
            eh.setDataToXML("TABLA", tabla);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return campo;
    }

     /**
     * Método que entrega un objeto con la data y los campos que resultan de
     * ejecutar la query del ID entregado, junto con los parámetros respectivos.
     * El objeto de DataTransfer contiene los siguientes datos:
     * (-) IdQuery: ID de la query que se quiere ejecutar.
     * (-) strData: Arreglo con los parámetros de entrada.
     * (-) arrVariables: Arreglo con variables que no necesariamente son
     * obligatorias, pero pueden estar definidas en el User o el ambiente
     * de la aplicación.
     * @param   dataTransfer    Objeto para transferencia de datos entre capas
     * @return  HashCampo       Objeto con el resultado obtenido
     * @throws ExceptionHandler
     */
    public HashCampo getDataByIdQuery(DataTransfer dataTransfer) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());
            hsCmp = connQ.getData(dataTransfer);

        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para obtener datos por el ID QUERY");
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return hsCmp;
    }

     /**
     * Método que entrega un objeto con la data y los campos que resultan de
     * ejecutar la query del ID entregado, junto con los parámetros respectivos.
     * El objeto de DataTransfer contiene los siguientes datos:
     * (-) IdQuery: ID de la query que se quiere ejecutar.
     * (-) strData: Arreglo con los parámetros de entrada.
     * (-) strWhere: Condiciones adicionales para agregar a la query.
     * (-) arrVariables: Arreglo con variables que no necesariamente son
     * obligatorias, pero pueden estar definidas en el User o el ambiente
     * de la aplicación.
     * @param   dataTransfer    Objeto para transferencia de datos entre capas
     * @return  HashCampo       Objeto con el resultado obtenido
     * @throws ExceptionHandler
     */
    public HashCampo getDataByIdQueryAndWhere(DataTransfer dataTransfer) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());
            hsCmp = connQ.getDataWithWhere(dataTransfer);

        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para obtener datos por el ID QUERY y WHERE");
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return hsCmp;
    }

    /**
     * Método que entrega un objeto con la data y los campos que resultan de
     * ejecutar la query del ID entregado, junto con los parámetros respectivos.
     * El objeto de DataTransfer contiene los siguientes datos: 
     * (-) IdQuery: ID de la query que se quiere ejecutar. 
     * (-) strData: Arreglo con los parámetros de entrada.
     * (-) strWhere: Condiciones adicionales para agregar a la query. 
     * (-) arrVariables: Arreglo con variables que no necesariamente son
     * obligatorias, pero pueden estar definidas en el User o el ambiente
     * de la aplicación.
     * @param   dataTransfer    Objeto para transferencia de datos entre capas
     * @return  HashCampo       Objeto con el resultado obtenido
     * @throws ExceptionHandler
     */
    public HashCampo getDataByIdQueryAndWhereAndData(DataTransfer dataTransfer)
            throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());
            hsCmp = connQ.getDataWithWhereAndData(dataTransfer);

        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                             "Problemas para obtener datos por el ID QUERY, WHERE y DATA");
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return hsCmp;
    }

    /**
     * Obtiene la query de borrado
     * @return  String  Query de borrado
     */
    public String getQueryDel() {
        return queryDel;
    }

    /**
     * Asigna la query de borrado
     * @param queryDel
     */
    public void setQueryDel(String queryDel) {
        this.queryDel = queryDel;
    }

    /**
     * Obtiene la query contenida en el objeto
     * @return  String  Query de borrado
     */
    public String getQuery() {
        return query;
    }

    /**
     * Asigna la query contenida en el objeto
     * @param query     Texto de la query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Obtiene el objeto de tipo HashCampo
     * @return  HashCampo   Objeto solicitado
     */
    public HashCampo getHashCampo() {
        return hashCampo;
    }

    /**
     * Asigna el objeto de tipo HashCampo
     * @param hashCampo     HashCampo a asignar
     */
    public void setHashCampo(HashCampo hashCampo) {
        this.hashCampo = hashCampo;
    }

    /**
     * Obtiene el objeto CampoForma
     * @return  CampoForma  Objeto solicitado
     */
    public CampoForma getCampoForma() {
        return campoForma;
    }

    /**
     * Asigna el CampoForma
     * @param campoForma    CampoForma a asignar
     */
    public void setCampoForma(CampoForma campoForma) {
        this.campoForma = campoForma;
    }

    /**
     * Obtiene el objeto Bitacora
     * @return  Bitacora    Objeto solicitado
     */
    public Bitacora getBitacora() {
        return bitacora;
    }

    /**
     * Asigna el objeto Bitacora
     * @param bitacora  Objeto a asignar
     */
    public void setBitacora(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public void obtieneEntidad(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO, al no requerirse de momento
     */
    public List obtieneMenu(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
