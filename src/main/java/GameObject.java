import javafx.geometry.Point2D;
import javafx.scene.Node;

class GameObject {

    private Node view;
    private Point2D velocity = new Point2D(0, 0);

    private boolean alive = true;

    GameObject(Node view){
        this.view = view;
    }

    void update(){
        view.setTranslateX(view.getTranslateX() + velocity.getX());
        view.setTranslateY(view.getTranslateY() + velocity.getY());
    }

    void setVelocity(Point2D velocity){
        this.velocity = velocity;
    }

    Point2D getVelocity() {
        return velocity;
    }

    Node getView() {
        return view;
    }

    boolean isDead(){
        return !alive;
    }

    void setAlive(boolean alive) {
        this.alive = alive;
    }

    private double getRotate(){
        return view.getRotate();
    }

    void rotateRight(){
        view.setRotate(view.getRotate() + 5);
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())),(Math.sin(Math.toRadians(getRotate())))));
    }

    void rotateLeft(){
        view.setRotate(view.getRotate() - 5);
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())),(Math.sin(Math.toRadians(getRotate())))));
    }

    boolean isColliding(GameObject other){
        return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
    }
}
