package hendrix11.wrapper;

import hendrix11.TwitMain;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Joe on 6/30/2017.
 */
public class TwitStatus extends TwitWrapper {
    private Status status;

    public static List<TwitStatus> listify(List<Status> statusList) {
        return statusList.stream().map(TwitStatus::new).collect(Collectors.toList());
    }


    public TwitStatus(Status status) {
        this.status = status;
    }

    public void retweet() {
        try {
            if (!status.isRetweetedByMe()) {
                TwitMain.getTwitter().retweetStatus(status.getId());
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public String getScreenName() {
        return "@" + status.getUser().getScreenName();
    }

    public TwitUser getUser() {
        return new TwitUser(status.getUser());
    }

    public String getText() {
        return status.getText().replaceAll("[\\r\\n]+"," ").trim();
    }

    public int getNumRetweets() {
        return status.getRetweetCount();
    }

    @Override
    public String getTwitterUrl() {
        return "https://twitter.com/" + status.getUser().getScreenName() + "/status/" + status.getId();
    }
}