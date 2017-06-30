package hendrix11;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import twitter4j.User;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Joe on 5/8/2017.
 */
public class UserInfo {

    @FXML
    private Button verifiedFilterButton;
    @FXML
    private TableView<TwitUser> userTable;
    private ObservableList<TwitUser> userList = FXCollections.observableArrayList();

    public UserInfo() {

    }

    @FXML
    private void initialize() {
        userTable.getColumns().forEach(this::setCellValueFactory);

        userTable.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {
                try {
                    Desktop.getDesktop().browse(new URI(userTable.getSelectionModel().getSelectedItem().getTwitterUrl()));
                } catch (URISyntaxException | IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        verifiedFilterButton.setOnAction(e -> {
            FilteredList<TwitUser> filteredData = new FilteredList<>(userList);

            switch (verifiedFilterButton.getText()) {
                case "All":
                    verifiedFilterButton.setText("Verified");
                    break;
                case "Verified":
                    verifiedFilterButton.setText("Unverified");
                    break;
                case "Unverified":
                    verifiedFilterButton.setText("All");
                    break;
                default:
            }

            filteredData.setPredicate(user -> {


                switch (verifiedFilterButton.getText()) {
                    case "Verified":
                        return user.isVerified();
                    case "Unverified":
                        return !user.isVerified();
                    default:
                        return true;
                }
            });

            SortedList<TwitUser> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(userTable.comparatorProperty());
            userTable.setItems(sortedData);
        });
    }

    private void setCellValueFactory(TableColumn<TwitUser, ?> column) {
        column.setCellValueFactory(new PropertyValueFactory<>(column.getId()));
    }

    public void addUser(User user) {
        userList.add(new TwitUser(user));
        userTable.setItems(userList);
    }
}
