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

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class RainOfFire extends AbstractPaintressCard {
    public static final String ID = makeID("RainOfFire");

    public RainOfFire() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 8;
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int extraBurn = isInDefensive() ? 2 : 0;
        atb(new DamageAction(m, new DamageInfo(adp(), damage, damageTypeForTurn), AttackEffect.FIRE));
        applyBurn(m, magicNumber + extraBurn);
        atb(new DamageAction(m, new DamageInfo(adp(), damage, damageTypeForTurn), AttackEffect.FIRE));
        applyBurn(m, magicNumber + extraBurn);
        enterOffensive();
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
    }
}
