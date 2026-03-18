package paintress.potions;

import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.potions.AbstractPotion.PotionColor;

public abstract class AbstractPaintressPotion extends CustomPotion {
    public AbstractPlayer.PlayerClass pool;
    public Color liquidColor, hybridColor, spotsColor;
    protected PotionStrings strings;

    public AbstractPaintressPotion(String id, String name, PotionRarity rarity, PotionSize size,
                                    Color liquidColor, Color hybridColor, Color spotsColor) {
        super(id, name, rarity, size, PotionColor.WHITE);
        this.liquidColor = liquidColor;
        this.hybridColor = hybridColor;
        this.spotsColor = spotsColor;
        strings = CardCrawlGame.languagePack.getPotionString(id);
    }

    public AbstractPaintressPotion(String id, String name, PotionRarity rarity, PotionSize size,
                                    Color liquidColor, Color hybridColor, Color spotsColor,
                                    AbstractPlayer.PlayerClass pool) {
        super(id, name, rarity, size, PotionColor.WHITE);
        this.liquidColor = liquidColor;
        this.hybridColor = hybridColor;
        this.spotsColor = spotsColor;
        this.pool = pool;
        strings = CardCrawlGame.languagePack.getPotionString(id);
    }
}
