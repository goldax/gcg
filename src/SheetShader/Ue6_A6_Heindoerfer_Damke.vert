varying vec3 eyeSpaceNormal;
uniform float radius;
uniform float angle; 

void main(void)
{
    gl_Position = gl_ModelViewProjectionMatrix * (gl_Vertex + radius * vec4(cos(angle), sin(angle), 0, 0));
	eyeSpaceNormal = gl_NormalMatrix*gl_Normal;
}
