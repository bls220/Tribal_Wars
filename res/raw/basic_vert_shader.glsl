uniform mat4 u_MVPMatrix;		// A constant representing the combined model/view/projection matrix.

attribute vec4 a_Position;		// Per-vertex position information we will pass in.

varying vec4 v_Color;			// This will be passed into the fragment shader.

void main(){					// The entry point for our vertex shader.                              
	v_Color = vec4(1.0,0.0,1.0,1.0);
	gl_Position = u_MVPMatrix * a_Position;
}