package paintress;

import basemod.abstracts.CustomEnergyOrb;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import paintress.cards.basic.Slash;
import paintress.cards.basic.Parry;
import paintress.cards.basic.VirtuoseStrike;
import paintress.cards.common.OffensiveSwitch;
import paintress.cards.common.Spark;
import paintress.powers.GradientChargePower;
import paintress.relics.ArtistsPalette;

import java.util.ArrayList;

import static paintress.Paintress.Enums.PAINTRESS_COLOR;
import static paintress.PaintressMod.*;

public class Paintress extends CustomPlayer {

    static final String ID = makeID("Paintress");
    public static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    static final String[] NAMES = characterStrings.NAMES;
    static final String[] TEXT = characterStrings.TEXT;

    public Paintress(String name, PlayerClass setClass) {
        super(name, setClass,
                new CustomEnergyOrb(orbTextures, makeCharacterPath("mainChar/orb/vfx.png"), null),
                new SpriterAnimation(makeCharacterPath("mainChar/static.scml")));
        initializeClass(null,
                SHOULDER1, SHOULDER2, CORPSE,
                getLoadout(),
                20.0F, -10.0F, 166.0F, 327.0F,
                new EnergyManager(3));
        dialogX = (drawX + 0.0F * Settings.scale);
        dialogY = (drawY + 240.0F * Settings.scale);
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(
                NAMES[0], TEXT[0],
                75, 75, 0, 99, 5,
                this, getStartingRelics(), getStartingDeck(), false
        );
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> deck = new ArrayList<>();
        for (int i = 0; i < 4; i++) deck.add(Slash.ID);
        for (int i = 0; i < 4; i++) deck.add(Parry.ID);
        deck.add(VirtuoseStrike.ID);
        deck.add(OffensiveSwitch.ID);
        deck.add(Spark.ID);
        return deck;
    }

    /** Gain 1 Gradient Charge at the start of every combat. */
    @Override
    public void atBattleStartPreDraw() {
        super.atBattleStartPreDraw();
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(this, this, new GradientChargePower(this, 1), 1));
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> relics = new ArrayList<>();
        relics.add(ArtistsPalette.ID);
        return relics;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("UNLOCK_PING", MathUtils.random(-0.2F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);
    }

    private static final String[] orbTextures = {
            makeCharacterPath("mainChar/orb/layer1.png"),
            makeCharacterPath("mainChar/orb/layer2.png"),
            makeCharacterPath("mainChar/orb/layer3.png"),
            makeCharacterPath("mainChar/orb/layer4.png"),
            makeCharacterPath("mainChar/orb/layer5.png"),
            makeCharacterPath("mainChar/orb/layer6.png"),
            makeCharacterPath("mainChar/orb/layer1d.png"),
            makeCharacterPath("mainChar/orb/layer2d.png"),
            makeCharacterPath("mainChar/orb/layer3d.png"),
            makeCharacterPath("mainChar/orb/layer4d.png"),
            makeCharacterPath("mainChar/orb/layer5d.png"),
    };

    @Override
    public String getCustomModeCharacterButtonSoundKey() { return "UNLOCK_PING"; }

    @Override
    public int getAscensionMaxHPLoss() { return 8; }

    @Override
    public AbstractCard.CardColor getCardColor() { return PAINTRESS_COLOR; }

    @Override
    public Color getCardTrailColor() { return characterColor.cpy(); }

    @Override
    public BitmapFont getEnergyNumFont() { return FontHelper.energyNumFontRed; }

    @Override
    public String getLocalizedCharacterName() { return NAMES[0]; }

    @Override
    public AbstractCard getStartCardForEvent() { return new Slash(); }

    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) { return TEXT[1]; }

    @Override
    public AbstractPlayer newInstance() { return new Paintress(name, chosenClass); }

    @Override
    public Color getCardRenderColor() { return characterColor.cpy(); }

    @Override
    public Color getSlashAttackColor() { return characterColor.cpy(); }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.FIRE
        };
    }

    @Override
    public String getSpireHeartText() { return TEXT[1]; }

    @Override
    public String getVampireText() { return TEXT[2]; }

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass PAINTRESS;
        @SpireEnum(name = "PAINTRESS_COLOR")
        public static AbstractCard.CardColor PAINTRESS_COLOR;
        @SpireEnum(name = "PAINTRESS_COLOR")
        @SuppressWarnings("unused")
        public static CardLibrary.LibraryType PAINTRESS_LIBRARY;
    }
}
