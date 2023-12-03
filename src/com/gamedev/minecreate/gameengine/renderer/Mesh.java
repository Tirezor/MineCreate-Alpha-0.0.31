package com.gamedev.minecreate.gameengine.renderer;

import com.gamedev.minecreate.gameengine.camera.Camera;
import com.gamedev.minecreate.gameengine.gameobject.GameObject;
import com.gamedev.minecreate.gameengine.shaders.Shader;
import com.gamedev.minecreate.gameengine.textures.Texture;
import com.gamedev.minecreate.gameengine.window.WindowRenderer;

import java.nio.*;

import org.lwjgl.BufferUtils;

import org.joml.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh
{
	public GameObject obj;
	
	public Shader shader;
	public Texture texture;
	
	public float[] vert_data;
	public float[] col_data;
	public float[] tex_c_data;
	public int[] ind_data;
	
	public int vao;
	public int[] vbo;
	public int ebo;
	
	public FrustumIntersection frustum;
	public Matrix4f pv;
	
	private boolean with_texture;
	
	public Mesh(final GameObject obj, final Shader shader, final Texture texture)
	{
		this.obj = obj;
		
		this.vert_data = obj.vert_data;
		this.col_data = obj.col_data;
		this.tex_c_data = obj.tex_c_data;
		this.ind_data = obj.ind_data;
		
		frustum = new FrustumIntersection();
		pv = new Matrix4f();
		
		with_texture = (texture != null);
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		this.shader = shader;
		
		if(with_texture)
		{
			this.texture = texture;
		}
		
		vbo = new int[3];
		
		FloatBuffer vert_buf = BufferUtils.createFloatBuffer(this.vert_data.length);
		vert_buf.put(this.vert_data);
		vert_buf.flip();
		FloatBuffer col_buf = BufferUtils.createFloatBuffer(this.col_data.length);
		col_buf.put(this.col_data);
		col_buf.flip();
		FloatBuffer tex_c_buf = BufferUtils.createFloatBuffer(this.tex_c_data.length);
		tex_c_buf.put(this.tex_c_data);
		tex_c_buf.flip();
		
		IntBuffer ind_buf = BufferUtils.createIntBuffer(this.ind_data.length);
		ind_buf.put(this.ind_data);
		ind_buf.flip();
		
		vbo[0] = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
		glBufferData(GL_ARRAY_BUFFER, vert_buf, GL_STREAM_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		vbo[1] = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
		glBufferData(GL_ARRAY_BUFFER, col_buf, GL_STREAM_DRAW);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		vbo[2] = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo[2]);
		glBufferData(GL_ARRAY_BUFFER, tex_c_buf, GL_STREAM_DRAW);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		ebo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, ind_buf, GL_STREAM_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
	}
	
	public void recreate()
	{
		glDeleteBuffers(ebo);
		glDeleteBuffers(vbo[2]);
		glDeleteBuffers(vbo[1]);
		glDeleteBuffers(vbo[0]);
		glDeleteVertexArrays(vao);
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		vbo = new int[3];
		
		vert_data = obj.vert_data;
		col_data = obj.col_data;
		tex_c_data = obj.tex_c_data;
		ind_data = obj.ind_data;
		
		FloatBuffer vert_buf = BufferUtils.createFloatBuffer(this.vert_data.length);
		vert_buf.put(this.vert_data);
		vert_buf.flip();
		FloatBuffer col_buf = BufferUtils.createFloatBuffer(this.col_data.length);
		col_buf.put(this.col_data);
		col_buf.flip();
		FloatBuffer tex_c_buf = BufferUtils.createFloatBuffer(this.tex_c_data.length);
		tex_c_buf.put(this.tex_c_data);
		tex_c_buf.flip();
		
		IntBuffer ind_buf = BufferUtils.createIntBuffer(this.ind_data.length);
		ind_buf.put(this.ind_data);
		ind_buf.flip();
		
		vbo[0] = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
		glBufferData(GL_ARRAY_BUFFER, vert_buf, GL_STREAM_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		vbo[1] = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
		glBufferData(GL_ARRAY_BUFFER, col_buf, GL_STREAM_DRAW);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		vbo[2] = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo[2]);
		glBufferData(GL_ARRAY_BUFFER, tex_c_buf, GL_STREAM_DRAW);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		ebo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, ind_buf, GL_STREAM_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
	}
	
	public void update(final Camera camera)
	{
		FloatBuffer matrix_buf = BufferUtils.createFloatBuffer(16);
		
		glUseProgram(shader.program);
		camera.proj.get(matrix_buf);
		matrix_buf.rewind();
		glUniformMatrix4(glGetUniformLocation(shader.program, "proj"), false, matrix_buf);
		camera.view.get(matrix_buf);
		matrix_buf.rewind();
		glUniformMatrix4(glGetUniformLocation(shader.program, "view"), false, matrix_buf);
		obj.model.get(matrix_buf);
		matrix_buf.rewind();
		glUniformMatrix4(glGetUniformLocation(shader.program, "model"), false, matrix_buf);
		glUniform3f(glGetUniformLocation(shader.program, "camera_pos"), camera.pos.x, camera.pos.y, camera.pos.z);
		glUniform4f(glGetUniformLocation(shader.program, "fog_color"), WindowRenderer.color.r, WindowRenderer.color.g, WindowRenderer.color.b, WindowRenderer.color.a);
		glUniform1f(glGetUniformLocation(shader.program, "fog_start"), WindowRenderer.fog_start);
		glUniform1f(glGetUniformLocation(shader.program, "fog_end"), WindowRenderer.fog_end);
		glUseProgram(0);
		
		glBindVertexArray(vao);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glUseProgram(shader.program);
		glActiveTexture(GL_TEXTURE0);
		
		if(with_texture)
		{
			glBindTexture(GL_TEXTURE_2D, texture.texture);
		}
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glDrawElements(GL_TRIANGLES, ind_data.length, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
		glUseProgram(0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	public void destroy()
	{
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		glDeleteBuffers(ebo);
		glDeleteBuffers(vbo[2]);
		glDeleteBuffers(vbo[1]);
		glDeleteBuffers(vbo[0]);
		glDeleteVertexArrays(vao);
	}
}