package Lesson1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame {
    private final Socket socket;
    private final DataOutputStream out;
    private final DataInputStream in;

    public Client() throws IOException {
        socket = new Socket("localhost",5678);
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        setSize(300,300);
        JPanel panel = new JPanel(new GridLayout(2,1));

        JButton btnSend = new JButton("SEND");
        JTextField textField = new JTextField();

        btnSend.addActionListener(a->{
            String message = textField.getText();
            if (message.equals("exit")){
                System.out.println("Client disconnected");
            }
            sendFile(message);
        });

        panel.add(textField);
        panel.add(btnSend);

        add(panel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                sendMessage("exit");
            }
        });

        setVisible(true);
    }

    private void sendFile(String filename) {
        try{
            File file = new File("Client" + File.separator + filename);
            if(!file.exists()){
                throw new FileNotFoundException();
            }

            long fileLength = file.length();
            FileInputStream fis = new FileInputStream(file);

            out.writeUTF("upload");
            out.writeUTF(filename);
            out.writeLong(fileLength);
            int read =0;
            byte[] buffer = new byte [8*1024];
            while ((read = fis.read(buffer))!=-1){
                out.write(buffer,0,read);
            };
            out.flush();

            String status = in.readUTF();
            System.out.println("sending status: " + status);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(String message){
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new Client();
    }


}
