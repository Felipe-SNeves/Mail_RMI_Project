package Classes;

import java.io.Serializable;

public final class Usuario implements Serializable{
    
    private String nome, senha, email, dominio;
    
    public Usuario(String n, String s, String e, String d){
        this.setNome(n);
        this.setSenha(s);
        this.setEmail(e);
        this.setDominio(d);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDominio(){
        return this.dominio;
    }
    
    public void setDominio(String d){
        this.dominio = d;
    }
    
    public String getEmailCompleto(){
        return this.getEmail() + this.getDominio();
    }

    @Override
    public String toString() {
        return "Usuario{" + "nome=" + nome + ", senha=" + senha + ", email=" + email + ", dominio=" + dominio + '}';
    }
    
}