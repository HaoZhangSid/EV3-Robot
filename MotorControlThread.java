// MotorControlThread.java
package ev3.exercises;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class MotorControlThread extends Thread {
    private EV3LargeRegulatedMotor leftMotor;
    private EV3LargeRegulatedMotor rightMotor;
    private int Tp;
    private double controlValue; // 用于存储控制量
    
    public MotorControlThread() {
        leftMotor = new EV3LargeRegulatedMotor(MotorPort.C);
        rightMotor = new EV3LargeRegulatedMotor(MotorPort.B);
        Tp = (int) (0.25f * (leftMotor.getMaxSpeed() + rightMotor.getMaxSpeed()) / 2);
    }
    
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            // 读取控制量，并设置电机速度
            adjustMotorsSpeed(controlValue);
            try {
                Thread.sleep(100); // 可以调整电机控制的频率
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // 公共方法用于设置控制量
    public void setControlValue(double controlValue) {
        this.controlValue = controlValue;
    }
    
    private void adjustMotorsSpeed(double controlValue) {
        // 根据传入的控制量调整电机速度
        // 在这里编写调整电机速度的逻辑
    }

	public void stopMotors() {
		// TODO Auto-generated method stub
		
	}
}
