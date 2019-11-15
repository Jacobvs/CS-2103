import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class IMDBGraphImpl implements IMDBGraph {

    private HashMap<String, ActorNode> actorMap = new HashMap<>();
    private HashMap<String, MovieNode> movieMap = new HashMap<>();

    public IMDBGraphImpl(String actorsFilename, String actressesFilename) throws FileNotFoundException {
        final Scanner actorScanner = new Scanner(new File(actorsFilename), "ISO-8859-1");
        final Scanner actressScanner = new Scanner(new File(actressesFilename), "ISO-8859-1");

        parseData(actorScanner);
        parseData(actressScanner);
    }

    private void parseData(Scanner scanner) {
        boolean reachedStart = false, onlyTV = true;
        Queue<MovieNode> moviesToUpdate = new ArrayDeque<>();
        ArrayList<MovieNode> movies = new ArrayList<>();
        String actorName = "", movieName;
        MovieNode mn;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.equals("")) {
                if (reachedStart) {
                    String[] parts = line.split("\t");
                    if (!parts[0].equals(""))
                        actorName = parts[0];

                    for (int i = 1; i < parts.length; i++) {
                        if (parts[i] != null && parts[i].length() > 0) {
                            if (!(parts[i].charAt(0) == '"') && !parts[i].contains("(TV)")) {
                                onlyTV = false;
                                movieName = parts[i].split("\\)")[0] + ")";

                                if (movieMap.containsKey(movieName))
                                    mn = movieMap.get(movieName);
                                else {
                                    mn = new MovieNode(movieName, new ArrayList<>());
                                    movieMap.put(movieName, mn); }
                                movies.add(mn);
                                moviesToUpdate.add(mn);
                            } break;
                        }
                    }
                } if (line.equals("----\t\t\t------"))
                    reachedStart = true;
            } else {
                if (!onlyTV) { // create actor node then update all movies in queue with new actor info
                    ActorNode an = new ActorNode(actorName, movies);
                    actorMap.put(actorName, an);
                    while (!moviesToUpdate.isEmpty()) {
                        mn = moviesToUpdate.poll();
                        mn.addNeighbor(an);
                    }
                } onlyTV = true;
            }
        }
    }

    @Override
    public Collection<? extends Node> getActors() {
        return actorMap.values();
    }

    @Override
    public Collection<? extends Node> getMovies() {
        return movieMap.values();
    }

    @Override
    public Node getActor(String name) {
        return actorMap.get(name);
    }

    @Override
    public Node getMovie(String name) {
        return movieMap.get(name);
    }
}
