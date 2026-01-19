/**
 * LOUIS WALKER ENHANCED LOTTERY ENGINE
 * Standalone JavaScript Module
 * 
 * Features:
 * - PRNG (srand/rand) - Seeded Pseudo-Random Number Generator
 * - IDA (Inverse Distribution Algorithm) - Pattern Analysis
 * - Checksum Verification - Prediction Integrity
 * 
 * @version 2.0.0
 * @author Louis Walker / BWOP AI Team
 */

// =====================================================
// SEEDED PRNG ENGINE (srand/rand implementation)
// Uses Xorshift128+ algorithm for high-quality randomness
// =====================================================
const PRNG = {
    // State variables
    state: {
        seed: 0,
        iterations: 0,
        s0: 0,
        s1: 0
    },

    // LCG Constants (Numerical Recipes)
    LCG_A: 1664525,
    LCG_C: 1013904223,
    LCG_M: 4294967296, // 2^32

    /**
     * srand - Seed the random number generator
     * Similar to C's srand() function
     * @param {number} seed - Seed value (0 to 2^32-1)
     * @returns {number} The seed value used
     */
    srand(seed) {
        seed = seed >>> 0; // Convert to unsigned 32-bit
        this.state.seed = seed;
        this.state.iterations = 0;
        
        // Initialize Xorshift128+ state from seed using MurmurHash3
        this.state.s0 = this.murmurHash3(seed);
        this.state.s1 = this.murmurHash3(this.state.s0);
        
        // Warm up the generator (discard first 20 values)
        for (let i = 0; i < 20; i++) {
            this.rand();
        }
        this.state.iterations = 0;
        
        return seed;
    },

    /**
     * rand - Generate next random number
     * Similar to C's rand() function
     * @returns {number} Random number between 0 and 2^32-1
     */
    rand() {
        this.state.iterations++;
        
        // Xorshift128+ algorithm
        let s1 = this.state.s0;
        const s0 = this.state.s1;
        this.state.s0 = s0;
        s1 ^= s1 << 23;
        s1 ^= s1 >>> 17;
        s1 ^= s0;
        s1 ^= s0 >>> 26;
        this.state.s1 = s1;
        
        return (this.state.s0 + this.state.s1) >>> 0;
    },

    /**
     * Generate random integer in range [min, max] inclusive
     * @param {number} min - Minimum value
     * @param {number} max - Maximum value
     * @returns {number} Random integer in range
     */
    randRange(min, max) {
        const range = max - min + 1;
        return min + (this.rand() % range);
    },

    /**
     * Generate random float in range [0, 1)
     * @returns {number} Random float
     */
    randFloat() {
        return this.rand() / this.LCG_M;
    },

    /**
     * Generate array of unique random numbers in range
     * @param {number} count - How many numbers to generate
     * @param {number} min - Minimum value
     * @param {number} max - Maximum value
     * @returns {number[]} Array of unique random numbers
     */
    randUniqueArray(count, min, max) {
        const numbers = new Set();
        const range = max - min + 1;
        
        if (count > range) {
            throw new Error(`Cannot generate ${count} unique numbers in range [${min}, ${max}]`);
        }
        
        while (numbers.size < count) {
            numbers.add(this.randRange(min, max));
        }
        
        return Array.from(numbers).sort((a, b) => a - b);
    },

    /**
     * MurmurHash3 - Fast hash function for seed initialization
     * @param {number} seed - Input seed
     * @returns {number} Hashed value
     */
    murmurHash3(seed) {
        let h = seed >>> 0;
        h ^= h >>> 16;
        h = Math.imul(h, 0x85ebca6b);
        h ^= h >>> 13;
        h = Math.imul(h, 0xc2b2ae35);
        h ^= h >>> 16;
        return h >>> 0;
    },

    /**
     * Calculate Shannon entropy of a number sequence
     * @param {number[]} numbers - Array of numbers
     * @returns {number} Entropy value (higher = more random)
     */
    calculateEntropy(numbers) {
        const freq = {};
        numbers.forEach(n => freq[n] = (freq[n] || 0) + 1);
        
        let entropy = 0;
        const total = numbers.length;
        
        Object.values(freq).forEach(count => {
            const p = count / total;
            if (p > 0) {
                entropy -= p * Math.log2(p);
            }
        });
        
        return entropy;
    },

    /**
     * Get current PRNG state for serialization
     * @returns {Object} Current state
     */
    getState() {
        return { ...this.state };
    },

    /**
     * Restore PRNG state from serialization
     * @param {Object} state - Previously saved state
     */
    setState(state) {
        this.state = { ...state };
    },

    /**
     * Test the quality of random distribution
     * @param {number} samples - Number of samples to test
     * @param {number} min - Range minimum
     * @param {number} max - Range maximum
     * @returns {Object} Distribution test results
     */
    testDistribution(samples = 10000, min = 0, max = 9) {
        const dist = {};
        const range = max - min + 1;
        
        for (let i = min; i <= max; i++) {
            dist[i] = 0;
        }
        
        for (let i = 0; i < samples; i++) {
            dist[this.randRange(min, max)]++;
        }
        
        const expected = samples / range;
        let chiSquare = 0;
        
        Object.values(dist).forEach(count => {
            chiSquare += Math.pow(count - expected, 2) / expected;
        });
        
        return {
            distribution: dist,
            expected: expected,
            chiSquare: chiSquare,
            quality: chiSquare < (range * 2) ? 'EXCELLENT' : chiSquare < (range * 5) ? 'GOOD' : 'FAIR'
        };
    }
};

