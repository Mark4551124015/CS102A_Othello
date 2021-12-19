package util;
import com.interactivemesh.jfx.importer.ModelImporter;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import com.interactivemesh.jfx.importer.x3d.X3dModelImporter;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.io.IOException;
public class Importer3D {

        public static Group load(final String fileUrl) throws IOException {

            final int dot = fileUrl.lastIndexOf('.');
            if (dot <= 0) {
                throw new IOException("Unknown 3D file format, url missing extension [" + fileUrl + "]");
            }
            final String extension = fileUrl.substring(dot + 1, fileUrl.length()).toLowerCase();

            switch (extension) {
                case "3ds":
                    ModelImporter tdsImporter = new TdsModelImporter();
                    tdsImporter.read(fileUrl);
                    final Node[] tdsMesh = (Node[]) tdsImporter.getImport();
                    tdsImporter.getNamedMaterials();
                    return new Group(tdsMesh);
                case "stl":
                    StlMeshImporter stlImporter = new StlMeshImporter();
                    stlImporter.read(fileUrl);
                    TriangleMesh cylinderHeadMesh = stlImporter.getImport();
                    stlImporter.close();
                    MeshView cylinderHeadMeshView = new MeshView();
                    cylinderHeadMeshView.setMaterial(new PhongMaterial(Color.GRAY));
                    cylinderHeadMeshView.setMesh(cylinderHeadMesh);
                    stlImporter.close();
                    return new Group(cylinderHeadMeshView);
                case "x3d":

                    X3dModelImporter x3dModel = new X3dModelImporter();
                    x3dModel.read(fileUrl);

                    final Node[] x3dMesh = x3dModel.getImport();
                    x3dModel.close();
                    return new Group(x3dMesh);

                default:
                    throw new IOException("Unsupported 3D file format [" + extension + "]");
            }

    }

}
