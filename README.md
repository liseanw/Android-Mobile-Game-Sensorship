# Android-Mobile-Game-Sensorship
YouTube Demonstration: https://www.youtube.com/watch?v=kcpX1lvl2gQ

Group Members:
- Brandon Widjaja
- Claire Shou
- Jiayao Wu
- Li Sean Wong
- Yun Ken Leong

## Mandatory Tasks
Code is designed to be run in Android Studio. To run and compile the app, you can either run it with an android phone connected, or on the emulator, however, the emulator will not work with the eyes and face controls since a phone camera is needed.

For Android Phone:
Connect to computer, then navigate to android> java> com.mygdx.game > AndroidLauncher. Then, you can press the play button to compile and run the game.

For Emulator:
The camera will not work with the emulator so you must comment/uncomment the following code:

Comment out:
- AndroidLauncher lines 93 & 94 
	setupLiveDemoUiComponents();
	startCamera();

Uncomment:
- Login line 140
- Profile line 80
- Register line 134

After commenting out and uncommenting these lines, you can press the play button in the android studio IDE to run the code on an emulator. We tested it on Pixel 6 API 34 2.
