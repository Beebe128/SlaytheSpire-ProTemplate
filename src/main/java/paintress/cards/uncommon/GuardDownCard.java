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

public class GuardDownCard extends AbstractPaintressCard {
    public static final String ID = makeID("GuardDown");

    public GuardDownCard() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        forAllMonstersLiving(mo -> applyToEnemy(mo, new com.megacrit.cardcrawl.powers.VulnerablePower(mo, adp(), magicNumber, false)));
        adp().energy.recharge(1);
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
