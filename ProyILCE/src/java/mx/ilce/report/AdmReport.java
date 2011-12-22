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
package mx.ilce.report;

import java.util.ArrayList;
import mx.ilce.handler.ExceptionHandler;
import java.util.Iterator;
import java.util.List;

/**
 * Clase administradora de la generación de Reportes
 * @author ccatrilef
 */
public class AdmReport {

    private Report report;
    private Config config;
    private Structure structure;
    private ElementStruct elementStruct;

    /**
     * Obtiene un objeto Structure
     * @return  Structure   Objeto Structure
     */
    public Structure getStructure() {
        return structure;
    }

    /**
     * Asigna un objeto Structure
     * @param structure     Objeto Structure
     */
    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    /**
     * Obtiene un Objeto ElementStruct
     * @return  ElementStruct       Objeto ElementStruct
     */
    public ElementStruct getElementStruct() {
        return elementStruct;
    }

    /**
     * Asigna un Objeto ElementStruct
     * @param elementStruct     Objeto ElementStruct
     */
    public void setElementStruct(ElementStruct elementStruct) {
        this.elementStruct = elementStruct;
    }

    /**
     * Obtiene un Objeto Config
     * @return  Config  Objeto Config
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Asigna un Objeto Config
     * @param config    Objeto Config
     */
    public void setConfig(Config config) {
        this.config = config;
    }

   /**
     * Entrega el Objeto Report
     * @return  Report  Objeto Report
     */
    public Report getReport() {
        return report;
    }

    /**
     * Asigna el Objeto Report
     * @param report    Objeto Report
     */
    public void setReport(Report report) {
        this.report = report;
    }

    /**
     * Obtiene un Objeto Report mediante su ID
     * @return  Report  Objeto Report
     * @throws ExceptionHandler
     */
    public Report getReportById()throws ExceptionHandler{
        Report repSld = null;
        String query = "select id_report, report from RPF_Report"
                + " where id_report = " + this.getReport().getIdReport();

        ConReport con = new ConReport();
        con.setQuery(query);
        repSld = con.getReport();

        return repSld;
    }

    /**
     * Obtiene un listado con los Reportes existentes
     * @return  List    Listado con los Reportes
     * @throws ExceptionHandler
     */
    public List getListReport() throws ExceptionHandler{

        String query = "select id_report, report from RPF_Report";

        ConReport con = new ConReport();
        con.setQuery(query);
        List listData = con.getListReport();

        return listData;
    }

    /**
     * Método para agregar reportes nuevos. Se construye el reporte más las
     * estructuras y sus configuraciones por defecto que debe poseer. Se
     * requiere el nombre del Reporte.
     * @return Integer  ID del reporte
     * @throws ExceptionHandler
     */
    public Integer addReport() throws ExceptionHandler{
        Integer idReport = -1;
        ConReport con = new ConReport();
        String strQuery = "insert into RPF_Report values "
                        + "('" + this.getReport().getReport().trim() + "')";

        idReport = con.executeInsert(strQuery);

        if (idReport>0){
            strQuery = "select id_typeStructure, typeStructure"
                    + " from RPF_TypeStructure ts "
                    + " where ts.main = 1";

            con.setQuery(strQuery);
            List listStructure = con.getListTypeStructure();
            Integer idData = -1;
            if (listStructure!=null){
                Iterator it = listStructure.iterator();
                int i=1;

                while (it.hasNext()){
                    Structure struct = (Structure) it.next();
                    strQuery = "insert into RPF_Structure "
                            + "(id_report, structure, id_typeStructure, orden) values "
                            + "(" + idReport
                            + ",'" + struct.getTypeStructure() + " MAIN'"
                            + "," + struct.getIdTypeStructure()
                            + "," + i++ + ")";
                    idData = con.executeInsert(strQuery);
                }
            }
            if (idData>0){
                strQuery = "insert into RPF_ConfigStructure "
                        + " (id_structure,id_typeConfig,id_typeValue,id_configValue,configValue,id_measure)"
                        + " (select id_structure, 1 , 2 , 1 ,'' , null from RPF_Structure st "
                        + "  where st.id_report = " + idReport
                        + "  and id_typeStructure = 2 "
                        + " union "
                        + "  select id_structure, 8 , 2 , 1 ,'', null from RPF_Structure st "
                        + "  where st.id_report = " + idReport
                        + "  and id_typeStructure = 4"
                        + " union "
                        + " select id_structure, 9 , 3 , 2 ,'' , null from RPF_Structure st "
                        + " where st.id_report = " + idReport
                        + " and id_typeStructure = 5)";
                idData = con.executeInsert(strQuery);
            }
        }
        return idReport;
    }

