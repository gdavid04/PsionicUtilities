package gdavid.psionicutilities.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellGrid;
import vazkii.psi.api.spell.SpellParam.Side;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.spell.other.PieceConnector;

import static net.minecraft.client.gui.screens.Screen.hasControlDown;

@Mixin(GuiProgrammer.class)
public abstract class ProgrammerGuiMixin {
	
	@Shadow public int gridTop, gridLeft;
	
	@Shadow public int cursorX, cursorY;
	
	@Shadow public Spell spell;
	
	@Shadow public boolean commentEnabled;
	
	@Shadow public abstract void onSpellChanged(boolean nameOnly);
	
	@Shadow public static int selectedX, selectedY;
	
	@Shadow @Final public TileProgrammer programmer;
	private int dragX, dragY;
	private boolean isMoving;
	
	@Inject(method = "mouseClicked", at = @At("TAIL"))
	private void dragStart(double mx, double my, int button, CallbackInfoReturnable<Boolean> callback) {
		if (commentEnabled) return;
		if (button == 0) {
			dragX = cursorX;
			dragY = cursorY;
			isMoving = hasControlDown();
		}
	}
	
	@Inject(method = "mouseMoved", at = @At("HEAD"), remap = false)
	private void dragActions(double mx, double my, CallbackInfo callback) {
		if (commentEnabled) return;
		if (Minecraft.getInstance().mouseHandler.activeButton != 0) return;
		int newX = ((int) mx - gridLeft) / 18, newY = ((int) my - gridTop) / 18;
		if (newX == dragX && newY == dragY) return;
		if (programmer != null) spell = programmer.spell;
		if (isMoving) movePiece(dragX, dragY, newX, newY);
		else drawConnectorChain(dragX, dragY, newX, newY);
	}
	
	private void movePiece(int x1, int y1, int x2, int y2) {
		if (!rangeCheck(x1, y1) || !rangeCheck(x2, y2)) return;
		if (spell.grid.gridData[x2][y2] != null) return;
		var piece = spell.grid.gridData[x1][y1];
		if (piece == null) return;
		spell.grid.gridData[x1][y1] = null;
		spell.grid.gridData[x2][y2] = piece;
		dragX = selectedX = piece.x = x2;
		dragY = selectedY = piece.y = y2;
		onSpellChanged(false);
	}
	
	private void drawConnectorChain(int x1, int y1, int x2, int y2) {
		if (!rangeCheck(x1, y1) || !rangeCheck(x2, y2)) return;
		int x = x1, y = y1;
		while (!(x == x2 && y == y2)) {
			if (x < x2) insertConnectorSilent(++x, y, Side.LEFT);
			else if (x > x2) insertConnectorSilent(--x, y, Side.RIGHT);
			else if (y < y2) insertConnectorSilent(x, ++y, Side.TOP);
			else insertConnectorSilent(x, --y, Side.BOTTOM);
		}
		dragX = selectedX = x2;
		dragY = selectedY = y2;
		onSpellChanged(false);
	}
	
	// onSpellChanged(false); must be called afterwards
	private void insertConnectorSilent(int x, int y, Side side) {
		if (spell.grid.gridData[x][y] != null) return;
		PieceConnector connector = new PieceConnector(spell);
		connector.x = x;
		connector.y = y;
		connector.paramSides.put(connector.target, side);
		spell.grid.gridData[x][y] = connector;
	}
	
	private boolean rangeCheck(int x, int y) {
		return x >= 0 && x < SpellGrid.GRID_SIZE && y >= 0 && y < SpellGrid.GRID_SIZE;
	}

}
