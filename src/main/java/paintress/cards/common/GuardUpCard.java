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

public class GuardUpCard extends AbstractPaintressCard {
    public static final String ID = makeID("GuardUp");

    public GuardUpCard() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 9;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        applyToSelf(new PlatedArmorPower(adp(), 2));
    }

    @Override
    public void upp() {
        upgradeBlock(4);
    }
}
