package gdavid.psionicutilities.mixin;

import java.util.Iterator;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.CompiledSpell;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.client.gui.widget.SpellCostsWidget;
import vazkii.psi.common.item.ItemCAD;

@Mixin(value = SpellCostsWidget.class, remap = false)
public class StatWidgetMixin {
	
	private static final ThreadLocal<EnumSpellStat> currentStat = new ThreadLocal<>();
	
	@Shadow @Final private GuiProgrammer parent;
	
	@Inject(method = "lambda$renderWidget$0",
			at = @At(value = "INVOKE", target = "Lvazkii/psi/api/spell/SpellMetadata;getStat(Lvazkii/psi/api/spell/EnumSpellStat;)I"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void capture(GuiGraphics graphics, int mouseX, int mouseY, CompiledSpell compiledSpell, CallbackInfo callback,
			int i, int statX, SpellMetadata meta, ItemStack cad, Iterator<?> var9, EnumSpellStat stat) {
		currentStat.set(stat);
	}
	
	@ModifyArg(method = "lambda$renderWidget$0",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I", remap = true),
			index = 4)
	private int setColor(int color) {
		if (currentStat.get() == EnumSpellStat.COST) {
			if (parent.compileResult.left().isPresent()) {
				Player player = parent.getMinecraft().player;
				SpellMetadata meta = parent.compileResult.left().get().metadata;
				ItemStack cad = PsiAPI.getPlayerCAD(player);
				int realCost = ItemCAD.getRealCost(cad, ItemStack.EMPTY, meta.getStat(EnumSpellStat.COST));
				int capacity = PsiAPI.internalHandler.getDataForPlayer(player).getTotalPsi()
						+ (cad.isEmpty() ? 0 : ((ICAD) cad.getItem()).getStatValue(cad, EnumCADStat.OVERFLOW));
				/* leggings sustainable:         green  */ if (realCost <= 25) color = 0x55FF55;
				/* loopcast sustainable:         cyan   */ else if (realCost <= 25 * 5) color = 0x55FFFF;
				/* kills the caster:             red    */ else if (realCost >= capacity + 125 * player.getMaxHealth()) color = 0xFF5555;
				/* kills the caster as loopcast: orange */ else if (realCost >= capacity + 50 * player.getMaxHealth()) color = 0xFFAA00;
				/* causes overflow:              yellow */ else if (realCost >= capacity) color = 0xFFFF55;
				/* TODO: tooltip showing
				 * - leggings and loopcast sustain duration or that the spell causes overflow
				 * - damage dealt when cast with full psi bar
				 * - damage dealt when manual cast after resistance N (when Phi is present)
				 */
			}
		}
		return color;
	}
	
}
