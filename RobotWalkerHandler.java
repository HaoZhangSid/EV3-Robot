// RobotWalkerHandler.java
package ev3.exercises;

import ev3.exercises.library.Lcd;

/**
 * Manages the walking behavior of a robot by controlling its movement according to
 * the current state shared between different parts of the robot's program. This class
 * uses a {@link RobotWalker} instance to execute movement commands like forward, backward,
 * turn left, and turn right based on the robot's current state.
 */
public class RobotWalkerHandler implements Runnable {
	/** Flag to control the execution of the run loop. */
    private volatile boolean running = true;
    
    /** Flag to pause the robot's movement. */
    private volatile boolean paused = false;
    
    /** Shared control object for managing the robot's state and behavior. */
    private SharedControl sharedControl;
    
    /** The {@link RobotWalker} instance used for executing movement commands. */
    private RobotWalker robotWalker; // Store RobotWalker as a class member variable
    
    /**
     * Constructs a new RobotWalkerHandler with a shared control object and a RobotWalker instance.
     *
     * @param sharedControl The shared control object used for coordinating robot actions.
     * @param robotWalker The RobotWalker instance used for executing movement commands.
     */
    public RobotWalkerHandler(SharedControl sharedControl, RobotWalker robotWalker) {
        this.sharedControl = sharedControl;
        this.robotWalker = robotWalker; // Store the RobotWalker object
    }
 
    /**
     * The main run method that controls the robot's movement based on the shared state.
     * It checks the current state and executes corresponding actions, such as moving forward,
     * turning, or stopping. The method adjusts the motor speed dynamically based on the output
     * from a PID controller, if applicable.
     */
    @Override
    public void run() {
        // Calculate time proportion factor Tp for adjusting motor speed
        int Tp = (int)(0.25f*(RobotWalker.motorA.getMaxSpeed()+RobotWalker.motorB.getMaxSpeed())/2);
        while (running) {
            if (!paused && sharedControl != null) { // Add a check for sharedControl object not being null
                // Update and display state
                String state = sharedControl.getRobotState();
                Lcd.clear(4); // Clear display of a specific line
                Lcd.print(4, "State: %s", state); // Display current state
                // Control the robot based on the state of shared resource
                switch (state) {
                    case "stop":
                        RobotWalker.stop(); // Use the stored RobotWalker object
                        RobotWalker.close();
                        stopRunning();
                        break;
                    case "forward":
                        // Get the output value of PID controller
                        double sum = sharedControl.getMotorSpeed();
                        // Set motor speed

                        RobotWalker.setMotorsSpeed((float)(Tp-sum), (float)(Tp+sum)); // Use the stored RobotWalker object

                        // Display PID output on LCD
                        Lcd.clear(1);
                        Lcd.print(1, "PID: %.2f", sum);
                        break;
                    case "turnLeft":
                        RobotWalker.turnLeft(RobotWalker.DEFAULT_SPEED);
                        break;
                    case "turnRight":
                        RobotWalker.turnRight(RobotWalker.DEFAULT_SPEED);
                        break;
                    case "forwardLine":
                        RobotWalker.forwardLine(RobotWalker.DEFAULT_SPEED);
                        break;    
                    default:

                }
            }

            try {
                Thread.sleep(5); // Control checking frequency
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stops the robot's movement and the execution of the run loop. This method also ensures
     * that the motors are stopped and the resources are released.
     */
    public void stopRunning() {
        running = false;
        paused = false; // Reset pause state when stopping
        robotWalker.stop(); // Stop motor operation
    }

    /**
     * Pauses the robot's movement. This can be used to temporarily halt the robot's actions
     * without stopping the execution of the run loop.
     */
    // Pause method
    public void pause() {
        paused = true;
        // Also stop robot action during pause
        robotWalker.stop();
    }

    /**
     * Resumes the robot's movement after being paused. This method restores the robot's
     * operation according to the current state.
     */
    // Resume method
    public void resume() {
        paused = false;
        // Additional logic can be added as needed before resuming forward
    }
}
