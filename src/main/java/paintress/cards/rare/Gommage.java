package paintress.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

/**
 * Gommage (French: "to erase") — Maelle erases a foe from existence.
 * Costs 0 energy but requires 3 Gradient Charges.
 *
 * vs normal/elite: Instantly kills the target.
 * vs boss:         Deals 20% of the boss's max HP as damage.
 */
public class Gommage extends AbstractPaintressCard {
    public static final String ID = makeID("Gommage");

    public Gommage() {
        super(ID, 0, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        gradientCost = 3;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        spendGradient(gradientCost);
        atb(actionify(() -> {
            if (m == null || m.isDeadOrEscaped()) return;
            if (m.type == AbstractMonster.EnemyType.BOSS) {
                // Deal 20% of boss max HP as damage
                int dmg = Math.max(1, (int)(m.maxHealth * 0.20f));
                att(new DamageAction(m,
                        new DamageInfo(adp(), dmg, DamageInfo.DamageType.NORMAL),
                        AttackEffect.FIRE));
            } else {
                // Instant kill for non-boss enemies
                int killDmg = m.currentHealth + m.currentBlock + 9999;
                att(new DamageAction(m,
                        new DamageInfo(adp(), killDmg, DamageInfo.DamageType.NORMAL),
                        AttackEffect.FIRE));
            }
        }));
    }

    @Override
    public void upp() {
        // Upgraded: boss damage increases to 30%
        isInnate = true;
    }
}
