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
package mx.ilce.report;

import java.io.Serializable;

/**
 * Clase para la contención de las secciones y sus configuraciones
 * @author ccatrilef
 */
class Section implements Serializable{

        private String tipo;
        private Object data;

        public Object getData() {
            return data;
        }
        public void setData(Object data) {
            this.data = data;
        }
        public String getTipo() {
            return tipo;
        }
        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

}
