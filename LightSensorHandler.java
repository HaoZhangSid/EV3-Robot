// LightSensorHandler.java
package ev3.exercises;

import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import ev3.exercises.library.Lcd;


/**
 * Handles the operations related to the light sensor, including measuring reflection
 * intensity and implementing a PID controller for following a line. This class is designed
 * to continuously adjust the robot's movement based on the light sensor readings to follow
 * a line on the ground.
 */
public class LightSensorHandler implements Runnable {

    private EV3ColorSensor colorSensor;
    private SharedControl sharedControl;
    private volatile boolean running = true;
    private volatile boolean paused = false;

    // Parameters for PID controller
    private double Kp = 18; // Proportional coefficient 
    private double Ki = 0.001; // Integral coefficient 0.00005 0.000002 0
    private double Kd = 18; // Derivative coefficient 15
    private int target; // Target reflection intensity value
    private double Derivative = 0; // Derivative term
    private double Integral = 0; // Integral term
    private double Lasterror = 0; // Last error
    private double Error = 0; // Current error
    private double sum = 0; // Control value
    private double speedFactor = 1.0;

    /**
     * Constructs a new LightSensorHandler with a shared control object. It initializes
     * the color sensor and calculates the target reflection intensity value by measuring
     * the reflection of a black line and a white area.
     *
     * @param sharedControl The shared control object used for managing robot state.
     */
    public LightSensorHandler(SharedControl sharedControl) {
        this.sharedControl = sharedControl;
        this.colorSensor = new EV3ColorSensor(SensorPort.S4); // Color sensor connected to port S4

        // Measure reflection intensity of black line and white area
        SensorMode redMode = colorSensor.getRedMode();
        float[] colorArr = new float[redMode.sampleSize()];

        Lcd.clear();
        Lcd.print(1, "on black");
        Lcd.print(2, "ENTER to measure");
        Button.waitForAnyPress();
        redMode.fetchSample(colorArr, 0);
        int blackValue = (int) (colorArr[0] * 100);

        Lcd.clear();
        Lcd.print(1, "on white");
        Lcd.print(2, "ENTER to measure");
        Button.waitForAnyPress();
        redMode.fetchSample(colorArr, 0);
        int whiteValue = (int) (colorArr[0] * 100);

        // Calculate target value
        target = (blackValue + whiteValue) / 2;
//        target = 20; 
        Lcd.clear();
        Lcd.print(1, "B,W,T: " + blackValue + "," + whiteValue + "," + target);
        Lcd.print(2, "Press ENTER to start");
        Button.waitForAnyPress();
    }

    /**
     * The main run method that implements the PID control loop for line following.
     * It adjusts the robot's speed based on the light sensor readings to follow a line.
     */
    @Override
    public void run() {
//        Lcd.clear();
        SensorMode redMode = colorSensor.getRedMode(); // Get red light mode
        float[] colorArr = new float[redMode.sampleSize()]; // Create an array to store sensor readings
        

        while (running) {
             // Update color sensor mode to red light mode and fetch sample
            redMode.fetchSample(colorArr, 0);
            sharedControl.setRedValue(colorArr[0]); 
            if (!paused && !sharedControl.isAvoidingObstacle()) {
               

                // Calculate current reflection intensity
                double reflectedLight = (double) colorArr[0] * 100;

                // PID controller calculation
                Error = reflectedLight - target;
                Integral = Integral + Error;
                Derivative = Error - Lasterror;
                Lasterror = Error;
                sum = Error * Kp + Integral * Ki + Derivative * Kd;
                

                // Multiply control value by speed factor
                sum *= speedFactor;

                // Adjust motor speed according to control value
                if (sum > 0) {
                    sum /= 2000;
                    sum *= 700;
                } else if (sum < 0) {
                    sum /= 2000;
                    sum *= 700;
                }

                // Update motor speed
                sharedControl.setMotorSpeed(sum);

                // Display reflection intensity and PID output on LCD
                Lcd.clear(10);
                Lcd.print(2, "Red: %.2f", colorArr[0]);
            }

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Check button events
            if (Button.ENTER.isDown()) {
                paused = !paused; // Toggle pause/resume state
            }
        }
    }

    /**
     * Stops the PID control loop and terminates the thread.
     */
    public void stopRunning() {
        running = false;
        paused = false; // Reset pause state when stopping
    }
}
