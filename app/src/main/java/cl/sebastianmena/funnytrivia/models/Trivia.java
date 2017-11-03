package cl.sebastianmena.funnytrivia.models;

/**
 * Created by Sebastián Mena on 31-10-2017.
 */

public class Trivia {

    private String text;
    private int number;

    public Trivia() {
    }

    public Trivia(String text, int number) {
        this.text = text;
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}
