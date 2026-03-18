package paintress.powers;

import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.atb;

/**
 * Virtuose Flow — At start of turn, if in Virtuose Stance, deal N damage to all enemies.
 * Rewards maintaining Virtuose Stance through multiple turns.
 */
public class VirtuoseFlowPower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("VirtuoseFlow");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public VirtuoseFlowPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        if (owner.hasPower(VirtuoseStancePower.POWER_ID)) {
            int[] multiDmg = new int[AbstractDungeon.getMonsters().monsters.size()];
            for (int i = 0; i < multiDmg.length; i++) multiDmg[i] = amount;
            atb(new DamageAllEnemiesAction(owner, multiDmg, DamageInfo.DamageType.THORNS,
                    com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
