import java.util.Collection;
import java.util.List;

public class ActorNode implements Node {

    private String _name;
    private List<MovieNode> _neighbors;

    public ActorNode(String name, List<MovieNode> neighbors){
        this._name = name;
        this._neighbors = neighbors;
    }

    public void addNeighbor(MovieNode n){
        this._neighbors.add(n);
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public Collection<? extends Node> getNeighbors() {
        return _neighbors;
    }

}
