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

public final class Consulta {
    private int claveConsulta;
    private int clavePerfil;
    private int claveAplicacion;
    private int claveForma;
    private String accion;
    private String sql;
    private int pk;
    private String w;
    private ArrayList<String> reglasDeReemplazo=new ArrayList<String> ();
    ArrayList<Campo> campos = new ArrayList<Campo> ();
    private ArrayList<ArrayList> registros = new ArrayList<ArrayList>();
    private String error;

    public int getClaveAplicacion() {
        return claveAplicacion;
    }

    public void setClaveAplicacion(int claveAplicacion) {
        this.claveAplicacion = claveAplicacion;
    }

    public int getClavePerfil() {
        return clavePerfil;
    }

    public void setClavePerfil(int clavePerfil) {
        this.clavePerfil = clavePerfil;
    }

    public int getClaveConsulta() {
        return claveConsulta;
    }

    public void setClaveConsulta(int claveConsulta) {
        this.claveConsulta = claveConsulta;
    }

    
    public void setAccion(String accion) {
        this.accion = accion;
    }

    public void setCampos(ArrayList<Campo> campos) {
        this.campos = campos;
    }
    
    public void setCampos(Conexion oDb) {
    
    Campo fdCampo;
    ResultSet oRs;
    ResultSet rsFieldDictionary;
    int nCols;
    String qryCampos="";
    try {    
            oRs = oDb.getRs(this.sql);
            if (!oDb.getError().equals("")) {
                this.error=oDb.getError();
                return;
            }

            for (int i = 0; i < oRs.getMetaData().getColumnCount(); i++) {
                fdCampo = new Campo(oRs.getMetaData().getColumnName(i+1), oRs.getMetaData().getColumnTypeName(i+1), oRs.getMetaData().isAutoIncrement(i+1));
                qryCampos=aplicaReglasDeReemplazo("SELECT * FROM campo_forma WHERE clave_forma=" + getClaveForma() + " AND campo='" + fdCampo.getNombre() + "' AND clave_perfil=%clave_perfil");
                
                rsFieldDictionary = oDb.getRs(qryCampos);
                    if (rsFieldDictionary.next()) {
                    fdCampo.setAlias(rsFieldDictionary.getString("alias_campo"));
                    fdCampo.setObligatorio(rsFieldDictionary.getByte("obligatorio"));
                    fdCampo.setTipoControl(rsFieldDictionary.getString("tipo_control"));
                    fdCampo.setEvento(rsFieldDictionary.getString("evento"));
                    fdCampo.setClaveFormaForanea(rsFieldDictionary.getInt("clave_forma_foranea"));
                    fdCampo.setFiltroForaneo(rsFieldDictionary.getString("filtro_foraneo"));
                    fdCampo.setEditaFormaForanea(rsFieldDictionary.getByte("edita_forma_foranea"));
                    fdCampo.setNoPermitirValorForaneoNulo(rsFieldDictionary.getByte("no_permitir_valor_foraneo_nulo"));
                    fdCampo.setAyuda(rsFieldDictionary.getString("ayuda"));
                    fdCampo.setDatoSensible(rsFieldDictionary.getByte("dato_sensible"));
                    fdCampo.setActivo(rsFieldDictionary.getByte("activo"));
                    fdCampo.setTamano(rsFieldDictionary.getInt("tamano"));
                    fdCampo.setVisible(rsFieldDictionary.getByte("visible"));
                    fdCampo.setValorPredeterminado(rsFieldDictionary.getString("valor_predeterminado"));
                    fdCampo.setJustificarCambio(rsFieldDictionary.getByte("justificar_cambio"));
                    fdCampo.setUsadoParaAgrupar(rsFieldDictionary.getByte("usado_para_agrupar"));
                    
                    //Vacía la regla de reemplazo de acuerdo al filtro foraneo
                    if (rsFieldDictionary.getString("filtro_foraneo")!=null) {
                        String wForaneo="";
                        
                        for (int k=0; k<this.getReglasDeReemplazo().size(); k++) {
                            if (this.getReglasDeReemplazo().get(k).split("=")[0].equals(rsFieldDictionary.getString("filtro_foraneo")))
                                wForaneo=this.getReglasDeReemplazo().get(k);
                                break;
                        }
                        
                        fdCampo.setFormaForanea(fdCampo.getClaveFormaForanea(),wForaneo, this.getReglasDeReemplazo() );
                    }             
                }
                rsFieldDictionary.close();
                this.campos.add(fdCampo);
            }
            /* Recupera datos del qry */
            nCols = oRs.getMetaData().getColumnCount();
            setRegistros( new ArrayList<ArrayList>());
            while (oRs.next()) {
                ArrayList<Object> row = new ArrayList<Object>();
                for (int i = 0; i < nCols; i++) {
                    row.add(oRs.getObject(i+1));
                }
                this.registros.add(row);
            }

            /* Cierra recordset */
            oRs.close();
        } catch (Exception e) {
            this.error = e.toString();
        } finally {
            oDb.cierraConexion();
        }
    }
    

