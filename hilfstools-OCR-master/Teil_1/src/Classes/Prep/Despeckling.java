package Classes.Prep;

import java.awt.image.BufferedImage;

import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;
import org.opencv.imgcodecs.*;

import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * class to remove noise from image or unnecessary contours that are probably
 * not letters.
 *
 */
public class Despeckling {

	static BufferedImage test;
	BufferedImage bufimg;
	static File file;

	/**
	 * provides a picture without noise.
	 *
	 * @param file
	 *            input file contains the image.
	 * @param output
	 *            bufferedImage
	 * @return new image without noise.
	 * @throws IOException
	 *             if the input file is empty.
	 */
	public Image imdDispackling(File file, BufferedImage output) throws IOException {
		System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
		Mat blurredImage = new Mat();
		Mat hsvImage = new Mat();
		Mat mask = new Mat();
		Mat morphOutput = new Mat();
		Mat frame = new Mat();
		Mat frame2 = new Mat();
		Despeckling.file = file;

		test = output;
		if (output != null) {
		}

		bufimg = ImageIO.read(file);

		frame2 = Despeckling.bufferedImageToMat(bufimg);

		frame2 = Despeckling.cleanImage(frame2);

		MatOfByte byteMat = new MatOfByte();
		Imgcodecs.imencode(".bmp", frame2, byteMat);
		return new Image(new ByteArrayInputStream(byteMat.toArray()));

	}

	/**
	 * remove noise from image or unnecessary contours that are probably not
	 * letters.
	 *
	 * @param srcImage
	 *            Mat with color values.
	 * @return new mat without noise.
	 */
	public static Mat cleanImage(Mat srcImage) {
		Mat dest = new Mat(srcImage.rows(), srcImage.cols(), CvType.CV_8UC1);
		Imgproc.cvtColor(srcImage, dest, Imgproc.COLOR_BGR2GRAY);
		Imgproc.GaussianBlur(dest, dest, new Size(7, 7), 0, 0);
		Imgproc.threshold(dest, dest, 120, 255, Imgproc.THRESH_BINARY);
		Mat morphingMatrix = Mat.ones(3, 3, CvType.CV_8UC1);
		Imgproc.morphologyEx(dest, dest, Imgproc.MORPH_OPEN, morphingMatrix);
		Photo.fastNlMeansDenoising(dest, dest);
		return dest;
	}

	/**
	 * Convert bufferedImage to matrix.
	 *
	 * @param bi
	 *            bufferedImage with color values.
	 * @return matrix.
	 */
	public static Mat bufferedImageToMat(BufferedImage bi) {
		byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		mat.put(0, 0, data);
		return mat;
	}

	/**
	 * Convert matrix to mufferedImage.
	 *
	 * @param in
	 *            matrix of the image with gray values.
	 * @return bufferedImage.
	 */
	public static BufferedImage mat2Img(Mat in) {
		byte[] data1 = new byte[in.rows() * in.cols() * (int) (in.elemSize())];
		in.get(0, 0, data1);
		BufferedImage image1 = new BufferedImage(in.cols(), in.rows(), BufferedImage.TYPE_BYTE_GRAY);
		image1.getRaster().setDataElements(0, 0, in.cols(), in.rows(), data1);
		return image1;
	}

}
