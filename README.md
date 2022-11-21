# BeastyButtons Project

<p align="center"><img src="Readme-Files/BeastyButtonsLogo.svg"></img></p>
This is the repository of the BeastyButtons Library for the Java Version of [Processing]("https://processing.org/").
This project is developed under the same license as the [official processing repository](https://github.com/processing/processing4).

# Installation

```
Option A: 

# Clone this repository and go to your processing install location.

# Paste the files in the local processing libraries folder

# Restart processing


Option B (currently not available):

# In processings sketch window go to the lybrary manager

# Search for Beastybuttons and install it form there
```

# Usage
For the usage please see the [BeastyButtons Project Website]("https://plhoster.github.io/BeastyButtons/") where you can find structural infos and Examples as well as the source code documentation.

# Tests
For now Testing was only done on Windows, however the usage on Linux and MacOS should mostly work just fine.
The only problems that can occure are related to interaction with the file system, so maybe avoid functions that read from or write to the file system.
That includes setting costum images as well as using import/export and using the logging system.

# Known Bugs
- tabswitch handler currently does not work
- importing files throws an error
- BB_Image copySettings() has some weird behaviour with image crops, for now crops have to be set manually

# Future Features (next Version update)
- rewritten fixed import and export system
- virtual keyboard for inputfields with the mousehandler
- individual costum font rendering for each Widget
- use HTML color codes in BeastyButtons

# Future Future Features (further version update)
- animating between surfaces with transitions like wipe and opacity transition
- new widgets: BB_Table, Dropdownlist, Slider, Radiobutton, Coordinatesystem
- auto scale new imports if the sketch size does not match up
- Visual GUI Builder to build a BeastyButtons Layout without coding
- export a Layout to a static HTML Website
- export a Layout to ControlP5

> current operating version: v1.1
