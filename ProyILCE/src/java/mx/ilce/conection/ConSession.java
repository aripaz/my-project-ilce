/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.conection;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import mx.ilce.bean.HashCampo;
import mx.ilce.bean.User;
import mx.ilce.component.ListHash;
import mx.ilce.controller.Aplicacion;
import mx.ilce.controller.Perfil;

/**
 *  Clase para la implementacion de los metodos de conexion y obtencion de datos
 * asociados a la Session
 * @author ccatrilef
 */
public class ConSession {

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
    public User getUser(String user, String password) throws SQLException{
        User usr = new User();
        try{
            String[] strData = new String[2];
            strData[0] = user;
            strData[1] = password;

            ConQuery connQ = new ConQuery();
            //validamos user y password
            HashCampo hsCmp = connQ.getData(4, strData);
            if (hsCmp.getListData().isEmpty()){
                usr.setIsLogged(false);
                String[] datUser = new String[1];
                datUser[0]=user;
                //validamos que es problema de la password
                HashCampo hsCmpUsr = connQ.getData(5, datUser );
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
            ex.printStackTrace();
            usr.setIsLogged(false);
        }finally{
            
        }
        return usr;
    }

    /**
     * Obtiene los datos de Usuario, desde el bean user, desde el se usan el
     * login y password, si no corresponde la dupla entregada, se hace una
     * segunda validacion para ver si al menos existe el usuario y solo hubo
     * error en la password
     * @param usuario
     * @return
     * @throws SQLException
     */
    public User getUser(User usuario) throws SQLException{
        User usr = new User();
        try{
            //String user, String password
            String[] strData = new String[2];
            strData[0] = usuario.getLogin();
            strData[1] = usuario.getPassword();

            ConQuery connQ = new ConQuery();
            //validamos user y password
            HashCampo hsCmp = connQ.getData(4, strData);
            if (hsCmp.getListData().isEmpty()){
                usr.setIsLogged(false);
                String[] datUser = new String[1];
                datUser[0]=usuario.getNombre();
                //validamos que es problema de la password
                HashCampo hsCmpUsr = connQ.getData(5, datUser );
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
            ex.printStackTrace();
            usr.setIsLogged(false);
        }finally{

        }
        return usr;
    }

    /**
     * Obtiene los datos del perfil de un usuario, desde el bean User se utiliza
     * el campo clavePerfil como parametro de entrada. Ademas de los datos del
     * perfil, entrega el listado de aplicaciones que le corresponden segun su
     * perfil
     * @param user
     * @return
     * @throws SQLException
     */
    public Perfil getPerfil(User user) throws SQLException{
        Perfil perfil = new Perfil();
        try{
            String[] strData = new String[1];
            strData[0] = String.valueOf(user.getClavePerfil());

            ConQuery connQ = new ConQuery();
            HashCampo hsCmp = connQ.getData(8, strData);
            if (!hsCmp.getListData().isEmpty()){
                //introducimos en el Bean los datos obtenidos
                ListHash lst = new ListHash();
                perfil = (Perfil) lst.getBean(Perfil.class ,hsCmp);
                List lstApli = lst.getListBean(Aplicacion.class, hsCmp);
                perfil.setLstAplicacion(lstApli);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{

        }
        return perfil;
    }

    /**
     * Obtiene los datos para completar el XML de TAB que le corresponden
     * segun Perfil. Desde el perfil se toma el campo lstAplicacion para
     * obtener todos los tab segun los datos aplicacion y forma.
     * @param perfil
     * @return
     */
    public HashCampo getTabForma(Perfil perfil){
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
                    hsCmpAux = connQ.getData(9, strData);
                    if ((hsCmp.getLengthCampo()==0)&&(hsCmpAux!=null)) {
                        hsCmp.setListCampos(hsCmpAux.getListCampos());
                    }
                    hsCmp.addListToListData(hsCmpAux);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
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
     * @param usuario
     * @return
     */
    public HashCampo getUserXML(User usuario){
        HashCampo hsCmp = new HashCampo();
        try{
            String[] strData = new String[1];
            ConQuery connQ = new ConQuery();
            strData[0] = usuario.getClaveEmpleado().toString();
            hsCmp = connQ.getData(6, strData);
            hsCmp.setObjData(usuario);

        }catch(Exception ex){
            ex.printStackTrace();
            usuario.setIsLogged(false);
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
     * @param usuario
     * @return
     */
    public HashCampo getMenuXML(User usuario){
        HashCampo hsCmp = new HashCampo();
        try{
            String[] strData = new String[1];
            ConQuery connQ = new ConQuery();
            strData[0] = usuario.getClavePerfil().toString();
            hsCmp = connQ.getData(7, strData);
            hsCmp.setObjData(usuario);

        }catch(Exception ex){
            ex.printStackTrace();
            usuario.setIsLogged(false);
        }finally{

        }
        return hsCmp;
    }

    /**
     * Entrega un objeto con la data y los campos que resultan de ejecutar la
     * uery del ID entregado, junto con los parametros resdpectivos
     * @param IdQuery   ID de la query que se quiere ejecutar
     * @param strData[] Arreglo con la data de entrada que se debe usar en la
     * Query   
     * @return
     */
    public HashCampo getDataByIdQuery(Integer IdQuery, String[] strData ){
        HashCampo hsCmp = new HashCampo();
        try{
            ConQuery connQ = new ConQuery();
            hsCmp = connQ.getData(IdQuery, strData);

        }catch(Exception ex){
            ex.printStackTrace();
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
