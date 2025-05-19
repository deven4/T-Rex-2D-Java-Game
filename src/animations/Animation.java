package animations;

import java.awt.image.BufferedImage;

public class Animation {
    private boolean looping;
    private final BufferedImage[] frames;

    public Animation(BufferedImage[] frames, boolean looping) {
        this.frames = frames;
        this.looping = looping;
    }

    public BufferedImage[] getFrames() {
        return frames;
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    @Override
    public String toString() {
        return "Animation{" +
                "looping=" + looping +
                ", frames=" + frames.length +
                '}';
    }
}
