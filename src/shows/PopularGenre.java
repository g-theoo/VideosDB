package shows;

public final class PopularGenre {
    private final String type;
    private int numberOfViews;


    public PopularGenre(final String type, final int numberOfViews) {
        this.type = type;
        this.numberOfViews = numberOfViews;
    }

    /**
     * Add the number of views from a movie to number of views of a genre
     *
     * @param views views of a video
     */
    public void add(final int views) {
        this.numberOfViews += views;
    }

    public String getType() {
        return type;
    }

    public int getNumberOfViews() {
        return numberOfViews;
    }
}
