package gdavid.psionicutilities;

import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.RegisterEvent;
import vazkii.psi.api.ClientPsiAPI;

@Mod(value = PsionicUtilities.modId, dist = Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
public class PsionicUtilities {
	
	public static final String modId = "psionicutilities";
	public static final Material HIGHLIGHT = new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(modId, "spell/highlight"));
	
	public PsionicUtilities(ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.CLIENT, Config.spec);
	}
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	private static void registerTextures(RegisterEvent e) {
		e.register(ClientPsiAPI.SPELL_PIECE_MATERIAL, ResourceLocation.fromNamespaceAndPath(modId, "highlight"), () -> HIGHLIGHT);
	}
	
}
