package ccs.mods.whale;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ccs.mods.books.TileEntityBookshelf;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager network,
			Packet250CustomPayload packet, Player player) {
		ByteArrayInputStream bytes;
		DataInputStream input;
		ItemStack stack1;
		ItemStack stack2;
		if ("WhaleMod1".equals(packet.channel)) {
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
}
