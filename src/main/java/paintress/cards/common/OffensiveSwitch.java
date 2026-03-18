package paintress.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class OffensiveSwitch extends AbstractPaintressCard {
    public static final String ID = makeID("OffensiveSwitch");

    public OffensiveSwitch() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 9;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AttackEffect.SLASH_DIAGONAL);
        enterOffensive();
        applyToEnemy(m, new VulnerablePower(m, adp(), 2, false));
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
