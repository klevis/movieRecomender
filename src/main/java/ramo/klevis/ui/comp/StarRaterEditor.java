package ramo.klevis.ui.comp;

import ramo.klevis.ml.Movie;
import ramo.klevis.ui.MovieRatingsTableModel;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * Created by klevis.ramo on 10/29/2017.
 */
public class StarRaterEditor extends AbstractCellEditor implements TableCellEditor {

    private final MovieRatingsTableModel movieRatingsTableModel;
    private final StarRater starRater;
    private int row;

    public StarRaterEditor(MovieRatingsTableModel movieRatingsTableModel) {

        this.movieRatingsTableModel = movieRatingsTableModel;
        starRater = new StarRater(5);
        starRater.addStarListener(selection -> {
            movieRatingsTableModel.getMovie(row).setRating(Double.valueOf(selection));
        });
    }

    @Override
    public Object getCellEditorValue() {

        return null;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Movie currentMovie = movieRatingsTableModel.getMovie(row);
        float rating = starRater.getRating();
        currentMovie.setRating(new Double(rating));
        this.row = row;
        return starRater;
    }
}
