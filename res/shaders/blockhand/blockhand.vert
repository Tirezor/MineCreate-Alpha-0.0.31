#version 150 core

in vec3 pos;
in vec3 col;
in vec2 tex_c;

uniform mat4 proj;
uniform mat4 view;
uniform mat4 model;

uniform vec3 camera_pos;

out vec3 frag_pos;
out vec3 cam_pos;

out vec3 color;
out vec2 tex_coord;

void main()
{
	cam_pos = camera_pos;
	frag_pos = vec3(model * vec4(pos.xyz, 1.0f));
	gl_Position = proj * view * model * vec4(pos.xyz, 1.0f);
	color = col;
	tex_coord = tex_c;
}
