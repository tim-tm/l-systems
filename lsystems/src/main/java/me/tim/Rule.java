package me.tim;

public class Rule {
    private final char value;
    private final String replacement;

    public Rule(char value, String replacement) {
        this.value = value;
        this.replacement = replacement;
    }

    public char getValue() {
        return value;
    }

    public String getReplacement() {
        return replacement;
    }

    @Override
    public String toString() {
        return this.getValue() + "--> " + this.getReplacement();
    }
}
