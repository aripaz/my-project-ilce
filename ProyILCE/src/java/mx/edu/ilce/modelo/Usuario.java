/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.ilce.modelo;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author danielm
 */
public final class Usuario {
    private int clave;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private int clavePerfil;
    private int claveArea;
    private int activo;
    private String foto;
    private ArrayList<Aplicacion> aplicaciones = new  ArrayList<Aplicacion>();
    private ArrayList<String> filtrosForaneos = new ArrayList<String>();
    private ArrayList<String> reglasDeReemplazo= new ArrayList<String>();
    private Consulta consulta;

    public ArrayList<String> getReglasDeReemplazo() {
        return reglasDeReemplazo;
    }

    public void setReglasDeReemplazo(ArrayList<String> reglasDeReemplazo) {
        this.reglasDeReemplazo = reglasDeReemplazo;
    }

    
    public ArrayList<String> getFiltrosForaneos() {
        return filtrosForaneos;
    }

    public void setFiltrosForaneos(ArrayList<String> filtrosForaneos) {
        if (filtrosForaneos==null)
            this.filtrosForaneos.clear();
        else
            this.filtrosForaneos = filtrosForaneos;
    }
    
    public void pushFiltrosForaneos(String filtrosForaneos) {
        this.filtrosForaneos.add(filtrosForaneos);
    }    
 
    public ArrayList<Aplicacion> getAplicaciones() {
        return aplicaciones;
    }

    public void setAplicaciones(ArrayList<Aplicacion> aplicaciones) {
        this.aplicaciones = aplicaciones;
    }
    
    public void setAplicaciones(Conexion oDb) {

        this.aplicaciones.clear();
        try {
            
            ResultSet rsApps= oDb.getRs("select a.clave_aplicacion, a.aplicacion, a.alias_menu_nueva_entidad, a.alias_menu_mostrar_entidad, "+
                                     "a.clave_forma_principal from perfil_aplicacion pa, aplicacion a " +
                                     "where pa.clave_aplicacion=a.clave_aplicacion and pa.activo=1 and pa.clave_perfil="+ this.clavePerfil + " order by a.clave_aplicacion");
            while (rsApps.next()) {
                Aplicacion app= new Aplicacion();
                app.setClaveAplicacion(rsApps.getInt("clave_aplicacion"));
                app.setAplicacion(rsApps.getString("aplicacion"));
                app.setAliasMenuNuevaEntidad(rsApps.getString("alias_menu_nueva_entidad"));
                app.setAliasMenuMuestraEntidad(rsApps.getString("alias_menu_mostrar_entidad"));
                app.setClaveFormaPrincipal(rsApps.getInt("clave_forma_principal"));
                
                /* Recupera las formas de la aplicación con sus permisos */ 
                ResultSet rsForms= oDb.getRs( "select  distinct pf.clave_forma, f.forma, "+
                                "(select top 1 1 from permiso_forma where clave_forma=pf.clave_forma AND clave_perfil=pf.clave_perfil AND clave_permiso=1) as selectx,"+
                                "(select top 1 1 from permiso_forma where clave_forma=pf.clave_forma AND clave_perfil=pf.clave_perfil AND clave_permiso=2) as insertx,"+
                                "(select top 1 1 from permiso_forma where clave_forma=pf.clave_forma AND clave_perfil=pf.clave_perfil AND clave_permiso=3) as updatex,"+
                                "(select top 1 1 from permiso_forma where clave_forma=pf.clave_forma AND clave_perfil=pf.clave_perfil AND clave_permiso=4) as deletex,"+
                                "(select top 1 1 from permiso_forma where clave_forma=pf.clave_forma AND clave_perfil=pf.clave_perfil AND clave_permiso=5) as sensitivedata "+
                                "from "+
                                "permiso_forma pf,"+
                                "forma f "+
                                "where pf.clave_forma=f.clave_forma " +
                                "and pf.clave_perfil=" + this.clavePerfil + 
                                " and f.clave_aplicacion=" + app.getClaveAplicacion());
                 while (rsForms.next()) {
                    Forma form = new Forma(); 
                    form.setClaveForma(rsForms.getInt("clave_forma"));
                    form.setForma(rsForms.getString("forma"));
                    form.setSelect(rsForms.getInt("selectx")>0?true:false);
                    form.setInsert(rsForms.getInt("insertx")>0?true:false);
                    form.setUpdate(rsForms.getInt("updatex")>0?true:false);
                    form.setDelete(rsForms.getInt("deletex")>0?true:false);
                    form.setSensitiveData(rsForms.getInt("sensitivedata")>0?true:false);   
                    app.pushForma(form);
                 }
                 rsForms.close();       
                this.aplicaciones.add(app);
            }
            
            rsApps.close(); 
        } catch (Exception e) {
            System.out.print(oDb.getError());
        } finally {

        }    
    }
   
    
    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public int getClave() {
        return clave;
    }

    public void setClave(int clave) {
        this.clave = clave;
    }

    public int getClaveArea() {
        return claveArea;
    }

    public void setClaveArea(int claveArea) {
        this.claveArea = claveArea;
    }

    public int getClavePerfil() {
        return clavePerfil;
    }

    public void setClavePerfil(int clavePerfil) {
        this.clavePerfil = clavePerfil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public void setConsulta(int clave_forma, String accion, int pk, String w ) {
        /*Se valida que exista la forma en el perfil del usuario*/
        if (this.getAplicaciones()==null) {
             this.consulta.setError("El perfil no tiene aplicaciones asociadas");
             return;
        }
        
        boolean found=false;     
        for (Aplicacion a : this.getAplicaciones()) {
            int nApp=a.getClaveAplicacion();
            for (Forma f : a.getFormas()) {
                if (f.getClaveForma()==clave_forma) {
                    found=true;

                    this.consulta = new  Consulta (clave_forma, accion, pk, w, this.reglasDeReemplazo);  
                    this.consulta.setClaveAplicacion(nApp);
                    break;
                }
            }
            if(found) break;
        }
        
        if(!found)
            this.consulta.setError("El perfil no está autorizado para acceder a la forma");
    }
    
    public Usuario(int clave) {
        this.clave = clave;
        
        Conexion oDb = new Conexion();
        
        try {
            ResultSet oRs= oDb.getRs("select * from empleado where clave_empleado="+ this.clave);
            if (oRs.next()) {
                this.nombre=oRs.getString("nombre");
                this.apellidoPaterno=oRs.getString("apellido_paterno");
                this.apellidoMaterno=oRs.getString("apellido_materno");
                this.claveArea=oRs.getInt("clave_area");
                this.clavePerfil=oRs.getInt("clave_perfil");
                this.email=oRs.getString("email");
                this.foto=oRs.getString("foto");
                this.activo=oRs.getInt("activo");
                this.setAplicaciones(oDb);
                this.reglasDeReemplazo.add("%clave_empleado="+Integer.toString(this.clave));
                this.reglasDeReemplazo.add("%clave_area="+Integer.toString(this.claveArea));
                this.reglasDeReemplazo.add("%clave_perfil="+Integer.toString(this.clavePerfil));
            }
            else
                System.out.print("Usuario no encontrado");
           oRs.close(); 
        } catch (Exception e) {
            System.out.print(oDb.getError());
        } finally {
            oDb.cierraConexion();
        }
        
        
    }
    
    
}
