// Main.java
package ev3.exercises;

import ev3.exercises.library.Lcd;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;

public class Main {
    public static void main(String[] args) {
        // 创建共享控制资源
        SharedControl sharedControl = new SharedControl();
        Brick brick = BrickFinder.getDefault();
        RegulatedMotor leftMotor = Motor.C; // 左电机
        RegulatedMotor rightMotor = Motor.B; // 右电机


 
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
        
        

//        // 初始化超声波传感器和避障处理器
//        ObstacleAvoidanceHandler obstacleAvoidanceHandler = new ObstacleAvoidanceHandler(sharedControl);
//        Thread obstacleAvoidanceThread = new Thread(obstacleAvoidanceHandler);
//        obstacleAvoidanceThread.start();

        // 等待退出信号
        Button.waitForAnyPress();

        // 停止所有线程并关闭资源
        lightSensorHandler.stopRunning();
        walkerHandler.stopRunning();
//        obstacleAvoidanceHandler.stopRunning();
        RobotWalker.close(); // 关闭电机资源
    }
}

/*
package ev3.exercises;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;

public class Main {
    public static void main(String[] args) {
        Brick brick = BrickFinder.getDefault();
        RegulatedMotor leftMotor = Motor.C; // 左电机
        RegulatedMotor rightMotor = Motor.B; // 右电机

        // 定义音符频率
        int[] notes = {1568, 1760, 1976, 2093, 1760, 2093, 2200, 2330, 1976, 2093,
                       1568, 1760, 1976, 2093, 1760, 2093, 2200, 2330, 1976, 2093};

        // 定义音符持续时间（毫秒）
        int[] durations = {200, 200, 200, 200, 200, 200, 400, 400, 200, 200,
                           200, 200, 200, 200, 200, 200, 400, 400, 200, 200}; // 对应音符的持续时间

        // 创建音乐播放和舞蹈程序执行的线程
        Thread musicThread = new Thread(new MusicPlayer(notes, durations));
        Thread danceThread = new Thread(new DanceProgram(leftMotor, rightMotor));

        // 启动线程
        musicThread.start();
        danceThread.start();

        try {
            // 等待两个线程执行完成
            musicThread.join();
            danceThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 关闭电机
        leftMotor.stop();
        rightMotor.stop();
    }
}
*/