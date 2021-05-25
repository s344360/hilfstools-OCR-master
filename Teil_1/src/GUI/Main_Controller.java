package GUI;

import java.awt.Color;
import java.awt.image.BufferedImage;

import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.*;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import Classes.Data.Page;
import Classes.Data.Region;

//import com.sun.deploy.uitoolkit.impl.fx.Utils;

import Classes.Prep.BlackWhite;
import Classes.Prep.Despeckling;

/**
 * Controller for Main View.
 *
 * related class: GUI.Main FXML: GUI.Main.fxml
 */

public class Main_Controller implements Initializable {
	private Stage stage;
	private BorderPane rootLayout;
	ImageView imageView;
	Image image;
	BufferedImage img;
	Image imageBW;
	File file;

	String despPath;
	BufferedImage fileBW;
	Group group_for_rectangles = new Group();
	Rectangle new_rectangle;
	boolean new_rectangle_is_being_drawn;
	BlackWhite bw = new BlackWhite();
	Mat mat1;
	static double clickMousX;
	static double clickMousY;
	static int count = 0;
	static int count1 = 0;
	static int count2 = 0;

	List<MatOfPoint> contou;
	Mat copy;
	Image imageContour;
	File contour = new File("contour.jpg");;
	List<Rect> checkPoint = new ArrayList<Rect>();
	BufferedImage con;

	Point p1 = new Point(0.0, 0.0);
	Point p2 = new Point(0.0, 0.0);
	Rect rectBB;
	static boolean test = false;

	// New variables

	@FXML
	private ImageView imv_page;

	@FXML
	private AnchorPane pan_center;

	@FXML
	private VBox vobx_buttons;

	@FXML
	private RadioButton rb_exact;
	@FXML
	private RadioButton rb_inside;
	@FXML
	private RadioButton rb_touching;

	private Page page;

	// Rectangle Startpoint
	double starting_point_x, starting_point_y;
	Point pStartRect, pEndRect;
	double scaleFac; // Scaling factor from ImageView Size to original Image
						// size