    /**
     * Método para la eliminación de reportes. Además de eliminar un reporte,
     * elimina las estructuras y configuraciones asociadas al reporte.
     * Si el ID es mayor que 0, fue una operación exitosa. Se requiere el ID del
     * Reporte
     * @return Integer      ID con el resultado de la operación
     * @throws ExceptionHandler
     */
    public Integer deleteReport() throws ExceptionHandler{
        ConReport con = new ConReport();
        String strQuery = "";
        Integer idData = -1;

        //Borrar queries
        strQuery = "delete from RPF_QueryReport qr where qr.id_structure in ("
                + " select st.id_structure from RPF_Structure st"
                + " where st.id_report = " + this.getReport().getIdReport()
                + " )";
        idData = con.executeUpdate(strQuery);

        //borrar configuración de elementos
        strQuery = "delete from RPF_ConfigElement ce "
                + " where ce.id_elementStruct in ( "
                + "   select es.id_elementStruct from RPF_ElementStruct es"
                + "   where es.id_structure in ("
                + "     select st.id_structure from RPF_Structure st"
                + "     where st.id_report = " + this.getReport().getIdReport()
                + "))";
        idData = con.executeUpdate(strQuery);

        //Borrar elementos
        strQuery = "delete from RPF_ElementStruct es "
                + " where es.id_structure in ( "
                + "   select st.id_structure from RPF_Structure st"
                + "   where st.id_report = " + this.getReport().getIdReport()
                + ")";
        idData = con.executeUpdate(strQuery);

        //borrar configuración de estructuras
        strQuery = "delete from RPF_ConfigStructure cs where cs.id_structure in ("
                + "   select st.id_structure from RPF_Structure st"
                + "   where st.id_report = " + this.getReport().getIdReport()
                + ")";
        idData = con.executeUpdate(strQuery);

        //borrar estructuras
        strQuery = "delete from RPF_Structure "
                + " where id_report = " + this.getReport().getIdReport();
        idData = con.executeUpdate(strQuery);

        //FINALMENTE DE REPORTES
        strQuery = "delete from RPF_Report "
                + " where id_report = " + this.getReport().getIdReport();
        idData = con.executeUpdate(strQuery);

        return idData;
    }

    /**
     * Método para la actualización de los datos del reporte. Se requiere el ID
     * del Reporte y el nuevo nombre
     * @return Integer  ID con el resultado de la operación
     * @throws ExceptionHandler
     */
    public Integer updateReport() throws ExceptionHandler{
        ConReport con = new ConReport();
        String strQuery = "update RPF_Report set report = "
                + "'" + this.getReport().getReport().trim() + "' "
                + " where id_report = " + this.getReport().getIdReport();
        Integer idData = con.executeUpdate(strQuery);
        return idData;
    }

    /**
     * Método para agregar una estructura. Se requiere el ID del Reporte al cual
     * se agregara la estructura
     * @return Integer  ID con el resultado de la operación
     * @throws ExceptionHandler
     */
    public Integer addStructure() throws ExceptionHandler{
        ConReport con = new ConReport();
        String strQuery = "select case when max(orden) is null then 1 "
                + " else (max(orden) + 1) end as num from RPF_Structure"
                + " where id_report = " + this.getReport().getIdReport();

        con.setQuery(strQuery);
        Integer posicion = con.getElementByQuery();

        Integer idData = -1;
        if (posicion>0){
            strQuery = "insert into RPF_Structure "
                    + "(id_report, structure, id_typeStructure, orden) values "
                    + "(" + this.getReport().getIdReport()
                    + ",'" + this.getStructure().getStructure() + "'"
                    + "," + this.getStructure().getIdTypeStructure()
                    + "," + posicion + ")";
            idData = con.executeInsert(strQuery);
        }
        return idData;
    }

    /**
     * Método para actualizar los datos de una Estructura. Se requiere el ID de
     * la estructura y se pueden actualizar el tipo y su descripción
     * @return Integer      ID con el resultado de la operación
     * @throws ExceptionHandler
     */
    public Integer updateStructure() throws ExceptionHandler{
        ConReport con = new ConReport();
        String strQuery = "update RPF_Structure "
                + " set id_typeStructure = " + this.getStructure().getIdTypeStructure()
                + " , structure = '" + this.getStructure().getStructure() + "'"
                + " where id_structure = " + this.getStructure().getIdStructure();
        Integer idData = con.executeUpdate(strQuery);
        return idData;
    }

    /**
     * Método para el borrado de una estructura. Requiere el ID de la estructura
     * @return Integer      ID con el resultado de la operación
     * @throws ExceptionHandler
     */
    public Integer deleteStructure() throws ExceptionHandler{
        ConReport con = new ConReport();
        String strQuery = "";
        Integer idData = -1;

        strQuery = "delete from RPF_QueryReport qr "
                + " where qr.id_structure = " + this.getConfig().getIdStructure();
        idData = con.executeUpdate(strQuery);

        strQuery = "delete from RPF_ConfigElement ce "
                + "where ce.id_elementStruct in ("
                + "    select es.id_elementStruct from RPF_ElementStruct es"
                + "    where es.id_structure = " + this.getConfig().getIdStructure()
                + ")";
        idData = con.executeUpdate(strQuery);

        strQuery = "delete from RPF_ElementStruct es "
                + " where es.id_structure = " + this.getConfig().getIdStructure();
        idData = con.executeUpdate(strQuery);

        strQuery = "delete from RPF_ConfigStructure cs "
                + " where cs.id_structure = " + this.getConfig().getIdStructure();
        idData = con.executeUpdate(strQuery);

        //BORRAR DE LA TABLA
        strQuery = "delete from RPF_Structure "
                + " where id_structure = " + this.getConfig().getIdStructure();
        idData = con.executeUpdate(strQuery);

        return idData;
    }

