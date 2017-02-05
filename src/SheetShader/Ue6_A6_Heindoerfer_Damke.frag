varying vec3 eyeSpaceNormal;

void main()
{
    gl_FragColor = vec4(normalize(eyeSpaceNormal), 1);
}
