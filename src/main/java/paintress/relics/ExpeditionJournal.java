package paintress.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paintress.Paintress;

import static paintress.PaintressMod.makeID;

/**
 * Uncommon Relic: Expedition Journal
 * "Whenever you consume Burn stacks (Burning Canvas, Combustion, Full Combustion),
 *  gain 1 Energy."
 * The mod tracks this via a static flag on the relic.
 */
public class ExpeditionJournal extends AbstractPaintressRelic {
    public static final String ID = makeID("ExpeditionJournal");

    /** Set to true by Burning Canvas / Combustion / Full Combustion before consuming Burn */
    public static boolean consumingBurnThisAction = false;

    public ExpeditionJournal() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT, Paintress.Enums.PAINTRESS_COLOR);
    }

    /** Called by burn-consuming cards after consuming */
    public static void onBurnConsumed() {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ID)) {
            ExpeditionJournal relic = (ExpeditionJournal) AbstractDungeon.player.getRelic(ID);
            relic.flash();
            AbstractDungeon.player.energy.recharge(1);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
