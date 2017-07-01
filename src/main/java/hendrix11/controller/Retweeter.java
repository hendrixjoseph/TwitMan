package hendrix11.controller;

import hendrix11.wrapper.TwitStatus;
import hendrix11.wrapper.TwitUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import twitter4j.*;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 6/30/2017.
 */
public class Retweeter extends TableHolder<TwitStatus> {
    @FXML
    private TableView<TwitStatus> tweetsTable;
    private ObservableList<TwitStatus> tweetsList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setTable(tweetsTable);
    }

    public void setQuery(Twitter twitter) throws TwitterException {
        Query query = new Query("hello");

        query.setResultType(Query.RECENT);
        query.setCount(10);
        QueryResult result = twitter.search(query);
        tweetsList.addAll(TwitStatus.listify(result.getTweets()));
        tweetsTable.setItems(tweetsList);
    }
}
