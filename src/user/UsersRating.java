package user;

public final class UsersRating {
    private final String name;
    private final int numberOfRatings;

    public UsersRating(final String name, final int numberOfRatings) {
        this.name = name;
        this.numberOfRatings = numberOfRatings;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }
}
