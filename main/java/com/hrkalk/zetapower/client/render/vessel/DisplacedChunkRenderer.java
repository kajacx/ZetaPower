package com.hrkalk.zetapower.client.render.vessel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.hrkalk.zetapower.dimension.ZetaDimensionHandler;
import com.hrkalk.zetapower.utils.L;
import com.hrkalk.zetapower.vessel.IterableSpace;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DisplacedChunkRenderer {

    private IterableSpace iterSpace;

    private int glRenderList = 0;

    public boolean isInFrustum = false;

    /** Should this renderer skip this render pass */
    public boolean[] skipRenderPass = new boolean[2];

    /** Boolean for whether this renderer needs to be updated or not */
    public boolean needsUpdate;
    public boolean isRemoved;

    /** Axis aligned bounding box */
    public AxisAlignedBB rendererBoundingBox;

    private boolean isInitialized = false;

    /** All the tile entities that have special rendering code for this chunk */
    private List<TileEntity> tileEntityRenderers = new ArrayList<TileEntity>();
    public List<TileEntity> tileEntities;

    /** Bytes sent to the GPU */
    private int bytesDrawn;

    public DisplacedChunkRenderer(IterableSpace iterSpace) {
        this.iterSpace = iterSpace;
        needsUpdate = true;

        tileEntities = new ArrayList<TileEntity>();
    }

    private void tryEndDrawing() {
        try {
            Tessellator.getInstance().draw();
            L.i("Drawing stopped");
        } catch (IllegalStateException ise) {
            L.w("Not drawing");
            ise.printStackTrace(System.out);
        }
    }

    public void render(float partialticks) {
        if (isRemoved) {
            if (glRenderList != 0) {
                GLAllocation.deleteDisplayLists(glRenderList);
                glRenderList = 0;
            }
            return;
        }

        if (needsUpdate) {
            try {
                updateRender();
            } catch (Exception e) {
                L.e("A mobile chunk render error has occured", e);
                tryEndDrawing();
            }
        }

        if (glRenderList != 0) {
            for (int pass = 0; pass < 2; ++pass) {
                GL11.glCallList(glRenderList + pass);

                RenderHelper.enableStandardItemLighting();
                Iterator<TileEntity> it = tileEntityRenderers.iterator();
                while (it.hasNext()) {
                    TileEntity tile = it.next();
                    try {
                        if (tile.shouldRenderInPass(pass)) {
                            renderTileEntity(tile, partialticks);
                        }
                    } catch (Exception e) {
                        it.remove();
                        L.e("A tile entity render error has occured", e);
                        tryEndDrawing();
                    }
                }
            }
        }
    }

    /**
     * Render this TileEntity at its current position from the player
     */
    public void renderTileEntity(TileEntity tileentity, float partialticks) {
        //int i = chunk.getLightBrightnessForSkyBlocks(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord, 0);
        //int j = i % 65536;
        //int k = i / 65536;
        //OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        BlockPos pos = tileentity.getPos();
        TileEntityRendererDispatcher.instance.renderTileEntityAt(tileentity, pos.getX(), pos.getY(), pos.getZ(), partialticks);
    }

    private void updateRender() {
        if (glRenderList == 0) {
            glRenderList = GLAllocation.generateDisplayLists(2);
        }

        for (int i = 0; i < 2; ++i) {
            skipRenderPass[i] = true;
        }

        //Chunk.isLit = false;
        HashSet<TileEntity> hashset0 = new HashSet<TileEntity>();
        hashset0.addAll(tileEntityRenderers);
        tileEntityRenderers.clear();

        //RenderBlocks renderblocks = new RenderBlocks(chunk);
        BlockModelRenderer renderblocks = new BlockModelRenderer(null);
        bytesDrawn = 0;

        World mallocWorld = ZetaDimensionHandler.getMallocWorld();

        for (int pass = 0; pass < 2; ++pass) {
            boolean flag = false;
            boolean flag1 = false;
            boolean glliststarted = false;

            //for (int y = chunk.minY(); y < chunk.maxY(); ++y) {
            //    for (int z = chunk.minZ(); z < chunk.maxZ(); ++z) {
            //        for (int x = chunk.minX(); x < chunk.maxX(); ++x) {

            for (BlockPos pos : iterSpace) {
                IBlockState blockState = pos == null ? null : mallocWorld.getBlockState(pos);
                if (blockState != null && blockState.getMaterial() != Material.AIR) {
                    if (!glliststarted) {
                        glliststarted = true;
                        GL11.glNewList(glRenderList + pass, GL11.GL_COMPILE);
                        GL11.glPushMatrix();
                        float f = 1.000001F;
                        GL11.glTranslatef(-8.0F, -8.0F, -8.0F);
                        GL11.glScalef(f, f, f);
                        GL11.glTranslatef(8.0F, 8.0F, 8.0F);
                        //Tessellator.getInstance().startDrawingQuads();
                    }

                    /*if (pass == 0 && blockState.hasTileEntity(chunk.getBlockMetadata(x, y, z))) {
                        TileEntity tileentity = chunk.getTileEntity(x, y, z);
                    
                        if (TileEntityRendererDispatcher.instance.hasSpecialRenderer(tileentity)) {
                            tileEntityRenderers.add(tileentity);
                        }
                    }*/

                    /*int blockpass = blockState.getRenderType();
                    if (blockpass > pass) {
                        flag = true;
                    }
                    if (!block.canRenderInPass(pass)) {
                        continue;
                    }*/
                    //flag1 |= renderblocks.renderBlockByRenderType(block, x, y, z);
                }
            }
            //        }
            //    }
            //}

            if (glliststarted) {
                /*bytesDrawn +=*/ Tessellator.getInstance().draw();
                GL11.glPopMatrix();
                GL11.glEndList();
                //Tessellator.getInstance().setTranslation(0D, 0D, 0D);
            } else {
                flag1 = false;
            }

            if (flag1) {
                skipRenderPass[pass] = false;
            }

            if (!flag) {
                break;
            }
        }

        HashSet<TileEntity> hashset1 = new HashSet<TileEntity>();
        hashset1.addAll(tileEntityRenderers);
        hashset1.removeAll(hashset0);
        tileEntities.addAll(hashset1);
        hashset0.removeAll(tileEntityRenderers);
        tileEntities.removeAll(hashset0);
        isInitialized = true;

        needsUpdate = false;
    }

    public void markDirty() {
        needsUpdate = true;
    }

    public void markRemoved() {
        isRemoved = true;

        try {
            if (glRenderList != 0) {
                L.d("Deleting mobile chunk display list " + glRenderList);
                GLAllocation.deleteDisplayLists(glRenderList);
                glRenderList = 0;
            }
        } catch (Exception e) {
            L.e("Failed to destroy mobile chunk display list", e);
        }
    }
}