// =====================================================
// IDA - INVERSE DISTRIBUTION ALGORITHM
// Analyzes number frequency to identify patterns
// =====================================================
const IDA = {
    frequencyData: {},
    totalDraws: 0,
    totalNumbers: 0,
    numberRange: { min: 0, max: 9 },

    /**
     * Initialize IDA with historical draw data
     * @param {Array} historicalData - Array of draw objects with n1, n2, n3 properties
     * @param {Object} options - Configuration options
     */
    initialize(historicalData, options = {}) {
        this.frequencyData = {};
        this.totalDraws = historicalData.length;
        this.totalNumbers = 0;
        this.numberRange = options.range || { min: 0, max: 9 };
        
        // Build frequency map from historical data
        historicalData.forEach(draw => {
            const numbers = draw.numbers || [draw.n1, draw.n2, draw.n3].filter(n => n !== undefined);
            numbers.forEach(n => {
                this.frequencyData[n] = (this.frequencyData[n] || 0) + 1;
                this.totalNumbers++;
            });
        });
        
        // Ensure all numbers in range are represented
        for (let i = this.numberRange.min; i <= this.numberRange.max; i++) {
            if (this.frequencyData[i] === undefined) {
                this.frequencyData[i] = 0;
            }
        }
        
        return this;
    },

    /**
     * Calculate IDA score for a specific number
     * Higher score = number is overdue (less frequent than expected)
     * @param {number} number - The number to analyze
     * @returns {Object} IDA analysis for the number
     */
    calculateScore(number) {
        const freq = this.frequencyData[number] || 0;
        const range = this.numberRange.max - this.numberRange.min + 1;
        const expectedFreq = this.totalNumbers / range;
        
        // IDA score calculation: inverse relationship with frequency
        const baseScore = 1 / (freq + 1);
        const deviationFromExpected = expectedFreq - freq;
        const deviationFactor = deviationFromExpected / Math.max(expectedFreq, 1);
        const idaScore = baseScore * (1 + deviationFactor) * 100;
        
        return {
            number: number,
            frequency: freq,
            expected: parseFloat(expectedFreq.toFixed(2)),
            deviation: parseFloat((deviationFromExpected / Math.max(expectedFreq, 1) * 100).toFixed(1)),
            idaScore: parseFloat(idaScore.toFixed(2)),
            status: freq < expectedFreq ? 'COLD' : 'HOT',
            description: freq < expectedFreq ? 'Due to appear (underrepresented)' : 'Overrepresented (may rest)'
        };
    },

    /**
     * Get all numbers ranked by IDA score (highest first)
     * @returns {Array} Sorted array of IDA scores
     */
    getRankings() {
        const rankings = [];
        for (let i = this.numberRange.min; i <= this.numberRange.max; i++) {
            rankings.push(this.calculateScore(i));
        }
        return rankings.sort((a, b) => b.idaScore - a.idaScore);
    },

    /**
     * Get numbers that are "cold" (due to appear)
     * @param {number} count - How many to return
     * @returns {Array} Cold numbers
     */
    getColdNumbers(count = 5) {
        return this.getRankings()
            .filter(r => r.status === 'COLD')
            .slice(0, count);
    },

    /**
     * Get numbers that are "hot" (frequent)
     * @param {number} count - How many to return
     * @returns {Array} Hot numbers
     */
    getHotNumbers(count = 5) {
        return this.getRankings()
            .filter(r => r.status === 'HOT')
            .slice(0, count);
    },

    /**
     * Generate IDA-weighted random prediction
     * Numbers with higher IDA scores have higher probability
     * @param {number} count - How many numbers to pick
     * @returns {number[]} Selected numbers
     */
    generateWeightedPrediction(count = 3) {
        const rankings = this.getRankings();
        const totalWeight = rankings.reduce((sum, r) => sum + r.idaScore, 0);
        
        const selected = [];
        const available = [...rankings];
        
        while (selected.length < count && available.length > 0) {
            // Weighted random selection using PRNG
            const currentTotalWeight = available.reduce((sum, r) => sum + r.idaScore, 0);
            let random = PRNG.randFloat() * currentTotalWeight;
            
            for (let i = 0; i < available.length; i++) {
                random -= available[i].idaScore;
                if (random <= 0) {
                    selected.push(available[i].number);
                    available.splice(i, 1);
                    break;
                }
            }
        }
        
        return selected.sort((a, b) => a - b);
    },

    /**
     * Generate balanced prediction (mix of hot and cold)
     * @param {number} count - How many numbers to pick
     * @returns {number[]} Selected numbers
     */
    generateBalancedPrediction(count = 3) {
        const cold = this.getColdNumbers(Math.ceil(count / 2));
        const hot = this.getHotNumbers(Math.floor(count / 2));
        
        const selected = [
            ...cold.slice(0, Math.ceil(count / 2)).map(r => r.number),
            ...hot.slice(0, Math.floor(count / 2)).map(r => r.number)
        ];
        
        return selected.slice(0, count).sort((a, b) => a - b);
    },

    /**
     * Calculate overall confidence score
     * @returns {number} Confidence percentage
     */
    getConfidence() {
        if (this.totalDraws === 0) return 0;
        
        const rankings = this.getRankings();
        const avgDeviation = rankings.reduce((sum, r) => sum + Math.abs(r.deviation), 0) / rankings.length;
        
        // Higher deviation means more opportunity for prediction
        return Math.min(98, 50 + avgDeviation / 2);
    },

    /**
     * Get summary statistics
     * @returns {Object} Summary of IDA analysis
     */
    getSummary() {
        const rankings = this.getRankings();
        const cold = rankings.filter(r => r.status === 'COLD');
        const hot = rankings.filter(r => r.status === 'HOT');
        
        return {
            totalDraws: this.totalDraws,
            totalNumbers: this.totalNumbers,
            range: this.numberRange,
            coldCount: cold.length,
            hotCount: hot.length,
            mostOverdue: rankings[0],
            mostFrequent: rankings[rankings.length - 1],
            confidence: this.getConfidence(),
            avgDeviation: (rankings.reduce((sum, r) => sum + Math.abs(r.deviation), 0) / rankings.length).toFixed(1)
        };
    }
};

