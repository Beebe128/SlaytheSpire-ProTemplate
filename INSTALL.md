# How to Install — The Paintress (Maelle) Mod

This guide walks you through installing the Paintress mod step by step.
No technical experience required!

---

## What You Need

- **Slay the Spire** (purchased on [Steam](https://store.steampowered.com/app/646570/Slay_the_Spire/))
- **ModTheSpire** — the mod loader for STS
- **BaseMod** — required framework for character mods
- **StSLib** — required utility library
- **ThePaintress.jar** — the mod file itself

---

## Step 1 — Install the Mod Loader (ModTheSpire)

ModTheSpire lets Slay the Spire load mods. Install it from Steam Workshop:

1. Open Steam and go to the [Slay the Spire Workshop](https://steamcommunity.com/app/646570/workshop/)
2. Search for **"ModTheSpire"** and click **Subscribe**
3. Steam will download it automatically

> **Alternative (manual install):** Download `ModTheSpire.jar` from the [ModTheSpire GitHub Releases](https://github.com/kiooeht/ModTheSpire/releases) and place it in your Slay the Spire folder.

---

## Step 2 — Install BaseMod

BaseMod is required by almost all character mods.

1. In the [Steam Workshop](https://steamcommunity.com/app/646570/workshop/), search for **"BaseMod"** and click **Subscribe**
2. Steam downloads it automatically alongside ModTheSpire

---

## Step 3 — Install StSLib

StSLib provides additional mod utilities.

1. In the [Steam Workshop](https://steamcommunity.com/app/646570/workshop/), search for **"StSLib"** and click **Subscribe**

---

## Step 4 — Download The Paintress Mod

1. Go to the [GitHub repository](https://github.com/Beebe128/SlaytheSpire-ProTemplate)
2. Click **Releases** on the right sidebar (or go to the `Releases` tab)
3. Download **`ThePaintress.jar`** from the latest release

> **Can't find a release?** See the "Building from Source" section at the bottom of this guide.

---

## Step 5 — Find Your Slay the Spire Mods Folder

The `mods` folder is inside your Slay the Spire installation directory.

### On Windows:
1. Open Steam → Right-click **Slay the Spire** → **Manage** → **Browse local files**
2. This opens the game folder. Look for a folder called **`mods`**
3. If `mods` doesn't exist yet, **create it** — make a new folder named `mods`

Default path (may vary):
```
C:\Program Files (x86)\Steam\steamapps\common\SlayTheSpire\mods\
```

### On Mac:
```
~/Library/Application Support/Steam/steamapps/common/SlayTheSpire/mods/
```

### On Linux:
```
~/.steam/steam/steamapps/common/SlayTheSpire/mods/
```

---

## Step 6 — Place the Mod File

Copy **`ThePaintress.jar`** into the `mods` folder you found in Step 5.

Your mods folder should look something like this:
```
mods/
  ThePaintress.jar
```

---

## Step 7 — Launch the Game with Mods

1. Open **Steam**
2. In your library, find **Slay the Spire**
3. Click the **down arrow** next to "Play" → Select **"Play with Mods (ModTheSpire)"**

   *(If you don't see this option, ModTheSpire may not be installed correctly — go back to Step 1)*

4. The **ModTheSpire launcher** opens showing a list of available mods
5. Check the boxes next to:
   - ✅ BaseMod
   - ✅ StSLib
   - ✅ **The Paintress**
6. Click **"Play"**

---

## Step 8 — Select Maelle in Character Select

1. Start a new run
2. In the character select screen, scroll right past the default characters
3. You should see **Maelle (The Paintress)** as a new option
4. Select her and begin your run!

---

## Troubleshooting

**The mod doesn't appear in the ModTheSpire launcher:**
- Make sure `ThePaintress.jar` is in the `mods` folder (not a subfolder inside `mods`)
- Try restarting Steam and launching again

**The game crashes on startup:**
- Make sure BaseMod and StSLib are both enabled and up to date
- Check the ModTheSpire log file: `SlayTheSpire/mods/mts-log.txt`

**Maelle doesn't appear in character select:**
- Make sure all three mods (BaseMod, StSLib, The Paintress) are checked in the launcher

**Card art looks like base-game cards with a color tint:**
- This is normal! The mod uses a fallback art system while custom card art is being created. The game is fully playable with placeholder art.

---

## Building from Source (Advanced)

If no compiled `.jar` release is available yet, you can build it yourself — but you need Slay the Spire installed.

**Requirements:**
- Java 8 JDK
- Apache Maven
- Slay the Spire installed on Steam

**Steps:**
1. Clone the repository:
   ```
   git clone https://github.com/Beebe128/SlaytheSpire-ProTemplate.git
   cd SlaytheSpire-ProTemplate
   git checkout claude/paintress-character-mod-MJ4XP
   ```

2. Open `pom.xml` and update the `<Steam.path>` property to your Steam installation path:
   ```xml
   <!-- Windows -->
   <Steam.path>C:/Program Files (x86)/Steam/steamapps/</Steam.path>

   <!-- Mac -->
   <Steam.path>/Users/YourName/Library/Application Support/Steam/steamapps/</Steam.path>

   <!-- Linux -->
   <Steam.path>/home/yourname/.steam/steam/steamapps/</Steam.path>
   ```

3. Build the mod:
   ```
   mvn package
   ```

4. Find the compiled mod at `target/ThePaintress.jar`

5. Copy it to your `mods` folder (Step 5 above)

---

## Uninstalling

To remove the mod:
1. Delete `ThePaintress.jar` from your `mods` folder
2. Any save files created with Maelle will no longer load — make sure to finish or abandon those runs first

---

*Mod based on Maelle from Clair Obscur: Expedition 33 by Sandfall Interactive.*
*Built using the ProTemplate by DarkVexon.*
