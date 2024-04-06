// RobotWalker.java

package ev3.exercises;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

/**
 * Provides functionalities for controlling the movement of a two-wheeled robot.
 * This includes basic movements such as forward, backward, turn left, turn right,
 * and more complex actions like setting specific speeds for each motor or rotating
 * the robot by a specified angle. The class is designed for robots using two EV3
 * large regulated motors.
 */
public class RobotWalker {
    
	/** Motor connected to port B of the EV3 brick, typically used for one side of the robot. */
    public static EV3LargeRegulatedMotor motorB = new EV3LargeRegulatedMotor(MotorPort.B);
    
    /** Motor connected to port C of the EV3 brick, typically used for the other side of the robot. */
    public static EV3LargeRegulatedMotor motorA = new EV3LargeRegulatedMotor(MotorPort.C);
    
    /** Default speed for the motors. */
    static final int DEFAULT_SPEED = 180; // Typical speed range is 0-900 deg/sec
    
    /**
     * Sets the speed of both motors, allowing for forward and backward movement.
     * The method adjusts the direction of each motor based on the sign of the speed parameters.
     *
     * @param _motorASpeed Speed for motor A. Positive values move forward, negative values move backward.
     * @param _motorBSpeed Speed for motor B. Positive values move forward, negative values move backward.
     */
    public static void setMotorsSpeed(float _motorASpeed, float _motorBSpeed) {
        if (_motorASpeed >= 0 && _motorBSpeed >= 0) {
            motorA.setSpeed(_motorASpeed);
            motorB.setSpeed(_motorBSpeed);
            motorA.forward();
            motorB.forward();
        } else if (_motorASpeed < 0 && _motorBSpeed >= 0) {
            _motorASpeed = -_motorASpeed;
            motorA.setSpeed(_motorASpeed);
            motorB.setSpeed(_motorBSpeed);
            motorA.backward();
            motorB.forward();
        } else if (_motorBSpeed < 0 && _motorASpeed >= 1) {
            _motorBSpeed = -_motorBSpeed;
            motorA.setSpeed(_motorASpeed);
            motorB.setSpeed(_motorBSpeed);
            motorA.forward();
            motorB.backward();
        } else {
            _motorASpeed = -_motorASpeed;
            _motorBSpeed = -_motorBSpeed;
            motorA.setSpeed(_motorASpeed);
            motorB.setSpeed(_motorBSpeed);
            motorA.backward();
            motorB.backward();
        }
    }
    
    /**
     * Moves the robot forward at the specified speed.
     *
     * @param speed Speed at which both motors should rotate to move the robot forward.
     */
    public static void forward(int speed) {
        motorA.setSpeed(speed);
        motorB.setSpeed(speed);
        motorA.forward();
        motorB.forward();
    }
    
    /**
     * Moves the robot forward on a straight line at the specified speed.
     * Similar to forward(int speed) but might be adjusted for straight line movement.
     *
     * @param speed Speed at which both motors should rotate.
     */
    public static void forwardLine(int speed) {
        motorA.setSpeed(speed);
        motorB.setSpeed(speed);
        motorA.forward();
        motorB.forward();
    }

    /**
     * Stops both motors immediately.
     */
    public static void stop() {
        motorA.stop(true);
        motorB.stop(true);
    }

    /**
     * Turns the robot to the left by stopping or slowing down the left motor and speeding up the right motor.
     *
     * @param speed Speed at which the right motor should rotate to turn the robot left.
     */
    public static void turnLeft(int speed) {
        motorA.setSpeed(0);
        motorB.setSpeed(speed);
        motorA.stop(true);
        motorB.forward();
    }

    /**
     * Turns the robot to the right by stopping or slowing down the right motor and speeding up the left motor.
     *
     * @param speed Speed at which the left motor should rotate to turn the robot right.
     */
    public static void turnRight(int speed) {
        motorA.setSpeed(speed);
        motorB.setSpeed(0);
        motorA.forward();
        motorB.stop(true);
    }
    
    /**
     * Moves the robot backward at the specified speed.
     *
     * @param speed Speed at which both motors should rotate to move the robot backward.
     */
    public static void backward(int speed) {
        motorA.setSpeed(speed);
        motorB.setSpeed(speed);
        motorA.backward();
        motorB.backward();
    }

    /**
     * Rotates the robot in place by the specified angle. Positive angles rotate the robot to the left,
     * and negative angles rotate it to the right.
     *
     * @param angle Angle in degrees by which the robot should rotate. Positive for left turn, negative for right turn.
     */
    public static void rotate(int angle) {
        motorA.rotate(angle, true);
        motorB.rotate(-angle, false);
    }

    /**
     * Closes the motor ports, releasing any resources they might be holding.
     */
    public static void close() {
        motorA.close();
        motorB.close();
    }
}
