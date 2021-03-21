package folderwatch.nextfile;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;

import folderwatch.FolderWatcher;

public class NextFileImage implements NextFileContent {

	private FolderWatcher folderWatcher;
	private String name;
	private int[] histogram;

	public NextFileImage(FolderWatcher folderWatcher, String name) {
		this.folderWatcher = folderWatcher;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public File getErrorFolder() {
		return folderWatcher.getImageErrorFolder();
	}

	@Override
	public File getDoneFolder() {
		return folderWatcher.getImageDoneFolder();
	}

	@Override
	public NextFile process() {
		try {
			BufferedImage image = ImageIO.read(new File(folderWatcher.getImageErrorFolder(), name));

			HistogramGenerator histogramGenerator = new HistogramGenerator(image);
			histogram = histogramGenerator.getHistogram();
			histogramGenerator.normalize(histogram);

			BufferedImage outputImage = new BufferedImage(256, 100, BufferedImage.TYPE_BYTE_GRAY);
			histogramGenerator.convert(histogram, outputImage);
			String outputName = name.substring(0, name.lastIndexOf(".")) + "-hist.bmp";

			ImageIO.write(outputImage, "bmp", new File(folderWatcher.getImageDoneFolder(), outputName));

			folderWatcher.moveFile(this.getErrorFolder(), this.getDoneFolder(), name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return folderWatcher.getNextFile();
	}

	public class HistogramGenerator {
		private final int HISTOGRAM_WIDTH = 256;
		private final int HISTOGRAM_HEIGHT = 100;
		private final int IMAGE_WIDTH;
		private final int IMAGE_HEIGHT;
		private final BufferedImage IMAGE;

		HistogramGenerator(BufferedImage image) {
			IMAGE = image;
			IMAGE_WIDTH = image.getWidth();
			IMAGE_HEIGHT = image.getHeight();
		}

		public int[] getHistogram() {
			int[] histogram = new int[HISTOGRAM_WIDTH];

			for (int y = 0; y < IMAGE_HEIGHT; y++) {
				for (int x = 0; x < IMAGE_WIDTH; x++) {
					histogram[getGrayscale(x, y)]++;
				}
			}
			return histogram;
		}

		private int getGrayscale(int x, int y) {
			int rgb = IMAGE.getRGB(x, y);
			int a = rgb >> 24 & 0xFF;
			int r = rgb >> 16 & 0xFF;
			int g = rgb >> 8 & 0xFF;
			int b = rgb & 0xFF;

			double grayscale = 0.11 * r + 0.56 * g + 0.33 * b;

			return (int) (grayscale * a / 0xFF);
		}

		public void normalize(int[] histogram) {
			int max = Arrays.stream(histogram).max().getAsInt();

			for (int i = 0; i < histogram.length; i++) {
				histogram[i] = histogram[i] * HISTOGRAM_HEIGHT / max;
			}
		}

		public void convert(int[] histogram, BufferedImage outputImage) {
			WritableRaster raster = outputImage.getRaster();

			int[] samples = new int[HISTOGRAM_WIDTH * HISTOGRAM_HEIGHT];

			for (int w = 0; w < HISTOGRAM_WIDTH; w++) {
				int value = histogram[w];
				for (int h = 0; h < HISTOGRAM_HEIGHT; h++) {
					samples[h * HISTOGRAM_WIDTH + w] = HISTOGRAM_HEIGHT - h < value ? 0 : 0xFF;
				}
			}
			raster.setSamples(0, 0, HISTOGRAM_WIDTH, HISTOGRAM_HEIGHT, 0, samples);
		}
	}

}
