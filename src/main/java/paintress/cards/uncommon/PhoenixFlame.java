package paintress.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

/**
 * Phoenix Flame — A powerful gradient skill that bathes all enemies in flame
 * and restores Maelle's vitality.
 * Costs 0 energy but requires 2 Gradient Charges.
 */
public class PhoenixFlame extends AbstractPaintressCard {
    public static final String ID = makeID("PhoenixFlame");

    public PhoenixFlame() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 5; // burn stacks
        gradientCost = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        spendGradient(gradientCost);
        forAllMonstersLiving(mo -> applyBurn(mo, magicNumber));
        int healAmt = (int)(p.maxHealth * 0.20f);
        atb(new HealAction(p, p, healAmt));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(3);
    }
}
