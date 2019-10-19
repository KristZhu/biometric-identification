## Java Face Identification


### Building the project
For command line interface:<br/>
1. First clone this repo and cd inside<br/>
```
$ git clone https://github.com/pgabriela/javagroup-biometric-identification
$ cd javagroup-biometric-identification
```
2. Add your photo to folder ./src/main/resources<br/>
3. Then build with Gradle<br/>
```
$ ./gradlew build
```

For IDE, import this project as a Gradle project


### Running the program
You need to provide the name of the picture that will be compared
with the face captured as the first argument.<br/> Currently there are
2 facial pictures included out of the box (face1.jpg and face2.jpg)
```
$ ./gradlew run --args="face1.jpg"
```
After the webcam is on, the program will just exit if the face is recognized.<br/>
Otherwise, the webcam will keep capturing.<br/>
Press Ctrl+C to exit.


### Implementation
Haar Cascade Pretrained model from OpenCV is used to detect faces.
For Face Embedding, the [OpenFace Pretrained Model] (https://cmusatyalab.github.io/openface/models-and-accuracies/#pre-trained-models) (nn4.small2.v1 in torch format) is used.
All others are implemented with OpenCV and JavaFX libraries.
