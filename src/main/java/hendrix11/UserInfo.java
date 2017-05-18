package hendrix11;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import twitter4j.User;

import java.util.Date;

/**
 * Created by Joe on 5/8/2017.
 */
public class UserInfo {

    @FXML
    private TableColumn<TwitUser, Boolean> verified;
    @FXML
    private TableColumn<TwitUser, Number> followers;
    @FXML
    private TableColumn<TwitUser, Number> following;
    @FXML
    private TableColumn<TwitUser, Number> userId;
    @FXML
    private TableColumn<TwitUser, Number> count;
    @FXML
    private TableColumn<TwitUser, String> description;
    @FXML
    private TableView<TwitUser> userTable;
    @FXML
    private TableColumn<TwitUser, String> screenName;
    @FXML
    private TableColumn<TwitUser, String> userName;


    @FXML
    private void initialize() {
        verified.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue().getUser().isVerified()));
        followers.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getUser().getFollowersCount()));
        following.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getUser().getFriendsCount()));
        count.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getNumber()));
        userId.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getUser().getId()));
        screenName.setCellValueFactory(param -> new SimpleStringProperty("@" + param.getValue().getUser().getScreenName()));
        userName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getUser().getName()));
        description.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNoNewlineDescription()));

    }

    public void addUser(User user) {
        userTable.getItems().add(new TwitUser(user));

        user.getDescription();
        user.getCreatedAt();
    }
}
