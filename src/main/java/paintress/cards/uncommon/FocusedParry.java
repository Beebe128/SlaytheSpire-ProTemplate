package paintress.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class FocusedParry extends AbstractPaintressCard {
    public static final String ID = makeID("FocusedParry");

    public FocusedParry() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 8;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean wasDefensive = isInDefensive();
        blck();
        enterDefensive();
        if (wasDefensive) {
            atb(new GainEnergyAction(1));
        }
    }

    @Override
    public void upp() {
        upgradeBlock(4);
    }
}
