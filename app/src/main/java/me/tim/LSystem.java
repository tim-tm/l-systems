package me.tim;

import java.util.List;

public class LSystem {
    private final List<Rule> rules;
    private final String start;
    private String current;

    public LSystem(List<Rule> rules, String start) {
        this.rules = rules;
        this.start = start;
        this.current = start;
    }

    private Rule getContainingRule(char c) {
        Rule r = null;
        for (Rule rule : this.rules) {
            if (c == rule.getValue()) {
                r = rule;
            }
        }
        return r;
    }

    public String step() {
        StringBuffer builder = new StringBuffer();
        for (char c : this.current.toCharArray()) {
            Rule rule = this.getContainingRule(c);
            if (rule != null) {
                builder.append(rule.getReplacement());
            } else {
                builder.append(c);
            }
        }
        this.current = builder.toString();
        return this.current;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getStart() {
        return start;
    }

    public String prettyString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Start: ").append(this.getStart()).append("\n\n");
        for (Rule rule : this.getRules()) {
            buf.append(rule.toString()).append("\n \n");
        }
        return buf.toString();
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Start: ").append(this.getStart()).append('\n');
        for (Rule rule : this.getRules()) {
            buf.append('\t').append(rule.toString()).append('\n');
        }
        return buf.toString();
    }
}
