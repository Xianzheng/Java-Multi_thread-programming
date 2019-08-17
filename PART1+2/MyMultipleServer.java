


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MyMultipleServer {
    public static void main(String[] args) {
        
        try{
            MyMultipleServer server = new MyMultipleServer();
            server.startServer(args[0],args[1]);
        }catch(ArrayIndexOutOfBoundsException ex){
            System.out.println("args[0] need to indicate the path of file, ./indicate the current path");
            System.out.println("args[1] need to indicate the port of server");
            System.out.println("Both of them can not be null");
            System.exit(1);
        }
        
    }


    public  void startServer(String directory,String s_port){
        try {
            int port = Integer.parseInt(s_port);
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Service is running at port: "+port);

            while(true){
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new Handler(socket));
                System.out.println("Get a new request");
                thread.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
