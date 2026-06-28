package gdavid.psionicutilities.mixin;

import gdavid.psionicutilities.ConnectorColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import vazkii.psi.api.spell.SpellParam.Side;
import vazkii.psi.api.spell.SpellPiece;

@Pseudo @Mixin(targets = "gdavid.phi.util.ParamHelper", remap = false)
public abstract class PhiMixin {
	
	@OnlyIn(Dist.CLIENT)
	@Overwrite public static int connectorColor(SpellPiece piece, Side side, int def) {
		return ConnectorColor.colorIdFor(piece, side);
	}
	
}
