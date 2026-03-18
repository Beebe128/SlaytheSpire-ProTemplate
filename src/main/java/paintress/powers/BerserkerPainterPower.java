package paintress.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.atb;
import static paintress.util.Wiz.applyToEnemy;

/**
 * Berserker Painter — Attacking applies 1 Burn to target.
 * Also: deal N more damage per Burn stack on the target.
 */
public class BerserkerPainterPower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("BerserkerPainter");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public BerserkerPainterPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type == DamageInfo.DamageType.NORMAL && target instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster) target;
            if (!m.isDeadOrEscaped()) {
                applyToEnemy(m, new BurnPower(m, owner, 1));
            }
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        // Bonus damage equal to amount × burn stacks on target
        // Note: We can't get target here, so we apply a flat bonus per Burn on ANY enemy
        // For simplicity: check if any enemy has Burn and add bonus
        if (type == DamageInfo.DamageType.NORMAL) {
            int totalBurn = 0;
            for (AbstractMonster m : com.megacrit.cardcrawl.dungeons.AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped()) {
                    totalBurn = Math.max(totalBurn, com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasPower(BurnPower.POWER_ID) ? 0
                            : m.hasPower(BurnPower.POWER_ID) ? m.getPower(BurnPower.POWER_ID).amount : 0);
                }
            }
            return damage + (amount * Math.min(totalBurn, 5)); // cap at 5 stacks for balance
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
