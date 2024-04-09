package gdavid.psionicutilities;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import vazkii.psi.api.spell.SpellPiece;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class PieceAnnotation {
	
	private static final Set<String> annotations = Set.of("color", "out-color");
	
	public static Optional<Integer> color(SpellPiece piece) {
		return getColor(piece, "color");
	}
	public static Optional<Integer> outColor(SpellPiece piece) {
		return getColor(piece, "out-color");
	}
	
	private static boolean isAnnotation(String ln) {
		return annotations.stream().anyMatch(an -> ln.startsWith("@" + an + "="));
	}
	
	public static boolean hasComment(SpellPiece piece) {
		return !commentLines(piece).allMatch(PieceAnnotation::isAnnotation);
	}
	
	public static void filterComment(List<Component> text) {
		text.removeIf(ln -> isAnnotation(ln.getString()));
	}
	
	private static Stream<String> commentLines(SpellPiece piece) {
		return piece.comment == null ? Stream.empty() : Arrays.stream(piece.comment.split(";"));
	}
	
	public static Stream<String[]> annotations(SpellPiece piece) {
		return commentLines(piece).filter(PieceAnnotation::isAnnotation).map(ln -> ln.substring(1).split("=", 2));
	}
	
	public static Optional<String> get(SpellPiece piece, String annotation) {
		return annotations(piece).filter(kv -> kv[0].equals(annotation)).findFirst().map(kv -> kv[1]);
	}
	
	public static Optional<Integer> getColor(SpellPiece piece, String annotation) {
		return get(piece, annotation).map(c -> Optional.ofNullable(TextColor.parseColor(c)).map(TextColor::getValue).orElse(ConnectorColor.errorColor));
	}
	
}
