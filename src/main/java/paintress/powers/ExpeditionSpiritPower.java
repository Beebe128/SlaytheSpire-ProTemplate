package paintress.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.applyToSelf;

/**
 * Expedition Spirit — At start of turn, if you switched stances last turn, gain N Gradient Charges.
 * Rewards dynamic stance play.
 */
public class ExpeditionSpiritPower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("ExpeditionSpirit");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean switchedLastTurn = false;
    private String lastStance = "none";

    public ExpeditionSpiritPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        // Check current stance vs last turn's stance
        String currentStance = getCurrentStance();
        if (!currentStance.equals(lastStance)) {
            switchedLastTurn = true;
        }
        lastStance = currentStance;

        if (switchedLastTurn) {
            applyToSelf(new GradientChargePower(owner, amount));
            switchedLastTurn = false;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        String currentStance = getCurrentStance();
        switchedLastTurn = !currentStance.equals(lastStance);
        lastStance = currentStance;
    }

    private String getCurrentStance() {
        if (owner.hasPower(DefensiveStancePower.POWER_ID)) return "defensive";
        if (owner.hasPower(OffensiveStancePower.POWER_ID)) return "offensive";
        if (owner.hasPower(VirtuoseStancePower.POWER_ID)) return "virtuose";
        return "none";
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
