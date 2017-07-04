package hendrix11.wrapper;

import hendrix11.TwitMain;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joe on 5/17/2017.
 */
public class TwitUser extends TwitWrapper {
    private static int count = 0;

    private static Map<Long, User> userHash = new HashMap<>();

    private static RateLimitStatus rateLimitStatus;

    private long userId;
    private User user;
    private int number;

    public TwitUser(User user) {
        this.user = user;
        userId = user.getId();

        if(userHash.get(userId) == null) {
            userHash.put(userId, user);
        }
    }

    public TwitUser(long userId) {
        this(userId, count++);
    }

    public TwitUser(long userId, int number) {
        this.number = count = number;
        this.userId = userId;
        loadUser();
    }

    @Override
    public TwitUser getUser() {
        return this;
    }

    private User loadUser() {
        if(user == null) {
            if(userHash.get(userId) == null) {
                try {
                    if(rateLimitStatus == null || rateLimitStatus.getRemaining() > 0) {
                        user = TwitMain.getTwitter().showUser(userId);
                        rateLimitStatus = user.getRateLimitStatus();
                    }
                } catch (TwitterException e) {
                    e.printStackTrace();
                    rateLimitStatus = e.getRateLimitStatus();
                }
                userHash.put(userId, user);
            } else {
                user = userHash.get(userId);
            }
        }

        return user;
    }

    public String getName() {
        return loadUser() != null ? user.getName() : "loading...";
    }

    public String getScreenName() {
        return loadUser() != null ? "@" + user.getScreenName() : "loading...";
    }

    @Override
    public String getTwitterUrl() {
        return loadUser() != null ? "https://twitter.com/" + user.getScreenName() : "loading...";
    }

    public long getId() {
        return userId;
    }

    public int getFollowersCount() {
        return loadUser() != null ? user.getFollowersCount() : 0;
    }

    public int getFriendsCount() {
        return loadUser() != null ? user.getFriendsCount() : 0;
    }

    public boolean isVerified() {
        return loadUser() != null ? user.isVerified() : false;
    }

    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return loadUser() != null ? user.getDescription().replaceAll("[\\r\\n]+"," ").trim() : "loading...";
    }

    @Override
    public boolean equals(Object o) {
        return this == o ||
                (o != null && o instanceof TwitUser
                        && ((TwitUser)o).getId() == getId());
    }

    @Override
    public int hashCode() {
        return Long.hashCode(userId);
    }
}
