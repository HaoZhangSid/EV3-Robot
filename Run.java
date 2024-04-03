// 导入必要的包，用于控制EV3硬件设备
package ev3.exercises;

import lejos.hardware.Button; // 导入按钮类
import lejos.hardware.port.SensorPort; // 导入传感器端口类
import lejos.hardware.sensor.EV3ColorSensor; // 导入EV3颜色传感器类
import lejos.hardware.motor.EV3LargeRegulatedMotor; // 导入EV3大型电机类
import lejos.hardware.port.MotorPort; // 导入电机端口类
import lejos.hardware.sensor.SensorMode; // 导入传感器模式类

public class Run {
    
    // 定义左右电机的静态变量，用于控制机器人的运动
    private static EV3LargeRegulatedMotor leftMotor;
    private static EV3LargeRegulatedMotor rightMotor;
    
    // 调整速度的因子
    private static double speedFactor = 1.5;
    
    // main方法是程序的入口点
    public static void main(String[] args) {
        
        // PID控制器的参数，用于调整电机速度
        double Kp = 40; // 比例系数
        double Ki = 0.0002; // 积分系数
        double Kd = 40; // 微分系数
        
        // 目标反射光强度值
        int target = 20;
        
        // PID控制器的变量
        double Derivative = 0; // 微分项
        double Integral = 0; // 积分项
        double Last_error = 0; // 上一次误差
        double Error = 0; // 当前误差
        double sum = 0; // 控制量
        
        // 初始化左右电机
        rightMotor = new EV3LargeRegulatedMotor(MotorPort.B); // 右电机连接到端口B
        leftMotor = new EV3LargeRegulatedMotor(MotorPort.C); // 左电机连接到端口C
        
        // 初始化颜色传感器
        EV3ColorSensor color = new EV3ColorSensor(SensorPort.S4); // 颜色传感器连接到端口S4
        SensorMode colorProvider; // 颜色传感器模式的提供者
        colorProvider = color.getAmbientMode(); // 获取环境光模式
        float[] colorArr = new float[colorProvider.sampleSize()]; // 创建一个数组，用于存储传感器读数
        
        // 计算时间比例因子Tp，用于调整电机速度
        int Tp = (int)(0.25f*(leftMotor.getMaxSpeed()+rightMotor.getMaxSpeed())/2);
        
        // 开始无限循环，控制机器人运动
        while(!Button.ESCAPE.isDown()) { // 添加按退出键停止的功能
            // 更新颜色传感器模式为红光模式，并读取样本
            colorProvider = color.getRedMode();
            colorProvider.fetchSample(colorArr, 0);
            
            // 计算当前反射光强度
            double reflectedLight = (double) colorArr[0] * 100;
            
            // 计算误差
            Error = reflectedLight - target;
            Integral = Integral + Error; // 更新积分项
            Derivative = Error - Last_error; // 更新微分项
            Last_error = Error; // 更新上一次误差
            
            // 计算控制量
            sum = Error*Kp + Integral*Ki + Derivative*Kd;
            
            // 根据控制量调整电机速度并乘以速度因子
            sum = sum * speedFactor;
            
            // 根据控制量调整电机速度
            if (sum > 0) {
                sum = sum/2000;
                sum = sum*700;
            } else if (sum < 0) {
                sum = sum/2000;
                sum = sum*700;
            }
            
            // 调用方法设置电机速度
            setMotorsSpeed((float)(Tp-sum), (float)(Tp+sum));
        }
        
        // 程序结束时停止电机
        leftMotor.stop();
        rightMotor.stop();
    }
    
    // setMotorsSpeed方法用于设置左右电机的速度
    private static void setMotorsSpeed(float _leftMotorSpeed, float _rightMotorSpeed) {
        // 根据传入的速度值设置电机运动方向和速度
        if (_leftMotorSpeed >= 0 && _rightMotorSpeed >= 0) {
            // 左右电机速度均大于等于0，电机向前运动
            leftMotor.setSpeed(_leftMotorSpeed); // 设置左电机速度
            rightMotor.setSpeed(_rightMotorSpeed); // 设置右电机速度
            leftMotor.forward(); // 左电机向前运动
            rightMotor.forward(); // 右电机向前运动
        } else if (_leftMotorSpeed < 0 && _rightMotorSpeed >= 0) {
            // 左电机速度小于0，右电机速度大于等于0，左电机向后运动，右电机向前运动
            _leftMotorSpeed = -_leftMotorSpeed; // 取反左电机速度
            leftMotor.setSpeed(_leftMotorSpeed); // 设置左电机速度
            rightMotor.setSpeed(_rightMotorSpeed); // 设置右电机速度
            leftMotor.backward(); // 左电机向后运动
            rightMotor.forward(); // 右电机向前运动
        } else if (_rightMotorSpeed < 0 && _leftMotorSpeed >= 1) {
            // 右电机速度小于0，左电机速度大于等于0，右电机向后运动，左电机向前运动
            _rightMotorSpeed = -_rightMotorSpeed; // 取反右电机速度
            leftMotor.setSpeed(_leftMotorSpeed); // 设置左电机速度
            rightMotor.setSpeed(_rightMotorSpeed); // 设置右电机速度
            leftMotor.forward(); // 左电机向前运动
            rightMotor.backward(); // 右电机向后运动
        } else {
            // 左右电机速度均小于0，电机向后运动
            _leftMotorSpeed = -_leftMotorSpeed; // 取反左电机速度
            _rightMotorSpeed = -_rightMotorSpeed; // 取反右电机速度
            leftMotor.setSpeed(_leftMotorSpeed); // 设置左电机速度
            rightMotor.setSpeed(_rightMotorSpeed); // 设置右电机速度
            leftMotor.backward(); // 左电机向后运动
            rightMotor.backward(); // 右电机向后运动
        }
    }
}
