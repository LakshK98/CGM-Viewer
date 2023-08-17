# CGM-Viewer
Java Fx desktop application that allows users to view and edit CGM files developed for DelCyCom Hackathon.

## Functions
- On clicking select a file browser opens up in which the CGM that is to be viewed can be selected.
- Files selected must have .cgm extension. Both 'cgm' and 'CGM' are considered acceptable inputs.
- The CGM file is then read and each of its bytes are converted into a string. This string is printed on to the screen and can be edited by the user.
- After the edit is complete save button must be clicked to make sure the changes are saved to the file.
- On clicking display the default browser will open up a page with the selected
CGM displayed.



## Technical Details
- The software has been developed using javaFx platform.
- A BufferedInputStream with a buffer of 16 Kb is used to read the data in bytes.
- The bytes are converted to Strings using ASCI II encoding, if the string contains characters which are unrecognizable then base64 encoding is used.
- The string created by adding the smaller strings, created by converting few bytes at a time, is created using the StringBuilder class to improve
efficiency.
- To display the CGM it is first converted into a PNG.
- JCGM Computer Graphics Metafile interpreter and renderer is an open source project that has been used to render CGM files. It uses ImageIo plugin for reading them. Binary encoded CGM files are converted to PNG
using the jcgm library.
- The jcgm library has been released under Cougaar open source license. The appropriate licence text that must be added for the use of the software has been included.
- An HTML file with the title and PNG in it is then generated.
- This HTML is then displayed by the default browser of the computer.

## Screenshots

<img width="509" alt="Screenshot 2023-08-17 at 5 46 24 PM" src="https://github.com/LakshK98/CGM-Viewer/assets/24439791/760fad99-3be7-4951-92d6-5c889ee47454">
<img width="615" alt="Screenshot 2023-08-17 at 5 46 35 PM" src="https://github.com/LakshK98/CGM-Viewer/assets/24439791/77d274c0-d5ca-4ac5-b8b2-de0864c3936c">

