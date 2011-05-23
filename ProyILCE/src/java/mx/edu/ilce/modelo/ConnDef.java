/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.ilce.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
/**
 *
 * @author danielm
 */
public class ConnDef {

    private String server;
    private String db;
    private String user;
    private String pw;
    private Connection conn;
    private Statement st;
    private ResultSet rs;
    private String error = "";

    public ConnDef(String server, String db, String user, String pw) {
        this.server = server;
        this.db = db;
        this.user = user;
        this.pw = pw;
    }

    public ConnDef() {
        this.server = "172.16.1.28:1433";
        this.db = "ILCE_frmwrk";
        this.user = "javaws";
        this.pw = "ikaro75";
    }

    public void setDb(String sDb) {
        db = sDb;
    }

    public void setError(String sError) {
        error = sError;
    }

    public String getError() {
        return error;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String openConnection() {
        try {
            /*Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");*/
            if (db == null) {
                db = "ILCE_frmwrk";
            }

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection("jdbc:sqlserver://" + server + ";databaseName=" + db + ";selectMethod=cursor;", user, pw);
            return "";
        } catch (Exception e) {
            // Error en algun momento.
            setError(e.toString());
            return error;
        }
    }

    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
            conn = null;
        } catch (Exception e) {
            setError(e.toString());
        }
    }

    public ResultSet getRs(String q) {
        try {
            String sResultado = openConnection();

            if (!sResultado.equals("")) {
                return null;
            }
            System.out.println(q);
            st = conn.createStatement();
            rs = st.executeQuery(q);
        } catch(SQLException sqlex) {
            setError(sqlex.toString());
        }
        catch (Exception e) {
            setError(e.toString());
        } finally {
            return rs;
        }
    }

    public ArrayList<FieldDef> getColumnDefFromQry(String s) {
        ArrayList<FieldDef> aRegistro = new ArrayList<FieldDef>();
        ResultSet oRs;
        FieldDef fdCampo;
        try {
            oRs = getRs(s);
            for (int i = 1; i <= oRs.getMetaData().getColumnCount(); i++) {
                fdCampo = new FieldDef(oRs.getMetaData().getColumnName(i), oRs.getMetaData().getColumnTypeName(i), oRs.getMetaData().isAutoIncrement(i));
                aRegistro.add(fdCampo);
            }
            oRs.close();
        } catch(SQLException sqlex) {
            setError(sqlex.toString());
        } catch (Exception e) {
            setError(e.toString());
        } finally {
            closeConnection();
            return aRegistro;
        }
    }

    public ArrayList<ArrayList> getDataByQry(String s) {
        ArrayList<ArrayList> aQryData = new ArrayList<ArrayList>();
        ResultSet oRs;
        int nCols;
        try {
            oRs = getRs(s);
            nCols = oRs.getMetaData().getColumnCount();
            while (oRs.next()) {
                ArrayList<Object> aRow = new ArrayList<Object>();
                for (int i = 1; i <= nCols; i++) {
                    aRow.add(rs.getObject(i));
                }
                aQryData.add(aRow);
            }
            oRs.close();
        } catch (Exception e) {
            setError(e.toString());
        } finally {
            closeConnection();
            return aQryData;
        }

    }

    public String getQryById(int nClaveForma, String sAccion) {
        ResultSet oRs;
        String sResultado = "";
        String s = "select consulta from "
                + " consulta_forma "
                + " where "
                + " clave_forma=" + nClaveForma + " AND "
                + " tipo_accion='" + sAccion + "'";

        try {
            oRs = getRs(s);

            if (oRs==null) { // No se pudo establecer conexión
                return "";
            }

            if (!oRs.next()) {
                setError("No hay consulta definida para la clave " + nClaveForma + " y acción " + sAccion);
                return "";
            }

            sResultado = oRs.getString("consulta");
            oRs.close();
        } catch(SQLException sqlex) {
            setError(sqlex.toString());
        } catch (Exception e) {
            // Error en algun momento.
            setError(e.toString());
        }
        finally {
            closeConnection();
            return sResultado;
        }

    }

    public ArrayList<ArrayList> getDataByQryId(int nClaveForma, String sAccion) {
        return getDataByQry(getQryById(nClaveForma, sAccion));
    }

    public int getLogin(String sUser, String sPw) {
        String s = openConnection();

        if (!s.equals("")) {
            return 0;
        }

        s = "SELECT e.clave_empleado FROM empleado e, aplicacion_empleado ae "
                + "WHERE e.clave_empleado=ae.clave_empleado AND "
                + "e.email='" + sUser.replace("'", "''") + "' AND "
                + "e.password='" + sPw.replace("'", "''") + "' AND "
                + "ae.activo=1";

        int nUsuario = 0;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(s);
            if (rs.next()) {
                nUsuario = rs.getInt("clave_empleado");
            }
        } catch (Exception e) {
            // Error en algun momento.
            setError(e.toString());
        } finally {
            closeConnection();
            return nUsuario;
        }
    }
}