package brains;

import com.example.lab_2_javafx.a_month;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class report_manager {

    private PrintWriter printer;

    private final a_month[] months;

    public report_manager(a_month[] months) {
        this.months = months;
        String file_path = "ataskaita.txt";
        try{
            FileWriter writer = new FileWriter(file_path, false);
            printer = new PrintWriter(writer);
        }catch (IOException e){
            System.out.println("Failed writting to file!");
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public void write_message() {
        printer.println("Paskola bus mokama " + months.length + " mėnesių.");
        printer.println("");

        printer.println("Kiekvieno mėnesio ataskaita: ");
        printer.println("");

        for (a_month month : months) {
            printer.println("Mėnesio nr: " + month.id);
            printer.println("Likusi suma: " + month.r);
            printer.println("Kreditas: " + month.c);
            printer.println("Palūkanos: " + month.i);
            printer.println("Bendra mėnesio įmoka: " + month.awp);
            printer.println("///////////////////////////////////////////////");
            printer.println("");
        }

        printer.close();
    }
}
