package ev3.exercises;

import ev3.exercises.library.UltraSonicSensor;
import ev3.exercises.library.Lcd;
import lejos.hardware.port.SensorPort;
import lejos.robotics.RegulatedMotor;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;

public class ObstacleAvoidanceHandler implements Runnable {
    private UltraSonicSensor ultrasonicSensor;
    private SharedControl sharedControl;
    private volatile boolean running = true;
    private final float DISTANCE_THRESHOLD = 0.1f; // 障碍物检测阈值，单位为米   
    private long firstObstacleDetectionTime; // 记录第一次检测到障碍物的时间
    private boolean obstacleDetected = false; // 标记是否已检测到障碍物
    private boolean stopRobot = false; // 标记是否停止机器人
    
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
                if (!obstacleDetected) {
                    // 第一次检测到障碍物
                    firstObstacleDetectionTime = System.currentTimeMillis();
                    obstacleDetected = true;
                    avoidObstacle();
                } else {
                    // 第二次检测到障碍物
                    long currentTime = System.currentTimeMillis(); // 获取当前时间
                    long elapsedTime = currentTime - firstObstacleDetectionTime; // 计算与第一次检测到障碍物的时间间隔
                    stopRobot = true; // 标记停止机器人
                    displayTimeSinceLastObstacle(elapsedTime); // 显示时间间隔信息
                }
            } 

            // 如果标志为true，则停止机器人
            if (stopRobot) {
                synchronized (this) {
                    sharedControl.setRobotState("stop");
                    running = false;
                    Sound.systemSound(true, Sound.BEEP); 
                    wait(100);
                    Sound.systemSound(true, Sound.BEEP); 
                    
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
                        // 关闭电机
                        leftMotor.close();
                        rightMotor.close();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                }
            }

            try {
                Thread.sleep(50); // 检查频率
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wait(5);
        }
    }

    private void displayTimeSinceLastObstacle(long elapsedTime) {
        Lcd.clear(7);
        Lcd.print(7, "Time: %d s", elapsedTime/1000); // 在LCD上显示时间间隔
    }
    
    private void avoidObstacle() {   
        // 设置正在避障状态
        sharedControl.setAvoidingObstacle(true);
        // 机器人转向 turnRight 转45度 需1000ms 速度180
        sharedControl.setRobotState("turnRight");
        wait(1000);           
                
        //前进L=21.21cm,用时2.7s
        sharedControl.setRobotState("forwardLine");
        wait(2000);
        
        // 机器人转向 turnLeft 转90度 需2000ms 速度180
        sharedControl.setRobotState("turnLeft");
        wait(1500);
        
        boolean keepLine = true;
        while (keepLine) {
        	sharedControl.setRobotState("forwardLine");
            float redValue = sharedControl.getRedValue();
            Lcd.clear();
            Lcd.print(1, "Red Value: %.2f", redValue);
           
            if (redValue < 0.2) {
                // 当颜色传感器检测到的红色值小于0.2时，机器人退出避障  
            	Sound.systemSound(true, Sound.BEEP); 
                keepLine = false;
//                break;
                
            }
            wait(10);
        }
        
        sharedControl.setAvoidingObstacle(false);
        sharedControl.setRobotState("forward");
//        Sound.systemSound(true, Sound.BEEP);   
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