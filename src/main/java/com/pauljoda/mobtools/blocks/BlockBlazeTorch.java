package com.pauljoda.mobtools.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockBlazeTorch extends Block {

	public BlockBlazeTorch() {
		super(Material.plants);
		setBlockName("blazeTorch");
		this.setLightLevel(1.0F);
		this.setBlockBounds(.3F, .3F, .3F, .7F, .7F, .7F);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(World par1World, int x, int y, int z, Random par5Random)
	{
		double xPos = (double)x + .5D;
		double yPos = (double)y + .5D;
		double zPos = (double)z + .5D;
		double randomValue = -.2D + (.2D - (-.2D)) * par5Random.nextDouble();
		double randomValue2 = -.2D + (.2D - (-.2D)) * par5Random.nextDouble();

		par1World.spawnParticle("smoke", xPos + randomValue + randomValue, yPos, zPos + randomValue2, 0.0D, .03D, 0.0D);
		par1World.spawnParticle("flame", xPos + randomValue, yPos + randomValue, zPos + randomValue2, 0.0D, .03D, 0.0D);
		par1World.spawnParticle("flame", xPos, yPos, zPos, 0.0D, 0.0D, 0.0D);

	}


	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3)
	{
		return null;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		return null;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		blockIcon = iconRegister.registerIcon("mobtools:blank");
	}

}
