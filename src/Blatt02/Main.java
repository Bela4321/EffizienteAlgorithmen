package Blatt02;

import java.io.File;

public class Main {

        public static void main(String[] args) {
            File dir = new File("Data/Daten02/Daten");
            File[] files = dir.listFiles();
            for (File file : files) {
                System.out.println(file.getName()+ ": ");
                long start = System.currentTimeMillis();
                MyGraph g = new MyGraph(file);
                double time = (System.currentTimeMillis() - start) / 1000.0;
                System.out.printf("\tReading: %.3f s\n", time);
                start = System.currentTimeMillis();
                boolean isCoCluster = !(g.isCoClustergraph().get(0)==null);
                time = (System.currentTimeMillis() - start) / 1000.0;
                System.out.printf("\tSolving: %.3f s\n", time);
                System.out.println("\tis CoCluster:" + isCoCluster);
            }
        }
}
