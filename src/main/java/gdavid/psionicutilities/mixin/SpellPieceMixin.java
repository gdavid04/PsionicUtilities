package gdavid.psionicutilities.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import gdavid.psionicutilities.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.spell.other.PieceConnector;

@Mixin(value = SpellPiece.class, remap = false)
public class SpellPieceMixin {
	
	@OnlyIn(Dist.CLIENT)
	@Redirect(method = "drawParam", at = @At(value = "INVOKE", target = "com/mojang/blaze3d/vertex/IVertexBuilder.color(IIII)Lcom/mojang/blaze3d/vertex/IVertexBuilder;"))
	private IVertexBuilder paramColor(IVertexBuilder builder, int r, int g, int b, int a, MatrixStack ms, IVertexBuilder buffer, int light, SpellParam<?> param) {
		SpellPiece self = (SpellPiece) (Object) this;
		if (self instanceof PieceConnector) {
			int[] t = Util.getPartialRedirect(self, ((PieceConnector) self).getRedirectionSide());
			float[] rgb = Util.getColor(t[0], t[1]);
			return builder.color(rgb[0], rgb[1], rgb[2], a / 255f);
		}
		return builder.color(r, g, b, a);
	}
	
}
