package com.hrkalk.zetapower.client.render.vessel;

import java.util.HashSet;
import java.util.Iterator;
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
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FakeRenderChunk_Old extends RenderChunk {
    /*private World world;
    private final RenderGlobal renderGlobal;
    public static int renderChunksUpdated;
    public CompiledChunk compiledChunk = CompiledChunk.DUMMY;
    private final ReentrantLock lockCompileTask = new ReentrantLock();
    private final ReentrantLock lockCompiledChunk = new ReentrantLock();
    private ChunkCompileTaskGenerator compileTask;
    private final Set<TileEntity> setTileEntities = Sets.<TileEntity>newHashSet();
    private final int index;
    private final FloatBuffer modelviewMatrix = GLAllocation.createDirectFloatBuffer(16);
    private final VertexBuffer[] vertexBuffers = new VertexBuffer[BlockRenderLayer.values().length];
    public AxisAlignedBB boundingBox;
    private int frameIndex = -1;
    private boolean needsUpdate = true;
    private BlockPos.MutableBlockPos position = new BlockPos.MutableBlockPos(-1, -1, -1);
    private BlockPos.MutableBlockPos[] mapEnumFacing = new BlockPos.MutableBlockPos[6];
    private boolean needsUpdateCustom;
    private ChunkCache region;*/

    private Iterable<BlockPos> iter;

    public FakeRenderChunk_Old(World world, RenderGlobal renderGlobal, int index, Iterable<BlockPos> iter) {
        super(world, renderGlobal, index);

        this.iter = iter;
    }



    @Override
    public void rebuildChunk(float x, float y, float z, ChunkCompileTaskGenerator generator) {
        //L.d("Rebuild chunk called on fake renderer");
        //L.d(String.format("x: %f, y: %f, z: %f%n", x, y, z));
        //GlStateManager.pushMatrix();
        //GlStateManager.translate(0, -16, 0);

        L.d("kappa");
        super.rebuildChunk(x, y, z, generator);
        //rebuildChunk1(x, y, z, generator);

        //GlStateManager.popMatrix();
    }

    public void rebuildChunk1(float x, float y, float z, ChunkCompileTaskGenerator generator) {
        CompiledChunk compiledchunk = new CompiledChunk();
        int i = 1;
        L.d("Getting position");
        BlockPos blockpos = Util.getField(this, "position");
        L.d("Position get");
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

        ChunkCache region = Util.getField(this, "region");

        if (!region.extendedLevelsInChunkCache()) {
            ++renderChunksUpdated;
            boolean[] aboolean = new boolean[BlockRenderLayer.values().length];
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();

            for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(blockpos, blockpos1)) {
                L.d("REndering in: " + blockpos$mutableblockpos);

                IBlockState iblockstate = region.getBlockState(blockpos$mutableblockpos);
                Block block = iblockstate.getBlock();

                if (iblockstate.isOpaqueCube()) {
                    lvt_9_1_.setOpaqueCube(blockpos$mutableblockpos);
                }

                if (block.hasTileEntity(iblockstate)) {
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
                }

                for (BlockRenderLayer blockrenderlayer1 : BlockRenderLayer.values()) {
                    if (!block.canRenderInLayer(iblockstate, blockrenderlayer1))
                        continue;
                    net.minecraftforge.client.ForgeHooksClient.setRenderLayer(blockrenderlayer1);
                    int j = blockrenderlayer1.ordinal();

                    if (block.getDefaultState().getRenderType() != EnumBlockRenderType.INVISIBLE) {
                        net.minecraft.client.renderer.VertexBuffer vertexbuffer = generator.getRegionRenderCacheBuilder().getWorldRendererByLayerId(j);

                        if (!compiledchunk.isLayerStarted(blockrenderlayer1)) {
                            compiledchunk.setLayerStarted(blockrenderlayer1);
                            //this.preRenderBlocks(vertexbuffer, blockpos);
                            Util.callMethod1(this, "preRenderBlocks", vertexbuffer, blockpos);
                        }

                        aboolean[j] |= blockrendererdispatcher.renderBlock(iblockstate, blockpos$mutableblockpos, region, vertexbuffer);
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
                    //this.postRenderBlocks(blockrenderlayer, x, y, z, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer), compiledchunk);
                    Util.callMethod1(this, "postRenderBlocks", blockrenderlayer, x, y, z, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer), compiledchunk);
                }
            }
        }

        compiledchunk.setVisibility(lvt_9_1_.computeVisibility());

        ReentrantLock lockCompileTask = Util.getField(this, "lockCompileTask");
        Set<TileEntity> setTileEntities = Util.getField(this, "setTileEntities");
        RenderGlobal renderGlobal = Util.getField(this, "renderGlobal");

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

    public void rebuildChunk0(float x, float y, float z, ChunkCompileTaskGenerator generator) {
        CompiledChunk compiledchunk = new CompiledChunk();
        int i = 1;
        //BlockPos blockpos = this.position;
        //BlockPos blockpos1 = blockpos.add(15, 15, 15);
        /*generator.getLock().lock();
        
        try {
            if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
                return;
            }
        
            generator.setCompiledChunk(compiledchunk);
        } finally {
            generator.getLock().unlock();
        }*/

        Iterator<BlockPos> iter = this.iter.iterator();

        VisGraph lvt_9_1_ = new VisGraph();
        HashSet lvt_10_1_ = Sets.newHashSet();

        ChunkCache region = Util.getField(this, "region");

        if (!region.extendedLevelsInChunkCache()) {
            ++renderChunksUpdated;
            boolean[] aboolean = new boolean[BlockRenderLayer.values().length];
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();

            while (iter.hasNext()) {
                BlockPos blockPos = iter.next();

                IBlockState iblockstate = region.getBlockState(blockPos);
                Block block = iblockstate.getBlock();

                if (iblockstate.isOpaqueCube()) {
                    lvt_9_1_.setOpaqueCube(blockPos);
                }

                if (block.hasTileEntity(iblockstate)) {
                    TileEntity tileentity = region.func_190300_a(blockPos, Chunk.EnumCreateEntityType.CHECK);

                    if (tileentity != null) {
                        TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = TileEntityRendererDispatcher.instance.<TileEntity>getSpecialRenderer(tileentity);

                        if (tileentityspecialrenderer != null) {
                            compiledchunk.addTileEntity(tileentity);

                            if (tileentityspecialrenderer.isGlobalRenderer(tileentity)) {
                                lvt_10_1_.add(tileentity);
                            }
                        }
                    }
                }

                for (BlockRenderLayer blockrenderlayer1 : BlockRenderLayer.values()) {
                    if (!block.canRenderInLayer(iblockstate, blockrenderlayer1))
                        continue;
                    net.minecraftforge.client.ForgeHooksClient.setRenderLayer(blockrenderlayer1);
                    int j = blockrenderlayer1.ordinal();

                    if (block.getDefaultState().getRenderType() != EnumBlockRenderType.INVISIBLE) {
                        net.minecraft.client.renderer.VertexBuffer vertexbuffer = generator.getRegionRenderCacheBuilder().getWorldRendererByLayerId(j);

                        if (!compiledchunk.isLayerStarted(blockrenderlayer1)) {
                            compiledchunk.setLayerStarted(blockrenderlayer1);
                            //this.preRenderBlocks(vertexbuffer, blockPos);
                            Util.callMethod1(this, "preRenderBlocks", vertexbuffer, blockPos);
                        }

                        aboolean[j] |= blockrendererdispatcher.renderBlock(iblockstate, blockPos, region, vertexbuffer);
                    }
                }
                net.minecraftforge.client.ForgeHooksClient.setRenderLayer(null);
            }

            for (BlockRenderLayer blockrenderlayer : BlockRenderLayer.values()) {
                if (aboolean[blockrenderlayer.ordinal()]) {
                    Util.callMethod1(compiledchunk, "setLayerUsed", blockrenderlayer);
                    //compiledchunk.setLayerUsed(blockrenderlayer);
                }

                if (compiledchunk.isLayerStarted(blockrenderlayer)) {
                    //this.postRenderBlocks(blockrenderlayer, x, y, z, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer), compiledchunk);
                    Util.callMethod1(this, "postRenderBlocks", blockrenderlayer, x, y, z, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer), compiledchunk);
                }
            }
        }

        compiledchunk.setVisibility(lvt_9_1_.computeVisibility());

        //ReentrantLock lockCompileTask = Util.getField(this, "lockCompileTask");
        Set<TileEntity> setTileEntities = Util.getField(this, "setTileEntities");
        RenderGlobal renderGlobal = Util.getField(this, "renderGlobal");

        //lockCompileTask.lock();

        try {
            Set<TileEntity> set = Sets.newHashSet(lvt_10_1_);
            Set<TileEntity> set1 = Sets.newHashSet(setTileEntities);
            set.removeAll(setTileEntities);
            set1.removeAll(lvt_10_1_);
            setTileEntities.clear();
            setTileEntities.addAll(lvt_10_1_);
            renderGlobal.updateTileEntities(set1, set);
        } finally {
            //lockCompileTask.unlock();
        }
    }


}