    /**
     * Método para agregar una configuración de una estructura. Se requiere el
     * ID de la estructura, el tipo de configuración, el valor de la configuración,
     * el tipo de valor de la configuración y su tipo de medida
     * @return Integer      ID con el resultado de la operación
     * @throws ExceptionHandler
     */
    public Integer addConfigStructure() throws ExceptionHandler{
        ConReport con = new ConReport();
        Integer idData = -1;
        String strQuery = "insert into RPF_ConfigStructure "
                + " (id_structure, id_typeConfig, id_typeValue "
                + ", id_configValue,configValue,id_measure) values "
                + " (" + this.getConfig().getIdStructure()
                + " ," + this.getConfig().getIdTypeConfig()
                + " ," + this.getConfig().getIdTypeValue()
                + " ," + this.getConfig().getIdConfigValue()
                + " ,'" + this.getConfig().getConfigValue() + "'"
                + " ," + this.getConfig().getIdMeasure() + ")";

        idData = con.executeInsert(strQuery);
        return idData;
    }

    /**
     * Método para la modificación de la configuración de una estructura. Se
     * requiere el ID de la configuración, se puede modificar su tipo, el valor,
     * el tipo de valor y su tipo de medida
     * @return Integer  ID con el resultado de la operación
     * @throws ExceptionHandler
     */
    public Integer updateConfigStructure() throws ExceptionHandler{
        ConReport con = new ConReport();
        String strQuery = "update RPF_ConfigStructure "
                + " set id_typeConfig = " + this.getConfig().getIdTypeConfig()
                + ", id_typeValue = " + this.getConfig().getIdTypeValue()
                + ", id_configValue = " + this.getConfig().getIdConfigValue()
                + ", configValue = '" + this.getConfig().getConfigValue() + "'"
                + ", id_measure = " + this.getConfig().getIdMeasure()
                + " where id_configStructure = " + this.getConfig().getIdConfigStructure();
        Integer idData = con.executeUpdate(strQuery);
        return idData;
    }

    /**
     * Método para eliminar una configuración de estructura
     * @return Integer  ID con el resultado de la operación
     * @throws ExceptionHandler
     */
    public Integer deleteConfigStructure() throws ExceptionHandler{
        ConReport con = new ConReport();
        String strQuery = "";
        Integer idData = -1;

        //BORRAR DE LA TABLA
        strQuery = "delete from RPF_ConfigStructure "
                + " where id_configStructure = " + this.getConfig().getIdConfigStructure();
        idData = con.executeUpdate(strQuery);

        return idData;
    }

    /**
     * Método para agregar un elemento a una estructura. Se requiere el ID de la
     * estructura
     * @return Integer  ID con el resultado de la operación
     * @throws ExceptionHandler
     */
    public Integer addElementStruct() throws ExceptionHandler{
        ConReport con = new ConReport();
        String strQuery = "select case when max(orden) is null then 1 "
                + " else (max(orden) + 1) end as num from RPF_ElementStruct"
                + " where id_structure = " + this.getElementStruct().getIdStructure();

        con.setQuery(strQuery);
        Integer posicion = con.getElementByQuery();

        Integer idData = -1;
        if (posicion>0){
            strQuery = "insert into RPF_ElementStruct "
                    + "(id_Structure, id_typeElement, valueElement, orden) values "
                    + "(" + this.getElementStruct().getIdStructure()
                    + "," + this.getElementStruct().getIdTypeElement()
                    + ",'" + this.getElementStruct().getValueElement() + "'"
                    + "," + posicion + ")";
            idData = con.executeInsert(strQuery);
        }
        return idData;
    }

    /**
     * Método para actualizar un elemento de una estructura. Se requiere el ID
     * del elemento, se pude actualizar su valor y el tipo de elemento
     * @return Integer  ID con el resultado de la operación
     * @throws ExceptionHandler
     */
    public Integer updateElementStruct() throws ExceptionHandler{
        ConReport con = new ConReport();
        String strQuery = "update RPF_ElementStruct "
                + " set id_typeElement = " + this.getElementStruct().getIdTypeElement()
                + ", valueElement = '" + this.getElementStruct().getValueElement() + "'"
                + " where id_elementStruct = " + this.getElementStruct().getIdElementStruct();
        Integer idData = con.executeUpdate(strQuery);
        return idData;
    }

