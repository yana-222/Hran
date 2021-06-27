package Lesson1;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private final Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream())
        ) {
            System.out.println("Client " + socket.getRemoteSocketAddress() + " connected");
            while (true) {
                String[] cmd = in.readUTF().split(" ");
                if ("upload".equals(cmd[0])) {
                    try {
                        File file = new File("server" + File.separator + cmd[1]);
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        FileOutputStream fos = new FileOutputStream(file);

                        long size = in.readLong();
                        byte[] buffer = new byte[8 * 1024];
                        // Объяснить почему так реализован цикл: нет ответа
                        for (int i = 0; i < (size + (buffer.length - 1))/ (buffer.length) ; i++) {
                            int read = in.read(buffer);
                            fos.write(buffer, 0, read);
                        }
                        fos.close();
                        out.writeUTF("uploading_OK");
                    } catch (Exception e) {
                        out.writeUTF("Error");
                    }
                }
                if ("download".equals(cmd[0])) {
                    sendFile(cmd[1]);
                }

                if ("exit".equals(cmd[0])) {
                    System.out.println("Client " + socket.getInetAddress() + " disconnected");
                    break;
                }

                out.writeUTF(fromArray(cmd));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendFile(String filename) {
        try {
            File file = new File("server" + File.separator + filename);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
            long fileLength = file.length();
            FileInputStream fis = new FileInputStream(file);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            out.writeUTF("download " +filename);
            out.writeLong(fileLength);
            int read =0;
            byte[] buffer = new byte [8*1024];
            while ((read = fis.read(buffer))!=-1){
                out.write(buffer,0,read);
            };
            out.flush();

            fis.close();

            String status = in.readUTF();
            System.out.println("sending status: " + status);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String fromArray (String[] arr){
        String str = "";
        for (int i = 0; i < arr.length; i++) {
            if (i != (arr.length - 1)) {
                str = str+ arr[i] + " ";
            } else str = str + arr[i];
        }
        return str;
    }
}
