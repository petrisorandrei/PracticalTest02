package eim.systems.cs.pub.ro.practicaltest02;

public class Moneda {

    private String EUR;
    private String USD;
    private String time;

    public Moneda(String EUR, String USD, String time) {
        this.EUR = EUR;
        this.USD = USD;
        this.time = time;
    }

    public String getEUR() {
        return EUR;
    }

    public void setEUR(String EUR) {
        this.EUR = EUR;
    }

    public String getUSD() {
        return USD;
    }

    public void setUSD(String USD) {
        this.USD = USD;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
