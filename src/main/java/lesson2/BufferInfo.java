package lesson2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class BufferInfo {
    public static void main(String[] args) throws IOException {
        // у буфера выделяют:
        // - position (изначально pos = 0);
        // - limit (количество учитываемых позиций);
        // - capacity (емкость, в байтах);
        // - mark

        FileChannel channel = new RandomAccessFile("Client" + File.separator + "1.txt","rw").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(10);
        System.out.println(buffer);
        channel.read(buffer);
        buffer.flip(); // "перевод в режим чтения", limit = pos + pos =0

     /*   while (buffer.hasRemaining()){
            System.out.print((char) buffer.get());
        }
        System.out.println("\n"+buffer);*/
        byte[] byteBuf = new byte[10];
        int pos = 0;
        while (buffer.hasRemaining()){
            byteBuf[pos++] = buffer.get();
        }
        System.out.println(new String(byteBuf, StandardCharsets.UTF_8));
        buffer.rewind(); // перевод буфера в режим записи и pos = 0;
    }
}
