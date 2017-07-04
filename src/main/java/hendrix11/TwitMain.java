package hendrix11;

import hendrix11.controller.Retweeter;
import hendrix11.controller.UserTable;
import hendrix11.wrapper.TwitUser;
import hendrix11.wrapper.TwitWrapper;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by Joe on 4/27/2017.
 */
public class TwitMain extends Application {

    private static Twitter twitter;

    public static Twitter getTwitter() {
        return twitter;
    }

    @FXML
    private UserTable followingTableController;

    @FXML
    private UserTable followerTableController;

    @FXML
    private Retweeter retweeterController;

    private static Twitter getTwitter(String key, String secret, String token, String tokenSecret) {

        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(key)
                .setOAuthConsumerSecret(secret)
                .setOAuthAccessToken(token)
                .setOAuthAccessTokenSecret(tokenSecret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }

    private static Twitter getTwitter(String propertiesFile) throws IOException {
        Properties props = new Properties();
        FileInputStream in = new FileInputStream(propertiesFile);
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
    private void initialize() throws IOException, TwitterException {
        twitter = getTwitter("puppysnuggles.properties");
        List<Long> followers = getFollowers();
        List<Long> following = getFollowing();

        followers.forEach(follower -> {
                followerTableController.addUser(new TwitUser(follower));
        });

        following.forEach(follower -> {
                followingTableController.addUser(new TwitUser(follower));
        });

        TwitWrapper.setFollowedFunction(followingTableController::containsUser);
        TwitWrapper.setFollowerFunction(followerTableController::containsUser);
    }

    private List<Long> getFollowers() {
        long cursor2 = -1;
        IDs friends;
        List<Long> following = new ArrayList<>();
        try {
            do {
            friends = twitter.getFollowersIDs(cursor2);
            for (long id : friends.getIDs()) {
                following.add(id);
            }
            } while ((cursor2 = friends.getNextCursor()) != 0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get friends' ids: " + te.getMessage());
        }

        return following;
    }

    private List<Long> getFollowing() {
        long cursor2 = -1;
        IDs friends;
        List<Long> following = new ArrayList<>();
        try {
            do {
                friends = twitter.getFriendsIDs(cursor2);

                for (long id : friends.getIDs()) {
                    following.add(id);
                }
            } while ((cursor2 = friends.getNextCursor()) != 0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get friends' ids: " + te.getMessage());
        }

        return following;//.subList(0,10);
    }
}
