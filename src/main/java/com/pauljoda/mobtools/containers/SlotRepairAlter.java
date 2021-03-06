package com.pauljoda.mobtools.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.pauljoda.mobtools.item.ItemManager;
import com.pauljoda.mobtools.tileentities.TileEntityRepairAlter;
import com.pauljoda.mobtools.tools.ToolManager;

public class SlotRepairAlter extends Slot {

	private int type;

	private TileEntityRepairAlter alter;
	public SlotRepairAlter(EntityPlayer entityplayer, IInventory iinventory, int i, int j, int k, int type)
	{
		super(iinventory, i, j, k);
		this.type = type;
	}


	public int getSlotStackLimit()
	{
		return 1;
	}

	public boolean isItemValid(ItemStack par1ItemStack)
	{

		//		|| par1ItemStack.getUnlocalizedName() == ToolManager.spidium.getUnlocalizedName() || par1ItemStack.getUnlocalizedName() == ToolManager.blazium.getUnlocalizedName())

		//Types
		//0 Creepium
		//1 Endium
		//2 Spidium
		//3 Blazium
		switch(type)
		{
		case 0 : 
			if(par1ItemStack.getUnlocalizedName().equals(ItemManager.creepium.getUnlocalizedName()))
				return true;
			else 
				return false;

		case 1 : 
			if(par1ItemStack.getUnlocalizedName().equals(ItemManager.endium.getUnlocalizedName()))
				return true;
			else
				return false;
		case 2 :
			if(par1ItemStack.getUnlocalizedName().equals(ItemManager.spidium.getUnlocalizedName()))
				return true;
			else
				return false;
		case 3 : 
			if(par1ItemStack.getUnlocalizedName().equals(ItemManager.blazium.getUnlocalizedName()))
				return true;
			else
				return false;
			
		
		default : return false;
		}

	}

	/*
	public IIcon getBackgroundIconIndex()
	{
		return MobToolsItem.backGround;
	}
*/
}
