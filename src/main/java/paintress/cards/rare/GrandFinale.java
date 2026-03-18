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

public class GrandFinale extends AbstractPaintressCard {
    public static final String ID = makeID("GrandFinale");

    public GrandFinale() {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 10;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(actionify(() -> {
            int charges = getGradientCharges();
            if (charges > 0) {
                if (adp().hasPower(GradientChargePower.POWER_ID)) {
                    adp().powers.removeValue(adp().getPower(GradientChargePower.POWER_ID), true);
                }
                int dmgEach = charges * magicNumber;
                int[] multiDmg = new int[AbstractDungeon.getMonsters().monsters.size()];
                java.util.Arrays.fill(multiDmg, dmgEach);
                att(new DamageAllEnemiesAction(adp(), multiDmg, DamageInfo.DamageType.NORMAL, AttackEffect.FIRE));
            } else {
                int[] multiDmg = new int[AbstractDungeon.getMonsters().monsters.size()];
                java.util.Arrays.fill(multiDmg, 15);
                att(new DamageAllEnemiesAction(adp(), multiDmg, DamageInfo.DamageType.NORMAL, AttackEffect.FIRE));
            }
        }));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(5);
        exhaust = false;
        uDesc();
    }
}
