Input data format:
Needs to be csv, separated by semicolon. First entry is ignored.
First column and first row(except last column) contain vertex names.
Entries (i,j) contains the capacits of the edge from vertex(i,1) to vertex(1,j).
If the Entry cant be parsed, it is assumed to be 0.
Last colum contains the capacities of the vertexes themselfs.
Algo looks for the keyword "Kapazitaet: " followed by a number, else there is no capacity on the vertex.

Methods:
MaxFlowOpenAI has the following static methods:
calcOptimalDistribution: Takes in a CSV file of the listed above format and prints
the optimal distribution of presents to the console. The list of nodes to be considered Lager and the Zentrale is stored as a static fields in MaxFlowOpenAI and can be changed.

comparewithoutL2:
Compares the Solution with and without using the L2 storage. Prints findings in console.
You can change the storage to be tested without by changing the "lagerToCheckWithout" variable in MaxFlowOpenAI.

maxFlow: takes in CSV file with above format and returns a MaxFlowOpenAI object. This is the Graph object after the max flow calculations.
You can boolean select, if you want to compute that with or witout "L2" (or other) storage.

findUnusedPipes: Takes in a MaxFlowOpenAI object and prints the unused pipes to the console.
that is Pipes that have a flow of 0 and also a Capacity greater than 0 (we consider pipes with capacity 0 to not exist anyway).
