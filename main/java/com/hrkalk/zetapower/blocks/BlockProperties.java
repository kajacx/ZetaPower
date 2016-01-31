package com.hrkalk.zetapower.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockProperties extends Block implements IMetaBlockName {

    public static final PropertyEnum TYPE = PropertyEnum.create("type", BlockProperties.EnumType.class);

    public BlockProperties(String unlocalizedName) {
        super(Material.iron);
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(2);
        this.setResistance(6);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumType.AND));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, meta == 0 ? EnumType.AND : EnumType.OR);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        EnumType type = (EnumType) state.getValue(TYPE);
        return type.getID();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { TYPE });
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }

    @Override
    public String getSpecialName(ItemStack stack) {
        return stack.getItemDamage() == 0 ? "and" : "or";
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos) {
        return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(world.getBlockState(pos)));
    }

    public enum EnumType implements IStringSerializable {
        AND(0, "and"), OR(1, "or");

        private int ID;
        private String name;

        private EnumType(int ID, String name) {
            this.ID = ID;
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public int getID() {
            return this.ID;
        }

        @Override
        public String toString() {
            return this.getName();
        }
    }
}
