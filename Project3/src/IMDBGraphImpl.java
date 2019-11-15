import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class IMDBGraphImpl implements IMDBGraph {

    private HashMap<String, ActorNode> actorMap = new HashMap<>();
    private HashMap<String, MovieNode> movieMap = new HashMap<>();

    public IMDBGraphImpl(String actorsFilename, String actressesFilename) throws IOException {
        final Scanner actorScanner = new Scanner(new File(actorsFilename), StandardCharsets.ISO_8859_1);
        final Scanner actressScanner = new Scanner(new File(actressesFilename), StandardCharsets.ISO_8859_1);

        parseData(actorScanner); // Parse Data for both actors and actresses
        parseData(actressScanner);
    }

    private void parseData(Scanner scanner) {
        boolean reachedStart = false, onlyTV = true; // Define variables
        Queue<MovieNode> moviesToUpdate = new ArrayDeque<>();
        ArrayList<MovieNode> movies = new ArrayList<>();
        String actorName = "", movieName;
        MovieNode mn;

        while (scanner.hasNextLine()) { // Loop through every line in file
            String line = scanner.nextLine();
            if (!line.equals("")) { // Blank lines between each actor after start of data
                if (reachedStart) {
                    String[] parts = line.split("\t"); // Split the line into an array at each tab
                    if (!parts[0].equals("")) // The first section of the line will either be the actor name or a tab if the line contains a movie
                        actorName = parts[0]; // save the actor name for subsequent lines

                    for (int i = 1; i < parts.length; i++) { // traverse the parts array
                        if (parts[i] != null && parts[i].length() > 0) { // If the part is the first (non-null) and non actor name part
                            if (!(parts[i].charAt(0) == '"') && !parts[i].contains("(TV)")) { // ensure the movie parsed is not a tv movie
                                onlyTV = false; // if we find at least one non-tv movie set boolean to save actor later
                                movieName = parts[i].split("\\)")[0] + ")"; //cut off everything after the movie name

                                if (movieMap.containsKey(movieName)) // check to see if movie has already been saved
                                    mn = movieMap.get(movieName); // if so don't add to movieMap
                                else {
                                    mn = new MovieNode(movieName, new ArrayList<>()); // if new movie, save to MovieMap
                                    movieMap.put(movieName, mn); }
                                movies.add(mn); // add to list of movies that
                                moviesToUpdate.add(mn);
                            } break;
                        }
                    }
                } if (line.equals("----\t\t\t------")) // Check for the start of the data if not reached yet
                    reachedStart = true;
            } else {
                if (!onlyTV) { // create actor node then update all movies in queue with new actor info
                    ActorNode an = new ActorNode(actorName, movies);
                    movies.clear(); // empty movie list
                    actorMap.put(actorName, an); // put new actor in actor map
                    while (!moviesToUpdate.isEmpty()) {
                        mn = moviesToUpdate.poll(); // add actor to all the movies they starred in and remove from queue
                        mn.addNeighbor(an);
                    }
                } onlyTV = true; // reset onlyTV actor boolean
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
