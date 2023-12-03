#version 150 core

in vec3 color;
in vec2 tex_coord;

uniform sampler2D tex;

out vec4 frag;

void main()
{
	frag = texture(tex, tex_coord) * vec4(color.xyz, 1.0f);
}
