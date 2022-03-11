package utils;

import actors.Actor;
import actors.ActorAwards;
import actors.ActorRating;
import entertainment.Season;
import actor.ActorsAwards;
import common.Constants;
import entertainment.Genre;
import fileio.ActionInputData;
import shows.PopularGenre;
import shows.ShowsRating;
import user.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import shows.AverageMovies;
import shows.AverageSerials;
import shows.Favorite;
import shows.LongestVideo;
import shows.MostViewedVideos;
import shows.Movie;
import shows.RatedMovies;
import shows.RatedSerials;
import shows.Serial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * The class contains static methods that helps with parsing.
 * <p>
 * We suggest you add your static methods here or in a similar class.
 */
public final class Utils {
    /**
     * for coding style
     */
    private Utils() {
    }

    /**
     * Transforms a string into an enum
     *
     * @param genre of video
     * @return an Genre Enum
     */
    public static Genre stringToGenre(final String genre) {
        return switch (genre.toLowerCase()) {
            case "action" -> Genre.ACTION;
            case "adventure" -> Genre.ADVENTURE;
            case "drama" -> Genre.DRAMA;
            case "comedy" -> Genre.COMEDY;
            case "crime" -> Genre.CRIME;
            case "romance" -> Genre.ROMANCE;
            case "war" -> Genre.WAR;
            case "history" -> Genre.HISTORY;
            case "thriller" -> Genre.THRILLER;
            case "mystery" -> Genre.MYSTERY;
            case "family" -> Genre.FAMILY;
            case "horror" -> Genre.HORROR;
            case "fantasy" -> Genre.FANTASY;
            case "science fiction" -> Genre.SCIENCE_FICTION;
            case "action & adventure" -> Genre.ACTION_ADVENTURE;
            case "sci-fi & fantasy" -> Genre.SCI_FI_FANTASY;
            case "animation" -> Genre.ANIMATION;
            case "kids" -> Genre.KIDS;
            case "western" -> Genre.WESTERN;
            case "tv movie" -> Genre.TV_MOVIE;
            default -> null;
        };
    }

    /**
     * Transforms a string into an enum
     *
     * @param award for actors
     * @return an ActorsAwards Enum
     */
    public static ActorsAwards stringToAwards(final String award) {
        return switch (award) {
            case "BEST_SCREENPLAY" -> ActorsAwards.BEST_SCREENPLAY;
            case "BEST_SUPPORTING_ACTOR" -> ActorsAwards.BEST_SUPPORTING_ACTOR;
            case "BEST_DIRECTOR" -> ActorsAwards.BEST_DIRECTOR;
            case "BEST_PERFORMANCE" -> ActorsAwards.BEST_PERFORMANCE;
            case "PEOPLE_CHOICE_AWARD" -> ActorsAwards.PEOPLE_CHOICE_AWARD;
            default -> null;
        };
    }

    /**
     * Transforms an array of JSON's into an array of strings
     *
     * @param array of JSONs
     * @return a list of strings
     */
    public static ArrayList<String> convertJSONArray(final JSONArray array) {
        if (array != null) {
            ArrayList<String> finalArray = new ArrayList<>();
            for (Object object : array) {
                finalArray.add((String) object);
            }
            return finalArray;
        } else {
            return null;
        }
    }

    /**
     * Transforms an array of JSON's into a map
     *
     * @param jsonActors array of JSONs
     * @return a map with ActorsAwardsa as key and Integer as value
     */
    public static Map<ActorsAwards, Integer> convertAwards(final JSONArray jsonActors) {
        Map<ActorsAwards, Integer> awards = new LinkedHashMap<>();

        for (Object iterator : jsonActors) {
            awards.put(stringToAwards((String) ((JSONObject) iterator).get(Constants.AWARD_TYPE)),
                    Integer.parseInt(((JSONObject) iterator).get(Constants.NUMBER_OF_AWARDS)
                            .toString()));
        }

        return awards;
    }

