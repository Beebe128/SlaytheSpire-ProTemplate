#!/usr/bin/env python3
"""
Paintress Mod — Image Manager
A local HTTP tool for managing mod artwork assets.
Run with: python3 tools/image_manager.py
"""

import json
import os
import sys
import threading
import webbrowser
from http.server import BaseHTTPRequestHandler, HTTPServer
from pathlib import Path
from urllib.parse import parse_qs, urlparse
import email
import io

# ---------------------------------------------------------------------------
# Path resolution — works whether run from repo root or tools/ subdir
# ---------------------------------------------------------------------------

def find_repo_root() -> Path:
    """Walk up from this script until we find paintressResources."""
    candidate = Path(__file__).resolve().parent
    for _ in range(5):
        resources = candidate / "src" / "main" / "resources" / "paintressResources"
        if resources.is_dir():
            return candidate
        candidate = candidate.parent
    raise FileNotFoundError(
        "Could not locate paintressResources/ — run from the repo root or tools/ directory."
    )

REPO_ROOT = find_repo_root()
RESOURCES = REPO_ROOT / "src" / "main" / "resources" / "paintressResources"
LOCALIZATION = RESOURCES / "localization" / "eng"
IMAGES = RESOURCES / "images"
AUDIO = RESOURCES / "audio"

# ---------------------------------------------------------------------------
# Data model construction
# ---------------------------------------------------------------------------

def load_json(path: Path) -> dict:
    try:
        with open(path, encoding="utf-8") as f:
            return json.load(f)
    except Exception as e:
        print(f"Warning: could not load {path}: {e}")
        return {}


def strip_prefix(key: str) -> str:
    """Remove 'paintress:' or '${ModID}:' prefix from a localization key."""
    if ":" in key:
        return key.split(":", 1)[1]
    return key


def build_data_model() -> dict:
    """Build the full asset manifest from JSON files and filesystem."""

    # ---- Cards ----
    card_strings = load_json(LOCALIZATION / "Cardstrings.json")
    cards = []
    for key, val in card_strings.items():
        card_id = strip_prefix(key)
        image_rel = f"images/cards/{card_id}.png"
        image_abs = IMAGES / "cards" / f"{card_id}.png"
        cards.append({
            "id": card_id,
            "name": val.get("NAME", card_id),
            "description": val.get("DESCRIPTION", ""),
            "image_rel": image_rel,
            "has_file": image_abs.exists(),
            "category": "cards",
        })
    cards.sort(key=lambda c: c["name"])

    # ---- Powers ----
    power_strings = load_json(LOCALIZATION / "Powerstrings.json")
    powers_dir = IMAGES / "powers"
    # Group files by base name (strip trailing 32/84)
    power_files: dict[str, list[str]] = {}
    if powers_dir.is_dir():
        for f in sorted(powers_dir.iterdir()):
            if f.suffix.lower() == ".png":
                stem = f.stem
                # strip size suffix
                base = stem.rstrip("0123456789")
                if base not in power_files:
                    power_files[base] = []
                power_files[base].append(f.name)

    powers = []
    for key, val in power_strings.items():
        power_id = strip_prefix(key)
        desc_parts = val.get("DESCRIPTIONS", [])
        description = "".join(desc_parts)
        sizes = []
        for size in ["32", "84"]:
            fname = f"{power_id}{size}.png"
            fpath = powers_dir / fname
            sizes.append({
                "label": f"{size}px",
                "filename": fname,
                "image_rel": f"images/powers/{fname}",
                "has_file": fpath.exists(),
            })
        powers.append({
            "id": power_id,
            "name": val.get("NAME", power_id),
            "description": description,
            "sizes": sizes,
            "category": "powers",
        })
    powers.sort(key=lambda p: p["name"])

    # ---- Relics ----
    relic_strings = load_json(LOCALIZATION / "Relicstrings.json")
    relics_dir = IMAGES / "relics"
    relics = []
    for key, val in relic_strings.items():
        relic_id = strip_prefix(key)
        desc_parts = val.get("DESCRIPTIONS", [])
        description = "".join(desc_parts)
        flavor = val.get("FLAVOR", "")
        variants = []
        for suffix, label in [("", "Main"), ("Outline", "Outline")]:
            fname = f"{relic_id}{suffix}.png"
            fpath = relics_dir / fname
            variants.append({
                "label": label,
                "filename": fname,
                "image_rel": f"images/relics/{fname}",
                "has_file": fpath.exists(),
            })
        relics.append({
            "id": relic_id,
            "name": val.get("NAME", relic_id),
            "description": description,
            "flavor": flavor,
            "variants": variants,
            "category": "relics",
        })
    relics.sort(key=lambda r: r["name"])

    # ---- Character sprites ----
    char_assets = [
        {
            "id": "shoulder",
            "filename": "shoulder.png",
            "image_rel": "images/char/mainChar/shoulder.png",
            "purpose": "Character left shoulder / combat idle sprite",
            "has_file": (IMAGES / "char" / "mainChar" / "shoulder.png").exists(),
        },
        {
            "id": "shoulder2",
            "filename": "shoulder2.png",
            "image_rel": "images/char/mainChar/shoulder2.png",
            "purpose": "Character right shoulder / combat alt sprite",
            "has_file": (IMAGES / "char" / "mainChar" / "shoulder2.png").exists(),
        },
        {
            "id": "corpse",
            "filename": "corpse.png",
            "image_rel": "images/char/mainChar/corpse.png",
            "purpose": "Character death / corpse sprite",
            "has_file": (IMAGES / "char" / "mainChar" / "corpse.png").exists(),
        },
        {
            "id": "main",
            "filename": "main.png",
            "image_rel": "images/char/mainChar/main.png",
            "purpose": "Main character spritesheet (combat animations)",
            "has_file": (IMAGES / "char" / "mainChar" / "main.png").exists(),
        },
        {
            "id": "charButton",
            "filename": "charButton.png",
            "image_rel": "images/charSelect/charButton.png",
            "purpose": "Character select button portrait",
            "has_file": (IMAGES / "charSelect" / "charButton.png").exists(),
        },
        {
            "id": "charBG",
            "filename": "charBG.png",
            "image_rel": "images/charSelect/charBG.png",
            "purpose": "Character select background panel",
            "has_file": (IMAGES / "charSelect" / "charBG.png").exists(),
        },
    ]

    # ---- Sounds ----
    # Sounds registered in ProAudio.java → paintressResources/audio/*.ogg
    sounds_dir = AUDIO
    KNOWN_SOUNDS = [
        {"id": "attack_slash",    "label": "Attack — Slash"},
        {"id": "attack_fire",     "label": "Attack — Fire / Burn"},
        {"id": "stance_defensive","label": "Stance Enter — Defensive"},
        {"id": "stance_offensive","label": "Stance Enter — Offensive"},
        {"id": "stance_virtuose", "label": "Stance Enter — Virtuose"},
        {"id": "gradient_gain",   "label": "Gradient Charge — Gain"},
        {"id": "gradient_spend",  "label": "Gradient Charge — Spend"},
        {"id": "parry_trigger",   "label": "Parry Status — Trigger"},
        {"id": "phoenix_flame",   "label": "Phoenix Flame — Cast"},
        {"id": "gommage",         "label": "Gommage — Cast"},
        {"id": "virtuose_strike", "label": "Virtuose Strike — Cast"},
    ]
    sounds = []
    for s in KNOWN_SOUNDS:
        fname = f"{s['id']}.ogg"
        fpath = sounds_dir / fname if sounds_dir.is_dir() else Path("/nonexistent") / fname
        sounds.append({
            "id": s["id"],
            "label": s["label"],
            "filename": fname,
            "audio_rel": f"audio/{fname}",
            "has_file": fpath.exists(),
            "category": "sounds",
        })
    # Also pick up any extra .ogg files already present
    if sounds_dir.is_dir():
        known_ids = {s["id"] for s in KNOWN_SOUNDS}
        for f in sorted(sounds_dir.iterdir()):
            if f.suffix.lower() == ".ogg" and f.stem not in known_ids:
                sounds.append({
                    "id": f.stem,
                    "label": f.stem,
                    "filename": f.name,
                    "audio_rel": f"audio/{f.name}",
                    "has_file": True,
                    "category": "sounds",
                })

    return {
        "cards": cards,
        "powers": powers,
        "relics": relics,
        "character": char_assets,
        "sounds": sounds,
    }


