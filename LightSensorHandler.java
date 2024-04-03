// LightSensorHandler.java
package ev3.exercises;

import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import ev3.exercises.library.Lcd;

public class LightSensorHandler implements Runnable {

    static EV3ColorSensor colorSensor;
    private SharedControl sharedControl;
    private volatile boolean running = true;
    private volatile boolean paused = false;

    // PID控制器的参数
    private double Kp = 9; // 比例系数 25
    private double Ki = 0.001; // 积分系数 0.00005 0.000002 0
    private double Kd = 18; // 微分系数 15
    private int target; // 目标反射光强度值
    private double Derivative = 0; // 微分项
    private double Integral = 0; // 积分项
    private double Lasterror = 0; // 上一次误差
    private double Error = 0; // 当前误差
    private double sum = 0; // 控制量
    private double speedFactor = 1.0;

    public LightSensorHandler(SharedControl sharedControl) {
        this.sharedControl = sharedControl;
        this.colorSensor = new EV3ColorSensor(SensorPort.S4); // 颜色传感器连接到端口S4

        // 测量黑色线和白色区域的反射光强度
        SensorMode redMode = colorSensor.getRedMode();
        float[] colorArr = new float[redMode.sampleSize()];

        Lcd.clear();
        Lcd.print(1, "on black");
        Lcd.print(2, "ENTER to measure");
        Button.waitForAnyPress();
        redMode.fetchSample(colorArr, 0);
        int blackValue = (int) (colorArr[0] * 100);

        Lcd.clear();
        Lcd.print(1, "on white");
        Lcd.print(2, "ENTER to measure");
        Button.waitForAnyPress();
        redMode.fetchSample(colorArr, 0);
        int whiteValue = (int) (colorArr[0] * 100);

     // 计算目标值target
        target = (blackValue + whiteValue) / 2;
//        target = 20; 
        Lcd.clear();
        Lcd.print(1, "B,W,T: " + blackValue + "," + whiteValue + "," + target);
        Lcd.print(2, "Press ENTER to start");
        Button.waitForAnyPress();
    }

    @Override
    public void run() {
        Lcd.clear();

        SensorMode redMode = colorSensor.getRedMode(); // 获取红光模式
        float[] colorArr = new float[redMode.sampleSize()]; // 创建一个数组，用于存储传感器读数

        while (running) {
            if (!paused && !sharedControl.isAvoidingObstacle()) {
                // 更新颜色传感器模式为红光模式，并读取样本
                redMode.fetchSample(colorArr, 0);

                // 计算当前反射光强度
                double reflectedLight = (double) colorArr[0] * 100;

                // PID控制器计算
                Error = reflectedLight - target;
                Integral = Integral + Error;
                Derivative = Error - Lasterror;
                Lasterror = Error;
                sum = Error * Kp + Integral * Ki + Derivative * Kd;
                

                // 控制量乘以速度因子
                sum *= speedFactor;

                // 根据控制量调整电机速度
                if (sum > 0) {
                    sum /= 2000;
                    sum *= 700;
                } else if (sum < 0) {
                    sum /= 2000;
                    sum *= 700;
                }

                // 更新电机速度
                sharedControl.setMotorSpeed(sum);

                // 在LCD上显示反射光强度和PID输出
                Lcd.clear(10);
                Lcd.print(2, "Red: %.2f", reflectedLight);
            }

            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 检查按键事件
            if (Button.ENTER.isDown()) {
                paused = !paused; // 切换暂停/继续状态
            }
        }
    }

    public void stopRunning() {
        running = false;
        paused = false; // 停止运行时也重置暂停状态
    }
}