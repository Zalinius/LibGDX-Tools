package com.darzalgames.libgdxtools.audio.engine;

import java.util.List;

import com.darzalgames.libgdxtools.audio.composition.Song;
import com.darzalgames.libgdxtools.audio.engine.sound.SimpleSound;

public interface ControlledProgrammedMusic<E extends Song> {

	public void queueSong(final E song);
	public void playSound(final List<SimpleSound> simpleSounds);

	public float getMusicVolume();
	public void setMusicVolume(float newVolume);
	public float getSoundEffectVolume();
	public void setSoundEffectVolume(float newVolume);
	public void setShouldTemporarilyMute(boolean showTemporarilyMute);
	public boolean getShouldTemporarilyMute(); 
	public void temporarilyMute();
	public void untemporarilyMute();
	public float getDecorativeBPS();
	
	public void shutdown();
	public void dispose();


}