# ---------------------------------------------------------------------------
# Multipart form-data parser (stdlib only)
# ---------------------------------------------------------------------------

def parse_multipart(rfile, content_type: str, content_length: int):
    """Parse a multipart/form-data POST. Returns dict of field_name -> (filename, bytes)."""
    body = rfile.read(content_length)
    # Build a fake email message so we can reuse email.parser
    headers_str = f"Content-Type: {content_type}\r\n\r\n"
    msg = email.message_from_bytes(headers_str.encode() + body)
    fields = {}
    if msg.get_content_maintype() == "multipart":
        for part in msg.walk():
            if part.get_content_maintype() == "multipart":
                continue
            disp = part.get("Content-Disposition", "")
            if not disp:
                continue
            # Extract name and filename
            name = None
            filename = None
            for segment in disp.split(";"):
                segment = segment.strip()
                if segment.startswith("name="):
                    name = segment[5:].strip('"')
                elif segment.startswith("filename="):
                    filename = segment[9:].strip('"')
            if name:
                payload = part.get_payload(decode=True)
                fields[name] = (filename, payload if payload is not None else b"")
    return fields


# ---------------------------------------------------------------------------
# HTML (embedded as a string)
# ---------------------------------------------------------------------------

HTML = r"""<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Paintress Mod — Asset Manager</title>
<style>
  :root {
    --bg:        #0d0b14;
    --surface:   #16122a;
    --surface2:  #1e1a36;
    --border:    #2e2850;
    --accent:    #7b5ea7;
    --accent2:   #a97bce;
    --text:      #e8e0f0;
    --text-dim:  #8a82a0;
    --red:       #e05252;
    --orange:    #d4873a;
    --green:     #52c97e;
    --drop-bg:   #1a1530;
    --drop-border: #3a3060;
    --drop-hover: #5a3090;
  }

  * { box-sizing: border-box; margin: 0; padding: 0; }

  body {
    background: var(--bg);
    color: var(--text);
    font-family: 'Segoe UI', system-ui, sans-serif;
    min-height: 100vh;
  }

  /* ---------- Header ---------- */
  header {
    background: linear-gradient(135deg, #1a1035 0%, #2a1a50 50%, #1a1035 100%);
    border-bottom: 2px solid var(--accent);
    padding: 20px 32px;
    display: flex;
    align-items: center;
    gap: 20px;
    position: sticky;
    top: 0;
    z-index: 100;
    box-shadow: 0 4px 24px rgba(0,0,0,0.6);
  }

  .header-icon {
    font-size: 2rem;
    filter: drop-shadow(0 0 8px var(--accent));
  }

  header h1 {
    font-size: 1.5rem;
    font-weight: 700;
    letter-spacing: 0.05em;
    background: linear-gradient(90deg, #c0a0ff, #e8d0ff);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }

  header p {
    color: var(--text-dim);
    font-size: 0.85rem;
    margin-top: 2px;
  }

  .header-stats {
    margin-left: auto;
    display: flex;
    gap: 16px;
    font-size: 0.8rem;
    color: var(--text-dim);
  }

  .stat-chip {
    background: var(--surface2);
    border: 1px solid var(--border);
    border-radius: 12px;
    padding: 4px 12px;
    display: flex;
    align-items: center;
    gap: 6px;
  }

  .stat-chip span { color: var(--text); font-weight: 600; }

  /* ---------- Filter tabs ---------- */
  .tabs {
    display: flex;
    gap: 4px;
    padding: 16px 32px 0;
    border-bottom: 1px solid var(--border);
    background: var(--surface);
    position: sticky;
    top: 81px;
    z-index: 90;
  }

  .tab {
    padding: 10px 20px;
    border: none;
    background: transparent;
    color: var(--text-dim);
    cursor: pointer;
    font-size: 0.9rem;
    font-weight: 600;
    letter-spacing: 0.03em;
    border-bottom: 3px solid transparent;
    margin-bottom: -1px;
    transition: color 0.15s, border-color 0.15s;
    border-radius: 4px 4px 0 0;
  }

  .tab:hover { color: var(--text); background: var(--surface2); }
  .tab.active { color: var(--accent2); border-bottom-color: var(--accent2); }

  .tab .count {
    display: inline-block;
    background: var(--surface2);
    border-radius: 10px;
    padding: 1px 7px;
    font-size: 0.75rem;
    margin-left: 6px;
    color: var(--text-dim);
  }

  .tab.active .count { background: var(--accent); color: #fff; }

  /* ---------- Main content ---------- */
  main {
    padding: 24px 32px;
    max-width: 1600px;
    margin: 0 auto;
  }

  .section-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 20px;
    margin-top: 8px;
  }

  .section-header h2 {
    font-size: 1.1rem;
    font-weight: 700;
    color: var(--accent2);
    letter-spacing: 0.05em;
    text-transform: uppercase;
  }

  .section-divider {
    flex: 1;
    height: 1px;
    background: var(--border);
  }

  /* ---------- Grid ---------- */
  .grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    gap: 20px;
    margin-bottom: 48px;
  }

  /* ---------- Asset card ---------- */
  .asset-card {
    background: var(--surface);
    border: 1px solid var(--border);
    border-radius: 12px;
    overflow: hidden;
    transition: border-color 0.2s, box-shadow 0.2s;
    display: flex;
    flex-direction: column;
  }

  .asset-card:hover {
    border-color: var(--accent);
    box-shadow: 0 4px 20px rgba(123,94,167,0.2);
  }

  /* ---------- Image preview ---------- */
  .preview-wrap {
    background: var(--surface2);
    display: flex;
    align-items: center;
    justify-content: center;
    height: 160px;
    position: relative;
    overflow: hidden;
    border-bottom: 1px solid var(--border);
  }

  .preview-wrap.no-art {
    background: linear-gradient(135deg, #0d0b14, #1a1030);
  }

  .preview-wrap img {
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
    image-rendering: pixelated;
    display: block;
  }

  .no-art-box {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 8px;
    color: var(--text-dim);
    font-size: 0.75rem;
    letter-spacing: 0.05em;
    text-transform: uppercase;
    opacity: 0.6;
  }

  .no-art-box .icon { font-size: 2.5rem; opacity: 0.4; }

  /* ---------- Card body ---------- */
  .card-body {
    padding: 14px 16px 12px;
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 8px;
  }

  .card-title-row {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 8px;
  }

  .card-name {
    font-size: 0.95rem;
    font-weight: 700;
    color: var(--text);
    line-height: 1.3;
  }

  .badges { display: flex; gap: 5px; flex-wrap: wrap; align-items: center; }

  .badge {
    font-size: 0.62rem;
    font-weight: 700;
    letter-spacing: 0.08em;
    padding: 2px 7px;
    border-radius: 4px;
    text-transform: uppercase;
    white-space: nowrap;
  }

  .badge-placeholder { background: #7a4010; color: #ffb066; border: 1px solid #a05020; }
  .badge-noart       { background: #5a1010; color: #ff8080; border: 1px solid #801818; }
  .badge-fallback    { background: #1a3a5a; color: #80c0ff; border: 1px solid #205080; }

  .card-desc {
    font-size: 0.78rem;
    color: var(--text-dim);
    line-height: 1.5;
    flex: 1;
  }

  .card-path {
    font-size: 0.68rem;
    color: #4a4268;
    font-family: monospace;
    word-break: break-all;
    margin-top: 4px;
  }

  /* ---------- Multi-size row (powers/relics) ---------- */
  .variants-row {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 10px;
    margin-top: 4px;
  }

  .variant-item {
    display: flex;
    flex-direction: column;
    gap: 6px;
  }

  .variant-label {
    font-size: 0.7rem;
    color: var(--text-dim);
    text-transform: uppercase;
    letter-spacing: 0.06em;
    font-weight: 600;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .mini-preview {
    background: var(--surface2);
    border: 1px solid var(--border);
    border-radius: 6px;
    height: 70px;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
  }

  .mini-preview img {
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
    image-rendering: pixelated;
  }

  .mini-preview.no-art { opacity: 0.5; font-size: 0.65rem; color: var(--text-dim); }

  /* ---------- Drop zone ---------- */
  .drop-zone {
    border: 2px dashed var(--drop-border);
    border-radius: 8px;
    padding: 10px 8px;
    text-align: center;
    cursor: pointer;
    font-size: 0.72rem;
    color: var(--text-dim);
    transition: border-color 0.2s, background 0.2s, color 0.2s;
    background: var(--drop-bg);
    margin-top: 6px;
    position: relative;
  }

  .drop-zone:hover,
  .drop-zone.drag-over {
    border-color: var(--drop-hover);
    background: #221840;
    color: var(--accent2);
  }

  .drop-zone input[type="file"] {
    position: absolute;
    inset: 0;
    opacity: 0;
    cursor: pointer;
    width: 100%;
    height: 100%;
  }

  .drop-zone .dz-icon { font-size: 1.2rem; display: block; margin-bottom: 3px; opacity: 0.7; }

  /* ---------- Toast ---------- */
  #toast-container {
    position: fixed;
    bottom: 24px;
    right: 24px;
    z-index: 999;
    display: flex;
    flex-direction: column;
    gap: 10px;
    pointer-events: none;
  }

  .toast {
    background: var(--surface2);
    border: 1px solid var(--border);
    border-radius: 8px;
    padding: 12px 18px;
    font-size: 0.85rem;
    display: flex;
    align-items: center;
    gap: 10px;
    box-shadow: 0 4px 20px rgba(0,0,0,0.6);
    animation: slideIn 0.25s ease;
    max-width: 360px;
  }

  .toast.success { border-color: var(--green); }
  .toast.error   { border-color: var(--red); }

  @keyframes slideIn {
    from { transform: translateX(40px); opacity: 0; }
    to   { transform: translateX(0); opacity: 1; }
  }

  @keyframes fadeOut {
    to { opacity: 0; transform: translateY(8px); }
  }

  .toast.hiding { animation: fadeOut 0.3s ease forwards; }

  /* ---------- Scrollbar ---------- */
  ::-webkit-scrollbar { width: 8px; }
  ::-webkit-scrollbar-track { background: var(--bg); }
  ::-webkit-scrollbar-thumb { background: var(--border); border-radius: 4px; }
  ::-webkit-scrollbar-thumb:hover { background: var(--accent); }

  /* ---------- Responsive ---------- */
  @media (max-width: 700px) {
    header { padding: 14px 16px; }
    main { padding: 16px; }
    .tabs { padding: 12px 16px 0; }
    header h1 { font-size: 1.1rem; }
    .header-stats { display: none; }
  }
</style>
</head>
<body>

<header>
  <div class="header-icon">🎨</div>
  <div>
    <h1>Paintress Mod — Asset Manager</h1>
    <p>Drag &amp; drop art images or sound files onto any asset below</p>
  </div>
  <div class="header-stats" id="header-stats"></div>
</header>

<div class="tabs" id="tabs">
  <button class="tab active" data-filter="all">All <span class="count" id="cnt-all">…</span></button>
  <button class="tab" data-filter="cards">Cards <span class="count" id="cnt-cards">…</span></button>
  <button class="tab" data-filter="powers">Powers <span class="count" id="cnt-powers">…</span></button>
  <button class="tab" data-filter="relics">Relics <span class="count" id="cnt-relics">…</span></button>
  <button class="tab" data-filter="character">Character <span class="count" id="cnt-character">…</span></button>
  <button class="tab" data-filter="sounds">Sounds <span class="count" id="cnt-sounds">…</span></button>
</div>

<main id="main"></main>
<div id="toast-container"></div>

<script>
// ===== Data (injected by server) =====
const DATA = __DATA_JSON__;

// ===== Helpers =====
function imgUrl(rel, bust) {
  return '/image?path=' + encodeURIComponent(rel) + (bust ? '&t=' + Date.now() : '');
}

function badge(cls, text) {
  return `<span class="badge ${cls}">${text}</span>`;
}

function escHtml(s) {
  return String(s).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
}

// ===== Drop zone =====
function makeDropZone(targetRel, labelText, afterUpload) {
  const id = 'dz-' + targetRel.replace(/[^a-z0-9]/gi,'_');
  const html = `
    <div class="drop-zone" id="${id}" data-path="${escHtml(targetRel)}">
      <input type="file" accept="image/png,image/jpeg,image/webp" tabindex="-1">
      <span class="dz-icon">⬆</span>
      ${escHtml(labelText)}
    </div>`;
  // Attach events after insertion
  requestAnimationFrame(() => {
    const el = document.getElementById(id);
    if (!el) return;
    const inp = el.querySelector('input[type="file"]');

    el.addEventListener('dragover', e => { e.preventDefault(); el.classList.add('drag-over'); });
    el.addEventListener('dragleave', () => el.classList.remove('drag-over'));
    el.addEventListener('drop', e => {
      e.preventDefault();
      el.classList.remove('drag-over');
      const file = e.dataTransfer?.files?.[0];
      if (file) uploadFile(file, targetRel, afterUpload);
    });
    inp.addEventListener('change', () => {
      if (inp.files?.[0]) uploadFile(inp.files[0], targetRel, afterUpload);
      inp.value = '';
    });
  });
  return html;
}

// ===== Upload =====
async function uploadFile(file, targetRel, afterUpload) {
  const fd = new FormData();
  fd.append('path', targetRel);
  fd.append('file', file);
  try {
    const res = await fetch('/upload', { method: 'POST', body: fd });
    const json = await res.json();
    if (json.ok) {
      showToast('success', '✓ Uploaded ' + file.name + ' → ' + targetRel.split('/').pop());
      if (afterUpload) afterUpload();
    } else {
      showToast('error', '✗ Upload failed: ' + (json.error || 'unknown error'));
    }
  } catch(e) {
    showToast('error', '✗ Network error: ' + e.message);
  }
}

// ===== Toast =====
function showToast(type, msg) {
  const c = document.getElementById('toast-container');
  const t = document.createElement('div');
  t.className = 'toast ' + type;
  t.textContent = msg;
  c.appendChild(t);
  setTimeout(() => {
    t.classList.add('hiding');
    setTimeout(() => t.remove(), 350);
  }, 3500);
}

// ===== Render helpers =====
function renderPreview(rel, hasFile, size='160px') {
  if (!hasFile) {
    return `<div class="preview-wrap no-art" style="height:${size}">
      <div class="no-art-box">
        <span class="icon">🖼</span>
        <span>No file</span>
      </div>
    </div>`;
  }
  const imgId = 'img-' + rel.replace(/[^a-z0-9]/gi,'_');
  return `<div class="preview-wrap" style="height:${size}">
    <img id="${imgId}" src="${imgUrl(rel)}" alt="" loading="lazy">
  </div>`;
}

function refreshImg(rel) {
  const imgId = 'img-' + rel.replace(/[^a-z0-9]/gi,'_');
  const el = document.getElementById(imgId);
  if (el) el.src = imgUrl(rel, true);
}

// ===== Cards section =====
function renderCards() {
  const cards = DATA.cards;
  if (!cards.length) return '';
  let html = `<div class="section-header"><h2>Cards</h2><div class="section-divider"></div></div>
  <div class="grid" id="section-cards">`;
  for (const c of cards) {
    const hasFile = c.has_file;
    const badgeHtml = hasFile
      ? badge('badge-placeholder', 'PLACEHOLDER')
      : badge('badge-fallback', 'NO ART — FALLBACK');
    const dropHtml = makeDropZone(c.image_rel, 'Drop image or click to browse', () => refreshImg(c.image_rel));
    html += `<div class="asset-card" data-category="cards">
      ${renderPreview(c.image_rel, hasFile)}
      <div class="card-body">
        <div class="card-title-row">
          <div class="card-name">${escHtml(c.name)}</div>
          <div class="badges">${badgeHtml}</div>
        </div>
        <div class="card-desc">${escHtml(c.description)}</div>
        <div class="card-path">${escHtml(c.image_rel)}</div>
        ${dropHtml}
      </div>
    </div>`;
  }
  html += '</div>';
  return html;
}

// ===== Powers section =====
function renderPowers() {
  const powers = DATA.powers;
  if (!powers.length) return '';
  let html = `<div class="section-header"><h2>Powers</h2><div class="section-divider"></div></div>
  <div class="grid" id="section-powers">`;
  for (const p of powers) {
    const anyPlaceholder = p.sizes.some(s => s.has_file);
    const badgeHtml = anyPlaceholder ? badge('badge-placeholder','PLACEHOLDER') : badge('badge-noart','NO ART');

    // Mini previews + drop zones for 32px and 84px
    let variantsHtml = '<div class="variants-row">';
    for (const s of p.sizes) {
      const dropHtml = makeDropZone(s.image_rel, `Drop ${s.label}`, () => refreshImg(s.image_rel));
      const imgId = 'img-' + s.image_rel.replace(/[^a-z0-9]/gi,'_');
      const miniHtml = s.has_file
        ? `<div class="mini-preview"><img id="${imgId}" src="${imgUrl(s.image_rel)}" alt="" loading="lazy"></div>`
        : `<div class="mini-preview no-art">No file</div>`;
      variantsHtml += `<div class="variant-item">
        <div class="variant-label">
          <span>${escHtml(s.label)}</span>
          ${s.has_file ? badge('badge-placeholder','PLH') : badge('badge-noart','MISSING')}
        </div>
        ${miniHtml}
        ${dropHtml}
      </div>`;
    }
    variantsHtml += '</div>';

    html += `<div class="asset-card" data-category="powers">
      <div class="card-body">
        <div class="card-title-row">
          <div class="card-name">${escHtml(p.name)}</div>
          <div class="badges">${badgeHtml}</div>
        </div>
        <div class="card-desc">${escHtml(p.description)}</div>
        ${variantsHtml}
      </div>
    </div>`;
  }
  html += '</div>';
  return html;
}

// ===== Relics section =====
function renderRelics() {
  const relics = DATA.relics;
  if (!relics.length) return '';
  let html = `<div class="section-header"><h2>Relics</h2><div class="section-divider"></div></div>
  <div class="grid" id="section-relics">`;
  for (const r of relics) {
    const anyPlaceholder = r.variants.some(v => v.has_file);
    const badgeHtml = anyPlaceholder ? badge('badge-placeholder','PLACEHOLDER') : badge('badge-noart','NO ART');

    let variantsHtml = '<div class="variants-row">';
    for (const v of r.variants) {
      const dropHtml = makeDropZone(v.image_rel, `Drop ${v.label}`, () => refreshImg(v.image_rel));
      const imgId = 'img-' + v.image_rel.replace(/[^a-z0-9]/gi,'_');
      const miniHtml = v.has_file
        ? `<div class="mini-preview"><img id="${imgId}" src="${imgUrl(v.image_rel)}" alt="" loading="lazy"></div>`
        : `<div class="mini-preview no-art">No file</div>`;
      variantsHtml += `<div class="variant-item">
        <div class="variant-label">
          <span>${escHtml(v.label)}</span>
          ${v.has_file ? badge('badge-placeholder','PLH') : badge('badge-noart','MISSING')}
        </div>
        ${miniHtml}
        ${dropHtml}
      </div>`;
    }
    variantsHtml += '</div>';

    const flavorHtml = r.flavor
      ? `<div class="card-desc" style="font-style:italic;color:#5a5278">"${escHtml(r.flavor)}"</div>` : '';

    html += `<div class="asset-card" data-category="relics">
      <div class="card-body">
        <div class="card-title-row">
          <div class="card-name">${escHtml(r.name)}</div>
          <div class="badges">${badgeHtml}</div>
        </div>
        <div class="card-desc">${escHtml(r.description)}</div>
        ${flavorHtml}
        ${variantsHtml}
      </div>
    </div>`;
  }
  html += '</div>';
  return html;
}

// ===== Character section =====
function renderCharacter() {
  const chars = DATA.character;
  if (!chars.length) return '';
  let html = `<div class="section-header"><h2>Character Sprites</h2><div class="section-divider"></div></div>
  <div class="grid" id="section-character">`;
  for (const ch of chars) {
    const badgeHtml = ch.has_file ? badge('badge-placeholder','PLACEHOLDER') : badge('badge-noart','NO ART');
    const dropHtml = makeDropZone(ch.image_rel, 'Drop image or click to browse', () => refreshImg(ch.image_rel));
    html += `<div class="asset-card" data-category="character">
      ${renderPreview(ch.image_rel, ch.has_file)}
      <div class="card-body">
        <div class="card-title-row">
          <div class="card-name">${escHtml(ch.filename)}</div>
          <div class="badges">${badgeHtml}</div>
        </div>
        <div class="card-desc">${escHtml(ch.purpose)}</div>
        <div class="card-path">${escHtml(ch.image_rel)}</div>
        ${dropHtml}
      </div>
    </div>`;
  }
  html += '</div>';
  return html;
}

// ===== Sounds section =====
function makeAudioDropZone(audioRel, labelText, afterUpload) {
  const id = 'adz-' + audioRel.replace(/[^a-z0-9]/gi,'_');
  const html = `
    <div class="drop-zone" id="${id}" data-path="${escHtml(audioRel)}">
      <input type="file" accept="audio/ogg,audio/*" tabindex="-1">
      <span class="dz-icon">🔊</span>
      ${escHtml(labelText)}
    </div>`;
  requestAnimationFrame(() => {
    const el = document.getElementById(id);
    if (!el) return;
    const inp = el.querySelector('input[type="file"]');
    el.addEventListener('dragover', e => { e.preventDefault(); el.classList.add('drag-over'); });
    el.addEventListener('dragleave', () => el.classList.remove('drag-over'));
    el.addEventListener('drop', e => {
      e.preventDefault();
      el.classList.remove('drag-over');
      const file = e.dataTransfer?.files?.[0];
      if (file) uploadAudio(file, audioRel, el, afterUpload);
    });
    inp.addEventListener('change', () => {
      if (inp.files?.[0]) uploadAudio(inp.files[0], audioRel, el, afterUpload);
      inp.value = '';
    });
  });
  return html;
}

async function uploadAudio(file, targetRel, dropEl, afterUpload) {
  const fd = new FormData();
  fd.append('path', 'audio/' + targetRel.replace('audio/', ''));
  fd.append('file', file);
  try {
    const res = await fetch('/upload-audio', { method: 'POST', body: fd });
    const json = await res.json();
    if (json.ok) {
      showToast('success', '✓ Uploaded ' + file.name);
      if (dropEl) dropEl.classList.add('has-file');
      if (afterUpload) afterUpload();
    } else {
      showToast('error', '✗ Upload failed: ' + (json.error || 'unknown'));
    }
  } catch(e) {
    showToast('error', '✗ Network error: ' + e.message);
  }
}

function renderSounds() {
  const sounds = DATA.sounds || [];
  let html = `<div class="section-header"><h2>Sounds</h2><div class="section-divider"></div></div>
  <div style="background:var(--surface2);border:1px solid var(--border);border-radius:10px;padding:16px 20px;margin-bottom:20px;font-size:0.82rem;color:var(--text-dim);line-height:1.7">
    <strong style="color:var(--accent2)">📁 Audio format:</strong> OGG Vorbis (.ogg) — place files in
    <code style="color:var(--text);background:var(--surface);padding:1px 6px;border-radius:4px">paintressResources/audio/</code><br>
    <strong style="color:var(--accent2)">🔧 Registration:</strong> Add the ID to <code style="color:var(--text);background:var(--surface);padding:1px 6px;border-radius:4px">paintress/util/ProAudio.java</code> enum,
    then reference via <code style="color:var(--text);background:var(--surface);padding:1px 6px;border-radius:4px">CardCrawlGame.sound.play(PaintressMod.makeID("YOUR_ID"))</code><br>
    <strong style="color:var(--accent2)">🎞 GIF Animations:</strong> STS uses <strong>Spine animations (.scml + .png atlas)</strong> for character sprites,
    not GIF files. For card art / power icons that animate, place a <code>.gif</code> where the <code>.png</code> would go —
    STS's LibGDX renderer supports animated GIFs via <code>AnimatedGifDecoder</code> or a custom loader.
    Character battle animations go in <code>paintressResources/images/char/mainChar/</code> as a Spine skeleton
    (<code>static.scml</code> is the current placeholder).
  </div>
  <div class="grid" id="section-sounds">`;
  for (const s of sounds) {
    const badgeHtml = s.has_file ? badge('badge-placeholder','HAS FILE') : badge('badge-noart','MISSING');
    const dropHtml = makeAudioDropZone(s.audio_rel, 'Drop .ogg or click to browse',
      () => showToast('success', 'Sound uploaded: ' + s.filename));
    const playHtml = s.has_file
      ? `<audio controls style="width:100%;margin-top:6px;accent-color:var(--accent)">
           <source src="/audio?path=${encodeURIComponent(s.audio_rel)}" type="audio/ogg">
         </audio>` : '';
    html += `<div class="asset-card" data-category="sounds">
      <div class="card-body">
        <div class="card-title-row">
          <div class="card-name">${escHtml(s.label)}</div>
          <div class="badges">${badgeHtml}</div>
        </div>
        <div class="card-path">${escHtml(s.audio_rel)}</div>
        ${playHtml}
        ${dropHtml}
      </div>
    </div>`;
  }
  html += '</div>';
  return html;
}

// ===== Counts =====
function updateCounts() {
  const sounds = DATA.sounds || [];
  const total = DATA.cards.length + DATA.powers.length + DATA.relics.length + DATA.character.length + sounds.length;
  document.getElementById('cnt-all').textContent     = total;
  document.getElementById('cnt-cards').textContent   = DATA.cards.length;
  document.getElementById('cnt-powers').textContent  = DATA.powers.length;
  document.getElementById('cnt-relics').textContent  = DATA.relics.length;
  document.getElementById('cnt-character').textContent = DATA.character.length;
  document.getElementById('cnt-sounds').textContent  = sounds.length;

  const missingCards = DATA.cards.filter(c => !c.has_file).length;
  const missingPowers = DATA.powers.reduce((n,p) => n + p.sizes.filter(s=>!s.has_file).length,0);
  const missingRelics = DATA.relics.reduce((n,r) => n + r.variants.filter(v=>!v.has_file).length,0);
  const missingChar = DATA.character.filter(c=>!c.has_file).length;
  const missingSounds = sounds.filter(s=>!s.has_file).length;
  const totalMissing = missingCards + missingPowers + missingRelics + missingChar + missingSounds;

  document.getElementById('header-stats').innerHTML = `
    <div class="stat-chip">Total <span>${total}</span></div>
    <div class="stat-chip" style="border-color:#7a4010">Placeholders / Missing <span style="color:#ffb066">${totalMissing}</span></div>
  `;
}

// ===== Filter tabs =====
function applyFilter(filter) {
  document.querySelectorAll('.tab').forEach(t => {
    t.classList.toggle('active', t.dataset.filter === filter);
  });
  document.querySelectorAll('[data-category]').forEach(card => {
    const cat = card.dataset.category;
    card.closest('.asset-card') || (card.style.display = '');
    if (filter === 'all' || cat === filter) {
      card.style.display = '';
    } else {
      card.style.display = 'none';
    }
  });
  // Show/hide section headers
  document.querySelectorAll('.section-header').forEach(h => {
    const grid = h.nextElementSibling;
    if (!grid) return;
    const visible = grid.querySelectorAll('[data-category]:not([style*="none"])').length;
    h.style.display = visible ? '' : 'none';
    grid.style.display = visible ? '' : 'none';
  });
}

// ===== Init =====
function init() {
  const main = document.getElementById('main');
  main.innerHTML = renderCards() + renderPowers() + renderRelics() + renderCharacter() + renderSounds();
  updateCounts();

  document.getElementById('tabs').addEventListener('click', e => {
    const tab = e.target.closest('.tab');
    if (tab) applyFilter(tab.dataset.filter);
  });
}

init();
</script>
</body>
</html>
"""

