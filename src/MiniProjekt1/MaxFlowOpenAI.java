package MiniProjekt1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.util.*;

public class MaxFlowOpenAI {
    //settings
    static List<String> lager = List.of(new String[]{ "L1", "L2", "L3", "L4", "L5" });
    static String lagerToCheckWithout = "L2";

    static String[] zentrale = {"Z"};


    // Number of vertices in the graph
    private int numVertices;
    // Graph represented as an adjacency list
    private List<List<Edge>> adjacencyList;
    //name-id hashmap
    HashMap<String, Integer> nameId;
    //max flow
    public int maxFlow = -1;


    /**
     * Constructor method
     * @param numVertices : number of vertices to create
     */
    public MaxFlowOpenAI(int numVertices) {
        this.numVertices = numVertices;
        this.adjacencyList = new ArrayList<>(numVertices);
        for (int i = 0; i < numVertices; i++) {
            this.adjacencyList.add(new ArrayList<>());
        }
    }

    /**
     * Creates and adds an edge between two given vertices into the graph
     * @param from : start vertex
     * @param to : end vertex
     * @param capacity : edge capacity
     */
    public void addEdge(int from, int to, int capacity) {
        Edge forward = new Edge(from, to, capacity);
        Edge backward = new Edge(to, from, 0);
        forward.backward = backward;
        backward.backward = forward;
        adjacencyList.get(from).add(forward);
        adjacencyList.get(to).add(backward);
    }

