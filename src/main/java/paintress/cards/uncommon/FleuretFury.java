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

public class FleuretFury extends AbstractPaintressCard {
    public static final String ID = makeID("FleuretFury");

    public FleuretFury() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 5;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean inVirtuose = isInVirtuose();
        for (int i = 0; i < 3; i++) {
            dmg(m, AttackEffect.SLASH_DIAGONAL);
        }
        if (inVirtuose) {
            atb(actionify(() -> {
                clearStances();
                applyToSelf(new VirtuoseStancePower(adp(), 1));
            }));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
    }
}
