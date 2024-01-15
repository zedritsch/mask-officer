package de.zedritsch.wrapper;

import java.io.File;
import javax.sound.sampled.*;

public class AudioStreamPlayer {
	private final Clip AUDIO_STREAM = getClip();
	private boolean fail = false;
	public boolean loop = false;

	public void open(final String PATH) {
		fail = false;
		try {
			AUDIO_STREAM.open(AudioSystem.getAudioInputStream(new File(PATH).getAbsoluteFile()));
		} catch (Exception e) {
			fail = true;
		}
	}

	public boolean fail() {
		return fail;
	}

	public void start() {
		if (loop)
			AUDIO_STREAM.loop(-1);
		else
			AUDIO_STREAM.start();
	}

	public void stop() {
		AUDIO_STREAM.stop();
	}

	public void setVolume(final float VOLUME) {
		((FloatControl) AUDIO_STREAM.getControl(FloatControl.Type.MASTER_GAIN)).setValue(VOLUME);
	}

	public void close() {
		AUDIO_STREAM.close();
	}

	private Clip getClip() {
		try {
			return AudioSystem.getClip();
		} catch (Exception e) {
			return null;
		}
	}
}