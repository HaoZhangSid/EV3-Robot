// Main.java
package ev3.exercises;

import lejos.hardware.Button;

public class Run {
    public static void main(String[] args) {
        // Create shared control resource
        SharedControl sharedControl = new SharedControl();


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
