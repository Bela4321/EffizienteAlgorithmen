package MiniProjekt1;


import java.io.File;

public class Main {

    static File file = new File("Data/Miniprojekt/Roehrentransportsystem.csv");

    public static void main(String[] args) {
        //give optimal Distribution over the 5 storages
        MaxFlowOpenAI.calcOptimalDistribution(file);
        //check if same # cycles without L2
        MaxFlowOpenAI.comparewithoutL2(file);

        //find unusedPipes with L2
        System.out.println("Unused Pipes with L2:");
        MaxFlowOpenAI graph1 = MaxFlowOpenAI.maxFlow(file, false);
        MaxFlowOpenAI.findUnusedPipes(graph1);

        //find unusedPipes without L2
        System.out.println("Unused Pipes without L2:");
        MaxFlowOpenAI graph2 = MaxFlowOpenAI.maxFlow(file, true);
        MaxFlowOpenAI.findUnusedPipes(graph2);
    }
}
