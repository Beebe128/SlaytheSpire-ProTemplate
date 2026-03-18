package paintress.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paintress.Paintress;
import paintress.powers.OffensiveStancePower;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.applyToSelf;

/**
 * Rare Relic: Maelle's Locket
 * "At the start of combat, enter Offensive Stance and gain 1 Energy."
 * Maelle enters battle ready to paint with fire.
 */
public class MaellesLocket extends AbstractPaintressRelic {
    public static final String ID = makeID("MaellesLocket");

    public MaellesLocket() {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL, Paintress.Enums.PAINTRESS_COLOR);
    }

    @Override
    public void atBattleStart() {
        flash();
        applyToSelf(new OffensiveStancePower(AbstractDungeon.player, 1));
        AbstractDungeon.player.energy.recharge(1);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
