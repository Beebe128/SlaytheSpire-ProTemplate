package paintress.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.potions.AbstractPotion.PotionColor;

public abstract class AbstractPaintressPotion extends CustomPotion {
    protected PotionStrings strings;

    public AbstractPaintressPotion(String id, String name, PotionRarity rarity, PotionSize size, PotionColor color) {
        super(id, name, rarity, size, color);
        strings = CardCrawlGame.languagePack.getPotionString(id);
    }
}
