package gdavid.psionicutilities.mixin;

import gdavid.psionicutilities.Config;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.client.gui.widget.PiecePanelWidget;
import vazkii.psi.common.spell.other.PieceConnector;

@Mixin(value = PiecePanelWidget.class, remap = false)
public class CatalogMixin {
	
	@Shadow @Final public GuiProgrammer parent;
	
	@Redirect(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;hasAltDown()Z", remap = true), remap = true)
	private boolean hasAltDown() {
		return Config.instance.foucsPreviousWithShift.get() ? Screen.hasShiftDown() : Screen.hasAltDown();
	}
	
	@Inject(method = "lambda$populatePanelButtons$0", at = @At(value = "INVOKE_ASSIGN", target = "Lvazkii/psi/api/spell/SpellPiece;copyFromSpell(Lvazkii/psi/api/spell/Spell;)Lvazkii/psi/api/spell/SpellPiece;"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void quickArg(Button button, CallbackInfo callback, SpellPiece piece) {
		var old = parent.spell.grid.gridData[GuiProgrammer.selectedX][GuiProgrammer.selectedY];
		if (!(old instanceof PieceConnector connector)) return;
		var side = connector.paramSides.get(connector.target);
		if (!side.isEnabled()) return;
		try {
			var input = parent.spell.grid.getPieceAtSideWithRedirections(old.x, old.y, side);
			if (input == null) return;
			for (var param : piece.params.values()) {
				if (param.canAccept(input)) {
					piece.paramSides.put(param, side);
					return;
				}
			}
		} catch (SpellCompilationException ignored) {}
	}
	
}
