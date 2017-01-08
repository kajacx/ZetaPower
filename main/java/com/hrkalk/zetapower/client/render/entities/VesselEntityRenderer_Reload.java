package com.hrkalk.zetapower.client.render.entities;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import com.hrkalk.zetapower.client.render.vessel.ClusterAccess;
import com.hrkalk.zetapower.client.render.vessel.FakeRenderChunk;
import com.hrkalk.zetapower.client.render.vessel.ScaledRotator;
import com.hrkalk.zetapower.dimension.ZetaDimensionHandler;
import com.hrkalk.zetapower.entities.vessel.VesselEntity;
import com.hrkalk.zetapower.utils.L;
import com.hrkalk.zetapower.utils.MathUtils;
import com.hrkalk.zetapower.utils.Util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class VesselEntityRenderer_Reload {

    public VesselEntityRenderer thiz;

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

    private VertexBuffer solidVertexBuffer = new VertexBuffer(2097152);
    private net.minecraft.client.renderer.vertex.VertexBuffer vertexBuffer = new net.minecraft.client.renderer.vertex.VertexBuffer(DefaultVertexFormats.BLOCK);
    private int glRenderList = 0;

    double lastX;

    private HashMap<Entity, Integer> glListMap = new HashMap<>();

    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        //L.s("kappa");
        VesselEntity ship = (VesselEntity) entity;
        //L.d("Cluster from: " + ship.cluster.getFrom());
        //L.d("Cluster to: " + ship.cluster.getTo());
        //L.d("Cluster anchor: " + ship.cluster.getAnchor());
        if (ship.cluster != null) {
            //L.s("new render");
            World mallocWorld = DimensionManager.getWorld(ZetaDimensionHandler.mallocDimension.type.getId());

            //L.d("ff: " + iter.hasNext());
            Vec3d anchor = ship.cluster.getAnchor();

            //L.d("Reprot");

            for (BlockPos pos : ship.cluster) {
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
                        double xOff = pos.getX() - anchor.xCoord;
                        double yOff = pos.getY() - anchor.yCoord;
                        double zOff = pos.getZ() - anchor.zCoord;
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

            boolean skipBlocks = false;
            FakeRenderChunk fakeRenderer = null;
            RenderGlobal renderGlobal = null;

            if (!skipBlocks) {
                renderGlobal = Minecraft.getMinecraft().renderGlobal;
                //fakeRenderer = new FakeRenderChunk(mallocWorld, renderGlobal, 0);
                //fakeRenderer = new FakeRenderChunk(entity.worldObj, renderGlobal, 0, ship.iterSpace);
                //fakeRenderer.setOrigin(0, 64, 0);

                //Set<RenderChunk> chunksToUpdate = Util.getField(renderGlobal, "chunksToUpdate");
                //chunksToUpdate.add(fakeRenderer);

                //L.d("Chunks to update length: " + chunksToUpdate.size());

                BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                ChunkRenderDispatcher crDispatcher = (ChunkRenderDispatcher) Util.getField(renderGlobal, "renderDispatcher");
                WorldVertexBufferUploader uploader = Util.getField(crDispatcher, "worldVertexUploader");
                //ChunkRenderContainer container = Util.getField(renderGlobal, "renderContainer");
                //List<RenderChunk> renderChunks = Util.getField(container, "renderChunks");

                /*for (RenderChunk chunk : renderChunks) {
                    //if (chunk.getPosition().equals(new BlockPos(0, 4, 0))) {
                    //    L.d("Bingo");
                    //}
                    L.d("Position: " + chunk.getPosition());
                }*/

                //state = ModBlocks.testTeleporter.getDefaultState();
                final boolean logEverything = false;
                Entity player = Minecraft.getMinecraft().thePlayer;
                Vec3d playerPos = player.getPositionVector();
                //Chunk chunk = Minecraft.getMinecraft().theWorld.getChunkFromBlockCoords(pos);
                //Biome b = chunk.getBiome(pos, Minecraft.getMinecraft().theWorld.getBiomeProvider());
                //L.d("Biome id: " + Biome.getIdForBiome(b));

                //L.d("Same x: " + (playerPos.xCoord == lastX));
                //lastX = playerPos.xCoord;

                World world = player.worldObj;
                //Block test = world.getBlockState(new BlockPos(0, 67, 0)).getBlock();
                //state = world.getBlockState(new BlockPos(0, 67, 0));
                //L.d("Block: " + test);

                IBlockAccess myBlockAccess = new ClusterAccess(ship.cluster);

                //L.d("Use vbo: " + OpenGlHelper.useVbo());
                //player.getEntityWorld().setWorldTime(0);
                int glRenderList = 0;

                try {
                    if (!glListMap.containsKey(entity) || glListMap.get(entity) != 1) {

                        solidVertexBuffer.begin(7, DefaultVertexFormats.BLOCK);
                        boolean success = false;
                        solidVertexBuffer.setTranslation(-anchor.xCoord, -anchor.yCoord, -anchor.zCoord);
                        //solidVertexBuffer.setTranslation((-pos.getX()), (-pos.getY()), (-pos.getZ()));
                        for (BlockPos b : ship.cluster) {
                            IBlockState state2 = mallocWorld.getBlockState(b);
                            success |= dispatcher.renderBlock(state2, b, myBlockAccess, solidVertexBuffer);
                        }
                        solidVertexBuffer.finishDrawing();
                        solidVertexBuffer.setTranslation(0, 0, 0);

                        //L.d("vertex count: " + solidVertexBuffer.getVertexCount()); //24
                        //L.d("is main thread: " + Minecraft.getMinecraft().isCallingFromMinecraftThread()); //true

                        //Tessellator.getInstance().draw();
                        glRenderList = GLAllocation.generateDisplayLists(1);
                        GlStateManager.glNewList(glRenderList, GL11.GL_COMPILE);

                        uploader.draw(solidVertexBuffer);

                        GlStateManager.glEndList();

                        //GlStateManager.

                        if (!success) {
                            L.d("Success: " + success);
                        }
                        glListMap.put(entity, glRenderList);

                        //TEST
                        /*ViewFrustum viewFrustum = Util.getField(renderGlobal, "viewFrustum");
                        RenderChunk[] chunks = viewFrustum.renderChunks;
                        for (RenderChunk chunk : chunks) {
                            if (chunk.getPosition().equals(new BlockPos(0, 64, 0))) {
                                //L.d("Pos: " + chunk.getPosition());
                                ListedRenderChunk listed = (ListedRenderChunk) chunk;
                                CompiledChunk compiledChunk = new CompiledChunk();
                                //compiledChunk.set
                                Util.callMethod1(compiledChunk, "setLayerUsed", BlockRenderLayer.SOLID);
                                glRenderList = listed.getDisplayList(BlockRenderLayer.SOLID, compiledChunk);
                                //L.d("Picked list id: " + glRenderList2);
                            }
                        }*/
                    } else {
                        glRenderList = glListMap.get(entity);
                    }

                    GlStateManager.pushMatrix();
                    double viewX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
                    double viewY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
                    double viewZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

                    double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
                    double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
                    double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;

                    GlStateManager.translate(posX - viewX, posY - viewY, posZ - viewZ);

                    float time = System.currentTimeMillis() % 1000000;

                    float rotationYaw;
                    float rotationPitch;
                    if (entity.getControllingPassenger() == player) {
                        rotationYaw = player.rotationYaw;
                        rotationPitch = player.rotationPitch;
                    } else {
                        rotationYaw = entity.rotationYaw;
                        rotationPitch = entity.rotationPitch;
                    }
                    float rotXY = rotationYaw * MathUtils.degToRadF;

                    //GlStateManager.rotate(-rotationPitch, (float) -Math.cos(rotXY), 0, (float) -Math.sin(rotXY));
                    //GlStateManager.rotate(-rotationYaw + 180, 0, 1, 0);

                    //L.d("Rendering vessel entity, entity: " + ship);
                    //L.d("Rendering vessel entity, cluster: " + ship.cluster);
                    //L.d("Rendering vessel entity, rotator: " + (ship.cluster.getRotator() == null ? "---" : ship.cluster.getRotator().toString()));

                    ScaledRotator rotator = ship.cluster.getRotator();
                    //rotator.rotateLookForward(0.01f);
                    //rotator.resetRotation();
                    if (rotator != null) {
                        //L.s("rotator found");
                        //L.s("up:" + rotator.lookUp);
                        rotator.pushMatrixToGlStack(false);
                    }

                    Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                    Minecraft.getMinecraft().entityRenderer.enableLightmap();

                    GL11.glCallList(glRenderList);

                    Minecraft.getMinecraft().entityRenderer.disableLightmap();

                    /*vertexBuffer.bindBuffer();
                    GlStateManager.glVertexPointer(3, 5126, 28, 0);
                    GlStateManager.glColorPointer(4, 5121, 28, 12);
                    GlStateManager.glTexCoordPointer(2, 5126, 28, 16);
                    OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
                    GlStateManager.glTexCoordPointer(2, 5122, 28, 24);
                    OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                    vertexBuffer.drawArrays(7);*/

                    GlStateManager.popMatrix();

                    //OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
                    GlStateManager.resetColor();
                } catch (Throwable t) {
                    L.e("Exception when rendering distant blocks", t);
                    L.e("Cause: ", t.getCause());
                }

                /*try {
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

    @Override
    public void finalize() {
        //L.d("finalising");
        /*if (glRenderList != 0) {
            GLAllocation.deleteDisplayLists(glRenderList);
            glRenderList = 0;
        }*/
    }
}
