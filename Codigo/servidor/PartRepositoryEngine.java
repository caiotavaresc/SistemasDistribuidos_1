package servidor;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import interfaces.PartRepository;
import interfaces.Part;
import java.net.InetAddress;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class PartRepositoryEngine implements PartRepository{
    
    //Mapa de partes
    Map<Integer, Part> PartsList;
    
    //Próximo ID de Parte
    int nextPartId;
    
    //Nome do repositório
    String repName;
    
    public PartRepositoryEngine()
    {
        super();
        
        //O ID da próxima parte adicionada é 1
        nextPartId = 1;
        
        this.PartsList = new HashMap<Integer, Part>();
    }
    
    //Método que lista as peças do repositório
    public Collection<Part> getParts()
    {
        return this.PartsList.values();
    }
    
    //Método que adiciona uma nova peça
    public void addPart(String name, String descr, Map<Part, Integer> subParts) throws RemoteException
    {
        PartEngine p = new PartEngine(this.nextPartId, name, descr, subParts, this);
        Part stub = (Part) UnicastRemoteObject.exportObject(p, 0);
        
        this.PartsList.put(this.nextPartId, stub);
        
        this.nextPartId++;
    }
    
    //Método que busca uma peça pelo código no repositório
    public Part getPartById(int id)
    {
        return PartsList.get(id);
    }
    
    //Método que retorna o número de peças do servidor
    public int getNumPecas()
    {
        return this.PartsList.size();
    }
    
    //Método que retorna o nome do servidor
    public String getRepName()
    {
        return this.repName;
    }
    
    //Método que atribui um nome ao servidor
    public void setRepName(String name)
    {
        this.repName = name;
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
            String porta = JOptionPane.showInputDialog("Digite o número da porta onde o RMI Registry roda nesta máquina");
            
            int _porta;
            
            if(porta.equals(""))
                _porta = 1099;
            else
                _porta = Integer.valueOf(porta);
            
            PartRepository repository = new PartRepositoryEngine();
            repository.setRepName(name);
            
            //Setar as configuracoes de NOME DO HOST
            System.setProperty("java.rmi.server.hostname",InetAddress.getLocalHost().getHostName());
            
            PartRepository stub = (PartRepository) UnicastRemoteObject.exportObject(repository, 0);
            Registry registry = LocateRegistry.getRegistry(_porta);
            registry.rebind(name, stub);
            
            System.out.println("PartRepository bound");
        }
        catch (Exception e) 
        {
            System.err.println("PartRepository exception:");
            e.printStackTrace();
        }
    }
    
    public String teste()
    {
        return "Testando";
    }
    
}
