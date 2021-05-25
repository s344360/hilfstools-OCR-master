package Classes.Data;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javafx.scene.image.Image;

/**
 * class that select and deselect contours, creates regions, merge and splits
 * them.
 */
public class Page {

	private int scaling;
	private Image img_o; // Original Image
	private Image img_bin;
	private File file;

	// Matrizen of the images
	private Mat matGray;
	private Mat matBin;
	private Mat matBinInv;
	private Mat matCur; // Mat to the current displayed Image (incl. Regions and
						// selected Contours)

	private List<Contour> allContours = new ArrayList<Contour>();
	private List<Contour> selectedContours = new ArrayList<Contour>();

	private LinkedList<Region> regions;

	// Colors
	private Scalar colSelectionRect = new Scalar(255, 0, 0); // Blue
	private Scalar colConture = new Scalar(0, 0, 255); // Red
	private Scalar colRegion = new Scalar(0, 255, 0); // Green
	private Scalar white = new Scalar(255, 255, 255);
	private Scalar black = new Scalar(0, 0, 0);
	
	
	/**
	 * @param f File of the Image to be displayed
	 */
	public Page(File f) {
		if (f == null)
			throw new NullPointerException("Error: Initializing with no file!");
		file = f;
		img_o = new Image("file:" + f.getAbsolutePath());
		regions = new LinkedList<Region>();

		System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
		matGray = Imgcodecs.imread(file.getPath(), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
		matBin = new Mat();
		Imgproc.threshold(matGray, matBin, 100, 255, Imgproc.THRESH_BINARY);

		matBinInv = new Mat();
		Imgproc.threshold(matGray, matBinInv, 100, 255, Imgproc.THRESH_BINARY_INV);

		init_binImg();

		init_Contours();
		updateCurImg();
	}

	/**
	 * Finds the contours in the image and saves them in variable allContours.
	 */
	private void init_Contours() {

		System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
		Mat workMat = matBinInv.clone(); // working copy of the binary Matrix
		Mat hierarchy = new Mat();

		List<MatOfPoint> allContoursMOP = new ArrayList<MatOfPoint>();

		Imgproc.findContours(workMat, allContoursMOP, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		for (MatOfPoint mop : allContoursMOP) {
			allContours.add(new Contour(mop));
		}
	}

	/**
	 * find the biggest contour and remove it from the list
	 *
	 * @param contour
	 *            List of contours, where the biggest should be removed
	 */
	public void removeBoundingRect(List<MatOfPoint> contour) {
		int index = 0;
		double area = Imgproc.contourArea(contour.get(0));
		for (int i = 1; i < contour.size(); i++) {
			if (area < Imgproc.contourArea(contour.get(i))) {
				area = Imgproc.contourArea(contour.get(i));
				index = i;
			}
		}
		contour.remove(index);
	}

	/**
	 * create the binary Image from the binary matrix
	 */
	private void init_binImg() {
		System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
		MatOfByte byteMat = new MatOfByte();
		Imgcodecs.imencode(".bmp", matBin, byteMat);
		img_bin = new Image(new ByteArrayInputStream(byteMat.toArray()));
	}

	// Contours ---------------------------------------------------------------
	/**
	 * Checks whether mouseclick was on selected contour, To select or deselect
	 * the contour.
	 *
	 * @param p
	 *            mouseclick point.
	 * @param ctrlP
	 *            control key variable, if true, saves all previous selected
	 *            contours on the image.
	 * @return a image with more selected contours or less.
	 */
	public Image CheckIfContourWasClicked(Point p, boolean ctrlP) {
		if (!selectedContours.isEmpty()) {
			int index = 0;
			for (int i = 0; i < selectedContours.size(); i++) {
				Rect rect = selectedContours.get(i).getBounds();
				if (p.inside(rect)) {
					index = i;
					return deselctSingelContour(p, ctrlP, index);

				}

			}

		}
		return drawSingelContour(p, ctrlP);
	}

	/**
	 * select a single contour and colors it at point p.
	 *
	 * @param p
	 *            mouseclick point.
	 * @param ctrlP
	 *            control key variable, if true, saves all previous selected
	 *            contours on the image.
	 * @return a image with selected contours to the position p.
	 */
	public Image drawSingelContour(Point p, boolean ctrlP) {
		if (!ctrlP)
			selectedContours.clear();
		for (int i = 0; i < allContours.size(); i++) {
			Rect rect = allContours.get(i).getBounds();
			if (p.inside(rect)) {
				selectedContours.add(allContours.get(i));
				break;
			}

		}
		updateCurImg();
		return mat2Img(matCur);
	}

	/**
	 * deselect a single contour and remove it from the selected Contours List.
	 *
	 * @param p
	 *            mouseclick point.
	 * @param ctrlP
	 *            control key variable, if true, saves all previous selected
	 *            contours on the image.
	 * @return a image after the deselect contour to the position p.
	 */
	public Image deselctSingelContour(Point p, boolean ctrlP, int index) {
		selectedContours.remove(index);
		return drawSelectedContours();

	}

	/**
	 * Draw all the contours that are inside the rectangle and when they touch
	 * it.
	 *
	 * @param rect
	 *            selection rectangle
	 * @param ctrlP
	 *            control key variable, if true, saves all previous selected
	 *            contours on the image.
	 * @return a image with selected contours within the rectangle and touch it.
	 *
	 */
	public Image drawContoursTouchingRect(Rect rect, boolean ctrlP) {
		if (!ctrlP)
			selectedContours.clear();
		Mat imgRGB = getBinMatRGB();
		// Draw Contours with at least 1 Point inside rect
		for (int i = 0; i < allContours.size(); i++) {
			boolean inside = false;
			Point[] points = allContours.get(i).getMop().toArray();
			for (Point p : points) {
				if (p.inside(rect)) {
					inside = true;
					break;
				}
			}
			if (inside) {
				selectedContours.add(allContours.get(i));
			}
		}
		Imgproc.rectangle(imgRGB, rect.tl(), rect.br(), colSelectionRect, scaling, 8, 0);
		updateCurImg();
		return mat2Img(matCur);
	}

	/**
	 * Draw all the contours that are only exact inside the rectangle.
	 *
	 * @param rect
	 *            selection rectangle
	 * @param ctrlP
	 *            control key variable, if true, saves all previous selected
	 *            contours on the image.
	 * @return a image with selected contours and part of contours.
	 *
	 */
	public Image drawContoursExactInRect(Rect rect, boolean ctrlP) {
		if (!ctrlP)
			selectedContours.clear();
		Mat imgRGB = getBinMatRGB();
		List<MatOfPoint> allContoursInRect = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();

		Mat matinRectMat = new Mat(matBin, rect);

		Mat mask_image = new Mat(matBin.rows(), matBin.cols(), matBin.type(), white);
		matinRectMat.copyTo(mask_image.rowRange((int) rect.tl().y, (int) rect.br().y).colRange((int) rect.tl().x,
				(int) rect.br().x));

		Imgproc.findContours(mask_image, allContoursInRect, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

		// find largest contour and remove it.
		removeBoundingRect(allContoursInRect);

		for (MatOfPoint mop : allContoursInRect) {
			selectedContours.add(new Contour(mop));
		}

		updateCurImg();
		return mat2Img(matCur);
	}

	/**
	 * Draw all the contours that are completely inside the rectangle.
	 *
	 * @param rect
	 *            selection rectangle
	 * @param ctrlP
	 *            control key variable, if true, saves all previous selected
	 *            contours on the image.
	 * @return a image with selected complete contours.
	 *
	 */
	public Image drawContoursInRect(Rect rect, boolean ctrlP) {
		if (!ctrlP)
			selectedContours.clear();
		Mat imgRGB = getBinMatRGB();

		// Draw Contours with all Points inside rect
		for (int i = 0; i < allContours.size(); i++) {
			boolean inside = true;
			Point[] points = allContours.get(i).getMop().toArray();
			for (Point p : points) {
				if (!p.inside(rect)) {
					inside = false;
					break;
				}
			}
			if (inside) {
				selectedContours.add(allContours.get(i));
			}
		}
		Imgproc.rectangle(imgRGB, rect.tl(), rect.br(), colSelectionRect, 1 * scaling, 8, 0);
		updateCurImg();
		return mat2Img(matCur);
	}

	/**
	 * color all contours red, which are in the list selectedContours.
	 *
	 * @return Image with colored contours.
	 */
	public Image drawSelectedContours() {
		Mat matImg = getBinMatRGB();

		for (Contour c : selectedContours) {
			List<MatOfPoint> tmp = new ArrayList<MatOfPoint>();
			tmp.add(c.getMop());
			Imgproc.drawContours(matImg, tmp, 0, colConture, -1);
		}
		return mat2Img(matImg);
	}
	// Regions -----------------------------------------------------------------

	/**
	 * @return image where regions boundaries are drawn.
	 */
	public Image drawRegions() {
		Mat imgRGB = getBinMatRGB();
		List<MatOfPoint> listRegions = new ArrayList<MatOfPoint>();
		for (int i = 0; i < regions.size(); i++) {
			MatOfPoint mopPoints = new MatOfPoint();
			mopPoints.fromList(regions.get(i).getPoints());
			listRegions.add(mopPoints);
		}
		Imgproc.polylines(imgRGB, listRegions, true, colRegion, 2 * scaling);
		// For Remove testing:
		for (Region r : regions) {
			for (Point p : r.getPoints()) {
				Imgproc.circle(imgRGB, p, 2 * scaling, colConture, 2 * scaling);
			}

		}
		selectedContours.clear();
		return mat2Img(imgRGB);

	}

	/**
	 * create regions contained contours and add them in the list regions.
	 */
	public void createRegion() {
		if (selectedContours.isEmpty()) {
			System.out.println("no Contours selected to create a Region");
			return;
		}
		regions.add(new Region(selectedContours, matBin));
		selectedContours.clear();
		updateCurImg();
	}

	/**
	 * Method to divide regions by a splitting line along two points.
	 *
	 * @param splitFirst
	 *            Point1 for the line.
	 * @param splitSecond
	 *            Point2 for the line.
	 */
	public void splitRegions(Point splitFirst, Point splitSecond) {
		// new Empty bin image, with all Regions filled and a new whit split
		// Line drawn
		Mat splitMat = new Mat(matBin.rows(), matBin.cols(), matBin.type(), black);
		LinkedList<MatOfPoint> regionsListMOP = new LinkedList<MatOfPoint>();
		for (Region r : regions) {

			regionsListMOP.add(r.getRegionMOP());
		}
		Imgproc.fillPoly(splitMat, regionsListMOP, white);
		Imgproc.line(splitMat, splitFirst, splitSecond, black, 2); // Thickness
																	// testen
		// find new Contours
		Mat hierarchy = new Mat();
		List<MatOfPoint> regionOutlineList = new LinkedList<MatOfPoint>();
		Imgproc.findContours(splitMat, regionOutlineList, hierarchy, Imgproc.RETR_EXTERNAL,
				Imgproc.CHAIN_APPROX_SIMPLE);
		// Override regions
		regions.clear();
		for (int i = 0; i < regionOutlineList.size(); i++) {
			// using each Region as a Stencil to get the Contours inside
			Mat regionMat = new Mat(matBin.rows(), matBin.cols(), matBin.type(), black);
			Imgproc.drawContours(regionMat, regionOutlineList, i, white, -1);
			Core core = new Core();
			Mat maskMat = matBin.clone();
			core.bitwise_and(matBin, regionMat, maskMat);
			// Find Contours in MaskedImage and create new Region with those
			// contours as selectedContours
			Mat rHierarchy = new Mat();
			List<MatOfPoint> rContoursMOP = new LinkedList<MatOfPoint>();
			Imgproc.findContours(maskMat, rContoursMOP, rHierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
			List<Contour> rContours = new LinkedList<Contour>();
			for (MatOfPoint m : rContoursMOP) {
				rContours.add(new Contour(m));
			}
			Region r = new Region(regionOutlineList.get(i));
			r.setInitialContours(rContours);
			regions.add(r);
			updateCurImg();
		}
	}

	// Old (without Contour Class
	public void splitRegionsO(Point splitFirst, Point splitSecond) {
		// new Empty bin image, with all Regions filled and a new whit split
		// Line drawn
		Mat splitMat = new Mat(matBin.rows(), matBin.cols(), matBin.type(), black);
		LinkedList<MatOfPoint> regionsListMOP = new LinkedList<MatOfPoint>();
		for (Region r : regions) {
			regionsListMOP.add(r.getRegionMOP());
		}
		Imgproc.fillPoly(splitMat, regionsListMOP, white);
		Imgproc.line(splitMat, splitFirst, splitSecond, black, 2); // Thickness
																	// testen
		// find new Contours
		Mat hierarchy = new Mat();
		List<MatOfPoint> ContourList = new LinkedList<MatOfPoint>();
		Imgproc.findContours(splitMat, ContourList, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		// Override regions
		regions.clear();
		for (MatOfPoint m : ContourList) {
			regions.add(new Region(m));
		}
	}

	/**
	 * Two regions are merged into big one.
	 *
	 * @param r1
	 *            Region contains set of contour.
	 * @param r2
	 *            Region contains set of contour.
	 * @return image after merge to regions.
	 */
	public Region mergeRegions(Region r1, Region r2) {
		// Create a new Region with all Contours in r1 and r2
		List<Contour> allContoursRegions = new LinkedList<Contour>(r1.getInitialContours());
		allContoursRegions.addAll(r2.getInitialContours());

		return new Region(allContoursRegions, matBin);
	}

	// Test / Old
	public Image mergeRegionsI(Region r1, Region r2) {

		Mat mergeMat = new Mat(matBin.rows(), matBin.cols(), matBin.type(), black);
		LinkedList<MatOfPoint> regionsListMOP = new LinkedList<MatOfPoint>();
		regionsListMOP.add(r1.getRegionMOP());
		regionsListMOP.add(r2.getRegionMOP());
		Imgproc.fillPoly(mergeMat, regionsListMOP, white);

		// find new Contours
		Mat hierarchy = new Mat();
		List<MatOfPoint> contourList = new LinkedList<MatOfPoint>();
		Imgproc.findContours(mergeMat, contourList, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

		List<Contour> tmpList = new ArrayList<Contour>();
		selectedContours.clear();
		for (MatOfPoint mop : contourList) {
			tmpList.add(new Contour(mop));
			selectedContours.add(new Contour(mop));
		}
		return drawSelectedContours();
	}

	/**
	 * remove all regions in the list regions.
	 */
	public void clearRegions() {
		regions.clear();
		updateCurImg();
	}

	/**
	 * Updates the current Image by displaying the selected Contours 
	 * and the Regions.
	 */
	private void updateCurImg() {
		matCur = getBinMatRGB();
		// Selected Contours
		for (Contour c : selectedContours) {
			List<MatOfPoint> tmp = new ArrayList<MatOfPoint>();
			tmp.add(c.getMop());
			Imgproc.drawContours(matCur, tmp, 0, colConture, -1);
		}
		// Regions
		List<MatOfPoint> listRegions = new ArrayList<MatOfPoint>();
		for (Region r : regions) {
			listRegions.add(r.getRegionMOP());
		}
		Imgproc.polylines(matCur, listRegions, true, colRegion, 2 * scaling);
	}

	// Getter / Setter ---------------------------------------------------------

	public Image getCurrentImg() {
		return mat2Img(matCur);
	}

	/**
	 * 
	 * @param mat Matrix to be converted
	 * @return Image of the mat
	 */
	public Image mat2Img(Mat mat) {
		MatOfByte byteMat = new MatOfByte();
		Imgcodecs.imencode(".bmp", mat, byteMat);
		return new Image(new ByteArrayInputStream(byteMat.toArray()));
	}

	public Mat getMatCur() {
		return matCur;
	}

	/**
	 * @return return the binary Mat but in Color-Code, so that you can draw
	 *         with colors on it.
	 */
	public Mat getBinMatRGB() {
		// create 8bit color image.
		Mat imgRGB = new Mat(matBin.size(), CvType.CV_8UC3);

		// convert binary to color image
		Imgproc.cvtColor(matBin, imgRGB, Imgproc.COLOR_GRAY2RGB);
		return imgRGB;
	}

	/**
	 * @return the inverted binary Mat but in Color-Code, so that you can draw with
	 *         colors on it.
	 */
	public Mat getBinInvMatRGB() {
		// create 8bit color image.
		Mat imgRGB = new Mat(matBinInv.size(), CvType.CV_8UC3);

		// convert binary to color image
		Imgproc.cvtColor(matBinInv, imgRGB, Imgproc.COLOR_GRAY2RGB);
		return imgRGB;
	}

	public Image getBinImage() {
		if (img_bin == null)
			init_binImg();
		return img_bin;
	}

	public Image getImage() {
		return img_o;
	}

	public void setImage(Image image) {
		this.img_o = image;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return deep Copy of the LinkedList of the regions
	 */
	public LinkedList<Region> getRegionsDeepCopy() {
		LinkedList<Region> res = new LinkedList<Region>();
		for (Region r : regions) {
			res.add(r.deepCopy());
		}
		return res;
	}

	public LinkedList<Region> getRegions() {
		return regions;
	}

	public void setRegions(LinkedList<Region> regions) {
		this.regions = regions;
	}

	public Mat getMatGray() {
		return matGray;
	}

	public void setMatGray(Mat matGray) {
		this.matGray = matGray;
	}

	public Mat getMatBin() {
		return matBin;
	}

	public void setMatBin(Mat matBin) {
		this.matBin = matBin;
	}

	public int getScaling() {
		return scaling;
	}

	public void setScaling(int scaling) {
		this.scaling = scaling;
	}
}
