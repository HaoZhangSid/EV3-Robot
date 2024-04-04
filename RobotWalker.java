// RobotWalker.java

package ev3.exercises;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class RobotWalker {
	
	public static EV3LargeRegulatedMotor motorB = new EV3LargeRegulatedMotor(MotorPort.B);
	public static EV3LargeRegulatedMotor motorA = new EV3LargeRegulatedMotor(MotorPort.C);
    
	 // 初始化左右电机
//	    motorB = new EV3LargeRegulatedMotor(MotorPort.B); // 右电机连接到端口B
//	    motorA = new EV3LargeRegulatedMotor(MotorPort.C); // 左电机连接到端口C

    static final int DEFAULT_SPEED = 180; // Typical speed range is 0-900 deg/sec
 // setMotorsSpeed方法用于设置左右电机的速度
    public static void setMotorsSpeed(float _motorASpeed, float _motorBSpeed) {
        // 根据传入的速度值设置电机运动方向和速度
        if (_motorASpeed >= 0 && _motorBSpeed >= 0) {
            // 左右电机速度均大于等于0，电机向前运动
        	motorA.setSpeed(_motorASpeed); // 设置左电机速度
        	motorB.setSpeed(_motorBSpeed); // 设置右电机速度
            motorA.forward(); // 左电机向前运动
            motorB.forward(); // 右电机向前运动
        } else if (_motorASpeed < 0 && _motorBSpeed >= 0) {
            // 左电机速度小于0，右电机速度大于等于0，左电机向后运动，右电机向前运动
            _motorASpeed = -_motorASpeed; // 取反左电机速度
            motorA.setSpeed(_motorASpeed); // 设置左电机速度
            motorB.setSpeed(_motorBSpeed); // 设置右电机速度
            motorA.backward(); // 左电机向后运动
            motorB.forward(); // 右电机向前运动
        } else if (_motorBSpeed < 0 && _motorASpeed >= 1) {
            // 右电机速度小于0，左电机速度大于等于0，右电机向后运动，左电机向前运动
            _motorBSpeed = -_motorBSpeed; // 取反右电机速度
            motorA.setSpeed(_motorASpeed); // 设置左电机速度
            motorB.setSpeed(_motorBSpeed); // 设置右电机速度
            motorA.forward(); // 左电机向前运动
            motorB.backward(); // 右电机向后运动
        } else {
            // 左右电机速度均小于0，电机向后运动
            _motorASpeed = -_motorASpeed; // 取反左电机速度
            _motorBSpeed = -_motorBSpeed; // 取反右电机速度
            motorA.setSpeed(_motorASpeed); // 设置左电机速度
            motorB.setSpeed(_motorBSpeed); // 设置右电机速度
            motorA.backward(); // 左电机向后运动
            motorB.backward(); // 右电机向后运动
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