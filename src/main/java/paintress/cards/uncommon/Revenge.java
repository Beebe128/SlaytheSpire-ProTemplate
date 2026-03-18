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

public class Revenge extends AbstractPaintressCard {
    public static final String ID = makeID("Revenge");

    public Revenge() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 12;
        baseMagicNumber = magicNumber = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AttackEffect.BLUNT_HEAVY);
        applyToEnemy(m, new com.megacrit.cardcrawl.powers.VulnerablePower(m, adp(), 2, false));
        applyToEnemy(m, new com.megacrit.cardcrawl.powers.WeakPower(m, adp(), 2, false));
    }

    @Override
    public void upp() {
        upgradeDamage(4);
    }
}
