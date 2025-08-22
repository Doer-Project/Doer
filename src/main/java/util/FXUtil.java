package util;

import datastructures.CustomList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FXUtil {

    // Convert any CustomList<T> to ObservableList<T>
    public static <T> ObservableList<T> toObservableList(CustomList<T> list) {
        ObservableList<T> obsList = FXCollections.observableArrayList();
        for (T item : list) {
            obsList.add(item);
        }
        return obsList;
    }
}
