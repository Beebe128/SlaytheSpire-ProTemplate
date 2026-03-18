# The Paintress — Maelle (Clair Obscur: Expedition 33) Slay the Spire Mod

A complete custom character mod for Slay the Spire featuring Maelle from *Clair Obscur: Expedition 33*, faithfully translated from her original gameplay mechanics.

## Character Overview

**Maelle** is a 16-year-old fencer and the party's primary damage dealer. She is the only character in Expedition 33 with a full stance system. In this mod, she brings her signature blend of Fire/Burn damage, stance cycling, and devastating Void attacks to Slay the Spire.

- **HP**: 75
- **Energy**: 3 per turn
- **Starter Deck**: 4× Slash + 4× Parry
- **Starter Relic**: Artist's Palette (gain 2 Gradient Charges at combat start)

## Core Mechanics

### Stances
Maelle can be in one of four states:

| Stance | Effect |
|--------|--------|
| **Stanceless** | Default. No bonuses or penalties. |
| **Defensive** | Take 50% less damage. Entering a new stance draws 1 card. |
| **Offensive** | Deal 50% more damage. Take 50% more damage. Entering draws 1. |
| **Virtuose** | Deal 200% more damage (triple total). The pinnacle of combat. |

**Stance Toggle Rule**: Playing a card that enters your current stance exits it (returns to Stanceless). Playing a different stance draws 1 card.

### Burn
A custom stackable DoT debuff applied to enemies. At the start of the afflicted creature's turn, each stack deals damage. Stacks decrease by 3 per turn. Many of Maelle's attacks deal increased damage to Burning targets.

### Gradient Charges
A stackable resource (max 15) building toward powerful Gradient Attacks (Phoenix Flame, Gommage). Represented as a buff power on the player.

## Card Pool (80 Cards)

### Archetypes

1. **Burn Combo**: Stack Burn via Spark/Rain of Fire/Pyrolyse, then cash out with Burning Canvas or Blaze of Glory.

2. **Stance Cycling**: Cycle Defensive→Offensive→Virtuose to draw cards and leverage Virtuose's triple damage.

3. **Defensive Counter**: Payback (cost reduced per Block) + Breaking Rules (extra turn if enemy Vulnerable).

4. **Gradient Build**: Accumulate Gradient Charges via powers/relics, release via Gommage and Phoenix Flame.

## Relics

| Name | Tier | Effect |
|------|------|--------|
| Artist's Palette | Starter | Start of combat: gain 2 Gradient Charges. |
| Fencing Foil | Common | On entering Offensive Stance: gain 1 Energy. |
| Painted Canvas | Common | Start of turn: if any enemy is Burning, draw 1. |
| Guardian's Crest | Common | On entering Defensive Stance: gain 3 Block. |
| Virtuose Medal | Uncommon | First Virtuose entry per combat: deal 5 AoE damage. |
| Expedition Journal | Uncommon | When Burn is consumed: gain 1 Energy. |
| Maelle's Locket | Rare | Start of combat: enter Offensive + gain 1 Energy. |
| Medalum | Rare | Start of combat: enter Virtuose. Attacks in Virtuose apply 1 Burn. |
| Gommage Core | Boss | Gradient Charges persist between combats (max 15). |

## Installation

1. Install ModTheSpire, BaseMod, and StSLib.
2. Copy the compiled `.jar` to your Slay the Spire mods folder.
3. Enable "The Paintress" in the ModTheSpire mod list.

## Building from Source

Requires Java 8+ and Maven.

```bash
mvn package
```

## Credits

Based on the ProTemplate by DarkVexon. Character and mechanics inspired by *Clair Obscur: Expedition 33* by Sandfall Interactive.
