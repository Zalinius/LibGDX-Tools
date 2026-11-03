package com.darzalgames.libgdxtools.maingame;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.badlogic.gdx.graphics.GL20;

public class TestGl20 implements GL20 {

	@Override
	public void glActiveTexture(int texture) {
		// not needed for testing
	}

	@Override
	public void glBindTexture(int target, int texture) {
		// not needed for testing
	}

	@Override
	public void glBlendFunc(int sfactor, int dfactor) {
		// not needed for testing
	}

	@Override
	public void glClear(int mask) {
		// not needed for testing
	}

	@Override
	public void glClearColor(float red, float green, float blue, float alpha) {
		// not needed for testing
	}

	@Override
	public void glClearDepthf(float depth) {
		// not needed for testing
	}

	@Override
	public void glClearStencil(int s) {
		// not needed for testing
	}

	@Override
	public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
		// not needed for testing
	}

	@Override
	public void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int imageSize, Buffer data) {
		// not needed for testing
	}

	@Override
	public void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, Buffer data) {
		// not needed for testing
	}

	@Override
	public void glCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height, int border) {
		// not needed for testing
	}

	@Override
	public void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
		// not needed for testing
	}

	@Override
	public void glCullFace(int mode) {
		// not needed for testing
	}

	@Override
	public void glDeleteTextures(int n, IntBuffer textures) {
		// not needed for testing
	}

	@Override
	public void glDeleteTexture(int texture) {
		// not needed for testing
	}

	@Override
	public void glDepthFunc(int func) {
		// not needed for testing
	}

	@Override
	public void glDepthMask(boolean flag) {
		// not needed for testing
	}

	@Override
	public void glDepthRangef(float zNear, float zFar) {
		// not needed for testing
	}

	@Override
	public void glDisable(int cap) {
		// not needed for testing
	}

	@Override
	public void glDrawArrays(int mode, int first, int count) {
		// not needed for testing
	}

	@Override
	public void glDrawElements(int mode, int count, int type, Buffer indices) {
		// not needed for testing
	}

	@Override
	public void glEnable(int cap) {
		// not needed for testing
	}

	@Override
	public void glFinish() {
		// not needed for testing
	}

	@Override
	public void glFlush() {
		// not needed for testing
	}

	@Override
	public void glFrontFace(int mode) {
		// not needed for testing
	}

	@Override
	public void glGenTextures(int n, IntBuffer textures) {
		// not needed for testing
	}

	@Override
	public int glGenTexture() {
		// not needed for testing
		return 0;
	}

	@Override
	public int glGetError() {
		// not needed for testing
		return 0;
	}

	@Override
	public void glGetIntegerv(int pname, IntBuffer params) {
		// not needed for testing
	}

	@Override
	public String glGetString(int name) {
		// not needed for testing
		return null;
	}

	@Override
	public void glHint(int target, int mode) {
		// not needed for testing
	}

	@Override
	public void glLineWidth(float width) {
		// not needed for testing
	}

	@Override
	public void glPixelStorei(int pname, int param) {
		// not needed for testing
	}

	@Override
	public void glPolygonOffset(float factor, float units) {
		// not needed for testing
	}

	@Override
	public void glReadPixels(int x, int y, int width, int height, int format, int type, Buffer pixels) {
		// not needed for testing
	}

	@Override
	public void glScissor(int x, int y, int width, int height) {
		// not needed for testing
	}

	@Override
	public void glStencilFunc(int func, int ref, int mask) {
		// not needed for testing
	}

	@Override
	public void glStencilMask(int mask) {
		// not needed for testing
	}

	@Override
	public void glStencilOp(int fail, int zfail, int zpass) {
		// not needed for testing
	}

	@Override
	public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, Buffer pixels) {
		// not needed for testing
	}

	@Override
	public void glTexParameterf(int target, int pname, float param) {
		// not needed for testing
	}

	@Override
	public void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, Buffer pixels) {
		// not needed for testing
	}

	@Override
	public void glViewport(int x, int y, int width, int height) {
		// not needed for testing
	}

	@Override
	public void glAttachShader(int program, int shader) {
		// not needed for testing
	}

	@Override
	public void glBindAttribLocation(int program, int index, String name) {
		// not needed for testing
	}

	@Override
	public void glBindBuffer(int target, int buffer) {
		// not needed for testing
	}

	@Override
	public void glBindFramebuffer(int target, int framebuffer) {
		// not needed for testing
	}

	@Override
	public void glBindRenderbuffer(int target, int renderbuffer) {
		// not needed for testing
	}

	@Override
	public void glBlendColor(float red, float green, float blue, float alpha) {
		// not needed for testing
	}

	@Override
	public void glBlendEquation(int mode) {
		// not needed for testing
	}

	@Override
	public void glBlendEquationSeparate(int modeRGB, int modeAlpha) {
		// not needed for testing
	}

	@Override
	public void glBlendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
		// not needed for testing
	}

	@Override
	public void glBufferData(int target, int size, Buffer data, int usage) {
		// not needed for testing
	}

	@Override
	public void glBufferSubData(int target, int offset, int size, Buffer data) {
		// not needed for testing
	}

	@Override
	public int glCheckFramebufferStatus(int target) {
		// not needed for testing
		return 0;
	}

	@Override
	public void glCompileShader(int shader) {
		// not needed for testing
	}

	@Override
	public int glCreateProgram() {
		// not needed for testing
		return 0;
	}

	@Override
	public int glCreateShader(int type) {
		// not needed for testing
		return 0;
	}

	@Override
	public void glDeleteBuffer(int buffer) {
		// not needed for testing
	}

	@Override
	public void glDeleteBuffers(int n, IntBuffer buffers) {
		// not needed for testing
	}

	@Override
	public void glDeleteFramebuffer(int framebuffer) {
		// not needed for testing
	}

	@Override
	public void glDeleteFramebuffers(int n, IntBuffer framebuffers) {
		// not needed for testing
	}

	@Override
	public void glDeleteProgram(int program) {
		// not needed for testing
	}

	@Override
	public void glDeleteRenderbuffer(int renderbuffer) {
		// not needed for testing
	}

	@Override
	public void glDeleteRenderbuffers(int n, IntBuffer renderbuffers) {
		// not needed for testing
	}

	@Override
	public void glDeleteShader(int shader) {
		// not needed for testing
	}

	@Override
	public void glDetachShader(int program, int shader) {
		// not needed for testing
	}

	@Override
	public void glDisableVertexAttribArray(int index) {
		// not needed for testing
	}

	@Override
	public void glDrawElements(int mode, int count, int type, int indices) {
		// not needed for testing
	}

	@Override
	public void glEnableVertexAttribArray(int index) {
		// not needed for testing
	}

	@Override
	public void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
		// not needed for testing
	}

	@Override
	public void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
		// not needed for testing
	}

	@Override
	public int glGenBuffer() {
		// not needed for testing
		return 0;
	}

	@Override
	public void glGenBuffers(int n, IntBuffer buffers) {
		// not needed for testing
	}

	@Override
	public void glGenerateMipmap(int target) {
		// not needed for testing
	}

	@Override
	public int glGenFramebuffer() {
		// not needed for testing
		return 0;
	}

	@Override
	public void glGenFramebuffers(int n, IntBuffer framebuffers) {
		// not needed for testing
	}

	@Override
	public int glGenRenderbuffer() {
		// not needed for testing
		return 0;
	}

	@Override
	public void glGenRenderbuffers(int n, IntBuffer renderbuffers) {
		// not needed for testing
	}

	@Override
	public String glGetActiveAttrib(int program, int index, IntBuffer size, IntBuffer type) {
		// not needed for testing
		return null;
	}

	@Override
	public String glGetActiveUniform(int program, int index, IntBuffer size, IntBuffer type) {
		// not needed for testing
		return null;
	}

	@Override
	public void glGetAttachedShaders(int program, int maxcount, Buffer count, IntBuffer shaders) {
		// not needed for testing
	}

	@Override
	public int glGetAttribLocation(int program, String name) {
		// not needed for testing
		return 0;
	}

	@Override
	public void glGetBooleanv(int pname, Buffer params) {
		// not needed for testing
	}

	@Override
	public void glGetBufferParameteriv(int target, int pname, IntBuffer params) {
		// not needed for testing
	}

	@Override
	public void glGetFloatv(int pname, FloatBuffer params) {
		// not needed for testing
	}

	@Override
	public void glGetFramebufferAttachmentParameteriv(int target, int attachment, int pname, IntBuffer params) {
		// not needed for testing
	}

	@Override
	public void glGetProgramiv(int program, int pname, IntBuffer params) {
		// not needed for testing
	}

	@Override
	public String glGetProgramInfoLog(int program) {
		// not needed for testing
		return null;
	}

	@Override
	public void glGetRenderbufferParameteriv(int target, int pname, IntBuffer params) {
		// not needed for testing
	}

	@Override
	public void glGetShaderiv(int shader, int pname, IntBuffer params) {
		// not needed for testing
	}

	@Override
	public String glGetShaderInfoLog(int shader) {
		// not needed for testing
		return null;
	}

	@Override
	public void glGetShaderPrecisionFormat(int shadertype, int precisiontype, IntBuffer range, IntBuffer precision) {
		// not needed for testing
	}

	@Override
	public void glGetTexParameterfv(int target, int pname, FloatBuffer params) {
		// not needed for testing
	}

	@Override
	public void glGetTexParameteriv(int target, int pname, IntBuffer params) {
		// not needed for testing
	}

	@Override
	public void glGetUniformfv(int program, int location, FloatBuffer params) {
		// not needed for testing
	}

	@Override
	public void glGetUniformiv(int program, int location, IntBuffer params) {
		// not needed for testing
	}

	@Override
	public int glGetUniformLocation(int program, String name) {
		// not needed for testing
		return 0;
	}

	@Override
	public void glGetVertexAttribfv(int index, int pname, FloatBuffer params) {
		// not needed for testing
	}

	@Override
	public void glGetVertexAttribiv(int index, int pname, IntBuffer params) {
		// not needed for testing
	}

	@Override
	public void glGetVertexAttribPointerv(int index, int pname, Buffer pointer) {
		// not needed for testing
	}

	@Override
	public boolean glIsBuffer(int buffer) {
		// not needed for testing
		return false;
	}

	@Override
	public boolean glIsEnabled(int cap) {
		// not needed for testing
		return false;
	}

	@Override
	public boolean glIsFramebuffer(int framebuffer) {
		// not needed for testing
		return false;
	}

	@Override
	public boolean glIsProgram(int program) {
		// not needed for testing
		return false;
	}

	@Override
	public boolean glIsRenderbuffer(int renderbuffer) {
		// not needed for testing
		return false;
	}

	@Override
	public boolean glIsShader(int shader) {
		// not needed for testing
		return false;
	}

	@Override
	public boolean glIsTexture(int texture) {
		// not needed for testing
		return false;
	}

	@Override
	public void glLinkProgram(int program) {
		// not needed for testing
	}

	@Override
	public void glReleaseShaderCompiler() {
		// not needed for testing
	}

	@Override
	public void glRenderbufferStorage(int target, int internalformat, int width, int height) {
		// not needed for testing
	}

	@Override
	public void glSampleCoverage(float value, boolean invert) {
		// not needed for testing
	}

	@Override
	public void glShaderBinary(int n, IntBuffer shaders, int binaryformat, Buffer binary, int length) {
		// not needed for testing
	}

	@Override
	public void glShaderSource(int shader, String string) {
		// not needed for testing
	}

	@Override
	public void glStencilFuncSeparate(int face, int func, int ref, int mask) {
		// not needed for testing
	}

	@Override
	public void glStencilMaskSeparate(int face, int mask) {
		// not needed for testing
	}

	@Override
	public void glStencilOpSeparate(int face, int fail, int zfail, int zpass) {
		// not needed for testing
	}

	@Override
	public void glTexParameterfv(int target, int pname, FloatBuffer params) {
		// not needed for testing
	}

	@Override
	public void glTexParameteri(int target, int pname, int param) {
		// not needed for testing
	}

	@Override
	public void glTexParameteriv(int target, int pname, IntBuffer params) {
		// not needed for testing
	}

	@Override
	public void glUniform1f(int location, float x) {
		// not needed for testing
	}

	@Override
	public void glUniform1fv(int location, int count, FloatBuffer v) {
		// not needed for testing
	}

	@Override
	public void glUniform1fv(int location, int count, float[] v, int offset) {
		// not needed for testing
	}

	@Override
	public void glUniform1i(int location, int x) {
		// not needed for testing
	}

	@Override
	public void glUniform1iv(int location, int count, IntBuffer v) {
		// not needed for testing
	}

	@Override
	public void glUniform1iv(int location, int count, int[] v, int offset) {
		// not needed for testing
	}

	@Override
	public void glUniform2f(int location, float x, float y) {
		// not needed for testing
	}

	@Override
	public void glUniform2fv(int location, int count, FloatBuffer v) {
		// not needed for testing
	}

	@Override
	public void glUniform2fv(int location, int count, float[] v, int offset) {
		// not needed for testing
	}

	@Override
	public void glUniform2i(int location, int x, int y) {
		// not needed for testing
	}

	@Override
	public void glUniform2iv(int location, int count, IntBuffer v) {
		// not needed for testing
	}

	@Override
	public void glUniform2iv(int location, int count, int[] v, int offset) {
		// not needed for testing
	}

	@Override
	public void glUniform3f(int location, float x, float y, float z) {
		// not needed for testing
	}

	@Override
	public void glUniform3fv(int location, int count, FloatBuffer v) {
		// not needed for testing
	}

	@Override
	public void glUniform3fv(int location, int count, float[] v, int offset) {
		// not needed for testing
	}

	@Override
	public void glUniform3i(int location, int x, int y, int z) {
		// not needed for testing
	}

	@Override
	public void glUniform3iv(int location, int count, IntBuffer v) {
		// not needed for testing
	}

	@Override
	public void glUniform3iv(int location, int count, int[] v, int offset) {
		// not needed for testing
	}

	@Override
	public void glUniform4f(int location, float x, float y, float z, float w) {
		// not needed for testing
	}

	@Override
	public void glUniform4fv(int location, int count, FloatBuffer v) {
		// not needed for testing
	}

	@Override
	public void glUniform4fv(int location, int count, float[] v, int offset) {
		// not needed for testing
	}

	@Override
	public void glUniform4i(int location, int x, int y, int z, int w) {
		// not needed for testing
	}

	@Override
	public void glUniform4iv(int location, int count, IntBuffer v) {
		// not needed for testing
	}

	@Override
	public void glUniform4iv(int location, int count, int[] v, int offset) {
		// not needed for testing
	}

	@Override
	public void glUniformMatrix2fv(int location, int count, boolean transpose, FloatBuffer value) {
		// not needed for testing
	}

	@Override
	public void glUniformMatrix2fv(int location, int count, boolean transpose, float[] value, int offset) {
		// not needed for testing
	}

	@Override
	public void glUniformMatrix3fv(int location, int count, boolean transpose, FloatBuffer value) {
		// not needed for testing
	}

	@Override
	public void glUniformMatrix3fv(int location, int count, boolean transpose, float[] value, int offset) {
		// not needed for testing
	}

	@Override
	public void glUniformMatrix4fv(int location, int count, boolean transpose, FloatBuffer value) {
		// not needed for testing
	}

	@Override
	public void glUniformMatrix4fv(int location, int count, boolean transpose, float[] value, int offset) {
		// not needed for testing
	}

	@Override
	public void glUseProgram(int program) {
		// not needed for testing
	}

	@Override
	public void glValidateProgram(int program) {
		// not needed for testing
	}

	@Override
	public void glVertexAttrib1f(int indx, float x) {
		// not needed for testing
	}

	@Override
	public void glVertexAttrib1fv(int indx, FloatBuffer values) {
		// not needed for testing
	}

	@Override
	public void glVertexAttrib2f(int indx, float x, float y) {
		// not needed for testing
	}

	@Override
	public void glVertexAttrib2fv(int indx, FloatBuffer values) {
		// not needed for testing
	}

	@Override
	public void glVertexAttrib3f(int indx, float x, float y, float z) {
		// not needed for testing
	}

	@Override
	public void glVertexAttrib3fv(int indx, FloatBuffer values) {
		// not needed for testing
	}

	@Override
	public void glVertexAttrib4f(int indx, float x, float y, float z, float w) {
		// not needed for testing
	}

	@Override
	public void glVertexAttrib4fv(int indx, FloatBuffer values) {
		// not needed for testing
	}

	@Override
	public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, Buffer ptr) {
		// not needed for testing
	}

	@Override
	public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, int ptr) {
		// not needed for testing
	}

}
