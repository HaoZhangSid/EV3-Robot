// RobotWalker.java

package ev3.exercises;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class RobotWalker {
    
    public static EV3LargeRegulatedMotor motorB = new EV3LargeRegulatedMotor(MotorPort.B);
    public static EV3LargeRegulatedMotor motorA = new EV3LargeRegulatedMotor(MotorPort.C);
    
    static final int DEFAULT_SPEED = 180; // Typical speed range is 0-900 deg/sec
    
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
    
    public static void forward(int speed) {
        motorA.setSpeed(speed);
        motorB.setSpeed(speed);
        motorA.forward();
        motorB.forward();
    }
    
    public static void forwardLine(int speed) {
        motorA.setSpeed(speed);
        motorB.setSpeed(speed);
        motorA.forward();
        motorB.forward();
    }

    public static void stop() {
        motorA.stop(true);
        motorB.stop(true);
    }

    public static void turnLeft(int speed) {
        motorA.setSpeed(0);
        motorB.setSpeed(speed);
        motorA.stop(true);
        motorB.forward();
    }

    public static void turnRight(int speed) {
        motorA.setSpeed(speed);
        motorB.setSpeed(0);
        motorA.forward();
        motorB.stop(true);
    }
    
    public static void backward(int speed) {
        motorA.setSpeed(speed);
        motorB.setSpeed(speed);
        motorA.backward();
        motorB.backward();
    }

    public static void rotate(int angle) {
        motorA.rotate(angle, true);
        motorB.rotate(-angle, false);
    }

    public static void close() {
        motorA.close();
        motorB.close();
    }
}
