package it.polito.tdp.metroparis.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	Graph <Fermata, DefaultEdge> grafo ;
	
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
		for(Fermata f1: this.grafo.vertexSet())
		{
			for(Fermata f2: this.grafo.vertexSet()) {
				if(!f1.equals(f2) && dao.fermateCollegate(f1, f2))
				{
					this.grafo.addEdge(f1, f2);
				}
			}
		}
		
		System.out.println(this.grafo);
	}
	
}
