package com.arman.voip.messages;

import java.io.File;

public class FileMessage extends Message {

    public FileMessage(String senderName, File message) {
        super(senderName, message);
    }

}
