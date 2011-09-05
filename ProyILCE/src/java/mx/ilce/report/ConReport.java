package mx.ilce.report;

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
import mx.ilce.bean.HashCampo;
import mx.ilce.bitacora.AdmBitacora;
import mx.ilce.bitacora.Bitacora;
import mx.ilce.component.AdminFile;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.handler.LogHandler;
import mx.ilce.util.UtilDate;

/**
 * Clase implementada para realizar las conexiones a la base de datos
 * para la obtencion de datos para los reportes
 * @author ccatrilef
 */
class ConReport {

    private String query;
    private Connection conn;
    private Bitacora bitacora;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Bitacora getBitacora() {
        return bitacora;
    }

    public void setBitacora(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

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
            this.conn = DriverManager.getConnection(strConexion.toString(),user,psw);
            if (this.conn.isClosed()){
                System.out.println("NO HAY CONEXION");
            }
        }catch (SQLException sqlex){
            throw new ExceptionHandler(sqlex,this.getClass(),"Problemas para abrir Conexion a Base de datos");
        } catch (ClassNotFoundException ex) {
            throw new ExceptionHandler(ex,this.getClass(),"No se encontro los Driver de Conexion");
        }catch (Exception e){
            throw new ExceptionHandler(e,this.getClass(),"Problemas para abrir Conexion a Base de datos");
        }
    }

    public List getConfigReport(String query)
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        String pk = "";
        try{
            getConexion();
                ps =this.conn.createStatement();
                rs = ps.executeQuery(query);
                while (rs.next()){
                    Config conf = new Config();
                    conf.setIdConfig(Integer.valueOf(rs.getString(1)));
                    conf.setIdConfigType(Integer.valueOf(rs.getString(2)));
                    conf.setIdSequence(Integer.valueOf((rs.getString(3)==null)?"0":rs.getString(3)));
                    conf.setName(rs.getString(4));
                    lstData.add(conf);
                }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),"Problemas para obtencion de datos con WHERE y DATA enviada");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),"Problemas para obtencion de datos con WHERE y DATA enviada");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                StringBuffer textData=new StringBuffer();
                textData.append(("QUERY: "+query)).append("\n");
                log.setStrQuery(query);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getDataWithWhereAndData"),textData);
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),"Problemas al excribir el LOG");
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
                throw new ExceptionHandler(es,this.getClass(),"Problemas para cerrar Conexion a Base de datos");
            }
        }
        return lstData;
    }


    public List getListConfig(String query)
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        String pk = "";
        try{
            getConexion();
            ps =this.conn.createStatement();

            rs = ps.executeQuery(query);
            while (rs.next()){
                Config conf = new Config();
                conf.setIdConfig(Integer.valueOf(rs.getString(1)));
                conf.setName(rs.getString(2));
                conf.setConfig(rs.getString(3));
                lstData.add(conf);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),"Problemas para obtencion de datos con WHERE y DATA enviada");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),"Problemas para obtencion de datos con WHERE y DATA enviada");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                StringBuffer textData=new StringBuffer();
                textData.append(("QUERY: "+query)).append("\n");
                log.setStrQuery(query);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getDataWithWhereAndData"),textData);
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),"Problemas al excribir el LOG");
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
                throw new ExceptionHandler(es,this.getClass(),"Problemas para cerrar Conexion a Base de datos");
            }
        }
        return lstData;
    }

    public List getListSection(String query)
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        String pk = "";
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(query);
            while (rs.next()){
                Section sec = new Section();
                sec.setIdSequence(Integer.valueOf(rs.getString(1)));
                sec.setIdOrder(Integer.valueOf(rs.getString(2)));
                sec.setIdQuery(Integer.valueOf((rs.getString(3)==null)?"0":rs.getString(3)));
                sec.setSequenceType(rs.getString(4));
                sec.setTextValue(rs.getString(5));
                lstData.add(sec);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),"Problemas para obtencion de datos con WHERE y DATA enviada");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),"Problemas para obtencion de datos con WHERE y DATA enviada");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                StringBuffer textData=new StringBuffer();
                textData.append(("QUERY: "+query)).append("\n");
                log.setStrQuery(query);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getDataWithWhereAndData"),textData);
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),"Problemas al excribir el LOG");
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
                throw new ExceptionHandler(es,this.getClass(),"Problemas para cerrar Conexion a Base de datos");
            }
        }
        return lstData;
    }

    public List getListHeader(String query)
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        String pk = "";
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(query);
            while (rs.next()){
                Section sec = new Section();
                sec.setIdSequence(Integer.valueOf(rs.getString(1)));
                sec.setTextValue(rs.getString(2));
                lstData.add(sec);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),"Problemas para obtencion de datos con WHERE y DATA enviada");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),"Problemas para obtencion de datos con WHERE y DATA enviada");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                StringBuffer textData=new StringBuffer();
                textData.append(("QUERY: "+query)).append("\n");
                log.setStrQuery(query);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getDataWithWhereAndData"),textData);
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),"Problemas al excribir el LOG");
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
                throw new ExceptionHandler(es,this.getClass(),"Problemas para cerrar Conexion a Base de datos");
            }
        }
        return lstData;
    }

    public List getListConfigSection(String query)
            throws ExceptionHandler{
        List lstData = new ArrayList();
        Statement ps = null;
        ResultSet rs = null;
        String pk = "";
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(query);
            while (rs.next()){
                Config conf = new Config();
                conf.setNumAdic(rs.getInt(1));
                conf.setNumAdicFirst(rs.getInt(2));
                conf.setNumAdicLast(rs.getInt(3));
                conf.setNumConf(rs.getInt(4));
                conf.setName(rs.getString(5));
                conf.setIdConfig(Integer.valueOf(rs.getString(6)));
                conf.setIdSequenceConfig(Integer.valueOf(rs.getString(7)));
                lstData.add(conf);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),"Problemas para obtencion de datos con WHERE y DATA enviada");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),"Problemas para obtencion de datos con WHERE y DATA enviada");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                StringBuffer textData=new StringBuffer();
                textData.append(("QUERY: "+query)).append("\n");
                log.setStrQuery(query);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getDataWithWhereAndData"),textData);
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),"Problemas al excribir el LOG");
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
                throw new ExceptionHandler(es,this.getClass(),"Problemas para cerrar Conexion a Base de datos");
            }
        }
        return lstData;
    }

    public String getQueryByID(String query)
            throws ExceptionHandler{
        String strSld = "";
        Statement ps = null;
        ResultSet rs = null;
        String pk = "";
        try{
            getConexion();
            ps =this.conn.createStatement();
            rs = ps.executeQuery(query);
            if (rs.next()){
                strSld = rs.getString(2);
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),"Problemas para obtencion de datos con WHERE y DATA enviada");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),"Problemas para obtencion de datos con WHERE y DATA enviada");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }finally{
            try{
                LogHandler log = new LogHandler();
                StringBuffer textData=new StringBuffer();
                textData.append(("QUERY: "+query)).append("\n");
                log.setStrQuery(query);
                log.logData(AdminFile.getKey(AdminFile.leerConfig(), AdminFile.LOGFILESERVER),
                            new StringBuffer("getDataWithWhereAndData"),textData);
            }catch(Exception ex){
                throw new ExceptionHandler(ex,this.getClass(),"Problemas al excribir el LOG");
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
                throw new ExceptionHandler(es,this.getClass(),"Problemas para cerrar Conexion a Base de datos");
            }
        }
        return strSld;
    }


    /**
     * Ejecuta una query, agregando el where respectivo, usando la data entregada
     * y las variables adicionales que se le sean entregadas
     * @param query
     * @param whereData
     * @param arrData
     * @param arrVariables
     * @return
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
                Bitacora bitacoraI = this.getBitacora();
                if ((bitacoraI!=null)&&(bitacoraI.isEnable()))
                {
                    AdmBitacora admBit = new AdmBitacora();
                    Integer evento = Integer.valueOf(AdmBitacora.getKey(admBit.getProp(),AdmBitacora.CONSULTAR));
                    bitacoraI.setConsulta(query);
                    bitacoraI.setClaveTipoEvento(evento);
                    bitacoraI.setEvento(AdmBitacora.CONSULTAR);

                    admBit.setBitacora(bitacoraI);
                    admBit.addBitacora();
                    this.getBitacora().cleanDataQuery();
                }
            }
        }catch(SQLException e){
            ExceptionHandler eh = new ExceptionHandler(e,this.getClass(),"Problemas para obtencion de datos con WHERE y DATA enviada");
            eh.setStrQuery(query);
            eh.setSeeStringData(true);
            throw eh;
        }catch(Exception ex){
            ExceptionHandler eh = new ExceptionHandler(ex,this.getClass(),"Problemas para obtencion de datos con WHERE y DATA enviada");
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
                throw new ExceptionHandler(ex,this.getClass(),"Problemas al excribir el LOG");
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
                throw new ExceptionHandler(es,this.getClass(),"Problemas para cerrar Conexion a Base de datos");
            }
        }
        return hsCmp;
    }

    /**
     * Metodo que toma una instruccion "WHERE" que se va adjuntar a una query y
     * la analiza para evaluar si requiere cambiar el WHERE por AND, agregar un
     * WHERE o un AND o simplemente juntar las instrucciones, con el fin de tener
     * una query correcta
     * @param query     Query Principal
     * @param strWhere  Instruccion a agregar a la query
     * @return
     */
    private String addWhereToQuery(String query, String strWhere){
        String strQuery = query;
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
                        strQuery = strQuery + " " + strWhere.toUpperCase().replaceFirst("WHERE ", "AND ");
                    //EMPIEZA CON AND
                    }else if (strWhere.toUpperCase().trim().substring(0,4).equals("AND ")){
                            strQuery = strQuery + " " + strWhere;
                    }else{
                            strQuery = strQuery + " AND " + strWhere;
                    }
                //evaluaremos solo el AND
                }else if (strWhere.trim().length()>4){
                    //EMPIEZA CON AND
                    if (strWhere.toUpperCase().trim().substring(0,4).equals("AND ")){
                        strQuery = strQuery + " " + strWhere;
                    }else{
                        strQuery = strQuery + " AND " + strWhere;
                    }
                }else{
                        strQuery = strQuery + " AND " + strWhere;
                }
            }else{
                //evaluaremos el WHERE Y el AND
                if (strWhere.trim().length()>6){
                    //COMIENZA CON WHERE
                    if (strWhere.toUpperCase().trim().substring(0,6).equals("WHERE ")){
                        strQuery = strQuery + " " + strWhere;
                    //COMIENZA CON AND
                    }else if (strWhere.toUpperCase().trim().substring(0,4).equals("AND ")){
                        strQuery = strQuery + " " + strWhere.toUpperCase().replaceFirst("AND "," WHERE ");
                    }else{
                        strQuery = strQuery + " WHERE " + strWhere;
                    }
                //evaluaremos solo el AND
                }else if (strWhere.trim().length()>4){
                    //COMIENZA CON AND
                    if (strWhere.toUpperCase().trim().substring(0,4).equals("AND ")){
                        strQuery = strQuery + " " + strWhere.toUpperCase().replaceFirst("AND "," WHERE ");
                    }else{
                        strQuery = strQuery + " WHERE " + strWhere;
                    }
                }else{
                    strQuery = strQuery + " " + strWhere;
                }
            }
        }else{
             strQuery = query;
        }
        return strQuery;
    }

    /**
     * Segun el tipo entregado que se tiene en la base de datos
     * se entrega un string con el tipo que le debe corresponder en Java
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
     * Segun el tipo de dato que se entrega, el cual viene desde la base de
     * datos, utiliza la conversion respectiva para dejar el valor como String
     * @param strType   Tipo de dato proveniente de la Base de Datos
     * @param rs        ResulSet donde esta el objeto a analizar
     * @param codigo    Codigo (posicion) dentro del resulset donde esta el dato
     * a analizar
     * @return
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
                }else if(strType.toUpperCase().equals("DATETIME") ){
                    Date date = rs.getDate(codigo.intValue());
                    if (date!=null){
                        UtilDate utDate = new UtilDate(date.getDate(), date.getMonth()+1, date.getYear()+1900);
                        sld = utDate.getFecha("/");
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
            throw new ExceptionHandler(es,this.getClass(),"Problemas para conversion de tipo de datos desde la Base");
        }
        return sld;
    }

    /**
     * Convierte un arreglo de String a un String
     * @param strData   Arreglo que se llevara a String
     * @return
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
