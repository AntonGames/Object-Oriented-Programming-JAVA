import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class code_modifier {

    JFrame frame;
    JTextArea code;
    JButton save;

    public void set_window(){
        frame = new JFrame("Code modifier");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLayout(null);

        code = new JTextArea();
        code.setBounds(0, 65, 500, 460);
        code.setVisible(true);
        code.setFont(new Font("Calibri", Font.PLAIN, 20));
        frame.add(code);

        save = new JButton("Save");
        save.setBounds(195,20, 100, 25);
        save.setVisible(true);
        save.addActionListener(e -> save_code_to_file());
        frame.add(save);
    }

    public void save_code_to_file() {
        try {
            File code_file = new File("code.txt");
            FileWriter writer = new FileWriter(code_file, false);
            BufferedWriter b_writer = new BufferedWriter(writer);
            b_writer.write(code.getText());
            b_writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        code_modifier c_m = new code_modifier();
        c_m.set_window();
    }
}
