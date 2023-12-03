package com.gamedev.minecreate.gameengine.shaders;

import com.gamedev.minecreate.console.*;

import java.io.*;

import static org.lwjgl.opengl.GL20.*;

public class Shader
{
	public int program;
	public int vert_shader;
	public int frag_shader;
	
	public Shader(final String vert_shader_path, final String frag_shader_path)
	{
		try
		{
			vert_shader = load_shader(GL_VERTEX_SHADER, vert_shader_path);
			frag_shader = load_shader(GL_FRAGMENT_SHADER, frag_shader_path);
			
			program = glCreateProgram();
			
			glAttachShader(program, vert_shader);
			glAttachShader(program, frag_shader);
			
			glBindAttribLocation(program, 0, "pos");
			glBindAttribLocation(program, 1, "col");
			glBindAttribLocation(program, 2, "tex_c");
			
			glLinkProgram(program);
			glValidateProgram(program);
			
			ConsoleMC.print("<" + vert_shader_path + "> - shader loaded successfully!", PrintType.INFO);
			ConsoleMC.print("<" + frag_shader_path + "> - shader loaded successfully!", PrintType.INFO);
		}
		
		catch(Exception e)
		{
			ConsoleMC.print("<" + vert_shader_path + "> - shader NOT loaded!", PrintType.ERROR);
			ConsoleMC.print("<" + frag_shader_path + "> - shader NOT loaded!", PrintType.ERROR);
			
			e.printStackTrace();
		}
	}
	
	public void destroy()
	{
		glDetachShader(program, vert_shader);
		glDetachShader(program, frag_shader);
		glDeleteShader(vert_shader);
		glDeleteShader(frag_shader);
		glDeleteProgram(program);
	}
	
	private int load_shader(final int type, final String path) throws Exception
	{
		StringBuilder source = new StringBuilder();
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(Shader.class.getResourceAsStream(path))))
		{
			String line;
			
			while((line = reader.readLine()) != null)
			{
				source.append(line).append("\n");
			}
		}
		
		int shader = glCreateShader(type);
		
		glShaderSource(shader, source);
		glCompileShader(shader);
		
		return shader;
	}
}