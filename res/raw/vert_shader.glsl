uniform mat4 u_MVPMatrix;		// A constant representing the combined model/view/projection matrix.
uniform vec4 u_Color;			// constant color information we will pass in.

attribute vec4 a_Position;		// Per-vertex position information we will pass in.
attribute vec2 a_TexCoord;		// Per-vertex texture map data

varying vec2 v_TexCoord;		// Passed to frag shader
varying vec4 v_Color;			// This will be passed into the fragment shader.

void main(){					// The entry point for our vertex shader.                              
	v_TexCoord = a_TexCoord;	// Pass TexMap to frag shader
	v_Color = u_Color;			// Pass the color through to the fragment shader. It will be interpolated across the triangle.

	// gl_Position is a special variable used to store the final position. Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
	gl_Position = u_MVPMatrix * a_Position;
}