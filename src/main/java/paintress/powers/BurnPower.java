package paintress.powers;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.atb;

/**
 * Burn — Stacking fire damage debuff on enemies.
 * At the start of the enemy's turn: lose HP equal to stacks (ignores Block).
 * Stacks reduce by 3 each time they trigger (fire burns out).
 * Applied by Spark, Rain of Fire, Pyrolyse, Phantom Strike, and others.
 */
public class BurnPower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("Burn");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final AbstractCreature source;

    public BurnPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, NAME, PowerType.DEBUFF, true, owner, amount);
        this.source = source;
        this.isTurnBased = false; // We manage tick-down manually
        priority = 5;
    }

    @Override
    public void atStartOfTurn() {
        // Deal damage equal to stacks (unblockable fire damage)
        if (amount > 0) {
            atb(new DamageAction(owner,
                    new DamageInfo(source, amount, DamageInfo.DamageType.THORNS),
                    com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect.FIRE));
            // Reduce stacks by 3
            this.amount -= 3;
            if (this.amount <= 0) {
                atb(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(owner, owner, this));
            }
            updateDescription();
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
