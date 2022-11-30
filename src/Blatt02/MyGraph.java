package Blatt02;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MyGraph implements Graph{

    private Set<Integer> vertices;
    private Map<Integer, List<Integer>> edges;


    public MyGraph(File file) {
        vertices = new HashSet<>();
        edges = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            FindMax findMax = new FindMax();

            reader.lines().forEach(findMax);

            for (int i = 1; i <= findMax.max; i++) {
                addVertex(i);
                edges.put(i, new ArrayList<>());
            }

            reader = new BufferedReader(new FileReader(file));
            reader.lines().forEach(x->{this.addEdge(x.split(" "));});
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public MyGraph(MyGraph g) {
        this.vertices = new HashSet<>(g.vertices);
        this.edges = new HashMap<>(g.edges);
        for (Map.Entry entry : this.edges.entrySet()) {
            this.edges.put((Integer) entry.getKey(), new ArrayList<>((List<Integer>) entry.getValue()));
        }
    }

    public List<Set<Integer>> isCoClustergraph(){

        //node-> color
        HashMap<Integer,Integer> color = new HashMap<>();
        //color -> first node
        HashMap<Integer,Integer> representative = new HashMap<>();

        int colorCount = 0;
        for (Integer v : vertices) {
            if (!color.containsKey(v)) {
                color.put(v, colorCount);
                representative.put(colorCount, v);
                colorCount++;
            }
            for (Integer w : vertices) {
                if (adjacent(v,w)) {
                    if (color.containsKey(w)){
                        if (color.get(v).equals(color.get(w))){
                            List<Set<Integer>> result = new ArrayList<>();
                            result.add(null);
                            Set<Integer> set = new HashSet<>();
                            set.add(v);set.add(w);set.add(representative.get(color.get(v)));
                            result.add(null);
                            return result;
                        }
                    }
                } else {
                    if (color.containsKey(w)){
                        if (!color.get(v).equals(color.get(w))){
                            List<Set<Integer>> result = new ArrayList<>();
                            result.add(null);
                            Set<Integer> set = new HashSet<>();
                            set.add(v);set.add(w);set.add(representative.get(color.get(v)));
                            result.add(null);
                            return result;
                        }
                    } else {
                        color.put(w, color.get(v));
                    }
                }
            }
        }
        return color.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue))
                .values().stream().map(x->x.stream().map(Map.Entry::getKey).collect(Collectors.toSet()))
                .collect(Collectors.toList());
    }

    @Override
    public void addVertex(Integer v) {
        vertices.add(v);
    }

    @Override
    public void addEdge(Integer v, Integer w) {
        edges.get(v).add(w);
        edges.get(w).add(v);
    }

    public void addEdge(String[] edge) {
        addEdge(Integer.parseInt(edge[0]), Integer.parseInt(edge[1]));
    }

    @Override
    public void deleteVertex(Integer v) {
        vertices.remove(v);

        for (Integer i : edges.get(v)) {
            edges.get(i).remove(v);
        }
        edges.put(v,new ArrayList<Integer>());
    }

    @Override
    public void deleteEdge(Integer u, Integer v) {
        edges.get(u).remove(v);
        edges.get(v).remove(u);
    }

    @Override
    public boolean contains(Integer v) {
        return vertices.contains(v);
    }

    @Override
    public int degree(Integer v) {
        return edges.get(v).size();
    }

    @Override
    public boolean adjacent(Integer v, Integer w) {
        return edges.get(v).contains(w);
    }

    @Override
    public Graph getCopy() {
        return new MyGraph(this);
    }

    @Override
    public Set<Integer> getNeighbors(Integer v) {
        return new ArrayList<>(this.edges.get(v)).stream().collect(Collectors.toSet());
    }

    @Override
    public int size() {
        return vertices.size();
    }

    @Override
    public int getEdgeCount() {
        return edges.size();
    }

    @Override
    public Set<Integer> getVertices() {
        return new HashSet<>(vertices);
    }
}
class FindMax implements Consumer<String> {
    int max=0;

    @Override
    public void accept(String s) {
        Arrays.stream(s.split(" "))
                .map(s1->Integer.parseInt(s1))
                .forEach(s1->{if (s1>max){max=s1;}});
    }
}