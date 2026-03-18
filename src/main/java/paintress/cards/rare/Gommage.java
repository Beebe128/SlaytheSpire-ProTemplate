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

public class Gommage extends AbstractPaintressCard {
    public static final String ID = makeID("Gommage");

    public Gommage() {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 6;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enterVirtuose();
        atb(actionify(() -> {
            int charges = getGradientCharges();
            if (charges > 0) {
                if (adp().hasPower(GradientChargePower.POWER_ID)) {
                    adp().powers.removeValue(adp().getPower(GradientChargePower.POWER_ID), true);
                }
                int totalDmg = charges * magicNumber;
                att(new DamageAction(m, new DamageInfo(adp(), totalDmg, DamageInfo.DamageType.NORMAL), AttackEffect.FIRE));
            } else {
                att(new DamageAction(m, new DamageInfo(adp(), 20, DamageInfo.DamageType.NORMAL), AttackEffect.SLASH_HEAVY));
            }
        }));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
    }
}
