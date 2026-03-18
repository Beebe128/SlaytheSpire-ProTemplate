package paintress.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import paintress.cards.AbstractPaintressCard;
import paintress.powers.*;
import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class GustaveHomage extends AbstractPaintressCard {
    public static final String ID = makeID("GustaveHomage");

    public GustaveHomage() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(actionify(() -> {
            if (!adp().discardPile.isEmpty()) {
                AbstractCard c = getRandomItem(adp().discardPile.group);
                if (c != null) {
                    adp().discardPile.removeCard(c);
                    c.costForTurn = 0;
                    c.isCostModified = true;
                    adp().hand.addToTop(c);
                    adp().hand.refreshHandLayout();
                }
            }
        }));
        if (this.upgraded) {
            atb(new DrawCardAction(adp(), 1));
        }
    }

    @Override
    public void upp() {
        uDesc();
    }
}
