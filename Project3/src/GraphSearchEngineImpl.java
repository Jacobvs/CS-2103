import java.util.*;

public class GraphSearchEngineImpl implements GraphSearchEngine {

    @Override
    /**
     * Finds shortest path between two nodes by storing visited paths of the same length until the target node is found
     * Returns path when node is found, null otherwise
     */
    public List<Node> findShortestPath(Node s, Node t) {
        Queue<LinkedList<Node>> _visitedPaths = new ArrayDeque<LinkedList<Node>>(); // Stores paths that need to be searched
        HashSet<Node> _visitedNodes = new HashSet<Node>(); // Stores list of visited nodes

        LinkedList<Node> startList = new LinkedList<Node>(); // Starting with Queue containing list containing first node
        startList.add(s);

        _visitedPaths.add(startList); // Adds list containing first node to nodesToVisit queue

        while(_visitedPaths.size() > 0){ // While nodesToVisit is not empty
            LinkedList<Node> temp = _visitedPaths.remove(); // sets temp LinkedList to first LinkedList in queue and removes it

            //_visitedNodes.add(temp); // Adds removed Node to list of visited nodes

            for(Node neighbor: temp.getLast().getNeighbors()){ // For all the neighbors of temp, add them to nodesToVisit if unseen
                if(neighbor.equals(t)){
                    temp.add(neighbor);
                    return temp;
                }
                else{
                    if(!_visitedNodes.contains(neighbor)) { // !! look at later
                        _visitedNodes.add(neighbor); //adds neighbor to visited nodes
                        temp.add(neighbor);
                        _visitedPaths.add(temp);
                        temp.removeLast(); //This might be wrong, will the temp stored in _visitedPaths be changed?
                    }
                }

            }
        }
        return null; // !! maybe replace with throwing an exception when node is not found
    }

}
