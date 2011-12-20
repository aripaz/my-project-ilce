/**
 * Desarrollado para ILCE (Instituto Latinoamericano de la Comunicación
 * Educativa) bajo el contexto del Proyecto de Migración de la Aplicación SAEP,
 * desde un esquema .NET a Java.
 * Marzo-Diciembre 2011
 * Autor: Carlos Leonel Catrilef Cea
 * Version: 1.0
 *
 * - Las licencias de los componentes y librerías utilizadas, están adjuntas en
 * el(los) archivo(s) LICENCE que corresponda(n), junto al código fuente de la
 * aplicación, tal como establecen para el uso no comercial de las mismas.
 * - Todos los elementos de la aplicación: Componentes, Módulos, Bean, Clases, etc,
 * se entienden revisadas y aprobadas solamente para esta aplicación.
 * - Sobre condiciones de uso, reproducción y distribución referirse al archivo
 * LICENCE-ILCE incluido en la raiz del proyecto.
 */
package mx.ilce.handler;

/**
 *  Clase implementada para hacer manejo del ingreso al realizar el login
 * @author ccatrilef
 */
public class LoginHandler extends ExecutionHandler{

    private boolean isLogin;

    /**
     * Obtiene el estado de Logged del User: TRUE lo esta, FALSE no lo esta
     * @return
     */
    public boolean isLogin() {
        return isLogin;
    }

    /**
     * Asigna el estado de Logged del User:  TRUE lo esta, FALSE no lo esta
     * @param isLogin   Estado que tendra el usuario.
     */
    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }    
}
