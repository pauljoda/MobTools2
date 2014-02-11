package com.pauljoda.mobtools;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;

import com.pauljoda.mobtools.common.CommonProxy;
import com.pauljoda.mobtools.handlers.ConfigurationHandler;
import com.pauljoda.mobtools.handlers.MobToolsEventHandler;
import com.pauljoda.mobtools.lib.Reference;
import com.pauljoda.mobtools.tools.ToolManager;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(name = Reference.MOD_NAME, modid = Reference.MOD_ID, version = Reference.Version)

public class MobTools {
	

	@Instance("mobtools")
	public static MobTools instance;
	
	@SidedProxy( clientSide="com.pauljoda.mobtools.client.ClientProxy", serverSide="com.pauljoda.mobtools.common.CommonProxy")
	public static CommonProxy proxy;
	
	
	//Creeper
	public static ToolMaterial CREEPER_TOOL_MATERIAL = EnumHelper.addToolMaterial("CREEPER_TOOL_MATERIAL", 3, 600, 10.0F, 6.0F, 0);
	//Ender
	public static ToolMaterial ENDER_TOOL_MATERIAL = EnumHelper.addToolMaterial("ENDER_TOOL_MATERIAL", 3, 1600, 8.0F, 3F, 4);
	//Blaze
	public static ToolMaterial BLAZE_TOOL_MATERIAL = EnumHelper.addToolMaterial("BLAZE_TOOL_MATERIAL", 3, 800, 9.0F, 4F, 8);
	//Spider
	public static ToolMaterial SPIDER_TOOL_MATERIAL = EnumHelper.addToolMaterial("SPIDER_TOOL_MATERIAL", 3, 800, 5.0F, 2F, 50);
	
	public static CreativeTabs tabMobTools = new CreativeTabs("tabMobTools") {

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return ToolManager.teleportAmulet;
		}
	};
	
	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		
        // Initialize the configuration
        ConfigurationHandler.init(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + Reference.CHANNEL_NAME.toLowerCase() + File.separator);
		
		//Add event handler
		MinecraftForge.EVENT_BUS.register(new MobToolsEventHandler());
		
		//Register Tools
		ToolManager.registerTools();
		ToolManager.registerCraftingRecipes();
				
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {

		
		
		//RegisterTileEntity
		proxy.registerTileEntities();
		
	    //Lets get Gui
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {}

}
