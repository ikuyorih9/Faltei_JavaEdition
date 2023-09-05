package ArvoreB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;


public class Iostream {
    private final String CODIFICACAO = "ISO-8859-1";
    private String filename;
    private String path;
    private File file;
    
    
    public Iostream(String path, String filename){
        this.path = path;
        this.filename = filename;
        file = new File(path, filename);
    }

    public Iostream(String filename){
        this.path = null;
        this.filename = filename;
        file = new File(filename);
    }

    public Iostream(File file){
        this.file = file;
        this.path = file.getAbsolutePath();
        this.filename = file.getName();
    }

    //Escreve um inteiro no arquivo, na posição offset.
    public void write(int value, int offset){
        try{
            RandomAccessFile stream = new RandomAccessFile(file, "rw");
            stream.seek(offset);
            stream.writeInt(value);
            stream.close();
        }
        catch(IOException e){
            System.out.println("Erro: " + e.getMessage());
        }
        
    }

    //Escreve um byte no arquivo, na posição offset.
    public void write(byte value, int offset){
        try{
            RandomAccessFile stream = new RandomAccessFile(file, "rw");
            stream.seek(offset);
            stream.writeByte(value);
            stream.close();
        }
        catch(IOException e){
            System.out.println("Erro: " + e.getMessage());
        }
    }

    //Escreve uma string no arquivo, na posição offset.
    public void write(String value, int offset){
        int length = value.length();
        char[] valueChar = value.toCharArray();
        for(int i = 0; i <  length; i++){
            write((byte)valueChar[i], offset + i);
        }
    }

    //Lê um byte na posição offset
    public byte read(int offset){
        byte readen = -1;
        try{
            RandomAccessFile stream = new RandomAccessFile(file, "r");
            stream.seek(offset);
            readen = stream.readByte();
            stream.close();
        }
        catch(IOException e){
            System.out.println("Erro: " + e.getMessage());
        }
        return readen;
    }

    //Lê uma sequencia de bytes na posição offset, de tamanho length.
    public byte [] read(int offset, int length){
        byte [] bytes = new byte[length];
        try{
            RandomAccessFile stream = new RandomAccessFile(file, "r");
            stream.seek(offset);
            for(int i = 0; i < length; i++){
                bytes [i] = read(offset + i);
            }
            stream.close();
        }
        catch(IOException e){
            System.out.println("Erro: " + e.getMessage());
        }
        return bytes;
    }

    public int byteToInt(byte [] in){
        return ByteBuffer.wrap(in).getInt();
    }

    public int iRead(int offset){
        byte [] bytes = read(offset, 4);
        return byteToInt(bytes);
    }

    public char cRead(int offset){
        return (char)read(offset);
    }

    public String sRead(int offset, int length){
        byte [] bytes = read(offset, length);
        String s = null;
        try{
            s = new String(bytes, CODIFICACAO);
        }
        catch(UnsupportedEncodingException e){
            System.out.println(e.getMessage());
        }
        return s;
    }
}
