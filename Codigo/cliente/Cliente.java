package cliente;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfaces.PartRepository;
import interfaces.Part;
import cliente.FrontEnd;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Cliente {
    
    public static Registry registry;
    public static PartRepository rep;
    
    public Cliente(String servidor) throws RemoteException, NotBoundException{
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        registry = LocateRegistry.getRegistry();
        rep = (PartRepository) registry.lookup(servidor);
}
    
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
            
            Part p = rep.getPartById(2);
            System.out.println("Peça " + p.getId() + ", Nome: " + p.getName() + ", Descr: " + p.getDescr() + "Repositório: " + p.getRepName());
            
        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
    }

    public boolean conectaServidor() throws RemoteException, NotBoundException {
            //Teste para ver se o servidor conectou - CAIO VERIFICAR
            String teste = rep.teste();
            boolean conectou = teste.isEmpty()? false : true;
            return conectou;
    }

    String getPecas() throws RemoteException {
        int numero = rep.getNumPecas();
        return String.valueOf(numero);
    }

    String getListaPecas() throws RemoteException {
        String ListaPecas = "";
        Part p;
        for (int i = 1; i <= rep.getNumPecas(); i++) {
            p = rep.getPartById(i);
            ListaPecas = ListaPecas + i + " - " + p.getName() + "\n";
        }
        return ListaPecas;
    }
    
    String getPecaUnica(int index) throws RemoteException{
        String detalhes = "";
        Part p = rep.getPartById(index);
        //String [] pecaDetalhada = new String [2];
        ArrayList pecaDetalhada1 = new ArrayList<String>();
        
        pecaDetalhada1.add("ID: " + index);
        pecaDetalhada1.add("Nome: " + p.getName());
        pecaDetalhada1.add("Descrição: " + p.getDescr());
        //pecaDetalhada1.add("Numero de Subpecas: " + p.getNumSubcomp(index)); //Ver Caio
        pecaDetalhada1.add("Tipo: " + p.getTipoPeca());
        pecaDetalhada1.add("");
        
        //IMPLEMENTAR LISTA DE SUBPECAS
        
        //Monta String
        for (int i = 0; i < pecaDetalhada1.size(); i++) {
          detalhes = detalhes + pecaDetalhada1.get(i) + "\n";
        }
        
        return detalhes;
        
    }
    
    
    
}
