import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class client extends Thread {

    JFrame frame = new JFrame();

    Socket s;
    ObjectOutputStream out;
    ObjectInputStream in;

    public client() throws IOException {
        initComponents();
        s = new Socket("localhost", 9999);
        out = new ObjectOutputStream(s.getOutputStream());
        in = new ObjectInputStream(s.getInputStream());
    }

    private void initComponents() {
        JScrollPane jScrollPane1 = new JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        JButton jButton1 = new JButton();
        jTextField1 = new javax.swing.JTextField();

        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Client");

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton1.setText("Send");
        jButton1.setToolTipText("");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTextField1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton1)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton1))
                                .addGap(22, 22, 22))
        );
        frame.pack();
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (!jTextField1.getText().equals("")) {
            String msg = jTextField1.getText();
            SendMessage(msg);
            jTextField1.setText("");
        }
    }

    public static void main(String[] args) throws IOException {
        client c = new client();
        c.frame.setVisible(true);
        c.frame.setResizable(false);
        c.ReceiveMessage();
    }

    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;

    private void SendMessage(String msg) {
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
                jTextArea1.append("Friend: " + msg + "\n");
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
    }
}