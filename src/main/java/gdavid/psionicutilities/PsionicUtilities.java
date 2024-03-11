package gdavid.psionicutilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.registries.RegisterEvent;
import vazkii.psi.api.ClientPsiAPI;

@Mod(PsionicUtilities.modId)
@EventBusSubscriber(bus = Bus.MOD, value = Dist.CLIENT)
public class PsionicUtilities {
	
	public static final String modId = "psionicutilities";
	
	public PsionicUtilities() {
		ModLoadingContext.get().registerExtensionPoint(DisplayTest.class,
			() -> new DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
		ModLoadingContext.get().registerConfig(Type.CLIENT, Config.spec);
	}
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerTextures(RegisterEvent e) {
		ClientPsiAPI.registerPieceTexture(new ResourceLocation(modId, "highlight"), new ResourceLocation(modId, "spell/highlight"));
	}
	
}
