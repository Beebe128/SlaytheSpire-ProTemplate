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
import paintress.relics.ExpeditionJournal;
import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class FullCombustion extends AbstractPaintressCard {
    public static final String ID = makeID("FullCombustion");

    public FullCombustion() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        forAllMonstersLiving(mo -> {
            if (mo.hasPower(BurnPower.POWER_ID)) {
                AbstractPower burnPow = mo.getPower(BurnPower.POWER_ID);
                final int stacks = burnPow.amount;
                final AbstractMonster finalMo = mo;
                mo.powers.remove(burnPow);
                atb(new DamageAction(finalMo,
                        new DamageInfo(adp(), stacks * magicNumber, DamageInfo.DamageType.NORMAL),
                        AttackEffect.FIRE));
                ExpeditionJournal.onBurnConsumed();
            }
        });
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