    /**
     * Transforms an array of JSON's into a map
     *
     * @param movies array of JSONs
     * @return a map with String as key and Integer as value
     */
    public static Map<String, Integer> watchedMovie(final JSONArray movies) {
        Map<String, Integer> mapVideos = new LinkedHashMap<>();

        if (movies != null) {
            for (Object movie : movies) {
                mapVideos.put((String) ((JSONObject) movie).get(Constants.NAME),
                        Integer.parseInt(((JSONObject) movie).get(Constants.NUMBER_VIEWS)
                                .toString()));
            }
        } else {
            System.out.println("NU ESTE VIZIONAT NICIUN FILM");
        }

        return mapVideos;
    }

    /**
     * Obtain an array with actors and their ratings
     *
     * @param ratedMovies           ArrayList with RatedMovies
     * @param ratedSerials          ArrayList with RatedSerials
     * @param serials               ArrayList with all serials
     * @param actors                ArrayList with all actors
     * @return an array with actors and their ratings
     */
        public static ArrayList<ActorRating> getActorsRatings(final ArrayList<RatedMovies> ratedMovies,
                                                          final ArrayList<RatedSerials>
                                                                  ratedSerials,
                                                          final ArrayList<Serial> serials,
                                                          final ArrayList<Actor> actors) {

        ArrayList<ActorRating> actorsRating = new ArrayList<>();
        ArrayList<ShowsRating> showsAverage =
                Utils.getShowsRating(ratedMovies, ratedSerials, serials);

        for (Actor actor : actors) {
            for (String title : actor.getFilmography()) {
                for (ShowsRating showsRating : showsAverage) {
                    if (showsRating.getTitle().equals(title)) {
                        actor.addRating(showsRating.getGrade());
                        break;
                    }
                }
            }
        }
        for (Actor actor : actors) {
            if (actor.getRating() != 0) {
                actorsRating.add(new ActorRating(actor.getName(), actor.getRating(),
                        actor.getCounterGrade()));
            }
        }
        for (ActorRating actorRating : actorsRating) {
            actorRating.finalGrade();
        }
        return actorsRating;
    }

    /**
     * @param action which contains the list of awards
     * @param actors list of actors
     * @return list of actors who got all awards from list of awards
     */
    public static ArrayList<ActorAwards> getActorsAwards(final ActionInputData action,
                                                         final ArrayList<Actor> actors) {
        ArrayList<ActorAwards> actorsAwards = new ArrayList<>();
        int numberOfAwards = action.getFilters().get(3).size();
        List<String> awards = action.getFilters().get(3);
        for (Actor actor : actors) {
            int awardsRecieved = 0;
            int counter = 0;
            for (String award : awards) {
                if (actor.getAwards()
                        .containsKey(ActorsAwards.valueOf(award))) {
                    counter++;
                }
            }

            if (counter == numberOfAwards) {
                for (Map.Entry<ActorsAwards, Integer> entry : actor
                        .getAwards().entrySet()) {
                    awardsRecieved += entry.getValue();
                }
                actorsAwards.add(new ActorAwards(actor.getName(),
                        awardsRecieved));
            }
        }
        return actorsAwards;
    }

