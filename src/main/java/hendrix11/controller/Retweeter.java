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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Joe on 6/30/2017.
 */
public class Retweeter extends TableHolder<TwitStatus> implements Runnable {
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

    @FXML
    private void retweet() {
        new Thread(this).start();
    }

    @FXML
    private void scheduled() {
        Date morning = Date.from(LocalDate.now().plusDays(1).atTime(9,0).atZone(ZoneId.systemDefault()).toInstant());
        Date afternoon = Date.from(LocalDate.now().plusDays(1).atTime(13,0).atZone(ZoneId.systemDefault()).toInstant());
        Date evening = Date.from(LocalDate.now().plusDays(1).atTime(18,0).atZone(ZoneId.systemDefault()).toInstant());

        long day = 86_400_000;
        Timer timer = new Timer();

        Date[] times = {morning, afternoon, evening};

        Arrays.stream(times).forEach(time -> {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Retweeting at " + LocalDateTime.now());
                    search();
                    refine();
                    Retweeter.this.run();
                }
            };

            timer.schedule(task, time, day);
        });

        System.out.println("scheduled.");
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

    @Override
    public void run() {
        tweetsTable.getItems().forEach(TwitStatus::retweet);
    }
}
