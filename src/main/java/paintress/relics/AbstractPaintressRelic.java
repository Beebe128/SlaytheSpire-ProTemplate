package paintress.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paintress.util.TexLoader;

import static paintress.PaintressMod.makeRelicPath;
import static paintress.PaintressMod.modID;

public abstract class AbstractPaintressRelic extends CustomRelic {
    public AbstractCard.CardColor color;

    public AbstractPaintressRelic(String setId, RelicTier tier, LandingSound sfx) {
        this(setId, tier, sfx, null);
    }

    public AbstractPaintressRelic(String setId, RelicTier tier, LandingSound sfx, AbstractCard.CardColor color) {
        super(setId,
                TexLoader.getTexture(makeRelicPath(setId.replace(modID + ":", "") + ".png")),
                tier, sfx);
        outlineImg = TexLoader.getTexture(makeRelicPath(setId.replace(modID + ":", "") + "Outline.png"));
        this.color = color;
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
