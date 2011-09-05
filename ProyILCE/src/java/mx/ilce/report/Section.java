package mx.ilce.report;

import java.io.Serializable;
import java.util.List;
import mx.ilce.bean.HashCampo;

/**
 * Clase para la contencion de las secciones y sus configuraciones
 * @author ccatrilef
 */
class Section implements Serializable{

    private Integer idSequence;
    private Integer idOrder;
    private Integer idQuery;
    private String strQuery;
    private String sequenceType;
    private String textValue;
    private List listHeader;
    private List listConfig;

    private List listConfigBlock;
    private List listConfigTable;
    private List listConfigColumn;
    private List listConfigBodyTable;
    private List listConfigRow;
    private List listConfigCell;

    private List listConfigFirstColumn;
    private List listConfigFirstRow;
    private List listConfigFirstCell;

    private List listConfigLastColumn;
    private List listConfigLastRow;
    private List listConfigLastCell;

    private HashCampo hsCampoData;

    /**
     * Obtiene un objeto con la data de un campo
     * @return
     */
    public HashCampo getHsCampoData() {
        return hsCampoData;
    }

    /**
     * Asigna un objeto con la data de un campo
     * @param hsCampoData
     */
    public void setHsCampoData(HashCampo hsCampoData) {
        this.hsCampoData = hsCampoData;
    }

    /**
     * Obtiene la configuracion de un BLOQUE
     * @return
     */
    public List getListConfigBlock() {
        return listConfigBlock;
    }

    /**
     * Asigna la configuracion de un BLOQUE
     * @param listConfigBlock
     */
    public void setListConfigBlock(List listConfigBlock) {
        this.listConfigBlock = listConfigBlock;
    }

    /**
     * Obtiene la configuracion de un BODYTABLE
     * @return
     */
    public List getListConfigBodyTable() {
        return listConfigBodyTable;
    }

    /**
     * Asigna la configuracion de un BODYTABLE
     * @param listConfigBodyTable
     */
    public void setListConfigBodyTable(List listConfigBodyTable) {
        this.listConfigBodyTable = listConfigBodyTable;
    }

    /**
     * Obtiene la configuracion de una Celda
     * @return
     */
    public List getListConfigCell() {
        return listConfigCell;
    }

    /**
     * Asigna la configuracion de una Celda
     * @param listConfigCell
     */
    public void setListConfigCell(List listConfigCell) {
        this.listConfigCell = listConfigCell;
    }

    /**
     * Obtiene la configuracion de una columna
     * @return
     */
    public List getListConfigColumn() {
        return listConfigColumn;
    }

    /**
     * Asigna la configuracion de una columna
     * @param listConfigColumn
     */
    public void setListConfigColumn(List listConfigColumn) {
        this.listConfigColumn = listConfigColumn;
    }

    /**
     * Obtiene la configuracion de una fila
     * @return
     */
    public List getListConfigRow() {
        return listConfigRow;
    }

    /**
     * Asigna la configuracion de una fila
     * @param listConfigRow
     */
    public void setListConfigRow(List listConfigRow) {
        this.listConfigRow = listConfigRow;
    }

    /**
     * Obtiene la configuracion de una Tabla
     * @return
     */
    public List getListConfigTable() {
        return listConfigTable;
    }

    /**
     * Asigna la configuracion de una tabla
     * @param listConfigTable
     */
    public void setListConfigTable(List listConfigTable) {
        this.listConfigTable = listConfigTable;
    }

    /**
     * Obtiene la configuracion de la primera celda
     * @return
     */
    public List getListConfigFirstCell() {
        return listConfigFirstCell;
    }

    /**
     * Asigna la configuracion de la primera celda
     * @param listFirstConfigCell
     */
    public void setListConfigFirstCell(List listConfigFirstCell) {
        this.listConfigFirstCell = listConfigFirstCell;
    }

    /**
     * Obtiene la configuracion de la primera columna
     * @return
     */
    public List getListConfigFirstColumn() {
        return listConfigFirstColumn;
    }

    /**
     * Asigna la configuracion de la primera columna
     * @param listFirstConfigColumn
     */
    public void setListConfigFirstColumn(List listConfigFirstColumn) {
        this.listConfigFirstColumn = listConfigFirstColumn;
    }

