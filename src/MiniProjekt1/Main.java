package MiniProjekt1;


import java.io.File;

public class Main {
    //Config here
    static File file = new File("Data/Miniprojekt/Roehrentransportsystem.csv");

    public static void main(String[] args) {
        //give optimal Distribution over the 5 storages
        MaxFlowOpenAI.calcOptimalDistribution(file);
        //check if same # cycles without L2
        MaxFlowOpenAI.comparewithoutL2(file);
    }
}
