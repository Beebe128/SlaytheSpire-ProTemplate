package paintress.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static paintress.PaintressMod.makeID;
import static paintress.cards.AbstractPaintressCard.*;

/**
 * Parry Status — When an enemy attack is fully blocked by your shields,
 * deal damage back to the attacker based on your current stance.
 *
 *   No stance / Defensive : 5% of attacker's max HP per stack
 *   Offensive              : 10% of attacker's max HP per stack
 *   Virtuose               : 15% of attacker's max HP per stack
 */
public class ParryStatusPower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("ParryStatus");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ParryStatusPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        // Only react to normal enemy attacks that were fully blocked (0 HP damage)
        if (info.type == DamageInfo.DamageType.NORMAL
                && info.owner instanceof AbstractMonster
                && damageAmount == 0
                && AbstractDungeon.player.currentBlock > 0) {

            AbstractMonster attacker = (AbstractMonster) info.owner;
            if (!attacker.isDeadOrEscaped()) {
                float pct;
                if (isInVirtuose())       pct = 0.15f;
                else if (isInOffensive()) pct = 0.10f;
                else                      pct = 0.05f; // Defensive or no stance

                int dmg = Math.max(1, (int)(attacker.maxHealth * pct * amount));

                AbstractDungeon.actionManager.addToBottom(
                        new DamageAction(attacker,
                                new DamageInfo(owner, dmg, DamageInfo.DamageType.THORNS),
                                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                flash();
            }
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
