package gdavid.psionicutilities.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.nbt.CompoundNBT;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorExtractY;

@Mixin(value = PieceOperatorVectorExtractY.class, remap = false)
public abstract class ExtractYMixin extends SpellPiece {
	
	private ExtractYMixin(Spell spell) {
		super(spell);
	}
	
	@Override public boolean interceptKeystrokes() { return true; }
	
	@Override public boolean onCharTyped(char character, int keyCode, boolean doit) {
		char ch = Character.toLowerCase(character);
		if (ch < 'x' || ch > 'z') return false;
		if (doit && ch != 'y') {
			CompoundNBT nbt = new CompoundNBT();
			writeToNBT(nbt);
			nbt.putString("key", "psi:operator_vector_extract_" + ch);
			spell.grid.gridData[x][y] = SpellPiece.createFromNBT(spell, nbt);
		}
		return true;
	}
	
}
