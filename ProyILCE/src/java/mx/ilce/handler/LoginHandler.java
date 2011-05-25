package mx.ilce.handler;

/**
 *  Clase implementada para hacer manejo del ingreso al realizar el login
 * @author ccatrilef
 */
public class LoginHandler extends ExecutionHandler{

    private boolean isLogin;

    /**
     * Obtiene el estado de logging del User: TRUE lo esta, FALSE no lo esta
     * @return
     */
    public boolean isLogin() {
        return isLogin;
    }

    /**
     * Asigna el estado de logging del User:  TRUE lo esta, FALSE no lo esta
     * @param isLogin   Estado que tendra el usuario.
     */
    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }    
}
