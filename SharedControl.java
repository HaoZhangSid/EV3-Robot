// SharedControl.java
package ev3.exercises;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedControl {
//private volatile String robotState = "forward";
private final Lock stateLock = new ReentrantLock();
private volatile String robotState = "forward";
private volatile float motorSpeed;
private volatile float redValue;
 public String getRobotState() {
     stateLock.lock();
     try {
         return robotState;
     } finally {
         stateLock.unlock();
     }
 }
 public synchronized float getMotorSpeed() {
     return motorSpeed;
 }

 public synchronized void setMotorSpeed(double speed) {
     this.motorSpeed = (float) speed;
 }
 public void setRobotState(String state) {
     stateLock.lock();
     try {
         robotState = state;
     } finally {
         stateLock.unlock();
     }
 }
//新增一个状态表示正在避障
 private volatile boolean avoidingObstacle = false;

 public boolean isAvoidingObstacle() {
     stateLock.lock();
     try {
         return avoidingObstacle;
     } finally {
         stateLock.unlock();
     }
 }

 public void setAvoidingObstacle(boolean avoiding) {
     stateLock.lock();
     try {
         avoidingObstacle = avoiding;
     } finally {
         stateLock.unlock();
     }
 }
public float getRedValue() {
	return redValue;
}
public void setRedValue(float redValue) {
    this.redValue = redValue;
}
}
