package gdavid.psionicutilities.mixin;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.spell.constant.PieceConstantNumber;

@Mixin(value = PieceConstantNumber.class, remap = false)
public abstract class NumberConstantMixin extends SpellPiece {
	
	@Shadow public String valueStr;
	
	private NumberConstantMixin(Spell spell) {
		super(spell);
	}
	
	// Phi style number constant editing
	// TODO: consider supporting exponent editing
	
	@Overwrite public boolean onCharTyped(char ch, int keyCode, boolean doit) {
		if ("FDfd".indexOf(ch) != -1) return false;
		String tmp = valueStr;
		if ((tmp.equals("0") || tmp.equals("-0")) && ch != '-' && ch != '+') tmp = tmp.replace("0", "");
		if (ch == '+') {
			if (tmp.startsWith("-")) tmp = tmp.substring(1);
		} else if (ch == '-') {
			if (!tmp.startsWith("-")) tmp = "-" + tmp;
		} else tmp = (tmp + ch).trim();
		if (tmp.length() > 5) {
			if (tmp.startsWith("0.")) tmp = tmp.substring(1);
			else if (tmp.startsWith("-0.")) tmp = "-" + tmp.substring(2);
		} else {
			if (tmp.startsWith(".")) tmp = "0" + tmp;
			else if (tmp.startsWith("-.")) tmp = "-0" + tmp.substring(1);
		}
		if (tmp.length() > 5) return false;
		if (tmp.isEmpty()) tmp = "0";
		try {
			Double.parseDouble(tmp);
		} catch (NumberFormatException e) {
			return false;
		}
		if (doit) valueStr = tmp;
		return true;
	}
	
	@Overwrite public boolean onKeyPressed(int key, int scanCode, boolean doit) {
		if (valueStr.length() == 0) return false;
		if (key == GLFW.GLFW_KEY_BACKSPACE) {
			if (doit) {
				String tmp = valueStr;
				if (tmp.startsWith(".")) tmp = "0" + tmp;
				else if (tmp.startsWith("-.")) tmp = "-0" + tmp.substring(1);
				if (tmp.equals("-0")) tmp = "0";
				else if (tmp.startsWith("-") && tmp.length() == 2) tmp = "-0";
				else tmp = tmp.substring(0, tmp.length() - 1).trim();
				if (tmp.isEmpty()) tmp = "0";
				try {
					Double.parseDouble(tmp);
				} catch (NumberFormatException e) {
					return false;
				}
				valueStr = tmp;
			}
			return true;
		}
		return false;
	}
	
}
