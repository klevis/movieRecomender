package ramo.klevis.ml;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import scala.Tuple2;

import java.io.IOException;
import java.util.List;

/**
 * Created by klevis.ramo on 10/29/2017.
 */
public class CollaborationFiltering {

    private JavaSparkContext sparkContext;

    public void train() throws IOException {
        if (sparkContext == null) {
            sparkContext = createSparkContext();
        }
        PrepareData prepareData = new PrepareData();
        List<Rating> ratings1 = prepareData.getRatings();
        JavaRDD<Rating> ratings = sparkContext.parallelize(ratings1);
        int rank = 100;
        int numIterations = 10;
        MatrixFactorizationModel model = ALS.train(JavaRDD.toRDD(ratings), rank, numIterations,0.01);

        JavaRDD<Tuple2<Object, Object>> userProducts =
                ratings.map(r -> new Tuple2<>(r.user(), r.product()));


        JavaPairRDD<Tuple2<Integer, Integer>, Double> predictions = JavaPairRDD.fromJavaRDD(
                model.predict(JavaRDD.toRDD(userProducts)).toJavaRDD()
                        .map(r -> new Tuple2<>(new Tuple2<>(r.user(), r.product()), r.rating()))
        );
        JavaRDD<Tuple2<Double, Double>> ratesAndPreds = JavaPairRDD.fromJavaRDD(
                ratings.map(r -> new Tuple2<>(new Tuple2<>(r.user(), r.product()), r.rating())))
                .join(predictions).values();
        double MSE = ratesAndPreds.mapToDouble(pair -> {
            double err = pair._1() - pair._2();
            return err * err;
        }).mean();
        System.out.println("Mean Squared Error = " + MSE);

    }

    private JavaSparkContext createSparkContext() {
        SparkConf conf = new SparkConf().setAppName("Movie Recomender").setMaster("local[*]");
        return new JavaSparkContext(conf);
    }

}
