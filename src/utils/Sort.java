package utils;

import actors.ActorAwards;
import actors.ActorRating;
import fileio.ActionInputData;
import shows.PopularGenre;
import shows.ShowsRating;
import user.UsersRating;
import shows.Favorite;
import shows.LongestVideo;
import shows.MostViewedVideos;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public final class Sort {

    private Sort() {
    }

    public static Comparator<UsersRating> ascUsersRating = new Comparator<UsersRating>() {
        @Override
        public int compare(final UsersRating o1, final UsersRating o2) {
            return o1.getNumberOfRatings() - o2.getNumberOfRatings();
        }
    };

    public static Comparator<UsersRating> descUsersRating = new Comparator<UsersRating>() {
        @Override
        public int compare(final UsersRating o1, final UsersRating o2) {
            return o2.getNumberOfRatings() - o1.getNumberOfRatings();
        }
    };

    /**
     * Sort an ArrayList of users by their names
     * @param usersRatings      Sorted ArrayList by numbersOfRatings;
     * @param action            current action
     * @return                  An sorted ArrayList by numberOfRatings and
     *                          users name;
     */
    public static ArrayList<UsersRating> sortUsersName(final ArrayList<UsersRating> usersRatings,
                                                       final ActionInputData action) {
        for (int i = 0; i < usersRatings.size() - 1; i++) {
            UsersRating user1 = usersRatings.get(i);
            for (int j = i + 1; j < usersRatings.size(); j++) {
                user1 = usersRatings.get(i);
                UsersRating user2 = usersRatings.get(j);
                if (user1.getNumberOfRatings() == user2.getNumberOfRatings()) {
                    switch (action.getSortType()) {
                        case "asc":
                            if (user1.getName().compareTo(user2.getName()) > 0) {
                                Collections.swap(usersRatings, i, j);
                            }
                            break;
                        case "desc":
                            if (user1.getName().compareTo(user2.getName()) < 0) {
                                Collections.swap(usersRatings, i, j);
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    break;
                }
            }
        }
        return usersRatings;
    }

    public static Comparator<LongestVideo> ascLongest = new Comparator<LongestVideo>() {
        @Override
        public int compare(final LongestVideo o1, final LongestVideo o2) {
            return o1.getDuration() - o2.getDuration();
        }
    };

    public static Comparator<LongestVideo> descLongest = new Comparator<LongestVideo>() {
        @Override
        public int compare(final LongestVideo o1, final LongestVideo o2) {
            return o2.getDuration() - o1.getDuration();
        }
    };

    /**
     *
     * @param longestVideos         ArrayList of all movies that contains name and duration
     *                              which is already sorted by duration
     * @param type                  Sort type
     * @return  A list that will be sorted by both duration and name
     */
    public static ArrayList<LongestVideo> longestSortName(final ArrayList<LongestVideo>
                                                                  longestVideos,
                                                          final String type) {
        for (int i = 0; i < longestVideos.size() - 1; i++) {
            LongestVideo video1 = longestVideos.get(i);
            for (int j = i + 1; j < longestVideos.size(); j++) {
                video1 = longestVideos.get(i);
                LongestVideo video2 = longestVideos.get(j);
                if (video1.getDuration() == video2.getDuration()) {
                    switch (type) {
                        case "asc":
                            if (video1.getTitle().compareTo(video2.getTitle()) > 0) {
                                Collections.swap(longestVideos, i, j);
                            }
                            break;
                        case "desc":
                            if (video2.getTitle().compareTo(video1.getTitle()) < 0) {
                                Collections.swap(longestVideos, i, j);
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    break;
                }
            }
        }
        return longestVideos;
    }

    public static Comparator<MostViewedVideos> ascMostViewed = new Comparator<MostViewedVideos>() {
        @Override
        public int compare(final MostViewedVideos o1, final MostViewedVideos o2) {
            return o1.getNumberOfViews() - o2.getNumberOfViews();
        }
    };

    public static Comparator<MostViewedVideos> descMostViewed = new Comparator<MostViewedVideos>() {
        @Override
        public int compare(final MostViewedVideos o1, final MostViewedVideos o2) {
            return o2.getNumberOfViews() - o1.getNumberOfViews();
        }
    };
    /**
     *
     * @param mostViewedVideos          ArrayList of videos sorted by views
     * @param type                      Sort type
     * @return  A list that will be sorted by both views and name
     */
    public static ArrayList<MostViewedVideos> mostViewedSortName(
                                                final ArrayList<MostViewedVideos> mostViewedVideos,
                                                final String type) {
        for (int i = 0; i < mostViewedVideos.size() - 1; i++) {
            MostViewedVideos video1 = mostViewedVideos.get(i);
            for (int j = i + 1; j < mostViewedVideos.size(); j++) {
                video1 = mostViewedVideos.get(i);
                MostViewedVideos video2 = mostViewedVideos.get(j);
                if (video1.getNumberOfViews() == video2.getNumberOfViews()) {
                    switch (type) {
                        case "asc":
                            if (video1.getTitle().compareTo(video2.getTitle()) > 0) {
                                Collections.swap(mostViewedVideos, i, j);
                            }
                            break;
                        case "desc":
                            if (video1.getTitle().compareTo(video2.getTitle()) < 0) {
                                Collections.swap(mostViewedVideos, i, j);
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    break;
                }
            }
        }
        return mostViewedVideos;
    }

    public static Comparator<Favorite> ascFavorite = new Comparator<Favorite>() {
        @Override
        public int compare(final Favorite o1, final Favorite o2) {
            return o1.getNumberOfAppearances() - o2.getNumberOfAppearances();
        }
    };

    public static Comparator<Favorite> descFavorite = new Comparator<Favorite>() {
        @Override
        public int compare(final Favorite o1, final Favorite o2) {
            return o2.getNumberOfAppearances() - o1.getNumberOfAppearances();
        }
    };
    /**
     *
     * @param favoriteVideos        ArrayList sorted by number of appearances
     * @return  ArrayList sorted by both number of appearances and name
     */
    public static ArrayList<Favorite> favoritesName(final ArrayList<Favorite> favoriteVideos,
                                                       final String type) {
        for (int i = 0; i < favoriteVideos.size() - 1; i++) {
            Favorite favorite1 = favoriteVideos.get(i);
            for (int j = i + 1; j < favoriteVideos.size(); j++) {
                favorite1 = favoriteVideos.get(i);
                Favorite favorite2 = favoriteVideos.get(j);
                if (favorite1.getNumberOfAppearances() == favorite2.getNumberOfAppearances()) {
                    switch (type) {
                        case"asc":
                            if (favorite1.getTitle().compareTo(favorite2.getTitle()) > 0) {
                                Collections.swap(favoriteVideos, i, j);
                            }
                            break;
                        case "desc":
                            if (favorite1.getTitle().compareTo(favorite2.getTitle()) < 0) {
                                Collections.swap(favoriteVideos, i, j);
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    break;
                }
            }
        }
        return favoriteVideos;
    }
    public static Comparator<ActorAwards> ascActorsAwards = new Comparator<ActorAwards>() {
        @Override
        public int compare(final ActorAwards o1, final ActorAwards o2) {
            return o1.getNumberOfAwards() - o2.getNumberOfAwards();
        }
    };

    /**
     *
     * @param actorsAwards          List of actors sorted by number of awrads they recieved
     * @return  List of actors sorted by both number of awards and name ( ascending )
     */
    public static ArrayList<ActorAwards> awardsAscName(final ArrayList<ActorAwards> actorsAwards) {
        for (int i = 0; i < actorsAwards.size() - 1; i++) {
            ActorAwards actor1 = actorsAwards.get(i);
            for (int j = i + 1; j < actorsAwards.size(); j++) {
                actor1 = actorsAwards.get(i);
                ActorAwards actor2 = actorsAwards.get(j);
                if (actor1.getNumberOfAwards() == actor2.getNumberOfAwards()) {
                    if (actor1.getName().compareTo(actor2.getName()) > 0) {
                        Collections.swap(actorsAwards, i, j);
                    }
                } else {
                    break;
                }
            }
        }
        return actorsAwards;
    }

    public static Comparator<ActorAwards> descActorsAwards = new Comparator<ActorAwards>() {
        @Override
        public int compare(final ActorAwards o1, final ActorAwards o2) {
            return o2.getNumberOfAwards() - o1.getNumberOfAwards();
        }
    };
    /**
     *
     * @param actorsAwards          List of actors sorted by number of awrads they recieved
     * @return  List of actors sorted by both number of awards and name ( descendent )
     */
    public static ArrayList<ActorAwards> awardsDescName(final ArrayList<ActorAwards> actorsAwards) {
        for (int i = 0; i < actorsAwards.size() - 1; i++) {
            ActorAwards actor1 = actorsAwards.get(i);
            for (int j = i + 1; j < actorsAwards.size(); j++) {
                actor1 = actorsAwards.get(i);
                ActorAwards actor2 = actorsAwards.get(j);
                if (actor1.getNumberOfAwards() == actor2.getNumberOfAwards()) {
                    if (actor1.getName().compareTo(actor2.getName()) < 0) {
                        Collections.swap(actorsAwards, i, j);
                    }
                } else {
                    break;
                }
            }
        }
        return actorsAwards;
    }

    /**
     *
     * @param showsRatings      ArrayList of videos sorted by rating
     * @return  A list of videos sorted by rating and name ( ascending )
     */
    public static ArrayList<ActorRating> ratingAscName(final ArrayList<ActorRating> actorsRating) {
        for (int i = 0; i < actorsRating.size() - 1; i++) {
            ActorRating actor1 = actorsRating.get(i);
            for (int j = i + 1; j < actorsRating.size(); j++) {
                actor1 = actorsRating.get(i);
                ActorRating actor2 = actorsRating.get(j);
                if (actor1.getGrade().equals(actor2.getGrade())) {
                    if (actor1.getName().compareTo(actor2.getName()) > 0) {
                        Collections.swap(actorsRating, i, j);
                    }
                } else {
                    break;
                }
            }
        }
        return actorsRating;
    }

    /**
     *
     * @param showsRatings      ArrayList of videos sorted by rating
     * @return  A list of videos sorted by rating and name ( descendent )
     */
    public static ArrayList<ActorRating> ratingDescName(final ArrayList<ActorRating> actorsRating) {
        for (int i = 0; i < actorsRating.size() - 1; i++) {
            ActorRating actor1 = actorsRating.get(i);
            for (int j = i + 1; j < actorsRating.size(); j++) {
                actor1 = actorsRating.get(i);
                ActorRating actor2 = actorsRating.get(j);
                if (actor1.getGrade().equals(actor2.getGrade())) {
                    if (actor1.getName().compareTo(actor2.getName()) < 0) {
                        Collections.swap(actorsRating, i, j);
                    }
                } else {
                    break;
                }
            }
        }
        return actorsRating;
    }
    public static Comparator<ActorRating> descRating = new Comparator<ActorRating>() {
        @Override
        public int compare(final ActorRating o1, final ActorRating o2) {
            return o2.getGrade().compareTo(o1.getGrade());
        }
    };

    public static Comparator<ActorRating> ascRating = new Comparator<ActorRating>() {
        @Override
        public int compare(final ActorRating o1, final ActorRating o2) {
            return o1.getGrade().compareTo(o2.getGrade());
        }
    };

    public static Comparator<ShowsRating> descShowsRating = new Comparator<ShowsRating>() {
        @Override
        public int compare(final ShowsRating o1, final ShowsRating o2) {
            return o2.getGrade().compareTo(o1.getGrade());
        }
    };

    public static Comparator<ShowsRating> ascShowsRating = new Comparator<ShowsRating>() {
        @Override
        public int compare(final ShowsRating o1, final ShowsRating o2) {
            return o1.getGrade().compareTo(o2.getGrade());
        }
    };
    /**
     *
     * @param showsRatings      ArrayList of videos sorted by rating
     * @return  A list of videos sorted by rating and name ( ascending )
     */
    public static ArrayList<ShowsRating> ascShowsRating(final ArrayList<ShowsRating> showsRatings) {
        for (int i = 0; i < showsRatings.size() - 1; i++) {
            ShowsRating video1 = showsRatings.get(i);
            for (int j = i + 1; i < showsRatings.size(); j++) {
                video1 = showsRatings.get(i);
                ShowsRating video2 = showsRatings.get(j);
                if (video1.getGrade().equals(video2.getGrade())) {
                    if (video1.getTitle().compareTo(video2.getTitle()) > 0) {
                        Collections.swap(showsRatings, i, j);
                    }
                } else {
                    break;
                }
            }
        }
        return showsRatings;
    }

    public static Comparator<String> ascString = new Comparator<String>() {
        @Override
        public int compare(final String o1, final String o2) {
            return o1.compareTo(o2);
        }
    };

    public static Comparator<String> descString = new Comparator<String>() {
        @Override
        public int compare(final String o1, final String o2) {
            return o2.compareTo(o1);
        }
    };

    public static Comparator<PopularGenre> descPopular = new Comparator<PopularGenre>() {
        @Override
        public int compare(final PopularGenre o1, final PopularGenre o2) {
            return o2.getNumberOfViews() - o1.getNumberOfViews();
        }
    };
}
