package utils.Sounds;

import org.lwjgl.openal.AL10;

public class Source {

    private final int sourceId;

    public Source(float vol, float offset) {
        sourceId = AL10.alGenSources();
        AL10.alSourcef(sourceId, AL10.AL_GAIN, vol);
       // AL10.alSourcef(sourceId, AL_SEC_OFFSET);
        AL10.alSourcef(sourceId, AL10.AL_PITCH, 1);
        //AL10.alSource3f(sourceId, AL10.AL_GAIN, 1);
    }

    public void play(int buffer) {
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
        AL10.alSourcePlay(sourceId);
    }

    public void playLooping(int buffer) {
        AL10.alSourcef(sourceId, AL10.AL_PITCH, 1.4f);
        AL10.alSourcei(sourceId, AL10.AL_LOOPING, AL10.AL_TRUE); // Set the loop flag for the source
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
        AL10.alSourcePlay(sourceId);
    }

    public void stop() {
        AL10.alSourceStop(sourceId);
    }

    public void delete() {
        AL10.alDeleteSources(sourceId);
    }
}