    /**
     * Calculate the final rating of each movie/serial
     *
     * @param ratedMovies           ArrayList with rated movies
     * @param ratedSerials          ArrayList with rated serials
     * @param serials               ArrayList with all serials
     * @return  a list of videos which contains all the movies/serials with
     *          their final ratings
     */
    public static ArrayList<ShowsRating> getShowsRating(final ArrayList<RatedMovies> ratedMovies,
                                                        final ArrayList<RatedSerials> ratedSerials,
                                                        final ArrayList<Serial> serials) {
        ArrayList<AverageMovies> averageMovies = new ArrayList<>();
        ArrayList<AverageSerials> averageSerials = new ArrayList<>();
        ArrayList<ShowsRating> showsAverage = new ArrayList<>();
        for (RatedMovies ratedMovie : ratedMovies) {
            int ok = 0;
            for (AverageMovies averageMovie : averageMovies) {
                if (ratedMovie.getTitle().equals(averageMovie.getTitle())) {
                    averageMovie.addGrade(ratedMovie.getGrade());
                    ok = 1;
                }
            }
            if (ok == 0) {
                averageMovies
                            .add(new AverageMovies(ratedMovie.getTitle(), ratedMovie.getGrade(), 1));
            }
        }

        for (AverageMovies averageMovie : averageMovies) {
            averageMovie.finalGrade();
            showsAverage.add(new ShowsRating(averageMovie.getTitle(), averageMovie.getFinalGrade(),
                    "Movie"));
        }
        for (Serial serial : serials) {
            for (RatedSerials ratedSerial : ratedSerials) {
                if (ratedSerial.getTitle().equals(serial.getTitle())) {
                    ratedSerial.setNumberOfSeasons(serial.getNumberSeason());
                }
            }
        }
        for (RatedSerials ratedSerial : ratedSerials) {
            int ok = 0;
            for (AverageSerials averageSerial : averageSerials) {
                if (ratedSerial.getTitle().equals(averageSerial.getTitle())) {
                    averageSerial.addGrade(ratedSerial.getGrade(), ratedSerial.getSeason());
                    ok = 1;
                }
            }
            if (ok == 0) {
                averageSerials.add(new AverageSerials(ratedSerial.getTitle(),
                        ratedSerial.getNumberOfSeasons(),
                        ratedSerial.getSeason(), ratedSerial.getGrade()));
            }
        }
        for (AverageSerials averageSerial : averageSerials) {
            averageSerial.finalGrade();
            showsAverage
                    .add(new ShowsRating(averageSerial.getTitle(), averageSerial.getFinalGrade(),
                            "Serial"));
        }
        return showsAverage;
    }

