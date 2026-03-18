package paintress.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import paintress.cards.AbstractPaintressCard;
import paintress.powers.*;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class Pyrolyse extends AbstractPaintressCard {
    public static final String ID = makeID("Pyrolyse");

    public Pyrolyse() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 14;
        baseMagicNumber = magicNumber = 5;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AttackEffect.FIRE);
        int burn = isInOffensiveOrVirtuose() ? magicNumber + 4 : magicNumber;
        applyBurn(m, burn);
        enterOffensive();
    }

    @Override
    public void upp() {
        upgradeDamage(4);
        upgradeMagicNumber(2);
    }
}
