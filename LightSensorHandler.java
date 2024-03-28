// LightSensorHandler.java
package ev3.exercises;

import ev3.exercises.library.ColorSensor;
import ev3.exercises.library.Lcd;
import lejos.hardware.Button;
import lejos.robotics.Color;

public class LightSensorHandler implements Runnable {
    private ColorSensor colorSensor;
    private SharedControl sharedControl;
    private volatile boolean running = true;
    private float thresholdHigh = 0.5f;
    private float thresholdLow = 0.1f;
    private volatile boolean paused = false;

    public LightSensorHandler(ColorSensor colorSensor, SharedControl sharedControl) {
        this.colorSensor = colorSensor;
        this.sharedControl = sharedControl;
    }

    @Override
    public void run() {
        Lcd.clear();
        colorSensor.setRedMode();
        colorSensor.setFloodLight(Color.RED);
        colorSensor.setFloodLight(true);

        while (running) {
        	if (!paused && !sharedControl.isAvoidingObstacle()) {
                float redReflection = colorSensor.getRed();
                String state = sharedControl.getRobotState();

                if (redReflection > thresholdHigh) {
                	// 使用同步方法设置状态
                    sharedControl.setRobotState("turnLeft");
                } else if (redReflection < thresholdLow) {
                	sharedControl.setRobotState("turnRight");
                } else {
                	sharedControl.setRobotState("forward");
                }

                Lcd.print(2, "Red: %.2f", redReflection);
                Lcd.print(4, "State: %s", state);
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // 检查按键事件
            if (Button.ENTER.isDown()) {
                paused = !paused; // 切换暂停/继续状态
            }

            if (Button.UP.isDown()) {
                thresholdHigh += 0.01f;
                Lcd.clear(6);
                Lcd.print(6, "High: %.2f", thresholdHigh);
            }

            if (Button.DOWN.isDown()) {
                thresholdHigh -= 0.01f;
                Lcd.clear(6);
                Lcd.print(6, "High: %.2f", thresholdHigh);
            }

            if (Button.LEFT.isDown()) {
                thresholdLow -= 0.01f;
                Lcd.clear(7);
                Lcd.print(7, "Low: %.2f", thresholdLow);
            }

            if (Button.RIGHT.isDown()) {
                thresholdLow += 0.01f;
                Lcd.clear(7);
                Lcd.print(7, "Low: %.2f", thresholdLow);
            }
        }
    }

    public void stopRunning() {
        running = false;
        paused = false; // 停止运行时也重置暂停状态
    }
}
