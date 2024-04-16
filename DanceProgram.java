package ev3.exercises;

import lejos.robotics.RegulatedMotor;

/**
 * Represents a dance program for a robot, making it perform a simple dance routine.
 * This program makes the robot execute a sequence of movements where it alternates
 * between moving forward with one motor and backward with the other, creating a dance effect.
 * Change a new music for the robot.
 */
public class DanceProgram implements Runnable {
    /** The motor attached to the left side of the robot. */
    private RegulatedMotor leftMotor;
    
    /** The motor attached to the right side of the robot. */
    private RegulatedMotor rightMotor;

    /**
     * Constructs a new DanceProgram with specified motors for the left and right sides.
     * 
     * @param leftMotor The regulated motor to control the left side of the robot.
     * @param rightMotor The regulated motor to control the right side of the robot.
     */
    public DanceProgram(RegulatedMotor leftMotor, RegulatedMotor rightMotor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
    }

    /**
     * Executes the dance routine. The routine consists of alternating movements
     * where the robot moves forward with one motor and backward with the other,
     * pausing for a second between each change of direction. This pattern repeats four times.
     */
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

    /**
     * Helper method to pause the execution for a specified amount of time.
     * 
     * @param millis The time to pause in milliseconds.
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

