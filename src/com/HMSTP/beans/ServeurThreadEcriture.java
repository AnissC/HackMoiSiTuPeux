import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;

public class ServeurThreadEcriture {
    public ArrayList <Message> listMessage;

    public ServeurThreadEcriture(Socket s, ArrayList <Message> list){
        listMessage = list;
    }

    public void message () throws IOException{
        Writer w;
        Message m;

        while(true){
            synchronized (listMessage) {
                if (!listMessage.isEmpty()) {
                    m = listMessage.remove(0);
                    w = new OutputStreamWriter(m.getSocket().getOutputStream());
                    w.write(m.getMessage());
                }
            }

        }

    }


}