    /**
     * Método para la eliminación de un elemento desde una estructura. Se requiere
     * el ID del elemento. Se elimina el elemento y su configuración
     * @return Integer  ID con el resultado de la operación
     * @throws ExceptionHandler
     */
    public Integer deleteElementStruct() throws ExceptionHandler{
        ConReport con = new ConReport();
        String strQuery = "";

        strQuery = "delete from RPF_ConfigElement "
                + " where id_elementStruct = "+ this.getElementStruct().getIdElementStruct();
        Integer idData = con.executeUpdate(strQuery);

        strQuery = "delete from RPF_ElementStruct "
                + " where id_elementStruct = " + this.getElementStruct().getIdElementStruct();
        idData = con.executeUpdate(strQuery);
        return idData;
    }

    /**
     * Método para agregar la configuración de un elemento. Se requiere el ID del
     * elemento, el tipo de configuración, el tipo de valor, el valor y el tipo
     * de medida
     * @return Integer  ID con el resultado de la operación
     * @throws ExceptionHandler
     */
    public Integer addConfigElement() throws ExceptionHandler{
        ConReport con = new ConReport();
        Integer idData = -1;
        String strQuery = "insert into RPF_ConfigElement "
                + " (id_elementStruct, id_typeConfig, id_typeValue "
                + ", id_configValue,configValue,id_measure) values "
                + " (" + this.getElementStruct().getIdElementStruct()
                + " ," + this.getElementStruct().getIdTypeConfig()
                + " ," + this.getElementStruct().getIdTypeValue()
                + " ," + this.getElementStruct().getIdConfigValue()
                + " ,'" 
                + ((this.getElementStruct().getConfigValue()==null)?"":this.getElementStruct().getConfigValue())
                + "'"
                + " ," + this.getElementStruct().getIdMeasure() + ")";

        idData = con.executeInsert(strQuery);
        return idData;
    }

    /**
     * Método para la actualización de una configuración de estructura. Se
     * requiere del ID de configuración, se pueden actualizar el tipo de
     * configuración, el tipo de valor, el valor, el tipo de medida.
     * @return  Integer  ID con el resultado de la operación
     * @throws ExceptionHandler
     */
    public Integer updateConfigElement() throws ExceptionHandler{
        ConReport con = new ConReport();
        String strQuery = "update RPF_ConfigElement "
                + " set id_typeConfig = " + this.getElementStruct().getIdTypeConfig()
                + ", id_typeValue = " + this.getElementStruct().getIdTypeValue()
                + ", id_configValue = " + this.getElementStruct().getIdConfigValue()
                + ", configValue = '" + this.getElementStruct().getConfigValue() + "' "
                + ", id_measure = " + this.getElementStruct().getIdMeasure()
                + " where id_configElement = " + this.getElementStruct().getIdConfigElement();
        Integer idData = con.executeUpdate(strQuery);
        return idData;
    }

    /**
     * Método para eliminar una configuración de elemento. Se requiere el ID de
     * la configuración
     * @return  Integer  ID con el resultado de la operación
     * @throws ExceptionHandler
     */
    public Integer deleteConfigElement() throws ExceptionHandler{
        ConReport con = new ConReport();
        String strQuery = "delete from RPF_ConfigElement "
                + " where id_configElement = " + this.getElementStruct().getIdConfigElement();
        Integer idData = con.executeUpdate(strQuery);
        return idData;
    }

    /**
     * Método para agregar una query a una estructura. Se requiere el ID de la
     * estructura, el texto de la query, si es externa o no.
     * @return Integer  ID con el resultado de la operación
     * @throws ExceptionHandler
     */
    public Integer addQueryStructure() throws ExceptionHandler{
        ConReport con = new ConReport();
        Integer idData = -1;
        String strQuery = "insert into RPF_QueryReport "
                + " (id_structure, query , isExtern) values "
                + " (" + this.getConfig().getIdStructure()
                + " ,'" + this.getConfig().getQuery() + "'"
                + " ," + this.getConfig().getIsExtern()
                + ")";

        idData = con.executeInsert(strQuery);
        return idData;
    }

    /**
     * Método para actualizar una query de estructura. Se requiere el ID de la
     * query de estructura.
     * @return Integer  ID con el resultado de la operación
     * @throws ExceptionHandler
     */
    public Integer updateQueryStructure() throws ExceptionHandler{
        ConReport con = new ConReport();
        String strQuery = "update RPF_QueryReport "
                + " set query = '" + this.getConfig().getQuery() + "'"
                + ", isExtern = " + this.getConfig().getIsExtern()
                + " where id_queryReport = " + this.getConfig().getIdQuery();
        Integer idData = con.executeUpdate(strQuery);
        return idData;
    }

    /**
     * Método para la eliminación de una query de estructura. Se requiere el ID
     * de la query de estructura
     * @return Integer  ID con el resultado de la operación
     * @throws ExceptionHandler
     */
    public Integer deleteQueryStructure() throws ExceptionHandler{
        ConReport con = new ConReport();
        String strQuery = "delete from RPF_QueryReport "
                + " where id_queryReport = " + this.getConfig().getIdQuery();
        Integer idData = con.executeUpdate(strQuery);
        return idData;
    }

