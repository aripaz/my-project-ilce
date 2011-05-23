/*
// * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ilce.modelo;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author danielm
 */

public class QryDef {

    private int id;
    private String accion;
    private String source;
    ArrayList<FieldDef> columnDefinition;
    private ArrayList<ArrayList> rows;
    private String error;

    public String getAccion() {
        return accion;
    }

    public ArrayList<FieldDef> getColumnDefinition() {
        return columnDefinition;
    }

    public String getError() {
        return error;
    }

    public int getId() {
        return id;
    }

    public ArrayList<ArrayList> getRows() {
        return rows;
    }

    public String getSource() {
        return source;
    }

    public QryDef(int id, String accion, String w ) {
        this.id = id;
        this.accion = accion;
        this.columnDefinition = new ArrayList<FieldDef>();

        ConnDef oDb = new ConnDef();

        ResultSet oRs;
        ResultSet rsFieldDictionary;
        FieldDef fdCampo;

        int nCols;
        try {
            /* Recupera source del qry */
            this.source = oDb.getQryById(this.id, this.accion);

            if (this.source.equals("")) {
                this.error=oDb.getError();
                return;
            }

            if (this.source.toLowerCase().contains("where")&& !w.equals(""))
                this.source+=" and " + w;
            else if (!w.equals(""))
                this.source+=" where  " + w;
            
            /* Recupera definición de columna */
            oRs = oDb.getRs(this.source);
            if (!oDb.getError().equals("")) {
                this.error=oDb.getError();
                return;
            }

            for (int i = 1; i <= oRs.getMetaData().getColumnCount(); i++) {
                fdCampo = new FieldDef(oRs.getMetaData().getColumnName(i), oRs.getMetaData().getColumnTypeName(i), oRs.getMetaData().isAutoIncrement(i));
                rsFieldDictionary = oDb.getRs("SELECT * FROM campo_forma WHERE clave_forma=" + this.id + " AND campo='" + fdCampo.getName() + "'");
                if (rsFieldDictionary.next()) {
                    fdCampo.setAlias(rsFieldDictionary.getString("alias_campo"));
                    fdCampo.setObligatorio(rsFieldDictionary.getByte("obligatorio"));
                    fdCampo.setTipo_control(rsFieldDictionary.getString("tipo_control"));
                    fdCampo.setEvento(rsFieldDictionary.getString("evento"));
                    fdCampo.setClave_foranea(rsFieldDictionary.getInt("evento"));
                    fdCampo.setFiltro_foraneo(rsFieldDictionary.getString("filtro_foraneo"));
                    fdCampo.setAyuda(rsFieldDictionary.getString("ayuda"));
                    fdCampo.setDato_sensible(rsFieldDictionary.getByte("dato_sensible"));
                    fdCampo.setActivo(rsFieldDictionary.getByte("activo"));
                }
                rsFieldDictionary.close();
                this.columnDefinition.add(fdCampo);
            }
            /* Recupera datos del qry */
            nCols = oRs.getMetaData().getColumnCount();
            this.rows = new ArrayList<ArrayList>();
            while (oRs.next()) {
                ArrayList<Object> row = new ArrayList<Object>();
                for (int i = 1; i <= nCols; i++) {
                    row.add(oRs.getObject(i));
                }
                this.rows.add(row);
            }

            /* Cierra recordset */
            oRs.close();
        } catch (Exception e) {
            this.error = e.toString();
        } finally {
            oDb.closeConnection();
        }
    }

    public static void main(String args[]) {
        QryDef configuracion = new QryDef(1, "select"," where x=2");
        //Verifica error en el constructor
        if (!configuracion.error.equals("")) {
            System.out.println(configuracion.error);
            return;
        }

        ArrayList<FieldDef> cd = configuracion.getColumnDefinition();
        ArrayList<ArrayList> d = configuracion.getRows();

        if  (!configuracion.error.equals("")) {
            System.out.println("Error de conectividad: " + configuracion.error );
            return;
        }

        System.out.println("*** Inicio de definición de columnas ***");
        for (FieldDef fd : cd) 
            System.out.println(fd.toString());

        System.out.println("*** Fin de definición de columnas ***");
        System.out.println("*** Inicio de datos ***");
        int i = 0;
        for (ArrayList r : d) {
            System.out.println("");
            for (Object sd : r) {
                 System.out.println(cd.get(i).name + ": "+ sd.toString());
                 i++;
            }
        }
        System.out.println("*** Fin de datos ***");
    }
}


