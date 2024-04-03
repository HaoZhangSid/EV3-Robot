package ev3.exercises;

import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class DanceProgram implements Runnable {
    private RegulatedMotor leftMotor;
    private RegulatedMotor rightMotor;

    public DanceProgram(RegulatedMotor leftMotor, RegulatedMotor rightMotor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
    }

    @Override
    public void run() {
        for (int i = 0; i < 4; i++) {
            leftMotor.forward();
            rightMotor.backward();
            sleep(1000); // 等待1秒钟
            leftMotor.backward();
            rightMotor.forward();
            sleep(1000); // 等待1秒钟
        }
    }

    // 等待一段时间的辅助方法
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
