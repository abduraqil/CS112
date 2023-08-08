package avengers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdjacencyList {
    private int[][] edges; 
    private int numberOfNodes;
    private double[] nodes;
    private boolean[] alive;
    public AdjacencyList(int amountOfNodes) {
        edges = new int[amountOfNodes][amountOfNodes];
        alive = new boolean[amountOfNodes];
        for (int i = 0; i < alive.length; i++) {
            alive[i] = true;
        }
        nodes = new double[amountOfNodes];
        numberOfNodes = amountOfNodes;
    }

    public int getNumberOfNodes(){
        return numberOfNodes;
    }

    public void setNode(int nodeId, Double weight){
        nodes[nodeId] = weight;
    }

    public int[] getNodeEdges(int nodeId){
        int[] result = new int[0];
        for (int j = 0; j < edges[nodeId].length; j++) {
            if(edges[nodeId][j] > 0){
                int[]temp = new int[result.length +1];
                for (int j2 = 0; j2 < result.length; j2++) {
                    temp[j2] = result[j2];
                }
                temp[temp.length -1] = j;
                result = temp;
            }
        }
        return result;
    }

    public Double getNode(int nodeId){
        return nodes[nodeId];
    }
    
    public void setEdge(int n1, int n2, int weight){
        edges[n1][n2] = weight;
    }

    public int getEdge(int n1, int n2){
        return edges[n1][n2];
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

    public void printEdges(){
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
            if (i ==0) {
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

    public  ArrayList<ArrayList<Integer>> getPaths() {
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i < edges.length; i++) {
            ArrayList<ArrayList<Integer>> r = getPathsFrom(0, i);
            for (ArrayList<Integer> arrayList : r) {
                result.add(arrayList);
            }
        }

        return result;
    }

    public void removeNode(int nodeId) {
        for (int i = 0; i < numberOfNodes; i++) {
            edges[nodeId][i] = 0;
            edges[i][nodeId] = 0;
        }
        alive[nodeId] = false;

    }

    public ArrayList<ArrayList<Integer>> getPathsFrom(int source, int destination) {
        boolean[] beingVisited = new boolean[edges.length];
        List<Integer> currentPath = new ArrayList<>();
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();


        currentPath.add(source);
        dfs(source, destination, beingVisited, currentPath, result);
        return result;
    }

    private void dfs(int source, int destination, boolean[] beingVisited, List<Integer> currentPath,ArrayList<ArrayList<Integer>> result) {
        if (source == destination)
        {
            result.add(new ArrayList<Integer>(currentPath));
            return;
        }

        beingVisited[source] = true; 

        for (int i = 0; i < edges[source].length; i++) {
            if(edges[source][i] == 0){
                continue;
            }
            if (!beingVisited[i])
            {
                currentPath.add(i);
                dfs(i, destination, beingVisited, currentPath,result);
                currentPath.remove(currentPath.size() -1);
            }
        }

        beingVisited[source] = false;
    }
    
    public ArrayList<Integer> BFS(int start) {
        ArrayList<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[numberOfNodes];
        Arrays.fill(visited, false);
        List<Integer> q = new ArrayList<>();
        q.add(start);
        result.add(start);
        visited[start] = true;
 
        int vis;
        while (!q.isEmpty()) {
            vis = q.get(0);
 
    
            result.add(vis);

            q.remove(q.get(0));
 
            for(int i = 0; i < numberOfNodes; i++) {
                if (edges[vis][i] == 1 && (!visited[i])) {
                    q.add(i);
                    visited[i] = true;
                }
            }
        }
        return result;
    
    }
    public boolean isConnected(boolean isDirect){
        boolean isConn = true;
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = isDirect ? 0 : i; j < numberOfNodes; j++) {
                if (i == j || !alive[i] || !alive[j]) continue;
                ArrayList<Integer> res = this.BFS(i);
                
                if(!res.contains(j)) isConn = false;
            }
        }
        return isConn;
    }

}
