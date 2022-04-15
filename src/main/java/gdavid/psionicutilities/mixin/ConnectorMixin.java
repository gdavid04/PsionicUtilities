package gdavid.psionicutilities.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import gdavid.psionicutilities.Util;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.spell.IRedirector;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.spell.other.PieceConnector;

@Mixin(value = PieceConnector.class, remap = false)
public abstract class ConnectorMixin extends SpellPiece implements IRedirector {
	
	private ConnectorMixin(Spell spell) {
		super(spell);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Redirect(method = "drawSide", at = @At(value = "INVOKE", target = "com/mojang/blaze3d/vertex/IVertexBuilder.color(FFFF)Lcom/mojang/blaze3d/vertex/IVertexBuilder;"))
	private IVertexBuilder sideColor(IVertexBuilder builder, float r, float g, float b, float a, MatrixStack ms, IRenderTypeBuffer buffers, int light, SpellParam.Side side) {
		int[] t = Util.getPartialRedirect(this, getRedirectionSide());
		float[] rgb = Util.getColor(t[0], t[1]);
		return builder.color(rgb[0], rgb[1], rgb[2], a);
	}
	
}
