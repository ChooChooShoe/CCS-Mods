package ccs.mods.books;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NBTTagString;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DungeonHooks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "ccsBookMod", name = "Libary Craft", version = "1.0.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {
		"BookMod1", "BM|BSign", "BM|TE", "BM|BEdit" }, packetHandler = ServerHandler.class)
public class BookMod {
	@SidedProxy(clientSide = "ccs.mods.books.client.ClientProxy", serverSide = "ccs.mods.books.CommonProxy")
	public static CommonProxy proxy;
	@Instance("ccsBookMod")
	public static BookMod instance = new BookMod();
	private static Configuration config;

	public static Block bookshelf;
	public static Block paperMill;
	public static Block bookPress;
	public static Item scrollWriting;
	public static Item scrollSigned;
	public static Item bookColour;
	public static Item bookWriting;
	public static Item bookSigned;
	public static List SignedStacks = new ArrayList();
	
	@Init
	public void loadMod(FMLInitializationEvent event) {
		proxy.registerRenderInformation();
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		Block.blocksList[47] = null;
		Item.itemsList[47] = null;

		bookshelf = new BlockBookshelf(47, 0).setBlockName("bookshelf")
				.setHardness(1.5F);

		paperMill = new BlockPaperMill(config.getBlock("PaperMillID", 200)
				.getInt(), 1).setBlockName("paperMill").setHardness(2.0F);
		bookPress = new BlockBookPress(config.getBlock("BookPressID", 201)
				.getInt()).setBlockName("bookPress").setHardness(2.2F);
		scrollWriting = new ItemScroll(config.getItem("ScrollID", 1010)
				.getInt(), false).setItemName("Scroll").setIconIndex(18)
				.setCreativeTab(CreativeTabs.tabMisc);

		scrollSigned = new ItemScroll(config.getItem("SignedScrollID", 1011)
				.getInt(), true).setItemName("ScrollSigned").setIconIndex(19);

		bookColour = new ItemRainbowBooks(config
				.getItem("ColouredBookID", 1012).getInt(), false, false)
		.setIconIndex(15).setItemName("RainbowBookBase").setMaxStackSize(64);

		bookWriting = new ItemRainbowBooks(config.getItem(
				"ColouredBookQuillID", 1013).getInt(), false, true)
		.setIconIndex(14).setItemName("RainbowBooks");

		bookSigned = new ItemRainbowBooks(config.getItem(
				"ColouredBookSignedID", 1014).getInt(), true, false)
		.setIconIndex(15).setItemName("SRainbowBooks");

		GameRegistry.registerBlock(bookshelf);
		GameRegistry.registerBlock(paperMill);
		GameRegistry.registerBlock(bookPress);
		GameRegistry.registerTileEntity(TileEntityBookshelf.class, "Bookshelf");
		GameRegistry.registerTileEntity(TileEntityPaperMill.class, "PaperMill");
		GameRegistry.registerTileEntity(TileEntityBookPress.class, "BookPress");
		for (int id = 0; id < 15; id++) {
			ItemStack output = new ItemStack(bookColour, 1, id);
			ItemStack output2 = new ItemStack(bookWriting, 1, 15);
			ItemStack wool = new ItemStack(Block.cloth, 1, ~(id & 15));
			GameRegistry.addShapelessRecipe(output, Item.paper, Item.paper,
					Item.paper, wool);
			GameRegistry.addShapelessRecipe(output2, output, Item.feather,
					new ItemStack(Item.dyePowder, 1, 0));
		}
		GameRegistry.addRecipe(new ItemStack(bookshelf), new Object[] { "WWW",
			"BBB", "WWW", 'W', Block.planks, 'B', bookColour });
		GameRegistry.addRecipe(new ItemStack(bookshelf), new Object[] { "WWW",
			"BBB", "WWW", 'W', Block.planks, 'B', Item.book });

		GameRegistry.addRecipe(new ItemStack(scrollWriting, 5), new Object[] {
			" S", "IPF", " S", 'S', Item.stick, 'I',
			new ItemStack(Item.dyePowder, 1, 0), 'P', Item.paper, 'F',
			Item.feather });
		GameRegistry.addRecipe(new ItemStack(paperMill), new Object[] { "III",
			"IWI", "IBI", 'I', Item.ingotIron, 'B', Item.bucketEmpty, 'W',
			Item.paper });
		GameRegistry.addRecipe(new ItemStack(bookPress), new Object[] { "III",
			"WWW", "III", 'I', Item.ingotIron, 'W', Item.paper });

		LanguageRegistry.addName(bookPress, "Book Press");
		LanguageRegistry.addName(paperMill, "Paper Mill");
		LanguageRegistry.addName(scrollWriting, "Scroll & Quill");
		LanguageRegistry.addName(scrollSigned, "Scroll");
		LanguageRegistry.instance().addStringLocalization("item.book.name",
				"Leather Book");
		LanguageRegistry.instance().addStringLocalization(
				"item.writingBook.name", "Leather Book & Quill");
		LanguageRegistry.instance().addStringLocalization(
				"itemGroup.books", "Books");

		try {
			this.makeRandomBooks(new Random());
			this.makeRandomScrolls();
		} catch (IOException e) {
			e.printStackTrace();
		}
		config.save();
	}

