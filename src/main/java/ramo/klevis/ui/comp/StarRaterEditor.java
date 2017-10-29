package ramo.klevis.ui.comp;

import ramo.klevis.ml.Movie;
import ramo.klevis.ui.RatingsTableModel;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * Created by klevis.ramo on 10/29/2017.
 */
public class StarRaterEditor extends AbstractCellEditor implements TableCellEditor {

    private final RatingsTableModel ratingsTableModel;
    private final StarRater starRater;
    private int row;

    public StarRaterEditor(RatingsTableModel ratingsTableModel) {

        this.ratingsTableModel = ratingsTableModel;
        starRater = new StarRater(5);
        starRater.addStarListener(selection -> {
            ratingsTableModel.getMovie(row).setRating(Double.valueOf(selection));
        });
    }

    @Override
    public Object getCellEditorValue() {

        return null;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Movie currentMovie = ratingsTableModel.getMovie(row);
        float rating = starRater.getRating();
        currentMovie.setRating(new Double(rating));
        this.row = row;
        return starRater;
    }
}
