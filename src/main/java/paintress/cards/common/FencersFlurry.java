package paintress.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class FencersFlurry extends AbstractPaintressCard {
    public static final String ID = makeID("FencersFlurry");

    public FencersFlurry() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        baseDamage = 8;
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        allDmg(AttackEffect.SLASH_HORIZONTAL);
        enterOffensive();
        forAllMonstersLiving(mo -> applyToEnemy(mo, new VulnerablePower(mo, 1, false)));
    }

    @Override
    public void upp() {
        upgradeDamage(2);
    }
}
