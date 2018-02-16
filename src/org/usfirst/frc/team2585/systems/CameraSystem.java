package org.usfirst.frc.team2585.systems;

import org.usfirst.frc.team2585.robot.Environment;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.wpilibj.CameraServer;

/**
 * Handle the cameras on the robot
 */
public class CameraSystem extends RobotSystem {
	VideoSink server;
	
	UsbCamera frontCam = null;
	UsbCamera backCam = null;
	
	boolean isFacingFront = false;
	boolean previousTrigger = false;
	
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.RobotSystem#init(org.usfirst.frc.team2585.robot.Environment)
	 */
	@Override
	public void init(Environment environ) {
		super.init(environ);
		
		frontCam = CameraServer.getInstance().startAutomaticCapture(0);
		backCam = CameraServer.getInstance().startAutomaticCapture(1);
		
		server = CameraServer.getInstance().getServer();
	}

	/**
	 * Switch the current view being shown
	 */
	public void switchCameras() {
		if (isFacingFront) {
			faceBackwards();
		} else {
			faceForwards();
		}
	}
	
	/**
	 * Switch to the back camera and set the wheelsystem to drive backwards
	 */
	private void faceBackwards() {
		server.setSource(backCam);
		((WheelSystem) environment.getSystem(Environment.WHEEL_SYSTEM)).faceBackwards();
		isFacingFront = false;
	}
	
	/**
	 * Switch to the front camera and set the wheelsystem to drive forwards
	 */
	private void faceForwards() {
		server.setSource(frontCam);
		((WheelSystem) environment.getSystem(Environment.WHEEL_SYSTEM)).faceForwards();
		isFacingFront = true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (input.shouldSwitchCamera() && !previousTrigger) {
			switchCameras();
		}
		previousTrigger = input.shouldSwitchCamera();
	}
	
	/**
	 * Destroy the cameras
	 */
	public void endCameras() {
		if (frontCam != null) {
			frontCam.free();
			frontCam = null;
		}
		
		if (backCam != null) {
			backCam.free();
			backCam = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.impact2585.lib2585.Destroyable#destroy()
	 */
	@Override
	public void destroy() {
		endCameras();
	}

	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.RobotSystem#stop()
	 */
	@Override
	public void stop() {
		endCameras();
	}
	
}
