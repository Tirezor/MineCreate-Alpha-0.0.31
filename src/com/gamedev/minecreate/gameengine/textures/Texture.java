package com.gamedev.minecreate.gameengine.textures;

import com.gamedev.minecreate.console.*;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Texture
{
	public int texture;
	
	public BufferedImage img;
	public ByteBuffer buf;
	
	private boolean is_opengl;
	
	public Texture(final String file_name, final boolean is_opengl)
	{
		this.is_opengl = is_opengl;
		
		try
		{
			img = ImageIO.read(Texture.class.getResourceAsStream(file_name));
			int[] pxs = new int[img.getWidth() * img.getHeight()];
			img.getRGB(0, 0, img.getWidth(), img.getHeight(), pxs, 0, img.getWidth());
			buf = ByteBuffer.allocateDirect(img.getWidth() * img.getHeight() * 4);
			
			for(int y = 0; y < img.getHeight(); y++)
			{
				for(int x = 0; x < img.getWidth(); x++)
				{
					int px = pxs[y * img.getWidth() + x];
					buf.put((byte)((px >> 16) & 0xFF));
					buf.put((byte)((px >> 8) & 0xFF));
		            buf.put((byte)(px & 0xFF));
		            buf.put((byte)((px >> 24) & 0xFF));
				}
			}
			
			buf.flip();
			
			if(this.is_opengl)
			{
				texture = glGenTextures();
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, texture);
				
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.getWidth(), img.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
				
			    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

			    glBindTexture(GL_TEXTURE_2D, 0);
			}
			
			ConsoleMC.print("<" + file_name + "> - texture loaded successfully!", PrintType.INFO);
		}
		
		catch(Exception e)
		{
			ConsoleMC.print("<" + file_name + "> - texture NOT loaded!", PrintType.ERROR);
			
			e.printStackTrace();
		}
	}
	
	public void destroy()
	{
		if(is_opengl)
		{
			glBindTexture(GL_TEXTURE_2D, 0);
			glDeleteTextures(texture);
		}
	}
}