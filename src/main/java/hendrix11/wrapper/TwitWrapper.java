package hendrix11.wrapper;

import twitter4j.User;

import java.util.function.Function;

/**
 * Created by Joe on 7/1/2017.
 */
public abstract class TwitWrapper {

    private static Function<TwitUser, Boolean> isFollower;
    private static Function<TwitUser, Boolean> isFollowed;


    public static void setFollowerFunction(Function<TwitUser, Boolean> isFollower){
        TwitWrapper.isFollower = isFollower;
    }

    public static void setFollowedFunction(Function<TwitUser, Boolean> isFollowed){
        TwitWrapper.isFollowed = isFollowed;
    }

    public boolean isFollowed() {
        return isFollowed != null && isFollowed.apply(new TwitUser(getUser()));
    }

    public boolean isFollower() {
        return isFollower != null && isFollower.apply(new TwitUser(getUser()));
    }

    public abstract User getUser();
    public abstract String getTwitterUrl();
}
