package com.ng.vis.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Transform;
import com.ng.gdxutils._GameManager;
import com.ng.vis.Enums;
import com.ng.vis.component.B2DSteerableComponent;
import com.ng.vis.component.MovementComponent;
import com.ng.vis.component.SteerableComponent;

/**
 * (c) 2016 Abhishek Aryan
 *
 * @author Abhishek Aryan
 * @since 11/29/2015.
 *
 * System that handle steering property of Entity.
 * Entity having SteerableComponent or B2DSteerableComponent
 * Steering is part of Artificial Intelligence (gdx-ai)
 *
 */

public class SteeringSystem extends EntityProcessingSystem{

	private static final String TAG = "[" + SteeringSystem.class.getSimpleName() + "]";

	//ComponentMapper<BasicComponent> transformMapper;
	ComponentMapper<Transform> transformCm;
	ComponentMapper<SteerableComponent> steerMapper;
	ComponentMapper<PhysicsBody> physicsMapper;
	ComponentMapper<B2DSteerableComponent> b2dSteerMapper;

	GroupManager groupManager;
	PlayerManager playerManager;

	public SteeringSystem() {
		super(Aspect.one(SteerableComponent.class,B2DSteerableComponent.class).all(Transform.class));
	}

	@Override
	protected void process(Entity e) {

		Transform transform=transformCm.get(e);

		SteerableComponent steerableComponent =steerMapper.getSafe(e);
		PhysicsBody physicsComponent=physicsMapper.getSafe(e);
		B2DSteerableComponent b2DSteerableComponent =b2dSteerMapper.getSafe(e);

			if(b2DSteerableComponent !=null){
				
				if(b2DSteerableComponent.steeringBehavior!=null){
					b2DSteerableComponent.steeringBehavior.calculateSteering(b2DSteerableComponent.steeringOutput);
					applySteering(b2DSteerableComponent, world.getDelta());
				}
				else {
						b2DSteerableComponent.getPosition().set(physicsComponent.body.getPosition());
						b2DSteerableComponent.setOrientation(physicsComponent.body.getAngle());
					 }
				
			}
			else if(steerableComponent !=null){

						if(steerableComponent.behavior!=null) {
							System.out.println("Steer"+steerableComponent.steeringOutput);
							steerableComponent.behavior.calculateSteering(steerableComponent.steeringOutput);
							applySteering(steerableComponent,world.delta,e);
							Vector2 vector2=steerableComponent.getPosition();
							transform.setPosition(vector2.x, vector2.y);
							transform.setRotation(steerableComponent.getOrientation()* MathUtils.radiansToDegrees);
							checkTargetAchieved(e);
						}
						else {
								steerableComponent.getPosition().set(transform.getX(), transform.getY());
								steerableComponent.setOrientation(transform.getRotation()* MathUtils.degreesToRadians);

							 }
				}
	}

	public void checkTargetAchieved(Entity e){
		String Player =playerManager.getPlayer(e);

		if(Player==null)
			return;

		if(Player.equalsIgnoreCase(Enums.Player.PLAYER.value)&& groupManager.isInGroup(e, Enums.Group.PLAYER_BULLET.value)){
			SteerableComponent steerableComponent =steerMapper.get(e);
			if(steerableComponent.getLinearVelocity().len()<2) {
				e.edit().remove(SteerableComponent.class);
				e.edit().add(new MovementComponent(.075f));
			}
		}
	}

	protected static void wrapAround (Vector2 pos, float maxX, float maxY) {
		if (pos.x > maxX) pos.x = 0.0f;

		if (pos.x < 0) pos.x = maxX;

		if (pos.y < 0) pos.y = maxY;

		if (pos.y > maxY) pos.y = 0.0f;
	}
	
	private void applySteering(SteerableComponent steer, float time, Entity e){

		if(!steer.steeringOutput.linear.isZero()){
			steer.getPosition().mulAdd(steer.getLinearVelocity(), time);
			steer.getLinearVelocity().mulAdd(steer.steeringOutput.linear, time).limit(steer.getMaxLinearSpeed());
		}
		
		// Update orientation and angular velocity
		if (steer.isIndependentFacing()) {
			if(steer.steeringOutput.angular!=0){
				steer.setOrientation(steer.getOrientation() + (steer.getAngularVelocity() * time));
				steer.angularVelocity += steer.steeringOutput.angular * time;
			}
		} 
		else {
				// If we haven't got any velocity, then we can do nothing.
				if (!steer.getLinearVelocity().isZero(steer.getZeroLinearSpeedThreshold())) {
					float newOrientation = steer.vectorToAngle(steer.getLinearVelocity());
					steer.angularVelocity = (newOrientation - steer.getOrientation()) * time; // this is superfluous if independentFacing is always true
					steer.setOrientation(newOrientation);
				}
			}
	}
	
	protected void applySteering (B2DSteerableComponent steer, float deltaTime) {
		boolean anyAccelerations = false;

		// Update position and linear velocity.
		if (!steer.steeringOutput.linear.isZero()) {
			// this method internally scales the force by deltaTime
			steer.body.applyForceToCenter(steer.steeringOutput.linear, true);
			anyAccelerations = true;
		}

		// Update orientation and angular velocity
		if (steer.isIndependentFacing()) {
			if (steer.steeringOutput.angular != 0) {
				// this method internally scales the torque by deltaTime
				steer.body.applyTorque(steer.steeringOutput.angular, true);
				anyAccelerations = true;
			}
		} else {
			// If we haven't got any velocity, then we can do nothing.
			Vector2 linVel = steer.getLinearVelocity();
			if (!linVel.isZero(steer.getZeroLinearSpeedThreshold())) {
				float newOrientation = steer.vectorToAngle(linVel);
				steer.body.setAngularVelocity((newOrientation - steer.getAngularVelocity()) * deltaTime); // this is superfluous if independentFacing is always true
				steer.body.setTransform(steer.body.getPosition(), newOrientation);
			}
		}

		if (anyAccelerations) {
			// body.activate();

			// TODO:
			// Looks like truncating speeds here after applying forces doesn't work as expected.
			// We should likely cap speeds form inside an InternalTickCallback, see
			// http://www.bulletphysics.org/mediawiki-1.5.8/index.php/Simulation_Tick_Callbacks

			// Cap the linear speed
			Vector2 velocity = steer.body.getLinearVelocity();
			float currentSpeedSquare = velocity.len2();
			float maxLinearSpeed = steer.getMaxLinearSpeed();
			if (currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
				steer.body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float)Math.sqrt(currentSpeedSquare)));
			}

			// Cap the angular speed
			float maxAngVelocity = steer.getMaxAngularSpeed();
			if (steer.body.getAngularVelocity() > maxAngVelocity) {
				steer.body.setAngularVelocity(maxAngVelocity);
			}
		}
	}

	@Override
	protected boolean checkProcessing() {
		return !_GameManager.isPaused();
	}
}
