# kyberlib
Standard libraries for FRC team 6502 for 2020 and beyond.

# Planned Features
- Motor Controller DSL for simplified construction along with an easy-to-use wrapper for many ESCs. `IN-PROGRESS`
```kotlin
val spark = sparkmax(0,MotorType.kBrushless) {
    pid(0.0, 0.0, 0.0)
    reverse(true)            
    ...
}
```
- Unit conversions and generic measurement types (Angle, Length, etc.) `IN-PROGRESS`
- Path generation and followers `IN-PROGRESS`
- Logging and dashboard wrapper
- Repurpose test mode for automatic diagnostics (like unit testing physical mechanisms) `✓`