    /**
     * Método que entrega los datos de una estructura. Se requiere el ID de la estructura
     * @return  Config  Objeto Config con la estructura
     * @throws ExceptionHandler
     */
    public Structure getStructureData() throws ExceptionHandler{

        String query = "select st.id_structure , ts.id_typeStructure "
                + " ,st.structure, ts.typeStructure, ts.main "
                + " from RPF_Structure st , RPF_TypeStructure ts "
                + " where st.id_typeStructure = ts.id_typeStructure"
                + " and st.id_structure = " + this.getStructure().getIdStructure();

        ConReport con = new ConReport();
        con.setQuery(query);
        Structure structData = con.getStructure();

        return structData;
    }

    /**
     * Método que entrega el listado de estructuras de un Reporte. Requiere el
     * ID del reporte
     * @return List Listado de Objetos Config con las estructuras
     * @throws ExceptionHandler
     */
    public List getListStructure() throws ExceptionHandler{

        String query = "select st.id_structure , st.structure, "
                + " ts.typeStructure, ts.main , st.orden"
                + " from RPF_Structure st , RPF_TypeStructure ts"
                + " where st.id_typeStructure = ts.id_typeStructure"
                + " and st.id_report = " + this.getReport().getIdReport()
                + " and ts.id_typeStructure not in (1,2,3,4,5) "
                + " order by st.orden, st.structure";

        ConReport con = new ConReport();
        con.setQuery(query);
        List listData = con.getListStructure();

        return listData;
    }

    /**
     * Método que entrega el listado con los tipos de estructura. Solo trae los
     * tipos que no son del tipo tabla y no son los principales (por defecto).
     * @return List Listado de Objetos COnfig con los tipos de estructura
     * @throws ExceptionHandler
     */
    public List getListTypeStructure() throws ExceptionHandler{

        String query = "select id_typeStructure, typeStructure "
                + " from RPF_TypeStructure "
                + " where main = 0 "
                + " and   isTable = 0";

        ConReport con = new ConReport();
        con.setQuery(query);
        List listData = con.getListTypeStructure();

        return listData;
    }

    /**
     * Método que entrega un Elemento de una estructura mediante el ID del
     * elemento. Se requiere el ID del elemento a buscar
     * @return ElementStruct    Objeto ElementStruct
     * @throws ExceptionHandler
     */
    public ElementStruct getElementStructById() throws ExceptionHandler{
        String query = "select el.id_elementStruct , el.id_typeElement "
                + ", el.orden , el.valueElement , te.typeElement"
                + " from RPF_ElementStruct el, RPF_TypeElement te"
                + " where el.id_elementStruct = " + this.getElementStruct().getIdElementStruct()
                + " and el.id_typeElement = te.id_typeElement";

        ConReport con = new ConReport();
        con.setQuery(query);
        ElementStruct data = con.getElementStruct();

        return data;
    }

    /**
     * Método que entrega el listado de elementos asociados a una estructura. Se
     * requiere el ID de la estructura
     * @return List     Listado de Objetos ElementStruct
     * @throws ExceptionHandler
     */
    public List getListElement() throws ExceptionHandler{
        String query = "select el.id_elementStruct , el.id_typeElement "
                + ", el.orden , el.valueElement , te.typeElement"
                + " from RPF_ElementStruct el, RPF_TypeElement te"
                + " where el.id_structure = " + this.getElementStruct().getIdStructure()
                + " and el.id_typeElement = te.id_typeElement";

        ConReport con = new ConReport();
        con.setQuery(query);
        List listData = con.getListElement();

        return listData;
    }

    /**
     * Método que entrega el listado con los tipos de elemento existentes
     * @return List     Listado con objetos ElementStruct
     * @throws ExceptionHandler
     */
    public List getListTypeElement() throws ExceptionHandler{

        String query = "select te.id_typeElement , te.typeElement "
                + " from RPF_TypeElement te,RPF_TypeElemTypeStruct ts "
                + " where ts.id_typeElement = te.id_typeElement"
                + " and ts.id_typeStructure = " + this.getStructure().getIdTypeStructure();

        ConReport con = new ConReport();
        con.setQuery(query);
        List listData = con.getListTypeElement();

        return listData;
    }

