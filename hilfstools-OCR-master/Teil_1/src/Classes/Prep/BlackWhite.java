package Classes.Prep;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import javafx.scene.image.Image;

/**
 * Class for binarizing the image with otsu algorithm.
 */

public class BlackWhite {
	Image image;
	File outp;
	BufferedImage bimgBW;
	Mat dest;

	/**
	 * Convert color image to gray and from gray to binary.
	 *
	 * @param output
	 *            input file contains the image.
	 * @return binary image.
	 */
	public Image convertToBlackWhite(File output) {
		System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
		try {
			bimgBW = ImageIO.read(output);
			Mat srcImage = BlackWhite.bufferedImageToMat(bimgBW);
			dest = new Mat(srcImage.rows(), srcImage.cols(), CvType.CV_8UC1);
			Imgproc.threshold(srcImage, dest, 120, 255, Imgproc.THRESH_BINARY);
			bimgBW = BlackWhite.mat2Img(dest);
			outp = new File("blackwhit.jpg");
			ImageIO.write(bimgBW, "jpg", outp);
			image = new Image("file:" + outp.getAbsolutePath());

		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
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
