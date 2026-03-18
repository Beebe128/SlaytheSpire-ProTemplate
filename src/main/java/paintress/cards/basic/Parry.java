package paintress.cards.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paintress.cards.AbstractPaintressCard;

import static paintress.PaintressMod.makeID;

/**
 * Dodge — Maelle's basic defense. She deflects attacks with her blade.
 * A fencer's evasive guard. Grants Parry Status on play.
 */
public class Parry extends AbstractPaintressCard {
    public static final String ID = makeID("Dodge");

    public Parry() {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        baseBlock = 5;
        baseMagicNumber = magicNumber = 1;
        tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        applyToSelf(new paintress.powers.ParryStatusPower(adp(), magicNumber));
    }

    @Override
    public void upp() {
        upgradeBlock(3);
        upgradeMagicNumber(1);
    }
}
