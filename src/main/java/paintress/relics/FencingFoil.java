package paintress.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paintress.Paintress;
import paintress.powers.OffensiveStancePower;

import static paintress.PaintressMod.makeID;

/**
 * Common Relic: Fencing Foil
 * "Whenever you enter Offensive Stance, gain 1 Energy."
 * Rewards aggressive stance cycling.
 */
public class FencingFoil extends AbstractPaintressRelic {
    public static final String ID = makeID("FencingFoil");

    private String prevStance = "none";

    public FencingFoil() {
        super(ID, RelicTier.COMMON, LandingSound.CLINK, Paintress.Enums.PAINTRESS_COLOR);
    }

    @Override
    public void atTurnStartPostDraw() {
        prevStance = getCurrentStance();
    }

    @Override
    public void onPlayCard(AbstractCard c, com.megacrit.cardcrawl.monsters.AbstractMonster m) {
        String current = getCurrentStance();
        if (!prevStance.equals("offensive") && current.equals("offensive")) {
            flash();
            AbstractDungeon.player.energy.recharge(1);
        }
        prevStance = current;
    }

    private String getCurrentStance() {
        if (AbstractDungeon.player.hasPower(OffensiveStancePower.POWER_ID)) return "offensive";
        if (AbstractDungeon.player.hasPower(paintress.powers.DefensiveStancePower.POWER_ID)) return "defensive";
        if (AbstractDungeon.player.hasPower(paintress.powers.VirtuoseStancePower.POWER_ID)) return "virtuose";
        return "none";
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
