# Part I - Region Selection Tool

The Region Selection Tool is used to select and create regions within a page for further OCR processing.

## Use Cases
This Section describes how the Region Selection Tool is used.

### Loading a Page
1. Click "Open" 
2. Select an Imagefile to open. The Image should be displayed

### Binarization
1. Click "Binary". The Page should now be displayed binarized.

### Despeckling
1. Click "Despeckling". The Page should now be freed from small speckles.

### Selecting Contours
You can select contours by dragging a rectangle or clicking on the contours:

#### Rectangular Selection

1. Create a rectangle on the displayed Page by dragging the mouse
2. Choose the selection mode by pressing the proper Radio-Button
3. Click "Rectangle". The selected Contours depending on the mode should now be highlighted. If the CTRL-Key is pressed while clicking the button the selection will be additional.

There are three different modes for the rectangular selection. 
- Exact: Selects all Contours exatcly within the rectangle. This will also split single contours.
- Inside: Selects all Contours that are completely inside the recantgle.
- Touching: Selects all Contours of which at least on Point is inside the rectangle 

#### Mouse click (De-)selection
Additionaly you can left click on single Contours to select those. If the CTRL-Key is pressed the clicked Contour will be added to the previous selection, otherwise it will be the only selected Contour. 


### Region creation
1. Select Contours you want to inlcude in a region
2. Click "Create". The new Region should be displayed with all previously selected Contours inside.

### Region splitting
1. Click "Split".
2. Draw the splitting line by clicking twice on the Page, first for the startpoint of the line, second for its endpoint.
3. After the second mouse click the Tool will split all regions which are crossed by this line and display the new regions accordingly.

### Region merging
This function is only implemented rudimentary, so that you can't select the regions you want to merge, but it will merge the first two regions in the internal region list.
1. Create at leaste 2 Regions.
2. click "Merge". The oldest two regions should be merged together and displayed accordingly.

## Developer Guide
The Developer Guide is provided in the form of a JavaDoc Documentation. JavaDoc can be used in your IDE, however, there's also an HTML-Version included in the Git reposory (/Teil_1/doc/).