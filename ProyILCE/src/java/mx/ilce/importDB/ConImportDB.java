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
package mx.ilce.importDB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import mx.ilce.component.AdminFile;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.LogHandler;

/**
 * Clase implementada para administrar las conexiones a la base de datos
 * @author ccatrilef
 */
class ConImportDB {
    private Connection conn;
    private String strQuery;
    private DataTable dataTable;

    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }



    public String getStrQuery() {
        return strQuery;
    }

    public void setStrQuery(String strQuery) {
        this.strQuery = strQuery;
    }

    /**
     * Método que se encarga de abrir una coenxión a la base de datos
     * @throws SQLException
     * @throws ExceptionHandler
     */
    private void getConection() throws SQLException, ExceptionHandler{
        StringBuilder strConexion = new StringBuilder();
        try {
            Properties prop = AdminFile.leerConfig();

            String server = AdminFile.getKey(prop,"SERVER");
            String base = base = AdminFile.getKey(prop,"BASE");
            String user = user = AdminFile.getKey(prop,"USR");
            String psw = AdminFile.getKey(prop,"PSW");

            if (this.getDataTable()!=null){
                base = this.getDataTable().getBaseTable();
                user = this.getDataTable().getUserTable();
                psw = this.getDataTable().getPassTable();
            }
            
            String port = AdminFile.getKey(prop,"PORT");
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
     * Método que se encarga de efectuar una operación del tipo INSERT en la
     * base de datos
     * @return  Integer     Resultado de la operción (>0:Exitoso)
     * @throws ExceptionHandler
     */
    public Integer executeInsert() throws ExceptionHandler{
        Statement st = null;
        ResultSet rs = null;
        Integer increment =Integer.valueOf(-1);
        try{
            getConection();
            if (this.conn!=null){
                st = this.conn.createStatement();
                this.conn.setAutoCommit(true);
                int res = st.executeUpdate(this.getStrQuery(), Statement.RETURN_GENERATED_KEYS);
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
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para ejecutar INSERT");
            eh.setStrQuery(this.getStrQuery());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para ejecutar INSERT");
            eh.setStrQuery(this.getStrQuery());
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
                this.conn.close();
            }catch(SQLException es){
                throw new ExceptionHandler(es,this.getClass(),
                        "Problemas para cerrar conexión a la Base de Datos");
            }
        }
        return increment;
    }

    /**
     * Método que se encarga de efectuar una operación del tipo UPDATE en la
     * base de datos
     * @return  Integer     Resultado de la operción (>0:Exitoso)
     * @throws ExceptionHandler
     */
    public Integer executeUpdate() throws ExceptionHandler{
        Statement st = null;
        ResultSet rs = null;
        Integer increment =Integer.valueOf(-1);
        try{
            getConection();
            if (this.conn!=null){
                st = this.conn.createStatement();
                this.conn.setAutoCommit(true);
                increment = st.executeUpdate(this.getStrQuery());
            }
        }catch(SQLException e){
            increment =Integer.valueOf(-1);
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para ejecutar UPDATE");
            eh.setStrQuery(this.getStrQuery());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            increment =Integer.valueOf(-1);
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para ejecutar UPDATE");
            eh.setStrQuery(this.getStrQuery());
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
                this.conn.close();
            }catch(SQLException es){
                increment =Integer.valueOf(-1);
                throw new ExceptionHandler(es,this.getClass(),
                        "Problemas para cerrar conexión a la Base de Datos");
            }
        }
        return increment;
    }

    /**
     * Método que se encarga de obtener los Campos para la carga de archivos
     * @return  List    Listado con los campos
     * @throws ExceptionHandler
     */
    public List getCampos() throws ExceptionHandler {
        List lst = new ArrayList();
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        HashMap hsNombre = new HashMap();
        HashMap hsAlias = new HashMap();
        List lstCampos = new ArrayList();
        try{
            getConection();
            query = this.getStrQuery();
            if (!"".equals(query)){
                st = this.conn.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()){
                    CargaArchivo ca = new CargaArchivo();
                    ca.setIdCampoTabla(getDataInteger(rs,"id_CampoTabla"));
                    ca.setNombreCampo(getDataString(rs,"nombreCampo"));
                    ca.setIdTipoCampo(getDataInteger(rs,"id_TipoCampo"));
                    ca.setTipoCampo(getDataString(rs,"tipoCampo"));
                    ca.setAliasCampo(getDataString(rs,"aliasCampo"));
                    ca.setObligatorio(getDataBoolean(rs,"obligatorio"));
                    ca.setFormato(getDataString(rs,"formato"));
                    ca.setTabla(getDataString(rs,"nombreTabla"));
                    ca.setSumable(getDataBoolean(rs,"sumable"));
                    ca.setPosicionInicio(getDataInteger(rs,"posicionInicio"));
                    ca.setPosicionInicioTotal(getDataInteger(rs,"posicionInicioTotal"));
                    ca.setLargo(getDataInteger(rs,"largo"));
                    ca.setLargoTotal(getDataInteger(rs,"largoTotal"));
                    ca.setNroFilaHeader(getDataInteger(rs,"nroFilaHeader"));
                    ca.setTagTotales(getDataString(rs,"tagTotales"));
                    ca.setTagNroRegistros(getDataString(rs,"tagNroRegistros"));
                    ca.setSeparador(getDataString(rs,"separador"));
                    ca.setPivote(getDataBoolean(rs,"pivote"));
                    ca.setIdTipoArchivoCarga(getDataInteger(rs,"id_TipoArchivoCarga"));
                    ca.setPosicionSeparador(getDataInteger(rs,"posicionSeparador"));
                    ca.setIgnoreIncomplete(getDataBoolean(rs,"ignoreIncomplete"));
                    ca.setPosicionHeader(getDataInteger(rs,"posicionHeader"));
                    
                    hsNombre.put(ca.getNombreCampo(), ca);
                    if (ca.getAliasCampo()!=null){
                        hsAlias.put(ca.getAliasCampo(), ca);
                    }
                    lstCampos.add(ca);
                }
                lst.add(hsNombre);
                lst.add(hsAlias);
                lst.add(lstCampos);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                                                "Problemas para obtención de datos");
            eh.setStrQuery(query);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtención de datos");
            eh.setStrQuery(query);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                StringBuffer textData=new StringBuffer();
                LogHandler log = new LogHandler();
                log.setStrQuery(query);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(),AdminFile.LOGFILESERVER),
                            new StringBuffer("getDataSimple"),
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
        return lst;
    }

    /**
     * Método que se encarga de obtener los Campos del Header para la carga
     * de archivos
     * @return  List    Listado con los campos
     * @throws ExceptionHandler
     */
    public List getCamposHeader() throws ExceptionHandler {
        List lst = new ArrayList();
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        HashMap hsNombre = new HashMap();
        List lstCampos = new ArrayList();
        try{
            getConection();
            query = this.getStrQuery();
            if (!"".equals(query)){
                st = this.conn.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()){
                    CargaArchivo ca = new CargaArchivo();
                    ca.setIdCampoHeader(getDataInteger(rs,"id_CampoHeader"));
                    ca.setNombreCampo(getDataString(rs,"nombreCampo"));
                    ca.setIdTipoCampo(getDataInteger(rs,"id_TipoCampo"));
                    ca.setTipoCampo(getDataString(rs,"tipoCampo"));
                    ca.setFila(getDataInteger(rs,"fila"));
                    ca.setPosicionInicio(getDataInteger(rs,"posicionInicio"));
                    ca.setLargo(getDataInteger(rs,"largo"));
                    ca.setFormato(getDataString(rs,"formato"));
                    ca.setTipoCampo(getDataString(rs,"tipoCampo"));
                    ca.setColumna(getDataInteger(rs,"columna"));
                   
                    hsNombre.put(ca.getNombreCampo(), ca);
                    lstCampos.add(ca);
                }
                lst.add(lstCampos);
                lst.add(hsNombre);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                                                "Problemas para obtención de datos");
            eh.setStrQuery(query);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtención de datos");
            eh.setStrQuery(query);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                StringBuffer textData=new StringBuffer();
                LogHandler log = new LogHandler();
                log.setStrQuery(query);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(),AdminFile.LOGFILESERVER),
                            new StringBuffer("getDataSimple"),
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
        return lst;
    }

    /**
     * Obtiene los datos adicionales de una tabla. Se entrega un resultado
     * distinto de null solo si vienen todos los datos.
     * @return  DataTable   Objeto con los datos adiconales de la tabla
     * @throws ExceptionHandler
     */
    public DataTable getTable() throws ExceptionHandler {
        DataTable  sld = null;
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try{
            getConection();
            query = this.getStrQuery();
            if (!"".equals(query)){
                st = this.conn.createStatement();
                rs = st.executeQuery(query);
                if (rs.next()){
                    DataTable dt = new DataTable();
                    dt.setNameTable(getDataString(rs,"nombreTabla"));
                    dt.setDominioTable(getDataString(rs,"dominio"));
                    dt.setBaseTable(getDataString(rs,"base"));
                    dt.setUserTable(getDataString(rs,"usuario"));
                    dt.setPassTable(getDataString(rs,"password"));
                    if (  (!"".equals(dt.getNameTable()))
                        &&(!"".equals(dt.getDominioTable()))
                        &&(!"".equals(dt.getBaseTable()))
                        &&(!"".equals(dt.getUserTable()))
                        &&(!"".equals(dt.getPassTable())) )
                    {
                        sld = dt;
                    }
                    
                }
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                                                "Problemas para obtención de datos de la tabla");
            eh.setStrQuery(query);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtención de datos de la tabla");
            eh.setStrQuery(query);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                StringBuffer textData=new StringBuffer();
                LogHandler log = new LogHandler();
                log.setStrQuery(query);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(),AdminFile.LOGFILESERVER),
                            new StringBuffer("getTable"),
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
        return sld;
    }

    /**
     * Método que se encarga de validar y filtrar un dato del tipo Integer,
     * obtenido desde un ResultSet
     * @param rs        ResultSet a revisar
     * @param campo     Nombre del campo buscado
     * @return  Integer Valor obtenido
     * @throws SQLException
     */
    private Integer getDataInteger(ResultSet rs, String campo) throws SQLException{
        String dato = "";
        Integer sld = null;
        dato = rs.getString(campo);
        if (dato!=null){
            sld = Integer.valueOf(dato);
        }
        return sld;
    }

    /**
     * Método que se encarga de validar y filtrar un dato del tipo String,
     * obtenido desde un ResultSet
     * @param rs        ResultSet a revisar
     * @param campo     Nombre del campo buscado
     * @return  Integer Valor obtenido
     * @throws SQLException
     */
    private String getDataString(ResultSet rs, String campo) throws SQLException{
        String sld = "";
        sld = rs.getString(campo);
        if (sld == null){
            sld = "";
        }
        return sld;
    }

    /**
     * Método que se encarga de validar y filtrar un dato del tipo boolean,
     * obtenido desde un ResultSet
     * @param rs        ResultSet a revisar
     * @param campo     Nombre del campo buscado
     * @return  Integer Valor obtenido
     * @throws SQLException
     */
    private boolean getDataBoolean(ResultSet rs, String campo) throws SQLException{
        boolean sld = false;
        String str = "";
        str = rs.getString(campo);
        if (str!=null){
            if ("1".equals(str)){
                sld = true;
            }
        }
        return sld;
    }

    /**
     * Método que se encarga de obtener los datos de los tipos de Archivo de
     * carga existentes en la Base de Datos
     * @return  List    Listado con los tipos de archivo
     * @throws ExceptionHandler
     */
    public List getListArchivo() throws ExceptionHandler {
        List lst = new ArrayList();
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try{
            getConection();
            query = this.getStrQuery();
            if (!"".equals(query)){
                st = this.conn.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()){
                    Archivo dato = new Archivo();
                    dato.setIdArchivoCarga(Integer.valueOf(rs.getString("id_archivoCarga")));
                    dato.setIdTabla(Integer.valueOf(rs.getString("id_Tabla")));
                    dato.setArchivoCarga(rs.getString("archivoCarga"));
                    dato.setIdTipoArchivoCarga(Integer.valueOf(rs.getString("id_TipoArchivoCarga")));
                    lst.add(dato);
                }
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                                                "Problemas para obtención de datos");
            eh.setStrQuery(query);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para obtención de datos");
            eh.setStrQuery(query);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                StringBuffer textData=new StringBuffer();
                LogHandler log = new LogHandler();
                log.setStrQuery(query);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(),AdminFile.LOGFILESERVER),
                            new StringBuffer("getDataSimple"),
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
        return lst;
    }

    /**
     * Método que se encarga de obtener un listado con el simbolo "?", el cual
     * se repite un número de veces equivalente al largo que se entrega
     * @param largo     Número de veces que debe ir el simbolo "?"
     * @return  String  Texto con el resultado
     */
    private String getVariables(int largo){
        String sld = "";

        for (int i=0;i<largo;i++){
            sld = sld + "?,";
        }
        if (sld.endsWith(",")){
            sld = sld.substring(0,sld.length()-1);
        }
        return sld;
    }

    /**
     * Método para la ejecución de un Store Procedure cualquiera, recibiendo una
     * cantidad variable de datos para su ejecución. El listado con los datos
     * posee a su vez un listado con dos datos, donde el primero es un String
     * con el nombre del tipo de dato del parametro y en el segundo va el objeto
     * respectivo con el valor.
     * @param nameProcedure     Texto con el nombre del Store Procedure
     * @param arrData           Listado con los datos del Store Procedure
     * @throws ExceptionHandler
     */
    public void executeProcedure(String nameProcedure, List listData) throws ExceptionHandler{
        CallableStatement proc = null;
        try{
            getConection();
            String textProc = "{ call "
                    + nameProcedure + "("
                    + getVariables(listData.size()) + ") }";

            proc = conn.prepareCall(textProc);
            for (int i =0;i<listData.size();i++){
                List lst = (List) listData.get(i);
                String tipo = (String) lst.get(0);
                if (("java.lang.String".equals(tipo))
                        || ("String".equals(tipo))) {
                    proc.setString(i+1, (String) lst.get(1));
                }else if (("java.lang.Integer".equals(tipo))
                        || ("Integer".equals(tipo))) {
                    proc.setInt(i+1, (Integer)lst.get(1));
                }else if (("java.lang.Double".equals(tipo))
                        || ("Double".equals(tipo))){
                    proc.setDouble(i+1, (Double) lst.get(1) );
                }else if (("java.sql.Date".equals(tipo))
                        || ("Date".equals(tipo))) {
                    proc.setDate(i+1, (Date) lst.get(1));
                }else{
                    proc.setString(i+1,null);
                }
            }
            proc.execute();
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                                                "Problemas para ejecutar el Store Procedure");
            eh.setStrQuery(nameProcedure);
            eh.setStringData(eh.getDataToXML());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                if (proc!=null){
                    proc.close();
                }
                this.conn.close();
            }catch(SQLException es){
                throw new ExceptionHandler(es,this.getClass(),
                                    "Problemas para cerrar conexión a la Base de Datos");
            }
        }
    }
}