package stage;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import util.Importer3D;

import java.io.IOException;

import static javafx.application.Application.launch;

public class BoardIn3D extends Application {
    private final Rotate cameraXRotate = new Rotate(-20, 0, 0, 0, Rotate.X_AXIS);
    private final Rotate cameraYRotate = new Rotate(-20, 0, 0, 0, Rotate.Y_AXIS);
    private final Translate cameraPosition = new Translate(0, 0, -20);

    private double scaleFactor = 1;

    public void start(Stage Board) throws IOException {
        BorderPane borderPane = new BorderPane();

        Scene scene = new Scene(borderPane, 800, 600);

        Board.setTitle("Board");
        Board.setScene(scene);
        Board.show();
    }

    private Group load3DModel() throws IOException {

        /**
         * 创建摄像头
         */
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(cameraXRotate, cameraYRotate, cameraPosition);

        /**
         * 导入3d模型
         */
        Group groupRoot = Importer3D.load("");
        groupRoot.getChildren().add(camera);

        /**
         * 创建SubScene
         */
        SubScene subScene = new SubScene(groupRoot, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        Group group = new Group();
        group.getChildren().add(subScene);

        return group;
    }
    public static void main(String[] args) {
        launch(args);
    }






}
