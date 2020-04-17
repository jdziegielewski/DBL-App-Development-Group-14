THIS IS A README OF SLEAP APP MADE BY GROUP 14 FOR DBL APP DEVELOPMENT COURSE AT THE EINDHOVEN UNIVERSITY OF TECHNOLOGY

First things first: HOW TO INSTALL OUR APP?
It's very easy!

On non-Samsung devices:
	- Go to your phone�s Settings.
	- Go to Security & privacy > More settings.
	- Tap on Install apps from external sources.
	- Select the browser (e.g., Chrome or Firefox) you want to download the 	  APK files from.
	- Toggle Allow app installs ON.
On Samsung devices:
	- Go to your phone�s Settings.
	- Go to Biometrics and security > Install unknown apps.
	- Select the browser (e.g., Chrome or Firefox) you want to download the 	  APK files from.
	- Toggle Allow app installs ON.

Then:
- Download the APK file provided by Group 14. You can see a pop-up asking if you want to let your browser save files to your phone's storage. Accept this. The warning "This type of file can harm your device." could be seen.
- Click OK to continue.
- If your phone's web browser doesn't give you the option to open the file after downloading, then open the file explorer app you installed, and go to  the Downloads folder on your device.
- Tap the APK file. Allow the app any required permissions it asks for. Then, at the bottom of the installer window, click INSTALL.
- You'll see a confirmation the app is installed. Now you'll see the app available in your list of installed apps.

You are now ready to use our App Sleap. Enjoy! We hope you will not have a problem with waking up ever again.
Greetings!
Team 14
Tessa van Beers, Calin Botoroaga, Jens Coenders, Julian Dziegielewski, Mihai-Dragos Ungureanu, Arjen van Vastenhoven

App Functionalities:
-The user is able to set an alarm and on the Alarm page (clock icon) via the plus symbol. For the Alarm he can choose the time to wake up, the days when the alarm will ring, the Alarm Name and Volume and the Challenge he wants to solve while the alarm goes off.
-On the Challenges page (labyrinth icon) the user can see the Built-in Challenges and Create his own challenge that will appear on 'My Challenges' tab. At the moment we are only supporting one editable challenge that is the 'Shake Phone Challenge'
-On the Score page (podium icon), the user is able to see a complete Leaderboard of him and his friends based on the points gained while competing the challenges (or lost while snoozing an alarm). Additionally he can see on this page, on the top, a pie chart where the percentage of completed challenges vs the snoozed challenges is showed.
-The last page is the User page (person icon). If the user is not yet log in, this page will show the login page where the user can enter the online world of our app by filling in his email address and password (if he has already an account). If he forgot his password, the user can reset it via the 'Forgot your password'. The user needs then to check his email in order to reset the password.
  @ If the user does not have an account he can create one by pressing the button 'Create an Account' and then fill in his personal details. The username should be between 4 and 12 characters and unique, the password should be of length 6 or longer.
  @ Once the user successfully log in into our app, the User page will change and become the profile page of the user. The user can check his details, send again an email to confirm his email address (if not confirmed yet), but also see his friends via the tab 'MY FRIENDS' and the date when they become friends. If the user taps on one of his friends he can see their profiles.
     Additionally, while still being on the 'MY Friends' tab the user can click a small blue button ( + profile icon) and he will be redirected to a new page where he can search for new friends. By default all the users of the app are showed but the user can type into the search bar the name of his new friend, tap on the magnifier icon and then only his new friend will be showed (search bar is case sensitive).
      Once the user had found his new friend he can type on it and the a profile page of this user will be displayed. He can then send him a friend request or cancel the already sent friend request. Once they become friends the user has also the possibility to unfriend this person.

Remarks:
In the implementation of application SLEAP we have used third party for some of a features.
For detecting a shake of the phone in the shake challenge we used seismic libary: com.squareup:seismic:1.0.2
For reading the barcodes and QR codes in the barcode challenge we used zxing libary:  me.dm7.barcodescanner:zxing:1.9
For presenting the percentage of challenges completed by the user in a pie-chart we used a github library": com.github.PhilJay:MPAndroidChart:v3.0.3
For having a circular profile image we used a github library: de.hdodenhof:circleimageview:3.1.0
For allowing the user to crop their image before submitting it to our cloud we used the github library: com.theartofdev.edmodo:android-image-cropper:2.8.+
For showing the profile image to the user we have used the github library: com.github.bumptech.glide:glide:4.11.0
We have also made use of Firebase SDK for Google Analytics and an android library multiDex.
Additionally, we have used the Firebase Authenticator, Firebase Database, Firebase Analytics and Firebase Storage for handling the user details.

