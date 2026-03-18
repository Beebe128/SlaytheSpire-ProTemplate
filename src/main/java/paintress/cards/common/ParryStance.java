package paintress.cards.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class ParryStance extends AbstractPaintressCard {
    public static final String ID = makeID("ParryStance");

    public ParryStance() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 6;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        enterDefensive();
    }

    @Override
    public void upp() {
        upgradeBlock(3);
    }
}
