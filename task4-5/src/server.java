import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class server extends JFrame {

    private ServerSocket serverSocket;
    private Socket s;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private JTextArea jTextArea1;
    private JTextField jTextField1;

    public server() {
        initComponents();
    }

    private void initServer() {
        try {
            serverSocket = new ServerSocket(9999);
            s = serverSocket.accept();
            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initComponents() {

        JScrollPane jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();
        JButton jButton1 = new JButton();
        jTextField1 = new JTextField();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server");

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton1.setText("Send");
        jButton1.setToolTipText("");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTextField1)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton1)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton1))
                                .addGap(22, 22, 22))
        );

        pack();
    }

    private void jButton1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (!jTextField1.getText().isEmpty()) {
            String msg = jTextField1.getText();
            SendMessage(msg);
            jTextField1.setText("");
        }
    }

    public static void main(String[] args) throws IOException {
        server ser = new server();
        ser.setVisible(true);
        ser.setResizable(false);
        ser.initServer();
        ser.ReceiveMessage();
    }

    private void SendMessage(String msg) {
        if (out == null) return;

        try {
            out.writeObject(msg);
            jTextArea1.append("You: " + msg + "\n");
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    private void ReceiveMessage() {
        String msg;
        while (true) {
            try {
                msg = (String) in.readObject();
                jTextArea1.append("Client: " + msg + "\t"+ "\n");
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
    }
}