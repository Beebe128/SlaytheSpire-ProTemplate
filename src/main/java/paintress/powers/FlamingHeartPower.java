package paintress.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.applyToSelf;

/**
 * Flaming Heart — You cannot die while below 10 HP. When you survive a lethal hit,
 * gain N Gradient Charges. (One-time trigger per combat.)
 */
public class FlamingHeartPower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("FlamingHeart");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean triggered = false;

    public FlamingHeartPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (!triggered && info.type == DamageInfo.DamageType.NORMAL) {
            int resultingHP = owner.currentHealth - damageAmount;
            if (resultingHP <= 0 && owner.currentHealth > 0) {
                // Would be lethal — survive with 1 HP
                triggered = true;
                applyToSelf(new GradientChargePower(owner, amount));
                return owner.currentHealth - 1; // take damage down to 1 HP
            }
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
