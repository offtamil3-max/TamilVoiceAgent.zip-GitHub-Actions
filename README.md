# தமிழ் Voice Agent 🎤

Tamil / Tanglish voice-controlled Android app.

## GitHub Actions மூலம் APK build

1. GitHub repository-ஐ திறக்கவும்.
2. **Actions** tab → **Build Tamil Voice Agent APK** தேர்வு செய்யவும்.
3. **Run workflow** அழுத்தவும்.
4. Build முடிந்ததும் workflow run-ன் **Artifacts** பகுதியில் `TamilVoiceAgent-debug` ZIP-ஐ download செய்யவும்.
5. ZIP-ஐ extract செய்து `app-debug.apk` install செய்யவும்.

ஒவ்வொரு `main` push / pull request-க்கும் debug APK build automatically ஆகும்.

## Local build

```bash
chmod +x gradlew
./gradlew assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`
