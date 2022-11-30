package Blatt04;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.*;

public class MyWeightedGraph implements WeightedGraph {
	
	private HashSet<Integer> nodes;				  // set of vertices of the graph
    private HashMap<Integer, HashMap<Integer,Integer>> adjacency; // sparse weighted adjacency matrix of the graph

    private HashMap<Integer, String> vertexNames;                 // required for outputting solutions
	/** Loads a graph from a file in edge list format
	 * 
	 * @param file Name of the file to be read.
	 * @throws IOException 
	 */
	public MyWeightedGraph (File file) {
		
		nodes = new HashSet<Integer>();
		adjacency = new HashMap<Integer, HashMap<Integer,Integer>>();
		HashMap<String, Integer> seenIds = new HashMap<String, Integer>();	// only required during construction 
		vertexNames = new HashMap<Integer, String>();
		
		try {
			final LineNumberReader reader = new LineNumberReader(new FileReader(file));
			String str;
			int id = 0;
			while ((str = reader.readLine()) != null)
			    {
				str = str.trim();										// trim away whitespace at either end of line
				if (!str.startsWith("#") && !str.startsWith("%") ) {								// skip comment lines
				    StringTokenizer tokens = new StringTokenizer(str);
				    if (tokens != null && tokens.countTokens() > 1) {	// only consider well-formed lines
					String vertexA = tokens.nextToken();
					String vertexB = tokens.nextToken();
					Integer l = Integer.valueOf(tokens.nextToken());
					if (!seenIds.containsKey(vertexA)) {		// add vertex 0 if never seen before
					    seenIds.put(vertexA, id);
					    addVertex(id,vertexA);
					    id++;
					}
					if (!seenIds.containsKey(vertexB)) {		// add vertex 1 if never seen before
					    seenIds.put(vertexB, id);
					    addVertex(id,vertexB);
					    id++;
					}
					// add edge to both adjacency lists
					addEdge(seenIds.get(vertexA), seenIds.get(vertexB),l);
				    }
				}
			    }
			reader.close();
		} catch (IOException e) {
			System.out.println("Could not locate input file '"+file.getName()+"'.");
			System.exit(0);
		}
	}
	
	public MyWeightedGraph () {
		nodes = new HashSet<Integer>();
		adjacency = new HashMap<Integer, HashMap<Integer,Integer>>();
		vertexNames = new HashMap<Integer,String>();
	}
	
	public void addVertex (Integer v) {
		nodes.add(v);
		vertexNames.put(v,v.toString());
		adjacency.put(v, new HashMap<Integer,Integer>());
	}

    public void addVertex (Integer v, String name) {
		nodes.add(v);
		vertexNames.put(v,name);
		adjacency.put(v, new HashMap<Integer,Integer>());
	}

    
    public void addEdge (Integer v, Integer w, Integer l) {
		if (v == w) return;		// No loops!
		adjacency.get(v).put(w,l);
		adjacency.get(w).put(v,l);
	}
	
	public int size () {
		return nodes.size();
	}
	
	/** Returns whether the given vertex ID belongs to the graph. */
	public boolean contains (Integer v) {
		return nodes.contains(v);
	}
	
	public int degree (Integer v) {
		return adjacency.get(v).size();
	}
	
	/** Returns whether vertices v and w are adjacent. */
	public boolean adjacent (Integer v, Integer w) {
		return adjacency.get(v).containsKey(w);
	}
	
	public HashSet<Integer> getVertices () {
		return nodes;
	}
	
	public int getEdgeCount () { /** Lege private Variable an */
		int edges = 0;
		for (int v : nodes)
			edges += adjacency.get(v).size();
		edges /= 2;
		return edges;
	}
	
	
	public Set<Integer> getNeighbors (Integer v) {
	    return adjacency.get(v).keySet();
	}
	

    public int edgeLength (Integer v, Integer w) {
	if (adjacency.get(v).containsKey(w)){
	    return adjacency.get(v).get(w);
	}
	else return Integer.MAX_VALUE;
    }

    
	public void deleteVertex (Integer vertex) {
	    for (int neighbor : adjacency.get(vertex).keySet())
			adjacency.get(neighbor).remove(vertex);
		nodes.remove(vertex);
		vertexNames.remove(vertex);
	}

	public void deleteEdge (Integer u, Integer v){ 
		adjacency.get(u).remove(v);
		adjacency.get(v).remove(u);
	}
	

    	public String getVertexName (Integer i) {
	    return vertexNames.get(i);
	}

}
