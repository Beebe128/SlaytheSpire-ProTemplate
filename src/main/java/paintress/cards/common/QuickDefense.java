package paintress.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import paintress.cards.AbstractPaintressCard;
import paintress.powers.*;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class QuickDefense extends AbstractPaintressCard {
    public static final String ID = makeID("QuickDefense");

    public QuickDefense() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 3;
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        if (isInDefensive()) {
            atb(new GainBlockAction(adp(), adp(), magicNumber));
        }
    }

    @Override
    public void upp() {
        upgradeBlock(2);
        upgradeMagicNumber(2);
    }
}
