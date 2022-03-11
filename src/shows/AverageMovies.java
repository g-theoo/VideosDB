package shows;

public final class AverageMovies {
    private final String title;
    private Double grade;
    private int numberOfGrades;
    private Double finalGrade;

    public AverageMovies(final String title, final Double grade, final int numberOfGrades) {
        this.title = title;
        this.grade = grade;
        this.numberOfGrades = numberOfGrades;
    }

    /**
     * Add grade and incerease the number of grades for actor
     * @param grade
     */
    public void addGrade(final Double amount) {
        this.grade += amount;
        this.numberOfGrades++;
    }


    public String getTitle() {
        return title;
    }

    public Double getGrade() {
        return grade;
    }

    public int getNumberOfGrades() {
        return numberOfGrades;
    }

    /**
     * Calculate the final grade for actor
     */
    public void finalGrade() {
        this.finalGrade = this.grade / this.numberOfGrades;
    }

    public Double getFinalGrade() {
        return finalGrade;
    }
}
