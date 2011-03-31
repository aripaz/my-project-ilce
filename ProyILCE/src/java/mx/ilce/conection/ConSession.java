/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.ilce.conection;

import java.sql.SQLException;
import mx.ilce.bean.HashCampo;
import mx.ilce.bean.User;
import mx.ilce.component.ListHash;

/**
 *
 * @author ccatrilef
 */
public class ConSession {

    /**
     * Obtiene los datos de Usuario, mediante los parametros ingresados de
     * User y password, si no corresponde la dupla entregada, se hace una
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
     * Obetiene los datos de Usuario, mediante los parametros ingresados de
     * User y password, si no corresponde la dupla entregada, se hace una
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
            strData[0] = usuario.getNombre();
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

    public boolean insertUser(User user){
        return true;
    }

    public boolean deleteUser(User user){
        return true;
    }

}
