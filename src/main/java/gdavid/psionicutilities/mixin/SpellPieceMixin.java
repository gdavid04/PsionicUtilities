package gdavid.psionicutilities.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import gdavid.psionicutilities.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.spell.IGenericRedirector;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;

@Mixin(value = SpellPiece.class, remap = false)
public class SpellPieceMixin {
	
	@OnlyIn(Dist.CLIENT)
	@Redirect(method = "drawParam(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ILvazkii/psi/api/spell/SpellParam$Side;ILvazkii/psi/api/spell/SpellParam$ArrowType;F)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;color(IIII)Lcom/mojang/blaze3d/vertex/VertexConsumer;", remap = true))
	private VertexConsumer paramColor(VertexConsumer builder, int r, int g, int b, int a, PoseStack ms, VertexConsumer buffer, int light, SpellParam.Side side, int color, SpellParam.ArrowType arrowType, float percent) {
		SpellPiece self = (SpellPiece) (Object) this;
		// TODO Phi IWarpRedirector support
		if (self instanceof IGenericRedirector) {
			int[] t = Util.getPartialRedirect(self, side);
			float[] rgb = Util.getColor(t[0], t[1]);
			return builder.color(rgb[0], rgb[1], rgb[2], a / 255f);
		}
		return builder.color(r, g, b, a);
	}
	
}
