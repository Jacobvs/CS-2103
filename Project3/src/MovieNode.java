import java.util.ArrayList;
import java.util.Collection;

public class MovieNode implements Node {

    private String _name;
    private ArrayList<ActorNode> _neighbors;

    public MovieNode(String name, ArrayList<ActorNode> neighbors) {
        this._name = name;
        this._neighbors = neighbors;
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
