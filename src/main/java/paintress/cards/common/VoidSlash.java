package paintress.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class VoidSlash extends AbstractPaintressCard {
    public static final String ID = makeID("VoidSlash");

    public VoidSlash() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 8;
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean wasDefOrVirtuose = isInDefensive() || isInVirtuose();
        dmg(m, AttackEffect.SLASH_DIAGONAL);
        if (wasDefOrVirtuose) {
            applyToEnemy(m, new VulnerablePower(m, 1, false));
        }
        enterOffensive();
    }

    @Override
    public void upp() {
        upgradeDamage(4);
    }
}
