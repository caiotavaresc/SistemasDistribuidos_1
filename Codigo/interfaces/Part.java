package interfaces;

import java.util.Map;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Part extends Remote{
    
    public static final int PRIMITIVA = 0;
    public static final int AGREGADA = 1;
    
    public static final int SUB_DIRETOS = 0;
    public static final int SUB_PRIMITIVOS = 1;
    
    public int getId() throws RemoteException;
    
    public String getName() throws RemoteException;
    public void setName(String name) throws RemoteException;
    
    public String getDescr() throws RemoteException;
    public void setDescr(String descr) throws RemoteException;
    
    public Map<Part, Integer> getSubparts() throws RemoteException;
    
    public PartRepository getRepository() throws RemoteException;
    public String getRepName() throws RemoteException;
    public int getTipoPeca() throws RemoteException;
    public int getNumSubcomp(int tipo) throws RemoteException;
    
}
