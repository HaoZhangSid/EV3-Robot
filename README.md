# EV3 Robot: Tracking and Obstacle Avoidance
> https://github.com/HaoZhangSid/EV3-Robot

## Introduction
This project aims to develop an intelligent robot based on LEGO Mindstorms EV3, focusing on efficient path following and automatic obstacle avoidance capabilities. By utilizing EV3 sensors and programming skills, this robot can autonomously navigate in various environments, recognizing paths and avoiding obstacles ahead.




## Group Members 

**Han Zhang - Leader/Developer:** ,  Route Tracking Function, Tracking Optimization

**Aijun Fan - Developer:** Obstacle Avoidance Function, Turn Strategy Design

**Mengyun Li - Developer:** Music Player, Dancing Function

**Yilin Lai - Developer:** Document Editing, Multithreading




## Feature
- **Path Following Ability**: Utilizes bottom sensors to recognize and follow predetermined paths, capable of adapting to different colors and complexities of the path.
- **Automatic Obstacle Avoidance**: Detects obstacles ahead with an ultrasonic sensor and automatically calculates a detour route to ensure unimpeded progress.
- **Dancing & Playing music**: After the robot completes a cycle of tracking and obstacle avoidance operations, it will play specific music and swing in place.



## The Program Entrance & Main Functions

The `Run.java` class acts as the central hub for launching our intelligent EV3 robot application. This application is meticulously crafted to empower the robot with the ability to autonomously navigate its environment, adeptly following paths and skillfully avoiding obstacles in its path.

Upon execution, the program initiates by creating a shared control resource, `SharedControl`, which is crucial for synchronizing activities between the robot's sensor inputs and its movement mechanisms. This ensures a harmonious operation where sensor readings effectively inform the robot's walking and obstacle avoidance behaviors.

Three pivotal components of the robot are then initialized and run in separate threads, enabling concurrent execution that enhances the robot's responsiveness and decision-making capabilities:

1. **Light Sensor Input Handling**: A thread dedicated to monitoring light sensor inputs, enabling the robot to recognize and follow designated paths. This is achieved through the `LightSensorHandler`, which continuously assesses the environment for path markers.
2. **Robot Walking Behavior**: Managed by the `RobotWalkerHandler`, this thread controls the robot's locomotion, utilizing input from the shared control resource to adjust speed and direction, ensuring smooth navigation along the path.
3. **Obstacle Avoidance**: The `ObstacleAvoidanceHandler` thread leverages ultrasonic sensor data to detect obstacles in the robot's path. Upon detection, it computes and executes detour maneuvers, seamlessly guiding the robot around obstacles without halting its progress.

The application awaits a button press to gracefully terminate, stopping all running threads and properly closing resources. 



## Preparation
### Hardware
- 1 x LEGO Mindstorms EV3
- 1 x EV3 Color Sensor
- 1 x EV3 Ultrasonic Sensor

### Software
- EV3 Mindstorms software, LeJOS
- Operating System: Windows11
- Eclipse



## Reference links 

[Java for Lego Mindstorms](https://lejos.sourceforge.io/)

[Programming the EV3 platform with Java, Some basic examples](https://stemrobotics.cs.pdx.edu/node/4576.html)

[Multithreading with Java LeJOS](https://www.juanantonio.info/docs/2008/JAVA-LEJOS-MULTITHREADING.pdf)



