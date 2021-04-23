import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException
    {
        File srcFile = new File("2.bmp");
        File dstFile = new File("2enc.bmp");

        String fileExt = decrypt(dstFile.getName());
        BufferedImage srcImage = ImageIO.read(srcFile);

        final int w = srcImage.getWidth();
        final int h = srcImage.getHeight();
        int[] rgb = srcImage.getRGB(0, 0, w, h, null, 0, w);

        System.out.println("Encrypting...");
        encrypt(rgb);

        BufferedImage dstImage = new BufferedImage(w, h, srcImage.getType());

        dstImage.setRGB(0, 0, w, h, rgb, 0, w);
        ImageIO.write(dstImage, fileExt, dstFile);
    }

    public static void encrypt(int[] rgb)
    {
        Random rnd = new Random();

        for(int i=0; i < rgb.length; i++)
        {
            rgb[i] = rgb[i] ^ rnd.nextInt();
        }
    }

    public static String decrypt(String filename)
    {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1)
            return null;
        return filename.substring(dotIndex+1);
    }
}
