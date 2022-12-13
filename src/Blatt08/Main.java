package Blatt08;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File dir = new File("Data/Daten08");
        String[] algos = {"Standard", "Bottleneck", "Capacity Scaling"};

        for (File file : dir.listFiles()) {
            System.out.println(file.getName());

            for (int algoType : new int[]{0,1,2}){
                System.out.println("\t"+algos[algoType]+":");
                MaxFlowOpenAI.maxFlow(file, algoType);
            }

        }
    }
}


/*
C:\Users\belas\.jdks\openjdk-18.0.2.1\bin\java.exe "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2022.2.1\lib\idea_rt.jar=60581:C:\Program Files\JetBrains\IntelliJ IDEA 2022.2.1\bin" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\belas\OneDrive\Documents\UNI\Semester 3\Effiziente Algorithmen\EffizienteAlgorithmen\out\production\EffizienteAlgorithmen" Blatt08.Main
100-p01-200000-1000000-20.txt
	Standard:
		Maximum flow: 1124505
		Time to compute max flow: 8ms
	Bottleneck:
		Maximum flow: 1124505
		Time to compute max flow: 4ms
	Capacity Scaling:
		Maximum flow: 1124505
		Time to compute max flow: 0ms
1000-p008-20000-100000-70.txt
	Standard:
		Maximum flow: 2325582
		Time to compute max flow: 172ms
	Bottleneck:
		Maximum flow: 2325582
		Time to compute max flow: 736ms
	Capacity Scaling:
		Maximum flow: 2325582
		Time to compute max flow: 282ms
500-p01-20000-100000-50.txt
	Standard:
		Maximum flow: 1521067
		Time to compute max flow: 35ms
	Bottleneck:
		Maximum flow: 1521067
		Time to compute max flow: 48ms
	Capacity Scaling:
		Maximum flow: 1521067
		Time to compute max flow: 38ms
500-p01-5000-10000-80.txt
	Standard:
		Maximum flow: 214283
		Time to compute max flow: 10ms
	Bottleneck:
		Maximum flow: 214283
		Time to compute max flow: 52ms
	Capacity Scaling:
		Maximum flow: 214283
		Time to compute max flow: 10ms
advogato-1000-1100.txt
	Standard:
		Maximum flow: 3098
		Time to compute max flow: 21ms
	Bottleneck:
		Maximum flow: 3098
		Time to compute max flow: 33ms
	Capacity Scaling:
		Maximum flow: 3098
		Time to compute max flow: 66ms
advogato-50-150.txt
	Standard:
		Maximum flow: 384
		Time to compute max flow: 17ms
	Bottleneck:
		Maximum flow: 384
		Time to compute max flow: 25ms
	Capacity Scaling:
		Maximum flow: 384
		Time to compute max flow: 37ms
econ-1000-1100.txt
	Standard:
		Maximum flow: 54487
		Time to compute max flow: 58ms
	Bottleneck:
		Maximum flow: 54487
		Time to compute max flow: 272ms
	Capacity Scaling:
		Maximum flow: 54487
		Time to compute max flow: 73ms
econ-50-150.txt
	Standard:
		Maximum flow: 4924
		Time to compute max flow: 15ms
	Bottleneck:
		Maximum flow: 4924
		Time to compute max flow: 254ms
	Capacity Scaling:
		Maximum flow: 4924
		Time to compute max flow: 17ms

Process finished with exit code 0
*/