// Main.java
package ev3.exercises;

import lejos.hardware.Button;
import ev3.exercises.library.ColorSensor;
//import ev3.exercises.library.UltraSonicSensor;
import lejos.hardware.port.SensorPort;

public class Main {
    public static void main(String[] args) {
        // 创建共享控制资源
        SharedControl sharedControl = new SharedControl();

        // 初始化颜色传感器和光传感器处理器
        ColorSensor colorSensor = new ColorSensor(SensorPort.S4);
        LightSensorHandler lightSensorHandler = new LightSensorHandler(colorSensor, sharedControl);
        Thread lightSensorThread = new Thread(lightSensorHandler);
        lightSensorThread.start();

        // 初始化机器人行走处理器
        RobotWalkerHandler walkerHandler = new RobotWalkerHandler(sharedControl);
        Thread walkerThread = new Thread(walkerHandler);
        walkerThread.start();
        
     // 初始化超声波传感器和避障处理器
        ObstacleAvoidanceHandler obstacleAvoidanceHandler = new ObstacleAvoidanceHandler(sharedControl);
        Thread obstacleAvoidanceThread = new Thread(obstacleAvoidanceHandler);
        obstacleAvoidanceThread.start();

        
        // 等待退出信号
        while (Button.ESCAPE.isUp()) {
            // 主线程的其他逻辑可以放在这里
        }

        // 停止所有线程并关闭资源
        lightSensorHandler.stopRunning();
        walkerHandler.stopRunning();
        obstacleAvoidanceHandler.stopRunning();
        RobotWalker.close();
    }
}
