package org.usfirst.frc.team2585.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team2585.input.InputMethod;
import org.usfirst.frc.team2585.systems.CubeLiftSystem;

/**
 * Unit Tests for the cube lift system
 */
public class CubeLiftSystemTest {
	private TestInput input;
	private TestCubeLiftSystem cubeLiftSystem;
	
	boolean shouldThrowCube;
	boolean shouldCollectCube;
	
	boolean isTopSwitchPressed;
	boolean isBottomSwitchPressed;
	
	double motorSpeedOutput;
	
	/**
	 * Create new input and cube lift system for test
	 */
	@Before
	public void setUp() {
		input = new TestInput();
		cubeLiftSystem = new TestCubeLiftSystem();
		cubeLiftSystem.setInput(input);
		
		isTopSwitchPressed = false;
		isBottomSwitchPressed = false;
	}
	
	/**
	 * Tests that the speed of the motor is more than 0
	 * when the cube lift button is pressed
	 */
	@Test
	public void motorRunsWhenThrowing() {
		shouldThrowCube = true;
		shouldCollectCube = false;
		cubeLiftSystem.run();
		Assert.assertTrue(motorSpeedOutput > 0);
	}
	
	/**
	 * Tests that the speed of the motor starts as zero when the 
	 * user gives no input
	 */
	@Test
	public void defaultsToZero() {
		shouldThrowCube = false;
		shouldCollectCube = false;
		cubeLiftSystem.run();
		Assert.assertTrue(motorSpeedOutput == 0);
	}
	
	/**
	 * Tests that the output returns to zero after releasing the throw button
	 */
	@Test
	public void returnsToZeroAfterRunning() {
		shouldThrowCube = true;
		shouldCollectCube = false;
		cubeLiftSystem.run();
		shouldThrowCube = false;
		cubeLiftSystem.run();
		
		Assert.assertTrue(motorSpeedOutput == 0);
	}
	
	/**
	 * Testable input for the cube lift system tests
	 */
	private class TestInput extends InputMethod {
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.input.InputMethod#shouldThrowCube()
		 */
		@Override
		public boolean shouldRotateUp() {
			return shouldThrowCube;
		}
		
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.input.InputMethod#shouldCollectCube()
		 */
		@Override
		public boolean shouldRotateDown() {
			return shouldCollectCube;
		}
	}
	
	/**
	 * A testable CubeLiftSystem
	 */
	private class TestCubeLiftSystem extends CubeLiftSystem {

		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.systems.CubeLiftSystem#setMotorSpeed(double)
		 */
		@Override
		public void setMotorSpeed(double motorSpeed) {
			motorSpeedOutput = motorSpeed;
		}
		
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.systems.CubeLiftSystem#isSwitchPressedTop()
		 */
		@Override
		public boolean isTopSwitchPressed() {
			return isTopSwitchPressed;
		}
		
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.systems.CubeLiftSystem#isSwitchPressedBottom()
		 */
		@Override
		public boolean isBottomSwitchPressed() {
			return isBottomSwitchPressed;
		}
	}
}
