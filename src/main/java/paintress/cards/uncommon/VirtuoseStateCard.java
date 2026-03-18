package paintress.cards.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.cards.AbstractPaintressCard;
import paintress.powers.StanceAwarenessPower;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class VirtuoseStateCard extends AbstractPaintressCard {
    public static final String ID = makeID("VirtuoseState");

    public VirtuoseStateCard() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new StanceAwarenessPower(adp(), 1, upgraded));
    }

    @Override
    public void upp() {
        // upgraded flag is already handled in use() via StanceAwarenessPower(adp(), 1, upgraded)
    }
}
