package paintress.powers;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.atb;

/**
 * Augmented Counter — After taking damage from an enemy, deal N damage back (counterattack).
 */
public class AugmentedCounterPower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("AugmentedCounter");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public AugmentedCounterPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL && info.owner != null && info.owner instanceof AbstractMonster) {
            AbstractMonster attacker = (AbstractMonster) info.owner;
            if (!attacker.isDeadOrEscaped()) {
                atb(new DamageAction(attacker,
                        new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS),
                        com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