	protected void makeRandomScrolls() throws IOException {
		ItemStack book = new ItemStack(scrollSigned);
		InputStream stream = ClassLoader
				.getSystemResourceAsStream("/ccs/res/books/scrolls.txt");
		if (stream == null) {
			System.out.println("null scroll");
			return;
		}
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		String line;
		NBTTagList bookPages = null;
		while ((line = reader.readLine()) != null) {
			if (!line.startsWith("#")) {
				String[] strings;
				if ((strings = line.split("=")).length == 2) {
					String key = strings[0];
					String value = strings[1];
					value = value.replaceAll("/-", "=").replaceAll("/n", "\n");

					if (key.equals("Author")) {
						if (bookPages == null) {
							bookPages = new NBTTagList("pages");
						} else {
							if (book.hasTagCompound()) {
								NBTTagCompound var7 = book.getTagCompound();
								var7.setTag("pages", bookPages);
							} else {
								book.func_77983_a("pages", bookPages);
							}
							if (!ItemScroll.validBookTagPages(book
									.getTagCompound())) {
								reader.close();
								throw new IOException("Invalid Book Tags @ "
										+ line + " #scrolls.txt");
							}
							SignedStacks.add(book);
							DungeonHooks.addDungeonLoot(book, 1);
							bookPages = new NBTTagList("pages");
							book = new ItemStack(scrollSigned);
							book.func_77983_a("author", new NBTTagString(
									"author", value));
						}
					} else if (key.equals("Title")) {
						if (value.length() > 15) {
							book.func_77983_a("title", new NBTTagString(
									"title", value));
						}
					} else if (key.equals("Page")) {
						bookPages.appendTag(new NBTTagString("1", value));
					}
				}
			}
		}
		reader.close();
	}

	protected void makeRandomBooks(Random rand) throws IOException {
		ItemStack book = new ItemStack(bookSigned, 1, rand.nextInt(15));
		InputStream stream = ClassLoader
				.getSystemResourceAsStream("ccs/res/books/books.txt");
		if (stream == null) {
			System.out.println("null book");
			return;
		}
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		String line;
		NBTTagList bookPages = null;
		while ((line = reader.readLine()) != null) {
			if (!line.startsWith("#")) {
				String[] strings;
				if ((strings = line.split("=")).length == 2) {
					String key = strings[0];
					String value = strings[1];
					value = value.replaceAll("/-", "=").replaceAll("/n", "\n");

					if (key.equals("Author")) {
						if (bookPages == null) {
							bookPages = new NBTTagList("pages");
							book.func_77983_a("author", new NBTTagString(
									"author", value));
						} else {
							book.func_77983_a("pages", bookPages);
							SignedStacks.add(book);
							DungeonHooks.addDungeonLoot(book, 1);
							bookPages = new NBTTagList("pages");
							book = new ItemStack(bookSigned, 1,
									rand.nextInt(15));
							book.func_77983_a("author", new NBTTagString(
									"author", value.trim()));
						}
					} else if (key.equals("Title")) {
						if (value.length() < 15) {
							book.func_77983_a("title", new NBTTagString(
									"title", value.trim()));
						}
					} else if (key.startsWith("Page")) {
						int pageNum = Character.getNumericValue(key
								.toCharArray()[4]);
						if (pageNum > 0 && pageNum <= 50
								&& value.length() < 256) {
							bookPages.appendTag(new NBTTagString(String
									.valueOf(pageNum), value));
						}
					}
				}
			}
		}
		reader.close();
	}

	@PreInit
	public void PreLoadMod(FMLPreInitializationEvent evt) {
		config = new Configuration(evt.getSuggestedConfigurationFile());

		config.load();
	}

	@PostInit
	public void PostLoadMod(FMLPostInitializationEvent evt) {
	}
}
