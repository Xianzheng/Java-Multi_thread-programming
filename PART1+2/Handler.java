
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Handler implements  Runnable {

    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private BufferedReader reader;
    private PrintWriter writer;
    private BufferedWriter out;

    public Handler(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run(){
        try{
            //File dir = new File(this.getClass().getResource("/").getPath());
            //WEB_ROOT = dir.getName();
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            writer = new PrintWriter(new OutputStreamWriter(outputStream));
            String msg;
            StringBuffer request = new StringBuffer();
            while((msg = reader.readLine()) != null && msg.length() > 0){
                request.append(msg);
                request.append("\n");
            }
            System.out.println(request);
            String[] msgs = request.toString().split(" ");
            
            if(msgs[1].equals("/")){
                msgs[1]="/LoginPage.html";
            }
            //System.out.println(msgs[1]);
            File directory = new File("");
            String WEB_ROOT = directory.getAbsolutePath();
           // System.out.println("temp path= "+temp);
            
            
            Path path = Paths.get(WEB_ROOT + msgs[1]);
            //System.out.println(path.toString());
            byte[] data =  Files.readAllBytes(path);
            
            
            File log = new File(WEB_ROOT+"\\log");
            out = new BufferedWriter(new FileWriter(log,true));
            InetAddress me = InetAddress.getLocalHost();
            Date nowTime=new Date(); 
            System.out.println(nowTime); 
            
            if(!log.exists()){
                log.createNewFile();              
                out.write(me.getHostAddress()+" ["+nowTime+" -0800] "+msgs[0]+" "+WEB_ROOT + msgs[1]+" 200 "+"\r\n");
                out.flush();
                out.close();
            }else{
                out.write(me.getHostAddress()+" ["+nowTime+" -0800] "+""+msgs[0]+" "+WEB_ROOT + msgs[1]+" 200 "+"\r\n");
                out.flush();
                out.close();
            }
            
            
           
            outputStream.write(data);
            
            outputStream.close();
            inputStream.close();
            reader.close();
            
        }catch (IOException e){
            
            writer.write("HTTP/1.1 404 ERROR:FILE NOT FINDED");
            writer.close();
        }
    }
}
