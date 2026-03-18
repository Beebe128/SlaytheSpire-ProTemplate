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

public class ShieldBreaker extends AbstractPaintressCard {
    public static final String ID = makeID("ShieldBreaker");

    public ShieldBreaker() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(actionify(() -> {
            int blocked = m.currentBlock;
            m.loseBlock(blocked);
            int dmg = blocked / magicNumber;
            if (dmg > 0) {
                att(new DamageAction(m, new DamageInfo(adp(), dmg, DamageInfo.DamageType.NORMAL), AttackEffect.BLUNT_LIGHT));
            }
        }));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(-1);
    }
}
