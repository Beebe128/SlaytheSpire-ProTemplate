package paintress.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import paintress.cards.AbstractPaintressCard;
import paintress.powers.*;
import paintress.relics.ExpeditionJournal;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class FlameWhip extends AbstractPaintressCard {
    public static final String ID = makeID("FlameWhip");

    public FlameWhip() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AttackEffect.FIRE);
        int burnConsumed = Math.min(getBurnStacks(m), 5);
        int extraDmg = burnConsumed * magicNumber;
        final int fExtra = extraDmg;
        final int fConsumed = burnConsumed;
        atb(actionify(() -> {
            if (fExtra > 0) {
                att(new DamageAction(m, new DamageInfo(adp(), fExtra, DamageInfo.DamageType.NORMAL), AttackEffect.FIRE));
            }
            if (!m.isDeadOrEscaped() && fConsumed > 0 && m.hasPower(BurnPower.POWER_ID)) {
                AbstractPower p2 = m.getPower(BurnPower.POWER_ID);
                p2.amount -= fConsumed;
                if (p2.amount <= 0) {
                    m.powers.remove(p2);
                } else {
                    p2.updateDescription();
                }
                ExpeditionJournal.onBurnConsumed();
            }
        }));
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
    }
}
