package ramo.klevis.ui.comp;

import ramo.klevis.ui.comp.StarRater;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by klevis.ramo on 10/29/2017.
 */
public class StarRaterRenderer implements TableCellRenderer {


    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        StarRater starRater = new StarRater(5);
        if (value != null && value instanceof Number) {
            starRater.setRating(((Number) value).floatValue());

        }
        return starRater;
    }
}