	public boolean ctrlP = false; // true when control is pressed
	// Split line Points
	Point splitFirst, splitSecond; // Null when not in Split mode

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		imv_page = new ImageView();
		init_mouseEvents();
	}

	/**
	 * Mouseevents to select and deselect cotours and drag rectangles.
	 */
	private void init_mouseEvents() {
		imv_page.setCursor(Cursor.CROSSHAIR);

		imv_page.setOnMousePressed((MouseEvent event) -> {
			pStartRect = new Point(event.getX() * scaleFac, event.getY() * scaleFac);
			Image imgCon = page.CheckIfContourWasClicked(new Point(event.getX() * scaleFac, event.getY() * scaleFac),
					ctrlP);

			imv_page.setImage(imgCon);
		});

		imv_page.setOnMouseDragged((MouseEvent event) -> {
			pEndRect = new Point(event.getX() * scaleFac, event.getY() * scaleFac);
			Mat inclRec = page.getMatCur().clone();

			Imgproc.rectangle(inclRec, pStartRect, pEndRect, new Scalar(0, 0, 255), (int) scaleFac, 8, 0);

			imv_page.setImage(page.mat2Img(inclRec));

		});

	}

	/**
	 * Gets the Stage object from the class Main. and set a key event control.
	 *
	 * @param primaryStage
	 *            is Stage of the image.
	 */
	public void init(Stage primaryStage) {
		this.stage = primaryStage;
		stage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
			if (KeyCode.CONTROL == event.getCode()) {
				ctrlP = false;
			}
		});
		stage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
			if (KeyCode.CONTROL == event.getCode()) {
				ctrlP = true;
			}
		});

	}

	/**
	 * Gets the Borderpane object from the class Main.
	 *
	 * @param rootLayout
	 *            is a Borderpane object contains the image and various buttons.
	 */
	public void init2(BorderPane rootLayout) {
		this.rootLayout = rootLayout;
	}

	/**
	 * Event for clicking the "Open"-button. Opens a jpeg color image.
	 *
	 * @param event
	 */
	public void clickOpen(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		imageView = new ImageView();

		FileChooser.ExtensionFilter extFilterALL = new FileChooser.ExtensionFilter("All Files", "*.jpeg", "*.jpg",
				"*.pdf", "*.tiff", "*.tif", "*.JPG", "*.JPEG", "*.PDF", "*.TIFF", "*.TIF");
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG",
				"*.jpg", "*.JPEG", "*.jpeg");
		FileChooser.ExtensionFilter extFilterTIFF = new FileChooser.ExtensionFilter("TIFF files (*.tiff)", "*.TIF",
				"*.TIFF", "*.tif", "*.tiff");
		fileChooser.getExtensionFilters().addAll(extFilterALL, extFilterJPG, extFilterTIFF);

		File f = fileChooser.showOpenDialog(stage);
		file = f;
		if (f != null) {

			page = new Page(f);

			imv_page.setImage(page.getImage());
			imv_page.setPreserveRatio(true);
			imv_page.setFitWidth(pan_center.getWidth());
			scaleFac = page.getImage().getWidth() / imv_page.getFitWidth();
			page.setScaling((int) scaleFac);
			rootLayout.setCenter(imv_page);
		}
	}

	/**
	 * Event for clicking the "Binary"-button. Displays the binary Image.
	 */

	public void clickBinary() {
		if (page != null)
			imv_page.setImage(page.getBinImage());
	}

	/**
	 * Event for clicking the "Despeckling"-button. To remove noise from image
	 * or unnecessary contours that are probably not letters.
	 *
	 * @throws IOException
	 *             If the file is empty.
	 */
	public void clickDespeckling() throws IOException {
		Despeckling desp = new Despeckling();
		Image imgCon = desp.imdDispackling(file, img);
		imv_page.setImage(imgCon);
	}

	/**
	 * Event for clicking the "Rectangle"-button. After selecting one of the
	 * three possibilities (exact, inside, touching,), contuors are recognized
	 * within the drawn rectangle and colored.
	 *
	 */
	public void clickRectangle() {
		if (rb_exact.isSelected()) {
			System.out.println("Rect mode: EXACT");
			imv_page.setImage(page.drawContoursExactInRect(new Rect(pStartRect, pEndRect), ctrlP));

		} else if (rb_inside.isSelected()) {
			System.out.println("Rect mode: INSIDE");
			imv_page.setImage(page.drawContoursInRect(new Rect(pStartRect, pEndRect), ctrlP));

		} else if (rb_touching.isSelected()) {
			System.out.println("Rect mode: TOUCHING");
			imv_page.setImage(page.drawContoursTouchingRect(new Rect(pStartRect, pEndRect), ctrlP));
		}

	}

	/**
	 * Event for clicking the "Create"-button. Create regions of contained
	 * contours within a rectangle.
	 */
	public void clickCreateRegion() {
		System.out.println("creating region");
		if (page == null) {
			System.out.println("No Page initiated!");
			return;
		}

		page.createRegion();
		imv_page.setImage(page.drawRegions());
	}

	/**
	 * Event for clicking the "Merge"-button. Provisionally Merge Methode,
	 * taking the first 2 Regions and merging them.
	 */
	public void clickMerge() {

		LinkedList<Region> regions = page.getRegionsDeepCopy();
		if (regions.size() < 2) {
			return;
		}
		Region r1 = regions.removeFirst();
		Region r2 = regions.removeFirst();
		regions.addFirst(page.mergeRegions(r1, r2));
		page.setRegions(regions);

		imv_page.setImage(page.drawRegions());
	}

	/**
	 * Event for clicking the "Split"-button. divides regions in two half by a
	 * solid line.
	 */
	public void clickSplit() {
		imv_page.setCursor(Cursor.HAND);
		// override Mouseevents
		imv_page.setOnMousePressed((MouseEvent event) -> {
			if (splitFirst == null) {
			} else {
				splitSecond = new Point(event.getX() * scaleFac, event.getY() * scaleFac);
				page.splitRegions(splitFirst, splitSecond);
				imv_page.setImage(page.drawRegions());
				init_mouseEvents();
				splitFirst = null;
				splitSecond = null;
			}
		});

		imv_page.setOnMouseMoved((MouseEvent event) -> {
			if (splitFirst != null && splitSecond == null) {
				Point pEndLine = new Point(event.getX() * scaleFac, event.getY() * scaleFac);
				Mat inclLine = page.getMatCur().clone();

				Imgproc.line(inclLine, splitFirst, pEndLine, new Scalar(255, 0, 255), (int) scaleFac * 2);

				imv_page.setImage(page.mat2Img(inclLine));

			}
		});
	}

	/**
	 * Event for clicking the "Clear Regions"-button. deletes all regions.
	 */
	public void clickClearRegions() {
		page.clearRegions();
		imv_page.setImage(page.getCurrentImg());
	}

}
