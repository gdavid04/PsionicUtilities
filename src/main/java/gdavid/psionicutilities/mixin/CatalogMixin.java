package gdavid.psionicutilities.mixin;

import gdavid.psionicutilities.Config;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.psi.client.gui.widget.PiecePanelWidget;

@Mixin(PiecePanelWidget.class)
public class CatalogMixin {
	
	@Redirect(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;hasAltDown()Z"))
	private boolean hasAltDown() {
		return Config.instance.foucsPreviousWithShift.get() ? Screen.hasShiftDown() : Screen.hasAltDown();
	}
	
}
