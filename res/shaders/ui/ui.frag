#version 150 core

in vec3 color;
in vec2 tex_coord;

uniform sampler2D tex;

uniform float alpha;
uniform float with_texture;

out vec4 frag;

void main()
{
	if(with_texture == 1)
	{
		if(texture(tex, tex_coord).a <= 0.0f)
		{
			discard;
		}

		vec4 frag_color = texture(tex, tex_coord) * vec4(color.xyz, alpha);
		frag = frag_color;
	}

	else
	{
		vec4 frag_color = vec4(color.xyz, alpha);
		frag = frag_color;
	}
}
