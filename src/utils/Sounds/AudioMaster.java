package utils.Sounds;

import org.lwjgl.openal.*;

import java.util.ArrayList;
import java.util.List;

public class AudioMaster {

    private static long audioDevice;
    private static long audioContext;
    private static final List<Integer> buffers = new ArrayList<>();

    public static void init() throws Exception {
        String defaultDeviceName = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
        assert defaultDeviceName != null;
        audioDevice = ALC10.alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = ALC10.alcCreateContext(audioDevice, attributes);
        ALC10.alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
        if (!alCapabilities.OpenAL10) {
            throw new Exception("Audio library not supported");
        }
    }

    public static int loadSound(String file) {
        int buffer = AL10.alGenBuffers();
        buffers.add(buffer);
        WaveData waveData = WaveData.create(file);
        assert waveData != null;
        AL10.alBufferData(buffer, waveData.format, waveData.data, waveData.sampleRate);
        waveData.dispose();
        return buffer;
    }

    public void destroy() {
        for (int buffer : buffers) AL10.alDeleteBuffers(buffer);
        ALC10.alcDestroyContext(audioContext);
        ALC10.alcCloseDevice(audioDevice);
    }
}
