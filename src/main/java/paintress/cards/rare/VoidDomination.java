package paintress.cards.rare;

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

public class VoidDomination extends AbstractPaintressCard {
    public static final String ID = makeID("VoidDomination");

    public VoidDomination() {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 8;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < 5; i++) {
            dmg(m, AttackEffect.FIRE);
            applyBurn(m, 1);
        }
        enterDefensive();
        gainGradient(2);
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
