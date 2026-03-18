package paintress.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class VirtuosePrep extends AbstractPaintressCard {
    public static final String ID = makeID("VirtuosePrep");

    public VirtuosePrep() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 5;
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean wasOffensive = isInOffensive();
        blck();
        gainGradient(magicNumber);
        enterVirtuose();
        if (wasOffensive) {
            atb(new DrawCardAction(1));
        }
    }

    @Override
    public void upp() {
        upgradeBlock(3);
        upgradeMagicNumber(1);
    }
}
