package shows;

public final class MostViewedVideos {
    private final String title;
    private int numberOfViews;

    public MostViewedVideos(final String title, final int numberOfViews) {
        this.title = title;
        this.numberOfViews = numberOfViews;
    }

    /**
     * Add number of views from a video
     * @param amount
     */
    public void add(final int amount) {
        this.numberOfViews += amount;
    }

    public String getTitle() {
        return title;
    }

    public int getNumberOfViews() {
        return numberOfViews;
    }
}
