package com.hrkalk.zetapower.client.render.entities;

import java.util.Iterator;
import java.util.Set;

import com.hrkalk.zetapower.client.render.vessel.FakeRenderChunk;
import com.hrkalk.zetapower.dimension.ZetaDimensionHandler;
import com.hrkalk.zetapower.entities.RideableShip;
import com.hrkalk.zetapower.utils.L;
import com.hrkalk.zetapower.utils.MathUtils;
import com.hrkalk.zetapower.utils.Util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class RideableShipRenderer_Reload {

    public RideableShipRenderer thiz;

    private RideableShipModel model;
    private ResourceLocation texture = new ResourceLocation("zetapower", "textures/entities/rideable_ship_plate.png");

    {
        model = new RideableShipModel(thiz);
        if (model == null) {
            L.i("init");
        }
    }

    public ResourceLocation getEntityTexture(Entity arg1) {
        return texture;
    }

    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        //L.s("kappa");
        RideableShip ship = (RideableShip) entity;
        if (ship.iterSpace != null) {
            //L.s("new render");
            Iterator<BlockPos> iter = ship.iterSpace.iterator();
            World mallocWorld = DimensionManager.getWorld(ZetaDimensionHandler.mallocDimension.type.getId());

            //L.d("ff: " + iter.hasNext());
            BlockPos anchor = ship.iterSpace.getAnchorPoint();

            //L.d("Reprot");

            while (iter.hasNext()) {
                BlockPos pos = iter.next();
                TileEntity te = mallocWorld.getTileEntity(pos);
                //L.d("Pos: " + pos + " kappa: " + mallocWorld.getBlockState(pos) + " te: " + te);
                //IBlockState state = mallocWorld.getBlockState(pos);
                //state.getRenderType();
                //L.d();

                // --- TILE ENTITY ---

                if (te != null) {
                    TileEntitySpecialRenderer<TileEntity> renderer = TileEntityRendererDispatcher.instance.<TileEntity>getSpecialRenderer(te);
                    //L.d("Found tile entity, renderer: " + renderer);
                    if (renderer != null) {
                        // L.s("Found renderer");
                        int xOff = pos.getX() - anchor.getX();
                        int yOff = pos.getY() - anchor.getY();
                        int zOff = pos.getZ() - anchor.getZ();
                        //L.d("xoff: " + xOff + ", yoff: " + yOff + ", zoff: " + zOff);
                        //renderer.renderTileEntityAt(te, x + xOff - .5, y + yOff, z + zOff - .5, partialTicks, 0);

                        //TileEntityRendererDispatcher.instance.renderTileEntity(te, partialTicks, -1);
                        TileEntityRendererDispatcher.instance.renderTileEntityAt(te, x + xOff - .5, y + yOff, z + zOff - .5, partialTicks);
                    }
                }

                /*if (skipBlocks) {
                    continue;
                }*/

                // --- BLOCKS PREPARE ---

                /*IBlockState state = mallocWorld.getBlockState(pos);
                Block block = state.getBlock();
                
                BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                
                ChunkRenderDispatcher crDispatcher = (ChunkRenderDispatcher) Util.getField(renderGlobal, "renderDispatcher");
                ChunkRenderWorker crWorker = (ChunkRenderWorker) Util.getField(crDispatcher, "renderWorker");*/
                //crWorker.getRe



            }

            // RENDER BLOCKS 

            boolean skipBlocks = true;
            FakeRenderChunk fakeRenderer = null;
            RenderGlobal renderGlobal = null;

            if (!skipBlocks) {
                renderGlobal = Minecraft.getMinecraft().renderGlobal;
                //fakeRenderer = new FakeRenderChunk(mallocWorld, renderGlobal, 0);
                fakeRenderer = new FakeRenderChunk(entity.worldObj, renderGlobal, 0, ship.iterSpace);
                fakeRenderer.setOrigin(0, 64, 0);

                Set<RenderChunk> chunksToUpdate = Util.getField(renderGlobal, "chunksToUpdate");
                chunksToUpdate.add(fakeRenderer);

                //L.d("Chunks to update length: " + chunksToUpdate.size());

                //BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                /*ChunkRenderDispatcher crDispatcher = (ChunkRenderDispatcher) Util.getField(renderGlobal, "renderDispatcher");
                try {
                    fakeRenderer.rebuildChunk(0, 0, 0, iter, crDispatcher.getNextChunkUpdate());
                } catch (InterruptedException e) {
                    e.printStackTrace(System.out);
                }*/
            }


        } else {
            renderOld(entity, x, y, z, entityYaw, partialTicks);
        }

    }

    private void renderOld(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        EntityLivingBase player = Minecraft.getMinecraft().thePlayer;

        float rotationYaw;
        float rotationPitch;
        if (entity.getControllingPassenger() == player) {
            rotationYaw = player.rotationYaw;
            rotationPitch = player.rotationPitch;
        } else {
            rotationYaw = entity.rotationYaw;
            rotationPitch = entity.rotationPitch;
        }

        thiz.call_bindEntityTexture(entity);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, .5f, 0);

        GlStateManager.translate(x, y, z);

        float rotXY = rotationYaw * MathUtils.degToRadF;
        float rotY = rotationPitch * MathUtils.degToRadF;

        float rot = System.currentTimeMillis() % 10000000;
        rot = rot * .10f;

        GlStateManager.rotate(-rotationPitch, (float) -Math.cos(rotXY), 0, (float) -Math.sin(rotXY));
        //GlStateManager.rotate(rot, 1, 0, 0);

        GlStateManager.rotate(-rotationYaw, 0, 1, 0);

        GlStateManager.translate(0, -.5f, 0);
        model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }

}
