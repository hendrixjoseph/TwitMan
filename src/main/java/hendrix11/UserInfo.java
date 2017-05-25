package hendrix11;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Callback;
import twitter4j.User;

import java.util.Date;

/**
 * Created by Joe on 5/8/2017.
 */
public class UserInfo {

    @FXML
    private TableView<TwitUser> userTable;

    @FXML
    private void initialize() {
        userTable.getColumns().forEach(this::setCellValueFactory);
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