    public void setError(String error) {
        this.error = error;
    }

    public void setClaveForma(int claveForma) {
        this.claveForma = claveForma;
    }

    public void setRegistros(ArrayList<ArrayList> registros) {
        this.registros = registros;
    }
    
    public String setSQL(Conexion oDb, int claveForma, String accion, int pk, String w, ArrayList<String> reglasDeReemplazo ) {
        setClaveForma(claveForma);
        setAccion(accion);
        setPk(pk);
        setW(w);
        setReglasDeReemplazo(reglasDeReemplazo);

        try {
            /* Recupera sql del qry */
            this.sql= oDb.getConsultaPorClaveFormaYAccion(this.claveForma, this.accion);

            if (this.sql.equals("")) {
                this.error=oDb.getError();
                return "";
            }

            if (this.sql.toLowerCase().contains("where")&& !w.equals(""))
                this.sql+=" and " + w;
            else if (!w.equals(""))
                this.sql+=" where  " + w;

            /*Reemplaza el valor de PK en la query obtenida */
            this.sql=this.sql.replaceAll("%pk",  String.valueOf(getPk()));
            
            /*Otras reglas de reemplazo*/
            this.sql=aplicaReglasDeReemplazo(this.sql);

           return this.sql;
            
        } catch (Exception e) {
            this.error = e.toString();
            return "";
        } finally {
        }
        
    }

    public String getSQL() {
        return sql;
    }

    public String getAccion() {
        return accion;
    }

    public ArrayList<Campo> getCampos() {
        return campos;
    }

    public String getError() {
        return error;
    }

    public int getClaveForma() {
        return claveForma;
    }

    public ArrayList<ArrayList> getRegistros() {
        return registros;
    }

    public void setRegistros(Conexion oDb) {
        int nCols=0;
        ResultSet oRs;
        
        try {
            oRs = oDb.getRs(this.sql);
            if (!oDb.getError().equals("")) {
                this.error=oDb.getError();
                return;
            }
            /* Recupera datos del qry */
            nCols = oRs.getMetaData().getColumnCount();
            while (oRs.next()) {
                ArrayList<Object> row = new ArrayList<Object>();
                for (int i = 0; i < nCols; i++) {
                    row.add(oRs.getObject(i+1));
                }
                this.registros.add(row);
            }

            /* Cierra recordset */
             oRs.close();

        } catch (Exception e) {
            this.error = e.toString();
        } finally {
           
        }

    }
    
    public void setSQL(String sql) {
            this.sql= sql;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public ArrayList<String> getReglasDeReemplazo() {
        return reglasDeReemplazo;
    }

    public void setReglasDeReemplazo(ArrayList<String> reglasDeReemplazo) {
        this.reglasDeReemplazo = reglasDeReemplazo;
    }
    
    public String aplicaReglasDeReemplazo(String s) {
            /*Otras reglas de reemplazo*/
            String parameter="";
            String value="";
            for (int i=0;i<reglasDeReemplazo.size();i++){
                parameter=reglasDeReemplazo.get(i).split("=")[0];
                value=reglasDeReemplazo.get(i).split("=")[1];
                s=s.replaceAll(parameter, value);
            }
            return s;
    }
    
    public Consulta() {
        
    }

    public Consulta(int claveForma, String accion, int pk, String w, ArrayList<String> reglasDeReemplazo ) {
        Conexion oDb=null;        
        
        try {
           oDb= new Conexion();
           if (this.claveForma!=claveForma || !this.accion.equals(accion) || this.campos.isEmpty()) {
               this.setSQL(oDb, claveForma, accion, pk, w, reglasDeReemplazo);
               this.setCampos(oDb); }
           else
               this.setSQL(oDb, claveForma, accion, pk, w, reglasDeReemplazo);
          
           this.setRegistros(oDb);
           
        } catch (Exception e) {
            this.error = e.toString();
        } finally {
            oDb.cierraConexion();
        }
    }

    public static void main(String args[]) {
        Consulta configuracion = new Consulta(2, "select",0,"",null);
        //Verifica error en el constructor
        if (configuracion.error!=null) {
            System.out.println(configuracion.error);
            return;
        }

        ArrayList<Campo> cd = configuracion.getCampos();
        ArrayList<ArrayList> d = configuracion.getRegistros();

        if  (configuracion.error!=null) {
            System.out.println("Error de conectividad: " + configuracion.error );
            return;
        }

        System.out.println("*** Inicio de definición de columnas ***");
        for (Campo fd : cd) 
            System.out.println(fd.toString());

        System.out.println("*** Fin de definición de columnas ***");
        System.out.println("*** Inicio de datos ***");
        int i=0;
        for (ArrayList r : d) {
            System.out.println("");
            i=0;
            for (Object sd : r) {
                 System.out.println(cd.get(i).nombre + ": "+ sd.toString());
                 i++;
            }
        }
        System.out.println("*** Fin de datos ***");
    }
}


