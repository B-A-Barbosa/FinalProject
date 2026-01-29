# FinalProject
**Included PDF houses day by day learning and work done**

**Google Drive link for runnable download:** https://drive.google.com/file/d/1oeSvPY-qU70T2iYTpRyJaYJehQdow0Qw/view?usp=sharing

**General Info:**
This project opens a window made with J frame, it allows you to pick a wav file on your computer, and add an effect to it.
It comes with 2 files included:
  A sin wave (Audio.wav) which is the most pure form of audio (a single note), this allows you to hear the effects easily.
  A bell sound, this allows you to hear the effects applied to real world sounds

**If I had to do this again:**
  If I had to do this projects again, I would likely have focused much more on the sound generation portion (creating my own sound waves). I did this in the OldMain file when i was learning how wav files worked (described in the doc).
Using math to generate sounds was much easier than managing and manipulating bytes.
I likely would have created an oscillator project. In this project you would have been able to choose between 1 - ~3 waveforms (sin wav, square wav, sawtooth, triangle, random noise) and select how loud each one would be, the pitch of each one, and then combine them.
Although this would be a primitive version of it, this (combined with the effects I did end up making) is  how all music on a computer is made (except recordings of real instuments or voices)

  Something I would have done differently in this specific project was not create the bitcrusher effect.
This effect was by far the hardest effect to make. At first I had a completely different way of doing it which involved dividing by a number, rounding it, and multipling it back by a number.
This effect is also (in my opinion) the least impressive sounding, in other words, the return on investment was very low.
In replacement, I would have made a fade in / fade out effect. 
However, in the end I am glad i made it, because it made me finally understand the << and >> operations, although i had seen them when messing with sound generation, I hadn't fully understood them until I had to use them myself.

**Main Topics learned:**
Format of a computer file
Format and specifications of a sound file (wav)
little endian vs big endian
java bit operations