    /**
     * Obtiene la configuracion de la primera fila
     * @return
     */
    public List getListConfigFirstRow() {
        return listConfigFirstRow;
    }

    /**
     * Asigna la configuracion de la primera fila
     * @param listFirstConfigRow
     */
    public void setListConfigFirstRow(List listConfigFirstRow) {
        this.listConfigFirstRow = listConfigFirstRow;
    }

    /**
     * Obtiene la configuracion de la ultima celda
     * @return
     */
    public List getListConfigLastCell() {
        return listConfigLastCell;
    }

    /**
     * Asigna la configuracion de la ultima celda
     * @param listLastConfigCell
     */
    public void setListConfigLastCell(List listConfigLastCell) {
        this.listConfigLastCell = listConfigLastCell;
    }

    /**
     * Obtiene la configuracion de la ultima columna
     * @return
     */
    public List getListConfigLastColumn() {
        return listConfigLastColumn;
    }

    /**
     * Asigna la configuracion de la ultima columna
     * @param listLastConfigColumn
     */
    public void setListConfigLastColumn(List listConfigLastColumn) {
        this.listConfigLastColumn = listConfigLastColumn;
    }

    /**
     * Obtiene la configuracion de la ultima fila
     * @return
     */
    public List getListConfigLastRow() {
        return listConfigLastRow;
    }

    /**
     * Asigna la configuracion de la ultima fila
     * @param listLastConfigRow
     */
    public void setListConfigLastRow(List listConfigLastRow) {
        this.listConfigLastRow = listConfigLastRow;
    }

    /**
     * Obtiene el listado con la configuracion
     * @return
     */
    public List getListConfig() {
        return listConfig;
    }

    /**
     * Asigna el listado con la configuracion
     * @param listConfig
     */
    public void setListConfig(List listConfig) {
        this.listConfig = listConfig;
    }

    /**
     * Obtiene la configuracion del header de una tabla
     * @return
     */
    public List getListHeader() {
        return listHeader;
    }

    /**
     * Asigna la configuracion del header de una tabla
     * @param listHeader
     */
    public void setListHeader(List listHeader) {
        this.listHeader = listHeader;
    }

    /**
     * Obtiene la query de la seccion
     * @return
     */
    public String getStrQuery() {
        return strQuery;
    }

    /**
     * Asigna la query de la seccion
     * @param strQuery
     */
    public void setStrQuery(String strQuery) {
        this.strQuery = strQuery;
    }

    /**
     * Obtiene el orden de la configuracion
     * @return
     */
    public Integer getIdOrder() {
        return idOrder;
    }

    /**
     * Asigna el orden de la configuracion
     * @param idOrder
     */
    public void setIdOrder(Integer idOrder) {
        this.idOrder = idOrder;
    }

    /**
     * Obtiene el ID de la query de la seccion
     * @return
     */
    public Integer getIdQuery() {
        return idQuery;
    }

    /**
     * Asigna el ID de la query de la seccion
     * @param idQuery
     */
    public void setIdQuery(Integer idQuery) {
        this.idQuery = idQuery;
    }

    /**
     * Obtiene el ID de la secuencia de configuracion
     * @return
     */
    public Integer getIdSequence() {
        return idSequence;
    }

    /**
     * Asigna el ID de la secuencia de configuracion
     * @param idSequence
     */
    public void setIdSequence(Integer idSequence) {
        this.idSequence = idSequence;
    }

    /**
     * Obtiene el tipo de la secuencia
     * @return
     */
    public String getSequenceType() {
        return sequenceType;
    }

    /**
     * Asigna el tipo de la secuencia
     * @param sequenceType
     */
    public void setSequenceType(String sequenceType) {
        this.sequenceType = sequenceType;
    }

    /**
     * Obtiene el valor en forma de texto
     * @return
     */
    public String getTextValue() {
        return textValue;
    }

    /**
     * Asigna el valor en forma de texto
     * @param textValue
     */
    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    @Override
    public String toString() {
        return "Section{" + "idSequence=" + idSequence + " || idQuery=" + idQuery + " || sequenceType=" + sequenceType + " || textValue=" + textValue + " || strQuery=" + strQuery + '}';
    }
}
