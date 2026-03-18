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

public class MezzoForteCard extends AbstractPaintressCard {
    public static final String ID = makeID("MezzoForte");

    public MezzoForteCard() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(actionify(() -> {
            if (isInDefensive()) {
                applyToSelf(new DefensiveStancePower(adp(), 1));
            } else if (isInOffensive()) {
                applyToSelf(new OffensiveStancePower(adp(), 1));
            } else if (isInVirtuose()) {
                applyToSelf(new VirtuoseStancePower(adp(), 1));
            }
        }));
        atb(actionify(() -> att(new DrawCardAction(adp(), magicNumber))));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
