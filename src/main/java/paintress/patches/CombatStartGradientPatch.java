package paintress.patches;

/**
 * Stub — gradient gain at combat start is handled directly by
 * ArtistsPalette (starter relic) via AbstractRelic.atBattleStart(),
 * which is the only reliable combat-start hook in this STS build.
 */
public class CombatStartGradientPatch {
    // See ArtistsPalette.atBattleStart()
}
