<img width="50%" src="https://6502.team/img/external/kyber-dark.png">
DARC SIDE's standard FRC libraries for 2020 and beyond.



<br>

# Features

<img width="30%" src="https://6502.team/img/external/speedcontrol.png">
Kyberlib provides unified ESC wrappers with consistent Kotlin-style API calls. Have you ever wanted a Victor SPX to follow a Spark MAX? Now you can. Also, gone are the days of CAN IDs. Kyberlib allows you to store device IDs in a central registry, and will automatically look them up when declaring motor controllers by a device name.
<br><br>
<img width="30%" src="https://6502.team/img/external/unitconversion.png">
Kyberlib allows for conversion between units of distance, angle, velocity, and more. All without messy function calls. For example, 4.feet.inches => 48. It also includes some useful operators, like finding the shortest route between two angles.
<br><br>
<img width="30%" src="https://6502.team/img/external/diagnostics.png">
Your team has made it to the last tiebreaker match of finals, but when the match begins you realize that a cable for your intake wasn't plugged back in after some routine maintenance, rendering your robot unusable. With Kyberlib's diagnostic tools, you can write automated tests for mechanisms that can be executed in queue before each and every match to prevent this very scenario. It's like unit testing, but for actual hardware. The diagnostic framework is built on WPILib's command-based architecture, so writing them is already familiar.
<br><br>
<img width="30%" src="https://6502.team/img/external/ledanim.png">
Kyberlib adds a variety of animations compatible with WPILib's new AddressableLED class. Multiple animation regions on a single strip, conditional regions for behavior-driven animation, and region transparency to overlap multiple animations will make your robot shine. Literally.

## More coming soon, including
- Limelight wrapper
- Automatic WPILib motion profile regeneration

# Installation

In `build.gradle`:

- Add `maven { url 'https://jitpack.io' }` to your `repositories { ... }` block
- Add `compile 'com.github.DurhamAcademy:kyberlib:master-SNAPSHOT'` to your  `dependencies { ... }` block

`master-SNAPSHOT` will always give you the latest version, but a version number can also be specified. More information [here](https://jitpack.io/#DurhamAcademy/kyberlib/). For development, kyberlib can also be published to and used from the local Maven repository.
