package hendrix11.controller;

import hendrix11.wrapper.TwitUser;
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
public class UserTable extends TableHolder<TwitUser> {

    @FXML
    private Button verifiedFilterButton;
    @FXML
    private TableView<TwitUser> userTable;
    private ObservableList<TwitUser> userList = FXCollections.observableArrayList();

    public UserTable() {

    }

    @FXML
    private void initialize() {
        setTable(userTable);

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

    public void addUser(User user) {
        userList.add(new TwitUser(user));
        userTable.setItems(userList);
    }

    public boolean containsUser(TwitUser user) {
        return userList.contains(user);
    }
}
