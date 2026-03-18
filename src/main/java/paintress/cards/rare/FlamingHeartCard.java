package paintress.cards.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.cards.AbstractPaintressCard;
import paintress.powers.FlamingHeartPower;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class FlamingHeartCard extends AbstractPaintressCard {
    public static final String ID = makeID("FlamingHeart");

    public FlamingHeartCard() {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 5;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new FlamingHeartPower(adp(), magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(3);
    }
}
