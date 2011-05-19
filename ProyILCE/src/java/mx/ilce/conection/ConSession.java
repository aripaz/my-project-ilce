/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.conection;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import mx.ilce.bean.HashCampo;
import mx.ilce.bean.User;
import mx.ilce.component.AdminFile;
import mx.ilce.component.ListHash;
import mx.ilce.controller.Aplicacion;
import mx.ilce.controller.Perfil;
import mx.ilce.handler.ExceptionHandler;

/**
 *  Clase para la implementacion de los metodos de conexion y obtencion de datos
 * asociados a la Session
 * @author ccatrilef
 */
public class ConSession {

    private Properties prop = null;
    private AdminFile adm = new AdminFile();

    public ConSession() throws ExceptionHandler{
        try{
            this.prop = AdminFile.leerIdQuery();
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para abrir Conexion ConSession");
        }
    }

    public Integer getIdQuery(String key) throws ExceptionHandler{
        Integer intSld = new Integer(0);
        try{
            if (prop == null){
                prop = AdminFile.leerIdQuery();
            }
            intSld = AdminFile.getIdQuery(prop,key);
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener ID QUERY desde properties");
        }
        return intSld;
    }

    /**
     * Obtiene los datos de Usuario, mediante los parametros ingresados de
     * user y password, si no corresponde la dupla entregada, se hace una
     * segunda validacion para ver si al menos existe el usuario y solo hubo
     * error en la password
     * @param user  Nombre dado en el sistema al usuario para su identificacion
     * @param password  Password asociada al usuario para identificarlo
     * @return
     * @throws SQLException
     */
    public User getUser(String user, String password) throws ExceptionHandler{
        User usr = new User();
        try{
            String[] strData = new String[2];
            strData[0] = user;
            strData[1] = password;

            ConQuery connQ = new ConQuery();
            //validamos user y password
            HashCampo hsCmp = connQ.getData(getIdQuery(AdminFile.LOGIN), strData);
            if (hsCmp.getListData().isEmpty()){
                usr.setIsLogged(false);
                String[] datUser = new String[1];
                datUser[0]=user;
                //validamos que es problema de la password
                HashCampo hsCmpUsr = connQ.getData(getIdQuery(AdminFile.USER), datUser );
                if (hsCmpUsr.getListData().isEmpty()){
                    usr.setMessage("Usuario no existe en los registros");
                }else{
                    usr.setMessage("No coincide la password con la del usuario");
                }
            }else{
                //introducimos en el Bean los datos obtenidos
                ListHash lst = new ListHash();
                usr = (User) lst.getBean(User.class ,hsCmp);
                usr.setIsLogged(true);
                usr.setLogin(user);
                usr.setPassword(password);
            }
        }catch(Exception ex){
            usr.setIsLogged(false);
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el USER");
        }finally{
            
        }
        return usr;
    }

    /**
     * Obtiene los datos de Usuario, desde el bean user, desde el se usan el
     * login y password, si no corresponde la dupla entregada, se hace una
     * segunda validacion para ver si al menos existe el usuario y solo hubo
     * error en la password
     * @param usuario   Bean que contiene los datos del usuario
     * @return
     * @throws SQLException
     */
    public User getUser(User usuario) throws ExceptionHandler{
        User usr = new User();
        try{
            //String user, String password
            String[] strData = new String[2];
            strData[0] = usuario.getLogin();
            strData[1] = usuario.getPassword();

            ConQuery connQ = new ConQuery();
            //validamos user y password
            HashCampo hsCmp = connQ.getData(getIdQuery(AdminFile.LOGIN), strData);
            if (hsCmp.getListData().isEmpty()){
                usr.setIsLogged(false);
                String[] datUser = new String[1];
                datUser[0]=usuario.getNombre();
                //validamos que es problema de la password
                HashCampo hsCmpUsr = connQ.getData(getIdQuery(AdminFile.USER), datUser );
                if (hsCmpUsr.getListData().isEmpty()){
                    usr.setMessage("Usuario no existe en los registros");
                }else{
                    usr.setMessage("No coincide la password con la del usuario");
                }
            }else{
                //introducimos en el Bean los datos obtenidos
                ListHash lst = new ListHash();
                usr = (User) lst.getBean(User.class ,hsCmp);
                usr.setIsLogged(true);
                usr.setLogin(usuario.getLogin());
                usr.setPassword(usuario.getPassword());
            }
        }catch(Exception ex){
            usr.setIsLogged(false);
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el USER");
        }finally{

        }
        return usr;
    }

