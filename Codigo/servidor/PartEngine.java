package servidor;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.io.Serializable;

import interfaces.Part;
import interfaces.PartRepository;
import java.rmi.RemoteException;

public class PartEngine implements Part, Serializable{
    
    int id;
    String name;
    String descr;
    Map<Part,Integer> subParts;
    PartRepository repository;
    
    //Criar um novo objeto de Part
    public PartEngine(int _id, String _name, String _descr, Map<Part, Integer> _subParts, PartRepository _repository)
    {
        super();
        
        this.id = _id;
        this.name = _name;
        this.descr = _descr;
        
        if(_subParts==null)
        {
            this.subParts = new HashMap<Part,Integer>();
        }
        else
        {
            this.subParts = _subParts;
        }
        
        this.repository = _repository;
    }
    
    public int getId()
    {
        return this.id;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getDescr()
    {
        return this.descr;
    }
    
    public void setDescr(String descr)
    {
        this.descr = descr;
    }
    
    public Map<Part, Integer> getSubparts()
    {
        return this.subParts;
    }
    
    //Retorna uma referência para o repositório da peça
    public PartRepository getRepository()
    {
        return this.repository;
    }
    
    //Retorna o nome do repositório da peça
    public String getRepName()
    {
        return this.repository.getRepName();
    }
    
    //Retorna o tipo da peça
    public int getTipoPeca()
    {
        if(this.subParts.isEmpty())
            return PRIMITIVA;
        else
            return AGREGADA;
    }
    
    //Retorna o número de subcomponentes da peça
    public int getNumSubcomp(int tipo) throws RemoteException
    {
        if(tipo==SUB_DIRETOS)
        {
            //Número de subcomponentes diretos é o tamanho da lista
            return this.subParts.size();
        }
        else
        {
            //Número de subcomponentes primitivos é uma busca em profundidade
            Set<Part> partes = this.subParts.keySet();
            
            Iterator<Part> it = partes.iterator();
            Part parte;
            int numP = 0;
            
            while(it.hasNext())
            {
                parte = it.next();
                numP+=this.numSubcompPrimitivos(parte.getSubparts());
            }
            
            return numP;
        }
    }
    
    private int numSubcompPrimitivos(Map<Part, Integer> subPartsList) throws RemoteException
    {
        if(subPartsList.isEmpty())
            return 1;
        else
        {
            //Iterar nos subcomponentes
            Set<Part> parts = subPartsList.keySet();
            
            Iterator<Part> it = parts.iterator();
            Part p;
            int numP= 0;
            
            while(it.hasNext())
            {
                p = it.next();
                numP+=numSubcompPrimitivos(p.getSubparts());
            }
            
            return numP;
        }
    }
    
}
