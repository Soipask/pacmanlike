# Pacmanlike

List of TODOs:

1. Game logic 
    - pacman working on a basic map (backend map: just 2d array, frontend map: probably png)
    - map ideally provided by some parameter (some mock class that has hardcoded map) to game logic
2. Few custom maps and save them to storage
    - one file for text representation of map and one for highscore for that map
3. Screen for selecting maps
    - goes through storage files and takes maps
    - maybe makes buttons for each map with its title, after clicking goes to game screen and sends this map as parameter
    - button for Create map, which will do nothing at the beginning
4. Main menu screen
    - logo
    - two buttons
        - Play
        - Exit
    - maybe some graphics, if we get bored of programming
    - maybe names and year with little font?
    - probably one of the custom maps in the background
5. Screen for creating custom maps
    - maybe folder in the storage
        - where there'll be png for every possible type of the road (at least 2x2 in cells of the map)
        - and same-named txt for text representation of that tile
    - show all these pngs in the toolbar (and make them selectable)
    - in the middle: empty map of set size with places, where the tiles can be placed
    - select tile and select place where to put it
    - button for Delete (maybe click on already placed tile removes it?)
    - button for Create (map will be constructed and checked if it's valid)
        - dfs probably
        - also check if map works from the graphics side?
    - maybe add text input for title of the map?
    - if everything goes well, add Import/Export button that either gets string of a map to parse and save or writes (and copies to clipboard) string for map sharing of some map