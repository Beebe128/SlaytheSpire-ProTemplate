package paintress.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class Spark extends AbstractPaintressCard {
    public static final String ID = makeID("Spark");

    public Spark() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 5;
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AttackEffect.FIRE);
        enterDefensive();
        applyBurn(m, magicNumber);
        if (isInOffensiveOrVirtuose()) {
            applyBurn(m, 2);
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
