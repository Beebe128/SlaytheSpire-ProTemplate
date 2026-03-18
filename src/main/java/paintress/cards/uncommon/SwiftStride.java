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

public class SwiftStride extends AbstractPaintressCard {
    public static final String ID = makeID("SwiftStride");

    public SwiftStride() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseDamage = 0;
        baseMagicNumber = magicNumber = 8;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enterVirtuose();
        forAllMonstersLiving(mo -> {
            if (getBurnStacks(mo) > 0) {
                atb(new DamageAction(mo, new DamageInfo(adp(), magicNumber, DamageInfo.DamageType.NORMAL), AttackEffect.FIRE));
            }
        });
    }

    @Override
    public void upp() {
        upgradeMagicNumber(4);
    }
}
