package com.arman.voip;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class AudioInputDevice extends Thread {

    private static AudioFormat defaultFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0f, 16, 2, 4, 8000.0f, true);

    private AudioFormat format;
    private TargetDataLine dataLine;
    private OutputStream stream;

    public AudioInputDevice(OutputStream stream) {
        this(stream, defaultFormat);
    }

    public AudioInputDevice(OutputStream stream, AudioFormat format) {
        this.stream = stream;
        this.format = format;
        this.dataLine = null;
    }

    public static Map<String, TargetDataLine.Info> listMicrophones() {
        Map<String, TargetDataLine.Info> mics = new HashMap<>();
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo : mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] lineInfos = mixer.getTargetLineInfo();
            if (lineInfos.length >= 1 && lineInfos[0].getLineClass().equals(TargetDataLine.class)) {
                mics.put(mixerInfo.getName(), (TargetDataLine.Info) lineInfos[0]);
            }
        }
        return mics;
    }

    private boolean open() {
        if (dataLine != null) {
            return true;
        }
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        try {
            dataLine = (TargetDataLine) AudioSystem.getLine(info);
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
                    int read = dataLine.read(buffer, 0, buffer.length);
                    stream.write(buffer, 0, read);
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