    /**
     * @param showsRating       ArrayList with all rated shows
     * @param movies            ArrayList with all movies
     * @param serials           ArrayList with all serials
     * @param action            current action
     * @param type              movie/serial
     * @return a list contains rated movies/serials filtered by year and genre
     */
    public static ArrayList<ShowsRating> getRating(final ArrayList<ShowsRating> showsRating,
                                                   final ArrayList<Movie> movies,
                                                   final ArrayList<Serial> serials,
                                                   final ActionInputData action,
                                                   final String type) {
        ArrayList<ShowsRating> finalShowsRating = new ArrayList<>();
        String year = action.getFilters().get(0).get(0);
        String genre = action.getFilters().get(1).get(0);


        switch (type) {
            case "Movie":
                for (ShowsRating showRating : showsRating) {
                    if (showRating.getType().equals("Movie")) {
                        for (Movie movie : movies) {
                            if (showRating.getTitle()
                                    .equals(movie.getTitle())) {
                                if (year == null) {
                                    if (movie.getGenres().contains(genre)) {
                                        finalShowsRating.add(showRating);
                                        break;
                                    }
                                }
                                if (genre == null) {
                                    if (year.equals(String.valueOf(movie.getYear()))) {
                                        finalShowsRating.add(showRating);
                                        break;
                                    }
                                }
                                if (movie.getGenres().contains(genre)
                                        && year.equals(String.valueOf(
                                                movie.getYear()))) {
                                    finalShowsRating.add(showRating);
                                }
                                break;
                            }
                        }
                    }
                }
                break;
            case "Serial":
                for (ShowsRating showRating : showsRating) {
                    if (showRating.getType().equals("Serial")) {
                        for (Serial serial : serials) {
                            if (showRating.getTitle()
                                    .equals(serial.getTitle())) {
                                if (year == null) {
                                    if (serial.getGenres().contains(genre)) {
                                        finalShowsRating.add(showRating);
                                        break;
                                    }
                                }
                                if (genre == null) {
                                    if (year.equals(String.valueOf(serial.getYear()))) {
                                        finalShowsRating.add(showRating);
                                        break;
                                    }
                                }
                                if (serial.getGenres().contains(genre)
                                        && year.equals(String.valueOf(
                                                serial.getYear()))) {
                                    finalShowsRating.add(showRating);
                                }
                                break;
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
        return finalShowsRating;
    }

    /**
     * @param users   ArrayList with all users
     * @param movies  ArrayList with all movies
     * @param serials ArrayList with all serials
     * @param action  current action
     * @param type    movie/serial
     * @return a list of favorite movies/serials filtered by year and genre
     */
    public static ArrayList<Favorite> getFavorite(final ArrayList<User> users,
                                                  final ArrayList<Movie> movies,
                                                  final ArrayList<Serial> serials,
                                                  final ActionInputData action,
                                                  final String type) {
        ArrayList<Favorite> favoriteVideos = new ArrayList<>();
        ArrayList<Favorite> finalFavorite = new ArrayList<>();
        String year = action.getFilters().get(0).get(0);
        String genre = action.getFilters().get(1).get(0);
        for (User user : users) {
            for (String favorite : user.getFavoriteMovies()) {
                int ok = 0;
                for (Favorite favoriteVideo : favoriteVideos) {
                    if (favoriteVideo.getTitle().equals(favorite)) {
                        favoriteVideo.add();
                        ok = 1;
                        break;
                    }
                }
                if (ok == 0) {
                    favoriteVideos.add(new Favorite(favorite, 1));
                }
            }
        }
        switch (type) {
            case "Movie":
                for (Favorite favoriteVideo : favoriteVideos) {
                    for (Movie movie : movies) {
                        if (favoriteVideo.getTitle().equals(movie.getTitle())) {
                            if (genre == null && year == null) {
                                finalFavorite.add(favoriteVideo);
                                break;
                            }
                            if (year == null) {
                                if (movie.getGenres().contains(genre)) {
                                    finalFavorite.add(favoriteVideo);
                                    break;
                                }
                            }
                            if (genre == null) {
                                if (year.equals(String.valueOf(movie.getYear()))) {
                                    finalFavorite.add(favoriteVideo);
                                    break;
                                }
                            }
                            if (movie.getGenres().contains(genre)
                                    && year.equals(String.valueOf(movie.getYear()))) {
                                finalFavorite.add(favoriteVideo);
                            }
                            break;
                        }
                    }
                }
                break;
            case "Serial":
                for (Favorite favoriteVideo : favoriteVideos) {
                    for (Serial serial : serials) {
                        if (favoriteVideo.getTitle().equals(serial.getTitle())) {
                            if (year == null && genre == null) {
                                finalFavorite.add(favoriteVideo);
                                break;
                            }
                            if (year == null) {
                                if (serial.getGenres().contains(genre)) {
                                    finalFavorite.add(favoriteVideo);
                                    break;
                                }
                            }
                            if (genre == null) {
                                if (year.equals(String.valueOf(serial.getYear()))) {
                                    finalFavorite.add(favoriteVideo);
                                    break;
                                }
                            }
                            if (serial.getGenres().contains(genre)
                                    && year.equals(String.valueOf(serial.getYear()))) {
                                finalFavorite.add(favoriteVideo);
                            }
                            break;
                        }
                    }
                }
                break;
            default:
                break;
        }
        return finalFavorite;
    }

    /**
     * Get all movies/serials (depends on input type) with their duration
     * The output will be filtered by year and genre
     *
     * @param action
     * @param movies            ArrayList with all movies
     * @param serials           ArrayList with all serials
     * @param type              type of video
     * @return ArrayList with all filtered movies/serials and their duration
     */
    public static ArrayList<LongestVideo> getLongest(final ActionInputData action,
                                                     final ArrayList<Movie> movies,
                                                     final ArrayList<Serial> serials,
                                                     final String type) {
        String year = action.getFilters().get(0).get(0);
        String genre = action.getFilters().get(1).get(0);
        ArrayList<LongestVideo> longestVideos = new ArrayList<>();
        switch (type) {
            case "Movie":
                for (Movie movie : movies) {
                    if (year == null && genre == null) {
                        longestVideos.add(new LongestVideo(movie.getTitle(), movie.getDuration()));
                        continue;
                    }
                    if (year == null) {
                        if (movie.getGenres().contains(genre)) {
                            longestVideos.add(new LongestVideo(movie.getTitle(),
                                    movie.getDuration()));
                            continue;
                        }
                    }
                    if (genre == null) {
                        if (year.equals(String.valueOf(movie.getYear()))) {
                            longestVideos
                                    .add(new LongestVideo(movie.getTitle(), movie.getDuration()));
                            continue;
                        }
                    }
                    if (movie.getGenres().contains(genre)
                            && year.equals(String.valueOf(movie.getYear()))) {
                        longestVideos.add(new LongestVideo(movie.getTitle(),
                                movie.getDuration()));
                    }
                }
                break;
            case "Serial":
                for (Serial serial : serials) {
                    int duration = 0;
                    for (Season season : serial.getSeasons()) {
                        duration += season.getDuration();
                    }
                    if (year == null && genre == null) {
                        longestVideos.add(new LongestVideo(serial.getTitle(), duration));
                        continue;
                    }
                    if (year == null) {
                        if (serial.getGenres().contains(genre)) {
                            longestVideos.add(new LongestVideo(serial.getTitle(), duration));
                            continue;
                        }
                    }
                    if (genre == null) {
                        if (year.equals(String.valueOf(serial.getYear()))) {
                            longestVideos.add(new LongestVideo(serial.getTitle(), duration));
                            continue;
                        }
                    }
                    if (serial.getGenres().contains(genre)
                            && year.equals(String.valueOf(serial.getYear()))) {
                        longestVideos.add(new LongestVideo(serial.getTitle(), duration));
                    }
                }
                break;
            default:
                break;
        }
        return longestVideos;
    }

    /**
     * Get all movies/serials ( depends on input type) with their views
     *
     * @param users         ArrayList with all users
     * @param movies        Arraylist with all movies
     * @param serials       ArrayList with all serials
     * @param action
     * @return  ArrayList with all movies/serials and how many times each video
     *          has been viewd
     */
    public static ArrayList<MostViewedVideos> getMostViwed(final ArrayList<User> users,
                                                           final ArrayList<Movie> movies,
                                                           final ArrayList<Serial> serials,
                                                           final ActionInputData action) {
        int ok = 0;
        ArrayList<MostViewedVideos> mostViewedVideos = new ArrayList<>();
        for (User user : users) {
            switch (action.getObjectType()) {
                case "movies":
                    for (Movie movie : movies) {
                        ok = 0;
                        if (user.getHistory().containsKey(movie.getTitle())) {
                            for (MostViewedVideos video : mostViewedVideos) {
                                if (video.getTitle().equals(movie.getTitle())) {
                                    video.add(user.getHistory().get(movie.getTitle()));
                                    ok = 1;
                                }
                            }
                            if (ok == 0) {
                                mostViewedVideos.add(new MostViewedVideos(movie.getTitle(),
                                        user.getHistory().get(movie.getTitle())));
                            }
                        }
                    }
                    break;
                case "shows":
                    for (Serial serial : serials) {
                        ok = 0;
                        if (user.getHistory().containsKey(serial.getTitle())) {
                            for (MostViewedVideos video : mostViewedVideos) {
                                if (video.getTitle().equals(serial.getTitle())) {
                                    video.add(user.getHistory().get(serial.getTitle()));
                                    ok = 1;
                                }
                            }
                            if (ok == 0) {
                                mostViewedVideos.add(new MostViewedVideos(serial.getTitle(),
                                        user.getHistory().get(serial.getTitle())));
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return mostViewedVideos;
    }

    /**
     * Filter videos by year and genre
     *
     * @param mostViewedVideos      ArrayList with videos and their views
     * @param movies                ArrayList with all movies
     * @param serials               ArrayList with all serials
     * @param action
     * @return  ArrayList with filtered mostViewedVideos by year and genre
     */
    public static ArrayList<MostViewedVideos> getFilteredMostViewd(
            final ArrayList<MostViewedVideos> mostViewedVideos,
            final ArrayList<Movie> movies,
            final ArrayList<Serial> serials,
            final ActionInputData action) {

        ArrayList<MostViewedVideos> filteredMostViewed = new ArrayList<>();
        String year = action.getFilters().get(0).get(0);
        String genre = action.getFilters().get(1).get(0);
        for (MostViewedVideos mostViewedVideo : mostViewedVideos) {
            switch (action.getObjectType()) {
                case "movies":
                    for (Movie movie : movies) {
                        if (mostViewedVideo.getTitle().equals(movie.getTitle())) {
                            if (year == null && genre == null) {
                                filteredMostViewed.add(mostViewedVideo);
                                continue;
                            }
                            if (year == null) {
                                if (movie.getGenres().contains(genre)) {
                                    filteredMostViewed.add(mostViewedVideo);
                                    continue;
                                }
                            }
                            if (genre == null) {
                                if (year.equals(String.valueOf(movie.getYear()))) {
                                    filteredMostViewed.add(mostViewedVideo);
                                    continue;
                                }
                            }
                            if (movie.getGenres().contains(genre)
                                    && year.equals(String.valueOf(movie.getYear()))) {
                                filteredMostViewed.add(mostViewedVideo);
                            }
                        }
                    }
                    break;
                case "shows":
                    for (Serial serial : serials) {
                        if (mostViewedVideo.getTitle().equals(serial.getTitle())) {
                            if (year == null && genre == null) {
                                filteredMostViewed.add(mostViewedVideo);
                                continue;
                            }
                            if (year == null) {
                                if (serial.getGenres().contains(genre)) {
                                    filteredMostViewed.add(mostViewedVideo);
                                    continue;
                                }
                            }
                            if (genre == null) {
                                if (year.equals(String.valueOf(serial.getYear()))) {
                                    filteredMostViewed.add(mostViewedVideo);
                                    continue;
                                }
                            }
                            if (serial.getGenres().contains(genre)
                                    && year.equals(String.valueOf(serial.getYear()))) {
                                filteredMostViewed.add(mostViewedVideo);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return filteredMostViewed;
    }

    /**
     *
     * @param movies            ArrayList with all movies
     * @param serials           ArrayList with all serials
     * @param videos            ArrayList with rated videos
     * @return  An ArrayList with all movies and serials
     */
    public static ArrayList<ShowsRating> getSearchVideos(final ArrayList<Movie> movies,
                                                         final ArrayList<Serial> serials,
                                                         final ArrayList<ShowsRating> videos) {
        for (Movie movie : movies) {
            int ok = 0;
            for (ShowsRating video : videos) {
                if (movie.getTitle().equals(video.getTitle())) {
                    ok = 1;
                    break;
                }
            }
            if (ok == 0) {
                videos.add(new ShowsRating(movie.getTitle(), (double) 0, "Movie"));
            }
        }
        for (Serial serial : serials) {
            int ok = 0;
            for (ShowsRating video : videos) {
                if (serial.getTitle().equals(video.getTitle())) {
                    ok = 1;
                    break;
                }
            }
            if (ok == 0) {
                videos.add(
                        new ShowsRating(serial.getTitle(), (double) 0, "Serial"));
            }
        }
        return videos;
    }

    /**
     *
     * @param videos            ArrayList with all videos
     * @param users             ArrayList with users
     * @param movies            ArrayList with all movies
     * @param serials           ArrayList with all serials
     * @param action
     * @return An ArrayList with all unseen videos of a specified user
     */
    public static ArrayList<String> search(final ArrayList<ShowsRating> videos,
                                           final ArrayList<User> users,
                                           final ArrayList<Movie> movies,
                                           final ArrayList<Serial> serials,
                                           final ActionInputData action) {
        ArrayList<String> outputVideos = new ArrayList<>();
        for (User user : users) {
            if (user.getUsername().equals(action.getUsername())) {
                if (user.getSubscriptionType().equals("BASIC")) {
                    break;
                } else {
                    for (ShowsRating video : videos) {
                        switch (video.getType()) {
                            case "Movie":
                                for (Movie movie : movies) {
                                    if (video.getTitle().equals(movie.getTitle())
                                            && movie.getGenres()
                                                    .contains(action.getGenre())) {
                                        if (!user.getHistory()
                                                .containsKey(video.getTitle())) {
                                            outputVideos.add(video.getTitle());
                                            break;
                                        }
                                        break;
                                    }
                                }
                                break;
                            case "Serial":
                                for (Serial serial : serials) {
                                    if (video.getTitle()
                                            .equals(serial.getTitle())
                                            && serial.getGenres()
                                                    .contains(action.getGenre())) {
                                        if (!user.getHistory()
                                                .containsKey(video.getTitle())) {
                                            outputVideos.add(video.getTitle());
                                            break;
                                        }
                                        break;
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        return outputVideos;
    }

    /**
     *
     * @param users             ArrayList with all users
     * @param movies            ArrayList with all movies
     * @param serials           ArrayList with all serials
     * @param favoriteVideos    ArrayList with favorite videos from all users
     * @param action
     * @return  First unseen video of an user from all favorite videos
     */
    public static String getFavorite(final ArrayList<User> users,
                                     final ArrayList<Movie> movies,
                                     final ArrayList<Serial> serials,
                                     final ArrayList<Favorite> favoriteVideos,
                                     final ActionInputData action) {
        int ok = 0;
        String output = null;
        for (User user : users) {
            if (user.getUsername().equals(action.getUsername())) {
                if (user.getSubscriptionType().equals("BASIC")) {
                    break;
                } else {
                    for (Favorite video : favoriteVideos) {
                        for (Movie movie : movies) {
                            if (video.getTitle().equals(movie.getTitle())) {
                                if (!user.getHistory()
                                        .containsKey(video.getTitle())) {
                                    ok = 1;
                                    output = video.getTitle();
                                    break;
                                }
                            }
                        }
                        if (ok == 0) {
                            for (Serial serial : serials) {
                                if (video.getTitle().equals(serial.getTitle())) {
                                    if (!user.getHistory()
                                            .containsKey(video.getTitle())) {
                                        ok = 1;
                                        output = video.getTitle();
                                        break;
                                    }
                                }
                            }
                        }
                        if (ok == 1) {
                            break;
                        }
                    }
                }
            }
        }
        return output;
    }

    /**
     *
     * @param users             ArrayList with all users
     * @param movies            ArrayList with all movies
     * @param serials           ArrayList with all serials
     * @param popularGenres     ArrayList with all found in videos
     * @param action
     * @return  First unseen video by an user, where the video contains one genre
     *          from popularGenre ( sorted from most popular to least popular)
     */
    public static String getPopular(final ArrayList<User> users,
                                    final ArrayList<Movie> movies,
                                    final ArrayList<Serial> serials,
                                    final ArrayList<PopularGenre> popularGenres,
                                    final ActionInputData action) {
        int ok = 0;
        String output = null;
        for (User user : users) {
            if (user.getUsername().equals(action.getUsername())) {
                if (user.getSubscriptionType().equals("BASIC")) {
                    break;
                } else {
                    for (PopularGenre genre : popularGenres) {
                        for (Movie movie : movies) {
                            if (!user.getHistory().containsKey(movie.getTitle())) {
                                if (movie.getGenres()
                                        .contains(genre.getType())) {
                                    ok = 1;
                                    output = movie.getTitle();
                                    break;
                                }
                            }
                        }
                        if (ok == 0) {
                            for (Serial serial : serials) {
                                if (!user.getHistory()
                                        .containsKey(serial.getTitle())) {
                                    if (serial.getGenres()
                                            .contains(genre.getType())) {
                                        ok = 1;
                                        output = serial.getTitle();
                                        break;
                                    }
                                }
                            }
                        }
                        if (ok == 1) {
                            break;
                        }
                    }
                }
            }
        }
        return output;
    }

    /**
     *
     * @param users             ArrayList with all users
     * @param movies            ArrayList with all movies
     * @param serials           ArrayList with all serials
     * @return  A list with all genre that appear in videos and views per genre
     */
    public static ArrayList<PopularGenre> getPopularGenre(final ArrayList<User> users,
                                                          final ArrayList<Movie> movies,
                                                          final ArrayList<Serial> serials) {
        ArrayList<PopularGenre> popularGenres = new ArrayList<>();
        int ok = 0;
        for (User user : users) {
            for (Movie movie : movies) {
                if (user.getHistory().containsKey(movie.getTitle())) {
                    for (String genre : movie.getGenres()) {
                        ok = 0;
                        for (PopularGenre video : popularGenres) {
                            if (video.getType().equals(genre)) {
                                video.add(user.getHistory().get(movie.getTitle()));
                                ok = 1;
                            }
                        }
                        if (ok == 0) {
                            popularGenres.add(new PopularGenre(genre,
                                    user.getHistory().get(movie.getTitle())));
                        }
                    }
                }
            }
        }
        for (User user : users) {
            for (Serial serial : serials) {
                if (user.getHistory().containsKey(serial.getTitle())) {
                    for (String genre : serial.getGenres()) {
                        ok = 0;
                        for (PopularGenre video : popularGenres) {
                            if (video.getType().equals(genre)) {
                                video.add(user.getHistory().get(serial.getTitle()));
                                ok = 1;
                            }
                        }
                        if (ok == 0) {
                            popularGenres.add(new PopularGenre(genre,
                                    user.getHistory().get(serial.getTitle())));
                        }
                    }
                }
            }
        }
        return popularGenres;
    }

    /**
     *
     * @param users             ArrayList with all users
     * @param movies            ArrayList with all movies
     * @param serials           ArrayList with all serials
     * @param action
     * @return  First unseen video, by an user, that appear in data base.
     */
    public static String getStandard(final ArrayList<User> users,
                                     final ArrayList<Movie> movies,
                                     final ArrayList<Serial> serials,
                                     final ActionInputData action) {
        String output = null;
        int ok = 0;
        for (User user : users) {
            if (action.getUsername().equals(user.getUsername())) {
                for (Movie movie : movies) {
                    if (!user.getHistory().containsKey(movie.getTitle())) {
                        ok = 1;
                        output = movie.getTitle();
                        break;
                    }
                }
                if (ok == 0) {
                    for (Serial serial : serials) {
                        if (!user.getHistory().containsKey(serial.getTitle())) {
                            ok = 1;
                            output = serial.getTitle();
                            break;
                        }
                    }
                }
                break;
            }
        }
        return output;
    }

    /**
     *
     * @param users             ArrayList with all users
     * @param movies            ArrayList with all movies
     * @param serials           ArrayList with all serials
     * @param showsRatings      ArrayList with all rated videos
     * @param action
     * @return  First best unseen video by an user
     */
    public static String getBestUnseen(final ArrayList<User> users,
                                       final ArrayList<Movie> movies,
                                       final ArrayList<Serial> serials,
                                       final ArrayList<ShowsRating> showsRatings,
                                       final ActionInputData action) {
        int ok = 0;
        String output = null;
        for (User user : users) {
            if (action.getUsername().equals(user.getUsername())) {
                for (ShowsRating showRating : showsRatings) {
                    if (!user.getHistory().containsKey(showRating.getTitle())) {
                        ok = 1;
                        output = showRating.getTitle();
                        break;
                    }
                }
                if (ok == 1) {
                    break;
                }
                for (Movie movie : movies) {
                    if (!user.getHistory().containsKey(movie.getTitle())) {
                        ok = 1;
                        output = movie.getTitle();
                        break;
                    }
                }
                if (ok == 0) {
                    for (Serial serial : serials) {
                        if (!user.getHistory().containsKey(serial.getTitle())) {
                            ok = 1;
                            output = serial.getTitle();
                            break;
                        }
                    }
                }
                break;
            }
        }
        return output;
    }
}
