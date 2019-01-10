package com.arman.voip;

public class AEC {

    private byte[] echoFreeSignal;
    private byte[] echoSignal;
    private byte[] transposeOfSpeakerSignal;
    private double[] weights;

    public AEC() {

    }

    public AEC(byte[] echoSignal, byte[] transposeOfSpeakerSignal, double[] weights) {
        this.set(echoSignal, transposeOfSpeakerSignal, weights);
    }

    public void set(byte[] echoSignal, byte[] transposeOfSpeakerSignal, double[] weights) {
        this.echoSignal = echoSignal;
        this.transposeOfSpeakerSignal = transposeOfSpeakerSignal;
        this.weights = weights;
    }

    public byte[] apply(byte[] bytes) {
        echoFreeSignal = new byte[bytes.length];
        for (int i = 0; i < echoFreeSignal.length; i++) {
            echoFreeSignal[i] = (byte) (echoSignal[i] - transposeOfSpeakerSignal[i] * weights[i]);
        }
        return echoFreeSignal;
    }

}
