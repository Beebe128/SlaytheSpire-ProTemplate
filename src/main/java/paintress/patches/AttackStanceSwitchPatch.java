package paintress.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.Paintress;
import paintress.cards.AbstractPaintressCard;
import paintress.powers.DefensiveStancePower;
import paintress.powers.OffensiveStancePower;

import static paintress.util.Wiz.*;

/**
 * Global stance-switch mechanic injected via SpirePatch.
 *
 * After any ATTACK card is played by Maelle, queue a stance switch:
 *   Defensive → Offensive
 *   Offensive → Defensive
 *   Virtuose  → no stance
 *
 * Cards that explicitly enter a new stance in their own use() method will
 * override this base switch, because their stance actions are queued with
 * atb() (bottom of queue) AFTER this patch's queued action.
 */
@SpirePatch2(clz = AbstractPlayer.class, method = "useCard")
public class AttackStanceSwitchPatch {

    @SpirePostfixPatch
    public static void postfix(AbstractPlayer __instance, AbstractCard card, AbstractMonster m, int energyOnUse) {
        if (!(__instance instanceof Paintress)) return;
        if (card.type != AbstractCard.CardType.ATTACK) return;

        atb(actionify(() -> {
            if (AbstractPaintressCard.isInDefensive()) {
                AbstractPaintressCard.clearStances();
                applyToSelf(new OffensiveStancePower(adp(), 1));
            } else if (AbstractPaintressCard.isInOffensive()) {
                AbstractPaintressCard.clearStances();
                applyToSelf(new DefensiveStancePower(adp(), 1));
            } else if (AbstractPaintressCard.isInVirtuose()) {
                AbstractPaintressCard.clearStances();
            }
        }));
    }
}
