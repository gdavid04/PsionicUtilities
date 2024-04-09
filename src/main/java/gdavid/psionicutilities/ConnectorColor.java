package gdavid.psionicutilities;

import java.awt.Color;

import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.SpellParam.Side;

public class ConnectorColor {
	
	private static final int defaultColor = 0xFFFFFF;
	public static final int errorColor = 0xFF0000;
	
	private static final float[] hues = new float[] {
		0.6375f, 0.4125f, 0.8f, 0.05f, 0.6875f, 0.5125f, 0.9375f, 0.625f, 0.7625f,
		0.3125f, 0.4625f, 0.8125f, 0.8625f, 0.775f, 0.5875f, 0.825f, 1f, 0.975f,
		0.7f, 0.0875f, 0.5625f, 0.875f, 0.675f, 0.2375f, 0.1375f, 0.7375f, 0.7125f,
		0.85f, 0.575f, 0.9f, 0.2125f, 0.3875f, 0.5f, 0.1125f, 0.45f, 0.8375f,
		0.175f, 0.9875f, 0.225f, 0.6125f, 0.3f, 0.1f, 0.25f, 0.375f, 0.7875f,
		0.725f, 0.4875f, 0.9625f, 0.2625f, 0.95f, 0.6f, 0.925f, 0.0375f, 0.475f,
		0.325f, 0.35f, 0.425f, 0.6625f, 0.025f, 0.5375f, 0.4375f, 0.3625f, 0.15f,
		0.0125f, 0.4f, 0.3375f, 0.125f, 0.65f, 0.275f, 0.55f, 0.75f, 0.525f,
		0.9125f, 0.1875f, 0.075f, 0.2f, 0.1625f, 0.0625f, 0.2875f, 0f, 0.8875f
	};
	
	public static float[] colorFor(SpellPiece piece, Side side) {
		return idToColor(colorIdFor(piece, side));
	}
	
	private static float[] idToColor(int color) {
		return new float[] {
			PsiRenderHelper.r(color) / 255f,
			PsiRenderHelper.g(color) / 255f,
			PsiRenderHelper.b(color) / 255f
		};
	}
	
	private static int colorIdForPosition(int x, int y) {
		return (SpellGrid.exists(x, y)) ? Color.HSBtoRGB(ConnectorColor.hues[x + y * 9], 0.8f, 1) : defaultColor;
	}
	
	private static final int noOverride = -1;
	
	private static int overrideFor(SpellPiece piece) {
		return PieceAnnotation.outColor(piece).orElse(noOverride);
	}
	
	public static int colorIdFor(SpellPiece piece, Side side) {
		if (!piece.isInGrid) return defaultColor;
		// Walk the path to the target
		// We have to do this so we can get the position at the end of a redirection chain even if it leads to an empty cell
		// The first piece in the chain with a color override takes precedence over later overrides and position based colors
		int[] t = new int[] { piece.x + side.offx, piece.y + side.offy, noOverride };
		try {
			SpellPiece target = piece.spell.grid.getPieceAtSideWithRedirections(piece.x, piece.y, side, o -> {
				if (t[2] != noOverride) return;
				t[0] = o.x;
				t[1] = o.y;
				t[2] = overrideFor(o);
			});
			if (t[2] != noOverride) return t[2];
			if (target != null) {
				// We arrived at a piece
				int override = overrideFor(target);
				if (override != noOverride) return override;
				t[0] = target.x;
				t[1] = target.y;
			} else if (SpellGrid.exists(t[0], t[1]) && piece.spell.grid.gridData[t[0]][t[1]] instanceof IRedirector) {
				// A connector lead to an empty cell
				side = ((IRedirector) piece.spell.grid.gridData[t[0]][t[1]]).getRedirectionSide();
				t[0] += side.offx;
				t[1] += side.offy;
			}
			// TODO support IGenericRedirector leading to an empty cell
			// We reached the end of the chain without a color override
			return colorIdForPosition(t[0], t[1]);
		} catch (SpellCompilationException e) {
			// Allow color overrides even in loops
			if (t[2] != noOverride) return t[2];
			// We're likely in a loop
			return errorColor;
		}
	}
	
}
