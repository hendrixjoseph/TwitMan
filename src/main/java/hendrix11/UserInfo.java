package hendrix11;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import twitter4j.User;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Joe on 5/8/2017.
 */
public class UserInfo {

    @FXML
    private TableView<TwitUser> userTable;

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
    }

    private void setCellValueFactory(TableColumn<TwitUser, ?> column) {
        column.setCellValueFactory(new PropertyValueFactory<>(column.getId()));
    }

    public void addUser(User user) {
        userTable.getItems().add(new TwitUser(user));

        user.getDescription();
        user.getCreatedAt();
    }
}
