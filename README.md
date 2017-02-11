# Berries
A project which changes Pokemon Berries from MDScript to JavaPlugin with configuration
Eventually planning to integrate with Apricorns and Pokeballs (snowballs)



##Configuration

1. version
  * No need to touch this, nothing changes if you do.
2. berryPatches
  * A List of Worldguard regions that corresponds to each spot a berry can either be planted, or grow into.
3. berries
  * A List of Berries as they would be read by the lore on the item. Ex. An apple with lore Aguav would only activate as a berry if the entry Aguav were in the config here, AND the color of the lore matched the `color` entry.
    + color: A single character, 0-9,a-f,k,l-o that matches the color code on the berry's lore seen in game. A berry with lore *Oran* would only activate if this entry were l. Defaults to 2 (Dark_Green).
    + stalktype: The typeID of the plant which appears when the berry is planted. Typically 59:`crops`, 141:`carrot`, or 142:`potato`. Defaults to 59.
    + effects: a comma-delimited list of what the berry does when activated. Valid values are:
      - `smallheal`: heals the user for 4 Health (2 Hearts)
      - `largeheal`: heals the user for 10 Health (5 Hearts)
      - `confusionflavor`: gives the user Confusion potion effect if their UUID corresponds to the flavor of the berry
      - `cureslow`: removes the potioneffect of slowness if the user has it
      - `curepoison`: removes the potioneffect of poison if the user has it
      - `cureconfusion`: removes the potioneffect of confusion if the user has it
      - `cureburn`: removes any fireticks the user has
      - `boostspeed`: gives the user Speed potioneffect with amplifier 1, duration 5 seconds
      - `boostdamage`: for on-hit activations, adds 4 damage to the attack
      - `fixitem`: for attached berries, gives the item 1/4 of its durability back
      - `boostmcmmoluck`: gives the user the permission to boost mcmmo Luck benefits for a short time (Doesn't rely on mcmmo, just gives a timed permission node)
      - `quarterdamage`: incoming attacks have their damage reduced to 1/4 of original damage
      - `reducedamage`: incoming attacks have their damage reduced by 5 (2.5 Hearts)
      - `addconfusion`: gives the user confusion potioneffect with amplifier 1, duration 5 seconds
    + flavor: only used in `confusionflavor`, determines which UUID will get confused by using it. Valid values are:
      - `bitter`: default
      - `sour`
      - `spicy`
      - `sweet`
      - `dry`
    + uses: a comma-delimited list of how the berry can be activated by the user. Valid values are:
      - `consume`: activates when the user eats the item
      - `attack`: activates when the user is attacking another player or entity
      - `damage`: activates when the user has been damaged by any source
      - `lowhealth`: for automatic activations, activates when the user has 5 health or less
      - `middlehealth`: for automatic activations, activates when the user has 8 health or less
      - `highhealth`: for automatic activations, activates when the user has 10 health or less
      - `criticalhit`: for automatic activations, activates when the user has performed a critical hit
      - `itemdurability`: for attached berries, activates when the item has 1/2 or less of its durability left
      - `physicaldamage`: for attached berries, activates when the user has been hit by a non-projectile attack
      - `rangeddamage`: for attached berries, activates when the user has been hit by a projectile attack
    + growthdelaychance: percent chance, when a berry grows, to fail and delay the step in increasing the berry plant size. Defaults to 0 (No delay).