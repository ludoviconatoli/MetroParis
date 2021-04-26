package it.polito.tdp.metroparis.model;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class ProvaGrafo {

	public static void main(String[] args) {
		
		Graph<String, DefaultEdge> grafo = new SimpleGraph<>(DefaultEdge.class);
		
		grafo.addVertex("UNO");
		grafo.addVertex("DUE");
		grafo.addVertex("TRE");
		
		System.out.println(grafo);
		
		grafo.addEdge("UNO", "TRE");
		grafo.addEdge("TRE", "DUE");
		
		//ci sono le parentesi graffe { }, se il grafo fosse orientato al loro posto
		//ci sarebbero le parentesi tonde ( ) 
		System.out.println(grafo);

	}

}