    /**
     * Método que entrega el listado de configuraciones que posee un elemento
     * de estructura. Se requiere el ID del elemento.
     * @return List     Listado con Objetos ElementStruct
     * @throws ExceptionHandler
     */
    public List getListConfigElement() throws ExceptionHandler{
        String query = "select id_configElement, typeConfig, typeValue "
                + ", ConfigValue + valor + medida as configValue from ("
                + "select ce.id_configElement, tc.typeConfig "
                + ", tcv.typeValue, cv.ConfigValue "
                + ", ce.configValue as valor , '' as medida "
                + "from RPF_ConfigElement ce , RPF_TypeConfig tc "
                + ", RPF_TypeValue tcv, RPF_ConfigValue cv "
                + "where ce.id_elementStruct = " + this.getElementStruct().getIdElementStruct()
                + "and ce.id_typeConfig = tc.id_typeConfig "
                + "and ce.id_typeValue = tcv.id_typeValue "
                + "and ce.id_configValue = cv.id_configValue "
                + "and ce.id_measure is null "
                + "union "
                + "select ce.id_configElement, tc.typeConfig "
                + ", tcv.typeValue, '' as configValue "
                + ", ce.configValue as valor, me.measure as medida "
                + "from RPF_ConfigElement ce , RPF_TypeConfig tc "
                + ", RPF_TypeValue tcv, RPF_Measure me "
                + "where ce.id_elementStruct = " + this.getElementStruct().getIdElementStruct()
                + "and ce.id_typeConfig = tc.id_typeConfig "
                + "and ce.id_typeValue = tcv.id_typeValue "
                + "and ce.id_configValue is null "
                + "and ce.id_measure = me.id_measure "
                + ") as T";

        ConReport con = new ConReport();
        con.setQuery(query);
        List listData = con.getListConfigElement();

        return listData;
    }

    /**
     * Método que entrega los datos de una configuración especifica de un
     * elemento. Se requiere el ID de la configuración
     * @return ElementStruct    Objeto ElementStruct con la configuración
     * @throws ExceptionHandler
     */
    public ElementStruct getConfigElement() throws ExceptionHandler{
        String query = "select ce.id_configElement , ce.id_typeConfig "
                + ", ce.id_typeValue , ce.id_configValue "
                + ", ce.configValue, ce.id_measure "
                + "from RPF_ConfigElement ce "
                + "where id_configElement = " + this.getElementStruct().getIdConfigElement();

        ConReport con = new ConReport();
        con.setQuery(query);
        ElementStruct elem = con.getConfigElement(query);

        return elem;
    }

    /**
     * Método que entrega los datos de una query asociada a una estructura
     * @return Config   Objeto Config con los datos de la query
     * @throws ExceptionHandler
     */
    public Config getQueryConfig() throws ExceptionHandler{
        String query = "select id_queryReport , id_structure, query , isExtern "
                + " from RPF_QueryReport"
                + " where id_structure = " + this.getConfig().getIdStructure();

        ConReport con = new ConReport();
        con.setQuery(query);
        Config conf = con.getQueryConfig();

        return conf;
    }

    /**
     * Método que entrega el listado con los tipos de configuración
     * @return List     Listado con Objetos Config
     * @throws ExceptionHandler
     */
    public List getListTypeConfig() throws ExceptionHandler{

        String query = "select tc.id_typeConfig , tc.typeConfig "
                + " from RPF_TypeConfig tc , RPF_TypeConfTypeStruct tt"
                + " where tt.id_typeConfig = tc.id_typeConfig"
                + " and tt.id_typeStructure = " + this.getStructure().getIdTypeStructure();

        ConReport con = new ConReport();
        con.setQuery(query);
        List listData = con.getListTypeConfig();

        return listData;
    }

    /**
     * Método que entrega el listado con los tipos de configuración para los elementos
     * @return List     Listado con Objetos ElementStruct
     * @throws ExceptionHandler
     */
    public List getListTypeConfigElem() throws ExceptionHandler{

        String query = "select id_typeConfig, typeConfig "
                + " from RPF_TypeConfig where id_typeConfig in ("
                + " select distinct tct.id_typeConfig from RPF_TypeConfTypeStruct tct"
                + " where tct.id_typeStructure in ("
                + "  select te.id_typeStructure from RPF_TypeElemTypeStruct te"
                + "  , RPF_TypeStructure ts"
                + "  where te.id_typeElement = (select id_typeElement "
                + "        from RPF_ElementStruct"
                + "        where id_elementStruct = " + this.getElementStruct().getIdElementStruct()
                + " ) and te.id_typeStructure = ts.id_typeStructure))";

        ConReport con = new ConReport();
        con.setQuery(query);
        List listData = con.getListTypeConfigElem();

        return listData;
    }

    /**
     * Método que entrega el listado con los tipos de valores asociados a un
     * tipo de configuración. Se requiere el ID del tipo de configuración
     * @return List     Listado con objetos Config
     * @throws ExceptionHandler
     */
    public List getListTypeValue() throws ExceptionHandler{

        String query = "select tc.id_TypeValue , tcv.typeValue "
                + " from rpf_typeValue tcv, RPF_TypeConfig tc "
                + " where tc.id_typeConfig = " + this.getConfig().getIdTypeConfig()
                + " and tcv.id_typeValue = tc.id_TypeValue";

        ConReport con = new ConReport();
        con.setQuery(query);
        List listData = con.getListTypeValue();

        return listData;
    }

