package hendrix11;

import twitter4j.Twitter;
import twitter4j.User;

/**
 * Created by Joe on 5/17/2017.
 */
public class TwitUser {
    private static int count = 0;

    private User user;
    private int number;
    private String noNewlineDescription;

    public TwitUser(User user) {
        number = count++;
        this.user = user;
        noNewlineDescription = user.getDescription().replaceAll("[\\r\\n]+"," ").trim();
    }

    public User getUser() {
        return user;
    }

    public int getNumber() {
        return number;
    }

    public String getNoNewlineDescription() {
        return noNewlineDescription;
    }
}
