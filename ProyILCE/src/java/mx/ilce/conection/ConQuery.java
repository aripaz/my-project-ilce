package mx.ilce.conection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import mx.ilce.bean.Campo;
import mx.ilce.bean.CampoForma;
import mx.ilce.bean.DataTransfer;
import mx.ilce.bean.HashCampo;
import mx.ilce.bitacora.AdmBitacora;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.component.AdminFile;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.LogHandler;
import mx.ilce.util.UtilDate;
import mx.ilce.util.UtilValue;

/**
 * Clase para la implementación de los métodos que se conectan a la Base de
 * Datos y manejan la construcción de las estructuras para contener los datos
 * de las queries que se desean ejecutar
 * @author ccatrilef
 */
class ConQuery {
    private Connection conn;
    private boolean enableDataLog=true;
    private Bitacora bitacora;
    private String hourMinSec;

    /**
     * Obtiene un String con la hora, minuto y segundo de un día. Se utiliza
     * como variable local
     * @return  String  Texto con la hora, minuto y segundo
     */
    private String getHourMinSec() {
        return ((hourMinSec==null)?"":hourMinSec);
    }

    /**
     * Asigna un String con la hora, minuto y segundo de un día. Se utiliza
     * como variable local
     * @param hourMinSec    Texto con la hora, minuto y segundo
     */
    private void setHourMinSec(String hourMinSec) {
        this.hourMinSec = hourMinSec;
    }

    /**
     * Obtiene el objeto Bitacora
     * @return  Bitacora    Objeto Bitacora
     */
    public Bitacora getBitacora() {
        return bitacora;
    }

