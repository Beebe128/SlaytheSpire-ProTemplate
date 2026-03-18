package paintress.cards.common;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class Preparation extends AbstractPaintressCard {
    public static final String ID = makeID("Preparation");

    public Preparation() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        gainGradient(magicNumber);
        atb(new DrawCardAction(1));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
