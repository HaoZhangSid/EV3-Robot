package ev3.exercises;

import lejos.hardware.Sound;
import lejos.utility.Delay;

public class MusicPlayer implements Runnable {
    private int[] notes;
    private int[] durations;

    public MusicPlayer(int[] notes, int[] durations) {
        this.notes = notes;
        this.durations = durations;
    }

    @Override
    public void run() {
        for (int i = 0; i < notes.length; i++) {
            if (i == notes.length - 1) { // If it's the last note
                durations[i] = 500; // Extend the duration of the last note slightly
            }
            Sound.playTone(notes[i], durations[i]);
            Delay.msDelay(durations[i] + 20); // Wait for the note to finish
        }
    }
}
