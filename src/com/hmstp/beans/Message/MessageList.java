package com.hmstp.beans.Message;

import com.hmstp.beans.Jeu.*;
import java.util.ArrayList;


public class MessageList extends MessageNombre{
    ArrayList<Participant> listMessageParticipant;

    public MessageList(int perdant, String m, ArrayList<Participant> listMessageParticipant){
        super(perdant,m);
        this.listMessageParticipant = listMessageParticipant;
    }
}
