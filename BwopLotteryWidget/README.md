# 🎯 BWOP Lottery Predictor Widget

**Android 15 Lottery Prediction App with Home Screen Widget**

A sleek, neon-themed lottery prediction app featuring:
- **PRNG Engine** - Xorshift128+ seeded pseudo-random number generator
- **IDA Algorithm** - Inverse Distribution Analysis for pattern detection
- **Checksum Verification** - CRC32 + Adler32 prediction integrity
- **26 AI Models** - Multiple prediction algorithms
- **Home Screen Widget** - Quick predictions at a glance

## 📱 Screenshots

The app features a cyberpunk/neon aesthetic with:
- Animated floating number balls
- Glowing purple/pink/cyan color scheme
- Swipeable tab navigation
- Dark theme throughout

## 🚀 Features

### Main App (6 Swipeable Screens)

1. **Dashboard** - Today's predictions, stats overview
2. **PRNG Engine** - Configure seed, generate random numbers
3. **IDA Analysis** - Heatmap showing hot/cold numbers
4. **AI Models** - Run all 26 prediction models
5. **Checksum** - Generate and verify prediction checksums
6. **History** - View historical draw data

### Home Screen Widget

- Shows 3 predicted numbers
- Checksum verification badge
- Tap to refresh predictions
- Tap to open full app
- Responsive sizing (small/medium/large)

## 🛠 Technical Stack

- **Kotlin** - 100% Kotlin codebase
- **Jetpack Compose** - Modern declarative UI
- **Glance** - AppWidget framework for Android 12+
- **Material 3** - Latest design system
- **Target SDK 35** - Android 15 ready

## 📦 Build

```bash
# Clone and open in Android Studio
cd BwopLotteryWidget

# Build debug APK
./gradlew assembleDebug

# Install on device
./gradlew installDebug
```

## 🎨 Theme Colors

| Color | Hex | Use |
|-------|-----|-----|
| Neon Cyan | `#00F5FF` | Primary accent |
| Neon Purple | `#9D4EDD` | Secondary |
| Neon Pink | `#FF006E` | Tertiary |
| Dark BG | `#0D0D1A` | Background |
| Dark Surface | `#1A1A2E` | Cards |

## 📊 Prediction Engine

### PRNG (srand/rand)
```kotlin
PRNG.srand(seed)     // Set seed
PRNG.rand()          // Get next random
PRNG.randRange(0, 9) // Get 0-9
```

### IDA Algorithm
- Analyzes frequency distribution
- Identifies "cold" (due) numbers
- Weights predictions accordingly

### Checksum
- CRC32 + Adler32 combined
- Verifies prediction integrity
- Tamper-evident

## 📄 License

MIT License - Louis Walker / BWOP AI Team

---

**Version:** 1.0.0  
**Min SDK:** 26 (Android 8.0)  
**Target SDK:** 35 (Android 15)
