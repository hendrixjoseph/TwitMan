package hendrix11;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Joe on 4/27/2017.
 */
public class TwitMain extends Application {

    @FXML
    private TableView friendsTable;
    @FXML
    private VBox list;

    public static Twitter getTwitter(String key, String secret, String token, String tokenSecret) {

        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(key)
                .setOAuthConsumerSecret(secret)
                .setOAuthAccessToken(token)
                .setOAuthAccessTokenSecret(tokenSecret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }

    public static Twitter joesGithubBlog() throws IOException {
        Properties props = new Properties();
        FileInputStream in = new FileInputStream("joesgithubblog.properties");
        props.load(in);
        in.close();

        return getTwitter(props.getProperty("key"),
                props.getProperty("secret"),
                props.getProperty("token"),
                props.getProperty("tokenSecret"));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TwitMain.fxml"));

        Scene scene = new Scene(root, 1500, 500);
        stage.setTitle("Twitter Manager");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void initialize() throws IOException {
        Twitter twitter = joesGithubBlog();
        List<Long> followers = getFollowers(twitter);

        //FXMLLoader loader = new FXMLLoader(getClass().getResource("UserInfo.fxml"));
        //Parent userTable = loader.load();
        friendsTable
        UserInfo controller = friendsTable;//loader.getController();



        //followers.forEach(follower -> {
        for(long follower : followers) {
            try {
                User user = twitter.showUser(follower);
                controller.addUser(user);
            } catch (TwitterException e) {
                e.printStackTrace();
                break;
            }
        }//);

        //list.getChildren().add(userTable);
    }

    private List<Long> getFollowers(Twitter twitter) {
        long cursor2 = -1;
        IDs friends;
        List<Long> following = new ArrayList<>();
        try {
            //do {
                friends = twitter.getFriendsIDs(cursor2);
                for (long id : friends.getIDs()) {
                    following.add(id);
                }
            //} while ((cursor2 = friends.getNextCursor()) != 0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get friends' ids: " + te.getMessage());
        }

        return following.subList(0,10);
    }
}
