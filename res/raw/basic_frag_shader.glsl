precision highp float;			// Set the default precision to low. We don't need as high of a precision in the fragment shader.

varying vec4 v_Color;			// This is the color from the vertex shader interpolated per fragment.

void main(){
	gl_FragColor = v_Color;		
}