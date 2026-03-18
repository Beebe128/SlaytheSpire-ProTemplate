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

public class CriticalBurn extends AbstractPaintressCard {
    public static final String ID = makeID("CriticalBurn");

    public CriticalBurn() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 5;
        baseMagicNumber = magicNumber = 8;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AttackEffect.FIRE);
        if (getBurnStacks(m) > 0) {
            atb(new DamageAction(m, new DamageInfo(adp(), magicNumber, damageTypeForTurn), AttackEffect.FIRE));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(4);
    }
}