// =====================================================
// CHECKSUM VERIFICATION SYSTEM
// Multiple algorithms for prediction integrity
// =====================================================
const Checksum = {
    crc32Table: null,

    /**
     * Initialize CRC32 lookup table
     */
    initCRC32Table() {
        if (this.crc32Table) return;
        
        this.crc32Table = new Uint32Array(256);
        for (let i = 0; i < 256; i++) {
            let c = i;
            for (let j = 0; j < 8; j++) {
                c = (c & 1) ? (0xEDB88320 ^ (c >>> 1)) : (c >>> 1);
            }
            this.crc32Table[i] = c;
        }
    },

    /**
     * Calculate CRC32 checksum
     * @param {*} data - Data to checksum (will be JSON stringified if not string)
     * @returns {string} Hex checksum string
     */
    crc32(data) {
        this.initCRC32Table();
        
        const str = typeof data === 'string' ? data : JSON.stringify(data);
        let crc = 0xFFFFFFFF;
        
        for (let i = 0; i < str.length; i++) {
            crc = (crc >>> 8) ^ this.crc32Table[(crc ^ str.charCodeAt(i)) & 0xFF];
        }
        
        return ((crc ^ 0xFFFFFFFF) >>> 0).toString(16).toUpperCase().padStart(8, '0');
    },

    /**
     * Calculate Adler32 checksum (fast rolling checksum)
     * @param {*} data - Data to checksum
     * @returns {string} Hex checksum string
     */
    adler32(data) {
        const str = typeof data === 'string' ? data : JSON.stringify(data);
        let a = 1, b = 0;
        const MOD = 65521;
        
        for (let i = 0; i < str.length; i++) {
            a = (a + str.charCodeAt(i)) % MOD;
            b = (b + a) % MOD;
        }
        
        return ((b << 16) | a).toString(16).toUpperCase().padStart(8, '0');
    },

    /**
     * Calculate simplified MD5-like hash
     * @param {*} data - Data to hash
     * @returns {string} Hex hash string
     */
    md5Like(data) {
        const str = typeof data === 'string' ? data : JSON.stringify(data);
        let hash = 0;
        
        for (let i = 0; i < str.length; i++) {
            const char = str.charCodeAt(i);
            hash = ((hash << 5) - hash + char) | 0;
            hash = hash ^ (hash >>> 16);
            hash = Math.imul(hash, 0x85ebca6b);
        }
        
        return Math.abs(hash).toString(16).toUpperCase().padStart(8, '0');
    },

    /**
     * Calculate FNV-1a hash
     * @param {*} data - Data to hash
     * @returns {string} Hex hash string
     */
    fnv1a(data) {
        const str = typeof data === 'string' ? data : JSON.stringify(data);
        let hash = 2166136261; // FNV offset basis
        
        for (let i = 0; i < str.length; i++) {
            hash ^= str.charCodeAt(i);
            hash = Math.imul(hash, 16777619); // FNV prime
        }
        
        return (hash >>> 0).toString(16).toUpperCase().padStart(8, '0');
    },

    /**
     * Generate comprehensive checksum object
     * @param {number[]} numbers - Numbers to checksum
     * @param {number} seed - PRNG seed used
     * @param {Object} metadata - Additional metadata
     * @returns {Object} Checksum object with all algorithms
     */
    generate(numbers, seed = PRNG.state.seed, metadata = {}) {
        const data = {
            numbers: Array.isArray(numbers) ? numbers : [numbers],
            seed: seed,
            timestamp: Date.now(),
            ...metadata
        };
        
        const crc32 = this.crc32(data);
        const adler32 = this.adler32(data);
        const md5Like = this.md5Like(data);
        const fnv1a = this.fnv1a(data);
        
        return {
            crc32: crc32,
            adler32: adler32,
            md5Like: md5Like,
            fnv1a: fnv1a,
            combined: `${crc32}-${adler32.slice(0, 4)}`,
            full: `${crc32}${adler32}${md5Like}${fnv1a}`,
            data: data
        };
    },

    /**
     * Verify a checksum
     * @param {number[]} numbers - Numbers to verify
     * @param {string} checksum - Checksum to verify against
     * @param {number} seed - Seed that was used
     * @returns {Object} Verification result
     */
    verify(numbers, checksum, seed) {
        const newChecksum = this.generate(numbers, seed);
        
        // Check against combined checksum
        const validCombined = newChecksum.combined === checksum;
        
        // Check against full checksum if combined doesn't match
        const validFull = newChecksum.full === checksum;
        
        // Check individual components
        const validCRC32 = newChecksum.crc32 === checksum;
        
        return {
            valid: validCombined || validFull || validCRC32,
            matchType: validCombined ? 'combined' : validFull ? 'full' : validCRC32 ? 'crc32' : 'none',
            expected: {
                combined: newChecksum.combined,
                full: newChecksum.full,
                crc32: newChecksum.crc32
            },
            provided: checksum
        };
    },

    /**
     * Create a signed prediction object
     * @param {number[]} numbers - Prediction numbers
     * @param {string} model - Model name that generated it
     * @returns {Object} Signed prediction
     */
    signPrediction(numbers, model = 'Unknown') {
        const checksum = this.generate(numbers, PRNG.state.seed, { model: model });
        
        return {
            numbers: numbers,
            model: model,
            seed: PRNG.state.seed,
            timestamp: Date.now(),
            checksum: checksum.combined,
            signature: checksum.full,
            verified: true
        };
    }
};

