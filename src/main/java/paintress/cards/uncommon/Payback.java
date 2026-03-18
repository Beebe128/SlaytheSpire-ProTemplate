package paintress.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

/**
 * Payback — Costs 1 less for each 5 Block Maelle has.
 * From Expedition 33: Payback has reduced AP cost for each attack parried.
 * STS translation: costs less if you have enough Block (representing successful parries).
 */
public class Payback extends AbstractPaintressCard {
    public static final String ID = makeID("Payback");

    public Payback() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 16;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AttackEffect.SLASH_HEAVY);
        enterOffensive();
    }

    @Override
    public void triggerOnCardPlayedWhileInHand(com.megacrit.cardcrawl.cards.AbstractCard c) {
        // Not used
    }

    @Override
    public float getCost() {
        // Cost reduced by 1 for every 5 Block the player has (min 0)
        int reduction = Math.min(2, adp().currentBlock / 5);
        return Math.max(0, cost - reduction);
    }

    @Override
    public void upp() {
        upgradeDamage(6);
    }
}
