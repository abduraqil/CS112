package avengers;

import java.util.HashMap;

public class AdjacencyList2{

    private int[][] edges; 
    private int numberOfNodes;
    private HashMap<String, Integer> nodes;
    private String[] nodeIDs;
    private int currentNode = 0;

    public AdjacencyList2(int amountOfNodes) {
        edges = new int[amountOfNodes][amountOfNodes];
        nodes = new HashMap<String, Integer>();
        nodeIDs = new String[amountOfNodes];
        numberOfNodes = amountOfNodes;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public void setNode(String nodeId) {
        nodes.put(nodeId, currentNode);
        nodeIDs[currentNode] = nodeId;
        currentNode++;
    }

    public String[] getNodeEdges(String nodeId) {
        int index = nodes.get(nodeId);
        String[] result = new String[0];
        for (int j = 0; j < edges[index].length; j++) {
            if(edges[j][index] > 0){
                String[]temp = new String[result.length +1];
                for (int j2 = 0; j2 < result.length; j2++) {
                    temp[j2] = result[j2];
                }
                temp[temp.length -1] = nodeIDs[j];
                result = temp;
            }
        }
        return result;
    }

    public String[] getNodeEdges(int nodeId) {
        int index = nodeId;
        String[] result = new String[0];
        for (int j = 0; j < edges[index].length; j++) {
            if(edges[j][index] > 0){
                String[]temp = new String[result.length +1];
                for (int j2 = 0; j2 < result.length; j2++) {
                    temp[j2] = result[j2];
                }
                temp[temp.length -1] = nodeIDs[j];
                result = temp;
            }
        }
        return result;
    }
    
    public int getNode(String nodeId) {
        return nodes.get(nodeId);
    }
    
    public void setEdge(String n1, String n2, int weight) {
        edges[nodes.get(n1)][nodes.get(n2)] = weight;
    }

    public int getEdge(String n1, String n2) {
        return edges[nodes.get(n1)][nodes.get(n2)] ;
    }

    public int getEdge(int n1, int n2) {
        return edges[n1][n2] ;
    }

    private int minDistance(int[] dist, boolean[] sptSet) {
        int min = Integer.MAX_VALUE, min_index = -1;
 
        for (int v = 0; v < numberOfNodes; v++)
            if (sptSet[v] == false && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }
 
        return min_index;
    }

    public void printEdges() {
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges.length; j++) {
                System.out.print(edges[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int dijkstra() {
        int[] minCost = new int[numberOfNodes];
        boolean[] dijkstraSet = new boolean[numberOfNodes];
         for (int i = 0; i < numberOfNodes; i++) {
             if(i == 0) {
                 minCost[i] = 0;
             } else {
                 minCost[i] = Integer.MAX_VALUE;
             }
         }

         for (int i = 0; i < numberOfNodes -1; i++) {
              int currentSource = minDistance(minCost, dijkstraSet);

              dijkstraSet[currentSource] = true;

              for (int j = 0; j < numberOfNodes; j++) {
                if(!dijkstraSet[j] && 
                getEdge(currentSource, j) != 0 && 
                minCost[currentSource] != Integer.MAX_VALUE && 
                minCost[j] > (minCost[currentSource] + getEdge(currentSource, j))){
                    minCost[j] = (minCost[currentSource] + getEdge(currentSource, j));
                }
              }
         }
         return minCost[minCost.length-1];
    }

}
