package Interfaces_Remotas;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Servicos_DNS extends Remote{
   
    public void adicionarEndereco(String servico, String endereco) throws RemoteException;
    public String consulta(String servico) throws RemoteException;
}