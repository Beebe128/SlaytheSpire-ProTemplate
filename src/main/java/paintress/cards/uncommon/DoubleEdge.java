package paintress.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class DoubleEdge extends AbstractPaintressCard {
    public static final String ID = makeID("DoubleEdge");

    public DoubleEdge() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 22;
        baseMagicNumber = magicNumber = 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AttackEffect.SLASH_HEAVY);
        atb(new DamageAction(adp(),
                new DamageInfo(adp(), magicNumber, DamageInfo.DamageType.HP_LOSS),
                AttackEffect.NONE));
        enterOffensive();
    }

    @Override
    public void upp() {
        upgradeDamage(6);
    }
}
