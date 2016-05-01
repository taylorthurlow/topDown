package tthurlow.topdown;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	public BufferedImage load(String path) {
		try {
			BufferedImage temp = ImageIO.read(getClass().getResource(path));
			return temp;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
