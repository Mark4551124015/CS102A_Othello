package component;

public interface Component {

    void update(double dt);

    void destroy();

    boolean isDestroy();

}