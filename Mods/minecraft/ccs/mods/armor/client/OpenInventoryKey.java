package ccs.mods.armor.client;

import java.util.EnumSet;


import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.KeyBinding;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class OpenInventoryKey implements ITickHandler {

	public static KeyBinding keyInvOpen = new KeyBinding("key.inventory", 19);
	private boolean KeyPressed;
	
	public OpenInventoryKey() {
		Minecraft.getMinecraft().gameSettings.keyBindings[9] = keyInvOpen;
	}

	@Override
	public String getLabel() {
		return "Mod_OpenPlayerInvetory";
	}
	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if(keyInvOpen.isPressed() && !KeyPressed){
			this.KeyPressed = true;
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if(KeyPressed){
			KeyPressed = false;
			EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
			FMLClientHandler.instance().displayGuiScreen(player, new GuiPlayerInventory(player));
		}
	}

}
