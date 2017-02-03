varying vec3 eyeSpaceNormal;
uniform float size; 

void main(void)
{
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    gl_Position.x = gl_Position.x*size;
    gl_Position.y = gl_Position.y*size;
	eyeSpaceNormal = gl_NormalMatrix*gl_Normal;
}
