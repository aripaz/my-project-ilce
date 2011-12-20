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
package mx.ilce.bitacora;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import mx.ilce.component.AdminFile;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.LogHandler;

/**
 * Clase implementada para la administración de los datos de la bitácora del
 * User y la Aplicación
 * @author ccatrilef
 */
public class AdmBitacora {

    private Connection conn;
    private String logDB;
    private String logDBProy;
    private String strQuery;
    private Bitacora bitacora;
    private Properties prop;
    private List lstVariables;
    private Integer intSld;

    private static File WORKING_DIRECTORY;
    private static String LOGBITACORA="LOGBITACORA";
    private static String LOGDB = "LOGDB";
    private static String LOGDBPROY = "LOGDBPROY";

    public static String AGREGAR = "AGREGAR";
    public static String ACTUALIZAR = "ACTUALIZAR";
    public static String ELIMINAR = "ELIMINAR";
    public static String CONSULTAR = "CONSULTAR";
    public static String LOGIN = "LOGIN";
    public static String LOGOUT = "LOGOUT";

    /**
     * Constructor del administrador de bitácora
     */
    public AdmBitacora() throws ExceptionHandler {
        inicializar();
    }

    /**
     * Obtiene el código numérico asignado como salida
     * @return  Integer     Valor asignado
     */
    public Integer getIntSld() {
        return intSld;
    }

    /**
     * Asigna el código numérico que se entregara como salida
     * @param intSld    Valor asignado
     */
    public void setIntSld(Integer intSld) {
        this.intSld = intSld;
    }

    /**
     * Obtiene el listado de variables de la bitácora
     * @return
     */
    public List getLstVariables() {
        return lstVariables;
    }

    /**
     * Agrega el listado de variables de la bitácora
     * @param lstVariables
     */
    public void setLstVariables(List lstVariables) {
        this.lstVariables = lstVariables;
    }

    /**
     * Obtiene el objeto Bitacora asignado
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
     * Obtiene el LogDB usado por el administrador
     * @return  String
     */
    public String getLogDB() {
        return ((logDB==null)?"":logDB);
    }

    /**
     * Asigna el LogDB que utilizara el administrador
     * @param logDB
     */
    public void setLogDB(String logDB) {
        this.logDB = ((logDB==null)?"":logDB);
    }

    /**
     * Obtiene el LogDBProy usado por el Administrador
     * @return
     */
    public String getLogDBProy() {
        return ((logDBProy==null)?"":logDBProy);
    }

    /**
     * Asigna el logDBProy usado por el administrador
     * @param logDBProy
     */
    public void setLogDBProy(String logDBProy) {
        this.logDBProy = ((logDBProy==null)?"":logDBProy);
    }

    /**
     * Obtiene la query a utilizar para el ingreso de datos
     * @return
     */
    public String getStrQuery() {
        return strQuery;
    }

    /**
     * Asigna la query a utilizar para el ingreso de datos
     * @param strQuery
     */
    public void setStrQuery(String strQuery) {
        this.strQuery = strQuery;
    }

    /**
     * Obtiene las properties asignadas al administrador
     * @return
     */
    public Properties getProp() {
        return prop;
    }

    /**
     * Asigna las properties que utilizara el administrador
     * @param prop  Objeto Propertie con los datos obtenidos
     */
    public void setProp(Properties prop) {
        this.prop = prop;
    }

