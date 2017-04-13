package cliente;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfaces.PartRepository;

import javax.swing.JOptionPane;

public class Cliente {
    
    public static void main(String[] args)
    {
        //Abrir o Security Manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        try {
            String name = JOptionPane.showInputDialog("Insira o nome do servidor que deseja conectar-se");
            Registry registry = LocateRegistry.getRegistry();
            PartRepository rep = (PartRepository) registry.lookup(name);
            
            //Executar um teste
            String teste = rep.teste();
            
            //Incluir duas peças
            rep.addPart("Arroz", "Arrozito", null);
            rep.addPart("Feijão", "Feijaozito", null);
            
            //Pegar o número de peças
            int numPecas = rep.getNumPecas();
            System.out.println(numPecas);
            System.out.println(teste);
        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
    }
    
}
