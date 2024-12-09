package openGl;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

import javax.swing.JFrame;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.FPSAnimator;
import openGl.Cube;
import openGl.GraphicalObject;

public class MainGl extends GLCanvas implements GLEventListener, KeyListener
{

    public ArrayList<GraphicalObject> objects;
    public Cube cube1, cube2, cube3;
    public float rot1, rot2, rot3;
    private Texture texture;

    /**
     *
     */
    public MainGl()
    {
        this.addGLEventListener(this);
        this.objects = new ArrayList<GraphicalObject>();
    }

    /**
     *
     */
    public void init(GLAutoDrawable drawable)
    {
        this.cube1 = new Cube(0, 0, 0, 0, 0, 0, 1, 0, 0, 0.4f);
        this.cube2 = new Cube(0, 1.0f, 0, 0, 0, 0, 1, 0, 0, 0.25f);
        this.cube3 = new Cube(0, 1.5f, 0, 0, 0, 0, 1, 0, 0, 0.125f);
        this.objects.add(cube1);
        this.objects.add(cube2);
        this.objects.add(cube3);

//        GL2 gl = drawable.getGL().getGL2();
//        gl.glEnable(GL2.GL_TEXTURE_2D);
//        try {
//            texture = TextureIO.newTexture(new File("img.jpg"), true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    /**
     *
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        final GLU glu = new GLU();
        float aspect = (float)width / height;
        // Set the view port (display area)
        gl.glViewport(0, 0, width, height);
        // Setup perspective projection,
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspect, 0.1, 100.0);
        // Enable the model-view transform
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        // Enable depth test
        gl.glEnable(GL2.GL_DEPTH_TEST);
    }

    /**
     *
     */
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -5);

        gl.glRotatef(this.rot1, 1.0f, 1.0f, 0.0f);
        this.cube1.display(gl);
        gl.glRotatef(this.rot2, 1.0f, 0.0f, 1.0f);
        this.cube3.display(gl);
        gl.glRotatef(this.rot3, 0.0f, 1.0f, 1.0f);
        this.cube2.display(gl);

        this.rot1 += 0.01f;
        this.rot2 += 0.0175f;
        this.rot3 += 0.025f;

//        texture.bind(gl);
//        gl.glTranslatef(0.0f, 0.0f, -2.0f);
//        gl.glRotatef(0.3f, 1.0f, 0.0f, 0.0f);
//        gl.glBegin(GL2.GL_QUADS);
//        gl.glTexCoord2f(0.25f, 0.25f); gl.glVertex3f(-0.5f, -0.5f, 0.0f);
//        gl.glTexCoord2f(0.7f, 0.4f); gl.glVertex3f(0.5f, -0.5f, 0.0f);
//        gl.glTexCoord2f(0.6f, 0.9f); gl.glVertex3f(0.5f, 0.5f, 0.0f);
//        gl.glTexCoord2f(0.4f, 1.0f); gl.glVertex3f(-0.5f, 0.5f, 0.0f);
//        gl.glEnd();
        //for (GraphicalObject o: this.objects)
        //	o.display(gl);
    }


    /**
     *
     */
    public void dispose(GLAutoDrawable drawable) { }

    public static void main(String[] args)
    {
        GLCanvas canvas = new MainGl();
        canvas.setPreferredSize(new Dimension(800, 600));
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(canvas);
        frame.setTitle("Java - OpenGL");
        frame.pack();
        frame.setVisible(true);
        final Animator animator = new Animator(canvas);
        animator.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                cube1.setY(0.1f); // Déplace le cube vers le haut
                break;
            case KeyEvent.VK_DOWN:
                cube1.setY(-0.1f); // Déplace le cube vers le bas
                break;
            case KeyEvent.VK_LEFT:
                cube1.setX(-0.1f); // Déplace le cube vers la gauche
                break;
            case KeyEvent.VK_RIGHT:
                cube1.setX(0.1f); // Déplace le cube vers la droite
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
