package paintress.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class Percee extends AbstractPaintressCard {
    public static final String ID = makeID("Percee");

    public Percee() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 9;
        baseMagicNumber = magicNumber = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (isInOffensiveOrVirtuose()) {
            att(new DamageAction(m, new DamageInfo(adp(), 5, damageTypeForTurn), AttackEffect.BLUNT_LIGHT));
        }
        dmg(m, AttackEffect.SLASH_DIAGONAL);
        enterDefensive();
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