    /**
     * Método para cargar los properties de la Bitácora
     * @return  Properties  Objeto Propertie con los datos obtenidos
     * @throws ExceptionHandler
     */
    private static Properties leerPropertie() throws ExceptionHandler{
        Properties prop = new Properties();
	InputStream is = null;
	File f = null;
        File fichero = null;
	try {
            String separador = String.valueOf(File.separator);
            URL url = AdmBitacora.class.getResource("AdmBitacora.class");

            if(url.getProtocol().equals("file")) {
		f = new File(url.toURI());
		f = f.getParentFile().getParentFile();
		f = f.getParentFile().getParentFile();
		WORKING_DIRECTORY = f.getParentFile();
            }
            fichero = new File(WORKING_DIRECTORY + separador + "bitacora.properties");
            if (fichero.exists()){
                is=new FileInputStream(fichero.getAbsolutePath());
                prop.load(is);
            }
        } catch(URISyntaxException u){
            throw new ExceptionHandler(u,AdminFile.class,
                    "Problemas para leer el archivo de properties");
	} catch(IOException e) {
            throw new ExceptionHandler(e,AdminFile.class,
                    "Problemas para leer el archivo de properties");
	}finally{
            try {
                if (is != null){
                    is.close();
                }
            }catch (Exception e){
                throw new ExceptionHandler(e,AdminFile.class,
                        "Problemas para cerrar el archivo de properties");
            }
        }
	return prop;
    }

    /**
     * Obtiene el valor de una palabra clave (key), desde un arreglo de
     * properties
     * @param prop      Listado de properties obtenido desde el archivo de configuracion
     * @param strKey    Palabra usada como Key para la busqueda dentro del propertie
     * @return
     */
    public static String getKey(Properties prop, String key) throws ExceptionHandler{
        String sld = "";
	try{
            Enumeration e = prop.keys();
            if (e.hasMoreElements()){
                sld = prop.getProperty(key);
            }
	}catch(Exception e){
            throw new ExceptionHandler(e,AdminFile.class,
                    "Problemas para obtener la llave desde el properties");
	}
        return sld;
    }

