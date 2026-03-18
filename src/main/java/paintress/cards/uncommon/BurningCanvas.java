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
import paintress.relics.ExpeditionJournal;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.*;

public class BurningCanvas extends AbstractPaintressCard {
    public static final String ID = makeID("BurningCanvas");

    public BurningCanvas() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(actionify(() -> {
            if (!m.isDeadOrEscaped() && m.hasPower(BurnPower.POWER_ID)) {
                AbstractPower pw = m.getPower(BurnPower.POWER_ID);
                int stacks = pw.amount;
                m.powers.removeValue(pw, true);
                int totalDmg = stacks * magicNumber;
                att(new DamageAction(m, new DamageInfo(adp(), totalDmg, DamageInfo.DamageType.NORMAL), AttackEffect.FIRE));
                ExpeditionJournal.onBurnConsumed();
            }
        }));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
