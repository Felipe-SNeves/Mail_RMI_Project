package Classes;

import java.io.Serializable;

public final class Servidor implements Serializable{

    private String dominio;
    
    public Servidor(String d){
        this.setDominio(d);
    }
    
    public String getDominio(){
        return this.dominio;
    }
    
    public void setDominio(String d){
        this.dominio = d;
    }
}