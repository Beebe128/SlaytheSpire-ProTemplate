package paintress.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paintress.cards.AbstractPaintressCard;
import paintress.powers.BurnPower;
import paintress.relics.ExpeditionJournal;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class CombustionStrike extends AbstractPaintressCard {
    public static final String ID = makeID("CombustionStrike");

    public CombustionStrike() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 10;
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int[] burnRef = {getBurnStacks(m)};
        dmg(m, AttackEffect.FIRE);
        atb(actionify(() -> {
            int bonus = Math.min(burnRef[0], 10) * magicNumber;
            if (bonus > 0) {
                att(new DamageAction(m, new DamageInfo(adp(), bonus, damageTypeForTurn), AttackEffect.FIRE));
            }
            if (!m.isDeadOrEscaped() && m.hasPower(BurnPower.POWER_ID)) {
                AbstractPower pw = m.getPower(BurnPower.POWER_ID);
                int consume = Math.min(pw.amount, 10);
                pw.amount -= consume;
                if (pw.amount <= 0) {
                    m.powers.remove(pw);
                } else {
                    pw.updateDescription();
                }
                ExpeditionJournal.onBurnConsumed();
            }
        }));
    }

    @Override
    public void upp() {
        upgradeDamage(3);
        upgradeMagicNumber(1);
    }
}
