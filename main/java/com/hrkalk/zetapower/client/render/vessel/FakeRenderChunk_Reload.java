package com.hrkalk.zetapower.client.render.vessel;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.collect.Sets;
import com.hrkalk.zetapower.utils.L;
import com.hrkalk.zetapower.utils.Util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.ChunkCache;

public class FakeRenderChunk_Reload {

    public FakeRenderChunk thiz;

    public void rebuildChunk(float x, float y, float z, ChunkCompileTaskGenerator generator) {
        //Thanks for using the Zeta Power Reloadable class generator.
        //L.d("rebuild chunk called");
        //GlStateManager.matrixMode(5888);
        //GlStateManager.pushMatrix();
        //GlStateManager.translate(0, 16, 0);
        //
        //thiz.super_rebuildChunk(x, y, z, generator);
        //GlStateManager.popMatrix();

        //GL11.glPushMatrix();
        //GL11.glTranslatef(0, 16, 0);
        rebuildChunk1(x, y, z, generator);
        //GL11.glPopMatrix();
    }

    public void rebuildChunk1(float x, float y, float z, ChunkCompileTaskGenerator generator) {
        //GL11.call

        CompiledChunk compiledchunk = new CompiledChunk();
        int i = 1;
        //L.d("Getting position");
        BlockPos blockpos = Util.getField(thiz, "position");
        //L.d("Position get");
        BlockPos blockpos1 = blockpos.add(15, 15, 15);
        generator.getLock().lock();

        try {
            if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
                return;
            }

            generator.setCompiledChunk(compiledchunk);
        } finally {
            generator.getLock().unlock();
        }

        VisGraph lvt_9_1_ = new VisGraph();
        HashSet lvt_10_1_ = Sets.newHashSet();

        ChunkCache region = Util.getField(thiz, "region");

        if (!region.extendedLevelsInChunkCache()) {
            ++thiz.renderChunksUpdated;
            boolean[] aboolean = new boolean[BlockRenderLayer.values().length];
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            BlockPos.MutableBlockPos renderPos = new MutableBlockPos();
            BlockPos.MutableBlockPos renderPos0 = new MutableBlockPos(blockpos);
            //renderPos0.add(0, 16, 0);

            for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(blockpos, blockpos1)) {
                renderPos.setPos(blockpos$mutableblockpos);
                //renderPos.add(0, 16, 0);
                //L.d("REndering in: " + blockpos$mutableblockpos);


                IBlockState iblockstate = region.getBlockState(blockpos$mutableblockpos);
                iblockstate = Blocks.IRON_ORE.getDefaultState();
                Block block = iblockstate.getBlock();

                if (block == Blocks.GOLD_ORE) {
                    L.d("Gold confirmed");
                }
                //L.s("block state: " + iblockstate);

                if (iblockstate.isOpaqueCube()) {
                    lvt_9_1_.setOpaqueCube(renderPos);
                }

                /*if (block.hasTileEntity(iblockstate)) {
                    TileEntity tileentity = region.func_190300_a(blockpos$mutableblockpos, Chunk.EnumCreateEntityType.CHECK);
                
                    if (tileentity != null) {
                        TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = TileEntityRendererDispatcher.instance.<TileEntity>getSpecialRenderer(tileentity);
                
                        if (tileentityspecialrenderer != null) {
                            compiledchunk.addTileEntity(tileentity);
                
                            if (tileentityspecialrenderer.isGlobalRenderer(tileentity)) {
                                lvt_10_1_.add(tileentity);
                            }
                        }
                    }
                }*/

                for (BlockRenderLayer blockrenderlayer1 : BlockRenderLayer.values()) {
                    if (!block.canRenderInLayer(iblockstate, blockrenderlayer1))
                        continue;
                    net.minecraftforge.client.ForgeHooksClient.setRenderLayer(blockrenderlayer1);
                    int j = blockrenderlayer1.ordinal();

                    if (block.getDefaultState().getRenderType() != EnumBlockRenderType.INVISIBLE) {
                        net.minecraft.client.renderer.VertexBuffer vertexbuffer = generator.getRegionRenderCacheBuilder().getWorldRendererByLayerId(j);

                        vertexbuffer.setTranslation(0, 0, 0);

                        if (!compiledchunk.isLayerStarted(blockrenderlayer1)) {
                            compiledchunk.setLayerStarted(blockrenderlayer1);
                            //thiz.preRenderBlocks(vertexbuffer, blockpos);
                            Util.callMethod1(thiz, "preRenderBlocks", vertexbuffer, renderPos0);
                        }
                        vertexbuffer.pos(0, 80, 0);

                        if (renderPos.getY() > 72) {
                            //L.i("Starting debugging");
                        }

                        boolean success = blockrendererdispatcher.renderBlock(iblockstate, renderPos, region, vertexbuffer);
                        aboolean[j] |= success;
                        if (success) {
                            // L.d("success");
                        }
                    }
                }
                net.minecraftforge.client.ForgeHooksClient.setRenderLayer(null);
            }

            for (BlockRenderLayer blockrenderlayer : BlockRenderLayer.values()) {
                if (aboolean[blockrenderlayer.ordinal()]) {
                    //compiledchunk.setLayerUsed(blockrenderlayer);
                    Util.callMethod1(compiledchunk, "setLayerUsed", blockrenderlayer);
                }

                if (compiledchunk.isLayerStarted(blockrenderlayer)) {
                    //thiz.postRenderBlocks(blockrenderlayer, x, y, z, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer), compiledchunk);
                    Util.callMethod1(thiz, "postRenderBlocks", blockrenderlayer, x, y, z, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer), compiledchunk);
                }
            }
        }

        compiledchunk.setVisibility(lvt_9_1_.computeVisibility());

        ReentrantLock lockCompileTask = Util.getField(thiz, "lockCompileTask");
        Set<TileEntity> setTileEntities = Util.getField(thiz, "setTileEntities");
        RenderGlobal renderGlobal = Util.getField(thiz, "renderGlobal");

        lockCompileTask.lock();

        try {
            Set<TileEntity> set = Sets.newHashSet(lvt_10_1_);
            Set<TileEntity> set1 = Sets.newHashSet(setTileEntities);
            set.removeAll(setTileEntities);
            set1.removeAll(lvt_10_1_);
            setTileEntities.clear();
            setTileEntities.addAll(lvt_10_1_);
            renderGlobal.updateTileEntities(set1, set);
        } finally {
            lockCompileTask.unlock();
        }
    }

}
