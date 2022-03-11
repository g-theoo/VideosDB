package actors;

public class ActorRating {
    private final String name;
    private Double grade;
    private int counterGrade;


    public ActorRating(final String name, final Double grade, final int counterGrade) {
        this.name = name;
        this.grade = grade;
        this.counterGrade = counterGrade;
    }

//    /**
//     *
//     * @param grade
//     */
//    public final void addGrade(final Double grade) {
//        this.grade += grade;
//        this.counterGrade++;
//    }

    /**
     * Calculates the final rating for actor
     */
    public final void finalGrade() {
        this.grade = this.grade / counterGrade;
    }

    public final String getName() {
        return name;
    }

    public final Double getGrade() {
        return grade;
    }

    public final int getCounterGrade() {
        return counterGrade;
    }

}
