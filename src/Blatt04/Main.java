package Blatt04;

import java.io.File;
import java.util.Random;
import java.util.function.BiFunction;

public class Main {

    public static void main(String[] args) {
        File dir = new File("Data/Daten04");
        File[] files = dir.listFiles();
        for (File file : files) {
            //seqcify instance
            System.out.println(file.getName()+ ": ");
            long start = System.currentTimeMillis();
            MyWeightedGraph g = new MyWeightedGraph(file);
            Dijkstra d = new Dijkstra(g);
            double time = (System.currentTimeMillis() - start) / 1000.0;
            System.out.printf("\tReading: %.3f s\n", time);

            //for regular dijkstra
            start = System.currentTimeMillis();
            int bound = g.getVertices().size();
            for (int i = 0; i < 50; i++) {
                //select random start and end vertex
                int s = new Random().nextInt(1,bound);
                int t = new Random().nextInt(1,bound);
                d.distFromStartToEnd(s, t);
                System.out.print("-");
            }
            System.out.println();
            time = (System.currentTimeMillis() - start) / 1000.0/50;
            System.out.printf("\tregular dijkstra: %.3f s\n", time);

            //for bidirectional dijkstra
            start = System.currentTimeMillis();
            for (int i = 0; i < 50; i++) {
                //select random start and end vertex
                int s = new Random().nextInt(1,bound);
                int t = new Random().nextInt(1,bound);
                d.bidirectionalDijkstra(s, t);
                System.out.print("-");
            }
            System.out.println();
            time = (System.currentTimeMillis() - start) / 1000.0/50;
            System.out.printf("\tbidirectional dijkstra: %.3f s\n", time);

            //for bidirectionalAlt dijkstra
            start = System.currentTimeMillis();
            for (int i = 0; i < 50; i++) {
                //select random start and end vertex
                int s = new Random().nextInt(1,bound);
                int t = new Random().nextInt(1,bound);
                d.bidirectionalDijkstraAlt(s, t);
                System.out.print("-");
            }
            System.out.println();
            time = (System.currentTimeMillis() - start) / 1000.0/50;
            System.out.printf("\tbidirectionalAlt dijkstra: %.3f s\n", time);

            //for heuristic dijkstra
            start = System.currentTimeMillis();
            for (int i = 0; i < 50; i++) {
                //select random start and end vertex
                int s = new Random().nextInt(1,bound);
                int t = new Random().nextInt(1,bound);
                d.heuristicDijkstra(s, t);
                System.out.print("-");
            }
            System.out.println();
            time = (System.currentTimeMillis() - start) / 1000.0/50;
            System.out.printf("\theuristic dijkstra: %.3f s\n", time);
        }


    }
}