    /**
     * Computing the maximum flow from given source to sink
     * @param source : id of source vertex
     * @param sink : id of sink vertex
     * @return : integer value of maximum flow
     */
    public int maximumFlow(int source, int sink) {
        int maxFlow = 0;

        // Edmonds-Karp algorithm
        while (true) {
            // Breadth-first search to find an augmenting path
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(source);
            int[] predecessors = new int[numVertices];
            Edge[] predecessorEdge = new Edge[numVertices];
            for (int i = 0; i < numVertices; i++) {
                predecessors[i] = -1;
            }
            predecessors[source] = source;

            while (!queue.isEmpty() && predecessors[sink] == -1) {
                int u = queue.poll();
                for (Edge edge : adjacencyList.get(u)) {
                    int v = edge.to;
                    if (predecessors[v] == -1 && edge.remainingCapacity() > 0) {
                        queue.offer(v);
                        predecessors[v] = u;
                        predecessorEdge[v] = edge;
                    }
                }
            }
            // If there is no augmenting path, we're done
            if (predecessors[sink] == -1) {
                break;
            }
            // Compute the maximum flow along the augmenting path
            int pathFlow = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = predecessors[v]) {
                int u = predecessors[v];
                pathFlow = Math.min(pathFlow, predecessorEdge[v].remainingCapacity());
            }
            // Update the flow along the augmenting path
            for (int v = sink; v != source; v = predecessors[v]) {
                int u = predecessors[v];
                predecessorEdge[v].addFlow(pathFlow);
            }

            // Add the flow to the maximum flow
            maxFlow += pathFlow;
        }
        return maxFlow;
    }

    /**
     * Inner class representing an edge in the graph
     */
    private static class Edge {
        int from, to;
        int capacity, flow;
        Edge backward; // The backward edge corresponding to this edge

        /**
         * Constructor method
         * @param from : id of start vertex
         * @param to : id of end vertex
         * @param capacity : edge capacity
         */
        public Edge(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
        }

        // Calculate the remaining capacity of this edge
        public int remainingCapacity() {
            return capacity - flow;
        }

        // Add flow to this edge
        public void addFlow(int flow) {
            this.flow += flow;
            this.backward.flow -= flow;
        }
    }

    /**
     * Main Method to create a MaxFlowOpenAI instance and generates a directed flow graph from a given file
     * @param file : input file containing information about the graph
     * @param ignoreL2 : boolean value if the vertex "L2" should be ignored (true) or not (false)
     * @return : directed flow graph generated from file
     */
    private static MaxFlowOpenAI maxFlow(File file, boolean ignoreL2) {
        //read csv file to table
        List<String[]> table = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                table.add(line.split("\s*;\s*"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //create graph
        HashMap<String, Integer> nameId = new HashMap<String, Integer>();
        HashMap<String, Integer> capacity = new HashMap<String, Integer>();

        //Lager
        List<String> lager = new ArrayList(MaxFlowOpenAI.lager);
        if (ignoreL2){
            for (int i = 0; i < lager.size()-1; i++) {
                if (lager.get(i).equals(lagerToCheckWithout)) {
                    lager.remove(i--);
                }
            }
        }
        int id = 2;

        //add vertices and check capacity
        boolean first = true;
        for (String[] row : table) {
            //skip first line since it contains only vertex names (headline of adjacency matrix)
            if (first) {
                first = false;
                continue;
            }
            // check for information about vertex capacity (last column)
            String[] parts = row[row.length - 1].split(" ");
            if (parts[0].equals("Kapazitaet:")) {
                try{
                    // if the vertex has capacity, split it into two vertices with an edge between them containing the capacity value
                    int capacityValue = Integer.parseInt(parts[1]);
                    capacity.put(row[0], capacityValue);
                    nameId.put(row[0], id);
                    id = id + 2;
                } catch (Exception ignored){
                    // capacity value cannot be parsed -> ignore capacity and add only one vertex
                    nameId.put(row[0], id);
                    id++;
                }
            } else {
                // no capacity -> add only one vertex
                nameId.put(row[0], id);
                id++;
            }
        }

        //add edges
        MaxFlowOpenAI graph = new MaxFlowOpenAI(id);
        graph.nameId = nameId;
        //intervertex edges
        for (String key : capacity.keySet()) {
            graph.addEdge(nameId.get(key), nameId.get(key) + 1, capacity.get(key));
        }
        //other edges
        first = true;
        // iterate adjacency (capacity) matrix
        for (String[] row : table) {
            //skip first line
            if (first) {
                first = false;
                continue;
            }
            for (int i = 1; i < row.length - 1; i++) {
                // check if edge exists, 0 capacity -> skip
                if (row[i].equals("0")) continue;
                // edge has capacity -> add to the graph
                int start = nameId.get(row[0]);
                if (capacity.containsKey(row[0])) {
                    start++;
                }
                int end = nameId.get(table.get(0)[i]);
                try {
                    int capacityValue = Integer.parseInt(row[i]);
                    graph.addEdge(start, end, capacityValue);
                } catch (Exception ignored) {}
            }
        }
        //add source and sink
        for (String l : lager) {
            graph.addEdge(0, nameId.get(l), Integer.MAX_VALUE);
        }
        for (String z : zentrale) {
            graph.addEdge(nameId.get(z), 1, Integer.MAX_VALUE);
        }
        graph.maxFlow = graph.maximumFlow(0, 1);
        return graph;
    }

    /**
     * A method that computes the optimal distribution between sources in order to minimize the number of cycles needed
     * @param file : a file containing information about the graph
     */
    public static void calcOptimalDistribution(File file){
        MaxFlowOpenAI graph = maxFlow(file, false);
        System.out.println("Optimale Lösung pro Durchgang:");
        System.out.println("Max Flow: "+graph.maxFlow);
        for (String lager: new String[]{"L1","L2","L3","L4","L5"}){
            for (Edge edge : graph.adjacencyList.get(0)){
                if (edge.to == graph.nameId.get(lager)){
                    System.out.println("Lager " + lager + " bekommt " + edge.flow + " Pakete");
                }
            }
        }
        int neededCycles = (int) Math.ceil(1000./(double) graph.maxFlow);
        System.out.println("Benötigte Durchgänge: " + neededCycles);
        System.out.println("Optimale Lösung für alle Durchgänge:");
        // calculating for 1000 presents
        int presentsLeft = 1000;
        for (String lager: new String[]{"L1","L2","L3","L4","L5"}){
            for (Edge edge : graph.adjacencyList.get(0)){
                if (edge.to == graph.nameId.get(lager)){
                    System.out.println("Lager " + lager + " bekommt " + (int) Math.min(edge.flow*neededCycles,presentsLeft) + " Pakete");
                    presentsLeft -= edge.flow*neededCycles;
                    if (presentsLeft<0){
                        presentsLeft=0;
                    }
                }
            }
        }
    }

    /**
     * A method that compares solutions of the maximum flow problem either including or ignoring the vertex "L2"
     * and prints both solutions to the console.
     * @param file : a file containing information about the graph
     */
    public static void comparewithoutL2(File file){
        // compute maxFlow for both cases (using and ignoring vertex L2)
        MaxFlowOpenAI graph1 = maxFlow(file, false);
        MaxFlowOpenAI graph2 = maxFlow(file, true);

        //compare needed cycles for both solutions
        int neededCycles1 = (int) Math.ceil(1000./(double) graph1.maxFlow);
        int neededCycles2 = (int) Math.ceil(1000./(double) graph2.maxFlow);
        if (neededCycles2>neededCycles1){
            System.out.println("Lösung ohne L2 ist schlechter, man benötigt " + (neededCycles2-neededCycles1) + " Durchgänge mehr");
        } else {
            System.out.println("Lösung ohne L2 ist gleich gut, man benötigt immer noch " + neededCycles1 + " Durchgänge");
        }
        System.out.println("Optimale Lösung ohne L2:");
        int presentsLeft = 1000;
        for (String lager: new String[]{"L1","L3","L4","L5"}){
            for (Edge edge : graph2.adjacencyList.get(0)){
                if (edge.to == graph2.nameId.get(lager)){
                    System.out.println("Lager " + lager + " bekommt " + (int) Math.min(edge.flow*neededCycles2,presentsLeft) + " Pakete");
                    presentsLeft -= edge.flow*neededCycles2;
                    if (presentsLeft<0){
                        presentsLeft=0;
                    }
                }
            }
        }
    }

    /**
     * A method that computes the maximum flow of a given graph and finds unnecessary edges that are not being used (edges with no flow)
     * @param file : a file containing information about the graph
     * @param ignoreL2 : boolean value if the vertex "L2" should be ignored (true) or not (false)
     */
    public static void findUnusedPipes(File file, boolean ignoreL2){

        // compute maxFlow
        MaxFlowOpenAI graph = MaxFlowOpenAI.maxFlow(file, ignoreL2);
        System.out.println("Unbenutzte Rohren:");

        // iterate all edges and find ones with 0 flow that has >0 capacity and is not connected to source or sink
        for (int i = 0; i < graph.adjacencyList.size(); i++) {
            for (Edge edge : graph.adjacencyList.get(i)){
                if (edge.flow == 0&&edge.capacity!=0&&edge.from!=0&&edge.to!=1){
                    System.out.println("Rohr von " + graph.reverseNameId(edge.from) + " nach " + graph.reverseNameId(edge.to));
                }
            }
        }
    }


    /**
     * A method that locates a vertex name from the original file using a vertex id.
     * This is necessary because some vertices have been split in two in order to implement vertex capacity
     * @param id : vertex id in the graph
     * @return : String that contains the original name
     */
    private String reverseNameId(int id){
        for (String key : nameId.keySet()){
            if (nameId.get(key) == id){
                return key;
            }
        }
        // if name was not found in the hashmap, the vertex might have been split in two to implement vertex capacity
        // in that case, the name is found at id-1
        for (String key : nameId.keySet()){
            if (nameId.get(key) == id-1){
                return key;
            }
        }
        // ... or there simply is no vertex with the given id
        return null;
    }
}
