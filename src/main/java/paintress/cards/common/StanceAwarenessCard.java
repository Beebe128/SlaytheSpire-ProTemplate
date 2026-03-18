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

public class StanceAwarenessCard extends AbstractPaintressCard {
    public static final String ID = makeID("StanceAwarenessCard");

    public StanceAwarenessCard() {
        super(ID, 1, CardType.POWER, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            applyToSelf(new StanceAwarenessPower(adp(), 1, true));
        } else {
            applyToSelf(new StanceAwarenessPower(adp(), 1, false));
        }
    }

    @Override
    public void upp() {
        // Upgrade changes the power's giveEnergy flag from false to true.
        // uDesc() is called automatically by AbstractPaintressCard.upgrade().
    }
}
