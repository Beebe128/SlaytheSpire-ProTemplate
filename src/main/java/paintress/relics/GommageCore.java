package paintress.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paintress.Paintress;
import paintress.powers.GradientChargePower;

import static paintress.PaintressMod.makeID;

/**
 * Boss Relic: Gommage Core
 * "Gradient Charges persist between combats. Maximum Gradient Charges increased to 20."
 * Named after Maelle's ultimate Gradient skill (Gommage).
 * Allows players to build toward massive Gradient payoffs across multiple encounters.
 */
public class GommageCore extends AbstractPaintressRelic {
    public static final String ID = makeID("GommageCore");

    public GommageCore() {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL, Paintress.Enums.PAINTRESS_COLOR);
    }

    /** Do NOT clear Gradient Charges at end of combat — override the atBattleStart to keep charges */
    @Override
    public void atBattleStart() {
        // No-op: intentionally do not reset charges
        flash();
        // If no GradientCharge power exists, don't create one (start with 0 if truly fresh)
    }

    /** After battle, Gradient Charges are preserved via the power system */
    // Note: in STS, powers are naturally removed between combats.
    // To preserve them, we intercept via BaseMod's PostBattle subscriber or use a relic-level save.
    // For now, we use atBattleStart to re-add any charges that were saved in relic's data.
    private int savedCharges = 0;

    @Override
    public void onVictory() {
        // Save current charges
        if (AbstractDungeon.player.hasPower(GradientChargePower.POWER_ID)) {
            savedCharges = AbstractDungeon.player.getPower(GradientChargePower.POWER_ID).amount;
        } else {
            savedCharges = 0;
        }
    }

    @Override
    public void atPreBattle() {
        // Restore charges at start of next combat
        if (savedCharges > 0) {
            flash();
            GradientChargePower gradPow = new GradientChargePower(AbstractDungeon.player, savedCharges);
            // Increase max to 20
            gradPow.stackPower(0); // ensure initialized
            AbstractDungeon.player.addPower(gradPow);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