    /**
     * Realiza la conexión a la base de datos. Los parámetros de conexión se
     * obtienen de un properties para una fácil mantención sin compilar
     * @throws SQLException
     */
    private void getConection() throws SQLException, ExceptionHandler{
        StringBuilder strConexion = new StringBuilder();
        try {
            if (this.getProp()!=null){
                String server = getKey(this.getProp(),"SERVER");
                String base = getKey(this.getProp(),"BASE");
                String port = getKey(this.getProp(),"PORT");
                String user = getKey(this.getProp(),"USR");
                String psw = getKey(this.getProp(),"PSW");

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
     * Validación de la query para comprobar que es un INSERT en las tablas autorizadas
     * @return
     */
    private boolean validateInsert(){
        boolean sld=false;
        String strData = this.getStrQuery().toUpperCase();

        String[] strDB = null;
        if (strData.contains(this.getLogDB().toUpperCase())){
            strDB = strData.split(this.getLogDB().toUpperCase());
        }
        String[] strDBProy = null;
        if (strData.contains(this.getLogDBProy().toUpperCase())){
            strDB = strData.split(this.getLogDBProy().toUpperCase());
        }
        if (strDB!=null){
            String strHeadXXX = "XXX" + ((strDB[0]==null)?"":strDB[0].toUpperCase().trim());
            String[] strHead = (strHeadXXX).split("INSERT");
            if ((strHead[0].equals("XXX"))&&(strHead.length==2)){
                sld = true;
            }
        }else if (strDBProy!=null){
            String strHeadXXX = "XXX" + ((strDBProy[0]==null)?"":strDBProy[0].toUpperCase().trim());
            String[] strHead = (strHeadXXX).split("INSERT");
            if ((strHead[0].equals("XXX"))&&(strHead.length==2)){
                sld = true;
            }
        }
        return sld;
    }

    /**
     * Método para ejecutar un INSERT en la tabla de bitácora
     * @return  Integer     Valor con el resultado de la operación
     * @throws ExceptionHandler
     */
    private Integer executeInsert() throws ExceptionHandler{
        Statement st = null;
        ResultSet rs = null;
        Integer increment =Integer.valueOf(-1);
        try{
            if(validateInsert()){
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
     * Validación de la query para comprobar que es un UPDATE en las tablas autorizadas
     * @return
     */
    private boolean validateUpdate(){
        boolean sld=false;
        String strData = this.getStrQuery().toUpperCase();

        String[] strDB = null;
        if (strData.contains(this.getLogDB().toUpperCase())){
            strDB = strData.split(this.getLogDB().toUpperCase());
        }
        String[] strDBProy = null;
        if (strData.contains(this.getLogDBProy().toUpperCase())){
            strDB = strData.split(this.getLogDBProy().toUpperCase());
        }
        if (strDB!=null){
            String strHeadXXX = "XXX" + ((strDB[0]==null)?"":strDB[0].toUpperCase().trim());
            String[] strHead = (strHeadXXX).split("UPDATE");
            if ((strHead[0].equals("XXX"))&&(strHead.length==2)){
                sld = true;
            }
        }else if (strDBProy!=null){
            String strHeadXXX = "XXX" + ((strDBProy[0]==null)?"":strDBProy[0].toUpperCase().trim());
            String[] strHead = (strHeadXXX).split("UPDATE");
            if ((strHead[0].equals("XXX"))&&(strHead.length==2)){
                sld = true;
            }
        }
        return sld;
    }

    /**
     * Método para ejecutar un UPDATE en la tabla de bitácora
     * @return  Integer     Valor con el resultado de la operación
     * @throws ExceptionHandler
     */
    private Integer executeUpdate() throws ExceptionHandler{
        Statement st = null;
        Integer increment =Integer.valueOf(-1);
        try{
            if (validateUpdate()){
                getConection();
                if (this.conn!=null){
                    st = this.conn.createStatement();
                    increment = st.executeUpdate(this.getStrQuery());
                }
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para ejecutar UPDATE");
            eh.setStrQuery(this.getStrQuery());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para ejecutar UPDATE");
            eh.setStrQuery(this.getStrQuery());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
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
        return increment;
    }

    /**
     * Validación de la query para comprobar que es un DELETE en las tablas autorizadas
     * @return
     */
    private boolean validateDelete(){
        boolean sld=false;
        String strData = this.getStrQuery().toUpperCase();

        String[] strDB = null;
        if (strData.contains(this.getLogDB().toUpperCase())){
            strDB = strData.split(this.getLogDB().toUpperCase());
        }
        String[] strDBProy = null;
        if (strData.contains(this.getLogDBProy().toUpperCase())){
            strDB = strData.split(this.getLogDBProy().toUpperCase());
        }
        if (strDB!=null){
            String strHeadXXX = "XXX" + ((strDB[0]==null)?"":strDB[0].toUpperCase().trim());
            String[] strHead = (strHeadXXX).split("DELETE");
            if ((strHead[0].equals("XXX"))&&(strHead.length==2)){
                sld = true;
            }
        }else if (strDBProy!=null){
            String strHeadXXX = "XXX" + ((strDBProy[0]==null)?"":strDBProy[0].toUpperCase().trim());
            String[] strHead = (strHeadXXX).split("DELETE");
            if ((strHead[0].equals("XXX"))&&(strHead.length==2)){
                sld = true;
            }
        }
        return sld;
    }

    /**
     * Método para ejecutar un DELETE de un registro en la tabla de bitácora
     * @return  Integer     Valor con el resultado de la operación
     * @throws ExceptionHandler
     */
    private Integer executeDelete() throws ExceptionHandler{
        Statement st = null;
        Integer increment =Integer.valueOf(-1);
        try{
            if (validateDelete()){
                getConection();
                if (this.conn!=null){
                    st = this.conn.createStatement();
                    increment = st.executeUpdate(this.getStrQuery());
                }
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para ejecutar DELETE");
            eh.setStrQuery(this.getStrQuery());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para ejecutar DELETE");
            eh.setStrQuery(this.getStrQuery());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                if (st!=null){
                    st.close();
                }
                this.conn.close();
            }catch(SQLException es){
                throw new ExceptionHandler(es,this.getClass(),
                        "Problemas para cerrar conexión a Base de Datos");
            }
        }
        return increment;
    }

    /**
     * Inicializa las variables que se requieren para la operación con el
     * administrador
     * @throws ExceptionHandler
     */
    private void inicializar() throws ExceptionHandler{
        Properties propDB = AdmBitacora.leerPropertie();
        this.setProp(propDB);

        setLogDB(getKey(propDB, AdmBitacora.LOGDB));
        setLogDBProy(getKey(propDB, AdmBitacora.LOGDBPROY));
    }

    /**
     * Método para el registro de login del usuario
     * @return  boolean     Valor con el resultado de la operación
     */
    public boolean login() throws ExceptionHandler{
        boolean sld = false;
        StringBuffer textData=new StringBuffer();
        if (this.getBitacora()!=null){
            try {
                StringBuilder strData = new StringBuilder();
                StringBuffer strCampos = new StringBuffer("(");
                StringBuffer strValues = new StringBuffer("(");
                String strTabla = this.getLogDB();

                strCampos.append("fecha,");
                strValues.append("getdate(),");

                if (this.getBitacora().getClaveEmpleado()!=null){
                    strCampos.append("clave_empleado,");
                    strValues.append(this.getBitacora().getClaveEmpleado())
                             .append(",");
                     textData.append("\tCLAVE_EMPLEADO: ")
                             .append(this.getBitacora().getClaveEmpleado())
                             .append("\n");
                }
                if (this.getBitacora().getIp()!=null){
                    strCampos.append("ip,");
                    strValues.append("'")
                             .append(this.getBitacora().getIp())
                             .append("',");
                     textData.append("\tIP: ")
                             .append(this.getBitacora().getIp())
                             .append("\n");
                }
                if (this.getBitacora().getNavegador()!=null){
                    strCampos.append("navegador,");
                    strValues.append("'")
                             .append(this.getBitacora().getNavegador().substring(0,50))
                             .append("',");
                     textData.append("\tNAVEGADOR: ")
                             .append(this.getBitacora().getNavegador().substring(0,50))
                             .append("\n");
                }
                if (this.getBitacora().getError()!=null){
                    strCampos.append("error");
                    strValues.append("'")
                             .append(("LOGIN: " + this.getBitacora().getError()))
                             .append("'");
                     textData.append("\tERROR: ")
                             .append(("LOGIN: " + this.getBitacora().getError()))
                             .append("\n");
                }
                String lastChar = strCampos.substring(strCampos.length()-1);
                if (",".equals(lastChar)){
                    strCampos.replace(strCampos.length()-1, strCampos.length(), " ");
                    strValues.replace(strValues.length()-1, strValues.length(), " ");
                }
                strCampos.append(")");
                strValues.append(")");

                strData.append("insert into ")
                       .append(strTabla)
                       .append(" ")
                       .append(strCampos)
                       .append(" values ")
                       .append(strValues);

                this.setStrQuery(strData.toString());
                Integer integerSld = this.executeInsert();

                if (integerSld>0){
                    sld = true;
                }
            } finally {
                try{
                    LogHandler log = new LogHandler();
                    log.setBoolSel(false);
                    
                    log.setStrQuery(this.getStrQuery());
                    log.logData(getKey(this.getProp(), AdmBitacora.LOGBITACORA),
                                new StringBuffer("login"),
                                textData,
                                "BITACORA");
                }catch(Exception ex){
                    ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                              "Problemas al escribir en la Bitacora de Login");
                    eh.setDataToXML(this.getBitacora());
                    eh.setStringData(eh.getDataToXML());
                    eh.setSeeStringData(true);
                    throw eh;
                }
            }
        }
        return sld;
    }

    /**
     * Método para el registro del logout del usuario
     * @return  boolean     Valor con el resultado de la operación
     */
    public boolean logout() throws ExceptionHandler{
        boolean sld = true;
        StringBuffer textData=new StringBuffer();
        try {
            StringBuilder strData = new StringBuilder();
            StringBuffer strCampos = new StringBuffer("(");
            StringBuffer strValues = new StringBuffer("(");
            String strTabla = this.getLogDB();

            strCampos.append("fecha,");
            strValues.append("getdate(),");

            if (this.getBitacora().getClaveEmpleado()!=null){
                strCampos.append("clave_empleado,");
                strValues.append(this.getBitacora().getClaveEmpleado())
                         .append(",");
                 textData.append("\tCLAVE_EMPLEADO: ")
                         .append(this.getBitacora().getClaveEmpleado())
                         .append("\n");
            }
            if (this.getBitacora().getIp()!=null){
                strCampos.append("ip,");
                strValues.append("'")
                         .append(this.getBitacora().getIp())
                         .append("',");
                 textData.append("\tIP: ")
                         .append(this.getBitacora().getIp())
                         .append("\n");
            }
            if (this.getBitacora().getNavegador()!=null){
                strCampos.append("navegador,");
                strValues.append("'")
                         .append(this.getBitacora().getNavegador())
                         .append("',");
                 textData.append("\tNAVEGADOR: ")
                         .append(this.getBitacora().getNavegador())
                         .append("\n");
            }
            if (this.getBitacora().getError()!=null){
                strCampos.append("error");
                strValues.append("'")
                         .append(("LOGOUT: " + this.getBitacora().getError()))
                         .append("'");
                 textData.append("\tERROR: ")
                         .append(("LOGOUT: " + this.getBitacora().getError()))
                         .append("\n");
            }
            String lastChar = strCampos.substring(strCampos.length()-1);
            if (",".equals(lastChar)){
                strCampos.replace(strCampos.length()-1, strCampos.length(), " ");
                strValues.replace(strValues.length()-1, strValues.length(), " ");
            }
            strCampos.append(")");
            strValues.append(")");

            strData.append("insert into ")
                   .append(strTabla)
                   .append(" ")
                   .append(strCampos)
                   .append(" values ")
                   .append(strValues);

            this.setStrQuery(strData.toString());
            Integer integerSld = this.executeInsert();

            if (integerSld>0){
                sld = true;
            }
        } finally {
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getStrQuery());
                log.logData(getKey(this.getProp(), AdmBitacora.LOGBITACORA),
                            new StringBuffer("logout"),
                            textData,
                            "BITACORA");
            }catch(Exception ex){
                ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                          "Problemas al escribir en la Bitacora de Logout");
                eh.setDataToXML(this.getBitacora());
                eh.setStringData(eh.getDataToXML());
                eh.setSeeStringData(true);
                throw eh;
            }
        }
        return sld;
    }

    /**
     * Método para agregar un evento a la bitácora
     * @return
     */
    public boolean addBitacora()throws ExceptionHandler{
        boolean sld = false;
        StringBuffer textData=new StringBuffer();
        if ( this.getBitacora()!=null){
            try {
                StringBuilder strData = new StringBuilder();
                StringBuffer strCampos = new StringBuffer("(");
                StringBuffer strValues = new StringBuffer("(");
                String strTabla = this.getLogDBProy();
                Integer claveConsulta = Integer.valueOf(getKey(this.getProp(),
                                                        AdmBitacora.CONSULTAR));

                strCampos.append("fecha_bitacora,");
                strValues.append("getdate(),");

                if (this.getBitacora().getBitacora()!=null){
                    strCampos.append("bitacora,");
                    strValues.append("'")
                             .append(replaceChar(this.getBitacora().getBitacora()))
                             .append("',");
                     textData.append("\tDESCRIPCION: ")
                             .append(this.getBitacora().getBitacora())
                             .append("\n");
                }
                if (this.getBitacora().getClaveAplicacion()!=null){
                    strCampos.append("clave_aplicacion,");
                    strValues.append(this.getBitacora().getClaveAplicacion())
                             .append(",");
                     textData.append("\tCLAVE_APLICACION: ")
                             .append(this.getBitacora().getClaveAplicacion())
                             .append("\n");
                }
                if (this.getBitacora().getClaveRegistro()!=null){
                    strCampos.append("clave_registro,");
                    strValues.append(this.getBitacora().getClaveRegistro())
                             .append(",");
                     textData.append("\tCLAVE_REGISTRO: ")
                             .append(this.getBitacora().getClaveRegistro())
                             .append("\n");
                }
                if (this.getBitacora().getClaveEmpleado()!=null){
                    strCampos.append("clave_empleado,");
                    strValues.append(this.getBitacora().getClaveEmpleado())
                             .append(",");
                     textData.append("\tCLAVE_EMPLEADO: ")
                             .append(this.getBitacora().getClaveEmpleado())
                             .append("\n");
                }
                if (this.getBitacora().getClaveForma()!=null){
                    strCampos.append("clave_forma,");
                    strValues.append(this.getBitacora().getClaveForma())
                             .append(",");
                     textData.append("\tCLAVE_FORMA: ")
                             .append(this.getBitacora().getClaveForma())
                             .append("\n");
                }
                if (this.getBitacora().getClaveTipoEvento()!=null){
                    strCampos.append("clave_tipo_evento,");
                    strValues.append(this.getBitacora().getClaveTipoEvento())
                             .append(",");
                     textData.append("\tCLAVE_TIPO_EVENTO: ")
                             .append(this.getBitacora().getClaveTipoEvento())
                             .append("\n");
                }
                if (this.getBitacora().getClaveTipoEvento()!=claveConsulta){
                    if (this.getBitacora().getConsulta()!=null){
                        strCampos.append("consulta");
                        strValues.append("'")
                                 .append(replaceChar(this.getBitacora().getConsulta()))
                                 .append("'");
                         textData.append("\tCONSULTA:\n\t")
                                 .append(this.getBitacora().getConsulta())
                                 .append("\n");
                    }
                }

                String lastChar = strCampos.substring(strCampos.length()-1);
                if (",".equals(lastChar)){
                    strCampos.replace(strCampos.length()-1, strCampos.length(), " ");
                    strValues.replace(strValues.length()-1, strValues.length(), " ");
                }
                
                strCampos.append(")");
                strValues.append(")");

                strData.append("insert into ");
                strData.append(strTabla);
                strData.append(" ");
                strData.append(strCampos);
                strData.append(" values ");
                strData.append(strValues);

                this.setStrQuery(strData.toString());
                Integer integerSld = this.executeInsert();

                if (integerSld>0){
                    sld = true;
                    this.setIntSld(integerSld);
                }
            } finally {
                try{
                    LogHandler log = new LogHandler();
                    log.setBoolSel(false);
                    log.setStrQuery(this.getStrQuery());
                    log.logData(getKey(this.getProp(), AdmBitacora.LOGBITACORA),
                                new StringBuffer("addBitacora"),
                                textData,
                                "BITACORA");
                }catch(Exception ex){
                    ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                              "Problemas al escribir en la Bitacora de Aplicación");
                    eh.setDataToXML(this.getBitacora());
                    eh.setStringData(eh.getDataToXML());
                    eh.setSeeStringData(true);
                    throw eh;
                }
            }
        }
        return sld;
    }

    /**
     * Método para realizar un UPDATE en la Bitacora
     * @return
     */
    private boolean updateBitacora() throws ExceptionHandler{
        boolean sld = true;
        StringBuffer textData=new StringBuffer();
        try {
            StringBuilder strData = new StringBuilder("");
            StringBuffer strCampos = new StringBuffer("");
            String strTabla = this.getLogDBProy();

            if (this.getBitacora().getFechaBitacora()!=null){
                strCampos.append(" fecha_bitacora = '")
                         .append(this.getBitacora().getFechaBitacora())
                         .append("',");
                 textData.append("\tFECHA BITACORA: ")
                         .append(this.getBitacora().getFechaBitacora())
                         .append("\n");
            }
            if (this.getBitacora().getBitacora()!=null){
                strCampos.append(" bitacora = '")
                         .append(replaceChar(this.getBitacora().getBitacora()))
                         .append("',");
                 textData.append("\tBITACORA: ")
                         .append(replaceChar(this.getBitacora().getBitacora()))
                         .append("\n");
            }
            if (this.getBitacora().getConsulta()!=null){
                strCampos.append(" consulta = '")
                         .append(replaceChar(this.getBitacora().getConsulta()))
                         .append("',");
                 textData.append("\tCONSULTA: ")
                         .append(replaceChar(this.getBitacora().getConsulta()))
                         .append("\n");
            }
            if (this.getBitacora().getClaveAplicacion()!=null){
                strCampos.append(" clave_aplicacion = ")
                         .append(this.getBitacora().getClaveAplicacion())
                         .append(",");
                 textData.append("\tCLAVE APLICACION: ")
                         .append(this.getBitacora().getClaveAplicacion())
                         .append("\n");
            }
            if (this.getBitacora().getClaveForma()!=null){
                strCampos.append(" clave_forma = ")
                         .append(this.getBitacora().getClaveForma())
                         .append(",");
                 textData.append("\tCLAVE FORMA: ")
                         .append(this.getBitacora().getClaveForma())
                         .append("\n");
            }
            if (this.getBitacora().getClaveRegistro()!=null){
                strCampos.append(" clave_registro = ")
                         .append(this.getBitacora().getClaveRegistro())
                         .append(",");
                 textData.append("\tCLAVE REGISTRO: ")
                         .append(this.getBitacora().getClaveRegistro())
                         .append("\n");
            }
            if (this.getBitacora().getClaveEmpleado()!=null){
                strCampos.append(" clave_empleado = ")
                         .append(this.getBitacora().getClaveEmpleado())
                         .append(",");
                 textData.append("\tCLAVE EMPLEADO: ")
                         .append(this.getBitacora().getClaveEmpleado())
                         .append("\n");
            }
            if (this.getBitacora().getClaveForma()!=null){
                strCampos.append(" clave_forma = ")
                         .append(this.getBitacora().getClaveForma())
                         .append(",");
                 textData.append("\tCLAVE FORMA: ")
                         .append(this.getBitacora().getClaveForma())
                         .append("\n");
            }
            if (this.getBitacora().getClaveTipoEvento()!=null){
                strCampos.append(" clave_tipo_evento = ")
                         .append(this.getBitacora().getClaveTipoEvento());
                 textData.append("\tCLAVE TIPO EVENTO: ")
                         .append(this.getBitacora().getClaveTipoEvento())
                         .append("\n");
            }

            String lastChar = strCampos.substring(strCampos.length()-1);
            if (",".equals(lastChar)){
                strCampos.replace(strCampos.length()-1, strCampos.length(), " ");
            }

            strData.append("update ");
            strData.append(strTabla);
            strData.append(" set ");
            strData.append(strCampos);
            strData.append(" where clave_bitacora_proyecto = ");
            strData.append(this.getBitacora().getClaveBitacoraProyecto());

            this.setStrQuery(strData.toString());
            Integer integerSld = this.executeUpdate();

            if (integerSld>0){
                sld = true;
            }
        } finally {
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                
                log.setStrQuery(this.getStrQuery());
                log.logData(getKey(this.getProp(), AdmBitacora.LOGBITACORA),
                            new StringBuffer("updateBitacora"),
                            textData,
                            "BITACORA");
            }catch(Exception ex){
                ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                          "Problemas al actualizar en la Bitacora de Aplicación");
                eh.setDataToXML(this.getBitacora());
                eh.setStringData(eh.getDataToXML());
                eh.setSeeStringData(true);
                throw eh;
            }
        }
        return sld;
    }

    /**
     * Método para invocar un DELETE en la bitácora
     * @return  boolean     Valor con el resultado de la operación
     * @throws ExceptionHandler
     */
    private boolean deleteBitacora()throws ExceptionHandler{
        boolean sld = true;
        StringBuffer textData=new StringBuffer();
        try {
            StringBuilder strData = new StringBuilder();
            String strTabla = this.getLogDBProy();

            strData.append("delete from ")
                   .append(strTabla)
                   .append(" where clave_bitacora_proyecto = ")
                   .append(this.getBitacora().getClaveBitacoraProyecto());

            textData.append("\tCLAVE BORRADA: ")
                    .append(this.getBitacora().getClaveBitacoraProyecto())
                    .append("\n");

            this.setStrQuery(strData.toString());
            Integer integerSld = this.executeInsert();

            if (integerSld>0){
                sld = true;
            }
        } finally {
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                
                log.setStrQuery(this.getStrQuery());
                log.logData(getKey(this.getProp(), AdmBitacora.LOGBITACORA),
                            new StringBuffer("deleteBitacora"),
                            textData,
                            "BITACORA");
            }catch(Exception ex){
                ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                          "Problemas al borrar en la Bitacora de Aplicacion");
                eh.setDataToXML(this.getBitacora());
                eh.setStringData(eh.getDataToXML());
                eh.setSeeStringData(true);
                throw eh;
            }
        }
        return sld;
    }

    /**
     * Método para manejar caracteres especiales que se requieren reemplazar
     * antes de enviarlos como data en una query, por ejemplo: (') por ('')
     * @param strData
     * @return
     */
    private String replaceChar(String strData){
        String strSld = "";
        strSld = strData;
        strSld = strSld.replaceAll("'", "''");
        return strSld;
    }

    /**
     * Método para agregar las variables asociadas a un INSERT o un UPDATE.
     * Retorna el número de campos operados exitosamente
     * @param strClaveBitacoraProy
     * @return  Integer     Número de campos agregados
     * @throws ExceptionHandler
     */
    public Integer addVariablesBitacora(Integer strClaveBitacoraProy)throws ExceptionHandler{
        Integer sld = 0;
        StringBuffer textData=new StringBuffer();
        if ( this.getBitacora()!=null){
            try {
                if (this.getLstVariables()!=null){
                    Iterator it = this.getLstVariables().iterator();
                    int i = 1;
                    while (it.hasNext()){
                        String[] strVar = (String[]) it.next();
                        StringBuilder query = new StringBuilder();
                        query.append("insert into Bitacora_variable (")
                             .append("clave_bitacora_proyecto,")
                             .append("nombre_variable,")
                             .append("alias_variable,")
                             .append("valor_variable,")
                             .append("tipo_variable) values (")
                             .append(strClaveBitacoraProy).append(",'")
                             .append(strVar[0]).append("','")
                             .append(strVar[1]).append("','")
                             .append(strVar[2]).append("','")
                             .append(strVar[3]).append("')");

                        textData.append(("\tDATA[" + i++ + "]:"))
                                .append(" Clave=")
                                .append(strClaveBitacoraProy)
                                .append(" Nombre=")
                                .append(strVar[0])
                                .append(" Alias=")
                                .append(strVar[1])
                                .append(" Valor=")
                                .append(strVar[2])
                                .append(" Tipo=")
                                .append(strVar[3])
                                .append("\n");

                        this.setStrQuery(query.toString());
                        Integer integerSld = 0;
                        try{
                            integerSld = executeInsert();
                        }catch(Exception e){}
                        if (integerSld>0){
                            sld++;
                        }
                    }
                }
            } finally {
                try{
                    LogHandler log = new LogHandler();
                    log.setBoolSel(false);
                    
                    log.setStrQuery(this.getStrQuery());
                    log.logData(getKey(this.getProp(), AdmBitacora.LOGBITACORA),
                                new StringBuffer("addVariablesBitacora"),
                                textData,
                                "BITACORA");
                }catch(Exception ex){
                    ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                              "Problemas al escribir las variables de la Bitacora de Aplicación");
                    eh.setDataToXML("CLAVE BITACORA",strClaveBitacoraProy);
                    eh.setDataToXML(this.getLstVariables());
                    eh.setStringData(eh.getDataToXML());
                    eh.setSeeStringData(true);
                    throw eh;
                }
            }
        }
        return sld;
    }
}
