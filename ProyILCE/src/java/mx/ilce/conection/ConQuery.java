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
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.ilce.bean.Campo;
import mx.ilce.bean.HashCampo;

/**
 *
 * @author ccatrilef
 */
public class ConQuery {

    private Connection conn;
    /**
     * Realiza la conexion a la base de datos. Los parametros de conexion se
     * deben cambiar a un properties para una facil mantencion sin compilar.
     * @throws SQLException
     */
    private void getConexion() throws SQLException{
        StringBuffer strConexion = new StringBuffer("");
        try {
            String server = "172.16.1.28";
            String base = "ILCE_frmwrk";

            strConexion.append("jdbc:sqlserver://");
            strConexion.append(server);
            strConexion.append(":1433");
            strConexion.append(";databasename=");
            strConexion.append(base);
            strConexion.append(";selectMethod=cursor;");

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(strConexion.toString(),"javaws","ikaro75");
        }catch (SQLException sqlex){
            Logger.getLogger(ConSession.class.getName()).log(Level.SEVERE, null, sqlex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConSession.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try{
            getConexion();
            String query = getQueryById(idQuery);
            if ((!"".equals(query)) && (arrData != null)){
                Statement ps = conn.createStatement();
                for(int i=1;i<=arrData.length;i++){
                    String strData = arrData[i-1];
                    query = query.replaceFirst("%"+i, strData);
                }
                ResultSet rs = ps.executeQuery(query);
                ResultSetMetaData rstm = rs.getMetaData();

                for (int i=1;i<=rstm.getColumnCount();i++){
                    Campo cmp = new Campo(rstm.getColumnName(i).toUpperCase(), Integer.valueOf(i),
                                        rstm.getColumnTypeName(i).toUpperCase());
                    cmp.setTypeDataAPL(castTypeDataDBtoAPL(rstm.getColumnTypeName(i).toUpperCase()));
                    hsCmp.addCampo(cmp);
                }
                while (rs.next()){
                    int i=0;
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
                    hsCmp.addListData(lstData,i);
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            conn.close();
        }
        return hsCmp;
    }

    
     /**
     * Obtiene la data aplicando la query seleccionada mediante el idQuery.
     * Esta query no posee parametros de entrada.
     * @param idQuery   Codigo de la query a utilizar
     * @return HashCampo.  Contiene el listado de registros obtenidos y los campos
     * que posee la query, con sus tipos de datos
     * @throws SQLException
     */
    public HashCampo getData(Integer idQuery) throws SQLException{
        HashCampo hsCmp = new HashCampo();
        try{
            getConexion();
            String query = getQueryById(idQuery);
            if (!"".equals(query)){
                Statement ps = conn.createStatement();
                ResultSet rs = ps.executeQuery(query);
                ResultSetMetaData rstm = rs.getMetaData();

                for (int i=1;i<=rstm.getColumnCount();i++){
                    Campo cmp = new Campo(rstm.getColumnName(i).toUpperCase(), Integer.valueOf(i),
                                        rstm.getColumnTypeName(i).toUpperCase());
                    cmp.setTypeDataAPL(castTypeDataDBtoAPL(rstm.getColumnTypeName(i).toUpperCase()));
                    hsCmp.addCampo(cmp);
                }
                while (rs.next()){
                    int i=0;
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
                    hsCmp.addListData(lstData,i);
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            conn.close();
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
        try{
            getConexion();
            String query = getQueryById(idQuery);
            if (!"".equals(query)){
                PreparedStatement ps = conn.prepareStatement(query);
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
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData rstm = rs.getMetaData();

                for (int i=1;i<=rstm.getColumnCount();i++){
                    Campo cmp = new Campo(rstm.getColumnName(i), Integer.valueOf(i),
                                        rstm.getColumnTypeName(i).toUpperCase());
                    hsCmp.addCampo(cmp);
                }
                while (rs.next()){
                    int i=0;
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
                    hsCmp.addListData(lstData,i);
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            conn.close();
        }
        return hsCmp;
    }

    /**
     * Busca una query desde la base de datos, se le pasa el ID que posee
     * en la tabla
     * @param idQuery
     * @return
     */
    private String getQueryById(Integer idQuery){
        String strSld = "";
        try{
            if (conn == null){
                getConexion();
            }
            String query = "select consulta from consulta_forma "
                    + " where clave_consulta = " + idQuery.toString();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()){
                strSld = rs.getString(1);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return strSld;
    }

    /**
     * Segun el tipo de dato que se le entrega, el cual viene desde la base de
     * datos, utiliza la conversion respectiva para dejar el valor como String
     * @param strType
     * @param rs
     * @param codigo
     * @return
     * @throws SQLException
     */
    private String getValueCampo(String strType, ResultSet rs, Integer codigo) throws SQLException{
        String sld = new String();
        if (strType.equals("VARCHAR")){
            sld = rs.getString(codigo.intValue());
        }else if(strType.equals("INT") ){
            sld = String.valueOf(rs.getBigDecimal(codigo.intValue()));
        }else if(strType.equals("DATETIME") ){
            sld = String.valueOf(rs.getDate(codigo.intValue()));
        }else if(strType.equals("BIT") ){
            sld = String.valueOf(rs.getString(codigo.intValue()));
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
        if (strType.equals("VARCHAR")){
            sld = "java.lang.String";
        }else if(strType.equals("INT") ){
            sld = "java.lang.Integer";
        }else if(strType.equals("DATETIME") ){
            sld = "java.sql.Date";
        }else if(strType.equals("BIT") ){
            sld = "java.lang.Integer";
        }
        return sld;
    }
}
