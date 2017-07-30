package sam.baldurgurkreactor

object ShopItemList {

    val allItems = mapOf<Int,Item>(
        0 to  Item(
                name = "rusty helmet",
                stat_requirement = StatRequirement(strength_range = 3..7,dexterity_range = 2..6,intelligence_range = 2..6),
                equipment_slot = 0,
                ability = Ability(type = "move", relative_pairs = listOf(Pair(0,1),Pair(1,0), Pair(-1,0),Pair(1,-1),Pair(-1,-1)),speed = 3),
                price = 0
        ),
        1 to Item(
                name = "tattered clothes",
                stat_requirement = StatRequirement(3..8,2..6,2..6),
                equipment_slot = 1,
                ability = Ability("move", listOf(Pair(3,0), Pair(-3,0)),6),
                price = 0
        ),
        2 to Item(
                name = "old shoes",
                stat_requirement = StatRequirement(3..9,2..6,2..6),
                equipment_slot = 2,
                ability = Ability("move", listOf(Pair(0,2),Pair(2,0), Pair(-2,0),Pair(0,-2)),5),
                price = 0
        ),
        3 to Item(
                name = "beaten sheild",
                stat_requirement = StatRequirement(3..8,2..6,2..6),
                equipment_slot = 3,
                ability = Ability("attack", listOf(Pair(0,1),Pair(1,0),Pair(-1,0)),3),
                price = 0
        ),
        4 to Item(
                name = "dusty knife",
                stat_requirement = StatRequirement(3..10,2..6,2..6),
                equipment_slot = 4,
                ability = Ability("attack", listOf(Pair(0,2),Pair(1,1), Pair(-1,1),Pair(0,-1)),4),
                price = 0
        ),
        5 to Item("sword of deadliness", StatRequirement(5..10,2..6,2..6),4, Ability("attack", listOf(Pair(0,1),Pair(0,3),Pair(1,2),Pair(-1,2)),8),5),
        6 to Item("mage stick", StatRequirement(5..8,2..6,2..6),4, Ability("attack", listOf(Pair(0,3),Pair(1,2),Pair(-1,2),Pair(-1,3),Pair(1,3)),3),5),
        7 to Item("cloth boots", StatRequirement(5..10,2..6,2..6),2, Ability("move", listOf(Pair(0,1),Pair(0,3),Pair(1,2),Pair(-1,2)),3),9),
        8 to Item("plate legs", StatRequirement(5..8,2..6,2..6),2, Ability("move", listOf(Pair(0,2),Pair(-1,3),Pair(1,2),Pair(-1,2)),8),3),
        9 to Item("knight sheild", StatRequirement(5..10,2..6,2..6),3, Ability("attack", listOf(Pair(0,2),Pair(0,3),Pair(1,2),Pair(-1,2)),5),2),
        10 to Item("great helm", StatRequirement(5..8,2..6,2..6),0, Ability("move", listOf(Pair(-1,-3),Pair(0,-3),Pair(1,2),Pair(-1,2)),3),7),
        11 to Item("duelist buckler", StatRequirement(5..10,2..6,2..6),3, Ability("attack", listOf(Pair(0,1),Pair(1,0),Pair(1,2),Pair(-1,2)),8),2),
        12 to Item("chainmail", StatRequirement(5..8,2..6,2..6),1, Ability("move", listOf(Pair(-1,-1),Pair(-1,-2),Pair(1,2),Pair(-3,1)),2),7)
    )
}