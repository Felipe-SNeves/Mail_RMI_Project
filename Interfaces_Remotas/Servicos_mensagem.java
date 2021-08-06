package Interfaces_Remotas;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Servicos_mensagem extends Remote{
    
    public boolean enviar(String remetente, String destinatario, String assunto, String corpo) throws RemoteException;
    public void excluir(String Dominio, String Email, String DominioDestino, String EmailDestino, String Corpo) throws RemoteException;
    public List<String> Listar(String email) throws RemoteException;
    public String Capturar(String assunto) throws RemoteException;
}