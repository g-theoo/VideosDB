package shows;

import java.util.ArrayList;

public final class AverageSerials {
    private final String title;
    private final int numberOfSeasons;
    private ArrayList<Double> grades = new ArrayList<>();
    private ArrayList<Integer> gradesRecieved = new ArrayList<>();
    private Double finalGrade = (double) 0;

    public AverageSerials(final String title, final int numberOfSeasons,
                          final int season, final Double grade) {
        this.title = title;
        this.numberOfSeasons = numberOfSeasons;
        for (int i = 0; i < numberOfSeasons; i++) {
            if (i == season - 1) {
                this.grades.add(grade);
                this.gradesRecieved.add(1);
            } else {
                this.grades.add((double) 0);
                this.gradesRecieved.add(0);
            }
        }
    }

    /**
     *  Add grade to a specified season of a serial
     * @param grade
     * @param season
     */
    public void addGrade(final Double grade, final int season) {
        Double currentGrade = this.grades.get(season - 1);
        currentGrade += grade;
        this.grades.remove(season - 1);
        this.grades.add(season - 1, currentGrade);
        int currentGradeesRecieved = this.gradesRecieved.get(season - 1);
        currentGradeesRecieved++;
        this.gradesRecieved.remove(season - 1);
        this.gradesRecieved.add(season - 1, currentGradeesRecieved);
    }

    /**
     * Calculate the final grade for serial
     */
    public void finalGrade() {
        for (int i = 0; i < numberOfSeasons; i++) {
            if (this.gradesRecieved.get(i) == 0) {
                finalGrade += 0;
            } else {
                finalGrade += this.grades.get(i) / this.gradesRecieved.get(i);
            }
        }
        finalGrade = finalGrade / numberOfSeasons;
    }

    public String getTitle() {
        return title;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public ArrayList<Double> getGrades() {
        return grades;
    }

    public ArrayList<Integer> getGradesRecieved() {
        return gradesRecieved;
    }

    public Double getFinalGrade() {
        return finalGrade;
    }
}
