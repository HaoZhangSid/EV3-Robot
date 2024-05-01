package ev3.exercises;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Manages shared control and state information for the robot. This class provides synchronized access
 * to shared robot states such as the current movement state, motor speed, and sensor values. It uses
 * locking to ensure thread-safe modifications and access to these shared states.
 *i will add a new update
 */
public class SharedControl {
	/** Lock for managing state changes in a thread-safe manner. */
    private final Lock stateLock = new ReentrantLock();
    
    /** The current state of the robot (e.g., "forward", "stop"). */
    private volatile String robotState = "forward";
    
    /** The current speed at which the motors should operate. */
    private volatile float motorSpeed;
    
    /** The current value read from the red color sensor. */
    private volatile float redValue;
    
    
    
    // New variables to store action, speed, proportional, integral, derivative
    private volatile int action;
    private volatile int speed;
    private volatile double proportional;
    private volatile double integral;
    private volatile double derivative;
    
   

    // Methods to set and get action
    public void setAction(int action) {
        stateLock.lock();
        try {
            this.action = action;
        } finally {
            stateLock.unlock();
        }
    }

    public int getAction() {
        stateLock.lock();
        try {
            return action;
        } finally {
            stateLock.unlock();
        }
    }

    // Methods to set and get speed
    public synchronized void setSpeed(int speed) {
        stateLock.lock();
        try {
            this.speed = speed;
        } finally {
            stateLock.unlock();
        }
    }

    public synchronized int getSpeed() {
        stateLock.lock();
        try {
            return speed;
        } finally {
            stateLock.unlock();
        }
    }

    // Methods to set and get proportional
    public void setProportional(double proportional) {
        stateLock.lock();
        try {
            this.proportional = proportional;
        } finally {
            stateLock.unlock();
        }
    }

    public double getProportional() {
        stateLock.lock();
        try {
            return proportional;
        } finally {
            stateLock.unlock();
        }
    }

    // Methods to set and get integral
    public void setIntegral(double integral) {
        stateLock.lock();
        try {
            this.integral = integral;
        } finally {
            stateLock.unlock();
        }
    }

    public double getIntegral() {
        stateLock.lock();
        try {
            return integral;
        } finally {
            stateLock.unlock();
        }
    }

    // Methods to set and get derivative
    public void setDerivative(double derivative) {
        stateLock.lock();
        try {
            this.derivative = derivative;
        } finally {
            stateLock.unlock();
        }
    }

    public double getDerivative() {
        stateLock.lock();
        try {
            return derivative;
        } finally {
            stateLock.unlock();
        }
    }
    

    /**
     * Retrieves the current state of the robot.
     *
     * @return The current robot state.
     */
    public String getRobotState() {
        stateLock.lock();
        try {
            return robotState;
        } finally {
            stateLock.unlock();
        }
    }

    /**
     * Retrieves the current speed setting for the motors.
     *
     * @return The current motor speed.
     */
    public synchronized float getMotorSpeed() {
        return motorSpeed;
    }

    /**
     * Sets the speed for the motors.
     *
     * @param speed The desired speed for the motors.
     */
    public synchronized void setMotorSpeed(double speed) {
        this.motorSpeed = (float) speed;
    }

    /**
     * Updates the robot's state.
     *
     * @param state The new state to set for the robot.
     */
    public void setRobotState(String state) {
        stateLock.lock();
        try {
            robotState = state;
        } finally {
            stateLock.unlock();
        }
    }

    /** Flag indicating whether the robot is currently avoiding an obstacle. */
    // New state indicating obstacle avoidance
    private volatile boolean avoidingObstacle = false;

    /**
     * Checks if the robot is currently in obstacle avoidance mode.
     *
     * @return {@code true} if the robot is avoiding an obstacle, otherwise {@code false}.
     */
    public boolean isAvoidingObstacle() {
        stateLock.lock();
        try {
            return avoidingObstacle;
        } finally {
            stateLock.unlock();
        }
    }

    /**
     * Sets the flag indicating whether the robot is avoiding an obstacle.
     *
     * @param avoiding {@code true} to indicate the robot is avoiding an obstacle, {@code false} otherwise.
     */
    public void setAvoidingObstacle(boolean avoiding) {
        stateLock.lock();
        try {
            avoidingObstacle = avoiding;
        } finally {
            stateLock.unlock();
        }
    }

    /**
     * Retrieves the current value from the red color sensor.
     *
     * @return The current value read from the red color sensor.
     */
    public float getRedValue() {
        return redValue;
    }

    /**
     * Sets the value read from the red color sensor.
     *
     * @param redValue The value to set from the red color sensor.
     */
    public void setRedValue(float redValue) {
        this.redValue = redValue;
    }
}
