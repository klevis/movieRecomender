package ramo.klevis.ui;

import ramo.klevis.ml.Movie;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MovieTableModel extends AbstractTableModel {

    protected List<Movie> movieList=new ArrayList<>();

    @Override
    public int getRowCount() {
        return movieList.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        String name = "??";
        switch (column) {
            case 0:
                name = "Title";
                break;
            case 1:
                name = "Genre";
                break;
        }
        return name;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class type = String.class;
        switch (columnIndex) {
            case 0:
            case 1:
                type = Integer.class;
                break;
        }
        return type;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Movie click = movieList.get(rowIndex);
        Object value = null;
        switch (columnIndex) {
            case 0:
                value = click.getTitle();
                break;
            case 1:
                value = click.getGenre();
                break;
        }
        return value;
    }

    public void restAndAddNewMovies(List<Movie> newMovies) {
        movieList.clear();
        movieList.addAll(newMovies);
    }
}