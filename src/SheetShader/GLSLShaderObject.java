package SheetShader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jogamp.opengl.GL2;

public class GLSLShaderObject
{
    private int vID;
    private int fID;
    private int program;
    public String[] vsrc;
    public String[] fsrc;

    //Initialise shader by attaching shaders 
    public void init( GL2 gl )
    {
        try
        {
            attachShaders(gl);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int getProgram()
    {
    	return program;
    }
    
    public int addUniform( GL2 gl, String name, float value)
    {
    	int uniformLocation = gl.glGetUniformLocation(program, name);
        gl.glUseProgram(program);
    	gl.glUniform1f(uniformLocation, value);
        gl.glUseProgram(0);
    	return uniformLocation;
    }

    public int addUniform( GL2 gl, String name, float[] value)
    {
    	int uniformLocation = gl.glGetUniformLocation(program, name);
        gl.glUseProgram(program);
        switch (value.length)
        {
	        case 1: gl.glUniform1f(uniformLocation, value[0]);
	        		break;
	        case 2: gl.glUniform2f(uniformLocation, value[0], value[1]);
    				break;
	        case 3: gl.glUniform3f(uniformLocation, value[0], value[1], value[2]);
    				break;
	        case 4: gl.glUniform4f(uniformLocation, value[0], value[1], value[2], value[3]);
    				break;
	        default:
        }
        	
        gl.glUseProgram(0);
    	return uniformLocation;
    }
    
    public int updateUniform( GL2 gl, String name, float value)
    {
    	float[] v = {value};
    	return addUniform(gl, name, v);
    }
    

    public int updateUniform( GL2 gl, String name, float[] value)
    {
    		return addUniform(gl, name, value);
    }
    
    
    // load fragment shader
    public void loadFragmentShader(String name )
    {
        System.out.println("\nFragment Shader: \n======================================" );
        String[] fragmentShaderSrc = loadShader(name);
        System.out.println("======================================\n" );
        fsrc = fragmentShaderSrc;
    }

    // load vertex shader
    public void loadVertexShader(String name )
    {
        System.out.println("\nVertex Shader: \n======================================" );
        String[] vertexShaderSrc = loadShader(name);
        System.out.println("======================================\n" );
        vsrc = vertexShaderSrc;
    }

    // load shader source file
    public String[] loadShader( String name )
    {
        StringBuilder sb = new StringBuilder();
        try
        {
        	//read shader source file
            InputStream is = getClass().getResourceAsStream(name);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
                sb.append('\n');
            }
            is.close();
        }
        catch (Exception e)
        {
            System.err.println("Error while reading shader file \""+ name + "\"\n");
            //e.printStackTrace();
        }
        
        //output input of the shader source file
        System.out.println(sb.toString());
        
        //return input of the shader source file
        return new String[]{ sb.toString() };
    }

    //attach fragment and vertex shader
    private void attachShaders( GL2 gl ) throws Exception
    {
    	//create empty fragment and vertex shader object
    	//and get ID of the shader object 
    	// -> ID = gl.glCreateShader(<shader-type>);
        vID = gl.glCreateShader(GL2.GL_VERTEX_SHADER);
        fID = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);
        
        //set shader source code
        gl.glShaderSource(vID, 1, vsrc, null, 0);
        gl.glShaderSource(fID, 1, fsrc, null, 0);
        
        //compile shader
        gl.glCompileShader(vID);
        gl.glCompileShader(fID);
        
        //attach shaders to the shader program
        program = gl.glCreateProgram();
        gl.glAttachShader(program, vID);
        gl.glAttachShader(program, fID);
        //link program
        gl.glLinkProgram(program);
        
        int[] params = new int[]{0};
        //check if link operation was successful
        gl.glGetProgramiv(program, GL2.GL_LINK_STATUS, params, 0);
        
        //if link status was not successful:
        if (params[0] != GL2.GL_TRUE) {
        	//output link status
            System.err.println("link status: " + params[0]);
            
            //get length of the information log
            gl.glGetProgramiv(program, GL2.GL_INFO_LOG_LENGTH, params, 0);
            System.err.println("info log length: " + params[0]);

            //get information log of the program object
            byte[] infoLog = new byte[params[0]];
            gl.glGetProgramInfoLog(program, params[0], params, 0, infoLog, 0);
            System.err.println("info log: " + new String(infoLog));
        }
    }
    
    //activate shader
    public int activate( GL2 gl )
    {
        gl.glUseProgram(program);
        return program;
    }

    //deactivate shader
    public void deactivate( GL2 gl )
    {
        gl.glUseProgram(0);
    }
}