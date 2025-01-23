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
            }

            // Get the shortest one. The agent can reach here quicker
            int shortestId = -1;
            int shortestPath = Integer.MAX_VALUE;
            for (int i = 0; i < shortestPaths.size(); i++) {
                if (shortestPaths.get(i).size() < shortestPath && shortestPaths.get(i).size() > 0) {
                    shortestId = i;
                    shortestPath = shortestPaths.get(i).size();
                }
            }
            
            List<Integer> shortestOne = shortestPaths.get(shortestId);
            String output = shortestOne.get(0) + " " + shortestOne.get(1);

            linkGraph.removeEdge(shortestOne.get(0), shortestOne.get(1));
            // Example: 0 1 are the indices of the nodes you wish to sever the link between
            System.out.println(output);
        }
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