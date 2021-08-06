package DAO;

import Interfaces_Remotas.Servicos_mensagem;
import java.sql.*;
import Conexao.Conexao;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

public final class MensagemDAO extends UnicastRemoteObject implements Servicos_mensagem{
    
    private Connection conexao;
    
    public MensagemDAO() throws RemoteException{
        super();
        try{
            this.conexao = Conexao.getConnection();
        }
        catch(Exception e){
            System.out.println("Erro ao brir o banco de dados: " + e.getMessage());
        }
    }
    
    public static void main(String[] args){
        
        try{
            MensagemDAO mensagem_remota = new MensagemDAO();
            String endereco = "//localhost/servico_mensagem";
            Naming.rebind(endereco, mensagem_remota);    
        }
        catch(RemoteException e){
            System.out.println("Erro: " + e.getMessage());
        }
        catch(MalformedURLException ex){
            System.out.println("Erro de url: " + ex.getMessage());
        }
    }
    
    @Override
    public boolean enviar(String remetente, String destinatario, String assunto, String corpo){
        
        String remetente_dominio = "", remetente_email = "", destinatario_dominio = "", destinatario_email = "";
        boolean estado = false;
        
        for(int i = 0; i < remetente.length(); i++)
        {
            if(remetente.charAt(i)=='@')
                estado = true;
            
            if(estado)
                remetente_dominio += remetente.charAt(i);
            else
                remetente_email += remetente.charAt(i);
        }
        
        estado = false;
        
        for(int i = 0; i < destinatario.length(); i++)
        {
            if(destinatario.charAt(i)=='@')
                estado = true;
            
            if(estado)
                destinatario_dominio += destinatario.charAt(i);
            else
                destinatario_email += destinatario.charAt(i);
        }      
 
        boolean resultado_final = false;
        
        try{
            PreparedStatement st = this.conexao.prepareStatement(
                    "SELECT s.dominio FROM servidor s WHERE dominio LIKE ?"
            );
            
            st.setString(1,destinatario_dominio);
            ResultSet resultado = st.executeQuery();
            
            // O servidor existe
            if(resultado.next()){
                String query = "SELECT u.email FROM usuario u INNER JOIN servidor s ON (s.dominio = u.dominio) WHERE u.email LIKE ? AND s.dominio LIKE ?";
                
                st = conexao.prepareStatement(query);
                st.setString(1,destinatario_email);
                st.setString(2,destinatario_dominio);
                resultado = st.executeQuery();
                
                // O email de destino existe
                if(resultado.next()){
                    query = "INSERT INTO mensagem(remetente_dominio, remetente_email, assunto, corpo, destinatario_dominio, destinatario_email) VALUES(?,?,?,?,?,?)";
                    st = conexao.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    st.setString(1,remetente_dominio);
                    st.setString(2,remetente_email);
                    st.setString(3,assunto);
                    st.setString(4,corpo);
                    st.setString(5,destinatario_dominio);
                    st.setString(6,destinatario_email);
                    st.execute();
                    resultado = st.getGeneratedKeys();
                    if(resultado.next())
                        resultado_final = true; // Email enviado
                }
                else
                    resultado_final = false; // Email destinatario nao existe
            }
            else
                resultado_final = false; // O servidor de destino nao existe
            
            resultado.close();
            st.close();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
        return resultado_final;
    }
    
    @Override
    public void excluir(String Dominio, String Email, String DominioDestino, String EmailDestino, String Corpo){
        
        try{
            PreparedStatement st = this.conexao.prepareStatement("DELETE FROM mensagem WHERE "
                    + "remetente_dominio LIKE ? AND remetente_email LIKE ? AND "
                    + "destinatario_dominio LIKE ? AND destinatario_email LIKE ? AND "
                    + "corpo = ?");
            
            st.setString(1, Dominio);
            st.setString(2, Email);
            st.setString(3, DominioDestino);
            st.setString(4, EmailDestino);
            st.setString(5, Corpo);
            
            st.execute();
            st.close();
            
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public List<String> Listar(String email) throws RemoteException{
        try{
            PreparedStatement st = this.conexao.prepareStatement("SELECT assunto FROM mensagem "
                    + " WHERE remetente_email LIKE ? OR destinatario_email LIKE ?");
            st.setString(1, email);
            st.setString(2, email);
            ResultSet resultado = st.executeQuery();
            
            List<String> assuntos_mensagens = new ArrayList<String>();
            
            while(resultado.next())
                assuntos_mensagens.add(resultado.getString("assunto"));
            
            resultado.close();
            st.close();
            
            return assuntos_mensagens;
            
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public String Capturar(String assunto) throws RemoteException{
        
        String remetente, destinatario, assun, corpo;
        
        try{
            PreparedStatement st = this.conexao.prepareStatement("SELECT remetente_email, "
                    + " remetente_dominio, destinatario_email, destinatario_dominio, assunto, corpo "
                    + " FROM mensagem WHERE assunto LIKE ? ");
            
            st.setString(1,assunto);
            ResultSet resultado = st.executeQuery();
            resultado.next();
            
            remetente = resultado.getString("remetente_email") + resultado.getString("remetente_dominio");
            destinatario = resultado.getString("destinatario_email") + resultado.getString("destinatario_dominio");
            assun = resultado.getString("assunto");
            corpo = resultado.getString("corpo");
            
            resultado.close();
            st.close();
            
            return remetente + "|" + destinatario + "|" + assun + "|" + corpo;
            
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    
}