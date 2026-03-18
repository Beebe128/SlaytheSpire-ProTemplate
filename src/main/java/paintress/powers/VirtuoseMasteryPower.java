package paintress.powers;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.atb;

/**
 * Virtuose Mastery — While in Virtuose Stance, your attacks hit twice.
 * (Second hit has reduced multiplier to prevent tripling tripling).
 * The second hit deals 50% of the original damage.
 */
public class VirtuoseMasteryPower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("VirtuoseMastery");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final boolean drawOnSecondHit;
    private boolean midAttack = false; // prevent recursive double-hit

    public VirtuoseMasteryPower(AbstractCreature owner, int amount, boolean drawOnSecondHit) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
        this.drawOnSecondHit = drawOnSecondHit;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (!midAttack && owner.hasPower(VirtuoseStancePower.POWER_ID)
                && info.type == DamageInfo.DamageType.NORMAL
                && target instanceof AbstractMonster
                && !((AbstractMonster) target).isDeadOrEscaped()
                && damageAmount > 0) {
            midAttack = true;
            int secondDmg = Math.max(1, damageAmount / 2);
            atb(new DamageAction(target,
                    new DamageInfo(owner, secondDmg, DamageInfo.DamageType.NORMAL),
                    com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            if (drawOnSecondHit) {
                atb(new DrawCardAction(AbstractDungeon.player, 1));
            }
            midAttack = false;
        }
    }

    @Override
    public void updateDescription() {
        description = drawOnSecondHit ? DESCRIPTIONS[1] : DESCRIPTIONS[0];
    }
}
