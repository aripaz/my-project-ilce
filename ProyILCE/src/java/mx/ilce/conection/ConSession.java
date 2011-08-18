package mx.ilce.conection;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import mx.ilce.bean.HashCampo;
import mx.ilce.bean.User;
import mx.ilce.bitacora.AdmBitacora;
import mx.ilce.bitacora.Bitacora;
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
    private Bitacora bitacora;

    /**
     * Obtiene el objeto bitacora
     * @return
     */
    public Bitacora getBitacora() {
        return bitacora;
    }

    /**
     * Asigna el objeto bitacora
     * @param bitacora
     */
    public void setBitacora(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

    /**
     * Constructor basico de la clase, al crearse se cargan los datos del
     * properties
     * @throws ExceptionHandler
     */
    public ConSession() throws ExceptionHandler{
        try{
            this.prop = AdminFile.leerIdQuery();
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para abrir Conexion ConSession");
        }
    }

     /**
     * Obtiene el IDQUERY desde el properties de queries
     * @param key   Clave que se esta buscando
     * @return
     * @throws ExceptionHandler
     */
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
     * @throws ExceptionHandler
     */
    public User getUser(String user, String password, String[][] arrVariables) throws ExceptionHandler{
        User usr = new User();
        try{
            String[] strData = new String[2];
            strData[0] = user;
            strData[1] = password;
            boolean enableBit = false;
            
            if (this.getBitacora()!=null){
                enableBit = this.getBitacora().isEnable();
            }
            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());

            //validamos user y password
            connQ.setEnableDataLog(false);
            HashCampo hsCmp = connQ.getData(getIdQuery(AdminFile.LOGIN), strData, arrVariables);
            if (hsCmp.getListData().isEmpty()){
                usr.setIsLogged(false);
                String[] datUser = new String[1];
                datUser[0]=user;
                //validamos que es problema de la password
                HashCampo hsCmpUsr = connQ.getData(getIdQuery(AdminFile.USER), datUser, arrVariables );
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

                this.getBitacora().setClaveEmpleado(usr.getClaveEmpleado());
                usr.setBitacora(this.getBitacora());
                if (enableBit){
                    AdmBitacora admBit = new AdmBitacora();
                    admBit.setBitacora(this.getBitacora());
                    admBit.login();
                }
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
     * @throws ExceptionHandler
     */
    public User getUser(User usuario, String[][] arrVariables) throws ExceptionHandler{
        User usr = new User();
        try{
            String[] strData = new String[2];
            strData[0] = usuario.getLogin();
            strData[1] = usuario.getPassword();
            boolean enableBit = false;

            if (this.getBitacora()!=null){
                enableBit = this.getBitacora().isEnable();
            }

            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());

            //validamos user y password
            connQ.setEnableDataLog(false);
            HashCampo hsCmp = connQ.getData(getIdQuery(AdminFile.LOGIN), strData, arrVariables);
            if (hsCmp.getListData().isEmpty()){
                usr.setIsLogged(false);
                String[] datUser = new String[1];
                datUser[0]=usuario.getNombre();
                //validamos que es problema de la password
                HashCampo hsCmpUsr = connQ.getData(getIdQuery(AdminFile.USER), datUser, arrVariables );
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

                this.getBitacora().setClaveEmpleado(usr.getClaveEmpleado());
                usr.setBitacora(this.getBitacora());
                if (enableBit){
                    AdmBitacora admBit = new AdmBitacora();
                    admBit.setBitacora(this.getBitacora());
                    admBit.login();
                }
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
     * @throws ExceptionHandler
     */
    public Perfil getPerfil(User user, String[][] arrVariables) throws ExceptionHandler{
        Perfil perfil = new Perfil();
        try{
            String[] strData = new String[1];
            strData[0] = String.valueOf(user.getClavePerfil());

            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());

            HashCampo hsCmp = connQ.getData(getIdQuery(AdminFile.PERFIL), strData, arrVariables);

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
     * @throws ExceptionHandler
     */
    public HashCampo getTabForma(Perfil perfil, String[][] arrVariables) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            List lstApl = perfil.getLstAplicacion();

            if ((lstApl != null)&&(!lstApl.isEmpty())){
                Iterator it = lstApl.iterator();
                String[] strData = new String[4];
                ConQuery connQ = new ConQuery();
                connQ.setBitacora(this.getBitacora());

                HashCampo hsCmpAux = null;
                Integer lenList = hsCmp.getLengthData();
                while (it.hasNext()){
                    Aplicacion apl = (Aplicacion) it.next();
                    strData[0]= String.valueOf(apl.getClaveAplicacion());
                    strData[1]= String.valueOf(apl.getClaveFormaPrincipal());
                    strData[2]= String.valueOf(apl.getClaveAplicacion());
                    strData[3]= String.valueOf(apl.getClaveFormaPrincipal());
                    hsCmpAux = connQ.getData(getIdQuery(AdminFile.TABFORMA), strData, arrVariables);
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
     * Bean User.En ObjectData se coloca un objeto Bean del tipo User, con los
     * datos que se obtuvieron.
     * @param usuario   Bean que contiene los datos del usuario
     * @return
      * @throws ExceptionHandler
      */
    public HashCampo getUserXML(User usuario, String[][] arrVariables) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            String[] strData = new String[1];
            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());

            strData[0] = usuario.getClaveEmpleado().toString();
            hsCmp = connQ.getData(getIdQuery(AdminFile.XMLSESSION), strData, arrVariables);
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
     * Bean User. En ObjectData se coloca un objeto Bean del tipo User, con los
     * datos que se obtuvieron.
     * @param usuario   Bean que contiene los datos del usuario
     * @return
     * @throws ExceptionHandler
     */
    public HashCampo getMenuXML(User usuario, String[][] arrVariables) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            String[] strData = new String[1];
            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());

            strData[0] = usuario.getClavePerfil().toString();
            hsCmp = connQ.getData(getIdQuery(AdminFile.XMLMENU), strData, arrVariables);
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
     * @throws ExceptionHandler
     */
    public HashCampo getDataByIdQuery(Integer IdQuery, String[] strData, String[][] arrVariables ) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());

            hsCmp = connQ.getData(IdQuery, strData, arrVariables);
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener la DATA con ID QUERY");
        }finally{

        }
        return hsCmp;
    }

    /**
     * NO IMPLEMENTADO
     */
    public boolean insertUser(User user){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * NO IMPLEMENTADO
     */
    public boolean deleteUser(User user){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
