package gdavid.psionicutilities.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import gdavid.psionicutilities.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.spell.IRedirector;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellParam.Side;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.spell.other.PieceConnector;

@Mixin(value = PieceConnector.class, remap = false)
public abstract class ConnectorMixin extends SpellPiece implements IRedirector {
	
	private ConnectorMixin(Spell spell) {
		super(spell);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Redirect(method = "drawSide", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;color(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;", remap = true))
	private VertexConsumer sideColor(VertexConsumer builder, float r, float g, float b, float a, PoseStack ms, MultiBufferSource buffers, int light, Side side) {
		int[] t = Util.getPartialRedirect(this, getRedirectionSide());
		float[] rgb = Util.getColor(t[0], t[1]);
		return builder.color(rgb[0], rgb[1], rgb[2], a);
	}
	
}
