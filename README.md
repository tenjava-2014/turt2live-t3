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
it too high or too low, or else you're at risk. Warm blocks increase your body temperature alongside being exposed to the sun while being sheltered 
decreases your body temperature alongside being exposed to the moon. But be warned, the sun and moon cause very harsh conditions: At night the world
temperature plumets and affects your ability to stay warm while during the day it skyrockets, harming your ability to stay cool.

There are five stages for which you are safe, and here they are:

| Temperature Range | Description | Effects                                        |
| :---------------- | :---------- | :--------------------------------------------- |
| < -20             | Hypothermia | You'll die within 30 seconds                   |
| -5 -> -20         | Shivering   | Your bones rattle your pockets, dropping items |
| -5 -> 15          | Normal      | Stay within this range to survive              |
| 15 -> 30          | Sweating    | It becomes more difficult to walk              |
| > 30              | Burning     | You'll die within 30 seconds                   |

In addition to the temperature, you will want to keep moving. At night if you stay in one place too long you will end up encountering some zombies.

Good luck and thank you for visiting tenjavria!

<!-- Hi, turt2live! This is the default README for every ten.java submission. -->
<!-- We encourage you to edit this README with some information about your submission – keep in mind you'll be scored on documentation! -->
