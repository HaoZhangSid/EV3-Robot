// Main.java
package ev3.exercises;

import app.httptest;
import lejos.hardware.Button;
import app.httptest;

/**
 * The main class for running the robot application. This class initializes and starts the threads for
 * handling light sensor input, robot walking behavior, and obstacle avoidance. It sets up a shared control
 * resource to coordinate actions between different parts of the application.
 *this is us team work,do us best
 * <p>After initializing the necessary handlers for the robot's sensors and movement, it waits for a button press
 * to terminate the application and stop all running threads, ensuring a graceful shutdown and resource cleanup.</p>
 */
public class Run {
    public static void main(String[] args) {
        // Create shared control resource
    	SharedControl sharedControl = new SharedControl();
    	// Create a thread for httptest
    	httptest httpTest = new httptest(sharedControl);
    	new Thread(httpTest).start();
        


        RobotWalker robotWalker = new RobotWalker(); // Create motor controller instance
        
        LightSensorHandler lightSensorHandler = new LightSensorHandler(sharedControl);
        Thread lightSensorThread = new Thread(lightSensorHandler);
        lightSensorThread.start();

        // Initialize robot walker handler
        RobotWalkerHandler walkerHandler = new RobotWalkerHandler(sharedControl, robotWalker);
        Thread walkerThread = new Thread(walkerHandler);
        walkerThread.start();

        // Initialize ultrasonic sensor and obstacle avoidance handler
        ObstacleAvoidanceHandler obstacleAvoidanceHandler = new ObstacleAvoidanceHandler(sharedControl);
        Thread obstacleAvoidanceThread = new Thread(obstacleAvoidanceHandler);
        obstacleAvoidanceThread.start();

        // Wait for exit signal
        Button.waitForAnyPress();

        // Stop all threads and close resources
        lightSensorHandler.stopRunning();
        walkerHandler.stopRunning();
        obstacleAvoidanceHandler.stopRunning();
        RobotWalker.close(); // Close motor resources
    }
}
