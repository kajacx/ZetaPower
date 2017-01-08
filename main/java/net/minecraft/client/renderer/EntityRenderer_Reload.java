package net.minecraft.client.renderer;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.hrkalk.zetapower.utils.MathUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ccc.CustomCameraControl;
import net.minecraft.client.renderer.ccc.IPlayerCamera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class EntityRenderer_Reload {

    public EntityRenderer thiz;

    public void orientCamera(float partialTicks) {
        Entity entity = thiz.mc.getRenderViewEntity();
        // CCC_EDIT

        if (thiz.mc.gameSettings.thirdPersonView <= 2) {
            //use old code for vanilla camera

            float f = entity.getEyeHeight();
            double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
            double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + f;
            double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;

            if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPlayerSleeping()) {
                f = (float) (f + 1.0D);
                GlStateManager.translate(0.0F, 0.3F, 0.0F);

                if (!thiz.mc.gameSettings.debugCamEnable) {
                    BlockPos blockpos = new BlockPos(entity);
                    IBlockState iblockstate = thiz.mc.theWorld.getBlockState(blockpos);
                    net.minecraftforge.client.ForgeHooksClient.orientBedCamera(thiz.mc.theWorld, blockpos, iblockstate, entity);

                    GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F, 0.0F, -1.0F, 0.0F);
                    GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0F, 0.0F, 0.0F);
                }
            } else if (thiz.mc.gameSettings.thirdPersonView > 0) {
                double d3 = thiz.thirdPersonDistancePrev + (4.0F - thiz.thirdPersonDistancePrev) * partialTicks;

                if (thiz.mc.gameSettings.debugCamEnable) {
                    GlStateManager.translate(0.0F, 0.0F, (float) (-d3));
                } else {
                    float f1 = entity.rotationYaw;
                    float f2 = entity.rotationPitch;

                    if (thiz.mc.gameSettings.thirdPersonView == 2) {
                        f2 += 180.0F;
                    }

                    double d4 = -MathHelper.sin(f1 * 0.017453292F) * MathHelper.cos(f2 * 0.017453292F) * d3;
                    double d5 = MathHelper.cos(f1 * 0.017453292F) * MathHelper.cos(f2 * 0.017453292F) * d3;
                    double d6 = (-MathHelper.sin(f2 * 0.017453292F)) * d3;

                    for (int i = 0; i < 8; ++i) {
                        float f3 = (i & 1) * 2 - 1;
                        float f4 = (i >> 1 & 1) * 2 - 1;
                        float f5 = (i >> 2 & 1) * 2 - 1;
                        f3 = f3 * 0.1F;
                        f4 = f4 * 0.1F;
                        f5 = f5 * 0.1F;
                        RayTraceResult raytraceresult = thiz.mc.theWorld.rayTraceBlocks(new Vec3d(d0 + f3, d1 + f4, d2 + f5), new Vec3d(d0 - d4 + f3 + f5, d1 - d6 + f4, d2 - d5 + f5));

                        if (raytraceresult != null) {
                            double d7 = raytraceresult.hitVec.distanceTo(new Vec3d(d0, d1, d2));

                            if (d7 < d3) {
                                d3 = d7;
                            }
                        }
                    }

                    if (thiz.mc.gameSettings.thirdPersonView == 2) {
                        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                    }

                    GlStateManager.rotate(entity.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(entity.rotationYaw - f1, 0.0F, 1.0F, 0.0F);
                    GlStateManager.translate(0.0F, 0.0F, (float) (-d3));
                    GlStateManager.rotate(f1 - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f2 - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
                }
            } else {
                GlStateManager.translate(0.0F, 0.0F, 0.05F);
            }

            if (!thiz.mc.gameSettings.debugCamEnable) {
                float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F;
                float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                float roll = 0.0F;
                if (entity instanceof EntityAnimal) {
                    EntityAnimal entityanimal = (EntityAnimal) entity;
                    yaw = entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0F;
                }
                IBlockState state = ActiveRenderInfo.getBlockStateAtEntityViewpoint(thiz.mc.theWorld, entity, partialTicks);
                net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup event = new net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup(thiz, entity, state, partialTicks, yaw, pitch, roll);
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
                GlStateManager.rotate(event.getRoll(), 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(event.getPitch(), 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(event.getYaw(), 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.translate(0.0F, -f, 0.0F);
            d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
            d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + f;
            d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
            thiz.cloudFog = thiz.mc.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
        } else {
            //use new code for CCC

            IPlayerCamera camera = CustomCameraControl.getCamera(thiz.mc.gameSettings);
            camera.update(entity, partialTicks);

            //camera position
            float cameraX = camera.getCameraPosition().x;
            float cameraY = camera.getCameraPosition().y;
            float cameraZ = camera.getCameraPosition().z;

            //entity position
            double entityX = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
            double entityY = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks;
            double entityZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;

            //look vectors
            Vector3f forward = MathUtils.vectorZNeg; //reference look forward vector
            Vector3f lookForw = camera.getLookVector(); //camera look forward vector new Vector3f(1, -1, 1);//
            //Vector3f lookForw = new Vector3f(1, 0, 0);
            Vector3f up = MathUtils.vectorY; //reference look up vector
            Vector3f lookUp = camera.getLookUpVector(); //camera look up vector new Vector3f(0, 1, 1); //
            //Vector3f lookUp = new Vector3f(0, 1, 0);

            //tmp vectors
            Vector3f tmpVector = new Vector3f();
            Vector3f tmpVector2 = new Vector3f();
            Matrix4f tmpMatrix = new Matrix4f();

            // -- Rotate by using MAD SCIENCE!

            //First, rotate up
            float angle1 = Vector3f.angle(lookForw, forward);
            //L.d("angle1: " + angle1);
            MathUtils.cross(lookForw, forward, tmpVector);
            tmpVector.normalise();
            //L.d("tmp1: " + tmpVector);

            //then, rotate up according to first rotation
            tmpMatrix.setIdentity();
            tmpMatrix.rotate(angle1, tmpVector);
            MathUtils.multiplyVec(tmpMatrix, lookUp, tmpVector2);
            tmpVector2.normalise();

            //after we have rotated up in tmp2, we need to rotate again, to move it to lookUp
            float angle2 = Vector3f.angle(tmpVector2, up);
            //L.d("angle2: " + angle2);
            if (Math.abs(angle2) < Math.PI - MathUtils.EPSILON) {
                MathUtils.cross(tmpVector2, up, tmpVector2);
                tmpVector2.normalise();
            } else {
                //full 180 degree rotation, use rotForward instead
                tmpVector2.set(lookForw);
            }

            //3rd transformation: align look up vector
            GlStateManager.rotate(angle2 * MathUtils.radToDegF, tmpVector2.x, tmpVector2.y, tmpVector2.z);//*/

            //2nd transformation: align look forward vector
            GlStateManager.rotate(angle1 * MathUtils.radToDegF, tmpVector.x, tmpVector.y, tmpVector.z);

            //1st transformation: simply move the camera to a correct spot
            GlStateManager.translate(entityX - cameraX, entityY - cameraY, entityZ - cameraZ);

            //GlStateManager.translate(0.0F, -entity.getEyeHeight(), 0.0F);

            thiz.cloudFog = thiz.mc.renderGlobal.hasCloudFog(entityX, entityY, entityZ, partialTicks);
        }
    }

}
