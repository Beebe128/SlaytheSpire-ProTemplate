package paintress.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.atb;

/**
 * Vigilant Painter — At start of turn, if in Defensive Stance, gain N Block.
 * Rewards commitment to defensive play.
 */
public class VigilantPainterPower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("VigilantPainter");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public VigilantPainterPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        if (owner.hasPower(DefensiveStancePower.POWER_ID)) {
            atb(new GainBlockAction(owner, owner, amount));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
