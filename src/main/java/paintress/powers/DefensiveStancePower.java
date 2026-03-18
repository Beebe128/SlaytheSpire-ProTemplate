package paintress.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paintress.PaintressMod;

import static paintress.PaintressMod.makeID;

/**
 * Defensive Stance — Maelle takes 30% less damage and gains 2 Block when playing a 0-cost card (parry).
 * Entering a new stance draws 1 card (AP gain equivalent).
 * Non-turn-based: persists until another stance is entered or a specific action removes it.
 */
public class DefensiveStancePower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("DefensiveStance");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public DefensiveStancePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    /** Reduce incoming damage by 30% (floored) */
    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * 0.70f;
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void stackPower(int stackAmount) {
        // Do not stack; just ensure active
        this.amount = 1;
    }
}
