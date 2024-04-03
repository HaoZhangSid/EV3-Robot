// ObstacleAvoidanceHandler.java
package ev3.exercises;

import ev3.exercises.library.UltraSonicSensor;
import ev3.exercises.library.Lcd;
import lejos.hardware.port.SensorPort;

public class ObstacleAvoidanceHandler implements Runnable {
    private UltraSonicSensor ultrasonicSensor;
    private SharedControl sharedControl;
    private volatile boolean running = true;
    private final float DISTANCE_THRESHOLD = 0.2f; // 障碍物检测阈值，单位为米

    public ObstacleAvoidanceHandler(SharedControl sharedControl) {
        this.ultrasonicSensor = new UltraSonicSensor(SensorPort.S2); // 假设超声波传感器连接在S2端口
        this.sharedControl = sharedControl;
    }

    @Override
    public void run() {
        while (running) {
            float distance = ultrasonicSensor.getRange();
            Lcd.clear(6);
            Lcd.print(6, "Distance: %.2f m", distance); // 在LCD上实时显示距离值

            // 如果检测到障碍物
            if (distance < DISTANCE_THRESHOLD) {
                // 执行避障动作
                avoidObstacle();
            }

            try {
                Thread.sleep(10); // 检查频率
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void avoidObstacle() {
    	// 设置正在避障状态
        sharedControl.setAvoidingObstacle(true);
        // 停止机器人
    	sharedControl.setRobotState("stop");
        wait(3000); // 等待半秒

        // 向右转一定角度
        sharedControl.setRobotState("turnRight");
        wait(3000); // 假设转动需要的时间

        // 向前移动一段距离
        sharedControl.setRobotState("forward");
        wait(1000); // 假设移动需要的时间

        // 向左转一定角度，完成半圆绕行
        sharedControl.setRobotState("turnLeft");
        wait(1000); // 假设转动需要的时间

        // 继续前进直到找到黑线
        sharedControl.setRobotState("forward");
        // 避障完成后，重置避障状态
        sharedControl.setAvoidingObstacle(false);
    }

    private void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopRunning() {
        running = false;
    }
}
