package paintress.relics;

import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paintress.Paintress;
import paintress.powers.VirtuoseStancePower;

import static paintress.PaintressMod.makeID;
import static paintress.util.Wiz.atb;

/**
 * Uncommon Relic: Virtuose Medal
 * "The first time you enter Virtuose Stance each combat, deal 5 damage to all enemies."
 * Rewards players who achieve Virtuose Stance.
 */
public class VirtuoseMedal extends AbstractPaintressRelic {
    public static final String ID = makeID("VirtuoseMedal");
    private boolean triggeredThisCombat = false;
    private String prevStance = "none";

    public VirtuoseMedal() {
        super(ID, RelicTier.UNCOMMON, LandingSound.CLINK, Paintress.Enums.PAINTRESS_COLOR);
    }

    @Override
    public void atBattleStart() {
        triggeredThisCombat = false;
        prevStance = "none";
    }

    @Override
    public void onPlayCard(com.megacrit.cardcrawl.cards.AbstractCard c, com.megacrit.cardcrawl.monsters.AbstractMonster m) {
        if (!triggeredThisCombat) {
            String current = getCurrentStance();
            if (!prevStance.equals("virtuose") && current.equals("virtuose")) {
                triggeredThisCombat = true;
                flash();
                int[] dmg = new int[AbstractDungeon.getMonsters().monsters.size()];
                for (int i = 0; i < dmg.length; i++) dmg[i] = 5;
                atb(new DamageAllEnemiesAction(AbstractDungeon.player, dmg,
                        DamageInfo.DamageType.THORNS,
                        com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }
            prevStance = current;
        }
    }

    private String getCurrentStance() {
        if (AbstractDungeon.player.hasPower(VirtuoseStancePower.POWER_ID)) return "virtuose";
        if (AbstractDungeon.player.hasPower(paintress.powers.OffensiveStancePower.POWER_ID)) return "offensive";
        if (AbstractDungeon.player.hasPower(paintress.powers.DefensiveStancePower.POWER_ID)) return "defensive";
        return "none";
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
