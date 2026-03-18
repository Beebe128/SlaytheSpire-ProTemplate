package paintress.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class AnnihilatingBlow extends AbstractPaintressCard {
    public static final String ID = makeID("AnnihilatingBlow");

    public AnnihilatingBlow() {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 30;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AttackEffect.SLASH_HEAVY);
        applyToEnemy(m, new VulnerablePower(m, 3, false));
        enterVirtuose();
    }

    @Override
    public void upp() {
        upgradeDamage(10);
    }
}
