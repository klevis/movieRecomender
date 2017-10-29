package ramo.klevis.ml;

import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.sql.catalyst.expressions.In;
import scala.Int;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by klevis.ramo on 10/29/2017.
 */
public class PrepareData {

    private final List<Movie> movies;
    private final List<Rating> ratings;

    public PrepareData() throws IOException {
        movies = readAllMovies();
        ratings = readUserRatings();
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public List<Movie> readAllMovies() throws IOException {
        return Files.readAllLines(Paths.get("ml-latest-small/movies.csv"))
                .stream().parallel().skip(1).map(line -> {
                    String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    return new Movie(values[0], values[1].replaceAll("\"", ""), values[2], 0d);
                }).collect(Collectors.toList());
    }

    public List<Rating> readUserRatings() throws IOException {
        return Files.readAllLines(Paths.get("ml-latest-small/ratings.csv"))
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
}
