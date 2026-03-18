package paintress.powers;

import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.atb;

/**
 * Stance Increase — Entering a stance deals N damage to all enemies.
 * Called by AbstractPaintressCard helper when entering a stance.
 */
public class StanceIncreasePower extends AbstractPaintressPower {
    public static final String POWER_ID = makeID("StanceIncrease");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public StanceIncreasePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    /** Called by AbstractPaintressCard.enterDefensive/Offensive/Virtuose */
    public void onStanceEnter() {
        int[] multiDmg = new int[AbstractDungeon.getMonsters().monsters.size()];
        for (int i = 0; i < multiDmg.length; i++) multiDmg[i] = amount;
        atb(new DamageAllEnemiesAction(owner, multiDmg, DamageInfo.DamageType.THORNS,
                com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
