import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Graph linkGraph = new Graph();

        Scanner in = new Scanner(System.in);
        int numberOfNodes = in.nextInt(); // the total number of nodes in the level, including the gateways
        int numberOfLinks = in.nextInt(); // the number of links
        int numberOfExitGateways = in.nextInt(); // the number of exit gateways

        for (int i = 0; i < numberOfLinks; i++) {
            int linkNode1 = in.nextInt(); // N1 and N2 defines a link between these nodes
            int linkNode2 = in.nextInt();

            linkGraph.addEdge(linkNode1, linkNode2);
        }

        int[] exitGateways = new int[numberOfExitGateways];
        for (int i = 0; i < numberOfExitGateways; i++) {
            exitGateways[i] = in.nextInt();  // the index of a gateway node
        }

        // game loop
        while (true) {
            int agentPosition = in.nextInt(); // The index of the node on which the Bobnet agent is positioned this turn

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            // Get the shortest paths to all gateways 
            List<List<Integer>> shortestPaths = new ArrayList<List<Integer>>();
            for (int exitGateway: exitGateways) {
                List<Integer> shortestPath = linkGraph.findShortestPath(agentPosition, exitGateway);
                shortestPaths.add(shortestPath);

                displayPath(shortestPath);  
            }

            // We don't always want the shortest one. Sometimes we may need to block another
            // since a node can connect to two exits
            int chosenId = getPrioritisedSever(shortestPaths);
            
            if (chosenId == -1) {
                chosenId = getShortestPathSever(shortestPaths);
            }
            
            List<Integer> chosenOne = shortestPaths.get(chosenId);
            String output = chosenOne.get(chosenOne.size() - 2) + " " + chosenOne.get(chosenOne.size() - 1);

            linkGraph.removeEdge(chosenOne.get(chosenOne.size() - 2), chosenOne.get(chosenOne.size() - 1));
            // Example: 0 1 are the indices of the nodes you wish to sever the link between
            System.out.println(output);
        }
    }

    private static int getPrioritisedSever(List<List<Integer>> shortestPaths) {
        int chosenId = -1;
        boolean severFound = false;

        for (int i = 0; i < shortestPaths.size(); i++) {
            List<Integer> currentPath = shortestPaths.get(i);

            // If the path is 2 long
            if (currentPath.size() == 3) {
                int penultimateNode = currentPath.get(currentPath.size()-2);

                int connectedExits = countConnectedExits(penultimateNode, shortestPaths);

                if (connectedExits == 2) {
                    chosenId = i;
                    severFound = true;
                    break;
                }
            }
        }

        return chosenId;
    }

     private static int getShortestPathSever(List<List<Integer>> shortestPaths) {
        int chosenId = -1;
        
        // Get the shortest one. The agent can reach here quicker
        int shortestPath = Integer.MAX_VALUE;
        for (int i = 0; i < shortestPaths.size(); i++) {
            if (shortestPaths.get(i).size() < shortestPath && shortestPaths.get(i).size() > 0) {
                chosenId = i;
                shortestPath = shortestPaths.get(i).size();
            }
        }

        return chosenId;
     }

    private static int countConnectedExits(int nodeId, List<List<Integer>> shortestPaths)
    {
        int count = 0;
        for (List<Integer> path: shortestPaths) {
            if (path.size() >= 3 && path.get(path.size()-2) == nodeId) {
                count++;
            }
        }
        return count;
    }

    private static void displayPath(List<Integer> path) {
        for (int i = 0; i < path.size(); i++) {
            System.err.print(path.get(i) + " ");
        }
        System.err.println();
    }
}

class Graph {
    private HashMap<Integer, List<Integer>> adjacencyList = new HashMap<Integer, List<Integer>>();

    public void addEdge(int node1, int node2) {
        adjacencyList.putIfAbsent(node1, new ArrayList<>());
        adjacencyList.get(node1).add(node2);

        adjacencyList.putIfAbsent(node2, new ArrayList<>());
        adjacencyList.get(node2).add(node1);
    }

    public void removeEdge(int node1, int node2) {
        if (adjacencyList.containsKey(node1)) {
            adjacencyList.get(node1).remove(Integer.valueOf(node2)); // This removes by object, not index
        }

        if (adjacencyList.containsKey(node2)) {
            adjacencyList.get(node2).remove(Integer.valueOf(node1)); // This removes by object, not index
        }
    }

    public List<Integer> getAdjVertices(int node) {
        return adjacencyList.get(node);
    }

    // Find the shortest path using a breadth first search
    public List<Integer> findShortestPath(int start, int end) {
        // Check that both nodes exist
        if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(end)) {
            return Collections.emptyList();
        }

        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> parentMap = new HashMap<>();
        Set<Integer> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);
        parentMap.put(start, null);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            // We've reached the target. Build the path
            if (current == end) {
                return buildPath(parentMap, start, end);
            }

            for (int neighbour : adjacencyList.get(current)) {
                if (!visited.contains(neighbour)) {
                    queue.add(neighbour);
                    visited.add(neighbour);
                    parentMap.put(neighbour, current);
                }
            }
        }

        return Collections.emptyList();
    }

    private List<Integer> buildPath(Map<Integer, Integer> parentMap, int start, int end) {
        List<Integer> path = new ArrayList<>();
        Integer current = end;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);
        return path;
    }
}