/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.conection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.ilce.bean.Campo;
import mx.ilce.bean.CampoForma;
import mx.ilce.bean.HashCampo;
import mx.ilce.component.AdminFile;

/**
 *  Clase para la implementacion de los metodos que se conectan a la Base de
 * Datos y manejan la construccion de las estructuras para contener los datos
 * de las queries que se desean ejecutar
 * @author ccatrilef
 */
class ConQuery {

    public ConQuery() {
    }

    private Connection conn;
    /**
     * Realiza la conexion a la base de datos. Los parametros de conexion se
     * obtienen de un properties para una facil mantencion sin compilar.
     * @throws SQLException
     */
    private void getConexion() throws SQLException{
        StringBuffer strConexion = new StringBuffer("");
        try {
            AdminFile admFile = new AdminFile();
            Properties prop = AdminFile.leerConfig();

            String server = admFile.getKey(prop,"SERVER"); 
            String base = admFile.getKey(prop,"BASE"); 
            String port = admFile.getKey(prop,"PORT");
            String user = admFile.getKey(prop,"USR");
            String psw = admFile.getKey(prop,"PSW");

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
            Logger.getLogger(ConSession.class.getName()).log(Level.SEVERE, null, sqlex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConSession.class.getName()).log(Level.SEVERE, null, ex);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Metodo que ejecuta una query de insercion, tras efectuarla, se obtiene el
     * ID o clave de los datos insertados, el cual es retornado en el atributo
     * Object del HashCampo. En caso que no se ejecute la operacion el numero
     * retornado es -1, en Caso que sea una tabla sin ID o clave, el valor
     * retornado es 0.
     * @param campoForma    Variable con datos para validacion
     * @param arrData       Query a ejecutar
     * @return
     * @throws SQLException
     */
    public HashCampo executeInsert(CampoForma campoForma, String arrData) throws SQLException{
        HashCampo hsCmp = new HashCampo();
        Statement st = null;
        ResultSet rs = null;
        try{
            Integer increment =Integer.valueOf(-1);
            if (validateInsert(campoForma.getTabla(), arrData)){
                getConexion();
                st = this.conn.createStatement();
                System.out.println("QUERY INSERT:");
                System.out.println(arrData);
                int res = st.executeUpdate(arrData, Statement.RETURN_GENERATED_KEYS);
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
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if (rs!=null){
                rs.close();
            }
            if (st!=null){
                st.close();
            }
            this.conn.close();
        }
        return hsCmp;
    }

    /**
     * Metodo para validar que se esta ejecutando una instruccion insert, en la
     * tabla que corresponde, con la query entregada
     * @param tabla     Nombre de la tabla
     * @param query     Query a validar
     * @return
     */
    private boolean validateInsert(String tabla, String query){
        boolean queryOK = true;
        String str = "insert into " + tabla;
        if ((!query.toUpperCase().contains(str.toUpperCase())) &&
                (!query.toUpperCase().contains("VALUES"))){
            queryOK = false;
        }
        return queryOK;
    }

    /**
     * Metodo que efectua una query de actualizacion de datos, tras su ejecucion 
     * entrega un numero 1, que efectuo correctamente la operacion, cero en caso 
     * contrario. El valor es retornado en el atributo Object del HashCampo
     * @param campoForma    Variable con datos para validacion
     * @param arrData       Query a ejecutar
     * @return
     * @throws SQLException
     */
    public HashCampo executeUpdate(CampoForma campoForma, String arrData) throws SQLException{
        HashCampo hsCmp = new HashCampo();
        Statement st = null;
        try{
            Integer increment =Integer.valueOf(-1);
            if (validateUpdate(campoForma.getTabla(), arrData)){
                getConexion();
                System.out.println("QUERY UPDATE:");
                System.out.println(arrData);
                st = this.conn.createStatement();
                increment = st.executeUpdate(arrData);
            }
            hsCmp.setObjData(increment);
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if (st!=null){
                st.close();
            }
            this.conn.close();
        }
        return hsCmp;
    }

    /**
     * Metodo para validar que se esta ejecutando una instruccion update, en la
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
     * Metodo que efectua una query de eliminacion de datos, tras su ejecucion
     * entrega un numero 1, que efectuo correctamente la operacion, cero en caso
     * contrario. El valor es retornado en el atributo Object del HashCampo
     * @param campoForma
     * @param arrData
     * @return
     * @throws SQLException
     */
    public HashCampo executeDelete(CampoForma campoForma, String arrData) throws SQLException{
        HashCampo hsCmp = new HashCampo();
        Statement st = null;
        try{
            Integer increment =Integer.valueOf(-1);
            if (validateDelete(campoForma.getTabla(), arrData)){
                getConexion();
                System.out.println("QUERY DELETE:");
                System.out.println(arrData);
                st = this.conn.createStatement();
                increment = st.executeUpdate(arrData);
            }
            hsCmp.setObjData(increment);
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if (st!=null){
                st.close();
            }
            this.conn.close();
        }
        return hsCmp;
    }

    /**
     * Metodo para validar que se esta ejecutando una instruccion Delete, en la
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
     * Obtiene la data, aplicando a la query los parametros de entrada entregados.
     * El idQuery entregado permite seleccionar la query respectiva.
     * Cada uno de los parametros de entrada deben convertirse a un String y
     * agregarse en el arreglo respectivo.
     * Los parametros de entrada deben venir en el orden que se indica en la
     * Query.
     * @param idQuery   Codigo de la query a utilizar
     * @param arrData   Arreglo con los parametros de entrada
     * @return HashCampo.  Contiene el listado de registros obtenidos y los campos
     * que posee la query, con sus tipos de datos
     * @throws SQLException
     */
    public HashCampo getData(Integer idQuery, String[] arrData) throws SQLException{
        HashCampo hsCmp = new HashCampo();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            String query = getQueryById(idQuery);
            if ((!"".equals(query)) && (arrData != null)){
                ps =this.conn.createStatement();
                for(int i=1;i<=arrData.length;i++){
                    String strData = arrData[i-1];
                    if (strData != null){
                        query = query.replaceFirst("%"+i, strData);
                    }
                }
                rs = ps.executeQuery(query);
                ResultSetMetaData rstm = rs.getMetaData();

                for (int i=1;i<=rstm.getColumnCount();i++){
                    Campo cmp = new Campo(rstm.getColumnName(i),
                                          Integer.valueOf(i),
                                          rstm.getColumnTypeName(i));
                    cmp.setTypeDataAPL(castTypeDataDBtoAPL(rstm.getColumnTypeName(i)));
                    hsCmp.addCampo(cmp);
                }
                int i=0;
                while (rs.next()){
                    List lstData = new ArrayList();
                    List lstCampo = hsCmp.getListCampos();
                    Iterator it = lstCampo.iterator();
                    while (it.hasNext()){
                        Campo itCmp = (Campo) it.next();
                        Campo cmp = new Campo(itCmp.getNombre(),
                                              itCmp.getNombreDB(),
                                              itCmp.getCodigo(),
                                              itCmp.getTypeDataDB(),
                                              castTypeDataDBtoAPL(itCmp.getTypeDataDB()),
                                              getValueCampo(itCmp.getTypeDataDB(), rs, itCmp.getCodigo()));
                        lstData.add(cmp);
                    }
                    hsCmp.addListData(lstData,i++);
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            try{
            if (rs!=null){
                rs.close();
            }
            if (ps!=null){
                ps.close();
            }
            this.conn.close();
            }catch(SQLException es){
                
            }
        }
        return hsCmp;
    }

    /**
     * Obtiene la data, aplicando a la query un parametro de entrada, el cual
     * consiste en un string con un "WHERE" o un "AND" dependiendo de la query
     * la cual condicionara la respuesta de la query.
     * El idQuery entregado permite seleccionar la query respectiva.
     * @param idQuery   Codigo de la query a utilizar
     * @param arrData   Arreglo con los parametros de entrada
     * @return HashCampo.  Contiene el listado de registros obtenidos y los campos
     * que posee la query, con sus tipos de datos
     * @throws SQLException
     */
    public HashCampo getDataWithWhere(Integer idQuery, String whereData) throws SQLException{
        HashCampo hsCmp = new HashCampo();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            String query = getQueryById(idQuery);
            if ((!"".equals(query)) && (whereData != null)){
                ps =this.conn.createStatement();
                query = addWhereToQuery(query,whereData);
                rs = ps.executeQuery(query);
                ResultSetMetaData rstm = rs.getMetaData();

                for (int i=1;i<=rstm.getColumnCount();i++){
                    Campo cmp = new Campo(rstm.getColumnName(i),
                                          Integer.valueOf(i),
                                          rstm.getColumnTypeName(i));
                    cmp.setTypeDataAPL(castTypeDataDBtoAPL(rstm.getColumnTypeName(i)));
                    hsCmp.addCampo(cmp);
                }
                int i=0;
                while (rs.next()){
                    List lstData = new ArrayList();
                    List lstCampo = hsCmp.getListCampos();
                    Iterator it = lstCampo.iterator();
                    while (it.hasNext()){
                        Campo itCmp = (Campo) it.next();
                        Campo cmp = new Campo(itCmp.getNombre(),
                                              itCmp.getNombreDB(),
                                              itCmp.getCodigo(),
                                              itCmp.getTypeDataDB(),
                                              castTypeDataDBtoAPL(itCmp.getTypeDataDB()),
                                              getValueCampo(itCmp.getTypeDataDB(), rs, itCmp.getCodigo()));
                        lstData.add(cmp);
                    }
                    hsCmp.addListData(lstData,i++);
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            try{
            if (rs!=null){
                rs.close();
            }
            if (ps!=null){
                ps.close();
            }
            this.conn.close();
            }catch(SQLException es){

            }
        }
        return hsCmp;
    }

    /**
     * Obtiene la data, aplicando a la query un parametro de entrada, el cual
     * consiste en 1)Un string con un "WHERE" o un "AND" dependiendo de la query
     * la cual condicionara la respuesta de la query, 2) Un arreglo con la data
     * de entreda para la query
     * El idQuery entregado permite seleccionar la query respectiva.
     * @param idQuery
     * @param whereData
     * @param arrData
     * @return
     * @throws SQLException
     */
    public HashCampo getDataWithWhereAndData(Integer idQuery, String whereData, String[] arrData) throws SQLException{
        HashCampo hsCmp = new HashCampo();
        Statement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
                                                                                                                                                                                                                                                                                                                            String query = getQueryById(idQuery);
            if ((!"".equals(query)) && (whereData != null)){
                ps =this.conn.createStatement();
                if (arrData!=null){
                    for(int i=1;i<=arrData.length;i++){
                        String strData = arrData[i-1];
                        if (strData != null){
                            query = query.replaceFirst("%"+i, strData);
                        }
                    }
                }
                query = addWhereToQuery(query,whereData);
                rs = ps.executeQuery(query);
                ResultSetMetaData rstm = rs.getMetaData();

                for (int i=1;i<=rstm.getColumnCount();i++){
                    Campo cmp = new Campo(rstm.getColumnName(i),
                                          Integer.valueOf(i),
                                          rstm.getColumnTypeName(i));
                    cmp.setTypeDataAPL(castTypeDataDBtoAPL(rstm.getColumnTypeName(i)));
                    hsCmp.addCampo(cmp);
                }
                int i=0;
                while (rs.next()){
                    List lstData = new ArrayList();
                    List lstCampo = hsCmp.getListCampos();
                    Iterator it = lstCampo.iterator();
                    while (it.hasNext()){
                        Campo itCmp = (Campo) it.next();
                        Campo cmp = new Campo(itCmp.getNombre(),
                                              itCmp.getNombreDB(),
                                              itCmp.getCodigo(),
                                              itCmp.getTypeDataDB(),
                                              castTypeDataDBtoAPL(itCmp.getTypeDataDB()),
                                              getValueCampo(itCmp.getTypeDataDB(), rs, itCmp.getCodigo()));
                        lstData.add(cmp);
                    }
                    hsCmp.addListData(lstData,i++);
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            try{
            if (rs!=null){
                rs.close();
            }
            if (ps!=null){
                ps.close();
            }
            this.conn.close();
            }catch(SQLException es){

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
        if ((strWhere!=null) && (!"".equals(strWhere))){
            if (query.toUpperCase().contains("WHERE ")){
                //evaluaremos el WHERE y el AND
                if (strWhere.trim().length()>6){
                    //EMPIEZA CON WHERE
                    if (strWhere.toUpperCase().trim().substring(0,6).equals("WHERE ")){
                        strQuery = query + " " + strWhere.toUpperCase().replaceFirst("WHERE ", "AND ");
                    //EMPIEZA CON AND
                    }else if (strWhere.toUpperCase().trim().substring(0,4).equals("AND ")){
                            strQuery = query + " " + strWhere;
                    }else{
                            strQuery = query + " AND " + strWhere;
                    }
                //evaluaremos solo el AND
                }else if (strWhere.trim().length()>4){
                    //EMPIEZA CON AND
                    if (strWhere.toUpperCase().trim().substring(0,4).equals("AND ")){
                        strQuery = query + " " + strWhere;
                    }else{
                        strQuery = query + " AND " + strWhere;
                    }
                }else{
                        strQuery = query + " AND " + strWhere;
                }
            }else{
                //evaluaremos el WHERE Y el AND
                if (strWhere.trim().length()>6){
                    //COMIENZA CON WHERE
                    if (strWhere.toUpperCase().trim().substring(0,6).equals("WHERE ")){
                        strQuery = query + " " + strWhere;
                    //COMIENZA CON AND
                    }else if (strWhere.toUpperCase().trim().substring(0,4).equals("AND ")){
                        strQuery = query + " " + strWhere.toUpperCase().replaceFirst("AND "," WHERE ");
                    }else{
                        strQuery = query + " WHERE " + strWhere;
                    }
                //evaluaremos solo el AND
                }else if (strWhere.trim().length()>4){
                    //COMIENZA CON AND
                    if (strWhere.toUpperCase().trim().substring(0,4).equals("AND ")){
                        strQuery = query + " " + strWhere.toUpperCase().replaceFirst("AND "," WHERE ");
                    }else{
                        strQuery = query + " WHERE " + strWhere;
                    }
                }else{
                    strQuery = query + " " + strWhere;
                }
            }
        }else{
             strQuery = query;
        }
        return strQuery;
    }

     /**
     * Obtiene la data aplicando la query seleccionada mediante el idQuery.
     * Esta query no posee parametros de entrada.
     * @param idQuery       Codigo de la query a utilizar
     * @return HashCampo    Contiene el listado de registros obtenidos y los
     * campos que posee la query, con sus tipos de datos
     * @throws SQLException
     */
    public HashCampo getData(Integer idQuery) throws SQLException{
        HashCampo hsCmp = new HashCampo();
        Statement st = null;
        ResultSet rs = null;
        try{
            getConexion();
            String query = getQueryById(idQuery);
            if (!"".equals(query)){
                st = this.conn.createStatement();
                rs = st.executeQuery(query);
                ResultSetMetaData rstm = rs.getMetaData();

                for (int i=1;i<=rstm.getColumnCount();i++){
                    Campo cmp = new Campo(rstm.getColumnName(i),
                                          Integer.valueOf(i),
                                          rstm.getColumnTypeName(i));
                    cmp.setTypeDataAPL(castTypeDataDBtoAPL(rstm.getColumnTypeName(i)));
                    hsCmp.addCampo(cmp);
                }
                int i=0;
                while (rs.next()){
                    List lstData = new ArrayList();
                    List lstCampo = hsCmp.getListCampos();
                    Iterator it = lstCampo.iterator();
                    while (it.hasNext()){
                        Campo itCmp = (Campo) it.next();
                        Campo cmp = new Campo(itCmp.getNombre(),
                                              itCmp.getNombreDB(),
                                              itCmp.getCodigo(),
                                              itCmp.getTypeDataDB(),
                                              castTypeDataDBtoAPL(itCmp.getTypeDataDB()),
                                              getValueCampo(itCmp.getTypeDataDB(), rs, itCmp.getCodigo()));
                        lstData.add(cmp);
                    }
                    hsCmp.addListData(lstData,i++);
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if (rs!=null){
                rs.close();
            }
            if (st!=null){
                st.close();
            }
            this.conn.close();
        }
        return hsCmp;
    }

    /**
     * Obtiene el nombre del campo PK de una tabla
     * @param tabla     Nombre de la tabla que se desea analizar
     * @return
     */
    public String getCampoPK(String tabla) throws SQLException{
        String str ="";
        Statement st = null;
        ResultSet rs = null;
        try{
            getConexion();
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
            e.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if (rs!=null){
                rs.close();
            }
            if (st!=null){
                st.close();
            }
            this.conn.close();
        }
        return str;
    }

    /**
     * Metodo para validar si la query entregada no posee alguna instruccion
     * que permita la modificacion de la base de datos. Esto es una medida de
     * seguridad, para que las queries de modificaccion solo sean las autorizadas,
     * es decir, que provengan del catalogo de queries de la base de datos y no
     * de una operacion externa.
     * @param query     Query que debe ser validada
     * @return
     */
    private boolean allowedQuery(String query){
        boolean sld = true;
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
        return sld;
    }

    /**
     * Obtiene la data aplicando la Query y Data entregada. Solo se permiten
     * queries de seleccion de datos, si se envia alguna que posea instrucciones
     * de modificacion o similar, se bloquea la operacion sin ejecutarla.
     * @param query     Query que se debe ejecutar en la base de Datos
     * @param arrData   Parametros con que se debe completar la query
     * @return
     * @throws SQLException
     */
    public HashCampo getDataByQuery(String query, String[] arrData) throws SQLException{
        HashCampo hsCmp = new HashCampo();
        Statement st = null;
        ResultSet rs = null;
        try{
            getConexion();
            if (allowedQuery(query)){
                if ((!"".equals(query)) && (arrData != null)){
                    st = this.conn.createStatement();
                    for(int i=1;i<=arrData.length;i++){
                        String strData = arrData[i-1];
                        if (strData != null){
                            query = query.replaceFirst("%"+i, strData);
                        }
                    }
                    rs = st.executeQuery(query);
                    ResultSetMetaData rstm = rs.getMetaData();

                    for (int i=1;i<=rstm.getColumnCount();i++){
                        Campo cmp = new Campo(rstm.getColumnName(i),
                                              Integer.valueOf(i),
                                              rstm.getColumnTypeName(i));
                        cmp.setTypeDataAPL(castTypeDataDBtoAPL(rstm.getColumnTypeName(i)));
                        if (rstm.isAutoIncrement(i)){
                            cmp.setIsIncrement(true);
                        }
                        hsCmp.addCampo(cmp);
                    }
                    int i=0;
                    while (rs.next()){
                        List lstData = new ArrayList();
                        List lstCampo = hsCmp.getListCampos();
                        Iterator it = lstCampo.iterator();
                        while (it.hasNext()){
                            Campo itCmp = (Campo) it.next();
                            Campo cmp = new Campo(itCmp.getNombre(),
                                                  itCmp.getNombreDB(),
                                                  itCmp.getCodigo(),
                                                  itCmp.getTypeDataDB(),
                                                  castTypeDataDBtoAPL(itCmp.getTypeDataDB()),
                                                  getValueCampo(itCmp.getTypeDataDB(), rs, itCmp.getCodigo()));
                            lstData.add(cmp);
                        }
                        hsCmp.addListData(lstData,i++);
                    }
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if (rs!=null){
                rs.close();
            }
            if (st!=null){
                st.close();
            }
            this.conn.close();
        }
        return hsCmp;
    }

    /**
     * Este metodo se debio utilizar, pero el PreparedStatement no esta
     * guardando las variables que se le entregan y deberia reemplazar en
     * las queries buscando el parametro con el signo de ?
     * @param idQuery
     * @param arrData
     * @return
     * @throws SQLException
     */
    private HashCampo getDataParam(Integer idQuery, String[][] arrData) throws SQLException{
        HashCampo hsCmp = new HashCampo();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            getConexion();
            String query = getQueryById(idQuery);
            if (!"".equals(query)){
                ps = this.conn.prepareStatement(query);
                for(int i=1;i<=arrData.length;i++){
                    String strTipo = arrData[i-1][0];
                    String strData = arrData[i-1][1];
                    if (strTipo.equals("1")){
                        ps.setString(i,"'"+strData+"'");
                    }else if (strTipo.equals("2")){
                        ps.setInt(i,Integer.parseInt(strData));
                    }else if (strTipo.equals("3")){
                        ps.setBigDecimal(i,new BigDecimal(strData));
                    }
                }
                rs = ps.executeQuery();
                ResultSetMetaData rstm = rs.getMetaData();

                for (int i=1;i<=rstm.getColumnCount();i++){
                    Campo cmp = new Campo(rstm.getColumnName(i),
                                          Integer.valueOf(i),
                                          rstm.getColumnTypeName(i));
                    hsCmp.addCampo(cmp);
                }
                int i=0;
                while (rs.next()){
                    List lstData = new ArrayList();
                    List lstCampo = hsCmp.getListCampos();
                    Iterator it = lstCampo.iterator();
                    while (it.hasNext()){
                        Campo itCmp = (Campo) it.next();
                        Campo cmp = new Campo();
                        cmp.setNombre(itCmp.getNombre());
                        cmp.setCodigo(itCmp.getCodigo());
                        cmp.setTypeDataDB(itCmp.getTypeDataDB());
                        cmp.setTypeDataAPL(castTypeDataDBtoAPL(itCmp.getTypeDataDB()));
                        cmp.setValor(getValueCampo(itCmp.getTypeDataDB(),rs,itCmp.getCodigo()));
                        lstData.add(cmp);
                    }
                    hsCmp.addListData(lstData,i++);
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if (rs!=null){
                rs.close();
            }
            if (ps!=null){
                ps.close();
            }
            this.conn.close();
        }
        return hsCmp;
    }

    /**
     * Busca una query desde la base de datos, pasandole el ID que posee
     * en la tabla
     * @param idQuery
     * @return
     */
    private String getQueryById(Integer idQuery) throws SQLException{
        String strSld = "";
        Statement st = null;
        ResultSet rs = null;
        try{
            if (this.conn == null){
                getConexion();
            }
            StringBuffer query = new StringBuffer();
            query.append("select consulta from consulta_forma ");
            query.append("where clave_consulta = ").append(idQuery.toString());
            st = this.conn.createStatement();
            rs = st.executeQuery(query.toString());
            if (rs.next()){
                strSld =  rs.getString(1);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (rs!=null){
                rs.close();
            }
            if (st!=null){
                st.close();
            }
            //Conn no se debe cerrar ya que es llamada privada
        }
        return strSld;
    }

    /**
     * Segun el tipo de dato que se entrega, el cual viene desde la base de
     * datos, utiliza la conversion respectiva para dejar el valor como String
     * @param strType
     * @param rs
     * @param codigo
     * @return
     * @throws SQLException
     */
    private String getValueCampo(String strType, ResultSet rs, Integer codigo) throws SQLException{
        String sld = new String();
        if (strType.toUpperCase().equals("CHAR")){
            sld = rs.getString(codigo.intValue());
        }else if (strType.toUpperCase().equals("VARCHAR")){
            sld = rs.getString(codigo.intValue());
        }else if(strType.toUpperCase().equals("INT") ){
            sld = String.valueOf(rs.getBigDecimal(codigo.intValue()));
        }else if(strType.toUpperCase().equals("DATETIME") ){
            sld = String.valueOf(rs.getDate(codigo.intValue()));
        }else if(strType.toUpperCase().equals("BIT") ){
            sld = String.valueOf(rs.getString(codigo.intValue()));
        }else if (strType.toUpperCase().equals("TEXT")){
            sld = rs.getString(codigo.intValue());
        }
        return sld;
    }

    /**
     * Segun el tipo entregado que se tiene en la base de datos
     * se entrega un string con el tipo que le debe corresponder en Java
     * @param strType
     * @return
     */
    private String castTypeDataDBtoAPL(String strType){
        String sld = new String();
        if (strType.toUpperCase().equals("CHAR")){
            sld = "java.lang.String";
        }else if (strType.toUpperCase().equals("VARCHAR")){
            sld = "java.lang.String";
        }else if(strType.toUpperCase().equals("INT") ){
            sld = "java.lang.Integer";
        }else if(strType.toUpperCase().equals("DATETIME") ){
            sld = "java.sql.Date";
        }else if(strType.toUpperCase().equals("BIT") ){
            sld = "java.lang.Integer";
        }else if(strType.toUpperCase().equals("TEXT") ){
            sld = "java.lang.String";
        }
        return sld;
    }
}
