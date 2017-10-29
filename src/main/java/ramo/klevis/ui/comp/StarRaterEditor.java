package ramo.klevis.ui.comp;

import ramo.klevis.ui.comp.StarRater;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * Created by klevis.ramo on 10/29/2017.
 */
public class StarRaterEditor extends AbstractCellEditor implements TableCellEditor {

    @Override
    public Object getCellEditorValue() {

        return null;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        StarRater starRater = new StarRater(5);
        if (value != null && value instanceof Number) {
            starRater.setRating(((Number) value).floatValue());

        }
        return starRater;
    }
}
