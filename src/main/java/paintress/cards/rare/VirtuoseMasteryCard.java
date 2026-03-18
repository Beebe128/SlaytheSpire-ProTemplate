package paintress.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import paintress.cards.AbstractPaintressCard;
import paintress.powers.*;
import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class VirtuoseMasteryCard extends AbstractPaintressCard {
    public static final String ID = makeID("VirtuoseMastery");

    public VirtuoseMasteryCard() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // When upgraded, the power draws 1 card after each second hit in Virtuose Stance
        applyToSelf(new VirtuoseMasteryPower(adp(), 1, this.upgraded));
    }

    @Override
    public void upp() {
        uDesc();
    }
}
