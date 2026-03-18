package paintress.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static paintress.PaintressMod.makeID;

/**
 * Energizing Parry — Whenever you play a 0-cost card, draw 1 card.
 * This represents Maelle's Defensive Stance AP generation from parrying.
 * Tracked via AbstractPower's onCardDraw hook (approximate - uses atEndOfTurn trick).
 *
 * In STS, we use the onCardUse/atEndOfTurnPreEndTurnCards pattern.
 * Actually using BaseMod's onAfterCardUsed subscriber would be best, but
 * for simplicity: at end of turn, draw 1 for each 0-cost card played this turn (tracked via counter).
 *
 * Simpler approach: track zero-cost cards per turn, draw at end.
 */
public class EnergizingParryPower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("EnergizingParry");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int zeroCostCardsThisTurn = 0;

    public EnergizingParryPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        zeroCostCardsThisTurn = 0;
    }

    /** Called by AbstractPaintressCard cards that cost 0 */
    public void onPlayZeroCostCard() {
        zeroCostCardsThisTurn++;
        if (zeroCostCardsThisTurn <= amount) { // draw up to N times per 0-cost card
            AbstractDungeon.player.drawCards(1);
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
