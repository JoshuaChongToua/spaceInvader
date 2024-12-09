package openGl;

import java.awt.Dimension;

import javax.swing.JFrame;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

public class GLEventL extends GLCanvas implements GLEventListener {



    /**
     *
     */
    public GLEventL() {
        this.addGLEventListener(this);
    }

    /**
     *
     */
    public void init(GLAutoDrawable drawable) {
    }

    /**
     *
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        final GLU glu = new GLU();
        float aspect = (float) width / height;
// Set the view port (display area) 
        gl.glViewport(0, 0, width, height);
// Setup perspective projection, 
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspect, 0.1, 100.0);
// Enable the model-view transform
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    /**
     *
     */
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glBegin(GL.GL_TRIANGLES);


    }

    /**
     *
     */
    public void dispose(GLAutoDrawable drawable) {
    }
}
