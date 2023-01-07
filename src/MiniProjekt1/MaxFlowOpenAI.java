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


    // Constructor
    public MaxFlowOpenAI(int numVertices) {
        this.numVertices = numVertices;
        this.adjacencyList = new ArrayList<>(numVertices);
        for (int i = 0; i < numVertices; i++) {
            this.adjacencyList.add(new ArrayList<>());
        }
    }

    // Add an edge to the graph
    public void addEdge(int from, int to, int capacity) {
        Edge forward = new Edge(from, to, capacity);
        Edge backward = new Edge(to, from, 0);
        forward.backward = backward;
        backward.backward = forward;
        adjacencyList.get(from).add(forward);
        adjacencyList.get(to).add(backward);
    }

    // Find the maximum flow from the source to the sink
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
    // Inner class representing an edge in the graph
    private static class Edge {
        int from, to;
        int capacity, flow;
        Edge backward; // The backward edge corresponding to this edge

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

    // Test the Ford Fulkerson algorithm
    public static MaxFlowOpenAI maxFlow(File file, boolean ignoreL2) {
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
            //skip first line
            if (first) {
                first = false;
                continue;
            }
            String[] parts = row[row.length - 1].split(" ");
            if (parts[0].equals("Kapazitaet:")) {
                    try{
                        int capacityValue = Integer.parseInt(parts[1]);
                        capacity.put(row[0], capacityValue);
                        nameId.put(row[0], id);
                        id = id + 2;
                    } catch (Exception ignored){
                        nameId.put(row[0], id);
                        id++;
                    }
            } else {
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
        for (String[] row : table) {
            //skip first line
            if (first) {
                first = false;
                continue;
            }
            for (int i = 1; i < row.length - 1; i++) {
                //check if edge exists
                if (row[i].equals("0")) continue;
                //add edge
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

    public static void comparewithoutL2(File file){
        MaxFlowOpenAI graph1 = maxFlow(file, false);
        MaxFlowOpenAI graph2 = maxFlow(file, true);
        //compare needed cycles
        int neededCycles1 = (int) Math.ceil(1000./(double) graph1.maxFlow);
        int neededCycles2 = (int) Math.ceil(1000./(double) graph2.maxFlow);
        if (neededCycles2>neededCycles1){
            System.out.println("Lösung ohne L2 ist schlechter, man benötigt " + (neededCycles2-neededCycles1) + " Durchgänge mehr");
        } else {
            System.out.println("Lösung ohne L2 ist gleichgut, man benötigt immer noch " + neededCycles1 + " Durchgänge");
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

    public static void findUnusedPipes(MaxFlowOpenAI graph){
        System.out.println("Unbenutzte Rohren:");
        for (int i = 0; i < graph.adjacencyList.size(); i++) {
            for (Edge edge : graph.adjacencyList.get(i)){
                if (edge.flow == 0&&edge.capacity!=0&&edge.from!=0&&edge.to!=1){
                    System.out.println("Rohr von " + graph.reverseNameId(edge.from) + " nach " + graph.reverseNameId(edge.to));
                }
            }
        }
    }


    //second for loop because of possible node splitting, when with node capacity
    private String reverseNameId(int id){
        for (String key : nameId.keySet()){
            if (nameId.get(key) == id){
                return key;
            }
        }
        for (String key : nameId.keySet()){
            if (nameId.get(key) == id-1){
                return key;
            }
        }
        return null;
    }
}
