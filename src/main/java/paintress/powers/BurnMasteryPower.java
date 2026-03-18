package paintress.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static paintress.PaintressMod.makeID;

/**
 * Burn Mastery — Enemies with Burn deal N less damage.
 * Defensive synergy with Maelle's Burn-application kit.
 */
public class BurnMasteryPower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("BurnMastery");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public BurnMasteryPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        // Check if the source has Burn - reduce incoming damage
        // This hook doesn't pass the attacker directly, so we reduce damage if any enemy has Burn
        // Best approach: check all monsters for Burn
        if (type == DamageInfo.DamageType.NORMAL) {
            for (AbstractMonster m : com.megacrit.cardcrawl.dungeons.AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped() && m.hasPower(BurnPower.POWER_ID)) {
                    return Math.max(0f, damage - amount);
                }
            }
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
