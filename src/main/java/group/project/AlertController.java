package group.project;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

import org.bytedeco.opencv.opencv_core.Mat;
import org.opencv.face.Face;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AlertController {

	@FXML
	private Button add;

	@FXML
	private ImageView image;

	@FXML
	private Button exit;

	@FXML
	private AnchorPane alertPane;

	private byte[] face;
	
	private Mat frameMat;

	public void setFace(byte[] feature) {
		face = feature;
		System.out.println("New feature Saved");
		// System.out.println(feature);

	}

	public void setImage(Mat frame) {
		frameMat = frame;
		try {

			File f = new File(getClass().getClassLoader().getResource("image").getFile());
			if (Files.notExists(f.toPath())) {
				Files.createDirectory(f.toPath());
			}
			imwrite(f.getAbsolutePath() + "/test.jpg", frameMat);  // thread issue!!!
			System.out.println("test.jpg generated!");

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		
		
		try {
			File f = new File(getClass().getClassLoader().getResource("image/test.jpg").getFile());
			//Image img = new Image(getClass().getResource("image/test.jpg").toURI().toString()); //getClass().getResource("/image/test.jpg").toURI().toString()
			Image img = new Image(f.toURI().toString());
			image.setImage(img);
		} catch (Exception e) {
			e.printStackTrace();
		} // src/main/resources/image/test.jpg

	}

	@FXML
	void addStudent(ActionEvent event) {

		Platform.runLater(() -> {
			FXMLLoader fxmlLoaderStuFxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("AddStu.fxml"));
			Parent stuParent;
			try {
				stuParent = (Parent) fxmlLoaderStuFxmlLoader.load();
				AddStudentController addStudentController = fxmlLoaderStuFxmlLoader.getController();
				// System.out.println ("add stu controller");
				addStudentController.setFeature(face);
				Scene stuScene = new Scene(stuParent);
				((Stage) add.getScene().getWindow()).setScene(stuScene);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});

	}

	@FXML
	void quit(ActionEvent event) {
		Stage stage = (Stage) alertPane.getScene().getWindow();
		stage.close();
	}

}
