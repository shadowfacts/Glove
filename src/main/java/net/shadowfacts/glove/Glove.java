package net.shadowfacts.glove;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.shadowfacts.shadowmc.ShadowMC;
import net.shadowfacts.shadowmc.item.ItemBase;
import org.lwjgl.input.Keyboard;

/**
 * @author shadowfacts
 */
@Mod(modid = Glove.MODID, name = Glove.NAME, version = Glove.VERSION, dependencies = "required-after:shadowmc@[3.4.5,);")
public class Glove {

	public static final String MODID = "glove";
	public static final String NAME = "Glove";
	public static final String VERSION = "@VERSION@";

	private static final int MSG_ID = 85663;

	public static KeyBinding keyBinding = new KeyBinding("glove:mode.key", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_G, NAME);
	public static boolean gloveMode = false;

//	Content
	public static ItemBase glove = new ItemBase("glove") {{
		setCreativeTab(CreativeTabs.MISC);
	}};

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		GameRegistry.register(glove);
		GameRegistry.addShapedRecipe(new ItemStack(glove), "W ", " L", 'W', Blocks.WOOL, 'L', Items.LEATHER);
	}

	@Mod.EventHandler
	@SideOnly(Side.CLIENT)
	public void preInitClient(FMLPreInitializationEvent event) {
		glove.initItemModel();
		MinecraftForge.EVENT_BUS.register(this);

		ClientRegistry.registerKeyBinding(keyBinding);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (keyBinding.isPressed()) {
			gloveMode = !gloveMode;
			ShadowMC.proxy.sendSpamlessMessage(Minecraft.getMinecraft().player, new TextComponentTranslation(gloveMode ? "glove:mode.enable" : "glove:mode.disable"), MSG_ID);
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onModelBake(ModelBakeEvent event) {
		IBakedModel original = event.getModelRegistry().getObject(new ModelResourceLocation("glove:glove", "inventory"));
		event.getModelRegistry().putObject(new ModelResourceLocation("glove:glove", "inventory"), new GloveModel(original));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRenderSpecificHand(RenderSpecificHandEvent event) {
		if (event.getHand() == EnumHand.MAIN_HAND && !event.getItemStack().isEmpty() && (gloveMode || event.getItemStack().getItem() == glove)) {
			event.setCanceled(true);

			EntityPlayerSP player = Minecraft.getMinecraft().player;

			if (player.isInvisible()) return;

			float partialTicks = event.getPartialTicks();
			float f = player.getSwingProgress(partialTicks);
			float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
			float f3 = event.getHand() == EnumHand.MAIN_HAND ? f : 0.0F;
			ItemRenderer renderer = Minecraft.getMinecraft().getItemRenderer();
			float f5 = 1.0F - (renderer.prevEquippedProgressMainHand + (renderer.equippedProgressMainHand - renderer.prevEquippedProgressMainHand) * partialTicks);
			renderer.renderItemInFirstPerson(player, partialTicks, f1, EnumHand.MAIN_HAND, f3, ItemStack.EMPTY, f5);
		}
	}

}
