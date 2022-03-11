package user;

import fileio.ActionInputData;
import shows.RatedMovies;
import shows.RatedSerials;

import java.util.ArrayList;
import java.util.Map;

/**
 * Information about an user, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class User {
    /**
     * User's username
     */
    private final String username;
    /**
     * Subscription Type
     */
    private final String subscriptionType;
    /**
     * The history of the movies seen
     */
    private final Map<String, Integer> history;
    /**
     * Movies added to favorites
     */
    private final ArrayList<String> favoriteMovies;
    /**
     * Number of ratings given
     */
    private int ratings;

    public User(final String username, final String subscriptionType,
                final Map<String, Integer> history,
                final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.favoriteMovies = favoriteMovies;
        this.history = history;
    }

    public String getUsername() {
        return username;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public int getRatings() {
        return ratings;
    }


    /**
     * Add a video to favoriteMovies if it was viewd
     *
     * @param action
     * @return
     */
    public String favorite(final ActionInputData action) {
        if (this.getFavoriteMovies().contains(action.getTitle())) {
            return "error -> " + action.getTitle() + " is already in favourite list";
        } else {
            if (this.getHistory().containsKey(action.getTitle())) {
                this.getFavoriteMovies().add(action.getTitle());
                return "success -> " + action.getTitle() + " was added as favourite";
            } else {
                return "error -> " + action.getTitle() + " is not seen";
            }
        }
    }

    /**
     * If a video is already viewd, it will increase the video views
     * If a video is not viewd, it will be added to viewd ones with total views of 1.
     *
     * @param action
     * @return
     */
    public String view(final ActionInputData action) {
        if (this.getHistory().containsKey(action.getTitle())) {
            int views = this.getHistory().get(action.getTitle());
            this.getHistory().replace(action.getTitle(), views, views + 1);
            return "success -> " + action.getTitle() + " was viewed with total views of "
                    + (views + 1);
        } else {
            this.getHistory().put(action.getTitle(), 1);
            return "success -> " + action.getTitle() + " was viewed with total views of 1";
        }
    }

    /**
     * Add rating to a viewd movie
     *
     * @param action
     * @param ratedMovies list of already rated movies
     * @return
     */
    public String ratingMovie(final ActionInputData action,
                              final ArrayList<RatedMovies> ratedMovies) {
        for (RatedMovies ratedMovie : ratedMovies) {
            if (ratedMovie.getTitle().equals(action.getTitle())
                    && ratedMovie.getUsername().equals(action.getUsername())) {
                return "error -> " + action.getTitle() + " has been already rated";
            }
        }
        if (this.getHistory().containsKey(action.getTitle())) {
            ratedMovies.add(new RatedMovies(action.getUsername(), action.getTitle(),
                    action.getGrade()));
            this.ratings++;
            return "success -> " + action.getTitle() + " was rated with " + action.getGrade()
                    + " by " + action.getUsername();
        } else {
            return "error -> " + action.getTitle() + " is not seen";
        }
    }

    /**
     * Add a rating to viewd serial
     *
     * @param action
     * @param ratedSerials list of already rated serials
     * @return
     */
    public String ratingSerial(final ActionInputData action,
                               final ArrayList<RatedSerials> ratedSerials) {
        for (RatedSerials ratedSerial : ratedSerials) {
            if (ratedSerial.getTitle().equals(action.getTitle())
                    && ratedSerial.getSeason() == action.getSeasonNumber()
                    && ratedSerial.getUsername().equals(action.getUsername())) {
                return "error -> " + action.getTitle() + " has been already rated";
            }
        }
        if (this.getHistory().containsKey(action.getTitle())) {
            ratedSerials.add(new RatedSerials(action.getUsername(), action.getTitle(),
                    action.getSeasonNumber(), action.getGrade()));
            this.ratings++;
            return "success -> " + action.getTitle() + " was rated with " + action.getGrade()
                    + " by " + action.getUsername();
        } else {
            return "error -> " + action.getTitle() + " is not seen";
        }
    }
}
