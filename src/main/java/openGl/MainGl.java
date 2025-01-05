package openGl;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;


import javax.swing.*;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.FPSAnimator;


public class MainGl extends GLCanvas implements GLEventListener, KeyListener
{

    public ArrayList<GraphicalObject> objects;
    public Cube cube1, cube2, cube3;
    public float rot1, rot2, rot3;
    private Texture texture;
    private ArrayList<Cube> projectiles = new ArrayList<>();
    private ArrayList<Cube> ennemiesProjectiles = new ArrayList<>();
    private ArrayList<GraphicalObject> targets = new ArrayList<>();
    private GLUT glut = new GLUT();
    private Timer targetMovementTimer;
    private Timer targetMovementXTimer;
    private Timer enemyFireTimer;
    private boolean xTarget = false;
    private int score;
    private int life = 2;


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
        this.cube1 = new Cube(0, -1.5f, 0, 0, 0, 1, 0.3f, 0.3f, 0, 0.125f, 1f);
        this.objects.add(cube1);
        generateTarget();
        targetMovementTimer = new Timer(7000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveTargetsDown();
            }
        });
        targetMovementTimer.start();

        targetMovementXTimer = new Timer(2500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveTargetsX();
            }
        });
        targetMovementXTimer.start();

        enemyFireTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enemyFire();
            }
        });
        enemyFireTimer.start();

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
        gl.glPushMatrix();
        this.cube1.display(gl);
        gl.glPopMatrix();

        if (this.life > 0) {
            if (!this.targets.isEmpty()) {
                for (GraphicalObject target : this.targets) {
                    target.display(gl);
                    if (checkCollision((Cube) target, this.cube1)) {
                        this.life = 0;
                    }
                }

                for (int i = 0; i < projectiles.size(); i++) {
                    Cube projectile = projectiles.get(i);

                    // Déplace le projectile
                    projectile.translate(0, 0.1f, 0);  // Déplace chaque projectile vers l'avant

                    // Vérifie si le projectile sort de l'écran
                    if (projectile.getZ() > 10) {
                        projectiles.remove(i);  // Supprime le projectile s'il sort de l'écran
                        i--;  // Ajuste l'index après suppression
                        continue;
                    }

                    // Vérifie si le projectile entre en collision avec des cibles
                    for (GraphicalObject target : this.targets) {
                        if (checkCollision(projectile, target)) {
                            System.out.println("Collision détectée !");
                            projectiles.remove(i);
                            targets.remove(target);
                            this.score += 100;
                            i--;
                            break;
                        }
                    }

                    for (GraphicalObject ennemiesProjectile : ennemiesProjectiles) {
                        if (checkCollision(projectile, ennemiesProjectile)) {
                            projectiles.remove(i);
                            ennemiesProjectiles.remove(ennemiesProjectile);
                            break;
                        }
                    }
                    // Affiche le projectile
                    projectile.display(gl);
                }

                for (int i = 0; i < this.ennemiesProjectiles.size(); i++) {
                    Cube enemyProjectile = this.ennemiesProjectiles.get(i);

                    // Déplace le projectile vers le bas
                    enemyProjectile.move();

                    // Vérifie si le projectile touche le joueur
                    if (checkCollision(enemyProjectile, cube1)) {
                        System.out.println("Le joueur a été touché !");
                        life--;
                        this.ennemiesProjectiles.remove(i);
                        i--;
                        continue;
                    }


                    // Affiche le projectile
                    enemyProjectile.display(gl);
                }
            } else {
                this.generateTarget();
                if (this.life<3) {
                    this.life++;
                }
            }
        } else {
            drawLargeText(gl,"Game Over",0, getWidth() - 370, getHeight() - 300);
        }

        drawLargeText(gl, "Life: ", this.life, getWidth()- 100, getHeight() - 40);
        drawLargeText(gl, "Score: ", this.score, 10, getHeight() - 40);
    }



    /**
     *
     */
    public void dispose(GLAutoDrawable drawable) { }

    public static void main(String[] args)
    {
        GLCanvas canvas = new MainGl();
        canvas.setPreferredSize(new Dimension(600, 600));
        canvas.addKeyListener((MainGl) canvas);
        canvas.setFocusable(true);
        canvas.requestFocus();
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(canvas);
        frame.setTitle("Java - OpenGL");
        frame.pack();
        frame.setVisible(true);
        //final Animator animator = new Animator(canvas);
        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Q:
                System.out.println("left");
                cube1.setX(-0.1f); // Déplace le cube vers la gauche
                System.out.println(cube1.getX());
                if (cube1.getX() == -2.3f) {
                    cube1.setX(4.3f);
                }
                break;
            case KeyEvent.VK_D:
                System.out.println("right");
                cube1.setX(0.1f); // Déplace le cube vers la droite
                System.out.println(cube1.getX());

                if (cube1.getX() == 2.3f) {
                    cube1.setX(-4.3f);
                }
                break;
            case KeyEvent.VK_SPACE:
                Cube projectile = new Cube(this.cube1.getX(), this.cube1.getY() + 0.1f, 0, 0, 0, 1, 0.3f, 0.3f, 0, 0.1f, 1f);
                this.projectiles.add(projectile);
                break;
        }

        this.display(); // Redessine la scène
    }



    @Override
    public void keyReleased(KeyEvent e) {

    }

    public boolean checkCollision(Cube projectile, GraphicalObject target) {
        // Calcul de la distance entre le projectile et la cible
        float dx = projectile.getX() - target.getX();
        float dy = projectile.getY() - target.getY();
        float dz = projectile.getZ() - target.getZ();
        float distance = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);

        // Définir une distance minimale de collision
        float collisionThreshold = 0.2f;

        // Si la distance est inférieure à la distance de collision, il y a collision
        return distance < collisionThreshold;
    }

    public void generateTarget() {
        float x = -1.7f;
        float y = 1f;
        for (int j = 0; j<4 ; j++) {
            for (int i = 0; i < 12; i++) {
                this.targets.add(new Cube(x, y, 0, 0, 0, 1, 0, 1f, 0, 0.08f, 0.05f));
                x += 0.3f;
            }
            y -= 0.3f;
            x = -1.7f;
        }
    }

    private void drawLargeText(GL2 gl, String text, int nbLife, float x, float y) {
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glOrtho(0, getWidth(), 0, getHeight(), -1, 1);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();

        gl.glTranslatef(x, y, 0);
        gl.glScalef(0.2f, 0.2f, 1.0f); // Augmente la taille du texte vectoriel

        // Afficher le texte
        for (char c : text.toCharArray()) {
            glut.glutStrokeCharacter(GLUT.STROKE_ROMAN, c);
        }

        if (nbLife > 0) {
            // Convertir nbLife en chaîne de caractères
            String lifeString = String.valueOf(nbLife);

            // Afficher chaque caractère de la chaîne nbLife
            for (char c : lifeString.toCharArray()) {
                glut.glutStrokeCharacter(GLUT.STROKE_ROMAN, c);
            }
        }

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPopMatrix();

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPopMatrix();
    }

    private void moveTargetsDown() {
        for (GraphicalObject target : targets) {
            target.translate(0, -0.3f, 0);
        }

        this.display();
    }

    private void moveTargetsX() {
        boolean reachedEdge = false;
        for (GraphicalObject target : targets) {
            float x = target.getX();

            // Vérifie si une cible atteint un bord spécifique
            if (x <= -2.0f || x >= 1.8f) {
                reachedEdge = true;
                break;
            }
        }
        // Si une cible atteint un bord, change la direction
        if (reachedEdge) {
            this.xTarget = !this.xTarget;
        }

        for (GraphicalObject target : targets) {
            if (this.xTarget) {
                target.translate(0.3f, 0, 0); // Déplace vers la droite
            } else {
                target.translate(-0.3f, 0, 0); // Déplace vers la gauche
            }
        }
        this.display();
    }

    private void enemyFire() {
        // Trouve la valeur minimale de Y (cible la plus basse)
        float minY = Float.MAX_VALUE;
        for (GraphicalObject target : targets) {
            if (target.getY() < minY) {
                minY = target.getY();
            }
        }

        // Récupère les cibles situées à la hauteur minimale (dernière ligne)
        ArrayList<GraphicalObject> lastRowTargets = new ArrayList<>();
        for (GraphicalObject target : targets) {
            if (Math.abs(target.getY() - minY) < 0.01f) { // Tolérance pour comparer les floats
                lastRowTargets.add(target);
            }
        }

        // Sélectionne une cible aléatoire de la dernière ligne pour tirer
        if (!lastRowTargets.isEmpty()) {
            int randomIndex = (int) (Math.random() * lastRowTargets.size());
            GraphicalObject firingTarget = lastRowTargets.get(randomIndex);

            Cube enemyProjectile = new Cube(firingTarget.getX(), firingTarget.getY() - 0.1f, 0,
                    1, 0, 0, 1f, 1f, 1, 0.1f, 0.02f);
            ennemiesProjectiles.add(enemyProjectile);
        }
    }


}
