package Utils.Sounds;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;


import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;


public class WaveData {

    final int format;
    final int sampleRate;
    final int totalBytes;
    final int bytesPerFrame;
    final ByteBuffer data;

    private final AudioInputStream audioStream;
    private final byte[] dataArray;

    private WaveData(AudioInputStream stream) {
        this.audioStream = stream;
        AudioFormat audioFormat = stream.getFormat();
        format = getOpenAlFormat(audioFormat.getChannels(), audioFormat.getSampleSizeInBits());
        this.sampleRate = (int) audioFormat.getSampleRate();
        this.bytesPerFrame = audioFormat.getFrameSize();
        this.totalBytes = (int) (stream.getFrameLength() * bytesPerFrame);
        this.data = BufferUtils.createByteBuffer(totalBytes);
        this.dataArray = new byte[totalBytes];
        loadData();
    }


    protected void dispose() {
        try {
            audioStream.close();
            data.clear();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void loadData() {
        try {
            int bytesRead = audioStream.read(dataArray, 0, totalBytes);
            data.clear();
            data.put(dataArray, 0, bytesRead);
            data.flip();
        } catch (IOException e) {
            System.err.println(e.getMessage() + ". Couldn't read bytes from audio stream!");
        }
    }

    public static WaveData create(String file) {
        InputStream stream = WaveData.class.getResourceAsStream(file);
        if (stream == null) {
            System.err.println("Couldn't find file: " + file);
            return null;
        }
        InputStream bufferedInput = new BufferedInputStream(stream);
        AudioInputStream audioStream = null;
        try {
            audioStream = AudioSystem.getAudioInputStream(bufferedInput);
        } catch (UnsupportedAudioFileException | IOException e) {
            System.err.println(e.getMessage());
        }
        assert audioStream != null;
        return new WaveData(audioStream);
    }

    private static int getOpenAlFormat(int channels, int bitsPerSample) {
        if (channels == 1) {
            return bitsPerSample == 8 ? AL10.AL_FORMAT_MONO8 : AL10.AL_FORMAT_MONO16;
        } else {
            return bitsPerSample == 8 ? AL10.AL_FORMAT_STEREO8 : AL10.AL_FORMAT_STEREO16;
        }
    }
}
