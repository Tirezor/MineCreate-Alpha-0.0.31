#version 150 core

in vec3 color;
in vec2 tex_coord;

in vec3 cam_pos;
in vec3 frag_pos;

uniform sampler2D tex;

uniform vec4 fog_color;

uniform float fog_start;
uniform float fog_end;

out vec4 frag;

void main()
{
	float distance = length(cam_pos - frag_pos);

	float fs = fog_start;
	float fe = fog_end;

	float fog = (distance - fs) / (fe - fs);
	fog = 1.0f - fog;
	fog = clamp(fog, 0.0f, 1.0f);

	vec4 frag_color = vec4(color.xyz, 1.0f);
	frag = mix(fog_color, frag_color, fog);
}
