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

public class StrikeOfGenius extends AbstractPaintressCard {
    public static final String ID = makeID("StrikeOfGenius");

    public StrikeOfGenius() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 14;
        baseMagicNumber = magicNumber = 14;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AttackEffect.SLASH_DIAGONAL);
        gainGradient(2);
        if (isInOffensiveOrVirtuose()) {
            atb(new DamageAction(m, new DamageInfo(adp(), damage, damageTypeForTurn), AttackEffect.SLASH_DIAGONAL));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(5);
    }
}
