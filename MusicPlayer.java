package ev3.exercises;

import lejos.hardware.Sound;
import lejos.utility.Delay;

/**
 * A simple music player class for playing a sequence of musical notes on the EV3 robot.
 * It uses the EV3's built-in speaker to play a series of notes, each with its specified duration.
 * The class allows for the creation of simple musical sequences that can be played back as part
 * of a robot's operation or as feedback to the user.
 */
public class MusicPlayer implements Runnable {
    /** Array of musical note frequencies (in Hz) to be played. */
    private int[] notes;
    /** Array of durations (in milliseconds) for each note to be played. */
    private int[] durations;

    /**
     * Constructs a MusicPlayer with specified arrays of musical notes and their durations.
     * Each index in the notes array corresponds to an index in the durations array, defining
     * the frequency of the note and how long it should be played.
     *
     * @param notes An array of integers representing the frequencies of the notes to be played.
     * @param durations An array of integers representing the duration (in milliseconds) of each note.
     */
    public MusicPlayer(int[] notes, int[] durations) {
        this.notes = notes;
        this.durations = durations;
    }

    /**
     * Plays the sequence of musical notes defined by the {@code notes} and {@code durations} arrays.
     * The method iterates through each note, plays it for its specified duration, and then waits
     * for a small delay before moving on to the next note. The duration of the last note in the sequence
     * is automatically extended to make the ending more distinct.
     */
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
