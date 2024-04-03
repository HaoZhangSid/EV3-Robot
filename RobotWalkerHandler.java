// RobotWalkerHandler.java
package ev3.exercises;

import ev3.exercises.library.Lcd;

public class RobotWalkerHandler implements Runnable {
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private SharedControl sharedControl;
    private RobotWalker robotWalker; // 将 RobotWalker 存储为类的成员变量

    public RobotWalkerHandler(SharedControl sharedControl, RobotWalker robotWalker) {
        this.sharedControl = sharedControl;
        this.robotWalker = robotWalker; // 存储 RobotWalker 对象
    }
 
    @Override
    public void run() {
        // 计算时间比例因子Tp，用于调整电机速度
        int Tp = (int)(0.15f*(RobotWalker.motorA.getMaxSpeed()+RobotWalker.motorB.getMaxSpeed())/2);
        while (running) {
            if (!paused && sharedControl != null) { // 添加对 sharedControl 对象是否为空的检查
                // 更新并显示状态
                String state = sharedControl.getRobotState();
                Lcd.clear(4); // 清除特定行的显示
                Lcd.print(4, "State: %s", state); // 显示当前状态
                // 根据共享资源的状态控制机器人
                switch (state) {
                    case "stop":
                        RobotWalker.stop(); // 使用存储的 RobotWalker 对象
                        break;
                    case "forward":
                        // 获取PID控制器的输出值
                        double sum = sharedControl.getMotorSpeed();
                        // 设置电机速度

                        RobotWalker.setMotorsSpeed((float)(Tp-sum), (float)(Tp+sum)); // 使用存储的 RobotWalker 对象

                        // 在LCD上显示PID输出
                        Lcd.clear(1);
                        Lcd.print(1, "PID: %.2f", sum);
                        break;
                    default:

                }
            }

            try {
                Thread.sleep(0); // 控制检查频率
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    // 停止运行方法
    public void stopRunning() {
    	running = false;
        paused = false; // 停止运行时也重置暂停状态
        robotWalker.stop(); // 停止电机运行
    }

    // 暂停方法
    public void pause() {
        paused = true;
        // 在暂停时也停止机器人的动作
        robotWalker.stop();
    }

    // 继续方法
    public void resume() {
        paused = false;
        // 在继续前进前，可以根据需要添加其他逻辑
    }
}
