package com.example.lab_2_javafx;

public class a_month {

    public int id;
    public double awp;
    public double awithp;
    public double p;   // procentai
    public double i;     // palūkanos
    public double c;       // kreditas
    public double r;

    public a_month(int month_id, double amount_without_percentage, double amount_with_percentage, double percentage, double interest, double credit, double remaining_amount){
        this.id = month_id;
        this.awp = amount_without_percentage;
        this.awithp = amount_with_percentage;
        this.p = percentage;
        this.i = interest;
        this.c = credit;
        this.r = remaining_amount;
    }

    public int getId() { return id; }
    public double getAwp() { return awp; }
    public double getAwithp() { return awithp; }
    public double getP() { return p; }
    public double getI() { return i; }
    public double getC() { return c; }
    public double getR() { return r; }
}
