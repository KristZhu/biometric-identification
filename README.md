## Java Face Identification


### Building the project
Make sure you have JDK version 11 installed.

For command line interface:<br/>
1. **clone** this repo and **cd** inside<br/>
```bash
$ git clone https://github.com/pgabriela/javagroup-biometric-identification
$ cd javagroup-biometric-identification
```
2. Build with Gradle<br/>
```bash
$ ./gradlew build
```
<br/>

For IDE, import this project as a Gradle project and build as usual after doing **Step 2** above.<br/>
You may need to run the command below in the root folder for eclipse:<br/>
Windows:
```
$ gradlew.bat eclipse
```

Linux/MacOS:
```bash
$ ./gradlew eclipse
```

<br/>

### Running the program
You need to specify the path to the google credential file as specified below:
```bash
$ GOOGLE_APPLICATION_CREDENTIALS="./Java Group Project-fce92f1db01e.json" ./gradlew run
```
Click the **Start Detecting** button to start the webcam.<br/>
After the webcam is on, the view will be changed to be the dashboard if the face is recognized (After printing the emotion likelihood).<br/>
Otherwise, the webcam will keep capturing.<br/>
Press Ctrl+C to exit when the webcam is on. Otherwise, just close the GUI window to exit the program.

<br/>

### Implementation
Haar Cascade Pretrained model from OpenCV is used to detect faces.<br/>
For Face Embedding, the [OpenFace Pretrained Model](https://cmusatyalab.github.io/openface/models-and-accuracies/#pre-trained-models) (nn4.small2.v1 in torch format) is used.<br/>
All others are implemented with OpenCV and JavaFX libraries.
