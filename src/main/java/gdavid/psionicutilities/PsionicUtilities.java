package gdavid.psionicutilities;

import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import vazkii.psi.api.ClientPsiAPI;

@Mod(value = PsionicUtilities.modId, dist = Dist.CLIENT)
public class PsionicUtilities {
	
	public static final String modId = "psionicutilities";
	public static final DeferredRegister<Material> SPELL_PIECE_MATERIAL = DeferredRegister.create(ClientPsiAPI.SPELL_PIECE_MATERIAL, modId);
	public static final DeferredHolder<Material, Material> HIGHLIGHT = SPELL_PIECE_MATERIAL.register("highlight", () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(modId, "spell/highlight")));

	public PsionicUtilities(ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.CLIENT, Config.spec);
	}
	
}
