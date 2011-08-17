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
 * Clase implementada para la administracion de los datos de Bitacora
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
     * Constructor del adminitrador de bitacora
     */
    public AdmBitacora() throws ExceptionHandler {
        inicializar();
    }

    public Integer getIntSld() {
        return intSld;
    }

    public void setIntSld(Integer intSld) {
        this.intSld = intSld;
    }

    /**
     * Obtiene el listado de variables de la bitacora
     * @return
     */
    public List getLstVariables() {
        return lstVariables;
    }

    /**
     * Agrega el listado de variables de la bitacora
     * @param lstVariables
     */
    public void setLstVariables(List lstVariables) {
        this.lstVariables = lstVariables;
    }

    /**
     * Obtiene el objeto Bitacora asignado al administrador de Bitacora
     * @return
     */
    public Bitacora getBitacora() {
        return bitacora;
    }

    /**
     * Asigna el objeto Bitacora para el administrador  de Bitacora
     * @param bitacora
     */
    public void setBitacora(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

    /**
     * Obtiene el LogDB usado por el administrador de Bitacora
     * @return
     */
    public String getLogDB() {
        return ((logDB==null)?"":logDB);
    }

    /**
     * Asigna el LogDB que utilizara el administrador de Bitacora
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
     * Obtiene las properties asignadas al administrador de bitacora
     * @return
     */
    public Properties getProp() {
        return prop;
    }

    /**
     * Asigna las properties que utilizara el administrador de bitacora
     * @param prop
     */
    public void setProp(Properties prop) {
        this.prop = prop;
    }

    /**
     * Metodo para cargar los properties de la Bitacora
     * @return
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
            throw new ExceptionHandler(u,AdminFile.class,"Problemas para leer el archivo de properties");
	} catch(IOException e) {
            throw new ExceptionHandler(e,AdminFile.class,"Problemas para leer el archivo de properties");
	}finally{
            try {
                if (is != null){
                    is.close();
                }
            }catch (Exception e){
                throw new ExceptionHandler(e,AdminFile.class,"Problemas para cerrar el archivo de properties");
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
            throw new ExceptionHandler(e,AdminFile.class,"Problemas para obtener la llave desde el properties");
	}
        return sld;
    }

    /**
     * Realiza la conexion a la base de datos. Los parametros de conexion se
     * obtienen de un properties para una facil mantencion sin compilar.
     * @throws SQLException
     */
    private void getConexion() throws SQLException, ExceptionHandler{
        StringBuffer strConexion = new StringBuffer();
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
            throw new ExceptionHandler(sqlex,this.getClass(),"Problemas para abrir Conexion a Base de datos");
        } catch (ClassNotFoundException ex) {
            throw new ExceptionHandler(ex,this.getClass(),"No se encontro los Driver de Conexion");
        }catch (Exception e){
            throw new ExceptionHandler(e,this.getClass(),"Problemas para abrir Conexion a Base de datos");
        }
    }

    /**
     * Validacion de la query para comprobar que es un insert en las tablas autorizadas
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
     * Permite ejecutar una query de insert en la tabla de bitacora
     * @return
     * @throws ExceptionHandler
     */
    private Integer executeInsert() throws ExceptionHandler{
        Statement st = null;
        ResultSet rs = null;
        Integer increment =Integer.valueOf(-1);
        try{
            if(validateInsert()){
                getConexion();
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
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),"Problemas para ejecutar INSERT");
            eh.setStrQuery(strQuery);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),"Problemas para ejecutar INSERT");
            eh.setStrQuery(strQuery);
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
                throw new ExceptionHandler(es,this.getClass(),"Problemas para cerrar Conexion a Base de datos");
            }
        }
        return increment;
    }

    /**
     * Validacion de la query para comprobar que es un update en las tablas autorizadas
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
     * Permite hacer una actualizacion en la tabla de bitacora
     * @return
     * @throws ExceptionHandler
     */
    private Integer executeUpdate() throws ExceptionHandler{
        Statement st = null;
        Integer increment =Integer.valueOf(-1);
        try{
            if (validateUpdate()){
                getConexion();
                if (this.conn!=null){
                    st = this.conn.createStatement();
                    increment = st.executeUpdate(this.getStrQuery());
                }
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),"Problemas para ejecutar UPDATE");
            eh.setStrQuery(strQuery);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),"Problemas para ejecutar UPDATE");
            eh.setStrQuery(strQuery);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                if (st!=null){
                    st.close();
                }
                this.conn.close();
            }catch(SQLException es){
                throw new ExceptionHandler(es,this.getClass(),"Problemas para cerrar Conexion a Base de datos");
            }
        }
        return increment;
    }

    /**
     * Validacion de la query para comprobar que es un delete en las tablas autorizadas
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
     * Permite hacer una eliminacion de un registro desde la tabla de bitacora
     * @return
     * @throws ExceptionHandler
     */
    private Integer executeDelete() throws ExceptionHandler{
        Statement st = null;
        Integer increment =Integer.valueOf(-1);
        try{
            if (validateDelete()){
                getConexion();
                if (this.conn!=null){
                    st = this.conn.createStatement();
                    increment = st.executeUpdate(this.getStrQuery());
                }
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),"Problemas para ejecutar DELETE");
            eh.setStrQuery(strQuery);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),"Problemas para ejecutar DELETE");
            eh.setStrQuery(strQuery);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                if (st!=null){
                    st.close();
                }
                this.conn.close();
            }catch(SQLException es){
                throw new ExceptionHandler(es,this.getClass(),"Problemas para cerrar Conexion a Base de datos");
            }
        }
        return increment;
    }

    /**
     * Inicializa las variables que se requieren para la operacion con el
     * administrador
     * @throws ExceptionHandler
     */
    private void inicializar() throws ExceptionHandler{
        Properties propDB = this.leerPropertie();
        this.setProp(propDB);

        setLogDB(getKey(propDB, this.LOGDB));
        setLogDBProy(getKey(propDB, this.LOGDBPROY));
    }

    /**
     * Metodo para el registro de login del usuario
     * @return
     */
    public boolean login() throws ExceptionHandler{
        boolean sld = false;
        if (this.getBitacora()!=null){
            try {
                StringBuffer strData = new StringBuffer();
                StringBuffer strCampos = new StringBuffer("(");
                StringBuffer strValues = new StringBuffer("(");
                String strTabla = this.getLogDB();

                strCampos.append("fecha,");
                strValues.append("getdate(),");

                if (this.getBitacora().getClaveEmpleado()!=null){
                    strCampos.append("clave_empleado,");
                    strValues.append(this.getBitacora().getClaveEmpleado());
                    strValues.append(",");
                }
                if (this.getBitacora().getIp()!=null){
                    strCampos.append("ip,");
                    strValues.append("'");
                    strValues.append(this.getBitacora().getIp());
                    strValues.append("',");
                }
                if (this.getBitacora().getNavegador()!=null){
                    strCampos.append("navegador,");
                    strValues.append("'");
                    strValues.append(this.getBitacora().getNavegador());
                    strValues.append("',");
                }
                if (this.getBitacora().getError()!=null){
                    strCampos.append("error");
                    strValues.append("'");
                    strValues.append(this.getBitacora().getError());
                    strValues.append("'");
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
                Integer intSld = this.executeInsert();

                if (intSld>0){
                    sld = true;
                }
            } finally {
                try{
                    LogHandler log = new LogHandler();
                    log.setBoolSel(false);
                    StringBuffer textData=new StringBuffer();
                    log.setStrQuery(this.getStrQuery());
                    log.logData(getKey(this.getProp(), this.LOGBITACORA),
                                new StringBuffer("login"),textData,"BITACOR");
                }catch(Exception ex){
                    throw new ExceptionHandler(ex,this.getClass(),"Problemas al excribir la Bitacora de Login");
                }
            }
        }
        return sld;
    }

    /**
     * Metodo para el registro del logout del usuario
     * @return
     */
    public boolean logout() throws ExceptionHandler{
        boolean sld = true;
        try {
            StringBuffer strData = new StringBuffer();
            StringBuffer strCampos = new StringBuffer("(");
            StringBuffer strValues = new StringBuffer("(");
            String strTabla = this.getLogDB();

            strCampos.append("fecha,");
            strValues.append("getdate(),");

            if (this.getBitacora().getClaveEmpleado()!=null){
                strCampos.append("clave_empleado,");
                strValues.append(this.getBitacora().getClaveEmpleado());
                strValues.append(",");
            }
            if (this.getBitacora().getIp()!=null){
                strCampos.append("ip,");
                strValues.append("'");
                strValues.append(this.getBitacora().getIp());
                strValues.append("',");
            }
            if (this.getBitacora().getNavegador()!=null){
                strCampos.append("navegador,");
                strValues.append("'");
                strValues.append(this.getBitacora().getNavegador());
                strValues.append("',");
            }
            if (this.getBitacora().getError()!=null){
                strCampos.append("error");
                strValues.append("'");
                strValues.append(this.getBitacora().getError());
                strValues.append("'");
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
            Integer intSld = this.executeInsert();

            if (intSld>0){
                sld = true;
            }
        } finally {
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                StringBuffer textData=new StringBuffer();
                log.setStrQuery(this.getStrQuery());
                log.logData(getKey(this.getProp(), this.LOGBITACORA),
                            new StringBuffer("logout"),textData,"BITACOR");
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),"Problemas al excribir la Bitacora de Logout");
            }
        }
        return sld;
    }

    /**
     * Metodo para agregar un evento a la bitacora
     * @return
     */
    public boolean addBitacora()throws ExceptionHandler{
        boolean sld = false;
        if ( this.getBitacora()!=null){
            try {
                StringBuffer strData = new StringBuffer();
                StringBuffer strCampos = new StringBuffer("(");
                StringBuffer strValues = new StringBuffer("(");
                String strTabla = this.getLogDBProy();

                strCampos.append("fecha_bitacora,");
                strValues.append("getdate(),");

                if (this.getBitacora().getBitacora()!=null){
                    strCampos.append("bitacora,");
                    strValues.append("'");
                    strValues.append(replaceChar(this.getBitacora().getBitacora()));
                    strValues.append("',");
                }
                if (this.getBitacora().getConsulta()!=null){
                    strCampos.append("consulta,");
                    strValues.append("'");
                    strValues.append(replaceChar(this.getBitacora().getConsulta()));
                    strValues.append("',");
                }
                if (this.getBitacora().getClaveProyecto()!=null){
                    strCampos.append("clave_aplicacion,");
                    strValues.append(this.getBitacora().getClaveProyecto());
                    strValues.append(",");
                }
                if (this.getBitacora().getClaveEmpleado()!=null){
                    strCampos.append("clave_empleado,");
                    strValues.append(this.getBitacora().getClaveEmpleado());
                    strValues.append(",");
                }
                if (this.getBitacora().getClaveTipoEvento()!=null){
                    strCampos.append("clave_tipo_evento");
                    strValues.append(this.getBitacora().getClaveTipoEvento());
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
                Integer intSld = this.executeInsert();

                if (intSld>0){
                    sld = true;
                    this.setIntSld(intSld);
                }
            } finally {
                try{
                    LogHandler log = new LogHandler();
                    log.setBoolSel(false);
                    StringBuffer textData=new StringBuffer();
                    log.setStrQuery(this.getStrQuery());
                    log.logData(getKey(this.getProp(), this.LOGBITACORA),
                                new StringBuffer("addBitacora"),textData,"BITACOR");
                }catch(Exception ex){
                    throw new ExceptionHandler(ex,this.getClass(),"Problemas al excribir en la Bitacora");
                }
            }
        }
        return sld;
    }

    /**
     * Metodo para realizar un UPDATE en la Bitacora
     * @return
     */
    private boolean updateBitacora() throws ExceptionHandler{
        boolean sld = true;
        try {
            StringBuffer strData = new StringBuffer("");
            StringBuffer strCampos = new StringBuffer("");
            String strTabla = this.getLogDBProy();

            if (this.getBitacora().getFechaBitacora()!=null){
                strCampos.append(" fecha_bitacora = '");
                strCampos.append(this.getBitacora().getFechaBitacora());
                strCampos.append("',");
            }
            if (this.getBitacora().getBitacora()!=null){
                strCampos.append(" bitacora = '");
                strCampos.append(replaceChar(this.getBitacora().getBitacora()));
                strCampos.append("',");
            }
            if (this.getBitacora().getConsulta()!=null){
                strCampos.append(" consulta = '");
                strCampos.append(replaceChar(this.getBitacora().getConsulta()));
                strCampos.append("',");
            }
            if (this.getBitacora().getClaveProyecto()!=null){
                strCampos.append(" clave_aplicacion = ");
                strCampos.append(this.getBitacora().getClaveProyecto());
                strCampos.append(",");
            }
            if (this.getBitacora().getClaveEmpleado()!=null){
                strCampos.append(" clave_empleado = ");
                strCampos.append(this.getBitacora().getClaveEmpleado());
                strCampos.append(",");
            }
            if (this.getBitacora().getClaveTipoEvento()!=null){
                strCampos.append(" clave_tipo_evento = ");
                strCampos.append(this.getBitacora().getClaveTipoEvento());
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
            Integer intSld = this.executeUpdate();

            if (intSld>0){
                sld = true;
            }
        } finally {
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                StringBuffer textData=new StringBuffer();
                log.setStrQuery(this.getStrQuery());
                log.logData(getKey(this.getProp(), this.LOGBITACORA),
                            new StringBuffer("updateBitacora"),textData,"BITACOR");
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),"Problemas al actualizar en la Bitacora");
            }
        }
        return sld;
    }

    /**
     * Metodo para realizar un DELETE en la bitacora
     * @return
     */
    private boolean deleteBitacora()throws ExceptionHandler{
        boolean sld = true;
        try {
            StringBuffer strData = new StringBuffer();
            String strTabla = this.getLogDBProy();

            strData.append("delete from ");
            strData.append(strTabla);
            strData.append(" where clave_bitacora_proyecto = ");
            strData.append(this.getBitacora().getClaveBitacoraProyecto());

            this.setStrQuery(strData.toString());
            Integer intSld = this.executeInsert();

            if (intSld>0){
                sld = true;
            }
        } finally {
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                StringBuffer textData=new StringBuffer();
                log.setStrQuery(this.getStrQuery());
                log.logData(getKey(this.getProp(), this.LOGBITACORA),
                            new StringBuffer("deleteBitacora"),textData,"BITACOR");
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),"Problemas al borrar en la Bitacora");
            }
        }
        return sld;
    }

    /**
     * Metodo para manejar caracteres especiales que se requieren reemplazar
     * antes de enviarlos como data en una query, ej ('), por ('')
     * @param strData
     * @return
     */
    private String replaceChar(String strData){
        String strSld = "";
        strSld = strData;
        strSld = strSld.replaceAll("'", "''");
        return strSld;
    }

    public void addVariablesBitacora(Integer strClaveBitacoraProy)throws ExceptionHandler{
        boolean sld = false;
        if ( this.getBitacora()!=null){
            try {
                if (this.getLstVariables()!=null){
                    Iterator it = this.getLstVariables().iterator();
                    while (it.hasNext()){
                        String[] strVar = (String[]) it.next();
                        StringBuffer strQuery = new StringBuffer();
                        strQuery.append("insert into Bitacora_variable (");
                        strQuery.append("clave_bitacora_proyecto,");
                        strQuery.append("nombre_variable,");
                        strQuery.append("alias_variable,");
                        strQuery.append("valor_variable,");
                        strQuery.append("tipo_variable) values (");
                        strQuery.append(strClaveBitacoraProy).append(",'");
                        strQuery.append(strVar[0]).append("','");
                        strQuery.append(strVar[1]).append("','");
                        strQuery.append(strVar[2]).append("','");
                        strQuery.append(strVar[3]).append("')");
                        this.setStrQuery(strQuery.toString());
                        executeInsert();
                    }
                }
            } finally {
                try{
                    LogHandler log = new LogHandler();
                    log.setBoolSel(false);
                    StringBuffer textData=new StringBuffer();
                    log.setStrQuery(this.getStrQuery());
                    log.logData(getKey(this.getProp(), this.LOGBITACORA),
                                new StringBuffer("addVariablesBitacora"),textData,"BITACOR");
                }catch(Exception ex){
                    throw new ExceptionHandler(ex,this.getClass(),"Problemas al excribir las variables de la Bitacora");
                }
            }
        }
        //return sld;
    }
}
