package servidor.componentes;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import servidor.interfaces.PartRepository;
import servidor.interfaces.Part;

import java.util.Collection;
import java.util.Map;

import javax.swing.JOptionPane;

public class PartRepositoryEngine implements PartRepository{
    
    //Mapa de partes
    Map<String, Part> PartsList;
    
    //Próximo ID de Parte
    int nextPartId;
    
    //Nome do repositório
    String repName;
    
    public PartRepositoryEngine()
    {
        super();
    }
    
    public Collection<Part> getParts()
    {
        return this.PartsList.values();
    }
    
    public void addPart(Part p)
    {
        PartsList.put(Integer.toString(nextPartId),p);
        
        nextPartId++;
    }
    
    public Part getPartById(String id)
    {
        return PartsList.get(id);
    }
    
    public static void main(String[] args)
    {
        if (System.getSecurityManager() == null) 
        {
            System.setSecurityManager(new SecurityManager());
        }
        
        try 
        {
            //Pedir o nome do repositório
            String name = JOptionPane.showInputDialog("Digite o nome deste repositório");
            
            PartRepository repository = new PartRepositoryEngine();
            PartRepository stub = (PartRepository) UnicastRemoteObject.exportObject(repository, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            
            System.out.println("PartRepository bound");
        }
        catch (Exception e) 
        {
            System.err.println("PartRepository exception:");
            e.printStackTrace();
        }
    }
    
}
