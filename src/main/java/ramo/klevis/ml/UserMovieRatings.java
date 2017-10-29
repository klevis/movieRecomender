package ramo.klevis.ml;

/**
 * Created by klevis.ramo on 10/29/2017.
 */
public class UserMovieRatings {
    private int userId;
    private int movieId;
    private int rate;

    public UserMovieRatings(int userId, int movieId, int rate) {
        this.userId = userId;
        this.movieId = movieId;
        this.rate = rate;
    }
}
