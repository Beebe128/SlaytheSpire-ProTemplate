package paintress.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static paintress.PaintressMod.makeID;

/**
 * Gradient Charge — Builds toward Maelle's powerful Gradient attacks.
 * Gained by Phantom Strike, Void Slash, Gradient Rush, and certain relics.
 * Spent by Gommage (needs 6), Grand Finale (spends all), Last Resort, etc.
 * Does not expire between turns; persists through combat.
 * Max 15 stacks (enough for Gommage and Grand Finale).
 */
public class GradientChargePower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("GradientCharge");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static final int MAX_STACKS = 3;

    public GradientChargePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount = Math.min(this.amount + stackAmount, MAX_STACKS);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        // Charges persist between turns — no expiry
    }
}
