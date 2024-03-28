// RobotWalkerHandler.java
package ev3.exercises;

import ev3.exercises.library.Lcd;

public class RobotWalkerHandler implements Runnable {
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private SharedControl sharedControl;

    public RobotWalkerHandler(SharedControl sharedControl) {
        this.sharedControl = sharedControl;
    }

    @Override
    public void run() {
        while (running) {
            if (!paused) {
            	// 更新并显示状态
            	String state = sharedControl.getRobotState();
                Lcd.clear(4); // 清除特定行的显示
                Lcd.print(4, "State: %s", state); // 显示当前状态
                // 根据共享资源的状态控制机器人
                switch (state) {
                    case "turnLeft":
                        RobotWalker.turnLeft(RobotWalker.DEFAULT_SPEED);
                        break;
                    case "turnRight":
                        RobotWalker.turnRight(RobotWalker.DEFAULT_SPEED);
                        break;
                    case "stop":
                        RobotWalker.stop(); // 停止机器人的动作
                        break;
                    case "forward":
                    default:
                        RobotWalker.forward(RobotWalker.DEFAULT_SPEED);
                        break;
                }
            }

            try {
                Thread.sleep(10); // 控制检查频率
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 停止运行方法
    public void stopRunning() {
        running = false;
        // 确保在停止运行时机器人也停止移动
        RobotWalker.stop();
    }

    // 暂停方法
    public void pause() {
        paused = true;
        // 在暂停时也停止机器人的动作
        RobotWalker.stop();
    }

    // 继续方法
    public void resume() {
        paused = false;
        // 在继续前进前，可以根据需要添加其他逻辑
    }
}
