package ramo.klevis.ui.comp;

import ramo.klevis.ml.Movie;
import ramo.klevis.ui.MovieRatingsTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by klevis.ramo on 10/29/2017.
 */
public class StarRaterRenderer implements TableCellRenderer {

    private final MovieRatingsTableModel movieRatingsTableModel;
    private final StarRater starRater;

    public StarRaterRenderer(MovieRatingsTableModel movieRatingsTableModel) {
        this.movieRatingsTableModel = movieRatingsTableModel;
        starRater = new StarRater(5);

    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        Movie currentMovie = movieRatingsTableModel.getMovie(row);
        starRater.setRating(currentMovie.getRating().floatValue());
        return starRater;
    }
}
