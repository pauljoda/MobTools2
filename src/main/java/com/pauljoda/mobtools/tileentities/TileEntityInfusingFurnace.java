package com.pauljoda.mobtools.tileentities;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import com.pauljoda.mobtools.blocks.BlockInfusingFurnace;
import com.pauljoda.mobtools.lib.RecipiesInfuser;
import com.pauljoda.mobtools.lib.Reference;
import com.pauljoda.mobtools.rendering.ParticleHelper;
import com.pauljoda.mobtools.structures.Structures;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityInfusingFurnace extends TileEntity implements IInventory {

	private static final int[] slots_top = new int[] {1};
	private static final int[] slots_bottom = new int[] {2};
	private static final int[] slots_sides = new int[] {1};

	/**
	 * The ItemStacks that hold the items currently being used in the furnace
	 */
	private ItemStack[] furnaceItemStacks = new ItemStack[3];

	/**
	 * The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for
	 */
	public int currentItemBurnTime = 0;

	/** The number of ticks that the current item has been cooking for */
	public int furnaceCookTime = 0;

	private String field_94130_e;

	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory()
	{
		return this.furnaceItemStacks.length;
	}

	/**
	 * Returns the stack in slot i
	 */
	public ItemStack getStackInSlot(int par1)
	{
		return this.furnaceItemStacks[par1];
	}


	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.furnaceItemStacks[par1] != null)
		{
			ItemStack var3;

			if (this.furnaceItemStacks[par1].stackSize <= par2)
			{
				var3 = this.furnaceItemStacks[par1];
				this.furnaceItemStacks[par1] = null;
				return var3;
			}
			else
			{
				var3 = this.furnaceItemStacks[par1].splitStack(par2);

				if (this.furnaceItemStacks[par1].stackSize == 0)
				{
					this.furnaceItemStacks[par1] = null;
				}

				return var3;
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
	 * like when you close a workbench GUI.
	 */
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (this.furnaceItemStacks[par1] != null)
		{
			ItemStack var2 = this.furnaceItemStacks[par1];
			this.furnaceItemStacks[par1] = null;
			return var2;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.furnaceItemStacks[par1] = par2ItemStack;

		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
		{
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	/**
	 * Returns the name of the inventory.
	 */
	public String getInvName()
	{
		return "Infuser";
	}
	public void func_94129_a(String par1Str)
	{
		this.field_94130_e = par1Str;
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items", 10);
		this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = var2.getCompoundTagAt(var3);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.furnaceItemStacks.length)
			{
				this.furnaceItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		this.furnaceCookTime = par1NBTTagCompound.getShort("CookTime");

		if (par1NBTTagCompound.hasKey("CustomName"))
		{
			this.field_94130_e = par1NBTTagCompound.getString("CustomName");
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setShort("CookTime", (short)this.furnaceCookTime);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.furnaceItemStacks.length; ++var3)
		{
			if (this.furnaceItemStacks[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)var3);
				this.furnaceItemStacks[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", var2);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.func_148857_g());
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
	 * this more of a set than a get?*
	 */
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@SideOnly(Side.CLIENT)

	/**
	 * Returns an integer between 0 and the passed value representing how close the current item is to being completely
	 * cooked
	 */
	public int getCookProgressScaled(int par1)
	{
		return this.furnaceCookTime * par1 / 2000;
	}


	/**
	 * Returns true if the furnace is currently burning
	 */
	public boolean isBurning()
	{
		return this.furnaceCookTime > 0;
	}

	/**
	 * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
	 * ticks and creates a new spawn inside its implementation.
	 */
	public void updateEntity()
	{

		boolean var1 = this.furnaceCookTime > 0;
		boolean var2 = false;

		if (!this.worldObj.isRemote)
		{

			if (this.canSmelt())
			{
				++this.furnaceCookTime;

				if (this.furnaceCookTime == 2000)
				{
					this.furnaceCookTime = 0;
					this.smeltItem();
					var2 = true;
				}
			}
			else
			{
				this.furnaceCookTime = 0;
			}

			if (var1 != this.furnaceCookTime > 0)
			{
				var2 = true;
				BlockInfusingFurnace.updateFurnaceBlockState(this.furnaceCookTime> 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
		}



		if (var2)
		{
			this.markDirty();
		}
	}

	/**
	 * Checks if the item to be infused can be infused based on structure
	 */
	public boolean canInfuseItem(Item item)
	{
		if(Reference.isWoodenTool(item))
		{
			if(!this.worldObj.isRemote)
			{
				if(Structures.areStructureEqual(Structures.StructureInfuserWood(), Structures.buildStructureFromCoords(worldObj, xCoord - 2, yCoord - 1, zCoord - 2))
						|| Structures.areStructureEqual(Structures.StructureInfuserStone(), Structures.buildStructureFromCoords(worldObj, xCoord - 2, yCoord - 1, zCoord - 2))
						|| Structures.areStructureEqual(Structures.StructureInfuserIron(), Structures.buildStructureFromCoords(worldObj, xCoord - 2, yCoord - 1, zCoord - 2))
						|| Structures.areStructureEqual(Structures.StructureInfuserGold(), Structures.buildStructureFromCoords(worldObj, xCoord - 2, yCoord - 1, zCoord - 2))
						|| Structures.areStructureEqual(Structures.StructureInfuserDiamond(), Structures.buildStructureFromCoords(worldObj, xCoord - 2, yCoord - 1, zCoord - 2)))
					return true;
			}
		}

		else if(Reference.isStoneTool(item))
		{
			if(!this.worldObj.isRemote)
			{
				if(Structures.areStructureEqual(Structures.StructureInfuserStone(), Structures.buildStructureFromCoords(worldObj, xCoord - 2, yCoord - 1, zCoord - 2))
						|| Structures.areStructureEqual(Structures.StructureInfuserIron(), Structures.buildStructureFromCoords(worldObj, xCoord - 2, yCoord - 1, zCoord - 2))
						|| Structures.areStructureEqual(Structures.StructureInfuserGold(), Structures.buildStructureFromCoords(worldObj, xCoord - 2, yCoord - 1, zCoord - 2))
						|| Structures.areStructureEqual(Structures.StructureInfuserDiamond(), Structures.buildStructureFromCoords(worldObj, xCoord - 2, yCoord - 1, zCoord - 2)))
					return true;
			}
		}
		
		else if(Reference.isIronTool(item))
		{
			if(!this.worldObj.isRemote)
			{
				if(Structures.areStructureEqual(Structures.StructureInfuserIron(), Structures.buildStructureFromCoords(worldObj, xCoord - 2, yCoord - 1, zCoord - 2))
						|| Structures.areStructureEqual(Structures.StructureInfuserGold(), Structures.buildStructureFromCoords(worldObj, xCoord - 2, yCoord - 1, zCoord - 2))
						|| Structures.areStructureEqual(Structures.StructureInfuserDiamond(), Structures.buildStructureFromCoords(worldObj, xCoord - 2, yCoord - 1, zCoord - 2)))
					return true;
			}
		}
		
		else if(Reference.isGoldTool(item) || Reference.isModItem(item))
		{
			if(!this.worldObj.isRemote)
			{
				if(Structures.areStructureEqual(Structures.StructureInfuserGold(), Structures.buildStructureFromCoords(worldObj, xCoord - 2, yCoord - 1, zCoord - 2))
						|| Structures.areStructureEqual(Structures.StructureInfuserDiamond(), Structures.buildStructureFromCoords(worldObj, xCoord - 2, yCoord - 1, zCoord - 2)))
					return true;
			}
		}
		
		else if(Reference.isDiamondTool(item))
		{
			if(!this.worldObj.isRemote)
			{
				if(Structures.areStructureEqual(Structures.StructureInfuserDiamond(), Structures.buildStructureFromCoords(worldObj, xCoord - 2, yCoord - 1, zCoord - 2)))
					return true;
			}
		}


		return false;
	}
	/**
	 * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
	 */
	private boolean canSmelt()
	{ 
		if(this.furnaceItemStacks[0] == null || this.furnaceItemStacks[1] == null){
			return false;
		}

		else
		{
			if(canInfuseItem(furnaceItemStacks[1].getItem()))
			{
				ItemStack var1 = RecipiesInfuser.getSmeltingResult(this.furnaceItemStacks[0], this.furnaceItemStacks[1]);
				if (var1 == null) return false;
				if (this.furnaceItemStacks[2] == null) return true;
				if (!this.furnaceItemStacks[2].isItemEqual(var1)) return false;
				int result = furnaceItemStacks[2].stackSize + var1.stackSize;
				return (result <= getInventoryStackLimit() && result <= var1.getMaxStackSize());
			}
		}
		return false;

	}


	/**
	 * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
	 */
	public void smeltItem()
	{

		if (this.canSmelt())
		{

			ItemStack var1 = RecipiesInfuser.getSmeltingResult(this.furnaceItemStacks[0], this.furnaceItemStacks[1]);

			if (this.furnaceItemStacks[2] == null)
			{
				this.furnaceItemStacks[2] = var1.copy();
			}
			else if (this.furnaceItemStacks[2].isItemEqual(var1))
			{
				furnaceItemStacks[2].stackSize += var1.stackSize;
			}
			--this.furnaceItemStacks[0].stackSize;
			--this.furnaceItemStacks[1].stackSize;

			if (this.furnaceItemStacks[0].stackSize <= 0)
			{
				this.furnaceItemStacks[0] = null;
			}
			if (this.furnaceItemStacks[1].stackSize <= 0)
			{
				this.furnaceItemStacks[1] = null;
			}
		}
	}


	/**
	 * Do not make give this method the name canInteractWith because it clashes with Container
	 */
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}


	public int getStartInventorySide(ForgeDirection side)
	{
		if (side == ForgeDirection.DOWN) return 1;
		if (side == ForgeDirection.UP) return 0;
		return 2;
	}

	public int getSizeInventorySide(ForgeDirection side)
	{
		return 1;
	}


	public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
	{
		return par1 == 2 ? false : ((par1 == 1 && Reference.isValidFusee(par2ItemStack)) ? true : true);
	}

	public int[] getAccessibleSlotsFromSide(int par1)
	{
		return par1 == 0 ? slots_bottom : (par1 == 1 ? slots_top : slots_sides);
	}

	public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3)
	{
		return this.isItemValidForSlot(par1, par2ItemStack);
	}

	public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3)
	{
		return true;
	}

	public void setGuiDisplayName(String par1Str)
	{
		this.field_94130_e = par1Str;
	}

	@Override
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return this.field_94130_e != null && this.field_94130_e.length() > 0;
	}

	@Override
	public void openInventory() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub

	}




}