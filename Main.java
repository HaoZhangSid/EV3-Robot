// Main.java
package ev3.exercises;

import ev3.exercises.library.Lcd;
import lejos.hardware.Button;

public class Main {
    public static void main(String[] args) {
        // 创建共享控制资源
        SharedControl sharedControl = new SharedControl();


        RobotWalker robotWalker = new RobotWalker(); // 创建电机控制器实例
        
//        // 等待按键指令
//        Lcd.clear();
//        Lcd.print(1, "Press ENTER to start");
//        Button.waitForAnyPress();
        
        LightSensorHandler lightSensorHandler = new LightSensorHandler(sharedControl);
        Thread lightSensorThread = new Thread(lightSensorHandler);
        lightSensorThread.start();

        // 初始化机器人行走处理器
        RobotWalkerHandler walkerHandler = new RobotWalkerHandler(sharedControl, robotWalker);
        Thread walkerThread = new Thread(walkerHandler);
        walkerThread.start();

        // 初始化超声波传感器和避障处理器
        ObstacleAvoidanceHandler obstacleAvoidanceHandler = new ObstacleAvoidanceHandler(sharedControl);
        Thread obstacleAvoidanceThread = new Thread(obstacleAvoidanceHandler);
        obstacleAvoidanceThread.start();

        // 等待退出信号
        Button.waitForAnyPress();

        // 停止所有线程并关闭资源
        lightSensorHandler.stopRunning();
        walkerHandler.stopRunning();
        obstacleAvoidanceHandler.stopRunning();
        RobotWalker.close(); // 关闭电机资源
    }
}
