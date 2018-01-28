package org.usfirst.frc.team2585.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team2585.input.InputMethod;
import org.usfirst.frc.team2585.systems.WheelSystem;

/**
 * Unit Tests for the wheel system
 */

public class WheelSystemTest {
	private TestInput input;
	private TestWheelSystem wheelSystem;
	
	private double forwardAmountInput;
	private double rotationAmountInput;
	
	private double forwardAmountOutput;
	private double rotationAmountOutput;
	
	/**
	 * Set up the wheel system for testing and initialize inputs to 0
	 */
	@Before
	public void setUp() {
		input = new TestInput();
		newWheelSystem();
		resetInput();
	}
	
	/**
	 * Create a new wheel system with reset values of movement and rotation
	 */
	public void newWheelSystem() {
		wheelSystem = new TestWheelSystem();
		wheelSystem.setInput(input);
	}
	
	/**
	 * Set the inputs to their default values
	 */
	public void resetInput() {
		forwardAmountInput = 0;
		rotationAmountInput = 0;
		forwardAmountOutput = 0;
		rotationAmountOutput = 0;
	}
	
	/**
	 * Tests that motor outputs are 0 when user gives no input
	 */
	@Test
	public void defaultsToZero() {
		wheelSystem.run();
		Assert.assertTrue(forwardAmountOutput == 0);
		Assert.assertTrue(rotationAmountOutput == 0);
	}
	
	/**
	 * Tests that forward ramps with positive difference
	 */
	@Test
	public void testPositiveRamp() {
		forwardAmountInput = 1;
		wheelSystem.run();
		Assert.assertTrue(forwardAmountOutput == 0.65);
	}
	
	/**
	 * Tests that forward ramps with negative difference
	 */
	@Test
	public void testNegativeRamp() {
		forwardAmountInput = -1;
		wheelSystem.run();
		Assert.assertTrue(forwardAmountOutput == -0.65);
	}
	
	/**
	 * Tests that the deadzone is dead
	 */
	@Test
	public void testDeadzone() {
		// Tests forward deadzone
		forwardAmountInput = 0.15;
		wheelSystem.run();
		Assert.assertTrue(forwardAmountOutput == 0);
		// Close to deadzone should still be able to move forward
		forwardAmountInput = 0.16;
		wheelSystem.run();
		Assert.assertTrue(forwardAmountOutput > 0);
		// Tests backward deadzone
		forwardAmountInput = -0.15;
		wheelSystem.run();
		Assert.assertTrue(forwardAmountOutput == 0);
		// Close to deadzone should still be able to move backwards
		forwardAmountInput = -0.16;
		wheelSystem.run();
		Assert.assertTrue(forwardAmountOutput < 0);
		// Tests right rotation deadzone
		rotationAmountInput = 0.15;
		wheelSystem.run();
		Assert.assertTrue(rotationAmountOutput == 0);
		// Close to deadzone should still be able to turn right
		rotationAmountInput = 0.16;
		wheelSystem.run();
		Assert.assertTrue(rotationAmountOutput > 0);
		// Tests left rotation deadzone
		rotationAmountInput = -0.15;
		wheelSystem.run();
		Assert.assertTrue(rotationAmountOutput == 0);
		// Close to deadzone should still be able to turn left
		rotationAmountInput = -0.16;
		wheelSystem.run();
		Assert.assertTrue(rotationAmountOutput < 0);
	}
	
	/**
	 * Tests that forward returns to 0 after multiple runs
	 */
	@Test
	public void testForwardReturnsToZero() {
		forwardAmountInput = 1;
		for(int i=0; i<10; i++) {
			wheelSystem.run();
		}
		Assert.assertTrue(forwardAmountOutput > 0);
		forwardAmountInput = 0;
		wheelSystem.run();
		Assert.assertTrue(forwardAmountOutput == 0);
	}
	
	/**
	 * Tests that backwards returns to 0 after multiple runs
	 */
	@Test
	public void testBackwardsReturnsToZero() {
		forwardAmountInput = -1;
		for(int i=0; i<10; i++) {
			wheelSystem.run();
		}
		Assert.assertTrue(forwardAmountOutput < 0);
		forwardAmountInput = 0;
		wheelSystem.run();
		Assert.assertTrue(forwardAmountOutput == 0);
	}
	
	/**
	 * Tests that rotating right returns to 0 after multiple runs
	 */
	@Test
	public void testRotateRightReturnsToZero() {
		rotationAmountInput = 1;
		for(int i=0; i<10; i++) {
			wheelSystem.run();
		}
		Assert.assertTrue(rotationAmountOutput > 0);
		rotationAmountInput = 0;
		wheelSystem.run();
		Assert.assertTrue(forwardAmountOutput == 0);
	}
	
	/**
	 * Tests that rotating left returns to 0 after multiple runs
	 */
	@Test
	public void testRotateLeftReturnsToZero() {
		rotationAmountInput = -1;
		for(int i=0; i<10; i++) {
			wheelSystem.run();
		}
		Assert.assertTrue(rotationAmountOutput < 0);
		rotationAmountInput = 0;
		wheelSystem.run();
		Assert.assertTrue(forwardAmountOutput == 0);
	}
	
	/**
	 * Tests that rotation multiplier is working
	 */
	@Test
	public void testRotationMultiplier() {
		rotationAmountInput = 1;
		wheelSystem.run();
		Assert.assertTrue(rotationAmountOutput == 0.5);
		rotationAmountInput = -1;
		wheelSystem.run();
		Assert.assertTrue(rotationAmountOutput == -0.5);
	}
	
	/**
	 * Tests that robot can turn while driving
	 */
	@Test
	public void testRotateWhileForward() {
		forwardAmountInput = 1;
		rotationAmountInput = 1;
		wheelSystem.run();
		Assert.assertTrue(forwardAmountOutput > 0);
		Assert.assertTrue(rotationAmountOutput > 0);
		forwardAmountInput = -1;
		rotationAmountInput = -1;
		wheelSystem.run();
		Assert.assertTrue(forwardAmountOutput < 0);
		Assert.assertTrue(rotationAmountOutput < 0);
	}

	/**
	 * Testable input for the wheel system tests
	 */
	private class TestInput extends InputMethod {
		@Override
		public double forwardAmount() {
			return forwardAmountInput;
		}
		
		@Override
		public double rotationAmount() {
			return rotationAmountInput;
		}
	}
	
	/**
	 * A testable wheel system
	 */
	private class TestWheelSystem extends WheelSystem {
		@Override
		public void arcadeDrive(double forward, double rotation){
			forwardAmountOutput = forward;
			rotationAmountOutput = rotation;
		}
	}
}
