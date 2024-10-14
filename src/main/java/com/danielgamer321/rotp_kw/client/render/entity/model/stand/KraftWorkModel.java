package com.danielgamer321.rotp_kw.client.render.entity.model.stand;

import com.danielgamer321.rotp_kw.action.stand.KraftWorkEnergyAccumulation;
import com.danielgamer321.rotp_kw.entity.stand.stands.KraftWorkEntity;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.client.render.entity.model.stand.HumanoidStandModel;
import com.github.standobyte.jojo.client.render.entity.pose.*;
import com.github.standobyte.jojo.client.render.entity.pose.anim.PosedActionAnimation;
import com.github.standobyte.jojo.entity.stand.StandPose;

// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class KraftWorkModel extends HumanoidStandModel<KraftWorkEntity> {

	public KraftWorkModel() {
		super();

		addHumanoidBaseBoxes(null);
		texWidth = 128;
		texHeight = 128;

		head.texOffs(34, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.1F, false);
		head.texOffs(82, 40).addBox(-4.0F, -8.0F, -4.189F, 8.0F, 8.0F, 1.0F, 0.1F, false);
		head.texOffs(102, 0).addBox(1.47F, -8.0F, -4.0F, 1.0F, 8.0F, 8.0F, 0.1F, false);
		head.texOffs(102, 18).addBox(1.641F, -8.0F, -4.0F, 1.0F, 8.0F, 8.0F, 0.1F, false);
		head.texOffs(102, 36).addBox(-2.644F, -8.0F, -4.0F, 1.0F, 8.0F, 8.0F, 0.1F, false);
		head.texOffs(102, 54).addBox(-2.473F, -8.0F, -4.0F, 1.0F, 8.0F, 8.0F, 0.1F, false);
		head.texOffs(34, 18).addBox(-4.0F, -4.413F, -4.0F, 8.0F, 1.0F, 8.0F, 0.1F, false);
		head.texOffs(48, 40).addBox(-4.0F, -3.042F, -4.0F, 8.0F, 1.0F, 8.0F, 0.1F, false);
		head.texOffs(68, 29).addBox(-4.0F, -5.099F, -4.0F, 8.0F, 1.0F, 8.0F, 0.1F, false);
		head.texOffs(34, 29).addBox(-4.0F, -2.8701F, -4.0F, 8.0F, 1.0F, 8.0F, 0.1F, false);
		head.texOffs(68, 18).addBox(-4.0F, -4.585F, -4.0F, 8.0F, 1.0F, 8.0F, 0.1F, false);
		head.texOffs(68, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.05F, false);
		head.texOffs(59, 22).addBox(-3.0F, -0.397F, -4.1F, 6.0F, 1.0F, 2.0F, 0.0F, false);
		head.texOffs(60, 18).addBox(1.038F, -1.41F, -4.08F, 1.0F, 1.0F, 1.0F, 0.02F, false);
		head.texOffs(66, 18).addBox(-2.04F, -1.41F, -4.08F, 1.0F, 1.0F, 1.0F, 0.02F, false);
		head.texOffs(66, 29).addBox(-4.0797F, -3.985F, -4.62F, 1.0F, 1.0F, 1.0F, 0.02F, false);
		head.texOffs(60, 0).addBox(-4.0795F, -3.99F, -1.019F, 1.0F, 1.0F, 1.0F, 0.02F, false);
		head.texOffs(65, 0).addBox(-4.0797F, -2.96F, -2.0475F, 1.0F, 1.0F, 1.0F, 0.02F, false);
		head.texOffs(70, 0).addBox(-4.0797F, -1.932F, -3.077F, 1.0F, 1.0F, 1.0F, 0.02F, false);
		head.texOffs(60, 4).addBox(3.0815F, -3.99F, -1.019F, 1.0F, 1.0F, 1.0F, 0.02F, false);
		head.texOffs(65, 4).addBox(3.0815F, -2.96F, -2.0475F, 1.0F, 1.0F, 1.0F, 0.02F, false);
		head.texOffs(70, 4).addBox(3.0815F, -1.932F, -3.077F, 1.0F, 1.0F, 1.0F, 0.02F, false);
		head.texOffs(66, 33).addBox(-4.0797F, -3.985F, -2.551F, 1.0F, 1.0F, 1.0F, 0.02F, false);
		head.texOffs(60, 29).addBox(3.0799F, -3.985F, -4.62F, 1.0F, 1.0F, 1.0F, 0.02F, false);
		head.texOffs(94, 29).addBox(3.62F, -3.99F, -4.08F, 1.0F, 1.0F, 1.0F, 0.02F, false);
		head.texOffs(94, 33).addBox(-4.623F, -3.99F, -4.08F, 1.0F, 1.0F, 1.0F, 0.02F, false);
		head.texOffs(60, 33).addBox(3.0799F, -3.985F, -2.551F, 1.0F, 1.0F, 1.0F, 0.02F, false);
		head.texOffs(27, 4).addBox(3.95F, -3.535F, -0.045F, 2.0F, 1.0F, 1.0F, -0.05F, false);
		head.texOffs(35, 4).addBox(-5.95F, -3.535F, -0.045F, 2.0F, 1.0F, 1.0F, -0.05F, false);

		torso.texOffs(38, 64).addBox(-4.0F, 0.1F, -2.0F, 8.0F, 4.0F, 4.0F, 0.4F, false);
		torso.texOffs(62, 64).addBox(4.8F, 0.1F, -2.0F, 3.0F, 2.0F, 4.0F, 0.4F, false);
		torso.texOffs(24, 64).addBox(-7.8F, 0.1F, -2.0F, 3.0F, 2.0F, 4.0F, 0.4F, false);
		torso.texOffs(44, 60).addBox(-0.5F, 2.3F, -3.35F, 1.0F, 1.0F, 1.0F, -0.05F, false);
		torso.texOffs(36, 64).addBox(-4.35F, -0.25F, -3.35F, 1.0F, 1.0F, 1.0F, -0.05F, false);
		torso.texOffs(60, 64).addBox(3.35F, -0.25F, -3.35F, 1.0F, 1.0F, 1.0F, -0.05F, false);
		torso.texOffs(38, 74).addBox(-4.35F, 1.0F, 2.35F, 1.0F, 1.0F, 1.0F, -0.05F, false);
		torso.texOffs(56, 74).addBox(3.35F, 1.0F, 2.35F, 1.0F, 1.0F, 1.0F, -0.05F, false);
		torso.texOffs(50, 74).addBox(1.5F, 1.0F, 2.35F, 1.0F, 1.0F, 1.0F, -0.05F, false);
		torso.texOffs(44, 74).addBox(-2.5F, 1.0F, 2.35F, 1.0F, 1.0F, 1.0F, -0.05F, false);

		leftArm.texOffs(0, 110).addBox(1.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.005F, false);
		leftArm.texOffs(44, 109).addBox(-1.0F, 2.5F, 1.6F, 2.0F, 2.0F, 1.0F, 0.0F, false);
		leftArm.texOffs(45, 107).addBox(-0.5F, 3.0F, 2.1F, 1.0F, 1.0F, 1.0F, 0.01F, false);

		leftForeArm.texOffs(53, 117).addBox(1.25F, 0.5F, -1.0F, 1.0F, 3.0F, 2.0F, -0.15F, false);
		leftForeArm.texOffs(46, 117).addBox(-2.25F, 0.5F, -1.0F, 1.0F, 3.0F, 2.0F, -0.15F, false);
		leftForeArm.texOffs(42, 97).addBox(1.5F, 5.1F, -2.0F, 1.0F, 1.0F, 4.0F, -0.2F, true);

		rightArm.texOffs(32, 110).addBox(-2.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.005F, false);
		rightArm.texOffs(12, 109).addBox(-1.0F, 2.5F, 1.6F, 2.0F, 2.0F, 1.0F, 0.0F, false);
		rightArm.texOffs(45, 107).addBox(-0.5F, 3.0F, 2.1F, 1.0F, 1.0F, 1.0F, 0.01F, false);

		rightForeArm.texOffs(0, 118).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, -0.001F, false);
		rightForeArm.texOffs(14, 117).addBox(-2.25F, 0.5F, -1.0F, 1.0F, 3.0F, 2.0F, -0.15F, false);
		rightForeArm.texOffs(21, 117).addBox(1.25F, 0.5F, -1.0F, 1.0F, 3.0F, 2.0F, -0.15F, false);

		leftLeg.texOffs(94, 119).addBox(-0.9F, 4.5F, -2.5F, 2.0F, 2.0F, 1.0F, 0.0F, true);

		leftLowerLeg.texOffs(85, 117).addBox(1.25F, 0.5F, -1.0F, 1.0F, 3.0F, 2.0F, -0.15F, false);
		leftLowerLeg.texOffs(78, 117).addBox(-2.25F, 0.5F, -1.0F, 1.0F, 3.0F, 2.0F, -0.15F, false);

		rightLeg.texOffs(62, 119).addBox(-1.1F, 4.5F, -2.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);

		rightLowerLeg.texOffs(78, 117).addBox(-2.25F, 0.5F, -1.0F, 1.0F, 3.0F, 2.0F, -0.15F, false);
		rightLowerLeg.texOffs(64, 118).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, -0.001F, false);
	}

	@Override
	protected RotationAngle[][] initSummonPoseRotations() {
		return new RotationAngle[][] {
				new RotationAngle[] {
						RotationAngle.fromDegrees(head, 5F, -20F, 0),
						RotationAngle.fromDegrees(body, -7.5F, -20F, 0),
						RotationAngle.fromDegrees(upperPart, 0, 0, 0),
						RotationAngle.fromDegrees(leftArm, -10F, -5F, -87.5F),
						RotationAngle.fromDegrees(leftArmJoint, -55F, 0, 0),
						RotationAngle.fromDegrees(leftForeArm, -110F, -5F, 0),
						RotationAngle.fromDegrees(rightArm, 0, 5F, 87.5F),
						RotationAngle.fromDegrees(rightArmJoint, -30F, 0, 0),
						RotationAngle.fromDegrees(rightForeArm, -69F, 0, 0),
						RotationAngle.fromDegrees(leftLeg, -80F, 0, 0),
						RotationAngle.fromDegrees(leftLegJoint, -30F, 0, 0),
						RotationAngle.fromDegrees(leftLowerLeg, 120F, 0, 0),
						RotationAngle.fromDegrees(rightLeg, -90F, 10F, 0),
						RotationAngle.fromDegrees(rightLegJoint, -30F, 0, 0),
						RotationAngle.fromDegrees(rightLowerLeg, 120F, 0, 0)
				},
				new RotationAngle[] {
						RotationAngle.fromDegrees(head, 5F, 0, 0),
						RotationAngle.fromDegrees(body, 0, 10F, 0),
						RotationAngle.fromDegrees(upperPart, 0, 0, 0),
						RotationAngle.fromDegrees(leftArm, 0, 0, -10F),
						RotationAngle.fromDegrees(leftForeArm, -5F, 0, 7.5F),
						RotationAngle.fromDegrees(rightArm, -5F, 0, 20F),
						RotationAngle.fromDegrees(rightArmJoint, 0, 0, -20F),
						RotationAngle.fromDegrees(rightForeArm, 0, 0, -45F),
						RotationAngle.fromDegrees(leftLeg, -12.5F, 0, -1.5F),
						RotationAngle.fromDegrees(leftLowerLeg, 25F, 0, 0),
						RotationAngle.fromDegrees(rightLeg, -5F, 0, 0),
						RotationAngle.fromDegrees(rightLowerLeg, 10F, 0, 0)
				}
		};
	}

	@Override
	protected void initActionPoses() {
		ModelPose<KraftWorkEntity> punchPose1 = new ModelPose<>(new RotationAngle[] {
				RotationAngle.fromDegrees(head, 0F, 0F, 0F),
				RotationAngle.fromDegrees(body, 20F, 0F, 0F),
				RotationAngle.fromDegrees(upperPart, 0F, 0F, 0F),
				RotationAngle.fromDegrees(leftArm, -5F, 25F, -30F),
				RotationAngle.fromDegrees(leftArmJoint, -45F, 0F, 0F),
				RotationAngle.fromDegrees(leftForeArm, -87.5F, 0F, 0F),
				RotationAngle.fromDegrees(rightArm, -5F, -25F, 30F),
				RotationAngle.fromDegrees(rightArmJoint, -45F, 0F, 0F),
				RotationAngle.fromDegrees(rightForeArm, -87.5F, 0F, 0F),
				RotationAngle.fromDegrees(leftLeg, -45F, 0F, -10F),
				RotationAngle.fromDegrees(leftLegJoint, 30F, 0F, 0F),
				RotationAngle.fromDegrees(leftLowerLeg, 45F, 0F, 0F),
				RotationAngle.fromDegrees(rightLeg, -45F, 0F, 10F),
				RotationAngle.fromDegrees(rightLegJoint, 30F, 0F, 0F),
				RotationAngle.fromDegrees(rightLowerLeg, 45F, 0F, 0F)
		});
		ModelPose<KraftWorkEntity> punchPose2 = new ModelPose<>(new RotationAngle[] {
				RotationAngle.fromDegrees(head, 0F, 0F, 0F),
				RotationAngle.fromDegrees(body, 40F, 0F, 0F),
				RotationAngle.fromDegrees(upperPart, 0F, 20F, 0F),
				RotationAngle.fromDegrees(leftArm, -29.6834F, 25.0169F, -41.795F),
				RotationAngle.fromDegrees(leftArmJoint, -45F, 0F, 0F),
				RotationAngle.fromDegrees(leftForeArm, -95F, 0F, 0F),
				RotationAngle.fromDegrees(rightArm, -5F, 5F, 90F),
				RotationAngle.fromDegrees(rightArmJoint, -45F, 0F, 0F),
				RotationAngle.fromDegrees(rightForeArm, -125F, 0F, -90F),
				RotationAngle.fromDegrees(leftLeg, -60F, 0F, -10F),
				RotationAngle.fromDegrees(leftLegJoint, 37.5F, 0F, 0F),
				RotationAngle.fromDegrees(leftLowerLeg, 80F, 0F, 0F),
				RotationAngle.fromDegrees(rightLeg, 10F, 0F, 10F),
				RotationAngle.fromDegrees(rightLegJoint, 0F, 0F, 0F),
				RotationAngle.fromDegrees(rightLowerLeg, 27F, 0F, 0F)
		});
		ModelPose<KraftWorkEntity> punchPose3 = new ModelPose<>(new RotationAngle[] {
				RotationAngle.fromDegrees(head, 0F, 0F, 0F),
				RotationAngle.fromDegrees(body, 40F, 0F, 0F),
				RotationAngle.fromDegrees(upperPart, 0F, -20F, 0F),
				RotationAngle.fromDegrees(leftArm, -7.5F, 0F, -15F),
				RotationAngle.fromDegrees(leftArmJoint, -45F, 0F, 0F),
				RotationAngle.fromDegrees(leftForeArm, -95F, 0F, 0F),
				RotationAngle.fromDegrees(rightArm, -59.308F, 34.8829F, 81.833F),
				RotationAngle.fromDegrees(rightArmJoint, -20F, 0F, 0F),
				RotationAngle.fromDegrees(rightForeArm, -37.8852F, -9.0337F, -12.7323F),
				RotationAngle.fromDegrees(leftLeg, -60F, 0F, -10F),
				RotationAngle.fromDegrees(leftLegJoint, 37.5F, 0F, 0F),
				RotationAngle.fromDegrees(leftLowerLeg, 80F, 0F, 0F),
				RotationAngle.fromDegrees(rightLeg, 10F, 0F, 10F),
				RotationAngle.fromDegrees(rightLegJoint, 0F, 0F, 0F),
				RotationAngle.fromDegrees(rightLowerLeg, 27F, 0F, 0F)
		});
		actionAnim.put(StandPose.HEAVY_ATTACK_FINISHER, new PosedActionAnimation.Builder<KraftWorkEntity>()
				.addPose(StandEntityAction.Phase.WINDUP, new ModelPoseTransition<>(punchPose1, punchPose2))
				.addPose(StandEntityAction.Phase.PERFORM, new ModelPoseTransition<>(punchPose2, punchPose2))
				.addPose(StandEntityAction.Phase.RECOVERY, new ModelPoseTransitionMultiple.Builder<>(punchPose2)
						.addPose(0.000001F, punchPose3)
						.addPose(0.5F, punchPose3)
						.build(idlePose))
				.build(idlePose));

		RotationAngle[] touchPose1 = new RotationAngle[] {
				RotationAngle.fromDegrees(head, 5F, -2.5F, 0),
				RotationAngle.fromDegrees(leftArm, -90F, -5F, -87.5F),
				RotationAngle.fromDegrees(leftForeArm, -45F, -5F, 0)
		};
		RotationAngle[] touchPose2 = new RotationAngle[] {
				RotationAngle.fromDegrees(head, 5F, -2.5F, 0),
				RotationAngle.fromDegrees(leftArm, -90F, -5F, -87.5F),
				RotationAngle.fromDegrees(leftForeArm, -45F, -1.67F, 0)
		};

		RotationAngle[] touchPose3 = new RotationAngle[] {
				RotationAngle.fromDegrees(head, 5F, -2.5F, 0),
				RotationAngle.fromDegrees(leftArm, -90F, -5F, -87.5F),
				RotationAngle.fromDegrees(leftForeArm, -45F, 0, 0)
		};
		RotationAngle[] touchPose4 = new RotationAngle[] {
				RotationAngle.fromDegrees(head, 5F, -2.5F, 0),
				RotationAngle.fromDegrees(leftArm, -90F, -5F, -87.5F),
				RotationAngle.fromDegrees(leftForeArm, -45F, -2.5, 0)
		};

		IModelPose<KraftWorkEntity> touchStart = new ModelPoseSided<>(
				new ModelPose<KraftWorkEntity>(touchPose1),
				new ModelPose<KraftWorkEntity>(touchPose1));

		IModelPose<KraftWorkEntity> touchTurn = new ModelPoseSided<>(
				new ModelPose<KraftWorkEntity>(touchPose2),
				new ModelPose<KraftWorkEntity>(touchPose2));

		IModelPose<KraftWorkEntity> touchImpact = new ModelPoseSided<>(
				new ModelPose<KraftWorkEntity>(touchPose3),
				new ModelPose<KraftWorkEntity>(touchPose3)).setEasing(x -> x * x * x);

		IModelPose<KraftWorkEntity> touchTurnBack = new ModelPoseSided<>(
				new ModelPose<KraftWorkEntity>(touchPose4),
				new ModelPose<KraftWorkEntity>(touchPose4)).setEasing(x -> x * x * x);

		IModelPose<KraftWorkEntity> touchEnd = new ModelPoseSided<>(
				new ModelPose<KraftWorkEntity>(touchPose1),
				new ModelPose<KraftWorkEntity>(touchPose1));

		actionAnim.putIfAbsent(KraftWorkEnergyAccumulation.GIVE_ENERGY_POSE,
				new PosedActionAnimation.Builder<KraftWorkEntity>()

						.addPose(StandEntityAction.Phase.WINDUP, new ModelPoseTransitionMultiple.Builder<KraftWorkEntity>(touchStart)
								.addPose(0.5F, touchTurn)
								.addPose(0.75F, touchImpact)
								.build(touchImpact))

						.addPose(StandEntityAction.Phase.PERFORM, new ModelPoseTransitionMultiple.Builder<KraftWorkEntity>(touchImpact)
								.addPose(0.25F, touchImpact)
								.addPose(0.5F, touchTurnBack)
								.build(touchEnd))

						.addPose(StandEntityAction.Phase.RECOVERY, new ModelPoseTransitionMultiple.Builder<KraftWorkEntity>(touchEnd)
								.addPose(0.75F, touchEnd)
								.build(idlePose))

						.build(idlePose));

		super.initActionPoses();
	}

	@Override
	protected ModelPose<KraftWorkEntity> initIdlePose() {
		return new ModelPose<>(new RotationAngle[] {
				RotationAngle.fromDegrees(body, 2.5F, 0.0F, 0.0F),
				RotationAngle.fromDegrees(upperPart, 0.0F, 0.0F, 0.0F),
				RotationAngle.fromDegrees(torso, 0.0F, 0.0F, 0.0F),
				RotationAngle.fromDegrees(leftArm, 0.0F, 1F, -5F),
				RotationAngle.fromDegrees(leftForeArm, -7.5F, 0.0F, 0.0F),
				RotationAngle.fromDegrees(rightArm, 0.0F, -1F, 5F),
				RotationAngle.fromDegrees(rightForeArm, -7.5F, 0.0F, 0.0F),
				RotationAngle.fromDegrees(leftLeg, -5F, 0.0F, -2.5F),
				RotationAngle.fromDegrees(leftLowerLeg, 10F, 0.0F, 2.5F),
				RotationAngle.fromDegrees(rightLeg, -10F, 0.0F, 2.5F),
				RotationAngle.fromDegrees(rightLowerLeg, 20F, 0.0F, -2.5F)
		});
	}

	@Override
	protected ModelPose<KraftWorkEntity> initIdlePose2Loop() {
		return new ModelPose<>(new RotationAngle[] {
				RotationAngle.fromDegrees(leftArm, 0.0F, 1F, -4.5F),
				RotationAngle.fromDegrees(leftForeArm, -7F, 0.0F, 0.0F),
				RotationAngle.fromDegrees(rightArm, 0.0F, -1F, 4.5F),
				RotationAngle.fromDegrees(rightForeArm, -7.5F, 0.0F, 0.0F)
		});
	}
}