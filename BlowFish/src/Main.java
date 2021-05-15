import javax.crypto.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try {
            //Создаем ключ для нашего Blowfish cipher
            KeyGenerator keygenerator = KeyGenerator.getInstance("Blowfish");
            SecretKey secretkey = keygenerator.generateKey();

            //Вводим текст
            Scanner scanner = new Scanner(System.in);
            System.out.print("Введите сообщение которое хотите зашифровать>>");
            String text = scanner.nextLine();

            File original = new File("src/original.txt");
            File encrypt = new File("src/encrypt.txt");
            File decrypt = new File("src/decrypt.txt");
            FileWriter ow = new FileWriter(original);
            ow.write(text);
            ow.close();
            encrypting(original, encrypt, secretkey);
            decrypt(encrypt, decrypt, secretkey);

        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void decrypt(File encrypt, File decrypt, SecretKey secretkey) {
        try {
            // создаем Cipher для Blowfish
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, secretkey);
            FileOutputStream fos = new FileOutputStream(decrypt);
            CipherOutputStream cos = new CipherOutputStream(fos, cipher);

            FileInputStream fis = new FileInputStream(encrypt);

            byte[] bytes = new byte[64];
            int numBytes;
            while ((numBytes = fis.read(bytes)) != -1) {
                cos.write(bytes, 0, numBytes);
            }
            cos.flush();
            cos.close();
            fis.close();
            fos.close();
            System.out.println("Сообщение расшифрованно в файл src/decrypt.txt");

        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void encrypting(File original, File encrypt, SecretKey secretkey) {
        try {
            // создаем Cipher для Blowfish
            Cipher cipher = Cipher.getInstance("Blowfish");

            // Инициализируем его с нашим ключём
            cipher.init(Cipher.ENCRYPT_MODE, secretkey);
            // шифруем наше сообщение.

            FileOutputStream fos = new FileOutputStream(encrypt);
            CipherOutputStream cos = new CipherOutputStream(fos, cipher);

            FileInputStream fis = new FileInputStream(original);
//            byte[] encrypted = cipher.doFinal(br.readLine().getBytes());
//            cos.write(encrypted);

            byte[] bytes = new byte[64];
            int numBytes;
            while ((numBytes = fis.read(bytes)) != -1) {
                cos.write(bytes, 0, numBytes);
            }
            cos.flush();
            cos.close();
            fis.close();
            fos.close();
            System.out.println("Сообщение успешно закодировалось в src/encrypt.txt");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }
    }
}
