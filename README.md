### In short, everyone gets 8 origin lives and each origin lasts 3 hours of time.

# Lives Mechanics
- Display 3-hour timer in bottom right of player's screen.
- If a player dies, they lose 1 hour of time. Display message for player: -1 hour.
- If the player dies with more than 1 hour remaining, their origin doesn't change.
- If the player dies with less than 1 hour remaining or their timer reaches 0, their origin changes and they lose one origin life.
- If a player loses an origin life, the color of their nametag changes depending on the number of origin lives left: 8 = White, 7 = Pink, 6 = Purple, 5 = Blue, 4 = Green, 3 = Yellow, 2 = Orange, 1 = Red
- If a player on their last origin dies with less than 1 hour left or their timer reaches 0, that player is put in spectator mode. Display message for all players: [playername] has run out of lives!

# Boogeyman Mechanics
- A random player is chosen as the boogeyman.
- If a player is not chosen, display message for players: You are NOT the boogeyman.
- If a player is chosen, display message for that player: You are the boogeyman.
- If the boogeyman kills a player, display message for boogeyman: You are cured.
- The player killed loses 2 hours of time. Display message: -2 hours.
- If the boogeyman doesn't kill a player, display message for boogeyman: You have failed. That player is put on their last origin