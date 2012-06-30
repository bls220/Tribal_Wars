precision mediump float;		// Set the default precision to medium. We don't need as high of a precision in the fragment shader.
uniform sampler2D u_Texture;	// Texture

varying vec4 v_Color;			// This is the color from the vertex shader interpolated per fragment.
varying vec2 v_TexCoord;		// TexMap from vert shader interpolated per fragment

void main(){

	gl_FragColor = texture2D(u_Texture, v_TexCoord);
	
	//if pixel is pure gray-scale overlay color
	if( (gl_FragColor.r == gl_FragColor.b) && (gl_FragColor.b == gl_FragColor.g) ) 
		gl_FragColor *= v_Color;		
}