# ---------------------------------------------------------------------------
# HTTP Handler
# ---------------------------------------------------------------------------

class Handler(BaseHTTPRequestHandler):

    # Suppress default request log spam — replace with minimal output
    def log_message(self, fmt, *args):
        pass  # silent

    def do_GET(self):
        parsed = urlparse(self.path)
        path = parsed.path

        if path == "/":
            self._serve_html()
        elif path == "/image":
            self._serve_image(parsed.query)
        elif path == "/audio":
            self._serve_audio(parsed.query)
        else:
            self._send_404()

    def do_POST(self):
        parsed = urlparse(self.path)
        if parsed.path == "/upload":
            self._handle_upload()
        elif parsed.path == "/upload-audio":
            self._handle_audio_upload()
        else:
            self._send_404()

    # ---- Serve the main HTML page with data injected ----
    def _serve_html(self):
        data = build_data_model()
        data_json = json.dumps(data, ensure_ascii=False)
        page = HTML.replace("__DATA_JSON__", data_json)
        body = page.encode("utf-8")
        self.send_response(200)
        self.send_header("Content-Type", "text/html; charset=utf-8")
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)

    # ---- Serve an audio file ----
    def _serve_audio(self, query: str):
        params = parse_qs(query)
        rel_parts = params.get("path", [""])
        rel = rel_parts[0] if rel_parts else ""
        rel_clean = os.path.normpath(rel).lstrip("/\\")
        if ".." in rel_clean:
            self._send_403()
            return
        abs_path = AUDIO / rel_clean.replace("audio/", "", 1)
        if not abs_path.is_file():
            self._send_404()
            return
        data = abs_path.read_bytes()
        self.send_response(200)
        self.send_header("Content-Type", "audio/ogg")
        self.send_header("Content-Length", str(len(data)))
        self.send_header("Cache-Control", "no-store")
        self.end_headers()
        self.wfile.write(data)

    # ---- Serve an image file ----
    def _serve_image(self, query: str):
        params = parse_qs(query)
        rel_parts = params.get("path", [""])
        rel = rel_parts[0] if rel_parts else ""

        # Security: prevent path traversal
        rel_clean = os.path.normpath(rel).lstrip("/\\")
        if ".." in rel_clean:
            self._send_403()
            return

        abs_path = IMAGES / rel_clean.replace("images/", "", 1)
        if not abs_path.is_file():
            self._send_404()
            return

        suffix = abs_path.suffix.lower()
        mime_map = {".png": "image/png", ".jpg": "image/jpeg",
                    ".jpeg": "image/jpeg", ".gif": "image/gif",
                    ".webp": "image/webp"}
        mime = mime_map.get(suffix, "application/octet-stream")

        data = abs_path.read_bytes()
        self.send_response(200)
        self.send_header("Content-Type", mime)
        self.send_header("Content-Length", str(len(data)))
        self.send_header("Cache-Control", "no-store")
        self.end_headers()
        self.wfile.write(data)

    # ---- Handle file upload ----
    def _handle_upload(self):
        content_type = self.headers.get("Content-Type", "")
        content_length = int(self.headers.get("Content-Length", 0))

        if content_length > 50 * 1024 * 1024:  # 50 MB guard
            self._json_response({"ok": False, "error": "File too large (max 50MB)"}, 413)
            return

        try:
            fields = parse_multipart(self.rfile, content_type, content_length)
        except Exception as e:
            self._json_response({"ok": False, "error": f"Parse error: {e}"}, 400)
            return

        path_field = fields.get("path", (None, None))
        file_field = fields.get("file", (None, None))

        rel_path = path_field[1].decode("utf-8").strip() if path_field[1] else ""
        file_data = file_field[1] if file_field[1] else b""

        if not rel_path or not file_data:
            self._json_response({"ok": False, "error": "Missing path or file"}, 400)
            return

        # Security: prevent path traversal
        rel_clean = os.path.normpath(rel_path).lstrip("/\\")
        if ".." in rel_clean:
            self._send_403()
            return

        # Map relative path to absolute
        # rel_path looks like: images/powers/Burn32.png
        dest = IMAGES / rel_clean.replace("images/", "", 1)
        dest.parent.mkdir(parents=True, exist_ok=True)

        try:
            dest.write_bytes(file_data)
            print(f"  [upload] {rel_path} ({len(file_data):,} bytes)")
            self._json_response({"ok": True, "path": str(dest)})
        except Exception as e:
            self._json_response({"ok": False, "error": str(e)}, 500)

    # ---- Handle audio upload ----
    def _handle_audio_upload(self):
        content_type = self.headers.get("Content-Type", "")
        content_length = int(self.headers.get("Content-Length", 0))
        if content_length > 50 * 1024 * 1024:
            self._json_response({"ok": False, "error": "File too large (max 50MB)"}, 413)
            return
        try:
            fields = parse_multipart(self.rfile, content_type, content_length)
        except Exception as e:
            self._json_response({"ok": False, "error": f"Parse error: {e}"}, 400)
            return
        path_field = fields.get("path", (None, None))
        file_field = fields.get("file", (None, None))
        rel_path = path_field[1].decode("utf-8").strip() if path_field[1] else ""
        file_data = file_field[1] if file_field[1] else b""
        if not rel_path or not file_data:
            self._json_response({"ok": False, "error": "Missing path or file"}, 400)
            return
        rel_clean = os.path.normpath(rel_path).lstrip("/\\")
        if ".." in rel_clean:
            self._send_403()
            return
        dest = AUDIO / rel_clean.replace("audio/", "", 1)
        dest.parent.mkdir(parents=True, exist_ok=True)
        try:
            dest.write_bytes(file_data)
            print(f"  [audio-upload] {rel_path} ({len(file_data):,} bytes)")
            self._json_response({"ok": True, "path": str(dest)})
        except Exception as e:
            self._json_response({"ok": False, "error": str(e)}, 500)

    # ---- Helpers ----
    def _json_response(self, obj, status=200):
        body = json.dumps(obj).encode("utf-8")
        self.send_response(status)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)

    def _send_404(self):
        self.send_response(404)
        self.end_headers()

    def _send_403(self):
        self.send_response(403)
        self.end_headers()


# ---------------------------------------------------------------------------
# Entry point
# ---------------------------------------------------------------------------

PORT = 8765
URL = f"http://localhost:{PORT}"


def main():
    print(f"Paintress Image Manager")
    print(f"  Repo root : {REPO_ROOT}")
    print(f"  Resources : {RESOURCES}")
    print(f"  Server    : {URL}")
    print()

    server = HTTPServer(("127.0.0.1", PORT), Handler)

    # Start server in a daemon thread so Ctrl+C kills it cleanly
    t = threading.Thread(target=server.serve_forever, daemon=True)
    t.start()

    print(f"  Opening browser at {URL} …")
    webbrowser.open(URL)

    try:
        t.join()
    except KeyboardInterrupt:
        print("\n  Shutting down.")
        server.shutdown()


if __name__ == "__main__":
    main()
