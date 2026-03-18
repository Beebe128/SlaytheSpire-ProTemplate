# Paintress Mod — Developer Tools

## image_manager.py

A browser-based image management tool for reviewing and replacing mod artwork.
No dependencies beyond the Python standard library (Python 3.6+).

### Usage

Run from the repo root:

```
python3 tools/image_manager.py
```

Or from inside the `tools/` directory:

```
cd tools
python3 image_manager.py
```

The tool auto-detects the `paintressResources/` directory by walking up parent
directories, so either location works.

It starts a local HTTP server on **port 8765** and opens
`http://localhost:8765` in your default browser automatically.

Press **Ctrl+C** in the terminal to stop the server.

### What it shows

| Tab | Content |
|-----|---------|
| **Cards** | All 80 cards from `Cardstrings.json`. No art files exist yet — the game uses CardArtRoller fallback at runtime. Cards show a "NO ART — FALLBACK" badge. |
| **Powers** | All 21 powers from `Powerstrings.json`. Shows both the 32px and 84px placeholder images side-by-side. |
| **Relics** | All 9 relics from `Relicstrings.json`. Shows the main image and the Outline variant. Includes flavor text. |
| **Character** | The 6 fixed sprite files: `shoulder.png`, `shoulder2.png`, `corpse.png`, `main.png`, `charButton.png`, `charBG.png`. |

### Replacing artwork

Drag an image file from your filesystem and drop it onto the drop zone beneath
any asset. Alternatively click the drop zone to open a file picker.

Accepted formats: PNG, JPEG, WebP.

The file is saved directly to the correct path inside `paintressResources/images/`
and the preview refreshes immediately. No restart required.

### Endpoints (internal)

| Method | Path | Purpose |
|--------|------|---------|
| `GET` | `/` | Main HTML page (data embedded at render time) |
| `GET` | `/image?path=images/relics/ArtistsPalette.png` | Serve an image file |
| `POST` | `/upload` | Receive multipart file, save to mod resources |

### Notes

- The server binds to `127.0.0.1` only — it is not accessible from other machines.
- Uploads are limited to 50 MB.
- Path traversal attacks are blocked server-side.
- The page rebuilds the data model on every page load, so newly added
  localization entries appear without restarting the tool.
