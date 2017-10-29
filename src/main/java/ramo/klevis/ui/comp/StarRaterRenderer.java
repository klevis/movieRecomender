package ramo.klevis.ui.comp;

import ramo.klevis.ml.Movie;
import ramo.klevis.ui.MovieTableModel;
import ramo.klevis.ui.comp.StarRater;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by klevis.ramo on 10/29/2017.
 */
public class StarRaterRenderer implements TableCellRenderer {

    private final MovieTableModel movieTableModel;
    private final StarRater starRater;

    public StarRaterRenderer(MovieTableModel movieTableModel) {
        this.movieTableModel = movieTableModel;
        starRater = new StarRater(5);

    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        Movie currentMovie = movieTableModel.getMovie(row);
        starRater.setRating(currentMovie.getRating().floatValue());
        return starRater;
    }
}
