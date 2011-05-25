package mx.ilce.component;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import mx.ilce.handler.ExceptionHandler;
import mx.ilce.util.UtilDate;

/**
 * Clase implementada para lograr control de la recuperacion de formularios
 * desde la capa Vista
 * @author ccatrilef
 */
public class AdminForm {

    /**
     * Entrega un HashMap que contiene los datos de un formulario, separados en
     * dos Hash: Datos (FORM) y Archivos (FILES) y dos ArrayList: con los nombres
     * de los campos del formulario (arrayFORM) y uno con los nombres de los
     * campos de archivo (arrayFILE). Para poder recuperar los Archivos se
     * requiere que el contentType del Form sea "multipart/form-data", sino es
     * de este tipo, no se envia el Hash de archivos y el campo correspondiente
     * al archivo se incluye dentro del Hash de FORM. La estructura de cada hash
     * contiene el nombre del campo como clave y el dato obtenido del formulario,
     * ambos en formato String. Cuando se envia el contentType correcto y existe
     * el archivo, se deja una copia del mismo, con un nombre nuevo en el servidor,
     * asociado al campo archivo, va el nuevo nombre y su ruta con que quedo en
     * el servidor.
     * @param request   Objeto de la Session que contiene los datos del formulario
     * @return
     */
    public HashMap getFormulario(HttpServletRequest request) throws ExceptionHandler{
        HashMap hs = null;
        try{
            String contentType = request.getContentType();
            if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0))
            {
                hs = getFormularioMultiPart(request);
            }else{
                hs = getFormularioSimple(request);
            }
        }catch(Exception e){
            throw new ExceptionHandler(e,this.getClass(),"Problemas para obtener el formulario");
        }
        return hs;
    }

    /**
     * Entrega un Hash con el contenido de un formulario, en el Hash de
     * datos (FORM) y un arreglo con los nombres de los campos de datos del
     * formulario (arrayFORM). Los datos asociados a archivos solo se entregan
     * como otro dato mas, no generandose el hash con los datos de archivo ni
     * realizandose la copia en el servidor
     * @param request   Objeto de la Session que contiene los datos del formulario
     * @return
     */
    private HashMap getFormularioSimple(HttpServletRequest request) throws ExceptionHandler{
        HashMap hs = null;
        HashMap hsForm = null;
        try{
            Enumeration enumData = request.getParameterNames();
            ArrayList arrayFORM = new ArrayList();
            if (enumData != null){
                hsForm = new HashMap();
                while (enumData.hasMoreElements()){
                    String strEnum = (String) enumData.nextElement();
                    String[] strData = request.getParameterValues(strEnum);
                    hsForm.put(strEnum, strData[0]);
                    arrayFORM.add(strEnum);
                }
                hs = new HashMap();
                hs.put("FORM", hsForm);
                hs.put("arrayFORM", arrayFORM);
            }
        }catch(Exception e){
            throw new ExceptionHandler(e,this.getClass(),"Problemas para obtener el formulario Simple");
        }
        return hs;

    }
    
    /**
     * Entrega un Hash con el contenido de un formulario, separados en el Hash
     * de datos (FORM), el de archivos (FILE), un arreglo con nombres campos de
     * datos del formulario (arrayFORM) y un arreglo con los nombres de los
     * campos de archivo (arrayFILE). Cuando los archivos que se indican
     * existen, se genera una copia en el servidor y se entrega en el Hash, el
     * nombre y ruta del archivo.
     * @param request   Objeto de la Session que contiene los datos del formulario
     * @return
     */
    private HashMap getFormularioMultiPart(HttpServletRequest request) throws ExceptionHandler{
        HashMap hs = null;
        HashMap hsForm = null;
        HashMap hsFile = null;
        try{
            AdminFile admF = new AdminFile();
            Properties prop = AdminFile.leerConfig();

            String FileServerPath = AdminFile.getKey(prop, AdminFile.FILESERVER);
            String maxSizeFile = AdminFile.getKey(prop, AdminFile.MAXSIZEFILE);
            int maxPostSize = Integer.valueOf(maxSizeFile);
            ArrayList arrayFORM = new ArrayList();
            ArrayList arrayFILE = new ArrayList();

            MultipartParser mp = new MultipartParser(request, maxPostSize,true,true);
            Part part;
            while ((part = mp.readNextPart()) != null) {
                if (hs==null){
                    hs = new HashMap();
                }
                String name = part.getName();
                if (part.isParam()) {
                    ParamPart paramPart = (ParamPart) part;
                    String value = paramPart.getStringValue();
                    if (hsForm==null){
                        hsForm = new HashMap();
                    }
                    hsForm.put(name, value);
                    arrayFORM.add(name);
                }else if (part.isFile()) {
                    FilePart filePart = (FilePart) part;
                    String fileName = filePart.getFileName();
                    if (fileName != null) {
                        if (hsFile==null){
                            hsFile = new HashMap();
                        }
                        UtilDate ud = new UtilDate();
                        String strDia = ud.getFechaHMS(UtilDate.formato.AMD,"_");
                        strDia = strDia.replaceAll(":",".");
                        String dirName = FileServerPath + fileName+"."+strDia;
                        File dir = new File(dirName);
                        long size = filePart.writeTo(dir);
                        if (size>0){
                            hsFile.put(name, dirName);
                            arrayFILE.add(name);
                        }
                    }
                }
            }
            hs.put("FORM", hsForm);
            hs.put("FILE", hsFile);
            hs.put("arrayFORM", arrayFORM);
            hs.put("arrayFILE", arrayFILE);
        }catch(Exception e){
            throw new ExceptionHandler(e,this.getClass(),"Problemas para obtener el formulario multipart");
        }
        return hs;
    }

    /**
     * Metodo que entrega los datos de una variable. Primero los busca como
     * atributo de la session, si no lo encuentra, lo busca como parametro de
     * un formulario
     * @param name  Nombre de la variable a buscar
     * @param request   Objeto de la Session que contiene los datos del formulario
     * @return
     */
    public String getStringRequest(String name, HttpServletRequest request) throws ExceptionHandler{
        String data = null;
        try{
            data = (String) request.getSession().getAttribute(name);
            if (data==null){
                data = (String) request.getParameter(name);
            }
        }catch(Exception e){
            throw new ExceptionHandler(e,this.getClass(),"Problemas para obtener los datos de un campo en el request");
        }
        return data;
    }
}
