package paintress.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import paintress.PaintressMod;

import static paintress.PaintressMod.makeID;

/**
 * Offensive Stance — Maelle deals 50% more damage, but takes 50% more damage.
 * Non-turn-based: persists until stance changes.
 */
public class OffensiveStancePower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("OffensiveStance");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public OffensiveStancePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    /** Deal 50% more damage */
    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * 1.50f;
        }
        return damage;
    }

    /** Take 50% more damage */
    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * 1.50f;
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount = 1;
    }
}
