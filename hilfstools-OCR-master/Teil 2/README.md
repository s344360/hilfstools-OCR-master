# Part II - Distribution Tool
## User Guide
This Section describes how the Distribution Tool is used.

### GUI Description
#### Main View
The Main View contains 4 main sections (described from top to bottom).
- The Train/Test Sets lists including the move and "distribute" buttons showing the current items in the Sets.
- The Detail View showing details on currently selected item (in the Set lists above).
- The Stats Logs showing statistics on the current Train/Test Sets.
- The Error Log showing errors and warnings (e.g. errors while loading files or performing distributions).

#### MovePreview View
The MovePreview View show changes that would occur if the move action was executed. See "Moving Items" section.

#### Settings View
The Settings View contains two tabs of different settings. See Settings section for details.

### Use Cases
#### Loading Files
1. Click "File" > "Open" in the Menu bar.
2. Select a folder to open.
3. Click "select folder". The Data should then be loaded.

#### Saving Files
1. Click "File" > "save" in Menu bar.
"Train" and "Test" folders will be created in the initially opened folder.

#### Changing Settings
1. Click "Settings" in the Menu bar.
See Settings section of the User Guide for details on settings.
2. Click "ok" to apply the settings or "cancel" to discard them.

#### Performing Distribution
Distribution is automatically performed when data is loaded. If you want to perform the distribution again, click the "distribute" button between the Train/Test Set lists.

#### Moving Items
1. Select the items in the list of the Set you want to remove them from. Use Shift/Ctrl.-keys for multiselect.
2. Click the "->" or "<-" buttons, depending on there you want to move the selected items.
3. The MovePreview View will show the effects of the change. To perform the move click "ok". Click "cancel" to keep the Sets unchanged.

#### Filter Train/Test Sets
You can multiselect items from the Stats Log to filter the Train/Test Sets by items that contain the selected character or match the filter type. All filter rules are linked using a logic or operation. To remove the filters, select the "Items" row.



## Settings
This section describes the settings available in the Settings View.

#### Distribution & Highlighting
Main settings page for the item distribution as well as the stat highlighting.
- Distribution Slider: Lets you chose how the items should be distributed into Train/Test Sets.
- Force 100% Train: Lets you force all items of a specific type into the Train Set.
- Character Compensation: Character Compensation tries to make the Train Set contain all available characters at least as many times as defined in the "Highlighting" > "warning" input.
- Highlighting / Thresholds: Set when Filter Results in the Stats Log are highlighted orange (note) or red (warning).

### Statistics
Lets you chose which statistics are to be shown in the Stats Log and MovePreview.
- item counts: total number of elements
- type counts: count of elements by Type (Paragraph, Heading, etc.)
- letter counts: count of characters (#A, #B, etc.)



## Developer Guide
The Developer Guide is provided in the form of a JavaDoc Documentation. JavaDoc can be used in your IDE, however, there's also an HTML-Version included in the Git reposory (/Teil 2/doc/).