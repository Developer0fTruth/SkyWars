Configuring Kits - kits.yml
===========================

This file is located at `plugins/SkyWars/kits.yml`.

Note: This document is on how to manually type in new kits into the config. To much more quickly create kits just from items in your inventory, check out [Creating a new kit](https://dabo.guru/projects/skywars/creating-a-new-kit).

The following is a version of the default kits.yml config, annotated with comments. For a complete reference of all possible values, see the [kits.yml reference](https://dabo.guru/projects/skywars/reference/kits/).

```yaml
# ####### kits.yml #######
#
# Kit configuration
#
# For documentation, please visit
# https://dabo.guru/projects/skywars/configuring-kits
# #########

# Note that to enable most of these kits, you must enable economy support in main-config.yml.
bowman:
  # This kit has a cost of $100. The cost is charged every game in which it is used.
  cost: 100
  items:
    # You can list any number of items here.
    # Each item has a type section. It also may optionally have an enchantments
    # section and amount value.
    # The type is a material name. You can get a list of these here:
    # https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html
    - {type: ARROW, amount: 16}
    # When amount is not given, it defaults to 1.
    - {type: BOW}
whacker:
  cost: 20
  items:
    - {type: STICK, enchantments: {KNOCKBACK: 3},
        name: "Whacking Stick", lore: ["&2Whacking", "&4and hacking"]}
potions:
  # This kit demonstrates how to enter potions into kits.
  cost: 100
  items:
    # Potions:
    # There are two ways to add potions to an item:
    #
    # Using `potion: {TYPE, extended: true/false, splash: true/false, level: 1/2}`
    # will turn the item into one of the set potions in minecraft, and update the
    # item name and lore accordingly. TYPE in this case is a PotionType, see links below.
    #
    # You can also add "custom" potion effects, using:
    # `extra-effects: [{type: TYPE, duration: SECONDS, amplifier: AMPLIFIER}, {type: TYPE2, ...}]`
    # For extra-effects, TYPE is a PotionEffectType, see the links below. SECONDS is how long it
    # will last, AMPLIFIER is similar to a potion level, but can go up to 128. amplifier: 0
    # represents the default effects, and is the default value.
    #
    # When adding custom potion effects, it is recommended to still have one "regular" potion set.
    # Even if you override it with a custom effect, it will allow the item's name and lore to
    # update for the potion it holds.

    # this is a drinkable invisibility potion
    - {type: POTION, amount: 2, potion: {type: INVISIBILITY}}
    # this potion is a double edged sword - it gives speed, and poison.
    # however, the speed lasts a bit longer than the poison does.
    - {type: POTION, amount: 2, potion: {type: SPEED, splash: true},
                          extra-effects: [
                            # this will overwrite the main speed effect, while allowing the text to remain.
                            # I honestly have no idea what this duration is counted in.
                            {type: SPEED, duration: 2000},
                            {type: POISON, duration: 1000}
                          ]}
    # Full heal - the higher the amplifier, the more effect it has.
    - {type: POTION, amount: 3, potion: {type: HEAL, splash: true}}
    - {type: POTION, amount: 4, potion: {type: HEAL, splash: true},
                          extra-effects: [{type: HEAL, amplifier: 5}]}

diamond-swordsman:
  # You can define a permission, a cost, or both for a kit.
  # When you define a permission, the kit is only shown to people with that
  # permission. This allows you to make kits only available to donors.
  permission: server.donor
  items:
    - {type: DIAMOND_SWORD, amount: 1}
full-donor-armor:
  # This kit is only available to donors, but it also costs $500 each game!
  permission: server.donor
  cost: 500
  # You can define a helmet, chestplate, leggings and boots.
  # These will automatically be placed in the armor slots of the player.
  helmet: {type: DIAMOND_HELMET}
  chestplate: {type: DIAMOND_CHESTPLATE, enchantments: {PROTECTION_PROJECTILE: 2}}
  leggings: {type: DIAMOND_LEGGINGS}
  # See link above for exact enchantment names
  boots: {type: DIAMOND_BOOTS, enchantments: {PROTECTION_FALL: 2}}
```
Complete list of possible names:
- Materials:
  - https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html.
- Enchantments:
  - https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/enchantments/Enchantment.html.
- Potions (potion: {type: ...}):
  - https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionType.html
- Potions (extra-effects: [{type: ...}]):
  - https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionEffectType.html

The links above have more information than needed, but they are continually the most up to date lists of full names.
All you need to use a material, enchantment or potion is the UPPERCASE name.
