#version 150 core

in vec3 pos;
in vec3 col;
in vec2 tex_c;

out vec3 color;
out vec2 tex_coord;

void main()
{
	gl_Position = vec4(pos.xyz, 1.0f);
	color = col;
	tex_coord = tex_c;
}
