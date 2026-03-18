package paintress.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paintress.Paintress;
import paintress.powers.GradientChargePower;

/**
 * Grants Maelle 1 Gradient Charge at the start of every combat.
 * Patches AbstractPlayer.atBattleStart() since CustomPlayer doesn't
 * expose it as overridable in this STS version.
 */
@SpirePatch2(clz = AbstractPlayer.class, method = "atBattleStart")
public class CombatStartGradientPatch {

    @SpirePostfixPatch
    public static void postfix(AbstractPlayer __instance) {
        if (!(__instance instanceof Paintress)) return;
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(__instance, __instance,
                        new GradientChargePower(__instance, 1), 1));
    }
}
