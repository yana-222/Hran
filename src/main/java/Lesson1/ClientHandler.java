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
         try(
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream());
        )
         {
             System.out.println("Clent " + socket.getInetAddress() + " connected");
             while (true){
                String command = in.readUTF();
                if("upload".equals(command)){
                    try {
                        File file = new File("server" + File.separator + in.readUTF());
                        if(!file.exists()){
                            file.createNewFile();
                        }
                        FileOutputStream fos = new FileOutputStream(file);

                        long size = in.readLong();
                        byte[] buffer = new byte [8 * 1024];
                        for (int i= 0; i < (size + (buffer.length - 1))/(buffer.length); i++) {
                            int read = in.read(buffer);
                            fos.write(buffer,0,read);
                        }
                        fos.close();
                        out.writeUTF("OK");
                    } catch (Exception e){
                        out.writeUTF("Error");
                    }
                }
                if("exit".equals(command)){
                    System.out.println("Clent " + socket.getInetAddress() + " disconnected");
                    break;
                }
                 System.out.println(command);
                out.writeUTF(command);
             }
        }
         catch (IOException e) {
             e.printStackTrace();
         }
    }
}
