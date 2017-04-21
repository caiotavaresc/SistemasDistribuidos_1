package cliente;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfaces.PartRepository;
import interfaces.Part;
import cliente.FrontEnd;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

import javax.swing.JOptionPane;

public class Cliente {
    
    public static Registry registry;
    public static PartRepository rep;
    
    //Peca atual com a qual as interações ocorrem
    public static Part pecaAtual;
    
    //Lista de subpecas atual com a qual as interaçõe ocorrem
    public static Map<Part, Integer> listaSubpAtual = new HashMap<Part, Integer>();
    
    public Cliente(String servidor) throws RemoteException, NotBoundException{
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        registry = LocateRegistry.getRegistry();
        rep = (PartRepository) registry.lookup(servidor);
}
    
    //Esse método MAIN foi feito para testes, mantivemos ele para manter o historico
    //Nao e utilizado no contexto da aplicacao
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
        Cliente.pecaAtual = p;
        Map<Part, Integer> listaSubp = p.getSubparts();
        
        ArrayList pecaDetalhada1 = new ArrayList<String>();
        
        pecaDetalhada1.add("ID: " + index);
        pecaDetalhada1.add("Nome: " + p.getName());
        pecaDetalhada1.add("Descrição: " + p.getDescr());
        pecaDetalhada1.add("Repositório: " + p.getRepName());
        pecaDetalhada1.add("Tipo: " + (p.getTipoPeca() == Part.AGREGADA ? "Agregada" : "Primitiva"));
        
        //Se a peça for composta, então apresentar o número de subcomponentes
        if (p.getTipoPeca() == Part.AGREGADA)
        {
            pecaDetalhada1.add("    Subcomp. diretos: " + p.getNumSubcomp(Part.SUB_DIRETOS));
            pecaDetalhada1.add("    Subcomp. primitivos: " + p.getNumSubcomp(Part.SUB_PRIMITIVOS));
        
            //E mostra a lista de subcomponentes
            pecaDetalhada1.add("");
            pecaDetalhada1.add("Lista de Subpeças:");
            
            //A lista de subpeças limitar-se-a aos subcomponentes diretos
            pecaDetalhada1.add(p.getSubpartsString());
        }
        
        //Monta String
        for (int i = 0; i < pecaDetalhada1.size(); i++) {
          detalhes = detalhes + pecaDetalhada1.get(i) + "\n";
        }
        
        return detalhes;
    }
    
    //Método que adiciona uma certa quantidade de uma peça ao repositório atual
    void addPecaListaAtual(Part p, int quantidade)
    {
        int qtd;
        
        //Se a peça já existir na lista, apenas aumentar sua frequencia em quantidade
        if(Cliente.listaSubpAtual.containsKey(p))
        {
            qtd = Cliente.listaSubpAtual.get(p);
            Cliente.listaSubpAtual.put(p, qtd + quantidade);
        }
        else
        {
            Cliente.listaSubpAtual.put(p, quantidade);
        }
    }
    
    //Método que obtém em STRING a lista de subpeças atual
    String getListaSubpAtual() throws RemoteException
    {
        Set<Part> s = Cliente.listaSubpAtual.keySet();
        Iterator<Part> it = s.iterator();
        Part p;
        
        String texto = "";
        
        while(it.hasNext())
        {
            p = it.next();
            texto = texto + p.getId() + " - " + p.getName() + " [" + Cliente.listaSubpAtual.get(p) + "] (" + p.getRepName() + ")\n";
        }
        
        return texto;
    }
    
    //Método que adiciona o estado atual do sistema como uma nova peça
    void adicionarNovaPeca(String _nome, String _descricao) throws RemoteException
    {
        rep.addPart(_nome, _descricao, Cliente.listaSubpAtual);
    }
    
}