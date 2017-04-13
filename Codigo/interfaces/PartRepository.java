package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import java.util.Map;

public interface PartRepository extends Remote{
    
    public Collection<Part> getParts() throws RemoteException;
    public void addPart(String name, String descr, Map<Part, Integer> subParts) throws RemoteException;
    public Part getPartById(int id) throws RemoteException;
    public int getNumPecas() throws RemoteException;
    public String getRepName() throws RemoteException;
    public void setRepName(String name) throws RemoteException;
    public String teste() throws RemoteException;
    
}
