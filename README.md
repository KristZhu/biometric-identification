## Java Face Identification


### Building the project
For command line interface:<br/>
1. **clone** this repo and **cd** inside<br/>
```bash
$ git clone https://github.com/pgabriela/javagroup-biometric-identification
$ cd javagroup-biometric-identification
```
2. Add your photo to folder *./src/main/resources*<br/>
3. Build with Gradle<br/>
```bash
$ ./gradlew build
```
<br/>

For IDE, import this project as a Gradle project and build as usual after doing **Step 2** above

<br/>

### Running the program
You need to provide the name of the picture that will be compared
with the face captured as the first argument.<br/> Currently there are
2 facial pictures included out of the box (**face1.jpg** and **face2.jpg**).<br/>
For example, if the picture you put in *./src/main/resources/* previously is named **face3.jpg**, <br/>
then you can run the program with the command below:
```bash
$ ./gradlew run --args="face3.jpg"
```
After the webcam is on, the program will just exit if the face is recognized.<br/>
Otherwise, the webcam will keep capturing.<br/>
Press Ctrl+C to exit.

<br/>

### Implementation
Haar Cascade Pretrained model from OpenCV is used to detect faces.<br/>
For Face Embedding, the [OpenFace Pretrained Model](https://cmusatyalab.github.io/openface/models-and-accuracies/#pre-trained-models) (nn4.small2.v1 in torch format) is used.<br/>
All others are implemented with OpenCV and JavaFX libraries.
