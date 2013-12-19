package com.pauljoda.mobtools.lib;

import com.pauljoda.mobtools.handlers.GeneralSettings;
import com.pauljoda.mobtools.tools.ToolManager;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Reference {
	
	public static final String MOD_ID = "mobtools";
	public static final String MOD_NAME = "Mob Tools";
	public static final String Version = "1.5";
	public static final int VERSION_CHECK_ATTEMPTS = 3;
	public static final String CHANNEL_NAME = MOD_ID;
	
	
	static int validInputs[] = {
						Item.diamond.itemID, Item.swordDiamond.itemID,
						Item.pickaxeDiamond.itemID, Item.shovelDiamond.itemID,
						Item.hoeDiamond.itemID, Item.axeDiamond.itemID,
						ToolManager.inertWandCore.itemID, ToolManager.goldenrod.itemID
						};
	
	public static boolean isValidFusee(ItemStack item)
	{
		for(int i = 0; i < validInputs.length; i++)
		{
			if(validInputs[i] == item.itemID)
				return true;
		}
		return false;
	}
	

	public static int getTierMax(int kills)
	{
		switch(kills)
		{
		case 0: return GeneralSettings.tierZeroKills;
		case 1: return GeneralSettings.tierOneKills;
		case 2: return GeneralSettings.tierTwoKills;
		case 3: return GeneralSettings.tierThreeKills;
		case 4: return GeneralSettings.tierFourKills;
		}
		return 0;
	}
	
}
