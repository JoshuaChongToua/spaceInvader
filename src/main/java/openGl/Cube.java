package openGl;

import com.jogamp.opengl.GL2;

import java.util.ArrayList;

public class Cube extends GraphicalObject {

    private ArrayList<Square> faces;
    private float speed;
    public Cube(float posX, float posY, float posZ,
                float angleX, float angleY, float angleZ,
                float r, float g, float b,
                float scale, float speed) {
        super(posX, posY, posZ, angleX, angleY, angleZ, r, g, b, scale);
        this.speed = speed;
        faces = new ArrayList<Square>();
        // Front face
//        faces.add(new Square(0, 0, 1, 0, 0, 0, 0.12f, 0.25f, 0.69f, 1));
        faces.add(new Square(0, 0, 1, 0, 0, 0, r, g, b, 1));
        // Back face
        faces.add(new Square(0, 0, -1, 0, 0, 0, r, g, b, 1));
        // Right face
        faces.add(new Square(1, 0, 0, 0, 90, 0, r, g, b, 1));
        // Left face
        faces.add(new Square(-1, 0, 0, 0, -90, 0, r, g, b, 1));
        // Top face
        faces.add(new Square(0, 1, 0, 90, 0, 0, r, g, b, 1));
        // Left face
        faces.add(new Square(0, -1, 0, 90, 0, 0, r, g, b, 1));
    }

    public void displayNormalized(GL2 gl) {
        // Affichage des faces
        for (Square face : faces) {
            face.display(gl);
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void move() {
        this.translate(0, -speed, 0); // Déplacement selon la vitesse
    }
}