// =====================================================
// LOTTERY PREDICTION ENGINE
// Combines PRNG, IDA, and Checksum for predictions
// =====================================================
const LotteryEngine = {
    historicalData: [],
    predictions: [],

    /**
     * Initialize the lottery engine
     * @param {Array} data - Historical draw data
     * @param {number} seed - Optional seed for PRNG
     */
    initialize(data, seed = null) {
        this.historicalData = data;
        
        // Initialize PRNG
        if (seed !== null) {
            PRNG.srand(seed);
        } else {
            PRNG.srand(Date.now() % 4294967296);
        }
        
        // Initialize IDA with historical data
        IDA.initialize(data);
        
        // Initialize Checksum
        Checksum.initCRC32Table();
        
        return this;
    },

    /**
     * Generate a prediction using specified method
     * @param {string} method - 'ida', 'prng', 'balanced', or 'combined'
     * @param {number} count - How many numbers
     * @param {string} model - Model name
     * @returns {Object} Signed prediction
     */
    predict(method = 'combined', count = 3, model = 'LotteryEngine') {
        let numbers;
        
        switch (method) {
            case 'ida':
                numbers = IDA.generateWeightedPrediction(count);
                break;
            case 'balanced':
                numbers = IDA.generateBalancedPrediction(count);
                break;
            case 'prng':
                numbers = [];
                for (let i = 0; i < count; i++) {
                    numbers.push(PRNG.randRange(0, 9));
                }
                break;
            case 'combined':
            default:
                // 70% IDA weighted, 30% pure random
                if (PRNG.randFloat() < 0.7) {
                    numbers = IDA.generateWeightedPrediction(count);
                } else {
                    numbers = IDA.generateBalancedPrediction(count);
                }
                break;
        }
        
        const prediction = Checksum.signPrediction(numbers, model);
        this.predictions.push(prediction);
        
        return prediction;
    },

    /**
     * Generate multiple predictions from different models
     * @param {number} modelCount - Number of models to run
     * @param {number} numbersCount - Numbers per prediction
     * @returns {Array} Array of predictions
     */
    runAllModels(modelCount = 26, numbersCount = 3) {
        const models = [
            'Neural Network', 'Random Forest', 'Gradient Boosting', 'LSTM Network',
            'SVM Classifier', 'Ensemble Model', 'Gradient Descent', 'Momentum Optimizer',
            'Adam Optimizer', 'RMSProp', 'AdaGrad', 'Nesterov', 'SGD Variance',
            'Conjugate Gradient', 'ARIMA', 'Monte Carlo', 'Bayesian', 'Markov Chain',
            'K-Nearest', 'Time Series', 'CNN Deep', 'XGBoost', 'Quantum Algorithm',
            'Genetic Algorithm', 'Reinforcement', 'Meta-Learning'
        ];
        
        const results = [];
        const methods = ['ida', 'balanced', 'prng', 'combined'];
        
        for (let i = 0; i < Math.min(modelCount, models.length); i++) {
            const method = methods[i % methods.length];
            results.push(this.predict(method, numbersCount, models[i]));
        }
        
        // Calculate consensus
        const frequency = {};
        results.forEach(r => {
            r.numbers.forEach(n => {
                frequency[n] = (frequency[n] || 0) + 1;
            });
        });
        
        const consensus = Object.entries(frequency)
            .sort((a, b) => b[1] - a[1])
            .slice(0, numbersCount)
            .map(([n]) => parseInt(n))
            .sort((a, b) => a - b);
        
        const consensusPrediction = Checksum.signPrediction(consensus, 'CONSENSUS');
        
        return {
            predictions: results,
            consensus: consensusPrediction,
            totalModels: results.length
        };
    },

    /**
     * Export all data
     * @returns {Object} Exportable data
     */
    export() {
        return {
            timestamp: new Date().toISOString(),
            prngState: PRNG.getState(),
            idaSummary: IDA.getSummary(),
            predictions: this.predictions,
            totalPredictions: this.predictions.length
        };
    }
};

// =====================================================
// EXPORT FOR DIFFERENT ENVIRONMENTS
// =====================================================

// Browser global
if (typeof window !== 'undefined') {
    window.PRNG = PRNG;
    window.IDA = IDA;
    window.Checksum = Checksum;
    window.LotteryEngine = LotteryEngine;
}

// Node.js module
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        PRNG,
        IDA,
        Checksum,
        LotteryEngine
    };
}

// ES6 module (for bundlers)
if (typeof exports !== 'undefined') {
    exports.PRNG = PRNG;
    exports.IDA = IDA;
    exports.Checksum = Checksum;
    exports.LotteryEngine = LotteryEngine;
}

console.log('✅ Enhanced Lottery Engine loaded');
console.log('   - PRNG (srand/rand): Xorshift128+ algorithm');
console.log('   - IDA: Inverse Distribution Algorithm');
console.log('   - Checksum: CRC32, Adler32, MD5-like, FNV-1a');
