package Blatt04;

import java.io.File;
import java.util.*;

public class ShortestPath{
    static WeightedGraph g;
    static final int n=100; //number of queries in experiments
    public static void main(String[] args){
	System.err.println("Computing a shortest (s,t)-paths in weighted graphs");
	WeightedGraph G = new MyWeightedGraph(new File(args[0]));
	Dijkstra algo = new Dijkstra(G);
	Random rand =  new Random();
	

	System.err.println("Standard Dijkstra");

	long startTime = System.nanoTime();


	
	for (int i = 0; i<n;i++){
	    int s = rand.nextInt(G.getVertices().size());
	    int t = rand.nextInt(G.getVertices().size());
	    
	    HashMap<Integer,Integer> dist = algo.distFromStartVertex(s);
	    System.out.println("Shortest path from "+G.getVertexName(s)+ " to "+G.getVertexName(t) + " has length "+dist.get(t));
	}
	System.err.println("Average time per query: "+(System.nanoTime() - startTime)/ (1000000*n)+"ms"); 


	
	System.err.println("Dijkstra with early stop");
	startTime = System.nanoTime();
	for (int i = 0; i<n;i++){
	    int s = rand.nextInt(G.getVertices().size());
	    int t = rand.nextInt(G.getVertices().size());
	    System.out.println("Shortest path from "+G.getVertexName(s)+ " to "+G.getVertexName(t) + " has length "+algo.distFromStartToEnd(s,t));
	}
	System.err.println("Average time per query: "+(System.nanoTime() - startTime)/ (1000000*n)+"ms"); 
    }
}
