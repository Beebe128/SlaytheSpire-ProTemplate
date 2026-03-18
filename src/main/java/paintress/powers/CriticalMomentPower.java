package paintress.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static paintress.PaintressMod.makeID;

/**
 * Critical Moment — Once per turn, your first attack deals N extra damage.
 * From Expedition 33: Critical Burn Lumina gives +25% crit with Burn.
 * STS translation: first attack per turn deals bonus damage.
 */
public class CriticalMomentPower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("CriticalMoment");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean usedThisTurn = false;

    public CriticalMomentPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        usedThisTurn = false;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (!usedThisTurn && type == DamageInfo.DamageType.NORMAL) {
            usedThisTurn = true;
            return damage + amount;
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
