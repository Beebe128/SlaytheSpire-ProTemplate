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

public class BreakingRules extends AbstractPaintressCard {
    public static final String ID = makeID("BreakingRules");

    public BreakingRules() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 0;
        baseMagicNumber = magicNumber = 6;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(actionify(() -> {
            int blocked = m.currentBlock;
            m.loseBlock(blocked);
            int energyGain = blocked / 3;
            if (energyGain > 0) att(new GainEnergyAction(energyGain));
            att(new DamageAction(m, new DamageInfo(adp(), magicNumber, damageTypeForTurn), AttackEffect.BLUNT_HEAVY));
            if (m.hasPower(com.megacrit.cardcrawl.powers.VulnerablePower.POWER_ID)) att(new GainEnergyAction(1));
        }));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(3);
    }
}
