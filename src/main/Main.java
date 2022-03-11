package main;

import actor.ActorsAwards;
import actors.Actor;
import actors.ActorAwards;
import actors.ActorRating;
import checker.Checker;
import checker.Checkstyle;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.MovieInputData;
import shows.PopularGenre;
import fileio.SerialInputData;
import shows.ShowsRating;
import user.User;
import fileio.UserInputData;
import user.UsersRating;
import fileio.Writer;
import common.Constants;
import entertainment.Season;
import org.json.simple.JSONArray;
import shows.Favorite;
import shows.LongestVideo;
import shows.MostViewedVideos;
import shows.Movie;
import shows.RatedMovies;
import shows.RatedSerials;
import shows.Serial;
import utils.Sort;
import utils.Utils;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();
        //TODO add here the entry point to your implementation

        ArrayList<Actor> actors = new ArrayList<>();
        for (ActorInputData actor : input.getActors()) {
            String name = actor.getName();
            String careerDescription = actor.getCareerDescription();
            ArrayList<String> filmograhy = actor.getFilmography();
            Map<ActorsAwards, Integer> awards = actor.getAwards();
            actors.add(new Actor(name, careerDescription, filmograhy, awards));
        }
        ArrayList<Serial> serials = new ArrayList<>();
        for (SerialInputData serial : input.getSerials()) {
            String title = serial.getTitle();
            int year = serial.getYear();
            ArrayList<String> cast = serial.getCast();
            ArrayList<String> genres = serial.getGenres();
            int numberOfSeasons = serial.getNumberSeason();
            ArrayList<Season> seasons = serial.getSeasons();
            serials.add(new Serial(title, cast, genres, numberOfSeasons, seasons, year));
        }
        ArrayList<Movie> movies = new ArrayList<>();
        for (MovieInputData movie : input.getMovies()) {
            String title = movie.getTitle();
            int year = movie.getYear();
            ArrayList<String> cast = movie.getCast();
            ArrayList<String> genres = movie.getGenres();
            int duration = movie.getDuration();
            movies.add(new Movie(title, cast, genres, year, duration));
        }
        ArrayList<User> users = new ArrayList<>();
        for (UserInputData user : input.getUsers()) {
            String username = user.getUsername();
            String subcriptionType = user.getSubscriptionType();
            Map<String, Integer> history = user.getHistory();
            ArrayList<String> favoriteMovies = user.getFavoriteMovies();
            users.add(new User(username, subcriptionType, history, favoriteMovies));
        }
        ArrayList<RatedMovies> ratedMovies = new ArrayList<>();
        ArrayList<RatedSerials> ratedSerials = new ArrayList<>();
        ArrayList<ActorRating> actorsRating;
        ArrayList<String> outputMovies;
        ArrayList<String> outputSerials;
        String message;

        for (ActionInputData action : input.getCommands()) {
            if (action.getActionType().equals("command")) {
                switch (action.getType()) {
                    case "favorite":
                        for (User user : users) {
                            if (user.getUsername().equals(action.getUsername())) {
                                message = user.favorite(action);
                                arrayResult.add(fileWriter
                                        .writeFile(action.getActionId(), null, message));
                            }
                        }
                        break;
                    case "rating":
                        for (User user : users) {
                            if (user.getUsername().equals(action.getUsername())) {
                                if (action.getSeasonNumber() == 0) {
                                    message = user.ratingMovie(action, ratedMovies);
                                    arrayResult.add(fileWriter
                                            .writeFile(action.getActionId(), null, message));
                                } else {
                                    message = user.ratingSerial(action, ratedSerials);
                                    arrayResult.add(fileWriter
                                            .writeFile(action.getActionId(), null, message));
                                }
                            }
                        }
                        break;
                    case "view":
                        for (User user : users) {
                            if (user.getUsername().equals(action.getUsername())) {
                                message = user.view(action);
                                arrayResult.add(fileWriter
                                        .writeFile(action.getActionId(), null, message));
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            if (action.getActionType().equals("query")) {
                switch (action.getObjectType()) {
                    case "actors":
                        switch (action.getCriteria()) {
                            case "average":
                                actorsRating =
                                        Utils.getActorsRatings(ratedMovies, ratedSerials, serials,
                                                actors);
                                ArrayList<String> outputActors = new ArrayList<>();
                                switch (action.getSortType()) {
                                    case "asc":
                                        Collections.sort(actorsRating, Sort.ascRating);
                                        actorsRating = Sort.ratingAscName(actorsRating);
                                        for (int i = 0; i < action.getNumber(); i++) {
                                            if (i == actorsRating.size()) {
                                                break;
                                            } else {
                                                outputActors.add(actorsRating.get(i).getName());
                                            }
                                        }
                                        message = "Query result: " + outputActors.toString();
                                        arrayResult.add(fileWriter
                                                .writeFile(action.getActionId(), null, message));
                                        for (Actor actor : actors) {
                                            actor.setRating((double) 0);
                                            actor.setCounterGrade(0);
                                        }
                                        break;
                                    case "desc":
                                        Collections.sort(actorsRating, Sort.descRating);
                                        actorsRating = Sort.ratingDescName(actorsRating);
                                        for (int i = 0; i < action.getNumber(); i++) {
                                            if (i == actorsRating.size()) {
                                                break;
                                            } else {
                                                outputActors.add(actorsRating.get(i).getName());
                                            }
                                        }
                                        message = "Query result: " + outputActors.toString();
                                        arrayResult.add(fileWriter
                                                .writeFile(action.getActionId(), null, message));
                                        for (Actor actor : actors) {
                                            actor.setRating((double) 0);
                                            actor.setCounterGrade(0);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            case "awards":
                                ArrayList<ActorAwards> actorsAwards = new ArrayList<>();
                                if (action.getFilters().get(3) != null) {
                                    actorsAwards = Utils.getActorsAwards(action, actors);
                                    switch (action.getSortType()) {
                                        case "asc":
                                            Collections.sort(actorsAwards, Sort.ascActorsAwards);
                                            actorsAwards = Sort.awardsAscName(actorsAwards);
                                            message = "Query result: " + actorsAwards.toString();
                                            arrayResult.add(fileWriter
                                                    .writeFile(action.getActionId(), null,
                                                            message));
                                            break;
                                        case "desc":
                                            Collections.sort(actorsAwards, Sort.descActorsAwards);
                                            actorsAwards = Sort.awardsDescName(actorsAwards);
                                            message = "Query result: " + actorsAwards.toString();
                                            arrayResult.add(fileWriter
                                                    .writeFile(action.getActionId(), null,
                                                            message));
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            case "filter_description":
                                if (action.getFilters().get(2) != null) {
                                    ArrayList<String> actorsDescription = new ArrayList<>();
                                    int numberOfWords = action.getFilters().get(2).size();
                                    List<String> words = action.getFilters().get(2);
                                    for (Actor actor : actors) {
                                        int wordsFound = 0;
                                        for (String word : words) {
                                            Pattern pattern = Pattern.compile("\\b" + word + "\\b",
                                                    Pattern.CASE_INSENSITIVE);
                                            Matcher matcher =
                                                    pattern.matcher(actor.getCareerDescription());
                                            if (matcher.find()) {
                                                wordsFound++;
                                            }
                                        }
                                        if (wordsFound == numberOfWords) {
                                            actorsDescription.add(actor.getName());
                                        }
                                    }
                                    switch (action.getSortType()) {
                                        case "asc":
                                            Collections.sort(actorsDescription, Sort.ascString);
                                            message =
                                                    "Query result: " + actorsDescription.toString();
                                            arrayResult.add(fileWriter
                                                    .writeFile(action.getActionId(), null,
                                                            message));
                                            break;
                                        case "desc":
                                            Collections.sort(actorsDescription, Sort.descString);
                                            message =
                                                    "Query result: " + actorsDescription.toString();
                                            arrayResult.add(fileWriter
                                                    .writeFile(action.getActionId(), null,
                                                            message));
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            default:
                                break;
                        }
                        break;
                    case "movies":
                        switch (action.getCriteria()) {
                            case "ratings":
                                outputMovies = new ArrayList<>();
                                ArrayList<ShowsRating> showsRating =
                                        Utils.getShowsRating(ratedMovies, ratedSerials, serials);
                                ArrayList<ShowsRating> finalShowsRating =
                                        Utils.getRating(showsRating,
                                                movies, serials, action, "Movie");

                                switch (action.getSortType()) {
                                    case "asc":
                                        Collections.sort(finalShowsRating, Sort.ascShowsRating);
                                        for (int i = 0; i < action.getNumber(); i++) {
                                            if (i == finalShowsRating.size()) {
                                                break;
                                            } else {
                                                outputMovies
                                                        .add(finalShowsRating.get(i).getTitle());
                                            }
                                        }
                                        message = "Query result: " + outputMovies.toString();
                                        arrayResult.add(fileWriter
                                                .writeFile(action.getActionId(), null,
                                                        message));
                                        break;

                                    case "desc":
                                        Collections.sort(finalShowsRating, Sort.descShowsRating);
                                        for (int i = 0; i < action.getNumber(); i++) {
                                            if (i == finalShowsRating.size()) {
                                                break;
                                            } else {
                                                outputMovies
                                                        .add(finalShowsRating.get(i).getTitle());
                                            }
                                        }
                                        message = "Query result: " + outputMovies.toString();
                                        arrayResult.add(fileWriter
                                                .writeFile(action.getActionId(), null,
                                                        message));
                                        break;
                                    default:
                                        break;
                                }
                                break;

                            case "favorite":
                                outputMovies = new ArrayList<>();
                                ArrayList<Favorite> favoriteMovies =
                                        Utils.getFavorite(users, movies, serials, action, "Movie");
                                switch (action.getSortType()) {
                                    case "asc":
                                        Collections.sort(favoriteMovies, Sort.ascFavorite);
                                        favoriteMovies = Sort.favoritesName(favoriteMovies,
                                                action.getSortType());
                                        for (int i = 0; i < action.getNumber(); i++) {
                                            if (i == favoriteMovies.size()) {
                                                break;
                                            } else {
                                                outputMovies.add(favoriteMovies.get(i).getTitle());
                                            }
                                        }
                                        message = "Query result: " + outputMovies.toString();
                                        arrayResult.add(fileWriter
                                                .writeFile(action.getActionId(), null,
                                                        message));
                                        break;
                                    case "desc":
                                        Collections.sort(favoriteMovies, Sort.descFavorite);
                                        favoriteMovies = Sort.favoritesName(favoriteMovies,
                                                action.getSortType());
                                        for (int i = 0; i < action.getNumber(); i++) {
                                            if (i == favoriteMovies.size()) {
                                                break;
                                            } else {
                                                outputMovies.add(favoriteMovies.get(i).getTitle());
                                            }
                                        }
                                        message = "Query result: " + outputMovies.toString();
                                        arrayResult.add(fileWriter
                                                .writeFile(action.getActionId(), null,
                                                        message));
                                        break;
                                    default:
                                        break;
                                }
                                break;

                            case "longest":
                                outputMovies = new ArrayList<>();
                                ArrayList<LongestVideo> longestMovies =
                                        Utils.getLongest(action, movies, serials, "Movie");
                                switch (action.getSortType()) {
                                    case "asc":
                                        Collections.sort(longestMovies, Sort.ascLongest);
                                        longestMovies = Sort.longestSortName(longestMovies,
                                                action.getSortType());
                                        for (int i = 0; i < action.getNumber(); i++) {
                                            if (i == longestMovies.size()) {
                                                break;
                                            } else {
                                                outputMovies.add(longestMovies.get(i).getTitle());
                                            }
                                        }
                                        message = "Query result: " + outputMovies.toString();
                                        arrayResult.add(fileWriter
                                                .writeFile(action.getActionId(), null,
                                                        message));
                                        break;
                                    case "desc":
                                        Collections.sort(longestMovies, Sort.descLongest);
                                        longestMovies = Sort.longestSortName(longestMovies,
                                                action.getSortType());
                                        for (int i = 0; i < action.getNumber(); i++) {
                                            if (i == longestMovies.size()) {
                                                break;
                                            } else {
                                                outputMovies.add(longestMovies.get(i).getTitle());
                                            }
                                        }
                                        message = "Query result: " + outputMovies.toString();
                                        arrayResult.add(fileWriter
                                                .writeFile(action.getActionId(), null,
                                                        message));
                                        break;
                                    default:
                                        break;
                                }
                                break;


                            case "most_viewed":
                                outputMovies = new ArrayList<>();
                                ArrayList<MostViewedVideos>
                                        mostViewedVideos =
                                        Utils.getMostViwed(users, movies, serials, action);
                                mostViewedVideos =
                                        Utils.getFilteredMostViewd(mostViewedVideos, movies,
                                                serials, action);
                                switch (action.getSortType()) {
                                    case "asc":
                                        Collections.sort(mostViewedVideos, Sort.ascMostViewed);
                                        mostViewedVideos = Sort.mostViewedSortName(mostViewedVideos,
                                                action.getSortType());
                                        for (int i = 0; i < action.getNumber(); i++) {
                                            if (i == mostViewedVideos.size()) {
                                                break;
                                            } else {
                                                outputMovies
                                                        .add(mostViewedVideos.get(i).getTitle());
                                            }
                                        }
                                        message = "Query result: " + outputMovies.toString();
                                        arrayResult.add(fileWriter
                                                .writeFile(action.getActionId(), null,
                                                        message));
                                        break;
                                    case "desc":

                                        Collections.sort(mostViewedVideos, Sort.descMostViewed);
                                        mostViewedVideos = Sort.mostViewedSortName(mostViewedVideos,
                                                action.getSortType());
                                        for (int i = 0; i < action.getNumber(); i++) {
                                            if (i == mostViewedVideos.size()) {
                                                break;
                                            } else {
                                                outputMovies
                                                        .add(mostViewedVideos.get(i).getTitle());
                                            }
                                        }
                                        message = "Query result: " + outputMovies.toString();
                                        arrayResult.add(fileWriter
                                                .writeFile(action.getActionId(), null,
                                                        message));
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            default:
                                break;
                        }
                        break;
                    case "shows":
                        switch (action.getCriteria()) {
                            case "ratings":
                                outputSerials = new ArrayList<>();
                                ArrayList<ShowsRating> showsRating =
                                        Utils.getShowsRating(ratedMovies, ratedSerials, serials);
                                ArrayList<ShowsRating> finalShowsRating =
                                        Utils.getRating(showsRating,
                                                movies, serials, action, "Serial");

                                switch (action.getSortType()) {
                                    case "asc":
                                        Collections.sort(finalShowsRating, Sort.ascShowsRating);
                                        for (int i = 0; i < action.getNumber(); i++) {
                                            if (i == finalShowsRating.size()) {
                                                break;
                                            } else {
                                                outputSerials
                                                        .add(finalShowsRating.get(i).getTitle());
                                            }
                                        }
                                        message = "Query result: " + outputSerials.toString();
                                        arrayResult.add(fileWriter
                                                .writeFile(action.getActionId(), null,
                                                        message));
                                        break;

                                    case "desc":
                                        Collections.sort(finalShowsRating, Sort.descShowsRating);
                                        for (int i = 0; i < action.getNumber(); i++) {
                                            if (i == finalShowsRating.size()) {
                                                break;
                                            } else {
                                                outputSerials
                                                        .add(finalShowsRating.get(i).getTitle());
                                            }
                                        }
                                        message = "Query result: " + outputSerials.toString();
                                        arrayResult.add(fileWriter
                                                .writeFile(action.getActionId(), null,
                                                        message));
                                        break;
                                    default:
                                        break;
                                }
                                break;

                            case "favorite":
                                outputSerials = new ArrayList<>();
                                ArrayList<Favorite> favoriteSerials =
                                        Utils.getFavorite(users, movies, serials, action, "Serial");
                                switch (action.getSortType()) {
                                    case "asc":
                                        Collections.sort(favoriteSerials, Sort.ascFavorite);
                                        favoriteSerials = Sort.favoritesName(favoriteSerials,
                                                action.getSortType());
                                        for (int i = 0; i < action.getNumber(); i++) {
                                            if (i == favoriteSerials.size()) {
                                                break;
                                            } else {
                                                outputSerials
                                                        .add(favoriteSerials.get(i).getTitle());
                                            }
                                        }
                                        message = "Query result: " + outputSerials.toString();
                                        arrayResult.add(fileWriter
                                                .writeFile(action.getActionId(), null,
                                                        message));
                                        break;
                                    case "desc":
                                        Collections.sort(favoriteSerials, Sort.descFavorite);
                                        favoriteSerials = Sort.favoritesName(favoriteSerials,
                                                                            action.getSortType());
                                        for (int i = 0; i < action.getNumber(); i++) {
                                            if (i == favoriteSerials.size()) {
                                                break;
                                            } else {
                                                outputSerials
                                                        .add(favoriteSerials.get(i).getTitle());
                                            }
                                        }
                                        message = "Query result: " + outputSerials.toString();
                                        arrayResult.add(fileWriter
                                                .writeFile(action.getActionId(), null,
                                                        message));
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case "longest":
                                outputSerials = new ArrayList<>();
                                ArrayList<LongestVideo> longestSerials =
                                        Utils.getLongest(action, movies, serials, "Serial");
                                switch (action.getSortType()) {
                                    case "asc":
                                        Collections.sort(longestSerials, Sort.ascLongest);
                                        longestSerials = Sort.longestSortName(longestSerials,
                                                action.getSortType());
                                        for (int i = 0; i < action.getNumber(); i++) {
                                            if (i == longestSerials.size()) {
                                                break;
                                            } else {
                                                outputSerials.add(longestSerials.get(i).getTitle());
                                            }
                                        }
                                        message = "Query result: " + outputSerials.toString();
                                        arrayResult.add(fileWriter
                                                .writeFile(action.getActionId(), null,
                                                        message));
                                        break;
                                    case "desc":
                                        Collections.sort(longestSerials, Sort.descLongest);
                                        longestSerials = Sort.longestSortName(longestSerials,
                                                action.getSortType());
                                        for (int i = 0; i < action.getNumber(); i++) {
                                            if (i == longestSerials.size()) {
                                                break;
                                            } else {
                                                outputSerials.add(longestSerials.get(i).getTitle());
                                            }
                                        }
                                        message = "Query result: " + outputSerials.toString();
                                        arrayResult.add(fileWriter
                                                .writeFile(action.getActionId(), null,
                                                        message));
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case "most_viewed":
                                outputSerials = new ArrayList<>();
                                ArrayList<MostViewedVideos>
                                        mostViewedVideos =
                                        Utils.getMostViwed(users, movies, serials, action);
                                mostViewedVideos =
                                        Utils.getFilteredMostViewd(mostViewedVideos, movies,
                                                serials, action);
                                switch (action.getSortType()) {
                                    case "asc":
                                        Collections.sort(mostViewedVideos, Sort.ascMostViewed);
                                        mostViewedVideos = Sort.mostViewedSortName(mostViewedVideos,
                                                action.getSortType());
                                        for (int i = 0; i < action.getNumber(); i++) {
                                            if (i == mostViewedVideos.size()) {
                                                break;
                                            } else {
                                                outputSerials
                                                        .add(mostViewedVideos.get(i).getTitle());
                                            }
                                        }
                                        message = "Query result: " + outputSerials.toString();
                                        arrayResult.add(fileWriter
                                                .writeFile(action.getActionId(), null,
                                                        message));
                                        break;
                                    case "desc":

                                        Collections.sort(mostViewedVideos, Sort.descMostViewed);
                                        mostViewedVideos = Sort.mostViewedSortName(mostViewedVideos,
                                                action.getSortType());
                                        for (int i = 0; i < action.getNumber(); i++) {
                                            if (i == mostViewedVideos.size()) {
                                                break;
                                            } else {
                                                outputSerials
                                                        .add(mostViewedVideos.get(i).getTitle());
                                            }
                                        }
                                        message = "Query result: " + outputSerials.toString();
                                        arrayResult.add(fileWriter
                                                .writeFile(action.getActionId(), null,
                                                        message));
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            default:
                                break;
                        }
                        break;
                    case "users":
                        ArrayList<String> outputUsers = new ArrayList<>();
                        ArrayList<UsersRating> usersRatings = new ArrayList<>();
                        for (User user : users) {
                            if (user.getRatings() != 0) {
                                usersRatings.add(new UsersRating(user.getUsername(),
                                        user.getRatings()));
                            }
                        }
                        switch (action.getSortType()) {
                            case "asc":
                                Collections.sort(usersRatings, Sort.ascUsersRating);
                                usersRatings = Sort.sortUsersName(usersRatings, action);
                                for (int i = 0; i < action.getNumber(); i++) {
                                    if (i == usersRatings.size()) {
                                        break;
                                    } else {
                                        outputUsers.add(usersRatings.get(i).getName());
                                    }
                                }
                                message = "Query result: " + outputUsers.toString();
                                arrayResult.add(fileWriter
                                        .writeFile(action.getActionId(), null,
                                                message));
                                break;
                            case "desc":
                                Collections.sort(usersRatings, Sort.descUsersRating);
                                usersRatings = Sort.sortUsersName(usersRatings, action);
                                for (int i = 0; i < action.getNumber(); i++) {
                                    if (i == usersRatings.size()) {
                                        break;
                                    } else {
                                        outputUsers.add(usersRatings.get(i).getName());
                                    }
                                }
                                message = "Query result: " + outputUsers.toString();
                                arrayResult.add(fileWriter
                                        .writeFile(action.getActionId(), null,
                                                message));
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;

                }
            }
            if (action.getActionType().equals("recommendation")) {
                switch (action.getType()) {
                    case "standard":
                        String output = Utils.getStandard(users, movies, serials, action);
                        if (output != null) {
                            message = "StandardRecommendation result: " + output;
                            arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), null,
                                            message));
                            break;
                        } else {
                            message = "StandardRecommendation cannot be applied!";
                            arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), null,
                                            message));
                            break;
                        }
                    case "best_unseen":
                        ArrayList<ShowsRating> showsRatings =
                                Utils.getShowsRating(ratedMovies, ratedSerials, serials);
                        Collections.sort(showsRatings, Sort.descShowsRating);
                        output = Utils.getBestUnseen(users, movies, serials, showsRatings, action);
                        if (output != null) {
                            message = "BestRatedUnseenRecommendation result: " + output;
                            arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), null,
                                            message));
                            break;
                        } else {
                            message = "BestRatedUnseenRecommendation cannot be applied!";
                            arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), null,
                                            message));
                            break;
                        }
                    case "popular":
                        output = null;
                        ArrayList<PopularGenre> popularGenres = Utils.getPopularGenre(users, movies,
                                serials);
                        Collections.sort(popularGenres, Sort.descPopular);
                        output  = Utils.getPopular(users, movies, serials, popularGenres, action);
                        if (output != null) {
                            message = "PopularRecommendation result: " + output;
                            arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), null,
                                            message));
                        } else {
                            message = "PopularRecommendation cannot be applied!";
                            arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), null,
                                            message));
                        }
                        break;
                    case "favorite":
                        int ok;
                        ArrayList<Favorite> favoriteVideos = new ArrayList<>();
                        for (Movie movie : movies) {
                            favoriteVideos.add(new Favorite(movie.getTitle(), 0));
                        }
                        for (Serial serial : serials) {
                            favoriteVideos.add(new Favorite(serial.getTitle(), 0));
                        }
                        for (User user : users) {
                            for (String favorite : user.getFavoriteMovies()) {
                                ok = 0;
                                for (Favorite favoriteVideo : favoriteVideos) {
                                    if (favoriteVideo.getTitle().equals(favorite)) {
                                        favoriteVideo.add();
                                        ok = 1;
                                        break;
                                    }
                                }
                            }
                        }
                        Collections.sort(favoriteVideos, Sort.descFavorite);
                        output = Utils.getFavorite(users, movies, serials, favoriteVideos, action);
                        if (output != null) {
                            message = "FavoriteRecommendation result: " + output;
                            arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), null,
                                            message));
                        } else {
                            message = "FavoriteRecommendation cannot be applied!";
                            arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), null,
                                            message));
                        }
                        break;
                    case "search":
                        ArrayList<String> outputVideos = new ArrayList<>();
                        ArrayList<ShowsRating> videos =
                                Utils.getShowsRating(ratedMovies, ratedSerials, serials);
                        videos = Utils.getSearchVideos(movies, serials, videos);
                        Collections.sort(videos, Sort.ascShowsRating);
                        videos = Sort.ascShowsRating(videos);
                        outputVideos = Utils.search(videos, users, movies, serials, action);
                        if (outputVideos.size() == 0) {
                            message = "SearchRecommendation cannot be applied!";
                            arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), null,
                                            message));
                        } else {
                            message = "SearchRecommendation result: " + outputVideos.toString();
                            arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), null,
                                            message));
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        fileWriter.closeJSON(arrayResult);
    }
}
