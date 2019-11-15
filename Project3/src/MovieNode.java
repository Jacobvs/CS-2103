import java.util.Collection;
import java.util.List;

public class MovieNode implements Node {

    private String _name;
    private List<ActorNode> _neighbors;

    public MovieNode(String name, List<ActorNode> neighbors) {
        this._name = name;
        this._neighbors = neighbors;
    }

    public void addNeighbor(ActorNode n){
        this._neighbors.add(n);
    }

    @Override
    public String getName() {
        return this._name;
    }

    @Override
    public Collection<? extends Node> getNeighbors() {
        return this._neighbors;
    }

}
