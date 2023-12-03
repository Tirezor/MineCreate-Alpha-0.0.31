package com.gamedev.minecreate.ui;

import com.gamedev.minecreate.gameengine.camera.Camera;
import com.gamedev.minecreate.gameengine.gameobject.GameObject;
import com.gamedev.minecreate.gameengine.textures.Texture;
import com.gamedev.minecreate.resources.ResourcesMC;

import java.util.ArrayList;

import org.joml.*;

import static org.lwjgl.opengl.GL20.*;

public class TextUI
{
	public GameObject obj;
	
	public ArrayList<Float> vert_data;
	public ArrayList<Float> col_data;
	public ArrayList<Float> t_coord_data;
	public ArrayList<Integer> ind_data;
	
	public float[] vert;
	public float[] col;
	public float[] tex_c;
	public int[] ind;
	
	public boolean enabled;
	
	public int count;
	
	public float alpha;
	
	public TextUI(final Texture texture)
	{
		vert_data = new ArrayList<Float>();
		col_data = new ArrayList<Float>();
		t_coord_data = new ArrayList<Float>();
		ind_data = new ArrayList<Integer>();
		
		count = 0;
		
		vert_add(new Vector3f(-1, -1, 0));
		vert_add(new Vector3f(-1, 1, 0));
		vert_add(new Vector3f(1, 1, 0));
		vert_add(new Vector3f(1, -1, 0));
		
		render_char(' ', false);
		
		add_col(new Vector4f(1, 1, 1, 1));
		
		add_ind();
		
		convert();
		
		enabled = true;
		
		obj = new GameObject(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), ResourcesMC.ui_shader, texture, vert, col, tex_c, ind);
	}
	
	public void update(final Camera camera)
	{
		glUseProgram(obj.mesh.shader.program);
		glUniform1f(glGetUniformLocation(obj.mesh.shader.program, "alpha"), alpha);
		glUniform1f(glGetUniformLocation(obj.mesh.shader.program, "with_texture"), 1);
		glUseProgram(0);
		
		if(enabled)
		{
			obj.update(camera, true);
		}
	}
	
	public void destroy()
	{
		obj.destroy();
	}
	
	public void render_char(final char c, final boolean shadow)
	{
		int X;
		int Y;
		
		switch(c)
		{
		case '0':
			X = 2;
			Y = 2;
			break;
		
		case '1':
			X = 3;
			Y = 2;
			break;
		
		case '2':
			X = 4;
			Y = 2;
			break;
		
		case '3':
			X = 5;
			Y = 2;
			break;
		
		case '4':
			X = 6;
			Y = 2;
			break;
		
		case '5':
			X = 7;
			Y = 2;
			break;
		
		case '6':
			X = 8;
			Y = 2;
			break;
		
		case '7':
			X = 9;
			Y = 2;
			break;
		
		case '8':
			X = 10;
			Y = 2;
			break;
		
		case '9':
			X = 11;
			Y = 2;
			break;
		
		case 'A':
			X = 19;
			Y = 2;
			break;
		
		case 'B':
			X = 20;
			Y = 2;
			break;
		
		case 'C':
			X = 21;
			Y = 2;
			break;
		
		case 'D':
			X = 22;
			Y = 2;
			break;
		
		case 'E':
			X = 0;
			Y = 3;
			break;
		
		case 'F':
			X = 1;
			Y = 3;
			break;
		
		case 'G':
			X = 2;
			Y = 3;
			break;
		
		case 'H':
			X = 3;
			Y = 3;
			break;
		
		case 'I':
			X = 4;
			Y = 3;
			break;
		
		case 'J':
			X = 5;
			Y = 3;
			break;
		
		case 'K':
			X = 6;
			Y = 3;
			break;
		
		case 'L':
			X = 7;
			Y = 3;
			break;
		
		case 'M':
			X = 8;
			Y = 3;
			break;
		
		case 'N':
			X = 9;
			Y = 3;
			break;
		
		case 'O':
			X = 10;
			Y = 3;
			break;
		
		case 'P':
			X = 11;
			Y = 3;
			break;
		
		case 'Q':
			X = 12;
			Y = 3;
			break;
		
		case 'R':
			X = 13;
			Y = 3;
			break;
		
		case 'S':
			X = 14;
			Y = 3;
			break;
		
		case 'T':
			X = 15;
			Y = 3;
			break;
		
		case 'U':
			X = 16;
			Y = 3;
			break;
		
		case 'V':
			X = 17;
			Y = 3;
			break;
		
		case 'W':
			X = 18;
			Y = 3;
			break;
		
		case 'X':
			X = 19;
			Y = 3;
			break;
		
		case 'Y':
			X = 20;
			Y = 3;
			break;
		
		case 'Z':
			X = 21;
			Y = 3;
			break;
		
		case 'a':
			X = 5;
			Y = 4;
			break;
		
		case 'b':
			X = 6;
			Y = 4;
			break;
		
		case 'c':
			X = 7;
			Y = 4;
			break;
		
		case 'd':
			X = 8;
			Y = 4;
			break;
		
		case 'e':
			X = 9;
			Y = 4;
			break;
		
		case 'f':
			X = 10;
			Y = 4;
			break;
		
		case 'g':
			X = 11;
			Y = 4;
			break;
		
		case 'h':
			X = 12;
			Y = 4;
			break;
		
		case 'i':
			X = 13;
			Y = 4;
			break;
		
		case 'j':
			X = 14;
			Y = 4;
			break;
		
		case 'k':
			X = 15;
			Y = 4;
			break;
		
		case 'l':
			X = 16;
			Y = 4;
			break;
		
		case 'm':
			X = 17;
			Y = 4;
			break;
		
		case 'n':
			X = 18;
			Y = 4;
			break;
		
		case 'o':
			X = 19;
			Y = 4;
			break;
		
		case 'p':
			X = 20;
			Y = 4;
			break;
		
		case 'q':
			X = 21;
			Y = 4;
			break;
		
		case 'r':
			X = 22;
			Y = 4;
			break;
		
		case 's':
			X = 0;
			Y = 5;
			break;
		
		case 't':
			X = 1;
			Y = 5;
			break;
		
		case 'u':
			X = 2;
			Y = 5;
			break;
		
		case 'v':
			X = 3;
			Y = 5;
			break;
		
		case 'w':
			X = 4;
			Y = 5;
			break;
		
		case 'x':
			X = 5;
			Y = 5;
			break;
		
		case 'y':
			X = 6;
			Y = 5;
			break;
		
		case 'z':
			X = 7;
			Y = 5;
			break;
		
		case ' ':
			X = 5;
			Y = 1;
			break;
		
		case '.':
			X = 0;
			Y = 2;
			break;
		
		case ',':
			X = 21;
			Y = 1;
			break;
		
		case ':':
			X = 12;
			Y = 2;
			break;
		
		case ';':
			X = 13;
			Y = 2;
			break;
		
		case '!':
			X = 10;
			Y = 1;
			break;
		
		case '?':
			X = 17;
			Y = 2;
			break;
		
		case '(':
			X = 17;
			Y = 1;
			break;
		
		case ')':
			X = 18;
			Y = 1;
			break;
		
		case '&':
			X = 15;
			Y = 1;
			break;
		
		case '#':
			X = 12;
			Y = 1;
			break;
		
		case '+':
			X = 20;
			Y = 1;
			break;
		
		case '-':
			X = 22;
			Y = 1;
			break;
		
		case '*':
			X = 19;
			Y = 1;
			break;
		
		case '/':
			X = 1;
			Y = 2;
			break;
		
		case '=':
			X = 15;
			Y = 2;
			break;
		
		default:
			X = 17;
			Y = 2;
			break;
		}
		
		float ox = 1.0f / 23.0f;
		float oy = 1.0f / 6.0f;
		
		float s = 8.0f;
		
		float ofx = ox / s;
		float ofy = oy / s;
		
		if(shadow)
		{
			t_coord_add(new Vector2f(X * ox - ofx, Y * oy + oy + ofy));
			t_coord_add(new Vector2f(X * ox + ox - ofx, Y * oy + oy + ofy));
			t_coord_add(new Vector2f(X * ox + ox - ofx, Y * oy + ofy));
			t_coord_add(new Vector2f(X * ox - ofx, Y * oy + ofy));
		}
		
		else
		{
			t_coord_add(new Vector2f(X * ox, Y * oy + oy));
			t_coord_add(new Vector2f(X * ox + ox, Y * oy + oy));
			t_coord_add(new Vector2f(X * ox + ox, Y * oy));
			t_coord_add(new Vector2f(X * ox, Y * oy));
		}
	}
	
	public void convert()
	{
		vert = new float[vert_data.size()];
		for(int i = 0; i < vert_data.size(); i++)
		{
			vert[i] = vert_data.get(i);
		}
		
		col = new float[col_data.size()];
		for(int i = 0; i < col_data.size(); i++)
		{
			col[i] = col_data.get(i);
		}
		
		tex_c = new float[t_coord_data.size()];
		for(int i = 0; i < t_coord_data.size(); i++)
		{
			tex_c[i] = t_coord_data.get(i);
		}
		
		ind = new int[ind_data.size()];
		for(int i = 0; i < ind_data.size(); i++)
		{
			ind[i] = ind_data.get(i);
		}
	}
	
	public void add_col(final Vector4f vec4)
	{
		col_add(new Vector3f(vec4.x, vec4.y, vec4.z));
		col_add(new Vector3f(vec4.x, vec4.y, vec4.z));
		col_add(new Vector3f(vec4.x, vec4.y, vec4.z));
		col_add(new Vector3f(vec4.x, vec4.y, vec4.z));
		alpha = vec4.w;
	}
	
	public void add_ind()
	{
		ind_add(0 + 4 * count, 1 + 4 * count, 2 + 4 * count);
		ind_add(0 + 4 * count, 2 + 4 * count, 3 + 4 * count);
		count++;
	}
	
	public void vert_add(final Vector3f vec3)
	{
		vert_data.add(vec3.x);
		vert_data.add(vec3.y);
		vert_data.add(vec3.z);
	}
	
	public void col_add(final Vector3f vec3)
	{
		col_data.add(vec3.x);
		col_data.add(vec3.y);
		col_data.add(vec3.z);
	}
	
	public void t_coord_add(final Vector2f vec2)
	{
		t_coord_data.add(vec2.x);
		t_coord_data.add(vec2.y);
	}
	
	public void ind_add(final int i0, final int i1, final int i2)
	{
		ind_data.add((i0));
		ind_data.add((i1));
		ind_data.add((i2));
	}
}