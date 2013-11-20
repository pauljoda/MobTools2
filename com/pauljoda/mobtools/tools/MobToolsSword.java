package com.pauljoda.mobtools.tools;

import com.pauljoda.mobtools.MobTools;
import com.pauljoda.mobtools.handlers.ToolHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

public class MobToolsSword extends ItemSword {

	int ingot;
	int type;
	public MobToolsSword(int par1, EnumToolMaterial par2EnumToolMaterial, String unlocalized, int ingot, int type) {
		super(par1, par2EnumToolMaterial);
		this.setUnlocalizedName(unlocalized);
		this.setCreativeTab(MobTools.tabMobTools);
		this.ingot = ingot;
		this.type = type;
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
	{
		ToolHandler.swordEffect(type, par1ItemStack, par2EntityLivingBase, par3EntityLivingBase);
		return true;
	}
	
	@Override
	 public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        
		ToolHandler.swordBlockEffect(type, par1ItemStack, par2World, par3EntityPlayer);
		par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        return par1ItemStack;
    }

	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon("mobtools:" + (this.getUnlocalizedName().substring(5)));

	}

	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
	{
		return ingot == par2ItemStack.itemID ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

}