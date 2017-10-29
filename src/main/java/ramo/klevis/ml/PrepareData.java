package ramo.klevis.ml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by klevis.ramo on 10/29/2017.
 */
public class PrepareData {

    private final List<Movie> movies;

    public List<Movie> readAllLines() throws IOException {
        return Files.readAllLines(Paths.get("ml-latest-small/movies.csv"))
                .stream().parallel().skip(1).map(line -> {
                    String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    return new Movie(values[0], values[1], values[2]);
                }).collect(Collectors.toList());
    }

    public PrepareData() throws IOException {
        movies = readAllLines();
    }

    public Collection<String> getAllGenres() {
        return movies.stream().flatMap(e ->
                Pattern.compile("\\|").splitAsStream(e.getGenre())
        ).distinct().sorted().collect(Collectors.toList());
    }

    public List<Movie> getMoviesByGenre(String genre) {
        return movies.stream().parallel().filter(e -> e.getGenre().equalsIgnoreCase(genre)).collect(Collectors.toList());
    }
}
