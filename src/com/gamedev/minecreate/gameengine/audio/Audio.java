package com.gamedev.minecreate.gameengine.audio;

import java.nio.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.*;

import org.joml.*;

public class Audio
{
	public Sound sound;
	
	public int buffer;
	public int source;
	
	public FloatBuffer source_pos;
	public FloatBuffer listener_pos;
	public FloatBuffer source_vel;
	public FloatBuffer listener_vel;
	public FloatBuffer listener_ori;
	
	public boolean is_delete;
	
	public boolean is_static;
	public boolean is_loop;
	
	public Audio(final Sound sound, final boolean is_static, final boolean is_loop, final float pitch, final float gain, final Vector3f source_position, final Vector3f listener_position, final Vector3f source_velocity, final Vector3f listener_velocity, final Vector3f listener_front, final Vector3f listener_up) throws Exception
	{
		this.is_static = is_static;
		this.is_loop = is_loop;
		
		is_delete = false;
		
		if(this.is_static)
		{
			source_pos = BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f}).rewind();
			listener_pos = BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f}).rewind();
			source_vel = BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f}).rewind();
			listener_vel = BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f}).rewind();
			listener_ori = BufferUtils.createFloatBuffer(6).put(new float[] {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}).rewind();
		}
		
		else
		{
			source_pos = BufferUtils.createFloatBuffer(3).put(new float[] {source_position.x, source_position.y, source_position.z}).rewind();
			listener_pos = BufferUtils.createFloatBuffer(3).put(new float[] {listener_position.x, listener_position.y, listener_position.z}).rewind();
			source_vel = BufferUtils.createFloatBuffer(3).put(new float[] {source_velocity.x, source_velocity.y, source_velocity.z}).rewind();
			listener_vel = BufferUtils.createFloatBuffer(3).put(new float[] {listener_velocity.x, listener_velocity.y, listener_velocity.z}).rewind();
			listener_ori = BufferUtils.createFloatBuffer(6).put(new float[] {listener_front.x, listener_front.y, listener_front.z, listener_up.x, listener_up.y, listener_up.z}).rewind();
		}
		
		buffer = AL10.alGenBuffers();
		
		this.sound = sound;
		
		AL10.alBufferData(buffer, sound.get_data().format, sound.get_data().data, sound.get_data().samplerate);
		sound.get_data().dispose();
		
		source = AL10.alGenSources();
		
		AL10.alSourcei(source, AL10.AL_BUFFER, buffer);
		AL10.alSourcef(source, AL10.AL_PITCH, pitch);
		AL10.alSourcef(source, AL10.AL_GAIN, gain);
		AL10.alSource(source, AL10.AL_POSITION, source_pos);
		AL10.alSource(source, AL10.AL_VELOCITY, source_vel);
		
		AL10.alListener(AL10.AL_POSITION, listener_pos);
		AL10.alListener(AL10.AL_VELOCITY, listener_vel);
		AL10.alListener(AL10.AL_ORIENTATION, listener_ori);
	}
	
	public void update(final Vector3f listener_position, final Vector3f listener_velocity, final Vector3f listener_front, final Vector3f listener_up)
	{
		if(is_static)
		{
			listener_pos = BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f}).rewind();
			listener_vel = BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f}).rewind();
			listener_ori = BufferUtils.createFloatBuffer(6).put(new float[] {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}).rewind();
		}
		
		else
		{
			listener_pos = BufferUtils.createFloatBuffer(3).put(new float[] {listener_position.x, listener_position.y, listener_position.z}).rewind();
			listener_vel = BufferUtils.createFloatBuffer(3).put(new float[] {listener_velocity.x, listener_velocity.y, listener_velocity.z}).rewind();
			listener_ori = BufferUtils.createFloatBuffer(6).put(new float[] {listener_front.x, listener_front.y, listener_front.z, listener_up.x, listener_up.y, listener_up.z}).rewind();
		}
		
		AL10.alListener(AL10.AL_POSITION, listener_pos);
		AL10.alListener(AL10.AL_VELOCITY, listener_vel);
		AL10.alListener(AL10.AL_ORIENTATION, listener_ori);
		
		if(is_loop)
		{
			if(AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED)
			{
				play();
			}
		}
		
		else
		{
			if(AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED)
			{
				destroy();
			}
		}
	}
	
	public void play()
	{	
		AL10.alSourcePlay(source);
	}
	
	public void pause()
	{	
		AL10.alSourcePause(source);
	}
	
	public void stop()
	{	
		AL10.alSourceStop(source);
	}
	
	public void set_gain(final float gain)
	{
		AL10.alSourcef(source, AL10.AL_GAIN, gain);
	}
	
	public void destroy()
	{
		AL10.alDeleteSources(source);
		AL10.alDeleteBuffers(buffer);
		is_delete = true;
	}
}