package mx.ilce.conection;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import mx.ilce.bean.CampoForma;
import mx.ilce.bean.DataTransfer;
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
    private Bitacora bitacora;

    /**
     * Obtiene el objeto bitacora
     * @return  Bitacora    Objeto Bitacora
     */
    public Bitacora getBitacora() {
        return bitacora;
    }

    /**
     * Asigna el objeto bitacora
     * @param bitacora  Objeto Bitacora
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
     * @return  Integer     ID de la query
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
     * @return  User    Objeto User con los datos del usuario
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
            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer.setIdQuery(getIdQuery(AdminFile.LOGIN));
            dataTransfer.setArrData(strData);
            dataTransfer.setArrVariables(arrVariables);

            HashCampo hsCmp = connQ.getData(dataTransfer);

            if (hsCmp.getListData().isEmpty()){
                usr.setIsLogged(false);
                String[] datUser = new String[1];
                datUser[0]=user;
                //validamos que es problema de la password
                dataTransfer = new DataTransfer();
                dataTransfer.setIdQuery(getIdQuery(AdminFile.USER));
                dataTransfer.setArrData(datUser);
                dataTransfer.setArrVariables(arrVariables);

                HashCampo hsCmpUsr = connQ.getData(dataTransfer);
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
        }
        return usr;
    }

    /**
     * Obtiene los datos de Usuario, desde el bean user, desde el se usan el
     * login y password, si no corresponde la dupla entregada, se hace una
     * segunda validacion para ver si al menos existe el usuario y solo hubo
     * error en la password
     * @param usuario   Bean que contiene los datos del usuario
     * @return  User    Objeto User con los datos del usuario
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
            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer.setIdQuery(getIdQuery(AdminFile.LOGIN));
            dataTransfer.setArrData(strData);
            dataTransfer.setArrVariables(arrVariables);

            HashCampo hsCmp = connQ.getData(dataTransfer);
            if (hsCmp.getListData().isEmpty()){
                usr.setIsLogged(false);
                String[] datUser = new String[1];
                datUser[0]=usuario.getNombre();
                //validamos que es problema de la password
                dataTransfer = new DataTransfer();
                dataTransfer.setIdQuery(getIdQuery(AdminFile.USER));
                dataTransfer.setArrData(strData);
                dataTransfer.setArrVariables(arrVariables);
                
                HashCampo hsCmpUsr = connQ.getData(dataTransfer);

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
        }
        return usr;
    }

    /**
     * Metodo implementado para la obtencion de los datos del usuario por su mail
     * @param dataTrans
     * @return  User    Objeto User con los datos del usuario
     * @throws ExceptionHandler
     */
    public User getUserByMail(DataTransfer dataTrans) throws ExceptionHandler{
        User usuario = (User) dataTrans.getDataObject();
        String[][] arrVariables = dataTrans.getArrVariables();

        User usr = new User();
        try{
            String[] strData = new String[1];
            strData[0] = usuario.getEmail();
            boolean enableBit = false;
            if (this.getBitacora()!=null){
                enableBit = this.getBitacora().isEnable();
            }
            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());
            //validamos user y password
            connQ.setEnableDataLog(false);
            DataTransfer dataTransfer = new DataTransfer();

            usr.setIsLogged(false);
            //validamos que es problema de la password
            dataTransfer = new DataTransfer();
            dataTransfer.setIdQuery(getIdQuery(AdminFile.USER));
            dataTransfer.setArrData(strData);
            dataTransfer.setArrVariables(arrVariables);

            HashCampo hsCmpUsr = connQ.getData(dataTransfer);

            if (hsCmpUsr.getListData().isEmpty()){
                usr.setMessage("Usuario no existe en los registros");
            }else{
                ListHash lh = new ListHash();
                usr = (User) lh.getBean(User.class, hsCmpUsr);
            }

        }catch(Exception ex){
            usr.setIsLogged(false);
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el USER");
        }
        return usr;
    }

    /**
     * Obtiene los datos del perfil de un usuario, desde el bean User se utiliza
     * el campo clavePerfil como parametro de entrada. Ademas de los datos del
     * perfil, entrega el listado de aplicaciones que le corresponden segun su
     * perfil
     * @param user  Bean que contiene los datos del usuario
     * @return  Perfil  Objeto Perfil con los datos del perfil solicitado
     * @throws ExceptionHandler
     */
    public Perfil getPerfil(User user, String[][] arrVariables) throws ExceptionHandler{
        Perfil perfil = new Perfil();
        try{
            String[] strData = new String[1];
            strData[0] = String.valueOf(user.getClaveEmpleado());

            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());
            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer.setIdQuery(getIdQuery(AdminFile.PERFIL));
            dataTransfer.setArrData(strData);
            dataTransfer.setArrVariables(arrVariables);

            HashCampo hsCmp = connQ.getData(dataTransfer);

            if (!hsCmp.getListData().isEmpty()){
                //introducimos en el Bean los datos obtenidos
                ListHash lst = new ListHash();
                perfil = (Perfil) lst.getBean(Perfil.class ,hsCmp);
                List lstApli = lst.getListBean(Aplicacion.class, hsCmp);
                perfil.setLstAplicacion(lstApli);
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el PERFIL");
        }
        return perfil;
    }

    /**
     * Obtiene los datos para completar el XML de TAB que le corresponden
     * segun Perfil. Desde el perfil se toma el campo lstAplicacion para
     * obtener todos los tab segun los datos aplicacion y forma.
     * @param perfil    Bean que contiene los datos del perfil
     * @return  HashCampo   Objeto Hash con los datos de los TAB de la forma
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

                while (it.hasNext()){
                    Aplicacion apl = (Aplicacion) it.next();
                    strData[0]= String.valueOf(apl.getClaveAplicacion());
                    strData[1]= String.valueOf(apl.getClaveFormaPrincipal());
                    strData[2]= String.valueOf(apl.getClaveAplicacion());
                    strData[3]= String.valueOf(apl.getClaveFormaPrincipal());

                    DataTransfer dataTransfer = new DataTransfer();
                    dataTransfer.setIdQuery(getIdQuery(AdminFile.TABFORMA));
                    dataTransfer.setArrData(strData);
                    dataTransfer.setArrVariables(arrVariables);

                    hsCmpAux = connQ.getData(dataTransfer);
                    if ((hsCmp.getLengthCampo()==0)&&(hsCmpAux!=null)) {
                        hsCmp.setListCampos(hsCmpAux.getListCampos());
                    }
                    hsCmp.addListToListData(hsCmpAux);
                }
            }
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el PERFIL");
        }
        return hsCmp;
    }

    /**
     * Obtiene los datos que se ingresaran al XML de Session, segun el usuario.
     * Se utiliza como parametro de entrada el campo claveEmpleado, desde el
     * Bean User.En ObjectData se coloca un objeto Bean del tipo User, con los
     * datos que se obtuvieron.
     * @param usuario   Bean que contiene los datos del usuario
     * @return  HashCampo   Objeto Hash con los datos para el XML de Usuario
      * @throws ExceptionHandler
      */
    public HashCampo getUserXML(User usuario, String[][] arrVariables) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            String[] strData = new String[1];
            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());

            strData[0] = usuario.getClaveEmpleado().toString();

            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer.setIdQuery(getIdQuery(AdminFile.XMLSESSION));
            dataTransfer.setArrData(strData);
            dataTransfer.setArrVariables(arrVariables);

            hsCmp = connQ.getData(dataTransfer);
            hsCmp.setObjData(usuario);
        }catch(Exception ex){
            usuario.setIsLogged(false);
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el XML del User");
        }
        return hsCmp;
    }

    /**
     * Obtiene los datos que se ingresaran al XML de Menu, segun el usuario.
     * Se utiliza como parametro de entrada el campo clavePerfil, desde el
     * Bean User. En ObjectData se coloca un objeto Bean del tipo User, con los
     * datos que se obtuvieron.
     * @param usuario   Bean que contiene los datos del usuario
     * @return  HashCampo   Objeto Hash con los datos del XML del Menu
     * @throws ExceptionHandler
     */
    public HashCampo getMenuXML(User usuario, String[][] arrVariables) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            String[] strData = new String[1];
            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());

            strData[0] = usuario.getClavePerfil().toString();

            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer.setIdQuery(getIdQuery(AdminFile.XMLMENU));
            dataTransfer.setArrData(strData);
            dataTransfer.setArrVariables(arrVariables);

            hsCmp = connQ.getData(dataTransfer);
            hsCmp.setObjData(usuario);
        }catch(Exception ex){
            usuario.setIsLogged(false);
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener el XML del MENU");
        }
        return hsCmp;
    }

    /**
     * Entrega un objeto con la data y los campos que resultan de ejecutar la
     * query del ID entregado, junto con los parametros respectivos
     * @param IdQuery   ID de la query que se quiere ejecutar
     * @param strData   Arreglo con la data de entrada que se debe usar en la
     * Query   
     * @return  HashCampo   Objeto Hash con los datos obtenidos desde la query
     * @throws ExceptionHandler
     */
    public HashCampo getDataByIdQuery(Integer IdQuery, String[] strData, String[][] arrVariables ) throws ExceptionHandler{
        HashCampo hsCmp = new HashCampo();
        try{
            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());

            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer.setIdQuery(IdQuery);
            dataTransfer.setArrData(strData);
            dataTransfer.setArrVariables(arrVariables);

            hsCmp = connQ.getData(dataTransfer);
        }catch(Exception ex){
            throw new ExceptionHandler(ex,this.getClass(),"Problemas para obtener la DATA con ID QUERY");
        }
        return hsCmp;
    }

    /**
     * Metodo implementado para la insercion del usuario
     * @param user
     * @return  User    Objeto User con los datos del nuevo Usuario
     * @throws ExceptionHandler
     */
    public User insertUser(User user) throws ExceptionHandler{
        User usr = new User();
        try{
            String[] strData = new String[1];
            strData[0] = user.getLogin();
            boolean enableBit = false;

            if (this.getBitacora()!=null){
                enableBit = this.getBitacora().isEnable();
            }

            ConQuery connQ = new ConQuery();
            connQ.setBitacora(this.getBitacora());

            //validamos user y password
            connQ.setEnableDataLog(false);
            usr.setIsLogged(false);

            DataTransfer dataTransfer = new DataTransfer();
            dataTransfer = new DataTransfer();
            dataTransfer.setIdQuery(getIdQuery(AdminFile.USER));
            dataTransfer.setArrData(strData);

            HashCampo hsCmpUsr = connQ.getData(dataTransfer);

            if (!hsCmpUsr.getListData().isEmpty()){
                usr.setMessage("Existe un usuario con ese mail en los registros");
            }else{
                String strQuery = "insert into empleado "
                        + "(nombre, apellido_paterno, apellido_materno,"
                        + "email, password, clave_perfil, clave_area, activo)"
                        + "values ('"+ user.getNombre() + "','"
                        + user.getApellidoPaterno() + "','"
                        + user.getApellidoMaterno() + "','"
                        + user.getEmail() + "','"
                        + user.getPassword() + "',"
                        + "0,"
                        + user.getClaveArea() + ",1)";
                
                dataTransfer.setQueryInsert(strQuery);
                CampoForma campoForma = new CampoForma();
                campoForma.setTabla("empleado");
                dataTransfer.setCampoForma(campoForma);

                HashCampo hs = connQ.executeInsert(dataTransfer);

                Integer intReg = (Integer) hs.getObjData();

                usr.setIsLogged(true);
                usr.setNombre(user.getNombre());
                usr.setApellidoPaterno(user.getApellidoPaterno());
                usr.setApellidoMaterno(user.getApellidoMaterno());
                usr.setClaveEmpleado(intReg);
                usr.setEmail(user.getEmail());
                usr.setPassword(user.getPassword());
                usr.setMessage("Usuario registrado correctamente");
                usr.setLogin(user.getEmail());

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
        }
        return usr;
    }

    /**
     * NO IMPLEMENTADO
     */
    public boolean deleteUser(User user){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
