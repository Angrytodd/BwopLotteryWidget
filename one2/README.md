# BWOP Lottery Intelligence Platform v3.0

## 🎯 Overview

Advanced lottery prediction system with AI-powered analytics, featuring:
- **PRNG (srand/rand)**: Seeded pseudo-random number generator for reproducible results
- **IDA (Inverse Distribution Algorithm)**: Pattern analysis for identifying underrepresented numbers
- **Checksum Verification**: Cryptographic integrity verification for all predictions
- **26 AI Models**: Neural Networks, Gradient Methods, Statistical, and Advanced algorithms
- **Modern Dark UI**: Professional dashboard with real-time analytics

## 📁 Files

| File | Description |
|------|-------------|
| `index.html` | **NEW** Modern dark-themed dashboard UI |
| `louis_walker_ultimate_enhanced.html` | Complete standalone HTML application (legacy) |
| `enhanced-lottery-engine.js` | Standalone JavaScript module for integration |
| `server.js` | Node.js HTTP server for local deployment |
| `package.json` | NPM configuration for easy deployment |

## 🚀 Quick Deploy

### Option 1: Node.js Server
```bash
cd one2
node server.js
# Open http://localhost:3000
```

### Option 2: NPM
```bash
cd one2
npm start
# Open http://localhost:3000
```

### Option 3: Direct Browser
Simply open `index.html` or `louis_walker_ultimate_enhanced.html` in any web browser.

### Option 4: Live Server (VS Code)
Right-click `index.html` → "Open with Live Server"

## 🔧 Features

### 1. PRNG Engine (srand/rand)

Implements a high-quality pseudo-random number generator using the **Xorshift128+** algorithm.

```javascript
// Seed the generator (like C's srand)
PRNG.srand(12345);

// Generate random numbers (like C's rand)
const num = PRNG.rand();

// Generate in range
const lotteryNum = PRNG.randRange(0, 9);

// Generate unique numbers
const pick5 = PRNG.randUniqueArray(5, 1, 69);
```

**Key Features:**
- Deterministic output for same seed
- High-quality randomness (passes statistical tests)
- State serialization for reproducibility
- Entropy calculation

### 2. IDA (Inverse Distribution Algorithm)

Analyzes historical data to identify numbers that are "due" to appear.

```javascript
// Initialize with historical data
IDA.initialize(historicalData);

// Get rankings (highest IDA score = most overdue)
const rankings = IDA.getRankings();

// Generate weighted prediction
const prediction = IDA.generateWeightedPrediction(3);

// Get hot/cold numbers
const coldNumbers = IDA.getColdNumbers(5);
const hotNumbers = IDA.getHotNumbers(5);
```

**IDA Score Formula:**
```
IDA_Score(n) = (1 / (freq + 1)) × (1 + deviation_factor) × 100

Where:
- freq = actual frequency of number n
- deviation_factor = (expected - actual) / expected
```

### 3. Checksum Verification

Multiple checksum algorithms for prediction integrity.

```javascript
// Generate checksum for numbers
const checksum = Checksum.generate([1, 2, 3]);

// Verify a checksum
const result = Checksum.verify([1, 2, 3], checksum.combined, seed);

// Sign a prediction
const signed = Checksum.signPrediction([1, 2, 3], 'ModelName');
```

**Algorithms Included:**
- **CRC32**: Standard cyclic redundancy check
- **Adler32**: Fast rolling checksum
- **MD5-like**: Simplified message digest
- **FNV-1a**: Fowler-Noll-Vo hash

## 🚀 Quick Start

### Standalone HTML (Browser)

Simply open `louis_walker_ultimate_enhanced.html` in any web browser.

### JavaScript Module

```html
<script src="enhanced-lottery-engine.js"></script>
<script>
    // Initialize with historical data
    LotteryEngine.initialize(historicalData, 12345);
    
    // Generate prediction
    const prediction = LotteryEngine.predict('combined', 3);
    console.log(prediction);
    
    // Run all 26 models
    const results = LotteryEngine.runAllModels();
    console.log(results.consensus);
</script>
```

### Node.js

```javascript
const { PRNG, IDA, Checksum, LotteryEngine } = require('./enhanced-lottery-engine.js');

// Seed PRNG
PRNG.srand(Date.now());

// Generate lottery numbers
const numbers = PRNG.randUniqueArray(3, 0, 9);
console.log('Numbers:', numbers);
```

## 📊 26 AI Models

The system includes 26 prediction models across 4 categories:

1. **Machine Learning (6)**: Neural Network, Random Forest, Gradient Boosting, LSTM, SVM, Ensemble
2. **Gradient Methods (8)**: Gradient Descent, Momentum, Adam, RMSProp, AdaGrad, Nesterov, SGD, Conjugate
3. **Statistical (6)**: ARIMA, Monte Carlo, Bayesian, Markov Chain, K-Nearest, Time Series
4. **Advanced (6)**: CNN, XGBoost, Quantum, Genetic, Reinforcement Learning, Meta-Learning

## 🔐 Seed Management

Seeds allow reproducible predictions:

```javascript
// Save current state
const state = PRNG.getState();
localStorage.setItem('prng_state', JSON.stringify(state));

// Restore state later
const saved = JSON.parse(localStorage.getItem('prng_state'));
PRNG.setState(saved);
```

## 📈 IDA Analysis Guide

| Status | Meaning | Action |
|--------|---------|--------|
| COLD | Below expected frequency | Number is "due" - higher prediction weight |
| HOT | Above expected frequency | Number may "rest" - lower prediction weight |

## ✓ Checksum Format

Combined checksum format: `CRC32-ADLR`
- Example: `A1B2C3D4-E5F6`

Full checksum: `CRC32+ADLER32+MD5LIKE+FNV1A`
- Example: `A1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6`

## 🎮 Usage Examples

### Generate Pick 3 with IDA

```javascript
IDA.initialize(historicalData);
const idaPick = IDA.generateWeightedPrediction(3);
const checksum = Checksum.generate(idaPick);
console.log(`Pick 3: ${idaPick.join('-')} [${checksum.combined}]`);
```

### Reproducible Predictions

```javascript
PRNG.srand(20240115); // Use date as seed
const prediction1 = LotteryEngine.predict('combined', 3);

PRNG.srand(20240115); // Same seed = same results
const prediction2 = LotteryEngine.predict('combined', 3);

console.log(prediction1.numbers); // [2, 5, 7]
console.log(prediction2.numbers); // [2, 5, 7] - identical!
```

## 📝 License

MIT License - Free for personal and commercial use.

## 👨‍💻 Author

Louis Walker / BWOP AI Team

---

**Version:** 2.0.0  
**Last Updated:** January 2026
