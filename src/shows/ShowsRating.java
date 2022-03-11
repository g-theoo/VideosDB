package shows;


public final class ShowsRating {
    private final String title;
    private final Double grade;
    private final String type;

    public ShowsRating(final String title, final Double grade, final String type) {
        this.title = title;
        this.grade = grade;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public Double getGrade() {
        return grade;
    }

    public String getType() {
        return type;
    }
}
