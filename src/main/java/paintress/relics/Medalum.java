package paintress.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paintress.Paintress;
import paintress.powers.BurnPower;
import paintress.powers.VirtuoseStancePower;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.applyToSelf;
import static paintress.util.Wiz.applyToEnemy;

/**
 * Rare Relic: Medalum
 * "At the start of combat, enter Virtuose Stance.
 *  While in Virtuose Stance, your attacks apply 1 Burn to the target."
 * Named after the weapon in Expedition 33 that starts Maelle in Virtuose Stance.
 */
public class Medalum extends AbstractPaintressRelic {
    public static final String ID = makeID("Medalum");

    public Medalum() {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL, Paintress.Enums.PAINTRESS_COLOR);
    }

    @Override
    public void atBattleStart() {
        flash();
        applyToSelf(new VirtuoseStancePower(AbstractDungeon.player, 1));
    }

    /** Called via AbstractPower.onAttack — applied by checking if in Virtuose */
    @Override
    public void onPlayCard(com.megacrit.cardcrawl.cards.AbstractCard card,
                           com.megacrit.cardcrawl.monsters.AbstractMonster target) {
        if (card.type == com.megacrit.cardcrawl.cards.AbstractCard.CardType.ATTACK
                && target != null && !target.isDeadOrEscaped()
                && AbstractDungeon.player.hasPower(VirtuoseStancePower.POWER_ID)) {
            applyToEnemy(target, new BurnPower(target, AbstractDungeon.player, 1));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
