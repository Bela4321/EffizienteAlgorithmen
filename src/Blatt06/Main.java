package Blatt06;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        File file = new File("Data/Daten06/");
        for (File f : file.listFiles()) {
            ClosestPair cp = new ClosestPair(f);
            System.out.println(f.getName() + ": ");
            //recursive
            System.out.print("\trecursive: ");
            long start = System.currentTimeMillis();
            float d = cp.recurse(0, cp.points.size() - 1);
            long time = System.currentTimeMillis() - start;
            System.out.println(d + " (" + time + "ms)");

            //recursiveNew
            System.out.print("\trecursiveNew: ");
            start = System.currentTimeMillis();
            d = cp.recurseNew(0, cp.points.size() - 1);
            time = System.currentTimeMillis() - start;
            System.out.println(d + " (" + time + "ms)");

            //recursiveLR
            System.out.print("\trecursiveLR: ");
            start = System.currentTimeMillis();
            d = cp.recurseLR(0, cp.points.size() - 1);
            time = System.currentTimeMillis() - start;
            System.out.println(d + " (" + time + "ms)");

            System.out.println();
            //Ergebnisse in unserem Abgabe PDF
        }
    }
}
