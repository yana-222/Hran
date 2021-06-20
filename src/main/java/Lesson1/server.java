package Lesson1;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 1. +JavaIO
    // 2. NIO
    // 3. Netty
    // 4. Stream API
    // 5. Code review
    // 6. Maven
    // 7. JMemoryM
    // 8. Code Review
    // ТЗ:
    // 1. Copy, rename, cut, paste 2. Delete 3. List files 4. Users features 5. Download / upload 6. Make dir
    // 7. Search 8. Navigation 9. Fix total space 10. Logging 11. Total + Free space 12. Sort (name, date, type)
    // 13. Bucket 14. Logging
public class server {
    // корректный вывод статуса в консоли
    public server(){
        ExecutorService service = Executors.newFixedThreadPool(4);
        try (ServerSocket server = new ServerSocket(5678)){
            System.out.println("Server started");
            while(true){
                service.execute((new ClientHandler(server.accept())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new server();
    }
}
