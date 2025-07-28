const fs = require('fs');

// Function to convert a number from any base to decimal (handles large numbers)
function baseToDecimal(value, base) {
    let result = 0n;
    const baseBig = BigInt(base);
    
    for (let i = 0; i < value.length; i++) {
        const digit = value[i];
        let digitValue;
        
        if (digit >= '0' && digit <= '9') {
            digitValue = BigInt(digit.charCodeAt(0) - '0'.charCodeAt(0));
        } else {
            // Handle letters for bases > 10 (a=10, b=11, etc.)
            digitValue = BigInt(digit.toLowerCase().charCodeAt(0) - 'a'.charCodeAt(0) + 10);
        }
        
        result = result * baseBig + digitValue;
    }
    
    return result;
}

// Function to perform Lagrange interpolation and find the constant term (handles BigInt)
function lagrangeInterpolation(points, k) {
    const selectedPoints = points.slice(0, k);
    let result = 0n;
    
    for (let i = 0; i < selectedPoints.length; i++) {
        const [xi, yi] = selectedPoints[i];
        
        let numerator = 1n;
        let denominator = 1n;
        
        for (let j = 0; j < selectedPoints.length; j++) {
            if (i !== j) {
                const [xj, _] = selectedPoints[j];
                numerator *= (-xj);
                denominator *= (xi - xj);
            }
        }
        
        result += yi * numerator / denominator;
    }
    
    return result;
}

// Function to solve a single test case
function solveTestCase(testCase) {
    const { keys } = testCase;
    const n = keys.n;
    const k = keys.k;
    
    console.log(`\nSolving test case with n=${n}, k=${k}`);
    
    const points = [];
    
    for (let i = 1; i <= n; i++) {
        if (testCase[i.toString()]) {
            const x = BigInt(i);
            const base = parseInt(testCase[i.toString()].base);
            const value = testCase[i.toString()].value;
            
            const y = baseToDecimal(value, base);
            points.push([x, y]);
            
            console.log(`Point ${i}: x=${x}, y=${y} (decoded from "${value}" in base ${base})`);
        }
    }
    
    const secret = lagrangeInterpolation(points, k);
    return secret;
}

// Test Case 1
const testCase1 = {
    "keys": {
        "n": 4,
        "k": 3
    },
    "1": {
        "base": "10",
        "value": "4"
    },
    "2": {
        "base": "2",
        "value": "111"
    },
    "3": {
        "base": "10",
        "value": "12"
    },
    "6": {
        "base": "4",
        "value": "213"
    }
};

// Test Case 2
const testCase2 = {
    "keys": {
        "n": 10,
        "k": 7
    },
    "1": {
        "base": "6",
        "value": "13444211440455345511"
    },
    "2": {
        "base": "15",
        "value": "aed7015a346d63"
    },
    "3": {
        "base": "15",
        "value": "6aeeb69631c227c"
    },
    "4": {
        "base": "16",
        "value": "e1b5e05623d881f"
    },
    "5": {
        "base": "8",
        "value": "316034514573652620673"
    },
    "6": {
        "base": "3",
        "value": "2122212201122002221120200210011020220200"
    },
    "7": {
        "base": "3",
        "value": "20120221122211000100210021102001201112121"
    },
    "8": {
        "base": "6",
        "value": "20220554335330240002224253"
    },
    "9": {
        "base": "12",
        "value": "45153788322a1255483"
    },
    "10": {
        "base": "7",
        "value": "1101613130313526312514143"
    }
};

console.log("=== SHAMIR'S SECRET SHARING SOLUTION ===");

console.log("\n--- TEST CASE 1 ---");
const secret1 = solveTestCase(testCase1);
console.log(`Secret for Test Case 1: ${secret1}`);

console.log("\n--- TEST CASE 2 ---");
const secret2 = solveTestCase(testCase2);
console.log(`Secret for Test Case 2: ${secret2}`);

console.log("\n=== FINAL RESULTS ===");
console.log(`Test Case 1 Secret: ${secret1}`);
console.log(`Test Case 2 Secret: ${secret2}`);
