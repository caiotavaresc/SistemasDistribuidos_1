package cliente;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfaces.PartRepository;
import interfaces.Part;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.Iterator;

import javax.swing.JOptionPane;

public class Cliente {
    
    public static Registry registry;
    public static PartRepository rep;
    
    //Peca atual com a qual as interações ocorrem
    public static Part pecaAtual;
    
    //Lista de subpecas atual com a qual as interaçõe ocorrem
    public static Map<Part, Integer> listaSubpAtual = new HashMap<Part, Integer>();
    
    //Construtor da classe Cliente, que incializa as obrigatoriedades do Java RMI
    public Cliente(String servidor) throws RemoteException, NotBoundException, MalformedURLException{
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        /*registry = LocateRegistry.getRegistry();
        rep = (PartRepository) registry.lookup(servidor);*/
        
        rep = (PartRepository) Naming.lookup(servidor);
    }
    
    /**********************************************************************************
    Esse método MAIN foi feito para testes, mantivemos ele para manter o historico
    Nao e utilizado no contexto da aplicacao
    ***********************************************************************************/
    public static void main(String[] args)
    {
        //Abrir o Security Manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        try {
            
            /*String[] lista = Naming.list("//25.95.69.52:1099/");
            int i;
            
            for(i = 0; i < lista.length; i++)
            {
                System.out.println(lista[i]);
            }*/
            
            System.out.println(InetAddress.getLocalHost().getHostName());
            
            /*String name = JOptionPane.showInputDialog("Insira o nome do servidor que deseja conectar-se");
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
            System.out.println("Peça " + p.getId() + ", Nome: " + p.getName() + ", Descr: " + p.getDescr() + "Repositório: " + p.getRepName());*/
            
        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
    }

    //Metodo que testa se o servidor conectou
    public boolean conectaServidor() throws RemoteException, NotBoundException {
            
            String teste = rep.teste();
            boolean conectou = teste.isEmpty()? false : true;
            return conectou;
    }

    //Metodo que retorna uma String com o numero de pecas de um PartRepository 
    String getPecas() throws RemoteException {
        int numero = rep.getNumPecas();
        return String.valueOf(numero);
    }

    //Metodo que retorna uma String com a lista de pecas, para ser exibida na GUI
    String getListaPecas() throws RemoteException {
        
        //String que ira conter a lista
        String ListaPecas = "";
        
        //Variavel auxiliar do tipo Part 
        Part p;
        
        //Laço for que monta a String com a lista
        for (int i = 1; i <= rep.getNumPecas(); i++) {
            p = rep.getPartById(i);
            ListaPecas = ListaPecas + i + " - " + p.getName() + "\n";
        }
        
        //Retorna a lista de peças
        return ListaPecas;
    }
    
    //Metodo que retorna uma String com os detalhes de uma peca
    String getPecaUnica(int index) throws RemoteException{
        
        //Stirn que sera retornada no final
        String detalhes = "";
        
        //Peca que sera obtido o detalhe
        Part p = rep.getPartById(index);
        Cliente.pecaAtual = p;
        Map<Part, Integer> listaSubp = p.getSubparts();
        
        //ArrayList de String, em que cada posicao é um campo com informaçoes
        ArrayList pecaDetalhada1 = new ArrayList<String>();
        
        //Adiciona as informações dos campos no ArrayList
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
        
        //Monta a String de resposta
        for (int i = 0; i < pecaDetalhada1.size(); i++) {
          detalhes = detalhes + pecaDetalhada1.get(i) + "\n";
        }
        
        //Retorna a string com os detalhes 
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
        //Variaveis que auxiliaram a montagem da String
        Set<Part> s = Cliente.listaSubpAtual.keySet();
        Set<String> lista = new TreeSet<String>();
        Iterator<Part> it = s.iterator();
        Part p;
        
        //Percorre adicionando informacoes na lista
        while(it.hasNext())
        {
            p = it.next();
            lista.add(p.getId() + " - " + p.getName() + " [" + Cliente.listaSubpAtual.get(p) + "] (" + p.getRepName() + ")");
        }
        
        //Iterar pela lista que está ordenada
        Iterator<String> it2 = lista.iterator();
        String texto = "";
        
        //Monta a string de retorno
        while(it2.hasNext())
        {
            texto += it2.next() + "\n";
        }
        
        //Retorna a string com a lista de subpecas atual
        return texto;
    }
    
    //Método que adiciona o estado atual do sistema como uma nova peça
    void adicionarNovaPeca(String _nome, String _descricao) throws RemoteException
    {
        rep.addPart(_nome, _descricao, Cliente.listaSubpAtual);
    }
    
}