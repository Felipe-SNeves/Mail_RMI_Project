package DAO;

import java.sql.*;
import Conexao.Conexao;
import Classes.Servidor;
import java.util.List;
import java.util.ArrayList;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import Interfaces_Remotas.Servicos_servidor;
import java.net.MalformedURLException;
import java.rmi.Naming;

public final class ServidorDAO extends UnicastRemoteObject implements Servicos_servidor{
    
    private Connection conexao;
    
    public ServidorDAO() throws RemoteException{
        super();
        try{
            this.conexao = Conexao.getConnection();
        }
        catch(Exception e){
            System.out.println("Erro ao abrir o banco de dados: " + e.getMessage());
        }
    }
    
    public static void main(String[] args){
        
        try {
            ServidorDAO server = new ServidorDAO();
            
            String endereco = "//localhost/servico_servidor";
            Naming.rebind(endereco, server);     
        }
        catch(RemoteException e){
            System.out.println("Erro: " + e.getMessage());
        }
        catch(MalformedURLException ex){
            System.out.println("Erro de url: " + ex.getMessage());
        }
    }
    
    public boolean adiciona(Servidor s){
        
        boolean resultado_final = false;
        
        try{
            PreparedStatement st = this.conexao.prepareStatement(
                    "SELECT dominio FROM servidor WHERE dominio LIKE ?;"
            );
            
            st.setString(1, s.getDominio());
            ResultSet resultado = st.executeQuery();
            
            if(!resultado.next()){
                String query = "INSERT INTO servidor(dominio) VALUES(?);";
                st = conexao.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                st.setString(1, s.getDominio());
                st.execute();
                resultado = st.getGeneratedKeys();
                if(resultado.next())
                    resultado_final = true;
                
            }
            else
                resultado_final = false;
            
            resultado.close();
            st.close();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
        return resultado_final;
    }
    
    @Override
    public List<String> Listar() throws RemoteException{
        try{
            PreparedStatement st = this.conexao.prepareStatement("SELECT dominio FROM servidor");
            ResultSet resultado = st.executeQuery();
            
            List<String> servidores_disponiveis = new ArrayList<String>();
            
            while(resultado.next())
            {
                Servidor server = new Servidor(resultado.getString("dominio"));
                System.out.println();
                
                servidores_disponiveis.add(server.getDominio());
            }
            
            resultado.close();
            st.close();
            
            return servidores_disponiveis;
            
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}