    /**
     * Asigna el objeto Bitacora
     * @param bitacora  Objeto Bitacora
     */
    public void setBitacora(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

    /**
     * Obtiene si el log debe incluir la query con datos o no
     * @return  Boolean     Valor validación
     */
    public boolean isEnableDataLog() {
        return enableDataLog;
    }

    /**
     * Asigna si el log debe inlcuir la query con datos o no
     * @param enableDataLog     Valor validación
     */
    public void setEnableDataLog(boolean enableDataLog) {
        this.enableDataLog = enableDataLog;
    }

    /**
     * Constructor básico de la clase
     */
    public ConQuery() {
    }

    /**
     * Método que realiza la conexión a la base de datos. Los parámetros de
     * conexión se obtienen de un properties para una fácil mantención sin
     * compilar.
     * @throws SQLException
     */
    private void getConection() throws SQLException, ExceptionHandler{
        StringBuilder strConexion = new StringBuilder();
        try {
            Properties prop = AdminFile.leerConfig();

            String server = AdminFile.getKey(prop,"SERVER");
            String base = AdminFile.getKey(prop,"BASE");
            String port = AdminFile.getKey(prop,"PORT");
            String user = AdminFile.getKey(prop,"USR");
            String psw = AdminFile.getKey(prop,"PSW");

            strConexion.append("jdbc:sqlserver://");
            strConexion.append(server);
            strConexion.append(":").append(port);
            strConexion.append(";databasename=");
            strConexion.append(base);
            strConexion.append(";selectMethod=cursor;");

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.conn = DriverManager.getConnection(strConexion.toString(),user,psw);
            if (this.conn.isClosed()){
                System.out.println("NO HAY CONEXION");
            }
        }catch (SQLException sqlex){
            throw new ExceptionHandler(sqlex,this.getClass(),
                    "Problemas para abrir conexión a la Base de Datos");
        } catch (ClassNotFoundException ex) {
            throw new ExceptionHandler(ex,this.getClass(),
                    "No se encontro los Driver de conexión");
        }catch (Exception e){
            throw new ExceptionHandler(e,this.getClass(),
                    "Problemas para abrir conexión a la Base de Datos");
        }
    }

    /**
     * Método que ejecuta una query de inserción, tras efectuarla, se obtiene el
     * ID o clave de los datos insertados, el cual es retornado en el atributo
     * Object del HashCampo. En caso que no se ejecute la operación el número
     * retornado es -1, en caso que sea una tabla sin ID o clave, el valor
     * retornado es 0.
     * El objeto DataTransfer contiene los siguientes datos:
     * (-) CampoForma: con los datos para la ejecución.
     * (-) Query: con la query a ejecutar.
     * @param  dataTransfer     Objeto para transferencia de datos entre capas
     * @return HashCampo        Objeto que contiene el resultado
     * @throws ExceptionHandler
     */
    public HashCampo executeInsert(DataTransfer dataTransfer) throws ExceptionHandler{

        CampoForma campoForma = dataTransfer.getCampoForma();
        String strQuery = dataTransfer.getQueryInsert();

        HashCampo hsCmp = new HashCampo();
        Statement st = null;
        ResultSet rs = null;
        try{
            Integer increment =Integer.valueOf(-1);
            if (validateInsert(campoForma.getTabla(), strQuery)){
                getConection();
                st = this.conn.createStatement();
                this.conn.setAutoCommit(true);
                strQuery = UtilValue.castAcent(strQuery);
                int res = st.executeUpdate(strQuery, Statement.RETURN_GENERATED_KEYS);
                rs = st.getGeneratedKeys();
                if (res!=0){
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                    if (rs.next()) {
                        do {
                            for (int i=1; i<=columnCount; i++) {
                                String key = rs.getString(i);
                                try{
                                    increment = Integer.valueOf(key);
                                }catch(NumberFormatException e){
                                    increment = res;
                                }
                            }
                        } while(rs.next());
                    }
                }
                Bitacora bitacoraI = this.getBitacora();
                if ((bitacoraI!=null)&&(bitacoraI.isEnable()))
                {
                    AdmBitacora admBit = new AdmBitacora();
                    Integer evento = Integer.valueOf(AdmBitacora.getKey(admBit.getProp()
                                                                ,AdmBitacora.AGREGAR));
                    bitacoraI.setConsulta(strQuery);
                    bitacoraI.setClaveRegistro(increment);
                    bitacoraI.setClaveTipoEvento(evento);
                    bitacoraI.setEvento(AdmBitacora.AGREGAR);
                    admBit.setBitacora(bitacoraI);
                    if (admBit.addBitacora()){
                        admBit.setLstVariables(bitacoraI.getLstVariables());
                        admBit.addVariablesBitacora(admBit.getIntSld());
                        admBit.setLstVariables(null);
                    }
                    this.getBitacora().cleanDataQuery();
                    this.getBitacora().setIdBitacora(admBit.getIntSld());
                }
            }
            hsCmp.setObjData(increment);
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                                                "Problemas para ejecutar INSERT");
            eh.setStrQuery(strQuery);
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para ejecutar INSERT");
            eh.setStrQuery(strQuery);
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                StringBuffer textData=new StringBuffer();
                textData.append(dataTransfer.toString());
                hsCmp.setStrQuery(strQuery);

                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(strQuery);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(),AdminFile.LOGFILESERVER),
                            new StringBuffer("executeInsert"),
                            textData);
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),
                                           "Problemas al escribir en el Archivo de Log");
            }
            try{
                if (rs!=null){
                    rs.close();
                }
                if (st!=null){
                    st.close();
                }
                this.conn.close();
            }catch(SQLException es){
                throw new ExceptionHandler(es,this.getClass(),
                                           "Problemas para cerrar conexión a la Base de Datos");
            }
        }
        return hsCmp;
    }

    /**
     * Método para validar que se esta ejecutando una instrucción insert, en la
     * tabla que corresponde, con la query entregada.
     * @param tabla     Nombre de la tabla
     * @param query     Query a validar
     * @return
     */
    private boolean validateInsert(String tabla, String query){
        boolean queryOK = true;
        String str = "insert into " + tabla;
        if ( (!query.toUpperCase().contains(str.toUpperCase())) &&
             (!query.toUpperCase().contains("VALUES")) ){
            queryOK = false;
        }
        return queryOK;
    }

    /**
     * Método que efectua una query de actualización de datos, tras su ejecución
     * entrega un número uno (1), que efectuo correctamente la operación, cero (0)
     * en caso contrario. El valor es retornado en el atributo Object del HashCampo.
     * El objeto DataTransfer contiene los siguientes datos:
     * (-) CampoForma: con los datos para realizar el update.
     * (-) String: con la query a ejecutar.
     * @param  dataTransfer     Objeto para transferencia de datos entre capas
     * @return HashCampo        Objeto que contiene el resultado
     * @throws ExceptionHandler
     */
    public HashCampo executeUpdate(DataTransfer dataTransfer) throws ExceptionHandler{
        CampoForma campoForma = dataTransfer.getCampoForma();
        String strQuery = dataTransfer.getQueryUpdate();

        HashCampo hsCmp = new HashCampo();
        Statement st = null;
        try{
            Integer increment =Integer.valueOf(-1);
            if (validateUpdate(campoForma.getTabla(), strQuery)){
                getConection();
                st = this.conn.createStatement();
                strQuery = UtilValue.castAcent(strQuery);
                increment = st.executeUpdate(strQuery);
                Bitacora bitacoraU = this.getBitacora();
                if ((bitacoraU!=null)&&(bitacoraU.isEnable()))
                {
                    AdmBitacora admBit = new AdmBitacora();
                    Integer evento = Integer.valueOf(AdmBitacora.getKey(admBit.getProp()
                                                                ,AdmBitacora.ACTUALIZAR));
                    bitacoraU.setConsulta(strQuery);
                    bitacoraU.setClaveTipoEvento(evento);
                    bitacoraU.setClaveForma(campoForma.getClaveForma());
                    bitacoraU.setEvento(AdmBitacora.ACTUALIZAR);
                    admBit.setBitacora(bitacoraU);
                    if (admBit.addBitacora()){
                        admBit.setLstVariables(bitacoraU.getLstVariables());
                        admBit.addVariablesBitacora(admBit.getIntSld());
                        admBit.setLstVariables(null);
                    }
                    this.getBitacora().cleanDataQuery();
                    this.getBitacora().setIdBitacora(admBit.getIntSld());
                }
            }
            hsCmp.setObjData(increment);
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                                                "Problemas para ejecutar UPDATE");
            eh.setStrQuery(strQuery);
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para ejecutar UPDATE");
            eh.setStrQuery(strQuery);
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                StringBuffer textData=new StringBuffer();
                textData.append(dataTransfer.toString());

                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(strQuery);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(),AdminFile.LOGFILESERVER),
                            new StringBuffer("executeUpdate"),
                            textData);
                hsCmp.setStrQuery(strQuery);
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),
                                    "Problemas al escribir en el Archivo de Log");
            }
            try{
                if (st!=null){
                    st.close();
                }
                this.conn.close();
            }catch(SQLException es){
                throw new ExceptionHandler(es,this.getClass(),
                                    "Problemas para cerrar conexión a la Base de Datos");
            }
        }
        return hsCmp;
    }

    /**
     * Método para validar que se esta ejecutando una instrucción update, en la
     * tabla que corresponde, con la query entregada
     * @param tabla     Nombre de la tabla
     * @param query     Query a validar
     * @return
     */
    private boolean validateUpdate(String tabla, String query){
        boolean queryOK = true;
        String str = "update " + tabla;
        if ((!query.toUpperCase().contains(str.toUpperCase())) &&
                (!query.toUpperCase().contains("WHERE"))){
            queryOK = false;
        }
        return queryOK;
    }

    /**
     * Método que efectua una query de eliminación de datos, tras su ejecución
     * entrega un número: (1) efectuo correctamente la operacion, (0) en caso
     * contrario. El valor es retornado en el atributo Object del HashCampo.
     * El objeto DataTransfer contiene los siguientes datos:
     * (-) CampoForma: contiene los datos para identificar la tabla.
     * (-) String: contiene la query que se ejecutara.
     * @param  dataTransfer     Objeto para transferencia de datos entre capas
     * @return HashCampo        Objeto que contiene el resultado
     * @throws ExceptionHandler
     */
    public HashCampo executeDelete(DataTransfer dataTransfer) throws ExceptionHandler{
        CampoForma campoForma = dataTransfer.getCampoForma();
        String strQuery = dataTransfer.getQueryDelete();

        HashCampo hsCmp = new HashCampo();
        Statement st = null;
        try{
            Integer increment =Integer.valueOf(-1);
            if (validateDelete(campoForma.getTabla(), strQuery)){
                getConection();
                st = this.conn.createStatement();
                strQuery = UtilValue.castAcent(strQuery);
                increment = st.executeUpdate(strQuery);
                Bitacora bitacoraD = this.getBitacora();
                if ((bitacoraD!=null)&&(bitacoraD.isEnable()))
                {
                    AdmBitacora admBit = new AdmBitacora();
                    Integer evento = Integer.valueOf(AdmBitacora.getKey(admBit.getProp()
                                                                ,AdmBitacora.ELIMINAR));
                    bitacoraD.setConsulta(strQuery);
                    bitacoraD.setClaveTipoEvento(evento);
                    bitacoraD.setClaveForma(campoForma.getClaveForma());
                    bitacoraD.setEvento(AdmBitacora.ELIMINAR);
                    admBit.setBitacora(bitacoraD);
                    if (admBit.addBitacora()){
                        admBit.setLstVariables(bitacoraD.getLstVariables());
                        admBit.addVariablesBitacora(admBit.getIntSld());
                        admBit.setLstVariables(null);
                    }
                    this.getBitacora().cleanDataQuery();
                    this.getBitacora().setIdBitacora(admBit.getIntSld());
                }
            }
            hsCmp.setObjData(increment);
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                                                "Problemas para ejecutar DELETE");
            eh.setStrQuery(strQuery);
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para ejecutar DELETE");
            eh.setStrQuery(strQuery);
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                StringBuffer textData=new StringBuffer();
                textData.append(dataTransfer.toString());

                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(strQuery);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(),AdminFile.LOGFILESERVER),
                            new StringBuffer("executeDelete"),
                            textData);
                hsCmp.setStrQuery(strQuery);
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),
                                    "Problemas al escribir en el Archivo de Log");
            }
            try{
                if (st!=null){
                    st.close();
                }
                this.conn.close();
            }catch(SQLException es){
                throw new ExceptionHandler(es,this.getClass(),
                                    "Problemas para cerrar conexión a la Base de Datos");
            }
        }
        return hsCmp;
    }

    /**
     * Método para efectuar un delete e insert conjunto. NO PROBADO
     * @param  dataTransfer     Objeto para transferencia de datos entre capas
     * @return HashCampo        Objeto que contiene el resultado
     * @throws ExceptionHandler
     */
    public HashCampo executeDeleteInsert(DataTransfer dataTransfer) throws ExceptionHandler{
        CampoForma campoForma = dataTransfer.getCampoForma();
        String qryDelete = dataTransfer.getQueryDelete();
        String qryInsert = dataTransfer.getQueryInsert();

        HashCampo hsCmp = new HashCampo();
        Statement st = null;
        ResultSet rs = null;
        try{
            Integer increment =Integer.valueOf(-1);
            if (validateDelete(campoForma.getTabla(), qryDelete)){
                getConection();
                st = this.conn.createStatement();
                increment = st.executeUpdate(qryDelete);
            }
            if (increment>0){
                increment =Integer.valueOf(-1);
                if (validateInsert(campoForma.getTabla(), qryInsert)){
                    getConection();
                    st = this.conn.createStatement();
                    this.conn.setAutoCommit(true);
                    int res = st.executeUpdate(qryInsert, Statement.RETURN_GENERATED_KEYS);
                    rs = st.getGeneratedKeys();
                    if (res!=0){
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        if (rs.next()) {
                            do {
                                for (int i=1; i<=columnCount; i++) {
                                    String key = rs.getString(i);
                                    increment = Integer.valueOf(key);
                                }
                            } while(rs.next());
                        }
                    }
                }
                hsCmp.setObjData(increment);
            }
            Bitacora bitacoraO = this.getBitacora();
            if ((bitacoraO!=null)&&(bitacoraO.isEnable()))
            {
                AdmBitacora admBit = new AdmBitacora();
                Integer evento = Integer.valueOf(AdmBitacora.getKey(admBit.getProp()
                                                            ,AdmBitacora.ELIMINAR));
                bitacoraO.setConsulta(qryDelete);
                bitacoraO.setClaveRegistro(increment);
                bitacoraO.setClaveTipoEvento(evento);
                bitacoraO.setClaveForma(campoForma.getClaveForma());
                bitacoraO.setEvento(AdmBitacora.ELIMINAR);
                admBit.setBitacora(bitacoraO);
                if (admBit.addBitacora()){
                    admBit.setLstVariables(bitacoraO.getLstVariables());
                    admBit.addVariablesBitacora(admBit.getIntSld());
                    admBit.setLstVariables(null);
                }
            }
            if ((bitacoraO!=null)&&(bitacoraO.isEnable()))
            {
                AdmBitacora admBit = new AdmBitacora();
                Integer evento = Integer.valueOf(AdmBitacora.getKey(admBit.getProp()
                                                            ,AdmBitacora.AGREGAR));
                bitacoraO.setConsulta(qryInsert);
                bitacoraO.setClaveRegistro(increment);
                bitacoraO.setClaveTipoEvento(evento);
                bitacoraO.setClaveForma(campoForma.getClaveForma());
                bitacoraO.setEvento(AdmBitacora.AGREGAR);
                admBit.setBitacora(bitacoraO);
                if (admBit.addBitacora()){
                    admBit.setLstVariables(bitacoraO.getLstVariables());
                    admBit.addVariablesBitacora(admBit.getIntSld());
                    admBit.setLstVariables(null);
                }
            }
            if (bitacoraO!=null){
                this.getBitacora().cleanDataQuery();
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                                                "Problemas para ejecutar DELETE");
            eh.setStrQuery("\nQUERY DELETE:\n"+qryDelete+"\nQUERY INSERT:\n"+qryInsert);
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para ejecutar DELETE");
            eh.setStrQuery("\nQUERY DELETE:\n"+qryDelete+"\nQUERY INSERT:\n"+qryInsert);
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                StringBuffer textData=new StringBuffer();
                textData.append(dataTransfer.toString());

                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(("\nQUERY DELETE: "+qryDelete+"\nQUERY INSERT:"+qryInsert));
                log.logData(AdminFile.getKey(AdminFile.leerConfig(),AdminFile.LOGFILESERVER),
                            new StringBuffer("executeDeleteInsert"),
                            textData);
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),
                                    "Problemas al escribir en el Archivo de Log");
            }
            try{
                if (st!=null){
                    st.close();
                }
                this.conn.close();
            }catch(SQLException es){
                throw new ExceptionHandler(es,this.getClass(),
                                    "Problemas para cerrar conexion a la Base de Datos");
            }
        }
        return hsCmp;
    }

    /**
     * Método para validar que se esta ejecutando una instrucción Delete, en la
     * tabla que corresponde, con la query entregada
     * @param tabla     Nombre de la tabla
     * @param query     Query a validar
     * @return
     */
    private boolean validateDelete(String tabla, String query){
        boolean queryOK = true;
        String str = "delete from " + tabla;
        if ((!query.toUpperCase().contains(str.toUpperCase())) &&
                (!query.toUpperCase().contains("WHERE"))){
            queryOK = false;
        }
        return queryOK;
    }

    /**
     * Método que obtiene la data, aplicando a la query los parametros de entrada
     * entregados. El idQuery entregado permite seleccionar la query respectiva.
     * Cada uno de los parámetros de entrada deben convertirse a un String y
     * agregarse en el arreglo respectivo.
     * Los parámetros de entrada deben venir en el orden que se indica en la
     * Query.
     * El objeto DataTransfer contiene los siguientes datos:
     * (-) idQuery: Codigo de la query a utilizar.
     * (-) arrData: Arreglo con los parámetros de entrada.
     * (-) arrVariables: Arreglo con variables que no necesariamente son obligatorias,
     * pero pueden estar definidas en el User o el ambiente de la aplicación.
     * @param  dataTransfer     Objeto para transferencia de datos entre capas
     * @return HashCampo        Objeto que contiene el resultado
     * @throws ExceptionHandler
     */
    public HashCampo getData(DataTransfer dataTransfer) throws ExceptionHandler{
        Integer idQuery = dataTransfer.getIdQuery();
        String[] arrData = dataTransfer.getArrData();
        String[][] arrVariables = dataTransfer.getArrVariables();

        HashCampo hsCmp = new HashCampo();
        Statement ps = null;
        ResultSet rs = null;
        String query = "";
        String queryLog = "";
        String pk="";
        try{
            getConection();
            query = getQueryById(idQuery);
            queryLog = query;
            if ((!"".equals(query)) && (arrData != null)){
                ps =this.conn.createStatement();
                if (arrVariables!=null){
                    for (int i=0; i<arrVariables.length;i++){
                        String strVar = arrVariables[i][0];
                        String strValue = arrVariables[i][1];
                        if (strVar!=null){
                            if (strVar.equals("$pk")){
                                strVar = "\\$pk";
                                pk = strValue;
                            }
                            query = query.replaceAll(strVar, strValue);
                        }
                    }
                }
                for(int i=1;i<=arrData.length;i++){
                    String strData = arrData[i-1];
                    if (strData != null){
                        query = query.replaceAll("%"+i, strData);
                    }
                }
                query = UtilValue.castAcent(query);
                rs = ps.executeQuery(query);
                ResultSetMetaData rstm = rs.getMetaData();

                for (int i=1;i<=rstm.getColumnCount();i++){
                    Campo cmp = new Campo(rstm.getColumnName(i).toLowerCase(),
                                          Integer.valueOf(i),
                                          rstm.getColumnTypeName(i));
                    cmp.setTypeDataAPL(castTypeDataDBtoAPL(rstm.getColumnTypeName(i)));
                    cmp.setIsIncrement(rstm.isAutoIncrement(i));
                    hsCmp.addCampo(cmp);
                }
                int i=0;
                while (rs.next()){
                    List lstData = new ArrayList();
                    List lstCampo = hsCmp.getListCampos();
                    Iterator it = lstCampo.iterator();
                    while (it.hasNext()){
                        Campo itCmp = (Campo) it.next();
                        Campo cmp = new Campo(itCmp.getNombre().toLowerCase(),
                                              itCmp.getNombreDB().toLowerCase(),
                                              itCmp.getCodigo(),
                                              itCmp.getTypeDataDB(),
                                              castTypeDataDBtoAPL(itCmp.getTypeDataDB()),
                                              getValueCampo(itCmp.getTypeDataDB(),
                                              rs, itCmp.getCodigo()));
                        cmp.setHourMinSec(this.getHourMinSec());
                        this.setHourMinSec("");
                        cmp.setIsIncrement(itCmp.getIsIncrement());
                        lstData.add(cmp);
                    }
                    hsCmp.addListData(lstData,i++);
                }
            }

            Bitacora bitacoraS = this.getBitacora();
            if ((bitacoraS!=null)&&(bitacoraS.isEnable()))
            {
                AdmBitacora admBit = new AdmBitacora();
                Integer evento = Integer.valueOf(AdmBitacora.getKey(admBit.getProp(),
                                                                    AdmBitacora.CONSULTAR));
                if (this.isEnableDataLog()){
                    bitacoraS.setConsulta(query);
                }else{
                    bitacoraS.setConsulta(queryLog);
                }
                bitacoraS.setClaveTipoEvento(evento);
                bitacoraS.setEvento(AdmBitacora.CONSULTAR);

                admBit.setBitacora(bitacoraS);
                admBit.addBitacora();
                this.getBitacora().cleanDataQuery();
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                                        "Problemas para obtención de datos con IDQUERY y DATA enviada");
            eh.setStrQuery(query);
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                        "Problemas para obtención de datos con IDQUERY y DATA enviada");
            eh.setStrQuery(query);
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                StringBuffer textData=new StringBuffer();
                textData.append(("IDQUERY: "+idQuery)).append("\n");
                if (this.isEnableDataLog()){
                    log.setStrQuery(query);
                    textData.append(dataTransfer.toString());
                    hsCmp.setStrQuery(query);
                }else{
                    queryLog = UtilValue.castAcent(queryLog);
                    log.setStrQuery(queryLog);
                    hsCmp.setStrQuery(query);
                }
                log.logData(AdminFile.getKey(AdminFile.leerConfig(),AdminFile.LOGFILESERVER),
                            new StringBuffer("getData"),
                            textData);
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),
                                    "Problemas al escribir en el Archivo de Log");
            }
            try{
            if (rs!=null){
                rs.close();
            }
            if (ps!=null){
                ps.close();
            }
            this.conn.close();
            }catch(SQLException es){
                throw new ExceptionHandler(es,this.getClass(),
                                    "Problemas para cerrar conexión a la Base de Datos");
            }
        }
        return hsCmp;
    }

    /**
     * Método que obtiene la data, aplicando a la query entregada un parámetro de entrada,
     * el cual consiste en un string con un "WHERE" o un "AND" dependiendo de la
     * query, este texto adicional condicionara la respuesta de la query.
     * El idQuery entregado permite seleccionar la query respectiva.
     * El objeto DataTransfer contiene los siguientes datos:
     * (-) idQuery: ID de la query a utilizar.
     * (-) arrData: Arreglo con los parámetros de entrada que se utilizara en la query.
     * (-) strWhere: Condiciones adicionales para agregar a la query.
     * (-) arrVariables: Arreglo con variables que no necesariamente son obligatorias,
     * pero pueden estar definidas en el User o el ambiente de la aplicación.
     * @param  dataTransfer     Objeto para transferencia de datos entre capas
     * @return HashCampo        Objeto que contiene el resultado
      * @throws ExceptionHandler
      */
    public HashCampo getDataWithWhere(DataTransfer dataTransfer) throws ExceptionHandler{
        Integer idQuery = dataTransfer.getIdQuery();
        String whereData = dataTransfer.getStrWhere();
        String[][] arrVariables = dataTransfer.getArrVariables();

        HashCampo hsCmp = new HashCampo();
        Statement ps = null;
        ResultSet rs = null;
        String query = "";
        String pk="";
        try{
            getConection();
            query = getQueryById(idQuery);
            if ((!"".equals(query)) && (whereData != null)){
                ps =this.conn.createStatement();
                query = addWhereToQuery(query,whereData);

                if (arrVariables!=null){
                    for (int i=0; i<arrVariables.length;i++){
                        String strVar = arrVariables[i][0];
                        String strValue = arrVariables[i][1];
                        if (strVar!=null){
                            if (strVar.equals("$pk")){
                                strVar = "\\$pk";
                                pk = strValue;
                            }
                            query = query.replaceAll(strVar, strValue);
                        }
                    }
                }
                if ((dataTransfer.getOrderBY()!=null)&&(!"".equals(dataTransfer.getOrderBY()) )){
                    query = "SELECT * FROM (" + query + ") AS TORDER ORDER BY " + dataTransfer.getOrderBY();
                }
                query = UtilValue.castAcent(query);
                rs = ps.executeQuery(query);
                ResultSetMetaData rstm = rs.getMetaData();

                for (int i=1;i<=rstm.getColumnCount();i++){
                    Campo cmp = new Campo(rstm.getColumnName(i).toLowerCase(),
                                          Integer.valueOf(i),
                                          rstm.getColumnTypeName(i));
                    cmp.setTypeDataAPL(castTypeDataDBtoAPL(rstm.getColumnTypeName(i)));
                    cmp.setIsIncrement(rstm.isAutoIncrement(i));
                    hsCmp.addCampo(cmp);
                }
                int i=0;
                while (rs.next()){
                    List lstData = new ArrayList();
                    List lstCampo = hsCmp.getListCampos();
                    Iterator it = lstCampo.iterator();
                    while (it.hasNext()){
                        Campo itCmp = (Campo) it.next();
                        Campo cmp = new Campo(itCmp.getNombre().toLowerCase(),
                                              itCmp.getNombreDB().toLowerCase(),
                                              itCmp.getCodigo(),
                                              itCmp.getTypeDataDB(),
                                              castTypeDataDBtoAPL(itCmp.getTypeDataDB()),
                                              getValueCampo(itCmp.getTypeDataDB(),
                                              rs, itCmp.getCodigo()));
                        cmp.setHourMinSec(this.getHourMinSec());
                        this.setHourMinSec("");
                        cmp.setIsIncrement(itCmp.getIsIncrement());
                        lstData.add(cmp);
                    }
                    hsCmp.addListData(lstData,i++);
                }
                Bitacora bitacoraS = this.getBitacora();
                if ((bitacoraS!=null)&&(bitacoraS.isEnable()))
                {
                    AdmBitacora admBit = new AdmBitacora();
                    Integer evento = Integer.valueOf(AdmBitacora.getKey(admBit.getProp(),
                                                                        AdmBitacora.CONSULTAR));
                    bitacoraS.setConsulta(query);
                    bitacoraS.setClaveTipoEvento(evento);
                    bitacoraS.setEvento(AdmBitacora.CONSULTAR);

                    admBit.setBitacora(bitacoraS);
                    admBit.addBitacora();
                    this.getBitacora().cleanDataQuery();
                }
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                                                "Problemas para obtención de datos con WHERE enviado");
            eh.setStrQuery(query);
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtención de datos con WHERE enviado");
            eh.setStrQuery(query);
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                StringBuffer textData=new StringBuffer();
                textData.append(dataTransfer.toString());

                LogHandler log = new LogHandler();
                log.setStrQuery(query);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(),AdminFile.LOGFILESERVER),
                            new StringBuffer("getDataWithWhere"),
                            textData);
                hsCmp.setStrQuery(query);
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),
                                    "Problemas al escribir en el Archivo de Log");
            }
            try{
            if (rs!=null){
                rs.close();
            }
            if (ps!=null){
                ps.close();
            }
            this.conn.close();
            }catch(SQLException es){
                throw new ExceptionHandler(es,this.getClass(),
                                    "Problemas para cerrar conexión a la Base de Datos");
            }
        }
        return hsCmp;
    }

    /**
     * Método que obtiene la data, aplicando a la query un parámetro de entrada, el cual
     * consiste en: (1) Un string con un "WHERE" o un "AND" dependiendo de la query,
     * que condicionara su respuesta, (2) Un arreglo con la data de entrada para
     * la query. El idQuery entregado permite seleccionar la query respectiva.
     * El objeto DataTransfer contiene los siguientes datos:
     * (-) idQuery: ID de la query a ejecutar.
     * (-) strWhere: Condiciones adicionales para agregar a la query.
     * (-) arrData: Arreglo de data con los parámetros de entrada que se utilizara en la query.
     * (-) arrVariables: Arreglo con variables que no necesariamente son obligatorias,
     * pero pueden estar definidas en el User o el ambiente de la aplicación.
     * @param  dataTransfer     Objeto para transferencia de datos entre capas
     * @return HashCampo        Objeto que contiene el resultado
     * @throws ExceptionHandler
     */
    public HashCampo getDataWithWhereAndData(DataTransfer dataTransfer) throws ExceptionHandler{
        Integer idQuery = dataTransfer.getIdQuery();
        String whereData = dataTransfer.getStrWhere();
        String[] arrData = dataTransfer.getArrData();
        String[][] arrVariables = dataTransfer.getArrVariables();

        HashCampo hsCmp = new HashCampo();
        Statement ps = null;
        ResultSet rs = null;
        String query = "";
        String pk = "";
        try{
            getConection();
            query = getQueryById(idQuery);
            if ((!"".equals(query)) && (whereData != null)){
                ps =this.conn.createStatement();
                query = addWhereToQuery(query,whereData);
                if (arrVariables!=null){
                    for (int i=0; i<arrVariables.length;i++){
                        String strVar = arrVariables[i][0];
                        String strValue = arrVariables[i][1];
                        if (strVar!=null){
                            if (strVar.equals("$pk")){
                                strVar = "\\$pk";
                                pk = strValue;
                            }
                            query = query.replaceAll(strVar, strValue);
                        }
                    }
                }

                if (arrData!=null){
                    for(int i=1;i<=arrData.length;i++){
                        String strData = arrData[i-1];
                        if (strData != null){
                            query = query.replaceAll("%"+i, strData);
                        }
                    }
                }
                query = UtilValue.castAcent(query);
                rs = ps.executeQuery(query);
                ResultSetMetaData rstm = rs.getMetaData();

                for (int i=1;i<=rstm.getColumnCount();i++){
                    Campo cmp = new Campo(rstm.getColumnName(i).toLowerCase(),
                                          Integer.valueOf(i),
                                          rstm.getColumnTypeName(i));
                    cmp.setTypeDataAPL(castTypeDataDBtoAPL(rstm.getColumnTypeName(i)));
                    cmp.setIsIncrement(rstm.isAutoIncrement(i));
                    hsCmp.addCampo(cmp);
                }
                int i=0;
                while (rs.next()){
                    List lstData = new ArrayList();
                    List lstCampo = hsCmp.getListCampos();
                    Iterator it = lstCampo.iterator();
                    while (it.hasNext()){
                        Campo itCmp = (Campo) it.next();
                        Campo cmp = new Campo(itCmp.getNombre().toLowerCase(),
                                              itCmp.getNombreDB().toLowerCase(),
                                              itCmp.getCodigo(),
                                              itCmp.getTypeDataDB(),
                                              castTypeDataDBtoAPL(itCmp.getTypeDataDB()),
                                              getValueCampo(itCmp.getTypeDataDB(),
                                              rs, itCmp.getCodigo()));
                        cmp.setHourMinSec(this.getHourMinSec());
                        this.setHourMinSec("");
                        cmp.setIsIncrement(itCmp.getIsIncrement());
                        lstData.add(cmp);
                    }
                    hsCmp.addListData(lstData,i++);
                }
                Bitacora bitacoraS = this.getBitacora();
                if ((bitacoraS!=null)&&(bitacoraS.isEnable()))
                {
                    AdmBitacora admBit = new AdmBitacora();
                    Integer evento = Integer.valueOf(AdmBitacora.getKey(admBit.getProp(),
                                                                        AdmBitacora.CONSULTAR));
                    bitacoraS.setConsulta(query);
                    bitacoraS.setClaveTipoEvento(evento);
                    bitacoraS.setEvento(AdmBitacora.CONSULTAR);

                    admBit.setBitacora(bitacoraS);
                    admBit.addBitacora();
                    this.getBitacora().cleanDataQuery();
                }
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                                        "Problemas para obtención de datos con WHERE y DATA enviada");
            eh.setStrQuery(query);
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                        "Problemas para obtención de datos con WHERE y DATA enviada");
            eh.setStrQuery(query);
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                StringBuffer textData=new StringBuffer();
                textData.append(dataTransfer.toString());

                LogHandler log = new LogHandler();
                log.setStrQuery(query);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(),AdminFile.LOGFILESERVER),
                            new StringBuffer("getDataWithWhereAndData"),
                            textData);
                hsCmp.setStrQuery(query);
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),
                                    "Problemas al escribir en el Archivo de Log");
            }
            try{
            if (rs!=null){
                rs.close();
            }
            if (ps!=null){
                ps.close();
            }
            this.conn.close();
            }catch(SQLException es){
                throw new ExceptionHandler(es,this.getClass(),
                                    "Problemas para cerrar conexión a la Base de Datos");
            }
        }
        return hsCmp;
    }

    /**
     * Método que toma una instrucción "WHERE" que se va adjuntar a una query y
     * la analiza para evaluar si requiere cambiar el WHERE por AND, agregar un
     * WHERE o un AND o simplemente juntar las instrucciones, con el fin de tener
     * una query correcta
     * @param query     Query Principal
     * @param strWhere  Instrucción a agregar a la query
     * @return
     */
    private String addWhereToQuery(String query, String strWhere){
        String strQuery = query;
        String strCopy = strWhere;
        String strConcat = "";
        boolean replaceAnd = false;
        boolean replaceWhere = false;

        if (query!=null){
            strQuery = strQuery.toUpperCase();
            strQuery = strQuery.replace("WHERE", "WHERE ");
        }
        if ((strWhere!=null) && (!"".equals(strWhere))){
            strWhere = strWhere.toUpperCase();
            if (strQuery.contains("WHERE ")){
                //evaluaremos el WHERE y el AND
                if (strWhere.trim().length()>6){
                    //EMPIEZA CON WHERE
                    if (strWhere.toUpperCase().trim().substring(0,6).equals("WHERE ")){
                        strConcat = " ";
                        replaceWhere = true;
                    //EMPIEZA CON AND
                    }else if (strWhere.toUpperCase().trim().substring(0,4).equals("AND ")){
                        strConcat = " ";
                    }else{
                        strConcat = " AND ";
                    }
                //evaluaremos solo el AND
                }else if (strWhere.trim().length()>4){
                    //EMPIEZA CON AND
                    if (strWhere.toUpperCase().trim().substring(0,4).equals("AND ")){
                        strConcat = " ";
                    }else{
                        strConcat = " AND ";
                    }
                }else{
                        strConcat = " AND ";
                }
            }else{
                //evaluaremos el WHERE Y el AND
                if (strWhere.trim().length()>6){
                    //COMIENZA CON WHERE
                    if (strWhere.toUpperCase().trim().substring(0,6).equals("WHERE ")){
                        strConcat = " ";
                    //COMIENZA CON AND
                    }else if (strWhere.toUpperCase().trim().substring(0,4).equals("AND ")){
                        strConcat = " ";
                        replaceAnd = true;
                    }else{
                        strConcat = " WHERE ";
                    }
                //evaluaremos solo el AND
                }else if (strWhere.trim().length()>4){
                    //COMIENZA CON AND
                    if (strWhere.toUpperCase().trim().substring(0,4).equals("AND ")){
                        strConcat = " ";
                        replaceAnd = true;
                    }else{
                        strConcat = " WHERE ";
                    }
                }else{
                    strConcat = " ";
                }
            }
        }else{
             strConcat = " ";
        }
        String sld = "";
        String queryOrder = query;
        String queryPos = "";
        if (queryOrder.toUpperCase().contains("ORDER BY")){
            int pos = queryOrder.toUpperCase().indexOf("ORDER BY");
            queryOrder = query.substring(0,pos-1);
            queryPos = " " + query.substring(pos,query.length());
        }
        if (replaceAnd){
            String paso = strCopy.toUpperCase();
            int pos = paso.indexOf("AND");
            String strMitad = paso.substring(pos+3);
            sld = queryOrder + " WHERE " +  strMitad;
        }else if (replaceWhere){
            String paso = strCopy.toUpperCase();
            int pos = paso.indexOf("WHERE");
            String strMitad = paso.substring(pos+3);
            sld = queryOrder + " AND " +  strMitad;
        }else{
            sld = queryOrder + strConcat + strCopy;
        }
        sld = sld + queryPos;

        return sld;
    }

     /**
     * Método que obtiene la data aplicando la query seleccionada mediante el idQuery.
     * Esta query no posee parámetros de entrada.
     * El objeto DataTransfer contiene los siguientes datos:
     * (-) idQuery: Código de la query a utilizar.
     * (-) arrVariables: Arreglo con variables que no necesariamente son obligatorias,
     * pero pueden estar definidas en el User o el ambiente de la aplicación.
     * @param  dataTransfer     Objeto para transferencia de datos entre capas
     * @return HashCampo        Objeto que contiene el resultado
     * @throws ExceptionHandler
     */
    private HashCampo getDataSimple(DataTransfer dataTransfer) throws ExceptionHandler {
        Integer idQuery = dataTransfer.getIdQuery();
        String[][] arrVariables = dataTransfer.getArrVariables();

        HashCampo hsCmp = new HashCampo();
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        String pk = "";
        try{
            getConection();
            query = getQueryById(idQuery);
            if (!"".equals(query)){
                if (arrVariables!=null){
                    for (int i=0; i<arrVariables.length;i++){
                        String strVar = arrVariables[i][0];
                        String strValue = arrVariables[i][1];
                        if (strVar!=null){
                            if (strVar.equals("$pk")){
                                strVar = "\\$pk";
                                pk = strValue;
                            }
                            query = query.replaceAll(strVar, strValue);
                        }
                    }
                }
                st = this.conn.createStatement();
                query = UtilValue.castAcent(query);
                rs = st.executeQuery(query);
                ResultSetMetaData rstm = rs.getMetaData();

                for (int i=1;i<=rstm.getColumnCount();i++){
                    Campo cmp = new Campo(rstm.getColumnName(i).toLowerCase(),
                                          Integer.valueOf(i),
                                          rstm.getColumnTypeName(i));
                    cmp.setTypeDataAPL(castTypeDataDBtoAPL(rstm.getColumnTypeName(i)));
                    cmp.setIsIncrement(rstm.isAutoIncrement(i));
                    hsCmp.addCampo(cmp);
                }
                int i=0;
                while (rs.next()){
                    List lstData = new ArrayList();
                    List lstCampo = hsCmp.getListCampos();
                    Iterator it = lstCampo.iterator();
                    while (it.hasNext()){
                        Campo itCmp = (Campo) it.next();
                        Campo cmp = new Campo(itCmp.getNombre().toLowerCase(),
                                              itCmp.getNombreDB().toLowerCase(),
                                              itCmp.getCodigo(),
                                              itCmp.getTypeDataDB(),
                                              castTypeDataDBtoAPL(itCmp.getTypeDataDB()),
                                              getValueCampo(itCmp.getTypeDataDB(),
                                              rs, itCmp.getCodigo()));
                        cmp.setHourMinSec(this.getHourMinSec());
                        this.setHourMinSec("");
                        cmp.setIsIncrement(itCmp.getIsIncrement());
                        lstData.add(cmp);
                    }
                    hsCmp.addListData(lstData,i++);
                }
                Bitacora bitacoraS = this.getBitacora();
                if ((bitacoraS!=null)&&(bitacoraS.isEnable()))
                {
                    AdmBitacora admBit = new AdmBitacora();
                    Integer evento = Integer.valueOf(AdmBitacora.getKey(admBit.getProp(),
                                                                        AdmBitacora.CONSULTAR));
                    bitacoraS.setConsulta(query);
                    bitacoraS.setClaveTipoEvento(evento);
                    bitacoraS.setEvento(AdmBitacora.CONSULTAR);

                    admBit.setBitacora(bitacoraS);
                    admBit.addBitacora();
                    this.getBitacora().cleanDataQuery();
                }
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                                                "Problemas para obtención de datos por ID de Query");
            eh.setStrQuery(query);
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtención de datos por ID de Query");
            eh.setStrQuery(query);
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                StringBuffer textData=new StringBuffer();
                textData.append(dataTransfer.toString());

                LogHandler log = new LogHandler();
                log.setStrQuery(query);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(),AdminFile.LOGFILESERVER),
                            new StringBuffer("getData"),
                            textData);
                hsCmp.setStrQuery(query);
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),
                                    "Problemas al escribir en el Archivo de Log");
            }
            try{
                if (rs!=null){
                    rs.close();
                }
                if (st!=null){
                    st.close();
                }
                this.conn.close();
            }catch(SQLException es){
                throw new ExceptionHandler(es,this.getClass(),
                                    "Problemas para cerrar conexión a la Base de Datos");
            }
        }
        return hsCmp;
    }

    /**
     * Obtiene el nombre del campo PK de una tabla
     * @param tabla     Nombre de la tabla que se desea analizar
     * @return  String  Nombre del campo PK
     */
    public String getCampoPK(String tabla) throws ExceptionHandler{
        String str ="";
        Statement st = null;
        ResultSet rs = null;
        try{
            getConection();
            String query = "select * from " + tabla;
            st = this.conn.createStatement();
            rs = st.executeQuery(query);
            ResultSetMetaData rstm = rs.getMetaData();

            for (int i=1;i<=rstm.getColumnCount();i++){
                if (rstm.isAutoIncrement(i)){
                    str = rstm.getColumnName(i);
                }
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                                                "Problemas para obtención del campo PK de una tabla");
            eh.setDataToXML("TABLA",tabla);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtención del campo PK de una tabla");
            eh.setDataToXML("TABLA",tabla);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                StringBuffer textData=new StringBuffer();
                textData.append(("TABLA: "+tabla));
                LogHandler log = new LogHandler();
                log.logData(AdminFile.getKey(AdminFile.leerConfig(),AdminFile.LOGFILESERVER),
                            new StringBuffer("getCampoPK"),
                            textData);
            }catch(Exception ex){
                ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                    "Problemas al escribir en el Archivo de Log");
                eh.setDataToXML("TABLA",tabla);
                eh.setStringData(eh.getDataToXML());
                eh.setSeeStringData(true);
                throw eh;
            }
            try{
                if (rs!=null){
                    rs.close();
                }
                if (st!=null){
                    st.close();
                }
                this.conn.close();
            }catch(SQLException es){
                throw new ExceptionHandler(es,this.getClass(),
                                    "Problemas para cerrar conexión a la Base de Datos");
            }
        }
        return str;
    }

    /**
     * Método para validar si la query entregada no posee alguna instrucción
     * que permita la modificación de la base de datos. Esto es una medida de
     * seguridad, para que las queries de modificacción solo sean las autorizadas,
     * es decir, que provengan del catálogo de queries de la base de datos y no
     * de una operación externa.
     * @param query     Query que debe ser validada
     * @return  Boolean     Valor de la validación
     */
    private boolean allowedQuery(String query){
        boolean sld = true;
        if (query!=null){
            String str = query.toUpperCase();
            if (str.contains("UPDATE")){
                sld = false;
            }else if (str.contains("DELETE")){
                sld = false;
            }else if (str.contains("INSERT")){
                sld = false;
            }else if (str.contains("DROP")){
                sld = false;
            }
        }
        return sld;
    }

    /**
     * Método que obtiene la data aplicando la Query y Data entregada. Solo se
     * permiten queries de selección de datos, si se envia alguna que posea
     * instrucciones de modificacion o similar, se bloquea la operación sin ejecutarla.
     * El objeto DataTransfer contiene los siguientes datos:
     * (-) query: Query que se debe ejecutar en la base de Datos.
     * (-) arrData: Parámetros con que se debe completar la query.
     * (-) arrVariables: Arreglo con variables que no necesariamente son obligatorias,
     * pero pueden estar definidas en el User o el ambiente de la aplicación.
     * @param  dataTransfer     Objeto para transferencia de datos entre capas
     * @return HashCampo        Objeto que contiene el resultado
     * @throws ExceptionHandler
     */
    public HashCampo getDataByQuery(DataTransfer dataTransfer) throws ExceptionHandler{
        String query = dataTransfer.getQuery();
        String[] arrData = dataTransfer.getArrData();
        String[][] arrVariables = dataTransfer.getArrVariables();

        HashCampo hsCmp = new HashCampo();
        Statement st = null;
        ResultSet rs = null;
        String pk = "";
        try{
            getConection();
            if (allowedQuery(query)){
                query = UtilValue.castAcent(query);
                if ((!"".equals(query)) && (arrData != null)){
                    st = this.conn.createStatement();
                    if (arrVariables!=null){
                        for (int i=0; i<arrVariables.length;i++){
                            String strVar = arrVariables[i][0];
                            String strValue = arrVariables[i][1];
                            if (strVar!=null){
                                if (strVar.equals("$pk")){
                                    strVar = "\\$pk";
                                    pk = strValue;
                                }
                                query = query.replaceAll(strVar, strValue);
                            }
                        }
                    }
                    for(int i=1;i<=arrData.length;i++){
                        String strData = arrData[i-1];
                        if (strData != null){
                            query = query.replaceAll("%"+i, strData);
                        }
                    }
                    query = UtilValue.castAcent(query);
                    rs = st.executeQuery(query);
                    ResultSetMetaData rstm = rs.getMetaData();

                    for (int i=1;i<=rstm.getColumnCount();i++){
                        Campo cmp = new Campo(rstm.getColumnName(i).toLowerCase(),
                                              Integer.valueOf(i),
                                              rstm.getColumnTypeName(i));
                        cmp.setTypeDataAPL(castTypeDataDBtoAPL(rstm.getColumnTypeName(i)));
                        cmp.setIsIncrement(rstm.isAutoIncrement(i));
                        hsCmp.addCampo(cmp);
                    }
                    int i=0;
                    while (rs.next()){
                        List lstData = new ArrayList();
                        List lstCampo = hsCmp.getListCampos();
                        Iterator it = lstCampo.iterator();
                        while (it.hasNext()){
                            Campo itCmp = (Campo) it.next();
                            Campo cmp = new Campo(itCmp.getNombre().toLowerCase(),
                                                  itCmp.getNombreDB().toLowerCase(),
                                                  itCmp.getCodigo(),
                                                  itCmp.getTypeDataDB(),
                                                  castTypeDataDBtoAPL(itCmp.getTypeDataDB()),
                                                  getValueCampo(itCmp.getTypeDataDB(),
                                                  rs,
                                                  itCmp.getCodigo()));
                            cmp.setHourMinSec(this.getHourMinSec());
                            this.setHourMinSec("");
                            cmp.setIsIncrement(itCmp.getIsIncrement());
                            lstData.add(cmp);
                        }
                        hsCmp.addListData(lstData,i++);
                    }
                }
                Bitacora bitacoraS = this.getBitacora();
                if ((bitacoraS!=null)&&(bitacoraS.isEnable()))
                {
                    AdmBitacora admBit = new AdmBitacora();
                    Integer evento = Integer.valueOf(AdmBitacora.getKey(admBit.getProp(),
                                                                        AdmBitacora.CONSULTAR));
                    bitacoraS.setConsulta(query);
                    bitacoraS.setClaveTipoEvento(evento);
                    bitacoraS.setEvento(AdmBitacora.CONSULTAR);

                    admBit.setBitacora(bitacoraS);
                    admBit.addBitacora();
                    this.getBitacora().cleanDataQuery();
                }
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                                                "Problemas para obtención de datos mediante Query");
            eh.setStrQuery(query);
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtención de datos mediante Query");
            eh.setStrQuery(query);
            eh.setDataToXML(dataTransfer);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                StringBuffer textData=new StringBuffer();
                textData.append(dataTransfer.toString());

                LogHandler log = new LogHandler();
                log.setStrQuery(query);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(),AdminFile.LOGFILESERVER),
                            new StringBuffer("getDataByQuery"),
                            textData);
                hsCmp.setStrQuery(query);
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),
                                    "Problemas al escribir en el Archivo de Log");
            }
            try{
                if (rs!=null){
                    rs.close();
                }
                if (st!=null){
                    st.close();
                }
                this.conn.close();
            }catch(SQLException es){
                throw new ExceptionHandler(es,this.getClass(),
                                    "Problemas para cerrar conexión a la Base de Datos");
            }
        }
        return hsCmp;
    }

    /**
     * Método que busca una query desde la base de datos, pasandole el ID que posee
     * en la tabla
     * @param idQuery   ID de la Query a buscar
     * @return  String  Texto con la query solicitada
     * @throws ExceptionHandler
     */
    private String getQueryById(Integer idQuery) throws ExceptionHandler{
        String strSld = "";
        Statement st = null;
        ResultSet rs = null;
        StringBuilder query = new StringBuilder();
        try{
            String strQuery = getQueryMain(idQuery);
            if (strQuery.equals("")){
                if (this.conn == null){
                    getConection();
                }
                query.append("select consulta from consulta_forma ");
                query.append(" where clave_consulta = ").append(idQuery.toString());
                st = this.conn.createStatement();
                rs = st.executeQuery(query.toString());
                if (rs.next()){
                    strSld =  rs.getString(1);
                }
                strSld = UtilValue.castAcent(strSld);
            }else{
                strSld = strQuery;
            }
        }catch(Exception e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                                                "Problemas para obtener Query por su ID");
            eh.setStrQuery(query.toString());
            eh.setDataToXML("ID QUERY", idQuery);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                if (rs!=null){
                    rs.close();
                }
                if (st!=null){
                    st.close();
                }
            }catch(SQLException es){
                throw new ExceptionHandler(es,this.getClass(),
                                    "Problemas para cerrar conexión a la Base de Datos");
            }
            //Conn no se debe cerrar ya que es llamada privada
        }
        return strSld;
    }

    /**
     * Método que según el tipo de dato que se entrega, el cual viene desde la base de
     * datos, utiliza la conversión respectiva para dejar el valor como String
     * @param strType   Tipo de dato proveniente de la Base de Datos
     * @param rs        ResulSet donde esta el objeto a analizar
     * @param codigo    Código (posición) dentro del resulset donde esta el dato
     * a analizar
     * @return  String  Valor del campo
     * @throws ExceptionHandler
     */
    private String getValueCampo(String strType, ResultSet rs, Integer codigo)
            throws ExceptionHandler {
        String sld = new String();
        try{
            if (strType!=null){
                if (strType.toUpperCase().equals("CHAR")){
                    sld = rs.getString(codigo.intValue());
                }else if (strType.toUpperCase().equals("NVARCHAR")){
                    sld = rs.getString(codigo.intValue());
                }else if (strType.toUpperCase().equals("VARCHAR")){
                    sld = rs.getString(codigo.intValue());
                }else if(strType.toUpperCase().equals("INT") ){
                    sld = String.valueOf(rs.getBigDecimal(codigo.intValue()));
                }else if (strType.toUpperCase().equals("SMALLDATETIME") ){
                    Date date = rs.getDate(codigo.intValue());
                    if (date!=null){
                        UtilDate utDate = new UtilDate(date);
                        sld = utDate.getFecha("/");
                        this.setHourMinSec("");
                    }else{
                        sld = "";
                    }
                }else if(strType.toUpperCase().equals("DATETIME") ){
                    Date date = rs.getDate(codigo.intValue());
                    if (date!=null){
                        UtilDate utDate = new UtilDate(date);
                        sld = utDate.getFecha("/");
                        this.setHourMinSec(rs.getString(codigo.intValue()));
                    }else{
                        sld = "";
                    }
                }else if(strType.toUpperCase().equals("BIT") ){
                    sld = String.valueOf(rs.getString(codigo.intValue()));
                }else if (strType.toUpperCase().equals("TEXT")){
                    sld = rs.getString(codigo.intValue());
                }else{
                    sld = rs.getString(codigo.intValue());
                }
            }
        }catch(SQLException es){
            ExceptionHandler eh = new ExceptionHandler(es,this.getClass(),
                                    "Problemas para conversión de tipo de datos desde la Base de Datos");
            eh.setDataToXML("TIPO DATO", strType);
            eh.setDataToXML("CÓDIGO", codigo);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }
        return sld;
    }

    /**
     * Método que según el Tipo de Dato entregado que se tiene en la base de datos
     * se entrega un string con el tipo que le debe corresponder en Java
     * @param strType   Texto con el tipo de dato a analizar
     * @return  String  Texto con la conversión del tipo de dato
     */
    private String castTypeDataDBtoAPL(String strType)
    {
        String sld = new String();
        if (strType!=null){
            if (strType.toUpperCase().equals("CHAR")){
                sld = "java.lang.String";
            }else if (strType.toUpperCase().equals("VARCHAR")){
                sld = "java.lang.String";
            }else if (strType.toUpperCase().equals("NVARCHAR")){
                sld = "java.lang.String";
            }else if(strType.toUpperCase().equals("INT") ){
                sld = "java.lang.Integer";
            }else if(strType.toUpperCase().equals("SMALLDATETIME") ){
                sld = "mx.ilce.bean.SmallDateTime";
            }else if(strType.toUpperCase().equals("DATETIME") ){
                sld = "java.sql.Date";
            }else if(strType.toUpperCase().equals("BIT") ){
                sld = "mx.ilce.bean.BIT";
            }else if(strType.toUpperCase().equals("MONEY") ){
                sld = "mx.ilce.bean.Money";
            }else if(strType.toUpperCase().equals("TEXT") ){
                sld = "mx.ilce.bean.Text";
            }else{
                sld = "java.lang.String";
            }
        }
        return sld;
    }

    /**
     * Método que entrega las queries principales de obtención de datos. Se busca
     * obtener una pequeña mejora en el performance de la aplicación quitando estos
     * llamados a la base de datos, dado el abuso de las llamadas a queries que
     * se realizan, y dado que estas no pueden estar sufriendo modificaciones
     * constantes como las otras queries definidas por la Capa Vista
     * @param idQuery
     * @return  String  Texto con la conversión de los datos
     */
    private String getQueryMain(Integer idQuery){
        String sld = "";
        switch (idQuery){
            case -1://LOGIN
                sld = "select clave_empleado as claveEmpleado "
                        + ", nombre, apellido_paterno as apellidoPaterno "
                        + ", apellido_materno as apellidoMaterno, email "
                        + ", clave_perfil as clavePerfil "
                        + ", clave_area as claveArea "
                        + ", foto as urlAvatar, foto as foto "
                        + "from empleado "
                        + "where email = '%1' and password = '%2'";
                break;
            case -2://USER
                sld = "select clave_empleado as claveEmpleado, nombre "
                        + ", apellido_paterno as apellidoPaterno "
                        + ", apellido_materno as apellidoMaterno "
                        + ", email, clave_perfil as clavePerfil "
                        + ", clave_area as claveArea , password "
                        + "from empleado where email = '%1'";
                break;
            case -3://PERFIL
                sld = "select distinct pe.clave_perfil, pe.perfil "
                        + ", ap.clave_aplicacion, ap.aplicacion, fo.clave_forma "
                        + ", ap.clave_forma_principal, ap.descripcion "
                        + ", ap.alias_menu_nueva_entidad, ap.alias_menu_mostrar_entidad "
                        + " from empleado em, perfil pe, aplicacion ap "
                        + ", permiso_forma per , forma fo "
                        + " where em.clave_empleado = %1 "
                        + " and   em.clave_perfil = pe.clave_perfil "
                        + " and   ap.clave_aplicacion = fo.clave_aplicacion "
                        + " and   per.clave_forma = fo.clave_forma "
                        + " order by ap.clave_aplicacion , fo.clave_forma ";
                break;
            case -4://TABFORMA
                sld = "select alias_tab, orden_tab, clave_aplicacion "
                        + ", clave_forma, clave_forma_padre "
                        + " from forma "
                        + " where clave_aplicacion=%1 "
                        + " and clave_forma=%2 "
                        + " union "
                        + " select alias_tab, orden_tab, clave_aplicacion "
                        + ", clave_forma, clave_forma_padre "
                        + " from forma "
                        + " where clave_aplicacion=%3 "
                        + " and clave_forma_padre=%4 "
                        + " order by orden_tab";
                break;
            case -5://XMLSESSION
                sld = "select clave_empleado, nombre, apellido_paterno "
                        + ", apellido_materno, email, clave_perfil, foto, clave_area "
                        + "from empleado where clave_empleado=%1";
                break;
            case -6://XMLMENU
                sld = "select distinct a.aplicacion, pa.clave_perfil "
                        + ", a.clave_aplicacion, pa.activo, p.clave_forma "
                        + ", fo.forma, a.alias_menu_nueva_entidad "
                        + ", a.alias_menu_mostrar_entidad "
                        + ",(select case when count(*)>0 then 1 else 0 end "
                        + " from permiso_forma pf "
                        + " where pf.clave_perfil = pa.clave_perfil "
                        + " and pf.clave_forma = p.clave_forma "
                        + " and pf.clave_permiso = 1) as mostrar "
                        + ",(select case when count(*)>0 then 1 else 0 end "
                        + " from permiso_forma pf "
                        + " where pf.clave_perfil = pa.clave_perfil "
                        + " and pf.clave_forma = p.clave_forma "
                        + " and pf.clave_permiso = 2) as insertar "
                        + ",(select case when count(*)>0 then 1 else 0 end "
                        + " from permiso_forma pf "
                        + " where pf.clave_perfil = pa.clave_perfil "
                        + " and pf.clave_forma = p.clave_forma "
                        + " and pf.clave_permiso = 3) as actualizar "
                        + ",(select case when count(*)>0 then 1 else 0 end "
                        + " from permiso_forma pf "
                        + " where pf.clave_perfil = pa.clave_perfil "
                        + " and pf.clave_forma = p.clave_forma "
                        + " and pf.clave_permiso = 4) as eliminar "
                        + ",(select case when count(*)>0 then 1 else 0 end "
                        + " from permiso_forma pf "
                        + " where pf.clave_perfil = pa.clave_perfil "
                        + " and pf.clave_forma = p.clave_forma "
                        + " and pf.clave_permiso = 5) as mostrar_informacion_sensible "
                        + " from perfil_aplicacion pa, aplicacion a "
                        + " , permiso_forma p , forma fo "
                        + " where a.clave_aplicacion=pa.clave_aplicacion "
                        + " and a.clave_forma_principal = p.clave_forma "
                        + " and a.clave_aplicacion = fo.clave_aplicacion "
                        + " and p.clave_forma = fo.clave_forma "
                        + " and pa.clave_perfil=%1";
                break;
            case -7://FORMACAMPOS
                sld = "select cf.clave_campo, cf.clave_forma, f.tabla "
                        + ", cf.campo, cf.alias_campo, cf.obligatorio "
                        + ", cf.tipo_control, cf.evento, cf.clave_forma_foranea "
                        + ", cf.filtro_foraneo, cf.ayuda, cf.dato_sensible "
                        + ", cf.activo, cf.tamano, cf.edita_forma_foranea "
                        + ", cf.visible, cf.valor_predeterminado, cf.justificar_cambio "
                        + ", f.alias_tab, cf.usado_para_agrupar "
                        + ", cf.no_permitir_valor_foraneo_nulo "
                        + ", cf.carga_dato_foraneos_retrasada "
                        + " from campo_forma cf, forma f "
                        + " where cf.clave_forma = f.clave_forma "
                        + " and cf.clave_forma = $pk "
                        + " and cf.campo in (%2)";
                break;
            case -8://FORMA
                sld = "select cf.clave_campo, cf.clave_forma, f.tabla, cf.campo "
                        + ", cf.alias_campo,  cf.obligatorio, cf.tipo_control "
                        + ", cf.evento, cf.clave_forma_foranea, cf.filtro_foraneo "
                        + ", cf.ayuda, cf.dato_sensible, cf.activo, cf.tamano "
                        + ", cf.edita_forma_foranea , cf.visible, f.forma "
                        + ", cf.valor_predeterminado, cf.justificar_cambio "
                        + ", f.alias_tab, cf.usado_para_agrupar "
                        + ", cf.no_permitir_valor_foraneo_nulo "
                        + ", cf.carga_dato_foraneos_retrasada "
                        + " from campo_forma cf, forma f "
                        + " where f.clave_forma = cf.clave_forma "
                        + " and f.clave_forma = %1 "
                        + " order by cf.clave_forma, cf.campo, cf.clave_campo";
                break;
            case -9://FORMAQUERY
                sld = "select * from consulta_forma "
                        + " where clave_forma = %1 "
                        + " and tipo_accion = '%2'"
                        + " and clave_perfil = %clave_perfil";
                break;
            case -10://PERMISOS
                sld = "select pf.clave_permiso , per.permiso, pf.clave_forma "
                        + " from  permiso per, permiso_forma pf, empleado em "
                        + ", perfil_aplicacion pa "
                        + " where per.clave_permiso = pf.clave_permiso "
                        + " and   pf.clave_perfil = em.clave_perfil "
                        + " and   em.clave_perfil = pa.clave_perfil "
                        + " and   em.clave_empleado = %1 "
                        + " and   pf.clave_forma = %2 "
                        + " group by pf.clave_permiso , per.permiso, pf.clave_forma";
                break;
            case -11: //EVENTO
                sld = "SELECT alias_tab, evento, instrucciones, forma "
                        + " FROM forma "
                        + " where clave_forma = %1";
                break;
            case -12: //FORMASFORANEAS
                sld = "SELECT clave_aplicacion, clave_forma, forma "
                        + "FROM forma "
                        + "WHERE clave_forma_padre= %1";
                break;
            default:
                sld = "";
                break;
        }
        return sld;
    }
}