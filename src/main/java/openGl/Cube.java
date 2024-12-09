package openGl;

import com.jogamp.opengl.GL2;

import java.util.ArrayList;

public class Cube extends GraphicalObject {

    private ArrayList<Square> faces;

    public Cube(float posX, float posY, float posZ,
                float angleX, float angleY, float angleZ,
                float r, float g, float b,
                float scale) {
        super(posX, posY, posZ, angleX, angleY, angleZ, r, g, b, scale);
        faces = new ArrayList<Square>();
        // Front face
        faces.add(new Square(0, 0, 1, 0, 0, 0, 0.12f, 0.25f, 0.69f, 1));
        // Back face
        faces.add(new Square(0, 0, -1, 0, 0, 0, 0.32f, 0.39f, 0.6f, 1));
        // Right face
        faces.add(new Square(1, 0, 0, 0, 90, 0, 0.05f, 0.16f, 0.53f, 1));
        // Left face
        faces.add(new Square(-1, 0, 0, 0, -90, 0, 0.41f, 0.47f, 0.69f, 1));
        // Top face
        faces.add(new Square(0, 1, 0, 90, 0, 0, 0.14f, 0.2f, 0.43f, 1));
        // Left face
        faces.add(new Square(0, -1, 0, 90, 0, 0, 0.57f, 0.63f, 0.83f, 1));
    }

    public void displayNormalized(GL2 gl) {
        // Affichage des faces
        for (Square face : faces) {
            face.display(gl);
        }
    }
}
