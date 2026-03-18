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

public class OffensiveFinale extends AbstractPaintressCard {
    public static final String ID = makeID("OffensiveFinale");

    public OffensiveFinale() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 12;
        baseMagicNumber = magicNumber = 6;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (isInOffensiveOrVirtuose()) {
            dmg(m, AttackEffect.SLASH_HEAVY);
            atb(new DamageAction(m, new DamageInfo(adp(), magicNumber, damageTypeForTurn), AttackEffect.SLASH_DIAGONAL));
        } else {
            dmg(m, AttackEffect.SLASH_DIAGONAL);
        }
    }

    @Override
    public void upp() {
        upgradeDamage(4);
        upgradeMagicNumber(3);
    }
}
