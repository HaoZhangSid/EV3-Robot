package ev3.exercises;

import lejos.robotics.RegulatedMotor;

public class DanceProgram implements Runnable {
    private RegulatedMotor leftMotor;
    private RegulatedMotor rightMotor;

    public DanceProgram(RegulatedMotor leftMotor, RegulatedMotor rightMotor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
    }

    @Override
    public void run() {
        for (int i = 0; i < 4; i++) {
            leftMotor.forward();
            rightMotor.backward();
            sleep(1000); // Wait for 1 second
            leftMotor.backward();
            rightMotor.forward();
            sleep(1000); // Wait for 1 second
        }
    }

    // Helper method to wait for a specified amount of time
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
