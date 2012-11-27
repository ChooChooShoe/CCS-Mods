package ccs.mods.books;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ItemWritableBook;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class ServerHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager network,
			Packet250CustomPayload packet, Player player) {
		ByteArrayInputStream bytes;
		DataInputStream input;
		ItemStack stack1;
		ItemStack stack2;
		int int1 = 0;
		if ("BM|BEdit".equals(packet.channel)) {
			try {
				input = new DataInputStream(bytes = new ByteArrayInputStream(
						packet.data));
				int1 = input.readInt();
				stack1 = Packet.readItemStack(input);
				
				stack2 = ((EntityPlayer) player).getHeldItem();

				if (stack1 != null && stack1.itemID == stack2.itemID) {
					stack2.setTagCompound(stack1.getTagCompound());
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if ("BM|BSign".equals(packet.channel)) {
			try {

				input = new DataInputStream(new ByteArrayInputStream(
						packet.data));
				int1 = input.readInt();
				stack1 = Packet.readItemStack(input);

				stack2 = ((EntityPlayer) player).inventory.getCurrentItem();

				if (stack1 != null && stack2 != null) {
					stack2.itemID = int1;
					stack2.setTagCompound(stack1.getTagCompound());
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} else if ("BM|TE".equals(packet.channel)) {
			try {
				ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
				int x = data.readInt();
				int y = data.readInt();
				int z = data.readInt();
				World world = ((EntityPlayer) player).worldObj;
				TileEntityBookshelf shelf = (TileEntityBookshelf) world
						.getBlockTileEntity(x, y, z);
				if (shelf != null) {
					for (int i = 0; i < 5; i++) {
						short ItemID = data.readShort();
						byte StackSize = data.readByte();
						short StaceDamage = data.readShort();

						if (ItemID > 0) {
							shelf.setInventorySlotContents(i, new ItemStack(
									ItemID, StackSize, StaceDamage));
						} else {
							shelf.setInventorySlotContents(i, null);
						}
						// shelf.handlePacketData(data);
					}
				}
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		}
	}

	/**
	 * Send A New Packet To All Connected Players For Golem Position and
	 * Inventory
	 * 
	 * @param golem The Golem The Packet is for
	 * 
	 * @param channelName Defaults to "GolemCompanions"
	 * 
	 * @param sendData Any Extra Data
	 * 
	 * public static void sendEntityPacket(EntityGolem golem, String
	 * channelName, Object... sendData) { ByteArrayOutputStream bytes = new
	 * ByteArrayOutputStream(); DataOutputStream data = new
	 * DataOutputStream(bytes); try { data.writeInt(golem.entityId);
	 * data.writeInt(golem.inventory.getSizeInventory()); for (int i = 0; i <
	 * golem.inventory.getSizeInventory(); i++) { try{ ItemStack item =
	 * golem.inventory.getStackInSlot(i); data.writeShort(item.itemID);
	 * data.writeByte(item.stackSize); data.writeShort(item.getItemDamage()); }
	 * catch (NullPointerException nullp){ data.writeShort(1);
	 * data.writeByte(0); data.writeShort(0); } }
	 * sendPacketToClients(channelName, bytes, data, sendData); } catch
	 * (IOException e) { e.printStackTrace(); } }
	 * 
	 * /** Send A New Packet To The Server For Golem Position and Inventory
	 * 
	 * @param golem The Golem The Packet is for
	 * 
	 * @param channelName Defaults to "GolemCompanions"
	 * 
	 * @param sendData Any Extra Data
	 * 
	 * public static void sendEntityPacketToServer(EntityGolem golem, String
	 * channelName, Object... sendData) { ByteArrayOutputStream bytes = new
	 * ByteArrayOutputStream(); DataOutputStream data = new
	 * DataOutputStream(bytes); ItemStack[] items =
	 * golem.inventory.getInventory(); try { data.writeInt(golem.entityId);
	 * data.writeInt(golem.inventory.getSizeInventory()); if (items != null) {
	 * for (int i = 0; i < golem.inventory.getSizeInventory(); i++) { try{
	 * data.writeShort(items[i].itemID); data.writeByte(items[i].stackSize);
	 * data.writeShort(items[i].getItemDamage()); } catch (NullPointerException
	 * nullp){ data.writeShort(2); data.writeByte(0); data.writeShort(0); } } }
	 * 
	 * sendPacketToServer(channelName, bytes, data, sendData); } catch
	 * (IOException e) { e.printStackTrace(); } } /** Sends A new Packet To All
	 * Players on the Server After Converting sendData to DataOutputStream data
	 */

	public static void encodeByteArray(ByteArrayOutputStream bytes,
			Object... encode) {
		DataOutputStream data = new DataOutputStream(bytes);
		try {
			for (Object value : encode) {
				if (value instanceof Integer) {
					data.writeInt((Integer) value);
				} else if (value instanceof Float) {
					data.writeFloat((Float) value);
				} else if (value instanceof Byte) {
					data.writeByte((Byte) value);
				} else if (value instanceof Boolean) {
					data.writeBoolean((Boolean) value);
				} else if (value instanceof String) {
					data.writeUTF((String) value);
				} else if (value instanceof Short) {
					data.writeShort((Short) value);
				} else if (value instanceof Long) {
					data.writeLong((Long) value);
				} else if (value instanceof Character) {
					data.writeChar((Character) value);
				}
			}
			data.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendPacketTo(int type, Player player,
			String channelName, ByteArrayOutputStream bytes, Object... sendData) {

		encodeByteArray(bytes, sendData);
		Packet250CustomPayload packet = new Packet250CustomPayload(channelName,
				bytes.toByteArray());

		switch (type) {
		case 0:
			PacketDispatcher.sendPacketToServer(packet);
			break;
		case 1:
			PacketDispatcher.sendPacketToPlayer(packet, player);
			break;
		case 2:
			PacketDispatcher.sendPacketToAllPlayers(packet);
			break;
		}
	}

	public static void sendBookToServer(String var8, ItemStack bookStack,
			int newBookID) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(bytes);
		try {
			data.writeInt(newBookID);
			Packet.writeItemStack(bookStack, data);
			Packet250CustomPayload packet = new Packet250CustomPayload(var8,
					bytes.toByteArray());
			PacketDispatcher.sendPacketToServer(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
