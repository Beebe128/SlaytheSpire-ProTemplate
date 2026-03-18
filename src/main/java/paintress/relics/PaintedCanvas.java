package paintress.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.Paintress;
import paintress.powers.BurnPower;

import static paintress.PaintressMod.makeID;

/**
 * Common Relic: Painted Canvas
 * "At the start of each turn, if any enemy has Burn, draw 1 card."
 * Rewards Burn-focused decks with card draw.
 */
public class PaintedCanvas extends AbstractPaintressRelic {
    public static final String ID = makeID("PaintedCanvas");

    public PaintedCanvas() {
        super(ID, RelicTier.COMMON, LandingSound.FLAT, Paintress.Enums.PAINTRESS_COLOR);
    }

    @Override
    public void atTurnStart() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped() && m.hasPower(BurnPower.POWER_ID)) {
                flash();
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
                return;
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
