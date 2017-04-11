package servidor.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface PartRepository extends Remote{
    
    public Collection<Part> getParts() throws RemoteException;
    public void addPart(Part p) throws RemoteException;
    public Part getPartById(String id) throws RemoteException;
    
}
