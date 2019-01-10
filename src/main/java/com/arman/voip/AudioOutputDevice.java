package com.arman.voip;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class AudioOutputDevice extends Thread {

    private static AudioFormat defaultFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0f, 16, 2, 4, 8000.0f, true);

    private SourceDataLine dataLine;
    private AudioFormat format;
    private InputStream stream;

    public AudioOutputDevice(InputStream stream) {
        this(stream, defaultFormat);
    }

    public AudioOutputDevice(InputStream stream, AudioFormat format) {
        this.stream = stream;
        this.format = format;
        this.dataLine = null;
    }

    private boolean open() {
        if (dataLine != null) {
            return true;
        }
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        try {
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open(format);
            return true;
        } catch (LineUnavailableException e) {
            return false;
        }
    }

    @Override
    public void run() {
        if (this.open()) {
            dataLine.start();
            try {
                while (true) {
                    byte[] buffer = new byte[dataLine.getBufferSize() / 5];
                    int read = stream.read(buffer, 0, buffer.length);
                    dataLine.write(buffer, 0, read);
                }
            } catch (IOException e) {
                shutdown();
            }
        }
    }

    public void shutdown() {
        dataLine.stop();
        dataLine.flush();
        try {
            stream.close();
        } catch (IOException e) {

        }
    }

}
