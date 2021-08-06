package Interfaces_Remotas;

import java.rmi.Remote;
import java.rmi.RemoteException;
import Classes.Usuario;

public interface Servicos_usuario extends Remote{
    
    public boolean adiciona(Usuario u) throws RemoteException;
    public void excluir(String email, String dominio) throws RemoteException;
    public Usuario Autenticacao(String a, String b) throws RemoteException;   
}