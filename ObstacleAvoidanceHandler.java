//ObstacleAvoidanceHandler.java
package ev3.exercises;

import ev3.exercises.library.UltraSonicSensor;
import ev3.exercises.library.Lcd;
import lejos.hardware.port.SensorPort;
import lejos.robotics.RegulatedMotor;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;

/**
 * Handles obstacle detection and avoidance for a robot using an ultrasonic sensor.
 * When an obstacle is detected within a predefined threshold, the robot executes
 * a predefined avoidance maneuver and optionally performs additional actions like stopping the robot,
 * playing a sound, or executing a dance routine.
 */
public class ObstacleAvoidanceHandler implements Runnable {
	/** The ultrasonic sensor used to detect obstacles. */
    private UltraSonicSensor ultrasonicSensor;
    
    /** Shared control object for managing the robot's state and behavior. */
    private SharedControl sharedControl;
    
    /** Flag to control the main loop execution. */
    private volatile boolean running = true;
    
    /** Distance threshold for obstacle detection in meters. */
    private final float DISTANCE_THRESHOLD = 0.1f; // Obstacle detection threshold, unit: meters
    
    /** Timestamp of the first obstacle detection, used for timing related decisions. */
    private long firstObstacleDetectionTime; // Record the time when the first obstacle is detected
    
    /** Flag indicating if an obstacle has been detected. */
    private boolean obstacleDetected = false; // Flag indicating whether an obstacle is detected
    
    /** Flag indicating if the robot should be stopped due to an obstacle. */
    private boolean stopRobot = false; // Flag indicating whether to stop the robot
    
    
    /**
     * Constructs an ObstacleAvoidanceHandler with a shared control for robot management.
     * Initializes the ultrasonic sensor on a specific port.
     *
     * @param sharedControl The shared control object used for coordinating robot actions.
     */
    public ObstacleAvoidanceHandler(SharedControl sharedControl) {
        this.ultrasonicSensor = new UltraSonicSensor(SensorPort.S2); // Assume ultrasonic sensor is connected to port S2
        this.sharedControl = sharedControl;
      
    }

    /**
     * Main method to detect and handle obstacles. Monitors the distance to potential obstacles
     * and initiates avoidance maneuvers if necessary. Can also stop the robot and perform
     * additional actions like playing sounds or executing a dance routine upon encountering obstacles.
     */
    @Override
    public void run() {
        while (running) {
            float distance = ultrasonicSensor.getRange();
            Lcd.clear(6);
            Lcd.print(6, "Distance: %.2f m", distance); // Display distance value in real-time on LCD

            // If an obstacle is detected
            if (distance < DISTANCE_THRESHOLD) {
                if (!obstacleDetected) {
                    // First time detecting an obstacle
                    firstObstacleDetectionTime = System.currentTimeMillis();
                    obstacleDetected = true;
                    avoidObstacle();
                } else {
                    // Second time detecting an obstacle
                    long currentTime = System.currentTimeMillis(); // Get current time
                    long elapsedTime = currentTime - firstObstacleDetectionTime; // Calculate time interval since first obstacle detection
                    stopRobot = true; // Flag to stop the robot
                    displayTimeSinceLastObstacle(elapsedTime); // Display time interval information
                }
            } 

            // If stopRobot flag is true, stop the robot
            if (stopRobot) {
                synchronized (this) {
                    sharedControl.setRobotState("stop");
                    running = false;
                    Sound.systemSound(true, Sound.BEEP); 
                    wait(100);
                    Sound.systemSound(true, Sound.BEEP); 
                    
                    RegulatedMotor leftMotor = Motor.C; // Left motor
                    RegulatedMotor rightMotor = Motor.B; // Right motor
                    // Define musical notes frequency
                    int[] notes = {1568, 1760, 1976, 2093, 1760, 2093, 2200, 2330, 1976, 2093,
                                   1568, 1760, 1976, 2093, 1760, 2093, 2200, 2330, 1976, 2093};

                    // Define duration of musical notes (milliseconds)
                    int[] durations = {200, 200, 200, 200, 200, 200, 400, 400, 200, 200,
                                       200, 200, 200, 200, 200, 200, 400, 400, 200, 200}; // Duration corresponding to each note

                    // Create threads for music playback and dance program execution
                    Thread musicThread = new Thread(new MusicPlayer(notes, durations));
                    Thread danceThread = new Thread(new DanceProgram(leftMotor, rightMotor));

                    // Start threads
                    musicThread.start();
                    danceThread.start();
                    try {
                        // Wait for both threads to finish execution
                        musicThread.join();
                        danceThread.join();
                        // Close motors
                        leftMotor.close();
                        rightMotor.close();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                }
            }

            try {
                Thread.sleep(50); // Check frequency
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wait(5);
        }
    }

    /**
     * Displays the elapsed time since the last obstacle was detected.
     *
     * @param elapsedTime The time elapsed since the last obstacle detection in milliseconds.
     */
    private void displayTimeSinceLastObstacle(long elapsedTime) {
        Lcd.clear(7);
        Lcd.print(7, "Time: %d s", elapsedTime/1000); // Display time interval on LCD
    }
    
    /**
     * Initiates an avoidance maneuver when an obstacle is detected. The specific sequence of actions
     * can include turning, moving forward, and adjusting the robot's path based on sensor inputs.
     */
    private void avoidObstacle() {   
        // Set avoiding obstacle state
        sharedControl.setAvoidingObstacle(true);
        // Robot turns right by 45 degrees, takes 1000ms, speed 180
        sharedControl.setRobotState("turnRight");
        wait(1000);           
                
        // Forward L=21.21cm, takes 2.7s
        sharedControl.setRobotState("forwardLine");
        wait(2000);
        
        // Robot turns left by 90 degrees, takes 2000ms, speed 180
        sharedControl.setRobotState("turnLeft");
        wait(1500);
        
        boolean keepLine = true;
        while (keepLine) {
            sharedControl.setRobotState("forwardLine");
            float redValue = sharedControl.getRedValue();
            Lcd.clear();
            Lcd.print(1, "Red Value: %.2f", redValue);
           
            if (redValue < 0.2) {
                // When red value detected by color sensor is less than 0.2, robot exits obstacle avoidance
                Sound.systemSound(true, Sound.BEEP); 
                keepLine = false;
            }
            wait(10);
        }
        
        sharedControl.setAvoidingObstacle(false);
        sharedControl.setRobotState("forward");
    }

    
    /**
     * Utility method to pause execution for a specified number of milliseconds.
     *
     * @param milliseconds The amount of time to pause in milliseconds.
     */
    private void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Stops the obstacle detection and handling loop.
     */
    public void stopRunning() {
        running = false;
    }
}
