// ColorSensorThread.java
package ev3.exercises;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;

public class ColorSensorThread extends Thread {
    private EV3ColorSensor colorSensor;
    private SensorMode colorProvider;
    private float[] colorArr;
    private double target = 20;
    private double Kp = 20;
    private double Ki = 0.00007;
    private double Kd = 40;
    private double integral = 0;
    private double lastError = 0;
    
    public ColorSensorThread() {
        colorSensor = new EV3ColorSensor(SensorPort.S4);
        colorProvider = colorSensor.getRedMode(); // 设置为红光模式
        colorArr = new float[colorProvider.sampleSize()];
    }
    
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            colorProvider.fetchSample(colorArr, 0);
            double reflectedLight = colorArr[0] * 100;
            double error = reflectedLight - target;
            integral += error;
            double derivative = error - lastError;
            lastError = error;
            double sum = error * Kp + integral * Ki + derivative * Kd;
            // 控制量处理
            // 调用电机控制线程的方法设置电机速度
            try {
                Thread.sleep(100); // 可以调整读取颜色的频率
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
