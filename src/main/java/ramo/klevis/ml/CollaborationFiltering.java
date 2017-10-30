package ramo.klevis.ml;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.catalyst.expressions.In;
import scala.Tuple2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by klevis.ramo on 10/29/2017.
 */
public class CollaborationFiltering {

    private static final int CURRENT_USER_ID = 9999999;
    private JavaSparkContext sparkContext;
    private double mse;

    public List<Movie> train(List<Movie> currentMovies, int featureSize) throws Exception {
        if (sparkContext == null) {
            sparkContext = createSparkContext();
        }
        PrepareData prepareData = new PrepareData();
        List<Rating> ratingsList = prepareData.getRatings();
        List<Rating> ratedByCurrentUser = currentMovies.stream().parallel().filter(e -> e.getRating() > 0d).map(e ->
                new Rating(CURRENT_USER_ID, Integer.parseInt(e.getId()), e.getRating())).collect(Collectors.toList());
        ratingsList.addAll(ratedByCurrentUser);

        JavaRDD<Rating> ratings = sparkContext.parallelize(ratingsList);
        int rank = featureSize;
        int numIterations = 10;
        MatrixFactorizationModel model = ALS.train(JavaRDD.toRDD(ratings), rank, numIterations, 0.01);

        JavaRDD<Tuple2<Object, Object>> userProducts =
                ratings.map(r -> new Tuple2<>(r.user(), r.product()));


        JavaPairRDD<Tuple2<Integer, Integer>, Double> predictions = JavaPairRDD.fromJavaRDD(
                model.predict(JavaRDD.toRDD(userProducts)).toJavaRDD()
                        .map(r -> new Tuple2<>(new Tuple2<>(r.user(), r.product()), r.rating()))
        );
        JavaRDD<Tuple2<Double, Double>> ratesAndPreds = JavaPairRDD.fromJavaRDD(
                ratings.map(r -> new Tuple2<>(new Tuple2<>(r.user(), r.product()), r.rating())))
                .join(predictions).values();
        mse = ratesAndPreds.mapToDouble(pair -> {
            double err = pair._1() - pair._2();
            return err * err;
        }).mean();
        System.out.println("Mean Squared Error = " + mse);

        List<Movie> notRatedMovies = currentMovies.stream().parallel().filter(e -> e.getRating() == 0d).collect(Collectors.toList());

        JavaRDD<Tuple2<Object, Object>> map = sparkContext.parallelize(notRatedMovies).map(r -> new Tuple2<>(CURRENT_USER_ID, Integer.parseInt(r.getId())));
        List<Rating> predicted = model.predict(JavaRDD.toRDD(map)).toJavaRDD().collect().stream().parallel().sorted(Comparator.comparing(Rating::rating).reversed()).collect(Collectors.toList());

        Map<String, Movie> notRatedMoviesMap = notRatedMovies.stream().parallel().collect(Collectors.toMap(Movie::getId, movie -> movie));

        List<Movie> topTen = new ArrayList<>();
        for (int i = 0; i < 30 && !predicted.isEmpty(); i++) {

            Movie movie = notRatedMoviesMap.get("" + predicted.get(i).product());
            movie.setRating(predicted.get(i).rating());
            topTen.add(movie);
        }

        return topTen;

    }

    private JavaSparkContext createSparkContext() {
        SparkConf conf = new SparkConf().setAppName("Movie Recomender").setMaster("local[*]");
        return new JavaSparkContext(conf);
    }

    public double getMse() {
        return mse;
    }

}
