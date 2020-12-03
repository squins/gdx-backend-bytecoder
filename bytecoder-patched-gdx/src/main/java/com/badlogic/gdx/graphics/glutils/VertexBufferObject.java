package com.badlogic.gdx.graphics.glutils;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.utils.BufferUtils;

public class VertexBufferObject implements VertexData {
    final VertexAttributes attributes;
    final FloatBuffer buffer;
    int bufferHandle;
    final boolean isStatic;
    final int usage;
    boolean isDirty = false;
    boolean isBound = false;

    public VertexBufferObject (boolean isStatic, int numVertices, VertexAttribute... attributes) {
        this(isStatic, numVertices, new VertexAttributes(attributes));
    }

    public VertexBufferObject (boolean isStatic, int numVertices, VertexAttributes attributes) {
        this.isStatic = isStatic;
        this.attributes = attributes;

        buffer = BufferUtils.newFloatBuffer(this.attributes.vertexSize / 4 * numVertices);
        buffer.flip();
        bufferHandle = Gdx.gl20.glGenBuffer();
        usage = isStatic ? GL20.GL_STATIC_DRAW : GL20.GL_DYNAMIC_DRAW;
    }

    @Override
    public VertexAttributes getAttributes () {
        return attributes;
    }

    @Override
    public int getNumVertices () {
        return buffer.limit() / (attributes.vertexSize / 4);
    }

    @Override
    public int getNumMaxVertices () {
        return buffer.capacity() / (attributes.vertexSize / 4);
    }

    @Override
    public FloatBuffer getBuffer () {
        isDirty = true;
        return buffer;
    }

    private void bufferChanged() {
        if (isBound) {
            Gdx.gl20.glBufferData(GL20.GL_ARRAY_BUFFER, buffer.limit(), buffer, usage);
            isDirty = false;
        }
    }

    @Override
    public void setVertices (float[] vertices, int offset, int count) {
        isDirty = true;
        BufferUtils.copy(vertices, buffer, count, offset);
        buffer.position(0);
        buffer.limit(count);
        bufferChanged();
    }

    @Override
    public void updateVertices (int targetOffset, float[] vertices, int sourceOffset, int count) {
        isDirty = true;
        final int pos = buffer.position();
        buffer.position(targetOffset);
        BufferUtils.copy(vertices, sourceOffset, count, buffer);
        buffer.position(pos);
        bufferChanged();
    }

    @Override
    public void bind (ShaderProgram shader) {
        bind(shader, null);
    }

    @Override
    public void bind (ShaderProgram shader, int[] locations) {
        // DISABLED: performance System.out.println("VertexBufferObject");
        final GL20 gl = Gdx.gl20;

        gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, bufferHandle);
        // DISABLED: performance System.out.println("after glBindBuffer in bind method");
        if (isDirty) {
            // DISABLED: performance System.out.println("isDirty");
            gl.glBufferData(GL20.GL_ARRAY_BUFFER, buffer.limit(), buffer, usage);
            // DISABLED: performance System.out.println("after glBufferData");
            isDirty = false;
        }
        // DISABLED: performance System.out.println("not isDirty");
        final int numAttributes = attributes.size();
        if (locations == null) {
            for (int i = 0; i < numAttributes; i++) {
                final VertexAttribute attribute = attributes.get(i);
                final int location = shader.getAttributeLocation(attribute.alias);
                if (location < 0)
                    continue;
                shader.enableVertexAttribute(location);

                if (attribute.usage == Usage.ColorPacked)
                    shader.setVertexAttribute(location, attribute.numComponents, GL20.GL_UNSIGNED_BYTE, true, attributes.vertexSize,
                            attribute.offset);
                else
                    shader.setVertexAttribute(location, attribute.numComponents, GL20.GL_FLOAT, false, attributes.vertexSize,
                            attribute.offset);
            }
        } else {
            for (int i = 0; i < numAttributes; i++) {
                final VertexAttribute attribute = attributes.get(i);
                final int location = locations[i];
                if (location < 0)
                    continue;
                shader.enableVertexAttribute(location);

                if (attribute.usage == Usage.ColorPacked)
                    shader.setVertexAttribute(location, attribute.numComponents, GL20.GL_UNSIGNED_BYTE, true, attributes.vertexSize,
                            attribute.offset);
                else
                    shader.setVertexAttribute(location, attribute.numComponents, GL20.GL_FLOAT, false, attributes.vertexSize,
                            attribute.offset);
            }
        }
        isBound = true;
    }

    /** Unbinds this VertexBufferObject.
     *
     * @param shader the shader */
    @Override
    public void unbind (final ShaderProgram shader) {
        unbind(shader, null);
    }

    @Override
    public void unbind (final ShaderProgram shader, final int[] locations) {
        // DISABLED: performance System.out.println("unbind shader and locations");
        final GL20 gl = Gdx.gl20;
        final int numAttributes = attributes.size();
        if (locations == null) {
            for (int i = 0; i < numAttributes; i++) {
                shader.disableVertexAttribute(attributes.get(i).alias);
            }
            // DISABLED: performance System.out.println("after disableVertexAttribute(attributes.get(i).alias)");
        } else {
            for (int i = 0; i < numAttributes; i++) {
                final int location = locations[i];
                if (location >= 0)
                    shader.disableVertexAttribute(location);
            }
        }
        // DISABLED: performance System.out.println("after shader.disableVertexAttribute(location)");
        gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
        // DISABLED: performance System.out.println("after glBindBuffer");
        isBound = false;
    }

    /** Invalidates the VertexBufferObject so a new OpenGL buffer handle is created. Use this in case of a context loss. */
    public void invalidate () {
        bufferHandle = Gdx.gl20.glGenBuffer();
        isDirty = true;
    }

    /** Disposes of all resources this VertexBufferObject uses. */
    @Override
    public void dispose () {
        GL20 gl = Gdx.gl20;
        gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
        gl.glDeleteBuffer(bufferHandle);
        bufferHandle = 0;
    }
}