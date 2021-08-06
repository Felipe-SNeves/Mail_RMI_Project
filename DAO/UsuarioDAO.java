package DAO;

import Interfaces_Remotas.Servicos_usuario;
import java.sql.*;
import Conexao.Conexao;
import Classes.Usuario;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public final class UsuarioDAO extends UnicastRemoteObject implements Servicos_usuario{
    
    private Connection conexao;
    
    public UsuarioDAO() throws RemoteException{
        super();
        try{
            this.conexao = Conexao.getConnection();
        }
        catch(Exception e){
            System.out.println("Erro ao abrir o banco de dados: " + e.getMessage());
        }
    }
    
    public static void main(String[] args){
        
        try{
            UsuarioDAO usuario_remoto = new UsuarioDAO();
            String endereco = "//localhost/servico_usuario";
            Naming.rebind(endereco, usuario_remoto);
        }
        catch(RemoteException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        catch(MalformedURLException ex){
            System.out.println("Erro de url: " + ex.getMessage());
        }
        
        
    }
    
    @Override
    public boolean adiciona(Usuario u){
        
        boolean resultado_final = false;   
        
        try{            
            PreparedStatement st = this.conexao.prepareStatement(
                    "SELECT s.dominio FROM servidor s WHERE dominio LIKE ?"
            );
            
            st.setString(1,u.getDominio());
            ResultSet resultado = st.executeQuery();
            
            // O servidor existe
            if(resultado.next()){
                String query = "SELECT u.email FROM usuario u INNER JOIN servidor s ON (s.dominio = u.dominio) WHERE email LIKE ? AND s.dominio LIKE ?";             
                st = conexao.prepareStatement(query);
                st.setString(1,u.getEmail());
                st.setString(2,u.getDominio());
                resultado = st.executeQuery();
            
                if(!resultado.next()){
                    query = "INSERT INTO usuario(nome, senha, email, dominio) VALUES (?,?,?,?)";
                    st = conexao.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    st.setString(1,u.getNome());
                    st.setString(2,u.getSenha());
                    st.setString(3,u.getEmail());
                    st.setString(4,u.getDominio());
                    st.execute();
                    resultado = st.getGeneratedKeys();
                    resultado_final = true;
                    if(resultado.next())
                        resultado_final = true;
                }
                else
                    resultado_final = false; // O usuario já existe
            }
            else
                resultado_final = false; // O servidor não existe
            
            resultado.close();
            st.close();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
        return resultado_final;
    }
    
    @Override
    public void excluir(String email, String dominio){
        try{
            PreparedStatement st = this.conexao.prepareStatement("DELETE FROM usuario WHERE "
                    + " email LIKE ? AND dominio LIKE ?");
            
            st.setString(1,email);
            st.setString(2,dominio);
            st.execute();
            st.close();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Usuario Autenticacao(String email_completo, String senha){
        
        String  dominio = "", email_tratado = "";
        boolean estado = false;
        
        for(int i = 0; i < email_completo.length(); i++)
        {
            if(email_completo.charAt(i)=='@')
                estado = true;
            
            if(estado)
                dominio += email_completo.charAt(i);
            else
                email_tratado += email_completo.charAt(i);
        }
        
        String query = "SELECT * FROM usuario WHERE email LIKE ? AND senha LIKE ? AND dominio LIKE ?";
        
        try{
            PreparedStatement st = this.conexao.prepareStatement(query);
            st.setString(1,email_tratado);
            st.setString(2,senha);
            st.setString(3,dominio);
            ResultSet resultado = st.executeQuery(); 
            resultado.last();
            
            if(resultado.getRow()==0)
                return null;
            
            Usuario u = new Usuario(resultado.getString("nome"),resultado.getString("senha"),resultado.getString("email"),resultado.getString("dominio"));
            
            resultado.close();
            st.close();
            
            return u;
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}