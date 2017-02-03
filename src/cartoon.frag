varying vec3 eyeSpaceNormal;
uniform vec4 color;


vec4 toonify(float intensity) 
{
    vec4 colorFinal = color;
    if (intensity > 0.95)
        colorFinal = vec4(1.0,1.0,1.0,1.0) * color;
    else if (intensity > 0.5)
        colorFinal = vec4(0.7,0.7,0.7,1.0) * color;
    else if (intensity > 0.05)
        colorFinal = vec4(0.35,0.35,0.35,1.0) * color;
    else
        colorFinal = vec4(0.1,0.1,0.1,1.0) * color;
    return colorFinal;
}

void main()
{
	vec3 normal = normalize(eyeSpaceNormal);
    float intensity = dot(normalize(gl_LightSource[0].position.xyz), normal);
    gl_FragColor = toonify(intensity);

}

