package paintress.relics;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paintress.Paintress;
import paintress.powers.DefensiveStancePower;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.atb;

/**
 * Common Relic: Guardian's Crest
 * "Whenever you enter Defensive Stance, gain 3 Block."
 * Makes defensive stance cycling rewarding for survivability.
 */
public class GuardiansCrest extends AbstractPaintressRelic {
    public static final String ID = makeID("GuardiansCrest");
    private String prevStance = "none";

    public GuardiansCrest() {
        super(ID, RelicTier.COMMON, LandingSound.CLINK, Paintress.Enums.PAINTRESS_COLOR);
    }

    @Override
    public void atTurnStartPostDraw() {
        prevStance = getCurrentStance();
    }

    @Override
    public void onPlayCard(AbstractCard c, com.megacrit.cardcrawl.monsters.AbstractMonster m) {
        String current = getCurrentStance();
        if (!prevStance.equals("defensive") && current.equals("defensive")) {
            flash();
            atb(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 3));
        }
        prevStance = current;
    }

    private String getCurrentStance() {
        if (AbstractDungeon.player.hasPower(DefensiveStancePower.POWER_ID)) return "defensive";
        if (AbstractDungeon.player.hasPower(paintress.powers.OffensiveStancePower.POWER_ID)) return "offensive";
        if (AbstractDungeon.player.hasPower(paintress.powers.VirtuoseStancePower.POWER_ID)) return "virtuose";
        return "none";
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
