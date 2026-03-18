package paintress.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static paintress.PaintressMod.makeID;

/**
 * Virtuose Stance — Maelle deals 200% more damage (triple damage total).
 * The pinnacle of Maelle's combat form.
 * Non-turn-based: persists until stance changes.
 */
public class VirtuoseStancePower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("VirtuoseStance");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public VirtuoseStancePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    /** Deal 200% more damage (3× total) */
    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * 3.0f;
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
