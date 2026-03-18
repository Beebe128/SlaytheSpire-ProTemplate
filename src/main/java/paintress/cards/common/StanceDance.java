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

public class StanceDance extends AbstractPaintressCard {
    public static final String ID = makeID("StanceDance");

    public StanceDance() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        final boolean blockBonus = upgraded;
        atb(actionify(() -> {
            if (isInDefensive()) {
                clearStances();
                applyToSelf(new OffensiveStancePower(adp(), 1));
            } else if (isInOffensive()) {
                clearStances();
                applyToSelf(new VirtuoseStancePower(adp(), 1));
            } else if (isInVirtuose()) {
                clearStances();
                // Go stanceless — no new stance applied
            } else {
                // Stanceless → enter Defensive
                applyToSelf(new DefensiveStancePower(adp(), 1));
            }
        }));
        atb(actionify(() -> adp().drawCards(1)));
        if (blockBonus) {
            atb(new GainBlockAction(adp(), adp(), 2));
        }
    }

    @Override
    public void upp() {
        // Upgraded: also gain 2 Block when cycling stances
        // The upgraded flag is checked at runtime; uDesc() is called automatically
    }
}