    /**
     * Obtiene los datos del perfil de un usuario, desde el bean User se utiliza
     * el campo clavePerfil como parametro de entrada. Ademas de los datos del
     * perfil, entrega el listado de aplicaciones que le corresponden segun su
     * perfil
     * @param user  Bean que contiene los datos del usuario
     * @return
     * @throws SQLException
     */
    public Perfil getPerfil(User user) throws ExceptionHandler{
        Perfil perfil = new Perfil();
        try{
            String[] strData = new String[1];
            strData[0] = String.valueOf(user.getClavePerfil());

            ConQuery connQ = new ConQuery();
            HashCampo hsCmp = connQ.getData(getIdQuery(AdminFile.PERFIL), strData);

            if (!hsCmp.getListData().isEmpty()){
                //introducimos en el Bean los datos obtenidos
                ListHash lst = new ListHash();
                perfil = (Perfil) lst.getBean(Perfil.class ,hsCmp);
                List lstApli = lst.getListBean(Aplicacion.class, hsCmp);
                perfil.setLstAplicacion(lstApli);
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el PERFIL");
        }finally{

        }
        return perfil;
    }

    /**
     * Obtiene los datos para completar el XML de TAB que le corresponden
     * segun Perfil. Desde el perfil se toma el campo lstAplicacion para
     * obtener todos los tab segun los datos aplicacion y forma.
     * @param perfil    Bean que contiene los datos del perfil
     * @return
     */
    public HashCampo getTabForma(Perfil perfil) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            List lstApl = perfil.getLstAplicacion();

            if ((lstApl != null)&&(!lstApl.isEmpty())){
                Iterator it = lstApl.iterator();
                String[] strData = new String[4];
                ConQuery connQ = new ConQuery();
                HashCampo hsCmpAux = null;
                Integer lenList = hsCmp.getLengthData();
                while (it.hasNext()){
                    Aplicacion apl = (Aplicacion) it.next();
                    strData[0]= String.valueOf(apl.getClaveAplicacion());
                    strData[1]= String.valueOf(apl.getClaveFormaPrincipal());
                    strData[2]= String.valueOf(apl.getClaveAplicacion());
                    strData[3]= String.valueOf(apl.getClaveFormaPrincipal());
                    hsCmpAux = connQ.getData(getIdQuery(AdminFile.TABFORMA), strData);
                    if ((hsCmp.getLengthCampo()==0)&&(hsCmpAux!=null)) {
                        hsCmp.setListCampos(hsCmpAux.getListCampos());
                    }
                    hsCmp.addListToListData(hsCmpAux);
                }
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el PERFIL");
        }finally{

        }
        return hsCmp;
    }

    /**
     * Obtiene los datos que se ingresaran al XML de Session, segun el usuario.
     * Se utiliza como parametro de entrada el campo claveEmpleado, desde el
     * Bean User.
     * En ObjectData se coloca un objeto Bean del tipo User, con los datos que
     * se obtuvieron.
     * @param usuario   Bean que contiene los datos del usuario
     * @return
     */
    public HashCampo getUserXML(User usuario) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            String[] strData = new String[1];
            ConQuery connQ = new ConQuery();
            strData[0] = usuario.getClaveEmpleado().toString();
            hsCmp = connQ.getData(getIdQuery(AdminFile.XMLSESSION), strData);
            hsCmp.setObjData(usuario);
        }catch(Exception ex){
            usuario.setIsLogged(false);
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el XML del User");
        }finally{

        }
        return hsCmp;
    }

    /**
     * Obtiene los datos que se ingresaran al XML de Menu, segun el usuario.
     * Se utiliza como parametro de entrada el campo clavePerfil, desde el
     * Bean User.
     * En ObjectData se coloca un objeto Bean del tipo User, con los datos que
     * se obtuvieron.
     * @param usuario   Bean que contiene los datos del usuario
     * @return
     */
    public HashCampo getMenuXML(User usuario) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            String[] strData = new String[1];
            ConQuery connQ = new ConQuery();
            strData[0] = usuario.getClavePerfil().toString();
            hsCmp = connQ.getData(getIdQuery(AdminFile.XMLMENU), strData);
            hsCmp.setObjData(usuario);
        }catch(Exception ex){
            usuario.setIsLogged(false);
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el XML del MENU");
        }finally{

        }
        return hsCmp;
    }

    /**
     * Entrega un objeto con la data y los campos que resultan de ejecutar la
     * query del ID entregado, junto con los parametros respectivos
     * @param IdQuery   ID de la query que se quiere ejecutar
     * @param strData   Arreglo con la data de entrada que se debe usar en la
     * Query   
     * @return
     */
    public HashCampo getDataByIdQuery(Integer IdQuery, String[] strData ) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            ConQuery connQ = new ConQuery();
            hsCmp = connQ.getData(IdQuery, strData);
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener la DATA con ID QUERY");
        }finally{

        }
        return hsCmp;
    }

    public boolean insertUser(User user){
        return true;
    }

    public boolean deleteUser(User user){
        return true;
    }
}
