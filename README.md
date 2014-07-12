turt2live's ten.java submission
==============================

[![ten.java](https://cdn.mediacru.sh/hu4CJqRD7AiB.svg)](https://tenjava.com/)

This is a submission for the 2014 ten.java contest.

- __Theme:__ Random Events
- __Time:__ Time 3 (7/12/2014 14:00 to 7/13/2014 00:00 UTC)
- __MC Version:__ 1.7.9 (latest Bukkit beta)
- __Stream URL:__ [Twitch](twitch.tv/turt2live)

<!-- put chosen theme above -->

---------------------------------------

Compilation
-----------

- Download & Install [Maven 3](http://maven.apache.org/download.html)
- Clone the repository: `git clone https://github.com/tenjava/turt2live-t3`
- Compile and create the plugin package using Maven: `mvn`

Maven will download all required dependencies and build a ready-for-use plugin package!

---------------------------------------

Usage
-----

1. Install plugin
2. Configure your default world to use this plugin's world generator by opening your Bukkit.yml and adding this to the end (where 'world' is your world name):
```
worlds:
  world:
    generator: turt2live-t3
```

Now all you have to do is survive.

Mechanics
---------

You have several things to look out for while exploring tenjavria (pronounced ten-jav-ree-ah). These things include all of your standard Minecraft
mechanics (health, food, not dying, etc) as well as the risks that come with tenjavria. One of the major risks is body temperature. You don't want
it too high or too low, or else you're at risk. Warm blocks (like fire) increase your body temperature while cold blocks (like ice) cool your body
temperature. During the day the world temperature hits an insane amount, so stay cool. During the night however, the temperature drops, so get yourself
warm! Be careful though, the day/night changes in temperature are very quick!

There are five stages of body temperature, and here they are:

| Temperature Range | Description | Effects                                        |
| :---------------- | :---------- | :--------------------------------------------- |
| < -20             | Hypothermia | You'll die VERY soon                           |
| -5 -> -20         | Shivering   | Your bones rattle your pockets, dropping items |
| -5 -> 15          | Normal      | Stay within this range to survive              |
| 15 -> 30          | Sweating    | It becomes more difficult to walk              |
| > 30              | Burning     | You'll die VERY soon                           |

In addition to the temperature, you will want to keep moving. At night if you stay in one place too long you will end up encountering some zombies.

Good luck and thank you for visiting tenjavria!

Notes
-----

Planned features that were supposed to be working include the following (see commit log for information):

- Surface temperature
- Player temperature (where armor, inventory item count, and how much you walk matter)
- Blocks which keep you warm/cool
- Random chests which have items (proved to be too resource intensive to have that many chests)

Ideally the plan was to have the sun/moon cause severe temperature differences so that the player would have to think about how much they move, where they build,
what they build, and what they wear. Having any of these mechanics balanced off by just enough would cause the player to loose items (shivering), walk slower (sweating),
or die VERY quickly (hypothermia / overheating). I may pick this up after the contest just to damn well finish it, but oh well.

<!-- Hi, turt2live! This is the default README for every ten.java submission. -->
<!-- We encourage you to edit this README with some information about your submission – keep in mind you'll be scored on documentation! -->
