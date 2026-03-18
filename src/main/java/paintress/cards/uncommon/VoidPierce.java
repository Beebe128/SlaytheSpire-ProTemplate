package paintress.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class VoidPierce extends AbstractPaintressCard {
    public static final String ID = makeID("VoidPierce");

    public VoidPierce() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 15;
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean alreadyVuln = m.hasPower(VulnerablePower.POWER_ID);
        dmg(m, AttackEffect.SLASH_DIAGONAL);
        applyToEnemy(m, new VulnerablePower(m, magicNumber, false));
        if (alreadyVuln) {
            enterVirtuose();
        } else {
            enterDefensive();
        }
    }

    @Override
    public void upp() {
        upgradeDamage(5);
    }
}
