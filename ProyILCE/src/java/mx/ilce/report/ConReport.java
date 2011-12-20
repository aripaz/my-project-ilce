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

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import mx.ilce.bean.Campo;
import mx.ilce.bean.HashCampo;
import mx.ilce.component.AdminFile;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.LogHandler;
import mx.ilce.util.UtilDate;

/**
 * Clase implementada para realizar la conexión a la base de datos
 * para la obtención de datos de los reportes
 * @author ccatrilef
 */
class ConReport {

    private String query;
    private Connection conn;
    private String hourMinSec;

    public String getHourMinSec() {
        return hourMinSec;
    }

    public void setHourMinSec(String hourMinSec) {
        this.hourMinSec = hourMinSec;
    }

    /**
     * Obtiene el texto de la query
     * @return  String  Texto de la query
     */
    public String getQuery() {
        return query;
    }

    /**
     * Asigna el texto de la query
     * @param query Texto de la query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Método que realiza la conexión a la base de datos
     * @throws SQLException
     * @throws ExceptionHandler
     */
    private void getConexion() throws SQLException, ExceptionHandler{
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
            if (conn!=null){
                if (conn.isClosed()){
                    this.conn = DriverManager.getConnection(strConexion.toString(),user,psw);
                    if (this.conn.isClosed()){
                        System.out.println("NO HAY CONEXION");
                    }
                }
            }else{
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
     * Método que realiza la ejecución de las queries de inserción de datos.
     * Entrega el ID del registro que se ingreso
     * @param strQuery  Query de insert a ejecutar
     * @return Integer  ID del registro ingresado
     * @throws ExceptionHandler
     */
    public Integer executeInsert(String strQuery) throws ExceptionHandler{
        Integer idData = Integer.valueOf(0);
        Statement st = null;
        ResultSet rs = null;
        try{
            getConexion();
            st = this.conn.createStatement();
            this.conn.setAutoCommit(true);
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
                                idData = Integer.valueOf(key);
                            }catch(NumberFormatException e){
                                idData = res;
                            }
                        }
                    } while(rs.next());
                }
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para efectuar el INSERT");
            eh.setStrQuery(strQuery);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para efectuar el INSERT");
            eh.setStrQuery(strQuery);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(strQuery);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(),AdminFile.LOGFILESERVER),
                            new StringBuffer("executeInsert"),new StringBuffer(""));
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),
                                           "Problemas al escribir en el Archivo de Log");
            }
            try{
            if (rs!=null){
                rs.close();
            }
            this.conn.close();
            }catch(SQLException es){
                throw new ExceptionHandler(es,this.getClass(),
                        "Problemas para cerrar conexión a la Base de Datos");
            }
        }
        return idData;
    }

    /**
     * Método que realiza la ejecución de las queries de Update de datos. Entrega
     * un ID que si es mayor que cero indica una operación exitosa
     * @param strQuery  Query de update a ejecutar
     * @return Integer  ID con el resultado de la operación (>0 exitosa)
     * @throws ExceptionHandler
     */
    public Integer executeUpdate(String strQuery) throws ExceptionHandler{

        Statement st = null;
        Integer increment =Integer.valueOf(-1);
        try{
            getConexion();
            st = this.conn.createStatement();
            increment = st.executeUpdate(strQuery);
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                                                "Problemas para efectuar UPDATE");
            eh.setStrQuery(strQuery);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                                                "Problemas para efectuar UPDATE");
            eh.setStrQuery(strQuery);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                StringBuffer textData=new StringBuffer();
                textData.append(("REGISTRO: "+increment.toString())).append("\n");
                log.setStrQuery(strQuery);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(),AdminFile.LOGFILESERVER),
                            new StringBuffer("executeUpdate"),textData);
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),
                                    "Problemas al escribir en el archivo de Log");
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
        return increment;
    }

    /**
     * Método que obtiene los datos de un Reporte
     * @return Report   Objeto Report
     * @throws ExceptionHandler
     */
    public Report getReport()throws ExceptionHandler{
        Report report = null;
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                report = new Report();
                report.setIdReport(Integer.valueOf(rs.getString(1)));
                report.setReport(rs.getString(2));
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención de datos del Reporte");
            eh.setStrQuery(this.getQuery());
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención de datos del Reporte");
            eh.setStrQuery(this.getQuery());
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getReport"),new StringBuffer(""));
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
        return report;
    }

    /**
     * Método que obtiene los datos de un listado de Reportes
     * @return List     Listado de objetos Report
     * @throws ExceptionHandler
     */
    public List getListReport()
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                Report rep = new Report();
                rep.setIdReport(Integer.valueOf(rs.getString(1)));
                rep.setReport(rs.getString(2));
                lstData.add(rep);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del listado de Reportes");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del listado de Reportes");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListReport"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return lstData;
    }

    /**
     * Método que obtiene los datos de una estructura
     * @return Structure   Objeto Structure con los datos
     * @throws ExceptionHandler
     */
    public Structure getStructure()
            throws ExceptionHandler{
        Structure struct = null;
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                struct = new Structure();
                struct.setIdStructure(Integer.valueOf(rs.getString(1)));
                struct.setIdTypeStructure(Integer.valueOf(rs.getString(2)));
                struct.setStructure(rs.getString(3));
                struct.setTypeStructure(rs.getString(4));
                struct.setMainFig(Integer.valueOf(rs.getString(5)));
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención de los datos de la estructura");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención de los datos de la estructura");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getStructure"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return struct;
    }

    /**
     * Método que obtiene un listado de estructuras. Vinculada a la configuración
     * del Reporte.
     * @return List     Listado con objetos Structure
     * @throws ExceptionHandler
     */
    public List getListStructure()
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                Structure struct = new Structure();
                struct.setIdStructure(Integer.valueOf(rs.getString(1)));
                struct.setStructure(rs.getString(2));
                struct.setTypeStructure(rs.getString(3));
                struct.setMainFig(rs.getInt(4));
                struct.setIdOrder(rs.getInt(5));
                lstData.add(struct);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del listado de estructuras");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del listado de estructuras");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListStructure"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return lstData;
    }

    /**
     * Método que obtiene un listado de tipos de estructuras 
     * @return List     Listado con objetos Structure
     * @throws ExceptionHandler
     */
    public List getListTypeStructure()
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                Structure struct = new Structure();
                struct.setIdTypeStructure(Integer.valueOf(rs.getString(1)));
                struct.setTypeStructure(rs.getString(2));
                lstData.add(struct);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención de los tipos de estructura");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención de los tipos de estructura");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListTypeStructure"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return lstData;
    }

    /**
     * Método que entrega un HashMap con los listados de elementos de cada
     * estructura. La llave corresponde al código de la estructura
     * @return HashMap  HashMap con listados de ElementStruct
     * @throws ExceptionHandler
     */
    public HashMap getListElementStruct() throws ExceptionHandler{
        HashMap hs = new HashMap();
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            String i = "0";
            while (rs.next()){
                ElementStruct conf = new ElementStruct();
                conf.setIdElementStruct(Integer.valueOf(rs.getString(1)));
                conf.setIdStructure(Integer.valueOf(rs.getString(2)));
                conf.setValueElement(rs.getString(3));
                conf.setOrden(Integer.valueOf(rs.getString(4)));
                conf.setTypeElement(rs.getString(5));
                if ((!i.equals("0"))&&(!i.equals(rs.getString(2)))) {
                    hs.put(Integer.valueOf(i), lstData);
                    i=rs.getString(2);
                    lstData = new ArrayList();
                }else{
                    i=rs.getString(2);
                }
                lstData.add(conf);
            }
            hs.put(Integer.valueOf(i), lstData);
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del listado de Elementos de estructura");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del listado de Elementos de estructura");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListElementStruct"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return hs;
    }

    /**
     * Método que entrega un listado con elementos de estructura
     * @return List Listado de objetos ElementStruct
     * @throws ExceptionHandler
     */
    public List getListElement()
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                ElementStruct elem = new ElementStruct();
                elem.setIdElementStruct(Integer.valueOf(rs.getString(1)));
                elem.setIdTypeElement(Integer.valueOf(rs.getString(2)));
                elem.setOrden(Integer.valueOf(rs.getString(3)));
                elem.setValueElement(rs.getString(4));
                elem.setTypeElement(rs.getString(5));

                lstData.add(elem);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del listado de elementos de estructura");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del listado de elementos de estructura");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListElement"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return lstData;
    }

    /**
     * Método que entrega un HashMap con los listados de configuraciones de los
     * elementos de  estructura recuperados. La llave corresponde el ID del
     * elemento de estructura
     * @return HashMap  HashMap con Listados que contienen objetos ElementStruct
     * @throws ExceptionHandler
     */
    public HashMap getListConfigElementStruct() throws ExceptionHandler{
        HashMap hs = new HashMap();
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            String i = "0";
            while (rs.next()){
                ElementStruct conf = new ElementStruct();
                conf.setIdConfigElement(Integer.valueOf(rs.getString(1)));
                conf.setIdElementStruct(Integer.valueOf(rs.getString(2)));
                conf.setTypeConfig(rs.getString(3));
                conf.setConfigValue(rs.getString(4));
                conf.setTypeElement(rs.getString(5));
                conf.setOrden(Integer.valueOf(rs.getString(6)));
                if ((!i.equals("0"))&&(!i.equals(rs.getString(2)))) {
                    hs.put(Integer.valueOf(i), lstData);
                    i=rs.getString(2);
                    lstData = new ArrayList();
                }else{
                    i=rs.getString(2);
                }
                lstData.add(conf);
            }
            hs.put(Integer.valueOf(i), lstData);
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del listado de configuración de elementos");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del listado de configuración de elementos");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListConfigElementStruct"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return hs;
    }

    /**
     * Método que entrega un listado con la configuración de los elementos de
     * estructura.
     * @return List     Listado con objetos Config
     * @throws ExceptionHandler
     */
    public List getListConfigElement()
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                Config conf = new Config();
                conf.setIdConfigElement(Integer.valueOf(rs.getString(1)));
                conf.setTypeConfig(rs.getString(2));
                conf.setTypeValue(rs.getString(3));
                conf.setConfigValue(rs.getString(4));

                lstData.add(conf);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del listado de configuración de elementos");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del listado de configuración de elementos");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListConfigElement"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return lstData;
    }

    /**
     * Método que entrega un HashMap con listados que contienen las queries
     * asociadas a las estructuras del reporte.
     * @return HashMap  HashMap con listados de objetos QueryStruct
     * @throws ExceptionHandler
     */
    public HashMap getListQueries() throws ExceptionHandler{
        HashMap hs = new HashMap();
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            String i = "0";
            while (rs.next()){
                QueryStruct qry = new QueryStruct();
                qry.setIdQueryReport(Integer.valueOf(rs.getString(1)));
                qry.setIdStructure(Integer.valueOf(rs.getString(2)));
                qry.setStrQuery(rs.getString(3));
                qry.setIsExtern(Integer.valueOf(rs.getString(4)));
                if ((!i.equals("0"))&&(!i.equals(rs.getString(2)))) {
                    hs.put(Integer.valueOf(i), lstData);
                    i=rs.getString(2);
                    lstData = new ArrayList();
                }else{
                    i=rs.getString(2);
                }
                lstData.add(qry);
            }
            hs.put(Integer.valueOf(i), lstData);
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención de las queries del reporte");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención de las queries del reporte");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListQueries"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a Base de datos");
            }
        }
        return hs;
    }

    /**
     * Método que entrega los datos de un elemento de estructura.
     * @return ElementStruct    Objeto ElementStruct
     * @throws ExceptionHandler
     */
    public ElementStruct getElementStruct()
            throws ExceptionHandler{
        ElementStruct elem = null;
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                elem = new ElementStruct();
                elem.setIdElementStruct(Integer.valueOf(rs.getString(1)));
                elem.setIdTypeElement(Integer.valueOf(rs.getString(2)));
                elem.setOrden(Integer.valueOf(rs.getString(3)));
                elem.setValueElement(rs.getString(4));
                elem.setTypeElement(rs.getString(5));
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención de los datos del elemento de estructura");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención de los datos del elemento de estructura");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getElementStruct"),new StringBuffer(""));
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
                        "Problemas para cerrar la conexión a la Base de datos");
            }
        }
        return elem;
    }

    /**
     * Método que entrega un valor entero, utilizado para obtener la nueva
     * posición que le corresponde a un elemento o estructura, según el último
     * elemento ingresado.
     * @return Integer  Valor obtenido
     * @throws ExceptionHandler
     */
    public Integer getElementByQuery()
            throws ExceptionHandler{
        Integer sld = -1;
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps = this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            if (rs.next()){
                sld = rs.getInt(1);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención de la posición del elemento o estructura");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención de la posición del elemento o estructura");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getElementByQuery"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return sld;
    }

    /**
     * Método que obtiene un listado con los tipos de elementos.
     * @return List     Listado con objetos ElementStruct
     * @throws ExceptionHandler
     */
    public List getListTypeElement()
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                ElementStruct elem = new ElementStruct();
                elem.setIdTypeElement(Integer.valueOf(rs.getString(1)));
                elem.setTypeElement(rs.getString(2));

                lstData.add(elem);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del listado de tipos de elementos");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del listado de tipos de elementos");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListTypeElement"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return lstData;
    }

    /**
     * Método que entrega un listado con los tipos de configuración
     * @return List     Listado con objetos Config
     * @throws ExceptionHandler
     */
    public List getListTypeConfig()
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                Config conf = new Config();
                conf.setIdTypeConfig(Integer.valueOf(rs.getString(1)));
                conf.setTypeConfig(rs.getString(2));
                lstData.add(conf);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del listado con los tipos de configuración");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del listado con los tipos de configuración");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListTypeConfig"),new StringBuffer(""));
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
                        "Problemas para cerrar conexion a la Base de datos");
            }
        }
        return lstData;
    }

    /**
     * Método que entrega un listado con los tipos de configuración de elementos
     * @return List     Listado con objetos ElementStruct
     * @throws ExceptionHandler
     */
    public List getListTypeConfigElem()
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                ElementStruct elem = new ElementStruct();
                elem.setIdTypeConfig(Integer.valueOf(rs.getString(1)));
                elem.setTypeConfig(rs.getString(2));

                lstData.add(elem);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del listado de tipo de configuración");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del listado de tipo de configuración");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListTypeConfigElem"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return lstData;
    }

    /**
     * Método que entrega un listado con los tipos de valores de configuración
     * @return List     Listado con objetos Config
     * @throws ExceptionHandler
     */
    public List getListTypeValue()
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                Config conf = new Config();
                conf.setIdTypeValue(Integer.valueOf(rs.getString(1)));
                conf.setTypeValue(rs.getString(2));
                lstData.add(conf);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del listado con los tipos de valores de configuración");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del listado con los tipos de valores de configuración");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListTypeValue"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return lstData;
    }

    /**
     * Método que entrega un listado con los tipos de valores de configuración
     * para elementos
     * @return List     Listado con objetos ElementStruct
     * @throws ExceptionHandler
     */
    public List getListTypeValueElem()
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                ElementStruct elem = new ElementStruct();
                elem.setIdTypeValue(Integer.valueOf(rs.getString(1)));
                elem.setTypeValue(rs.getString(2));
                lstData.add(elem);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del listado con los tipos de valores de configuración");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del listado con los tipos de valores de configuración");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListTypeValueElem"),new StringBuffer(""));
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
                        "Problemas para cerrar conexion a la Base de datos");
            }
        }
        return lstData;
    }

    /**
     * Método que obtiene el listado con los valores de configuracion
     * @return List     Listado con objetos Config
     * @throws ExceptionHandler
     */
    public List getListConfigValue()
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                Config conf = new Config();
                conf.setIdConfigValue(Integer.valueOf(rs.getString(1)));
                conf.setConfigValue(rs.getString(2));
                lstData.add(conf);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del listado de valores de configuración");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del listado de valores de configuración");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListConfigValue"),new StringBuffer(""));
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
                        "Problemas para cerrar conexion a la Base de datos");
            }
        }
        return lstData;
    }

    /**
     * Método que obtiene el listado con los valores de configuración de los
     * elementos de estructura
     * @return List     Listado con objetos ElementStruct
     * @throws ExceptionHandler
     */
    public List getListConfigValueElem()
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                ElementStruct elem = new ElementStruct();
                elem.setIdConfigValue(Integer.valueOf(rs.getString(1)));
                elem.setConfigValue(rs.getString(2));
                lstData.add(elem);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del listado de valores de configuración");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del listado de valores de configuración");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListConfigValueElem"),new StringBuffer(""));
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
                        "Problemas para cerrar conexion a la Base de datos");
            }
        }
        return lstData;
    }

    /**
     * Método que entrega listado con las unidades de medidas manejadas por la aplicación
     * @return List     Listado con objetos Config
     * @throws ExceptionHandler
     */
    public List getListMeasure()
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                Config conf = new Config();
                conf.setIdMeasure(Integer.valueOf(rs.getString(1)));
                conf.setMeasure(rs.getString(2));
                lstData.add(conf);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del listado con las unidades de medida");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del listado con las unidades de medida");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListMeasure"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return lstData;
    }

    /**
     * Método que entrega listado con las unidades de medidas manejadas por la aplicación
     * @return List     Listado con objetos ElementStruct
     * @throws ExceptionHandler
     */
    public List getListMeasureElem()
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                ElementStruct elem = new ElementStruct();
                elem.setIdMeasure(Integer.valueOf(rs.getString(1)));
                elem.setMeasure(rs.getString(2));
                lstData.add(elem);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del listado con las unidades de medida");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del listado con las unidades de medida");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListMeasureElem"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return lstData;
    }

    /**
     * Método que entrega los datos de la query de una estructura
     * @return Config   Objeto Config
     * @throws ExceptionHandler
     */
    public Config getQueryConfig()
            throws ExceptionHandler{
        Config conf = null;
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            if (rs.next()){
                conf = new Config();
                conf.setIdQuery(Integer.valueOf(rs.getString(1)));
                conf.setIdStructure(Integer.valueOf(rs.getString(2)));
                conf.setQuery(rs.getString(3));
                conf.setIsExtern(Integer.valueOf(rs.getString(4)));
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención de los datos de la query");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención de los datos de la query");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getQueryConfig"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return conf;
    }

    /**
     * Método que entrega el listado con las configuraciones de estructura.
     * Usado en la generación del reporte
     * @return List     Listado con objetos Config
     * @throws ExceptionHandler
     */
    public HashMap getListConfigStruct() throws ExceptionHandler{
        HashMap hs = new HashMap();
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            String i = "0";
            while (rs.next()){
                Config conf = new Config();
                conf.setIdConfigStructure(Integer.valueOf(rs.getString(1)));
                conf.setIdStructure(Integer.valueOf(rs.getString(2)));
                conf.setTypeConfig(rs.getString(3));
                conf.setConfigValue(rs.getString(4));
                conf.setTypeStructure(rs.getString(5));
                conf.setIdOrder(Integer.valueOf(rs.getString(6)));
                if ((!i.equals("0"))&&(!i.equals(rs.getString(2)))) {
                    hs.put(Integer.valueOf(i), lstData);
                    i=rs.getString(2);
                    lstData = new ArrayList();
                }else{
                    i=rs.getString(2);
                }
                lstData.add(conf);
            }
            hs.put(Integer.valueOf(i), lstData);
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del listado de configuraciones de estructura");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del listado de configuraciones de estructura");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListConfigStruct"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return hs;
    }

    /**
     * Método que entrega el listado con las configuraciones de estructura.
     * Usado en la configuracion del reporte
     * @return List     Listado con objetos Config
     * @throws ExceptionHandler
     */
    public List getListConfigStructure()
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                Config conf = new Config();
                conf.setIdConfigStructure(Integer.valueOf(rs.getString(1)));
                conf.setTypeConfig(rs.getString(2));
                conf.setTypeValue(rs.getString(3));
                conf.setConfigValue(rs.getString(4));

                lstData.add(conf);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del listado de configuraciones de estructura");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del listado de configuraciones de estructura");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getListConfigStructure"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return lstData;
    }

    /**
     * Método que entrega la configuración de una estructura
     * @param query     Query entregada
     * @return Config   Configuracion de la estructura
     * @throws ExceptionHandler
     */
    public Config getConfigStructure(String query)
            throws ExceptionHandler{
        Config conf = null;
        Statement ps = null;
        ResultSet rs = null;
        String pk = "";
        try{
            getConexion();
                ps =this.conn.createStatement();
                rs = ps.executeQuery(query);
                if (rs.next()){
                    conf = new Config();
                    conf.setIdConfigStructure(Integer.valueOf(rs.getString(1)));
                    conf.setIdTypeConfig(Integer.valueOf(rs.getString(2)));
                    conf.setIdTypeValue(Integer.valueOf(rs.getString(3)));
                    if (rs.getString(4)!=null){
                        conf.setIdConfigValue(Integer.valueOf(rs.getString(4)));
                    }
                    conf.setConfigValue((rs.getString(5)==null)?"":rs.getString(5));
                    if (rs.getString(6)!=null){
                        conf.setIdMeasure(Integer.valueOf(rs.getString(6)));
                    }
                }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención de la configuración de la estructura");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención de la configuración de la estructura");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getConfigStructure"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return conf;
    }

    /**
     * Método que obtiene la configuración de un elemento
     * @param query     Query entregada
     * @return ElementStruct    Objeto ElementStruct
     * @throws ExceptionHandler
     */
    public ElementStruct getConfigElement(String query)
            throws ExceptionHandler{
        ElementStruct elem = null;
        Statement ps = null;
        ResultSet rs = null;
        String pk = "";
        try{
            getConexion();
                ps =this.conn.createStatement();
                rs = ps.executeQuery(query);
                while (rs.next()){
                    elem = new ElementStruct();
                    elem.setIdConfigElement(Integer.valueOf(rs.getString(1)));
                    elem.setIdTypeConfig(Integer.valueOf(rs.getString(2)));
                    elem.setIdTypeValue(Integer.valueOf(rs.getString(3)));
                    if (rs.getString(4)!=null){
                        elem.setIdConfigValue(Integer.valueOf(rs.getString(4)));
                    }
                    elem.setConfigValue((rs.getString(5)==null)?"":rs.getString(5));
                    if (rs.getString(6)!=null){
                        elem.setIdMeasure(Integer.valueOf(rs.getString(6)));
                    }
                }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención de la configuración de un elemento");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención de la configuración de un elemento");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setBoolSel(false);
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getConfigElement"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return elem;
    }

    /**
     * Método que ejecuta una query, agregando el where respectivo, usando la
     * data entregada y las variables adicionales que se le sean proporcionadas
     * @param query     Query entregada
     * @param whereData     condicion where
     * @param arrData       Arreglo con datos
     * @param arrVariables  Arreglo con variables adiconales
     * @return HashCampo    Objeto Hash con los datos recuperados
     * @throws ExceptionHandler
     */
    public HashCampo getDataWithWhereAndData(String query, String whereData,
                                                           String[] arrData,
                                                           String[][] arrVariables)
            throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        Statement ps = null;
        ResultSet rs = null;
        //String query = "";
        String pk = "";
        try{
            getConexion();
            if ((!"".equals(query)) && (whereData != null)){
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

                if (arrData!=null){
                    for(int i=1;i<=arrData.length;i++){
                        String strData = arrData[i-1];
                        if (strData != null){
                            query = query.replaceAll("%"+i, strData);
                        }
                    }
                }
                query = addWhereToQuery(query,whereData);
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
                                              getValueCampo(itCmp.getTypeDataDB(), rs, itCmp.getCodigo()));
                        cmp.setIsIncrement(itCmp.getIsIncrement());
                        lstData.add(cmp);
                    }
                    hsCmp.addListData(lstData,i++);
                }
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención de datos con WHERE y DATA enviada");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención de datos con WHERE y DATA enviada");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                StringBuffer textData=new StringBuffer();
                textData.append(("QUERY: "+query)).append("\n");
                textData.append(("ENTRADA: "+arrayToString(arrData))).append("\n");
                textData.append(("WHERE: "+whereData.toString()));
                log.setStrQuery(query);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getDataWithWhereAndData"),textData);
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
                        "Problemas para cerrar conexion a la Base de datos");
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
                if (strWhere.equals("1=2")){
                    strConcat = " WHERE ";
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
     * Método que según el tipo entregado que se tiene en la base de datos
     * entrega un string con el tipo que le debe corresponder en Java
     * @param strType   Texto con el tipo de dato a analizar
     * @return
     */
    private String castTypeDataDBtoAPL(String strType){
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
     * Método que según el tipo de dato que se entrega, el cual viene desde la base de
     * datos, utiliza la conversión respectiva para dejar el valor como String
     * @param strType   Tipo de dato proveniente de la Base de Datos
     * @param rs        ResulSet donde esta el objeto a analizar
     * @param codigo    Codigo (posicion) dentro del resulset donde esta el dato
     * a analizar
     * @return String   Valor del campo
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
            throw new ExceptionHandler(es,this.getClass(),
                    "Problemas para conversión de tipo de datos desde la Base");
        }
        return sld;
    }

    /**
     * Método que obtiene el listado de elementos factibles de usarse para formar
     * un reporte
     * @return  List    Listado con objetos ElementStruct
     * @throws ExceptionHandler
     */
    public List getElementToSelect()
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            while (rs.next()){
                ElementStruct elem = new ElementStruct();
                elem.setIdStructure(Integer.valueOf(rs.getString(1)));
                elem.setStructure(rs.getString(2));
                elem.setIdTypeStructure(Integer.valueOf(rs.getString(3)));
                elem.setTypeStructure(rs.getString(4));
                lstData.add(elem);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del listado de elementos");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del listado de elementos");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getElementToSelect"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return lstData;
    }

    /**
     * Método para obtener el número máximo de orden existente en la configuración
     * de un reporte
     * @return  Integer Valor obtenido
     * @throws ExceptionHandler
     */
    public Integer getMaxOrderReport()
            throws ExceptionHandler{
        Integer sld = null;
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(this.getQuery());
            if (rs.next()){
                sld = Integer.valueOf(rs.getString(1));
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),
                    "Problemas para la obtención del número máximo de orden");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),
                    "Problemas para la obtención del número máximo de orden");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                log.setStrQuery(this.getQuery());
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getMaxOrderReport"),new StringBuffer(""));
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
                        "Problemas para cerrar conexión a la Base de datos");
            }
        }
        return sld;
    }


    /**
     * Método que convierte un arreglo de String a un String
     * @param strData   Arreglo que se llevara a String
     * @return String   String con el resultado
     */
    private String arrayToString(String[] strData){
        StringBuilder sld= new StringBuilder();
        if (strData!=null){
            for (int i=0;i<strData.length;i++){
                sld.append("nro ").append(i+1).append(": ").append(strData[i]).append("\n");
            }
        }
        return sld.toString();
    }
}
