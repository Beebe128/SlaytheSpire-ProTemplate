package paintress.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static paintress.PaintressMod.makeID;

/**
 * Painted Power — Each attack deals N bonus damage per Burn stack on the target.
 * Inspired by Expedition 33's Painted Power Picto which removes the damage cap.
 * STS translation: significant damage amplifier based on Burn synergy.
 */
public class PaintedPowerPower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("PaintedPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public PaintedPowerPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        // Applied as damage bonus via atDamageGive - the hook here is for potential future effects
    }

    /** For each Burn stack on the target, deal bonus damage.
     *  This requires checking target at damage time.
     *  We use atDamageFinalGive with a simplified approach: flat bonus per Burn stacks overall. */
    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            // Add N damage per Burn stack on all enemies (we take the max for the primary target)
            int maxBurn = 0;
            for (AbstractMonster m : com.megacrit.cardcrawl.dungeons.AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped() && m.hasPower(BurnPower.POWER_ID)) {
                    maxBurn = Math.max(maxBurn, m.getPower(BurnPower.POWER_ID).amount);
                }
            }
            return damage + (amount * Math.min(maxBurn, 10));
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
