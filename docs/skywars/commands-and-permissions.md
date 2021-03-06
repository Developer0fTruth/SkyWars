Commands and Permissions
========================

### User Commands
| Command               | Permission         | Description         |
| :-------------------- | :----------------  | :------------------ |
| **/sw**               | N/A                | Lists all available SkyWars commands |
| **/sw** **join**      | skywars.join       | Joins the queue for the next game |
| **/sw** **leave**     | skywars.leave      | Removes you from the queue or the game you're in |
| **/sw** **kit**       | skywars.kit        | Lists kits, and lets you choose one |
| **/sw** **status**    | skywars.status     | Displays information on current games and queue status |
| **/sw** **version**   | skywars.version    | Displays the plugin's version |
| **/sw** **rank** `<name>` | skywars.rank   | Displays player stats for yourself or someone else |
| **/sw** **top**       | skywars.top        | Shows top 10 players with most score |

### Admin Commands

| Command               | Permission         | Description         |
| :-------------------- | :----------------  | :------------------ |
| **/sw** **setlobby**  | skywars.setlobby   | Sets the lobby location |
| **/sw** **lobby**     | skywars.lobby      | Teleports you to the lobby location |
| **/sw** **setportal** | skywars.setportal  | Sets a new portal at your current location |
| **/sw** **cancel**    | skywars.cancel     | Force stops a currently running game of the given ID |
| **/sw** **report**    | skywars.report     | Generates and submits a report. |
| **/sw** **forcestart**| skywars.forcestart | Force starts a game with the people in the queue. |
| **/sw** **testkit**   | skywars.testkit    | Replaces your inventory with a kit. |

Note: **/sw testkit** will immediately replaces your inventory with the kit, with no regard for what world you are in.

Note: **/sw report** will submit Server Software, Server Version, plugin information and all configuration information to gist.github.com, then give you a URL link to the data. This is useful when debugging.

### Setup Commands
| Command               | Permission         | Description         |
| :-------------------- | :----------------  | :------------------ |
| **/sws**              | skywars.setup      | Lists all available SkyWars setup commands that you can use currently |
| **/sws** **start** `<Name>`   | skywars.setup      | Starts creating a new arena, this will discard all unsaved arenas. |
| **/sws** **setpos1**  | skywars.setup      | Sets the first position for where to copy the arena from. |
| **/sws** **setpos2**  | skywars.setup      | Sets the position opposite from the first position to copy the arena from. |
| **/sws** **addspawn** | skywars.setup      | Adds a spawn position to the arena at your current location. |
| **/sws** **save**     | skywars.setup      | Saves the configuration to file under the name you started with. |
| **/sws** **createkit**    | skywars.setup  | Creates a kit from your current inventory. |
| **/sws** **update-arena** `<Name>` | skywars.setup | Updates an arena's "block cache". See Setting up a new arena for more information |

For a full guide on using the setup commands, check out [Setting up a new arena](https://dabo.guru/projects/skywars/creating-an-arena).

For a full guide on **/sws createkit**, check out [Creating a new kit](https://dabo.guru/projects/skywars/creating-a-new-kit)


### Permissions
| Permission        | Defaults to       | Function              |
| :---------------- | :---------------- | :-------------------- |
| skywars.join      | Everyone          | **/sw join**          |
| skywars.leave     | Everyone          | **/sw leave**         |
| skywars.version   | Everyone          | **/sw version**       |
| skywars.status    | Everyone          | **/sw status**        |
| skywars.top       | Everyone          | **/sw top**           |
| skywars.rank      | Everyone          | **/sw rank**          |
| skywars.rank.other | Everyone         | **/sw rank <name>**   |
| skywars.kit       | OP Only           | **/sw kit**           |
| skywars.lobby     | OP Only           | **/sw lobby**         |
| skywars.testkit   | OP Only           | **/sw testkit**       |
| skywars.setlobby  | OP Only           | **/sw setlobby**      |
| skywars.setportal | OP Only           | **/sw setportal**     |
| skywars.cancel    | OP Only           | **/sw cancel**        |
| skywars.report    | OP Only           | **/sw report**        |
| skywars.setup     | OP Only           | **/sw setup**         |
| skywars.forcestart| OP Only           | **/sw forcestart**    |
