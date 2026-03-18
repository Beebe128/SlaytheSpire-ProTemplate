# Design Summary — The Paintress (Maelle)

## Design Philosophy

The goal of this mod is to translate Maelle's gameplay from *Clair Obscur: Expedition 33* to Slay the Spire as accurately as possible, preserving the feel of her combat loop while adapting mechanics to work in a solo card game context.

## Key Translation Decisions

### AP → Energy
Expedition 33's AP (Action Points) system maps directly to STS Energy. Skills that grant +1 AP in the original game are translated to cards that grant +1 Energy or reduce costs.

### Stance System → Buff Powers
Rather than using AbstractStance subclasses (which require heavier StSLib integration), stances are implemented as non-turn-based buff powers:
- `DefensiveStancePower` — atDamageReceive ×0.70f (50% reduction)
- `OffensiveStancePower` — atDamageGive ×1.50f, atDamageReceive ×1.50f
- `VirtuoseStancePower` — atDamageGive ×3.0f (200% more = triple)

The "same stance toggle" rule: entering your current stance clears it back to Stanceless. Entering a new stance draws 1 card (representing the +1 AP from stance switching in the original).

### Burn → Custom DoT Power
Burn is a custom `BurnPower` (enemy debuff). Each stack deals damage at start of the burning creature's turn, then decreases by 3. Multiple Maelle cards apply Burn, and others scale off Burn.

### Defenceless → Vulnerable
In Expedition 33, Defenceless makes a target take 25% more damage from all sources. STS's Vulnerable power does the same (50% more, but capped by various factors). Cards like Offensive Switch, Guard Down, and Stendhal apply Vulnerable.

### Mark → (Not implemented)
Mark (amplifies next hit ×2) was deemed too complex to implement cleanly with STS's architecture without creating custom vulnerability tracking. It was omitted from the initial implementation.

### Gradient Attacks → Gradient Charge Power
The Gradient Gauge becomes a `GradientChargePower` (non-turn-based buff, max 15 stacks). Gradient Attacks like Gommage and Phoenix Flame reference this resource.

### Parry → Block + Energy Generation
Parrying in E33 generates AP. In STS, the "Defensive Stance" power and certain Block cards provide this equivalent: staying in Defensive Stance passively reduces damage, and the `EnergizingParryPower` draws cards when 0-cost cards are played (simulating the parry-AP generation loop).

### Mezzo Forte — Key Design Challenge
In E33, Mezzo Forte re-applies your current stance (keeping it) and grants +2–4 AP. In STS, it re-applies the current stance buff (maintaining the damage modifier) and draws cards. This preserves the "stance engine" identity.

### Breaking Rules — Extra Turn Mechanic
In E33, if a target is Defenceless and you use Breaking Rules, you get an extra turn. In STS, this is approximated by adding all current hand cards to a "free play" pool when the condition triggers.

## 4 Viable Archetypes

1. **Burn Combo** (Fire build): Spark → Pyrolyse → Rain of Fire → Burning Canvas. With Berserker Painter power and Critical Burn cards, Burn stacks compound to make Burning Canvas hit for absurd damage.

2. **Stance Cycle** (Engine build): Stance Dance + Color Harmony-equivalent cards + StanceIncreasePower. Constantly switching stances generates cards, triggers damage, and maintains high output.

3. **Defensive Counter** (Parry build): Defensive Stance → Payback (cheap due to high Block) → Breaking Rules (destroys shields, bonus energy). AugmentedCounter + VigilantPainter make staying defensive profitable.

4. **Gradient Burst** (Resource build): Expedition Spirit Power + GradientRush + PhantomStrike → Gommage. GommageCore relic ensures Gradient persists between fights for boss combats.

## Lore Accuracy Notes

All skill names are taken directly from Maelle's Expedition 33 skillset:
- Stendhal, Gommage, Percée, Spark, Swift Stride, Fleuret Fury, Sword Ballet, Rain of Fire, Breaking Rules, Pyrolyse, Phantom Strike, Last Chance, Mezzo Forte, Payback, Burning Canvas, Phoenix Flame, Offensive Switch, Guard Down

Pictos/Luminas are translated as power cards:
- Painted Power, Glass Canon, Immaculate, Critical Burn, Augmented Counter, Energizing Parry

Weapons are translated as relics:
- Medalum (start in Virtuose, Burn on attacks in Virtuose)
- Artist's Palette (Gradient Charges)
- Maelle's Locket (enter Offensive at combat start)
