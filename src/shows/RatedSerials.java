package shows;

public final class RatedSerials {
    private final String username;
    private final String title;
    private final int season;
    private final Double grade;
    private int numberOfSeasons;

    public RatedSerials(final String username, final String title,
                        final int season, final Double grade) {
        this.username = username;
        this.title = title;
        this.season = season;
        this.grade = grade;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public int getSeason() {
        return season;
    }

    public Double getGrade() {
        return grade;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }
}
