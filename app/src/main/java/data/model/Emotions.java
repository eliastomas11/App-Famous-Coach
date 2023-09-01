package data.model;

public enum Emotions {
    RANDOM("random"),
    HAPPY("happy"),
    SAD("sad"),
    WORRIED("worried"),
    EXCITED("excited"),
    ANGRY("angry");

    public String getName() {
        return name;
    }

    private final String name;
    Emotions(String name) {
        this.name = name;
    }

}
