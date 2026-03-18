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

If no compiled `.jar` release is available yet, you can build it yourself. This requires a bit of setup but is straightforward if you follow each step.

---

### What you need

**Java 8 JDK** — the programming language the mod is written in.
**Apache Maven** — a build tool. Think of it as a "compiler + packager" that reads the project recipe (`pom.xml`) and produces the `.jar` file.
**Git** — to download the source code.
**Slay the Spire** installed on Steam (the build needs to reference the game's own `.jar` files).

---

### Step A — Install Java 8 JDK

The mod requires Java **8** specifically (not 11, not 17 — the game itself runs on Java 8).

**Windows / Mac:**
1. Go to [Adoptium](https://adoptium.net/temurin/releases/?version=8) and download the **JDK 8** installer for your OS
2. Run the installer, click through the defaults
3. To confirm it worked, open a terminal (Command Prompt on Windows, Terminal on Mac) and type:
   ```
   java -version
   ```
   You should see something like `openjdk version "1.8.0_..."`. The `1.8` means Java 8.

**Linux:**
```
sudo apt install openjdk-8-jdk    # Debian/Ubuntu
sudo pacman -S jdk8-openjdk       # Arch
```

---

### Step B — Install Apache Maven

Maven is what turns the source code into a `.jar` file.

**Windows:**
1. Download the binary zip from [maven.apache.org/download.cgi](https://maven.apache.org/download.cgi) — grab the file ending in `-bin.zip`
2. Unzip it somewhere permanent, like `C:\Program Files\Maven\`
3. Add Maven's `bin` folder to your system PATH:
   - Search "environment variables" in the Start menu → Edit the system environment variables
   - Under "System variables", find `Path` → Edit → New → paste the path to Maven's `bin` folder (e.g. `C:\Program Files\Maven\apache-maven-3.x.x\bin`)
4. Open a new Command Prompt and confirm:
   ```
   mvn -version
   ```

**Mac (using Homebrew):**
```
brew install maven
```

**Linux:**
```
sudo apt install maven      # Debian/Ubuntu
sudo pacman -S maven        # Arch
```

Confirm it works: `mvn -version` — you should see version info printed out.

---

### Step C — Download the source code

Open a terminal and run:

```
git clone https://github.com/Beebe128/SlaytheSpire-ProTemplate.git
cd SlaytheSpire-ProTemplate
git checkout claude/paintress-character-mod-MJ4XP
```

This downloads the source code and switches to the correct branch.

---

### Step D — Point the build at your Steam folder

The mod needs to find Slay the Spire's game files to compile against. Open `pom.xml` in any text editor (Notepad works fine) and find this line:

```xml
<Steam.path>C:/Program Files (x86)/Steam/steamapps/</Steam.path>
```

Replace that path with wherever Steam is installed on your machine:

**Windows (default):**
```xml
<Steam.path>C:/Program Files (x86)/Steam/steamapps/</Steam.path>
```

**Mac:**
```xml
<Steam.path>/Users/YOUR_USERNAME/Library/Application Support/Steam/steamapps/</Steam.path>
```

**Linux:**
```xml
<Steam.path>/home/YOUR_USERNAME/.steam/steam/steamapps/</Steam.path>
```

Replace `YOUR_USERNAME` with your actual username. Save the file.

---

### Step E — Build the mod

In the terminal, make sure you're inside the `SlaytheSpire-ProTemplate` folder, then run:

```
mvn package
```

**What this does:** Maven reads `pom.xml` (the project recipe), compiles all the Java source files, and bundles everything — the compiled code plus the image/audio resources — into a single `.jar` file. This is the mod file.

It will print a lot of output as it works. When it finishes you should see:
```
[INFO] BUILD SUCCESS
```

If you see `BUILD FAILURE`, the most common causes are:
- Wrong Steam path in `pom.xml` — double-check it points to a folder that contains `common/SlayTheSpire/`
- Wrong Java version — run `java -version` and confirm it says `1.8`

---

### Step F — Copy the mod file

The finished mod is at:
```
target/ThePaintress.jar
```

Copy this file to your `mods` folder (see Step 5 in the main install guide above).

---

## Uninstalling

To remove the mod:
1. Delete `ThePaintress.jar` from your `mods` folder
2. Any save files created with Maelle will no longer load — make sure to finish or abandon those runs first

---

*Mod based on Maelle from Clair Obscur: Expedition 33 by Sandfall Interactive.*
*Built using the ProTemplate by DarkVexon.*
