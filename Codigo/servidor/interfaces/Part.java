package servidor.interfaces;

import java.util.Map;

public interface Part{
    
    public String getId();
    
    public String getName();
    public void setName(String name);
    
    public String getDescr();
    public void setDescr(String descr);
    
    public Map<Part, Integer> getSubparts();
    
}
