package shows;

public final class Favorite {
    private final String title;
    private int numberOfAppearances;

    public Favorite(final String title, final int numberOfAppearances) {
        this.title = title;
        this.numberOfAppearances = numberOfAppearances;
    }

    /**
     * Increase number of apperance of a video
     */
    public void add() {
        this.numberOfAppearances++;
    }

    public String getTitle() {
        return title;
    }

    public int getNumberOfAppearances() {
        return numberOfAppearances;
    }
}