    /**
     * Método que entrega el listado con los tipos de valores asociados a un
     * tipo de configuración para los Elementos de Estructura. Se requiere el
     * ID del tipo de configuración
     * @return List     Listado con elementos ElementStruct
     * @throws ExceptionHandler
     */
    public List getListTypeValueElem() throws ExceptionHandler{

        String query = "select tc.id_TypeValue , tcv.typeValue "
                + " from rpf_typeValue tcv, RPF_TypeConfig tc "
                + " where tc.id_typeConfig = " + this.getElementStruct().getIdTypeConfig()
                + " and tcv.id_typeValue = tc.id_TypeValue";

        ConReport con = new ConReport();
        con.setQuery(query);
        List listData = con.getListTypeValueElem();

        return listData;
    }

    /**
     * Método que entrega el listado de valores de configuración. Se requiere
     * el ID del tipo de configuración
     * @return List     Listado con objetos Config
     * @throws ExceptionHandler
     */
    public List getListConfigValue() throws ExceptionHandler{

        String query = "select cv.id_configValue, cv.ConfigValue"
                + " from RPF_ConfigValue cv, RPF_TypeConfig tc"
                + " where cv.id_typeValue = tc.id_typeValue"
                + " and tc.id_typeConfig = " + this.getConfig().getIdTypeConfig();

        ConReport con = new ConReport();
        con.setQuery(query);
        List listData = con.getListConfigValue();

        return listData;
    }

    /**
     * Método que entrega el listado de valores de configuración para elemento.
     * Se requiere el ID del tipo de configuración
     * @return List     Listado con objetos ElementStruct
     * @throws ExceptionHandler
     */
    public List getListConfigValueElem() throws ExceptionHandler{

        String query = "select cv.id_configValue, cv.ConfigValue"
                + " from RPF_ConfigValue cv, RPF_TypeConfig tc"
                + " where cv.id_typeValue = tc.id_typeValue"
                + " and tc.id_typeConfig = " + this.getElementStruct().getIdTypeConfig();

        ConReport con = new ConReport();
        con.setQuery(query);
        List listData = con.getListConfigValueElem();

        return listData;
    }

    /**
     * Método que entrega el listado de medidas
     * @return List     Listado con objetos Config
     * @throws ExceptionHandler
     */
    public List getListMeasure() throws ExceptionHandler{

        String query = "select id_measure, measure"
                + " from RPF_Measure";

        ConReport con = new ConReport();
        con.setQuery(query);
        List listData = con.getListMeasure();

        return listData;
    }

    /**
     * Método que entrega el listado de medidas para elementos
     * @return List     Listado con objetos ElementStruct
     * @throws ExceptionHandler
     */
    public List getListMeasureElem() throws ExceptionHandler{

        String query = "select id_measure, measure"
                + " from RPF_Measure";

        ConReport con = new ConReport();
        con.setQuery(query);
        List listData = con.getListMeasureElem();

        return listData;
    }

    /**
     * Método que entrega el listado de configuraciones para una estructura. Se
     * requiere el ID de la estructura
     * @return List     Listado con objetos Config
     * @throws ExceptionHandler
     */
    public List getListConfigStructure() throws ExceptionHandler{
        String query = "select id_configStructure, typeConfig, typeValue , "
                + " ConfigValue + V + medida as valor"
                + " from ("
                + " select cs.id_configStructure , tc.typeConfig , tcv.typeValue"
                + ", cv.ConfigValue , cs.configValue as V , '' as medida "
                + " from RPF_ConfigStructure cs , RPF_TypeConfig tc"
                + ", RPF_TypeValue tcv, RPF_ConfigValue cv"
                + " where cs.id_structure = " + this.getStructure().getIdStructure()
                + " and cs.id_typeConfig = tc.id_typeConfig"
                + " and cs.id_typeValue = tcv.id_typeValue"
                + " and cs.id_configValue = cv.id_configValue"
                + " and cs.id_measure is null"
                + " union "
                + " select cs.id_configStructure , tc.typeConfig , tcv.typeValue"
                + ", '' as configValue , cs.configValue as V , me.measure "
                + " from RPF_ConfigStructure cs , RPF_TypeConfig tc"
                + ", RPF_TypeValue tcv, RPF_Measure me"
                + " where cs.id_structure = " + this.getStructure().getIdStructure()
                + " and cs.id_typeConfig = tc.id_typeConfig"
                + " and cs.id_typeValue = tcv.id_typeValue"
                + " and cs.id_configValue is null"
                + " and cs.id_measure = me.id_measure) as T" ;

        ConReport con = new ConReport();
        con.setQuery(query);
        List listData = con.getListConfigStructure();

        return listData;
    }

