package brains;
import com.example.lab_2_javafx.a_month;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class months_manager  {

    public a_month[] months_annuity;
    public a_month[] months_linear;

    public double remaining_amount_a;    // in the beginning it is wanted sum
    public double total_amount_a;

    public double percentage_a;
    double amount_without_percentage_a;
    double amount_with_percentage_a;
    double interest_a;
    double credit_a;

    public double remaining_amount_l;    // in the beginning it is wanted sum
    public double total_amount_l;

    public double percentage_l;
    double amount_without_percentage_l;
    double amount_with_percentage_l;
    double interest_l;
    double credit_l;

    public int total_months;

    public void create_months(int months_count){
        System.out.println(months_count + " months were created.");

        months_annuity = new a_month[months_count];
        months_linear = new a_month[months_count];

        total_amount_a = remaining_amount_a;
        total_amount_l = remaining_amount_l;

        for (int i=0; i<months_count; i++) {
            //calculating annuity
            annuity_manager(months_count);

            months_annuity[i] = new a_month(i+1, amount_without_percentage_a, amount_with_percentage_a, percentage_a, interest_a, credit_a, remaining_amount_a);

            remaining_amount_a -= months_annuity[i].c;
            remaining_amount_a = round(remaining_amount_a, 2);
            System.out.println(months_annuity[i].id + " : " + months_annuity[i].r + " : " + months_annuity[i].c + " : " + months_annuity[i].i + " : " + months_annuity[i].awp);

            //calculating linear
            linear_manager(months_count);

            months_linear[i] = new a_month(i+1, amount_without_percentage_l, amount_with_percentage_l, percentage_l, interest_l, credit_l, remaining_amount_l);

            remaining_amount_l -= months_linear[i].c;
            remaining_amount_l = round(remaining_amount_l, 2);
            System.out.println(months_linear[i].id + " : " + months_linear[i].r + " : " + months_linear[i].c + " : " + months_linear[i].i + " : " + months_linear[i].awp);

        }
    }

    public void annuity_manager(int months_count){  // pagrindinis anuiteto skaiciavimas
        interest_a = percentage_a / 12 / 100;
        amount_without_percentage_a = (interest_a * Math.pow((1 + interest_a), months_count)) / (Math.pow((1 + interest_a), months_count) - 1) * total_amount_a;
        amount_with_percentage_a = amount_without_percentage_a + percentage_a;
        amount_without_percentage_a = round(amount_without_percentage_a, 2);
        amount_with_percentage_a = round(amount_with_percentage_a, 2);

        interest_a = remaining_amount_a * (percentage_a / 12 / 100);
        interest_a = round(interest_a, 2);

        credit_a = amount_without_percentage_a - interest_a;
        credit_a = round(credit_a, 2);
    }

    public void linear_manager(int months_count){   // pagrindinis linijinio skaiciavimas
        interest_l = remaining_amount_l * (percentage_l / 100 / 12);
        interest_l = round(interest_l, 2);

        credit_l = total_amount_l / months_count;
        credit_l = round(credit_l, 2);

        amount_without_percentage_l = interest_l + credit_l;
        amount_with_percentage_l = amount_without_percentage_l + percentage_l;
        amount_without_percentage_l = round(amount_without_percentage_l, 2);
        amount_with_percentage_l = round(amount_with_percentage_l, 2);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}