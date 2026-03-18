package paintress.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static paintress.PaintressMod.makeID;

/**
 * Stance Awareness — Whenever you enter a new stance, draw 1 card (+ gain 1 energy if upgraded).
 * Called by AbstractPaintressCard.enterDefensive/Offensive/Virtuose via helper.
 */
public class StanceAwarenessPower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("StanceAwareness");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final boolean giveEnergy;

    public StanceAwarenessPower(AbstractCreature owner, int amount, boolean giveEnergy) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
        this.giveEnergy = giveEnergy;
    }

    /** Called by AbstractPaintressCard on stance enter */
    public void onStanceEnter() {
        AbstractDungeon.player.drawCards(1);
        if (giveEnergy) {
            AbstractDungeon.player.energy.recharge(1);
        }
    }

    @Override
    public void updateDescription() {
        description = giveEnergy ? DESCRIPTIONS[1] : DESCRIPTIONS[0];
    }
}
