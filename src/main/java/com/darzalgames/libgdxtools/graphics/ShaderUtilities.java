package com.darzalgames.libgdxtools.graphics;

import java.util.function.Supplier;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Some utility stuff to help deal with the trashfire that is LibGDX's default shader
 */
public class ShaderUtilities {

	private ShaderUtilities() {}

	/**
	 * Creates an object from a supplier in the context of libGDX's old fashioned shader style (version 140)
	 * @param <E>         The type to return
	 * @param initializer A supplier that initializes an object. Usually a constructor
	 * @return An object initialized with libGDX in the old fashioned shader style state
	 */
	public static <E> E initializeWithDefaultShaderSettings(Supplier<E> initializer) {
		// Prepends shader programs created in this function with version 140 GLSL compatibility code
		ShaderProgram.prependVertexCode = "#version 140\n#define varying out\n#define attribute in\n";
		ShaderProgram.prependFragmentCode = "#version 140\n#define varying in\n#define texture2D texture\n#define gl_FragColor fragColor\nout vec4 fragColor;\n";

		// Initialize the shader containing object in the shader compilation environment compatibility state
		E e = initializer.get();

		// Remove the prepending compatibility code, and returns the shader compilation environment to the normal state
		ShaderProgram.prependVertexCode = "";
		ShaderProgram.prependFragmentCode = "";

		return e;
	}

}
