package com.pauljoda.mobtools.tools;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import com.pauljoda.mobtools.MobTools;
import com.pauljoda.mobtools.lib.Reference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MobToolsItemPowerCore extends Item {

	public MobToolsItemPowerCore(int par1) {
		super(par1);
		setUnlocalizedName("powerCore");
		setCreativeTab(MobTools.tabMobTools);
		setMaxStackSize(1);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add(EnumChatFormatting.DARK_AQUA + "Kill Mobs to Fuel Power the Core");
		if(par1ItemStack.stackTagCompound != null)
		{

			par3List.add("Mob Stored: " + par1ItemStack.stackTagCompound.getString("mobName"));
			par3List.add("Kills: " + par1ItemStack.stackTagCompound.getInteger("kills"));
			par3List.add("Tier: " + par1ItemStack.stackTagCompound.getInteger("tier"));
			if(par1ItemStack.stackTagCompound.getInteger("tier") < 5)
				par3List.add("Progress: " + par1ItemStack.stackTagCompound.getInteger("tierKills") + "/" + Reference.getTierMax(par1ItemStack.stackTagCompound.getInteger("tier")));
			par3List.add("Dimension: " + getDimension(par1ItemStack.stackTagCompound.getString("mobName")));
		}
	}

	public static String getMobName(String mobName)
	{
		if(mobName == EntityCow.class.getName())
			return "Cow";
		if(mobName == EntityChicken.class.getName())
			return "Chicken";
		if(mobName == EntityPig.class.getName())
			return "Pig";
		if(mobName == EntitySheep.class.getName())
			return "Sheep";
		if(mobName == EntityVillager.class.getName())
			return "Villager";
		if(mobName == EntityEnderman.class.getName())
			return "Enderman";
		if(mobName == EntitySpider.class.getName())
			return "Spider";
		if(mobName == EntityBlaze.class.getName())
			return "Blaze";
		if(mobName == EntitySkeleton.class.getName())
			return "Skeleton";
		if(mobName == EntitySlime.class.getName())
			return "Slime";
		if(mobName == EntityCreeper.class.getName())
			return "Creeper";
		if(mobName == EntityZombie.class.getName())
			return "Zombie";
		return "Thingy";
	}

	public static String getDimension(String mobName)
	{
		if(mobName == "Blaze")
			return "Nether";
		return "Overworld";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon("mobtools:" + (this.getUnlocalizedName().substring(5)));
	}

}
