# 🏎️ Race Strategy Optimiser — Java Simulator

An intelligent, modular Java application that simulates race strategies across multiple predefined tracks by allowing full or preset car customisation. The system evaluates performance metrics, matches configurations to track demands, and issues professional-grade recommendations.

---

## 📦 Features

✅ Predefined Track Profiles with Realistic Data  
✅ Custom and Preset Car Configurations  
✅ Fuel, Tyre, Aero, and Engine Logic  
✅ Context-Aware Strategy Recommendations  
✅ Detailed Performance Simulation  
✅ Penalty Warnings for Suboptimal Setups  
✅ Dynamic Recommendations Matching Track Complexity  
✅ Extendable Architecture (e.g., Track Difficulty Score, Preset Profiles)

---

## 📁 Project Structure

```
src/
├── aerodynamic/
│   ├── AerodynamicKit.java
│   ├── DownforceKit.java
│   ├── ...
├── engine/
│   ├── Engine.java
│   ├── TurboEngine.java
│   ├── ...
├── tyre/
│   ├── Tyre.java
│   ├── SoftTyre.java
│   ├── ...
├── RaceCar.java
├── RaceTrack.java
├── TrackFactory.java
├── RaceStrategySimulator.java
└── Main.java
```

---

## 🚗 How It Works

1. **Track Selection**  
   Choose from 5 professionally designed circuits with varied difficulty, weather, temperature, and elevation.

2. **Car Configuration**  
   - Option 1: Manual – Choose your own engine, tyres, aerodynamic kit, and fuel tank size  
   - Option 2: Preset – Select a recommended car setup for your chosen track

3. **Race Simulation**  
   Calculates:
   - Lap Time  
   - Total Race Time  
   - Fuel Consumption & Stops  
   - Tyre Changes

4. **Strategy Evaluation**  
   Intelligent logic:
   - Detects mismatched components (engine, tyre, aero, fuel)
   - Suggests improvements
   - Confirms ideal configurations

---

## 📊 Sample Output

```
🏎️ Recommended Setup for Mountain Twistway:
- Engine: Turbo Engine
- Tyres: Soft
- Aero Kit: Downforce Kit
- Fuel Tank: 90.0L

💡 Why this setup? ❄️ Cold, wet, twisty track demands control and grip.

🚗 Selected Car Setup:
- Engine: Standard Engine
- Tyres: Soft
- Aero Kit: Standard Kit
- Fuel Tank: 90.0L

🧠 Final Strategy Recommendation:
- ❗ Engine selection is suboptimal. Recommended: Electric Engine
- ❗ Aerodynamic kit choice may not provide the ideal performance. Recommended: Downforce Kit

🛠️ Recommended Setup:
- Engine: Electric Engine
- Tyres: Soft Tyres
- Aero Kit: Downforce Kit
```

---

## 🧠 Intelligence Behind the System

Each recommendation is context-aware:
- Wet + twisty track → Downforce & Electric engine  
- Long straight track → Low Drag & Turbo engine  
- Cold track → Soft tyres  
- Hot track → Hard tyres  
- Low fuel efficiency → Higher tank or aero optimization

Warnings are shown **only when** your configuration **diverges from the optimal match** — making it educational, accurate, and fair.

---

## 🧩 Technologies Used

- Java 17
- Object-Oriented Architecture
- No external libraries — fully native Java
- Clean separation of concerns (Engine, Tyre, Aero, Logic)

---

## 🏁 How to Run

1. Clone the repo or copy all `.java` files into a Java project (`src/` folder)
2. Set `Main.java` as your entry point
3. Run the program in any Java IDE (e.g., IntelliJ, Eclipse)
4. Follow the prompts interactively

---

## 💡 Possible Extensions

- Add weather forecasting module  
- Track difficulty scoring system (1–10)  
- Setup match score (0–100%)  
- Export simulation reports  
- Add multiplayer comparisons  

---

