package actors;

public class ActorAwards {
    private final String name;
    private final int numberOfAwards;

    public ActorAwards(final String name, final int numberOfAwards) {
        this.name = name;
        this.numberOfAwards = numberOfAwards;
    }

    public final String getName() {
        return name;
    }

    public final int getNumberOfAwards() {
        return numberOfAwards;
    }

    @Override
    public final String toString() {
        return name;
    }
}
