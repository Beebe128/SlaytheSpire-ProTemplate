package paintress.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class BurningFlurry extends AbstractPaintressCard {
    public static final String ID = makeID("BurningFlurry");

    public BurningFlurry() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 5;
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            dmg(m, AttackEffect.FIRE);
            applyBurn(m, 1);
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
    }
}
