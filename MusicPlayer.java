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
            if (i == notes.length - 1) { // 如果是最后一个音符
                durations[i] = 500; // 最后一个音符持续时间稍长一些
            }
            Sound.playTone(notes[i], durations[i]);
            Delay.msDelay(durations[i] + 20); // 等待音符结束
        }
    }
}