    /**
     * Método que entrega una configuración especifica de una estructura. Se
     * requiere el ID de la estructura
     * @return Config   Objeto Config con los datos de la configuración
     * @throws ExceptionHandler
     */
    public Config getConfigStructure() throws ExceptionHandler{
        String query = "select cs.id_configStructure , cs.id_typeConfig "
                + ", cs.id_typeValue, cs.id_configValue "
                + ", cs.configValue, cs.id_measure "
                + " from RPF_ConfigStructure cs "
                + " where cs.id_configStructure = " + this.getConfig().getIdConfigStructure();

        ConReport con = new ConReport();
        con.setQuery(query);
        Config conf = con.getConfigStructure(query);
        return conf;
    }

    /**
     * Método para entregar las respuestas en un formato de XML
     * @param data  Data de la respuesta
     * @return  String  Texto con el XML de respuesta
     */
    public String salidaXML(String data, String ruta){
        StringBuilder str = new StringBuilder();
        str.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
        str.append("<qry>\n");
        str.append("<respuesta>").append(data).append("</respuesta>\n");
        str.append("<ruta>").append(ruta).append("</ruta>\n");
        str.append("</qry>");

        return str.toString();
    }

    /**
     * Método para obtener los elementos que pueden ser seleccionados para
     * generar un nuevo Reporte. Aquí se utilizan reportes generados con
     * anterioridad que no necesariamente están disponibles al usuario, pero
     * contienen elementos que pueden ser usados para generar otros reportes.
     * Se entregara un listados con los distintos tipos de elementos que se
     * obtuvieron.
     * @return  HashMap     Objeto que contiene los listados de elementos
     */
    public List getElementToSelect() throws ExceptionHandler{
        List sld = new ArrayList();

        String query = "select st.id_structure, st.structure "
                + ", st.id_typeStructure, ts.typeStructure "
                + " from RPF_Structure st , RPF_TypeStructure ts "
                + " where st.id_report in (7,12) "
                + " and st.id_typeStructure = ts.id_typeStructure"
                + " and st.orden >=7 "
                + " order by st.id_typeStructure";

        ConReport con = new ConReport();
        con.setQuery(query);
        sld = con.getElementToSelect();

        return sld;
    }

    public Integer getMaxOrderReport(Integer idReport) throws ExceptionHandler{
        Integer sld = null;
        String query = "select max(orden) from RPF_Structure "
                + " where id_report = " + idReport;

        ConReport con = new ConReport();
        con.setQuery(query);
        sld = con.getMaxOrderReport();
        return sld;
    }

    public Integer copyStructure(Integer IDnew, Integer IDold, Integer orden) throws ExceptionHandler{
        Integer sld = null;
        String query = " insert into RPF_Structure (id_report,structure,id_typeStructure,orden)"
                        + " select " + IDnew
                        + ",structure,id_typeStructure, " + orden
                        + " from RPF_Structure where id_structure = " + IDold;

        ConReport con = new ConReport();
        con.setQuery(query);
        sld = con.executeInsert(query);

        return sld;
    }

    public Integer copyConfigStructure(Integer IDnew, Integer IDold) throws ExceptionHandler{
        Integer sld = null;
        String query = "insert into RPF_ConfigStructure "
                + " (id_structure,id_typeConfig, id_typeValue, id_configValue, configValue, id_measure) "
                + "  select " + IDnew
                + ", id_typeConfig, id_typeValue, id_configValue, configValue, id_measure"
                + " from RPF_ConfigStructure where id_structure = " + IDold;

        ConReport con = new ConReport();
        con.setQuery(query);
        sld = con.executeInsert(query);

        return sld;
    }

    public List getListElementByIdStruct(Integer idStruct) throws ExceptionHandler{
        List sld = new ArrayList();

        String query = "select id_elementStruct, id_structure"
                + ", id_typeElement, orden, valueElement , '' "
                + " from RPF_ElementStruct "
                + " where id_structure = " + idStruct;

        ConReport con = new ConReport();
        con.setQuery(query);
        sld = con.getListElement();

        return sld;
    }

    public Integer copyElementStructure(Integer IdStruct, Integer IdElemOld) throws ExceptionHandler{
        Integer sld = null;
        String query = "insert into RPF_ElementStruct "
                + " (id_structure, id_typeElement, orden, valueElement) "
                + " select " + IdStruct
                + ", id_typeElement,orden,valueElement "
                + " from RPF_ElementStruct where id_elementStruct = " + IdElemOld;

        ConReport con = new ConReport();
        con.setQuery(query);
        sld = con.executeInsert(query);

        return sld;
    }

    public Integer copyConfigElementStructure(Integer IdElemNew, Integer IdElemOld) throws ExceptionHandler{
        Integer sld = null;
        String query = "insert into RPF_ConfigElement "
                + " (id_elementStruct, id_typeConfig, id_typeValue,id_configValue,id_measure, configValue) "
                + " select " + IdElemNew
                + " , id_typeConfig, id_typeValue,id_configValue,id_measure,configValue "
                + " from RPF_ConfigElement where id_elementStruct = " + IdElemOld;

        ConReport con = new ConReport();
        con.setQuery(query);
        sld = con.executeInsert(query);

        return sld;
    }
}
