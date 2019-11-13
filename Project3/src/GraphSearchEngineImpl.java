import java.util.*;

public class GraphSearchEngineImpl implements GraphSearchEngine {

    @Override
    public List<Node> findShortestPath(Node s, Node t) {
        Queue<Node> _nodesToVisit = new ArrayDeque<>();
        HashSet<Node> _visitedNodes = new HashSet<>();
        ArrayList<Node> _pathOfNodes = new ArrayList<>();

        _nodesToVisit.add(s); // Adds first node to nodesToVisit queue

        while(_nodesToVisit.size() > 0){ // While nodesToVisit is not empty
            Node temp = _nodesToVisit.remove(); // sets temp Node to first node in queue and removes
            _visitedNodes.add(temp); // Adds removed Node to list of visited nodes
            for(Node neighbor: temp.getNeighbors()){ // For all the neighbors of temp, add them to nodesToVisit if unseen
                if(!_nodesToVisit.contains(neighbor) && !_visitedNodes.contains(neighbor)){
                    _nodesToVisit.add(neighbor);
                }
            }
        }

        return null;
    }

}
