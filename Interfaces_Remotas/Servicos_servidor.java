package Interfaces_Remotas;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Servicos_servidor extends Remote{
    
    public List<String> Listar() throws RemoteException;
}