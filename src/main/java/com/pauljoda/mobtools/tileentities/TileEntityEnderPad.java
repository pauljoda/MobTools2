package com.pauljoda.mobtools.tileentities;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import com.pauljoda.mobtools.blocks.BlockManager;
import com.pauljoda.mobtools.handlers.GeneralSettings;

public class TileEntityEnderPad extends TileEntity {
	int cooldown;

	@Override
	public void updateEntity()
	{
		World world = this.worldObj;
		for (EntityPlayer player : getPlayersOnGrid()) 
		{
			if(player.isSneaking())
			{
				for(int i = 1; i <= GeneralSettings.maxEnderPadDistance; i++)
				{
					int xPos = xCoord;
					int yPos = yCoord;
					int zPos = zCoord;
					double zOffSet = .5D;
					double xOffSet = .5D;
					int dir = world.getBlockMetadata(xCoord, yCoord, zCoord);
					switch(dir)
					{
					case 0: xPos = xCoord;
					yPos = yCoord;
					zPos = zCoord - i;
					break;
					case 1: xPos = xCoord + i;
					yPos = yCoord;
					zPos = zCoord;
					break;
					case 2: xPos = xCoord;
					yPos = yCoord;
					zPos = zCoord + i;
					break;
					case 3: xPos = xCoord - i;
					yPos = yCoord;
					zPos = zCoord;
					break;
					}
					if(world.getBlock(xPos, yPos, zPos).getUnlocalizedName().equals(BlockManager.enderPad.getUnlocalizedName()))
					{
						if(world.getTileEntity(xPos, yPos, zPos) instanceof TileEntityEnderPad)
						{
							TileEntityEnderPad enderPad = (TileEntityEnderPad)world.getTileEntity(xPos, yPos, zPos);
							if(enderPad.cooldown <= 0)
							{
								if(world.isAirBlock(xPos, yPos + 1, zPos) && world.isAirBlock(xPos, yPos + 2, zPos))
								{
									player.setPositionAndUpdate((double)xPos + xOffSet, (double)yPos + 1, (double)zPos + zOffSet);
									world.playSoundAtEntity(player, "mob.endermen.portal", 1.0F, 1.0F);
									cooldown = 10;
								}
							}
						}
					}
				}
			}
		}

		cooldown--;
	}


	@SuppressWarnings("unchecked")
	protected List<EntityPlayer> getPlayersOnGrid() {
		AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(xCoord, yCoord + 1, zCoord, xCoord + 1, yCoord + 2, zCoord + 1);
		return worldObj.getEntitiesWithinAABB(EntityPlayer.class, bb);
	}

}


