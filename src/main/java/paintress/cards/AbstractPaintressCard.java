package paintress.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paintress.Paintress;
import paintress.powers.*;
import paintress.util.CardArtRoller;

import java.util.function.Consumer;

import static paintress.PaintressMod.makeImagePath;
import static paintress.PaintressMod.modID;
import static paintress.util.Wiz.*;

public abstract class AbstractPaintressCard extends CustomCard {

    protected final CardStrings cardStrings;

    public int secondMagic;
    public int baseSecondMagic;
    public boolean upgradedSecondMagic;
    public boolean isSecondMagicModified;

    public int secondDamage;
    public int baseSecondDamage;
    public boolean upgradedSecondDamage;
    public boolean isSecondDamageModified;

    /** Gradient Charges required to play this card (0 = no cost). */
    public int gradientCost = 0;

    private boolean needsArtRefresh = false;

    public AbstractPaintressCard(final String cardID, final int cost, final CardType type,
                                  final CardRarity rarity, final CardTarget target) {
        this(cardID, cost, type, rarity, target, Paintress.Enums.PAINTRESS_COLOR);
    }

    public AbstractPaintressCard(final String cardID, final int cost, final CardType type,
                                  final CardRarity rarity, final CardTarget target, final CardColor color) {
        super(cardID, "", getCardTextureString(cardID.replace(modID + ":", ""), type),
                cost, "", type, color, rarity, target);
        cardStrings = CardCrawlGame.languagePack.getCardStrings(this.cardID);
        // Null-safe fallbacks so cards without JSON entries don't crash on upgrade
        rawDescription = cardStrings.DESCRIPTION != null ? cardStrings.DESCRIPTION : "";
        name = originalName = cardStrings.NAME != null ? cardStrings.NAME
                : cardID.replace(modID + ":", "");
        initializeTitle();
        initializeDescription();

        if (textureImg.contains("ui/missing.png")) {
            if (CardLibrary.cards != null && !CardLibrary.cards.isEmpty()) {
                CardArtRoller.computeCard(this);
            } else {
                needsArtRefresh = true;
            }
        }
    }

    @Override
    protected Texture getPortraitImage() {
        if (textureImg.contains("ui/missing.png")) {
            return CardArtRoller.getPortraitTexture(this);
        }
        return super.getPortraitImage();
    }

    public static String getCardTextureString(final String cardName, final CardType cardType) {
        String textureString;
        switch (cardType) {
            case ATTACK:
            case POWER:
            case SKILL:
                textureString = makeImagePath("cards/" + cardName + ".png");
                break;
            default:
                textureString = makeImagePath("ui/missing.png");
                break;
        }
        FileHandle h = Gdx.files.internal(textureString);
        if (!h.exists()) {
            textureString = makeImagePath("ui/missing.png");
        }
        return textureString;
    }

    @Override
    public void applyPowers() {
        if (baseSecondDamage > -1) {
            secondDamage = baseSecondDamage;
            int tmp = baseDamage;
            baseDamage = baseSecondDamage;
            super.applyPowers();
            secondDamage = damage;
            baseDamage = tmp;
            super.applyPowers();
            isSecondDamageModified = (secondDamage != baseSecondDamage);
        } else super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (baseSecondDamage > -1) {
            secondDamage = baseSecondDamage;
            int tmp = baseDamage;
            baseDamage = baseSecondDamage;
            super.calculateCardDamage(mo);
            secondDamage = damage;
            baseDamage = tmp;
            super.calculateCardDamage(mo);
            isSecondDamageModified = (secondDamage != baseSecondDamage);
        } else super.calculateCardDamage(mo);
    }

    public void resetAttributes() {
        super.resetAttributes();
        secondMagic = baseSecondMagic;
        isSecondMagicModified = false;
        secondDamage = baseSecondDamage;
        isSecondDamageModified = false;
    }

    public void displayUpgrades() {
        super.displayUpgrades();
        if (upgradedSecondMagic) { secondMagic = baseSecondMagic; isSecondMagicModified = true; }
        if (upgradedSecondDamage) { secondDamage = baseSecondDamage; isSecondDamageModified = true; }
    }

    protected void upgradeSecondMagic(int amount) { baseSecondMagic += amount; secondMagic = baseSecondMagic; upgradedSecondMagic = true; }
    protected void upgradeSecondDamage(int amount) { baseSecondDamage += amount; secondDamage = baseSecondDamage; upgradedSecondDamage = true; }

