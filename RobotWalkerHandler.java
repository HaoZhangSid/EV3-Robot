// RobotWalkerHandler.java
package ev3.exercises;

import ev3.exercises.library.Lcd;

public class RobotWalkerHandler implements Runnable {
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private SharedControl sharedControl;
    private RobotWalker robotWalker; // Store RobotWalker as a class member variable

    public RobotWalkerHandler(SharedControl sharedControl, RobotWalker robotWalker) {
        this.sharedControl = sharedControl;
        this.robotWalker = robotWalker; // Store the RobotWalker object
    }
 
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


    // Method to stop running
    public void stopRunning() {
        running = false;
        paused = false; // Reset pause state when stopping
        robotWalker.stop(); // Stop motor operation
    }

    // Pause method
    public void pause() {
        paused = true;
        // Also stop robot action during pause
        robotWalker.stop();
    }

    // Resume method
    public void resume() {
        paused = false;
        // Additional logic can be added as needed before resuming forward
    }
}
