package group.project;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.IOException;
import java.io.File;

import org.bytedeco.javacv.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_objdetect.*;
import org.bytedeco.opencv.opencv_face.*;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.bytedeco.opencv.opencv_dnn.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_highgui.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_face.*;
import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_objdetect.*;
import static org.bytedeco.opencv.global.opencv_dnn.*;


public class FaceRecognizer {
	public static final double THRESHOLD = 0.80;
	private CascadeClassifier faceCascade;
	private Net openFaceModel;
	private Mat currImage;
	private List<Mat> embeddings;
	private List<Mat> croppedFaces;

	public FaceRecognizer(String haarPath, String openFacePath) throws
			IOException {

		// Load Haar Cascade Pretrained Model
		System.out.println("Loading Haar Cascade Classifier for Face Detection from OpenCV pretrained model");
		File f1 = new File(App.class.getClassLoader().getResource(haarPath).getFile());
		faceCascade = new CascadeClassifier(f1.getAbsolutePath());
		System.out.println("Finished loading face detector");

		// Load OpenFace Pretrained Model
		System.out.println("Loading OpenFace Pretrained Model for Face Embedding");
		File f2 = new File(App.class.getClassLoader().getResource(openFacePath).getFile());
		openFaceModel = readNet(f2.getAbsolutePath());
		System.out.println("Loaded Model");

		// Init
		currImage = new Mat();

		embeddings = new ArrayList<>();
		croppedFaces = new ArrayList<>();
	}

	private double getCosineSim(Mat embedding1, Mat embedding2) {
		// Calculate the similarity between the two embeddings
		double ab = embedding1.dot(embedding2);
		double aa = embedding1.dot(embedding1);
		double bb = embedding2.dot(embedding2);
		double cossim = ab / (Math.sqrt(aa) * Math.sqrt(bb));
		return cossim;
	}

	// 128 dimension embedding produced by OpenFace
	private Mat getFaceEmbedding(Mat faceOnlyMat) {
		// Change the image matrix to a blob with shape [1, 3, 96, 96] to align with OpenFace input shape
		// Also swap Red and Blue of this matrix (due to internal image type difference [BGR to RGB])
		Mat blob = blobFromImage(faceOnlyMat, 1.0/255, new Size(96, 96), new Scalar(), true, false, CV_32F);
		openFaceModel.setInput(blob);
		// Has to be copied to other matrix because opencv uses the same pointer for the hereafter output
		Mat output = new Mat();
		openFaceModel.forward().copyTo(output);
		return output;
	}

	private List<Mat> getFaceOnlyMatList(Mat picture, Rect[] facesArray) {
		if(facesArray.length > 0) {
			List<Mat> faceOnlyMats = new ArrayList<>();
			for(Rect rect : facesArray) {
				faceOnlyMats.add(picture.rowRange((int)rect.tl().y(), (int)rect.br().y())
					.colRange((int)rect.tl().x(), (int)rect.br().x()));
			}
			return faceOnlyMats;
			// Face Alignment (not yet implemented) may improve the result
		} else {
			// Return empty list
			return new ArrayList<Mat>();
		}
	}

	public void preprocessAndDetectFaces(Mat picture, RectVector faces) {
		Mat grayFrame = new Mat();

		// convert the frame in gray scale
		cvtColor(picture, grayFrame, COLOR_BGR2GRAY);
		// equalize the frame histogram to improve the result
		equalizeHist(grayFrame, grayFrame);

		// compute minimum face size (20% of the frame height, in our case)
		int absoluteFaceSize = 0;
		int height = grayFrame.rows();
		if (Math.round(height * 0.2f) > 0) {
			absoluteFaceSize = Math.round(height * 0.2f);
		}

		// detect faces and store them in faces RectVector
		faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | CASCADE_SCALE_IMAGE,
				new Size(absoluteFaceSize, absoluteFaceSize), new Size());
	}

	public void setImage(Mat picture) {
		this.currImage = picture;
	}

	public void calculateEmbedding(Rect[] facesArray) {
		// Get the face only image matrix of image 1
		List<Mat> croppedFaces = getFaceOnlyMatList(this.currImage, facesArray);

		// Get the face embedding of image 1
		List<Mat> outputs = new ArrayList<>();
		for(Mat cropped : croppedFaces) {
			outputs.add(getFaceEmbedding(cropped));
		}

		this.embeddings = outputs;
		this.croppedFaces = croppedFaces;
	}

	// Returns a double array with size 2 (cossim & index of croppedFace)
	public double[] compare(Mat storedEmbedding) {
		Mat output2 = storedEmbedding;
		double bestCossim = 0;
		double bestIdx = 0;
		for(int i=0; i < embeddings.size(); i++) {
			Mat output1 = embeddings.get(i);

			// Cosine Similarity output space is [-1, 1]
			double cossim = getCosineSim(output1, output2);
			
			if(cossim > bestCossim) {
				bestCossim = cossim;
				bestIdx = i;
			}
		}
		System.out.println("COSSIM: " + bestCossim);
		double[] retVal = {bestCossim, bestIdx};
		if(bestCossim > THRESHOLD) {
			return retVal;
		} else {
			return null;
		}
	}

	public Mat getCroppedFace(double index) {
		return croppedFaces.get((int)index);
	}

	public Mat getCurrentEmbedding() {
		return embeddings.get(0);
	}

	public Mat getCurrentEmbedding(int i) {
		return embeddings.get(i < embeddings.size() ? i : embeddings.size()-1);
	}

	public void reset() {
		embeddings = new ArrayList<>();
		croppedFaces = new ArrayList<>();
		currImage = new Mat();
	}
}
