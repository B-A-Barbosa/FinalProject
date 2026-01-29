# FinalProject
*CHECK INCLUDED PDF FOR PLANNING / PROCCESS*

Outline:
The program will have a jframe where you can select an audio file (wav, maybe others) and an effect. Once you select the effect you will be able to press a button labeled “apply” where it will change the data and create a new file. Each effect class will also have an undo function, and I will have an arraylist of the effects applied so that you can get your original audio back. The audio effect class will be abstract with the abstract method apply effect. I will make the project using the terminal first, and then port it to jframe.

All effect ideas: 
Easy: Echo/delay/reverb (what is the difference between these), bitcrusher, normalize, reverse
Medium: Low/high pass filter, distortion, speed change, fade in / out, tremolo
Hard: equalizer

Step 1:
Learn the format of a wav file, read it, make clips and then write it back.
Classes: WavFileReader, WavFileWriter, AudioClip

Step 1.5:
Research plugins and mp3. Will I have mp3 support, which plugins are best to add.

Step 2:
First plugin: Change volume and pitch of the file
Classes: AudioEffect

Step 3:
Echo Effect

Step 4:
Bitcrush

Step 5:
Distortion 

Throughout:
Each audio effect will be its own class, and they will be organized using the (arraylist or custom list).
