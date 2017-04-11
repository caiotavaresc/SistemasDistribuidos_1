package servidor.componentes;

import java.util.Map;
import java.io.Serializable;

import servidor.interfaces.Part;

public class PartEngine implements Part, Serializable{
    
    String id;
    String name;
    String descr;
    Map<Part,Integer> subParts;
    
    //Criar um novo objeto de Part
    public PartEngine(String name, String descr, Map<Part, Integer> subParts)
    {
        
    }
    
    public String getId()
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
    
}
