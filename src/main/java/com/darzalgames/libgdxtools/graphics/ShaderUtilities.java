package com.darzalgames.libgdxtools.graphics;

import java.util.function.Supplier;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderUtilities {
	
	private ShaderUtilities() {}
	
	public static <E> E initializeWithDefaultShaderSettings(Supplier<E> initializer) {
		//The sprite batch uses the default shader and requires this prepended code to work
		ShaderProgram.prependVertexCode = "#version 140\n#define varying out\n#define attribute in\n";
		ShaderProgram.prependFragmentCode = "#version 140\n#define varying in\n#define texture2D texture\n#define gl_FragColor fragColor\nout vec4 fragColor;\n";
		E e = initializer.get();
		ShaderProgram.prependVertexCode = "";
		ShaderProgram.prependFragmentCode = "";

		return e;
	}

}
