package paintress.cards.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.cards.AbstractPaintressCard;
import paintress.powers.BerserkerPainterPower;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class RelentlessAssault extends AbstractPaintressCard {
    public static final String ID = makeID("RelentlessAssault");

    public RelentlessAssault() {
        super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new BerserkerPainterPower(adp(), magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
