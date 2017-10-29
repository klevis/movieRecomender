package ramo.klevis.ui;

import ramo.klevis.ml.Movie;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MovieRatingsTableModel extends AbstractTableModel {

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
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public String getColumnName(int column) {
        String name = "??";
        switch (column) {
            case 0:
                name = "Title";
                break;
            case 1:
                name = "Rating";
                break;
        }
        return name;
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Movie movie = movieList.get(rowIndex);
        Object value = null;
        switch (columnIndex) {
            case 0:
                value = movie.getTitle();
                break;
            case 1:
                value = movie.getRating();
                break;
        }
        return value;
    }

    public void restAndAddNewMovies(List<Movie> newMovies) {
        movieList.clear();
        movieList.addAll(newMovies);
    }

    public Movie getMovie(int row) {
       return movieList.get(row);
    }
}