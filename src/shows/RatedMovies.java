package shows;

public final class RatedMovies {
    private final String username;
    private final String title;
    private final Double grade;

    public RatedMovies(final String username, final String title, final Double grade) {
        this.username = username;
        this.title = title;
        this.grade = grade;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public Double getGrade() {
        return grade;
    }
}
