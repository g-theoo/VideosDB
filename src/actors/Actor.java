package actors;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.Map;

/**
 * Information about an actor, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public class Actor {
    /**
     * actor name
     */
    private String name;
    /**
     * description of the actor's career
     */
    private String careerDescription;
    /**
     * videos starring actor
     */
    private ArrayList<String> filmography;
    /**
     * awards won by the actor
     */
    private Map<ActorsAwards, Integer> awards;

    private Double rating = (double) 0;

    private int counterGrade = 0;

    public Actor(final String name, final String careerDescription,
                 final ArrayList<String> filmography,
                 final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
    }

    /**
     * Method used to calculate actors rating
     *
     * @param ratingRecieved
     */
    public final void addRating(final Double ratingRecieved) {
        this.rating += ratingRecieved;
        this.counterGrade++;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final ArrayList<String> getFilmography() {
        return filmography;
    }

    public final void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    public final Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public final String getCareerDescription() {
        return careerDescription;
    }

    public final void setCareerDescription(final String careerDescription) {
        this.careerDescription = careerDescription;
    }

    public final Double getRating() {
        return rating;
    }

    public final void setRating(final Double rating) {
        this.rating = rating;
    }

    public final int getCounterGrade() {
        return counterGrade;
    }

    public final void setCounterGrade(final int counterGrade) {
        this.counterGrade = counterGrade;
    }

    @Override
    public final String toString() {
        return "ActorInputData{"
                + "name='" + name + '\''
                + ", careerDescription='"
                + careerDescription + '\''
                + ", filmography=" + filmography + '}';
    }

}
