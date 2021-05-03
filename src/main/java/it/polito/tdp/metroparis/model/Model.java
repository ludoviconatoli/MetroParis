package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	Graph <Fermata, DefaultEdge> grafo ;
	Map<Fermata, Fermata> predecessore;
	
	public void creaGrafo() {
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		
		MetroDAO dao = new MetroDAO();
		List<Fermata> fermate = dao.getAllFermate();
		
		//aggiungo vertici -> fermate
		//for(Fermata f: fermate)
		//	this.grafo.addVertex(f);
		
		//Alternativa per aggiungere i vertici
		Graphs.addAllVertices(this.grafo, fermate);
		
		//Aggiungiamo archi
		/*for(Fermata f1: this.grafo.vertexSet())
		{
			for(Fermata f2: this.grafo.vertexSet()) {
				if(!f1.equals(f2) && dao.fermateCollegate(f1, f2))
				{
					this.grafo.addEdge(f1, f2);
				}
			}
		}*/
		//Alternativa
		List<Connessione> connessioni = dao.getAllConnessioni(fermate);
		for(Connessione c: connessioni) {
			this.grafo.addEdge(c.getStazP(), c.getStazA());
		}
		//System.out.println(this.grafo);
		
		Fermata f;
		/*Set<DefaultEdge> archi = this.grafo.edgesOf(f);
		for(DefaultEdge e: archi) {
			Fermata f1 = this.grafo.getEdgeSource(e);
			//oppure
			Fermata f2 = this.grafo.getEdgeTarget(e);
			if(f1.equals(f)) {
				//f2 è quello che mi serve
			}else {
				//f1 è quello che mi serve
			}
		}*/
		
		//la ricerca di prima si può fare così
		//Fermata f1 = Graphs.getOppositeVertex(this.grafo, e, f);
		
		//il ciclo for di prima si può fare così
		//perchè gli archi non sono orientati e il metodo restituisce
		//tutti i vertici vicini al vertice dato
		//List<Fermata> fermateAdiacenti = Graphs.successorListOf(this.grafo, f);
		
	
	}
	
	public List<Fermata> fermateRaggiungibili(Fermata partenza){
		//Visita in ampiezza
		BreadthFirstIterator<Fermata, DefaultEdge> bfv = new BreadthFirstIterator<>(this.grafo, partenza);
		
		this.predecessore = new HashMap<>();
		this.predecessore.put(partenza, null);
		
		bfv.addTraversalListener(new TraversalListener<Fermata, DefaultEdge>(){

			
			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
				DefaultEdge arco = e.getEdge();
				Fermata a = grafo.getEdgeSource(arco);
				Fermata b = grafo.getEdgeTarget(arco);
				
				if(predecessore.containsKey(b) && !predecessore.containsKey(a)) {
					//ho scoperto 'a' arrivando da 'b' ( se 'b' lo conosco già ( è nella mappa) )
					predecessore.put(a, b);
				}else if(predecessore.containsKey(a) && !predecessore.containsKey(b)){
					//conosco 'b' arrivando da 'a'
					predecessore.put(b, a);
				}
				
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Fermata> e) {
				Fermata nuova = e.getVertex();
				//Fermata precedente ;
				//predecessore.put(nuova, precedente);
				
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Fermata> e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		List<Fermata> result = new ArrayList<>();
		
		while(bfv.hasNext()) {
			Fermata f = bfv.next();
			result.add(f);
		}
		
		return result;
	}
	
	public Fermata trovaFermata(String nome) {
		for(Fermata f: this.grafo.vertexSet()) {
			if(f.getNome().equals(nome))
				return f;
		}
		
		return null;
	}
	
	public List<Fermata> trovaCammino(Fermata partenza, Fermata arrivo){
		fermateRaggiungibili(partenza);
		
		List<Fermata> result = new LinkedList<>();
		result.add(arrivo);
		Fermata f = arrivo;
		
		while(predecessore.get(f) != null) {
			f = predecessore.get(f);
			result.add(0, f);
		}
		
		return result;
	}
}
