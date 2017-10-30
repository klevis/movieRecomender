package ramo.klevis.ml;

import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.sql.catalyst.expressions.In;
import scala.Int;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by klevis.ramo on 10/29/2017.
 */
public class PrepareData {

    private static final String ML_LATEST_SMALL_MOVIES_CSV = "data/movies.csv";
    private static final String ML_LATEST_SMALL_RATINGS_CSV = "data/ratings.csv";
    private final List<Movie> movies;
    private final List<Rating> ratings;

    public PrepareData() throws Exception {
        movies = readAllMovies();
        ratings = readUserRatings();
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public List<Movie> readAllMovies() throws Exception {
        return Files.readAllLines(getPath(ML_LATEST_SMALL_MOVIES_CSV))
                .stream().parallel().skip(1).map(line -> {
                    String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    return new Movie(values[0], values[1].replaceAll("\"", ""), values[2], 0d);
                }).collect(Collectors.toList());
    }

    public List<Rating> readUserRatings() throws Exception {
        return Files.readAllLines(getPath(ML_LATEST_SMALL_RATINGS_CSV))
                .stream().parallel().skip(1).map(line -> {
                    String[] values = line.split(",");
                    return new Rating(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Double.parseDouble(values[2]));
                }).collect(Collectors.toList());
    }


    public List<Movie> getNonRatedMovies() {
        Map<Integer, Integer> movieUser = ratings.stream().collect(Collectors.toMap(Rating::product, Rating::user));
        return movies.stream().parallel().filter(e -> movieUser.containsKey(Integer.parseInt(e.getId()))).collect(Collectors.toList());
    }

    public Collection<String> getAllGenres() {
        return movies.stream().flatMap(e ->
                Pattern.compile("\\|").splitAsStream(e.getGenre())
        ).distinct().sorted().collect(Collectors.toList());
    }

    public List<Movie> getMoviesByGenre(String genre) {
        return movies.stream().parallel().filter(e -> e.getGenre().equalsIgnoreCase(genre)).collect(Collectors.toList());
    }

    public List<Movie> getMovies() {
        return movies;
    }

    private Path getPath(String path) throws IOException, URISyntaxException {
        return getPath(this.getClass().getResource("/" + path).toURI());
    }

    private Path getPath(URI uri) throws IOException {
        Path start = null;
        try {
            start = Paths.get(uri);
        } catch (FileSystemNotFoundException e) {
            Map<String, String> env = new HashMap<>();
            env.put("create", "true");
            FileSystems.newFileSystem(uri, env);
            start = Paths.get(uri);
        }
        return start;
    }
}
