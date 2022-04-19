package gdavid.psionicutilities.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import gdavid.psionicutilities.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.spell.SpellParam.Side;
import vazkii.psi.api.spell.SpellPiece;

@Pseudo @Mixin(targets = "gdavid.phi.util.ParamHelper", remap = false)
public abstract class PhiMixin {
	
	@OnlyIn(Dist.CLIENT)
	@Overwrite public static int connectorColor(SpellPiece piece, Side side, int def) {
		int[] t = Util.getPartialRedirect(piece, side);
		return Util.getColorId(t[0], t[1]);
	}
	
}
