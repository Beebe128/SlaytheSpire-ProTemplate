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
 * Burn Affinity — Whenever Burn is applied to an enemy (via BurnPower.stackPower on a new application),
 * deal N damage to that enemy.
 * Tracked via: this power on player; BurnPower calls back via AbstractDungeon.player check.
 * Simpler implementation: at start of each turn, if any enemy has Burn, deal N damage.
 */
public class BurnAffinityPower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("BurnAffinity");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public BurnAffinityPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        // At start of turn, for each burning enemy, deal N damage
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped() && m.hasPower(BurnPower.POWER_ID)) {
                atb(new DamageAction(m,
                        new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS),
                        com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect.FIRE));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
