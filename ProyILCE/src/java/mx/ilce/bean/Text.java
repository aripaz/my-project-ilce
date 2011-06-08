package mx.ilce.bean;

/**
 *  Clase implementada para dar equivalencia al tipo de data TEXT desde
 * la base de datos
 * @author ccatrilef
 */
public class Text{

    private String str = "";

    public Text(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public void clean(){
        setStr("");
    }

    public void concat(String data){
        if (data!=null){
            str = str.concat(data);
        }
    }

    public boolean equals(String data){
        return ((data==null)?false:str.equals(data));
    }

    public int indexOf(String data){
        return str.indexOf(data);
    }

    public void valueOf(String data){
        str = String.valueOf(data);
    }

    public void trim(){
        str = str.trim();
    }

    public String subString(int beginIndex, int endIndex){
        return str.substring(beginIndex, endIndex);
    }

    public String toUpperCase(){
        return str.toUpperCase();
    }

    public String toLowerCase(){
        return str.toLowerCase();
    }

    public int length(){
        return str.length();
    }

    public boolean isEmpty(){
        return ((str.length()==0)?true:str.isEmpty());
    }

    public String[] split(String data){
        return str.split(data);
    }

    public void replace(String oldData, String newData){
        str = str.replace(oldData, newData);
    }

    public void replaceFirst(String oldData, String newData){
        str = str.replaceFirst(oldData, newData);
    }

    public void replaceAll(String oldData, String newData){
        str = str.replaceAll(oldData, newData);
    }

    public String toString(){
        return str;
    }

}
