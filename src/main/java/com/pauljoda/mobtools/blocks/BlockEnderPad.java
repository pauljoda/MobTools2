package com.pauljoda.mobtools.blocks;

import com.pauljoda.mobtools.MobTools;
import com.pauljoda.mobtools.tileentities.TileEntityEnderPad;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockEnderPad extends BlockContainer {

	@SideOnly(Side.CLIENT)
	private IIcon padIconTopNorth;
	@SideOnly(Side.CLIENT)
	private IIcon padIconTopEast;
	@SideOnly(Side.CLIENT)
	private IIcon padIconTopSouth;
	@SideOnly(Side.CLIENT)
	private IIcon padIconTopWest;
	
	String direction = "mobtools:enderPadNorth";

	public BlockEnderPad() {
		super(Material.cloth);
		this.setCreativeTab(MobTools.tabMobTools);
		this.setBlockName("enderPad");
		this.setHardness(1.5F);
	}
	
	
	@Override
	public TileEntity createNewTileEntity(World par1World, int i)
    {
		return new TileEntityEnderPad();
    }

	@Override
	public IIcon getIcon(int par1, int par2)
	{
		if(par1 == 1)
			{
				return (par2 == 0 ? this.padIconTopNorth : (par2 == 1 ? this.padIconTopEast : (par2 == 2 ? this.padIconTopSouth : (par2 == 3 ? this.padIconTopWest  : this.blockIcon))));
			}
		else
			return this.blockIcon;
	}

	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon("mobtools:" + (this.getUnlocalizedName().substring(5)));
		this.padIconTopNorth = par1IconRegister.registerIcon("mobtools:enderPadNorth");
		this.padIconTopEast = par1IconRegister.registerIcon("mobtools:enderPadEast");
		this.padIconTopSouth = par1IconRegister.registerIcon("mobtools:enderPadSouth");
		this.padIconTopWest = par1IconRegister.registerIcon("mobtools:enderPadWest");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if(player.isSneaking())
			{
				int dir = world.getBlockMetadata(x, y, z);
				dir++;
				if(dir == 4)
					dir = 0;
				
				world.setBlockMetadataWithNotify(x, y, z, dir, 2);
				return true;
			}
		return false;
	}
	
	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
	{
		if(par1World.getBlockMetadata(par2, par3, par4) == 0)
		{
			par1World.setBlockMetadataWithNotify(par2, par3, par3, 0, 2);
		}
	}

}


