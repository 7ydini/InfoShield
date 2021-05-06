import java.io.*;
import java.util.Scanner;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferByte;

public class Steganography{

    private static final int bytesForTextLengthData = 4;
    private static final int bitsInByte = 8;

    public static void main(String[] args) {
        try {
            File encode = new File("src/Encode.txt");
            FileWriter writer = new FileWriter(encode);
            Scanner scanner = new Scanner(System.in);
            System.out.print("Введите сообщение для коддировки >> ");
            writer.write(scanner.nextLine());
            writer.close();
            scanner.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        encode("src/2.bmp", "src/Encode.txt");
        decode("src/2encoded.bmp");
    }


    // Encode

    private static void encode(String imagePath, String textPath) {
        BufferedImage image = getImage(imagePath);
        BufferedImage encodeImage = coppyImage(image);
        String text = getText(textPath);

        byte imageInBytes[] = getImageBytes(encodeImage);
        byte textInBytes[] = text.getBytes();
        byte textLengthInBytes[] = getLengthBytes(textInBytes.length);
        try {
            encodeImage(imageInBytes, textLengthInBytes,  0);
            encodeImage(imageInBytes, textInBytes, bytesForTextLengthData*bitsInByte);
        }
        catch (Exception exception) {
            System.out.println(exception);
            return;
        }

        String fileName = imagePath;
        int position = fileName.lastIndexOf(".");
        if (position > 0) {
            fileName = fileName.substring(0, position);
        }

        String finalFileName = fileName + "encoded.bmp";
        System.out.println("Сообщение зашифрованно в .bmp: " + finalFileName);
        saveImageToPath(encodeImage, new File(finalFileName),"bmp");
        return;
    }

    private static byte[] encodeImage(byte[] image, byte[] addition, int offset) {
        if (addition.length + offset > image.length) {
            throw new IllegalArgumentException("Image file is not long enough to store provided text");
        }
        for (int i=0; i<addition.length; i++) {
            int additionByte = addition[i];
            for (int bit=bitsInByte-1; bit>=0; --bit, offset++) {
                int b = (additionByte >>> bit) & 0x1;
                image[offset] = (byte)((image[offset] & 0xFE) | b);
            }
        }
        return image;
    }


    // Decode

    private static String decode(String imagePath) {
        byte[] decodedByteText;
        try {
            BufferedImage imageFromPath = getImage(imagePath);
            BufferedImage imageInUserSpace = coppyImage(imageFromPath);
            byte imageInBytes[] = getImageBytes(imageInUserSpace);
            decodedByteText = decodeImage(imageInBytes);
            String hiddenText = new String(decodedByteText);
            saveTextToPath(hiddenText, new File("src/Decode.txt"));
            System.out.println("Successfully extracted text to: " + "src/Decode.txt");
            return hiddenText;
        } catch (Exception exception) {
            System.out.println("No hidden message. Error: " + exception);
            return "";
        }
    }

    private static byte[] decodeImage(byte[] image) {
        int length = 0;
        int offset  = bytesForTextLengthData*bitsInByte;

        for (int i=0; i<offset; i++) {
            length = (length << 1) | (image[i] & 0x1);
        }

        byte[] result = new byte[length];

        for (int b=0; b<result.length; b++ ) {
            for (int i=0; i<bitsInByte; i++, offset++) {
                result[b] = (byte)((result[b] << 1) | (image[offset] & 0x1));
            }
        }
        return result;
    }
    private static void saveImageToPath(BufferedImage image, File file, String extension) {
        try {
            file.delete();
            ImageIO.write(image, extension, file);
        } catch (Exception exception) {
            System.out.println("Image file could not be saved. Error: " + exception);
        }
    }

    private static void saveTextToPath(String text, File file) {
        try {
            if (!file.exists()) {
                file.createNewFile( );
            }
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(text);
            bufferedWriter.close();
        } catch (Exception exception) {
            System.out.println("Couldn't write text to file: " + exception);
        }
    }

    private static BufferedImage getImage(String path) {
        BufferedImage image	= null;
        File file = new File(path);
        try {
            image = ImageIO.read(file);
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return image;
    }

    private static String getText(String textFile) {
        String text = "";
        try {
            Scanner scanner = new Scanner( new File(textFile) );
            text = scanner.useDelimiter("\\A").next();
            scanner.close();
        } catch (Exception exception) {
            System.out.println("Couldn't read text from file. Error: " + exception);
        }
        return text;
    }


    private static BufferedImage coppyImage(BufferedImage image) {
        BufferedImage imageInUserSpace  = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = imageInUserSpace.createGraphics();
        graphics.drawRenderedImage(image, null);
        graphics.dispose();
        return imageInUserSpace;
    }

    private static byte[] getImageBytes(BufferedImage image) {
        WritableRaster raster = image.getRaster();
        DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
        return buffer.getData();
    }

    private static byte[] getLengthBytes(int integer) {
        return ByteBuffer.allocate(bytesForTextLengthData).putInt(integer).array();
    }
}