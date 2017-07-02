package hendrix11.controller;

import hendrix11.wrapper.TwitWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Joe on 6/30/2017.
 */
public class TableHolder<R extends TwitWrapper> {
    private TableView<R> table;
    private ObservableList<R> list = FXCollections.observableArrayList();

    public void setTable(TableView<R> table) {
        this.table = table;
        table.getColumns().forEach(this::setCellValueFactory);

        table.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {
                try {
                    Desktop.getDesktop().browse(new URI(table.getSelectionModel().getSelectedItem().getTwitterUrl()));
                } catch (URISyntaxException | IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void setCellValueFactory(TableColumn<R, ?> column) {
        column.setCellValueFactory(new PropertyValueFactory<>(column.getId()));
    }
}
