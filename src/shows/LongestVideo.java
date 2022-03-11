package shows;

public final class LongestVideo {
    private final String title;
    private final int duration;

    public LongestVideo(final String title, final int duration) {
        this.title = title;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }
}
