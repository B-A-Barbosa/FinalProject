import java.util.ArrayList;
import java.util.List;

public class Tremolo extends AudioEffect {
    private final double rate;
    private final double depth = 0.9;

    public Tremolo(double rate) {
        this.rate = rate;
    }

    @Override
    //A tremolo is like vibrato or a whammy bar on a guitar, it adds that "waving effect" to the sound. (its easier to understand when you hear it)
    //this is done by taking a sine wave, which goes between -1 and 1 to change the amplitude (volume) of the sound wave.
    public AudioClip applyEffect(AudioClip clip) {
        List<Short> in = clip.getSamples();

        ArrayList<Short> out = new ArrayList<>(in.size());

        //lfo stands for low frequency oscillator, this is the sine wave mentioned earlier
        //LFO phase increments once per "frame" to keep the left and right channels in sync
        //The bytes for the left and right channels (which earbud the sound comes out) at given moment in the audio, are back to back, 
        //(left byte, right byte, left byte, right byte, etc),
        // so if we just went through each sample and changed the amplitude, one side would be slightly louder than the other
        //the phase is the current position in the wave
        double phase = 0.0;
        //this is the amount the phase changes each sample, its calculated using the rate (how many times per second the wave goes up and down) 
        //and the sample rate (how many samples per second the audio has)
        //dividing them makes sure that the wave goes up and down the correct number of times per second, no matter the sample rate of the audio
        double dPhase = 2.0 * Math.PI * rate / clip.getSampleRate();

        //index through each frame (all channels)
        for (int i = 0; i < in.size(); i += clip.getNumChannels()) {
            //this calculates the lfo value for the current phase, phase is the current position in the wave
            double lfo = (1.0 - depth) + depth * 0.5 * (1.0 + Math.sin(phase));

            //go through each channel in the frame and apply the lfo to it, this ensures that all channels are affected equally
            for (int j = 0; j < clip.getNumChannels(); j++) {
                int v = (int) Math.round(in.get(i + j) * lfo);
                out.add((short) ByteManager.ClampShort(v));
            }
            //change the phase for the next frame
            phase += dPhase;
            //clamp phase to 0..2PI to avoid numerical issues over long audio clips
            if (phase >= 2.0 * Math.PI) phase -= 2.0 * Math.PI;
        }
        return new AudioClip(clip.getFileName() + "_tremolo", out, clip.getHeader().clone());
    }
}