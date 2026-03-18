package paintress.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class MomentumStrike extends AbstractPaintressCard {
    public static final String ID = makeID("MomentumStrike");

    public MomentumStrike() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 15;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AttackEffect.SLASH_HEAVY);
        enterDefensive();
        if (isInOffensiveOrVirtuose()) {
            att(new DamageAction(m, new DamageInfo(adp(), 7, damageTypeForTurn), AttackEffect.SLASH_DIAGONAL));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(4);
    }
}
