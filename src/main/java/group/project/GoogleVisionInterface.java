package group.project;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.HashMap;


public class GoogleVisionInterface {
	private Vision vision;

	public GoogleVisionInterface() throws IOException, GeneralSecurityException {
		vision = getVisionService();
	}

	private Vision getVisionService() throws IOException, GeneralSecurityException {
		GoogleCredential credential = GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all());
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		return new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
			.setApplicationName("Facial Identification")
			.build();
	}

	private List<FaceAnnotation> detectFaces(Path path) throws IOException {
		byte[] data = Files.readAllBytes(path);

		AnnotateImageRequest request =
			new AnnotateImageRequest()
				.setImage(new Image().encodeContent(data))
				.setFeatures(ImmutableList.of(
					new Feature()
						.setType("FACE_DETECTION")
						.setMaxResults(4)));
		Vision.Images.Annotate annotate = vision.images()
			.annotate(new BatchAnnotateImagesRequest().setRequests(ImmutableList.of(request)));
		// Due to a bug: requests to Vision API containing large images fail when GZipped.
		annotate.setDisableGZipContent(true);

		try {
			BatchAnnotateImagesResponse batchResponse = annotate.execute();
			assert batchResponse.getResponses().size() == 1;
			AnnotateImageResponse response = batchResponse.getResponses().get(0);
			if(response.getFaceAnnotations() == null) {
				throw new IOException(
					response.getError() != null
						? response.getError().getMessage()
						: "Unknown error getting image annotations");
			}
			return response.getFaceAnnotations();
		} catch(Exception e) {
			System.err.println("Internet connection is needed to detect emotions");
			return null;
		}
	}

	private String beautify(String likelihood) {
		switch(likelihood) {
			case "VERY_UNLIKELY":
				return "Very Unlikely";
			case "UNLIKELY":
				return "Unlikely";
			case "POSSIBLE":
				return "Possible";
			case "LIKELY":
				return "Likely";
			case "VERY_LIKELY":
				return "Very Likely";
			default:
				return "Unknown";
		}
	}

	// path is the path to the picture to be analysed
	public HashMap<String, String> getFaceEmotion(Path path) {
		HashMap<String, String> emotions = new HashMap<>();
		try {
			List<FaceAnnotation> annotated = detectFaces(path);
			if(annotated != null) {
				// Only get the first image annotation
				emotions.put("JOY", beautify(annotated.get(0).getJoyLikelihood()));
				emotions.put("SORROW", beautify(annotated.get(0).getSorrowLikelihood()));
				emotions.put("ANGER", beautify(annotated.get(0).getAngerLikelihood()));
				emotions.put("SURPRISE", beautify(annotated.get(0).getSurpriseLikelihood()));
			} else {
				// if cannot get face annotations (may be due to no internet connection)
				emotions.put("JOY", "Unknown");
				emotions.put("SORROW", "Unknown");
				emotions.put("ANGER", "Unknown");
				emotions.put("SURPRISE", "Unknown");
			}
		} catch(IOException e) {
			System.err.println("I/O Exception:");
			e.printStackTrace();
			System.exit(1);
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return emotions;
	}
}