    protected void uDesc() { this.rawDescription = this.cardStrings.UPGRADE_DESCRIPTION; this.initializeDescription(); }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upp();
            if (this.cardStrings.UPGRADE_DESCRIPTION != null) this.uDesc();
        }
    }

    public abstract void upp();

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (gradientCost > 0 && getGradientCharges() < gradientCost) {
            cantUseMessage = "Need " + gradientCost + " Gradient Charge" + (gradientCost > 1 ? "s" : "") + ".";
            return false;
        }
        return super.canUse(p, m);
    }

    /** Spends gradient charges (call this in use() for gradient-cost cards). */
    protected void spendGradient(int amount) {
        if (adp().hasPower(GradientChargePower.POWER_ID)) {
            AbstractPower p = adp().getPower(GradientChargePower.POWER_ID);
            p.amount -= amount;
            if (p.amount <= 0) {
                removePower(p);
            } else {
                p.updateDescription();
                p.flash();
            }
        }
    }

    public void update() {
        super.update();
        if (needsArtRefresh) CardArtRoller.computeCard(this);
    }

    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard result = super.makeStatEquivalentCopy();
        if (result instanceof AbstractPaintressCard) {
            AbstractPaintressCard c = (AbstractPaintressCard) result;
            c.baseSecondDamage = c.secondDamage = baseSecondDamage;
            c.baseSecondMagic = c.secondMagic = baseSecondMagic;
        }
        return result;
    }

    // ---- Stance helpers ----
    /** Returns true if the player is currently in Defensive Stance */
    public static boolean isInDefensive() {
        return adp().hasPower(DefensiveStancePower.POWER_ID);
    }

    /** Returns true if the player is in Offensive Stance */
    public static boolean isInOffensive() {
        return adp().hasPower(OffensiveStancePower.POWER_ID);
    }

    /** Returns true if the player is in Virtuose Stance */
    public static boolean isInVirtuose() {
        return adp().hasPower(VirtuoseStancePower.POWER_ID);
    }

    /** Returns true if in Offensive OR Virtuose (for cards that say "bonus in Offensive") */
    public static boolean isInOffensiveOrVirtuose() {
        return isInOffensive() || isInVirtuose();
    }

    /** Removes all stance powers and enters Defensive Stance */
    protected void enterDefensive() {
        boolean alreadyIn = isInDefensive();
        clearStances();
        if (!alreadyIn) {
            applyToSelf(new DefensiveStancePower(adp(), 1));
            // Entering a new stance: draw 1 (represents AP gain)
            atb(actionify(() -> {
                if (adp().drawPile.size() > 0 || adp().discardPile.size() > 0)
                    att(new DrawCardAction(adp(), 1));
            }));
        }
        // If already in Defensive, clearStances already removed it (go to Stanceless)
    }

    /** Removes all stance powers and enters Offensive Stance */
    protected void enterOffensive() {
        boolean alreadyIn = isInOffensive();
        clearStances();
        if (!alreadyIn) {
            applyToSelf(new OffensiveStancePower(adp(), 1));
            atb(actionify(() -> {
                if (adp().drawPile.size() > 0 || adp().discardPile.size() > 0)
                    att(new DrawCardAction(adp(), 1));
            }));
        }
    }

    /** Removes all stance powers and enters Virtuose Stance */
    protected void enterVirtuose() {
        boolean alreadyIn = isInVirtuose();
        clearStances();
        if (!alreadyIn) {
            applyToSelf(new VirtuoseStancePower(adp(), 1));
            atb(actionify(() -> {
                if (adp().drawPile.size() > 0 || adp().discardPile.size() > 0)
                    att(new DrawCardAction(adp(), 1));
            }));
        }
    }

    /** Removes all stance powers (go to Stanceless) */
    public static void clearStances() {
        if (adp().hasPower(DefensiveStancePower.POWER_ID))
            removePower(adp().getPower(DefensiveStancePower.POWER_ID));
        if (adp().hasPower(OffensiveStancePower.POWER_ID))
            removePower(adp().getPower(OffensiveStancePower.POWER_ID));
        if (adp().hasPower(VirtuoseStancePower.POWER_ID))
            removePower(adp().getPower(VirtuoseStancePower.POWER_ID));
    }

    /** Gets current Gradient Charge count */
    public static int getGradientCharges() {
        return pwrAmt(adp(), GradientChargePower.POWER_ID);
    }

    /** Adds Gradient Charges to the player */
    protected void gainGradient(int amount) {
        applyToSelf(new GradientChargePower(adp(), amount));
    }

    /** Gets Burn stacks on a monster */
    protected int getBurnStacks(AbstractMonster m) {
        return pwrAmt(m, BurnPower.POWER_ID);
    }

    /** Applies Burn stacks to a monster */
    protected void applyBurn(AbstractMonster m, int stacks) {
        applyToEnemy(m, new BurnPower(m, adp(), stacks));
    }

    // ---- Combat shortcuts ----
    protected void dmg(AbstractMonster m, AbstractGameAction.AttackEffect fx) {
        atb(new DamageAction(m, new DamageInfo(AbstractDungeon.player, damage, damageTypeForTurn), fx));
    }

    protected void dmgTop(AbstractMonster m, AbstractGameAction.AttackEffect fx) {
        att(new DamageAction(m, new DamageInfo(AbstractDungeon.player, damage, damageTypeForTurn), fx));
    }

    protected void allDmg(AbstractGameAction.AttackEffect fx) {
        atb(new DamageAllEnemiesAction(AbstractDungeon.player, multiDamage, damageTypeForTurn, fx));
    }

    protected void altDmg(AbstractMonster m, AbstractGameAction.AttackEffect fx) {
        atb(new DamageAction(m, new DamageInfo(AbstractDungeon.player, secondDamage, damageTypeForTurn), fx));
    }

    protected void dmgRandom(AbstractGameAction.AttackEffect fx) {
        atb(actionify(() -> {
            AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            if (target != null) {
                calculateCardDamage(target);
                att(new DamageAction(target, new DamageInfo(AbstractDungeon.player, damage, damageTypeForTurn), fx));
            }
        }));
    }

    protected void blck() {
        atb(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
    }

    protected void blckTop() {
        att(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
    }

    public String cardArtCopy() { return null; }
    public CardArtRoller.ReskinInfo reskinInfo(String ID) { return null; }
}
