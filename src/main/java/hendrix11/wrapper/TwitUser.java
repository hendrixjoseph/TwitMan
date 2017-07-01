package hendrix11.wrapper;

import twitter4j.Twitter;
import twitter4j.User;

/**
 * Created by Joe on 5/17/2017.
 */
public class TwitUser implements MasterWrapper {
    private static int count = 0;

    private User user;
    private int number;

    public TwitUser(User user) {
        number = count++;
        this.user = user;
    }

    public TwitUser(User user, int number) {
        this.number = count = number;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return user.getName();
    }

    public String getScreenName() {
        return "@" + user.getScreenName();
    }

    @Override
    public String getTwitterUrl() {
        return "https://twitter.com/" + user.getScreenName();
    }

    public long getId() {
        return user.getId();
    }

    public int getFollowersCount() {
        return user.getFollowersCount();
    }

    public int getFriendsCount() {
        return user.getFriendsCount();
    }

    public boolean isVerified() {
        return user.isVerified();
    }

    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return user.getDescription().replaceAll("[\\r\\n]+"," ").trim();
    }
}
