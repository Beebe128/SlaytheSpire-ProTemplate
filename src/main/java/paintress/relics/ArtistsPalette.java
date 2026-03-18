package paintress.relics;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paintress.Paintress;
import paintress.powers.GradientChargePower;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.applyToSelf;

/**
 * Starter Relic: Artist's Palette
 * "At the start of each combat, gain 2 Gradient Charges."
 * Sets up Maelle's Gradient mechanic from the first turn.
 */
public class ArtistsPalette extends AbstractPaintressRelic {
    public static final String ID = makeID("ArtistsPalette");

    public ArtistsPalette() {
        super(ID, RelicTier.STARTER, LandingSound.FLAT, Paintress.Enums.PAINTRESS_COLOR);
    }

    @Override
    public void atBattleStart() {
        flash();
        applyToSelf(new GradientChargePower(AbstractDungeon.player, 1));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
