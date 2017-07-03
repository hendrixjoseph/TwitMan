package hendrix11.controller;

import hendrix11.TwitMain;
import hendrix11.wrapper.TwitStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.TwitterException;

import java.util.Arrays;

/**
 * Created by Joe on 6/30/2017.
 */
public class Retweeter extends TableHolder<TwitStatus> {
    @FXML
    private HBox refiner;
    @FXML
    private ChoiceBox<String> followedChoice;
    @FXML
    private ChoiceBox<String> followerChoice;
    @FXML
    private TextField includeText;
    @FXML
    private TextField excludeText;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<TwitStatus> tweetsTable;
    private ObservableList<TwitStatus> tweetsList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setTable(tweetsTable);
    }

    @FXML
    private void refine() {
        FilteredList<TwitStatus> filteredData = new FilteredList<>(tweetsList);

        filteredData.setPredicate(status -> {
            switch(followedChoice.getValue()) {
                case "Followed Only":
                    if(!status.isFollowed()) {
                        return false;
                    }
                    break;
                case "Not Followed Only":
                    if(status.isFollowed()) {
                        return false;
                    }
                    break;
            }

            switch(followerChoice.getValue()) {
                case "Followers Only":
                    if(!status.isFollower()) {
                        return false;
                    }
                    break;
                case "Non-Followers Only":
                    if(status.isFollower()) {
                        return false;
                    }
                    break;
            }

            String[] includes = includeText.getText().split(" ");
            String[] excludes = excludeText.getText().split(" ");
            boolean include = Arrays.stream(includes).anyMatch(string -> status.getText().contains(string));
            boolean exclude = Arrays.stream(excludes).anyMatch(string -> status.getText().contains(string));

            if(includeText.getText().length() > 0 && excludeText.getText().length() > 0) {
                return include && !exclude;
            } else if(includeText.getText().length() > 0) {
                assert excludeText.getText().length() <= 0;
                return include;
            } else if(excludeText.getText().length() > 0) {
                assert includeText.getText().length() <= 0;
                return !exclude;
            } else {
                assert includeText.getText().length() <= 0 && excludeText.getText().length() <= 0;
                return true;
            }

        });

        SortedList<TwitStatus> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tweetsTable.comparatorProperty());
        tweetsTable.setItems(sortedData);
    }

    @FXML
    private void search() {
        search(searchField.getText());
        refiner.setDisable(false);
    }

    private void search(String text) {
        try {
            Query query = new Query(text);

            query.setResultType(Query.RECENT);
            query.setCount(100);
            QueryResult result = TwitMain.getTwitter().search(query);

            tweetsList.clear();
            tweetsList.addAll(TwitStatus.listify(result.getTweets()));
            tweetsTable.setItems(tweetsList);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
