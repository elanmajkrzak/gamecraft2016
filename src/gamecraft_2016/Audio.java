package gamecraft_2016;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

public class Audio {
	  /** Buffers hold sound data. */
	  private IntBuffer buffer;
	 
	  /** Sources are points emitting sound. */
	  private IntBuffer source;
	  
	  /** Position of the source sound. */
	  private FloatBuffer sourcePos;
	   
	  /** Velocity of the source sound. */
	  private FloatBuffer sourceVel;
	   
	  /** Position of the listener. */
	  private FloatBuffer listenerPos;
	   
	  /** Velocity of the listener. */
	  private FloatBuffer listenerVel;
	   
	  /** Orientation of the listener. (first 3 elements are "at", second 3 are "up") */
	  private FloatBuffer listenerOri;
	  
	  private int error;
	  
	  private static int NUM_SONGS = 3;
	  
	  public Audio() {
		  this.buffer = BufferUtils.createIntBuffer(NUM_SONGS);
		  this.source = BufferUtils.createIntBuffer(NUM_SONGS);
		  this.sourcePos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
		  this.sourceVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
		  this.listenerPos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
		  this.listenerVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
		  this.listenerOri =  (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f }).rewind();
		  error = AL10.alGetError();
		  
		  AL10.alGenBuffers(buffer);
		  error = AL10.alGetError();
		  if(error != AL10.AL_NO_ERROR) {
			  try {
				throw new IOException("Failed to generate wavData buffer");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(error);
			}
		  }
		  
	  }
	  
	  public void killALData() {
		  AL10.alDeleteSources(source);
		  AL10.alDeleteBuffers(buffer);
	  }
	  
	  public void play() {
		  AL10.alSourcePlay(source.get(0));
	  }
	  
	  public void stop() {
		  System.out.println("Audio stop");
		  AL10.alSourceStop(source.get(0));
		  AL10.alDeleteSources(source);
	  }
	  
	  public void loadFile(String path, int pos) {
		  try {
			  
			  AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path));
			  BufferedInputStream bufferedInputStream = new BufferedInputStream(audioInputStream);
			  audioInputStream = new AudioInputStream(bufferedInputStream, audioInputStream.getFormat(), audioInputStream.getFrameLength());
			  
			  audioInputStream = convertToPCM(audioInputStream);

			  WaveData waveFile = WaveData.create(audioInputStream);
		  
			  AL10.alBufferData(buffer.get(pos), 	waveFile.format, waveFile.data, waveFile.samplerate);
			  waveFile.dispose();
			  
			// Bind the buffer with the source.
			  AL10.alGenSources(source);

			  error = AL10.alGetError();
			  if(error != AL10.AL_NO_ERROR) {
				  try {
					throw new IOException("Failed to generate wavData buffer");
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println(error);
				}
			  }
			   
			  AL10.alSourcei(source.get(0), AL10.AL_BUFFER,   buffer.get(pos) );
			  AL10.alSourcef(source.get(0), AL10.AL_PITCH,    1.0f          );
			  AL10.alSourcef(source.get(0), AL10.AL_GAIN,     1.0f          );
			  AL10.alSourcefv(source.get(0), AL10.AL_POSITION, (FloatBuffer) sourcePos.position(pos*3)     );
			  AL10.alSourcefv(source.get(0), AL10.AL_VELOCITY, (FloatBuffer) sourceVel.position(pos*3)     );
			  AL10.alSourcei(source.get(0), AL10.AL_LOOPING,  AL10.AL_TRUE  );
			  
			  setListenerValues();

		  }
		  catch (Exception e) {
			  e.printStackTrace();
		  }
	  }
	  
	  // src at: http://www.cs.unc.edu/~luv/teaching/COMP110/programs/AudioPlayer.java
	  private static AudioInputStream convertToPCM(AudioInputStream audioInputStream)
	    {
	        AudioFormat m_format = audioInputStream.getFormat();

	        if ((m_format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) &&
	            (m_format.getEncoding() != AudioFormat.Encoding.PCM_UNSIGNED))
	        {
	            AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
	                m_format.getSampleRate(), 16,
	                m_format.getChannels(), m_format.getChannels() * 2,
	                m_format.getSampleRate(), m_format.isBigEndian());
	            audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
	    }

	    return audioInputStream;
	}
	  
	  public void setListenerValues() {
		  AL10.alListenerfv(AL10.AL_POSITION,    listenerPos);
		  AL10.alListenerfv(AL10.AL_VELOCITY,    listenerVel);
		  AL10.alListenerfv(AL10.AL_ORIENTATION, listenerOri);
	  }
	  
	  
}
