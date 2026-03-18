package paintress.cards.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;

/**
 * Virtuose Strike — A starter gradient attack that unleashes five rapid hits
 * then elevates Maelle into Virtuose Stance.
 * Costs 0 energy but requires 1 Gradient Charge.
 */
public class VirtuoseStrike extends AbstractPaintressCard {
    public static final String ID = makeID("VirtuoseStrike");

    public VirtuoseStrike() {
        super(ID, 0, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        baseDamage = 5;
        baseMagicNumber = magicNumber = 5; // number of hits
        gradientCost = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        spendGradient(gradientCost);
        for (int i = 0; i < magicNumber; i++) {
            dmg(m, AttackEffect.SLASH_DIAGONAL);
        }
        enterVirtuose();
    }

    @Override
    public void upp() {
        upgradeDamage(2);
    }
}
