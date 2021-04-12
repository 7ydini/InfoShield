import java.io.*;
import java.security.spec.EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int p, q, n, m, d = 0, e = 0;
        char[] word = new char[] {
                'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И',
                'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т',
                'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ь', 'Ы', 'Ъ',
                'Э', 'Ю', 'Я', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
                'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
                'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ',
                '0', '1', '2', '3', '4', '5','6', '7', '8', '9'
        };


        String message = "";
        Scanner scan = new Scanner(System.in);
        //Ввод чисел с клавиатуры через сканер.
        //System.out.print("Ручной/авто?(1/2) >>");
     do{
        System.out.println("Введите простые числа!\n");
        System.out.print("P = ");
        p = scan.nextInt();
        System.out.print("Q = ");
        q = scan.nextInt();
     }while(!simpleNum(p) || !simpleNum(q));
     n = p * q;
     System.out.println("N = " + n);
     //Вычисляем m
     m = (p - 1) * (q - 1);
     System.out.println("M = " + m);
     //Ищем случайное простое число d
//     while(!simpleNum(d)) {
//        d = (int) (2 + Math.random() * 100);
//     }
        d = m - 1;
        for(int i = 2; i <= m; i++) {
           if (m % i == 0 && d % i == 0) {
                d--;
               i = 2;
           }
          }
     System.out.println("D = " + d);
     //Ищем e;
        e = 10;
        while(!((e * d)%(m) == 1)){
            e++;
        }

        System.out.println("E = " + e);
        System.out.println("M = " + m);
        System.out.println("E = " + e + "; and N =" + n + "; open key!");
        System.out.println("D = " + d + "; and N =" + n + "; close key!");
        try {
            File key = new File("src/key.txt");
            FileWriter keyWrite = new FileWriter(key);
            String keyMess = "E = " + e + "; M = " + m + "; D = " + d + "; N = " + n;
            keyWrite.write(keyMess);
        } catch (IOException exception) {
            exception.printStackTrace();
        }


        while (message.strip().length() == 0){
            System.out.println("Введите сообщение >>");
            message = scan.nextLine();
        }
        scan.close();
        message = message.toUpperCase();//Переводим все буквы в верний регистр
        try{
            File mess = new File("src/Message.txt");
            FileWriter writeMess = new FileWriter(mess);
            writeMess.write(message);
            writeMess.close();
        }catch (FileNotFoundException exception){
            System.out.println("File not found");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        System.out.println("Шифруем сообщение...");
        Encrypt(message, e, n, word);
        System.out.println("Сообщение успешно зашифровано!");

        System.out.println("Расшифровываем сообщение...");
        Decrypt(d, n, word);
        System.out.println("Сообщение успешно Расшифровано!");
    }
    public static boolean simpleNum(int num){// Метод проверяющий число, простое ли оно.
        if(num <= 1){
            return false;
        }
        for(int i = 2; i < num; i++){
            if(num % i == 0){
                return false;
            }
        }
        return true;
    }
    public static void Encrypt(String mess, int e, int n, char[] word){
            String encMess = "";
            int index = -1;
            char[] messArr = mess.toCharArray();
        for (char ch : messArr) {
            for (int j = 0; j < word.length; j++) {
                if (word[j] == ch) {
                    index = (j) ^ e % n;
                    encMess = encMess + index+ " ";
                }
            }
        }
        try {
            File enc = new File("src/encrypt.txt");
            FileWriter fw = new FileWriter(enc);
            fw.write(encMess);
            fw.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }


    }
    public static void Decrypt( int d, int n, char[] word){


        try {
            File enc = new File("src/encrypt.txt");
            FileReader encrypt = new FileReader(enc);
            BufferedReader encRead = new BufferedReader(encrypt);
            File dec = new File("src/decrypt.txt");
            FileWriter decWrite = new FileWriter(dec);
            String encMess = "";
            String message = "";
            String decrypt = "";
            encMess = String.valueOf(encRead.readLine());
            message = encMess;
            //System.out.println(encMess);
            int[] messageNumb = Arrays.stream(message.split(" ")).mapToInt(Integer::parseInt).toArray();
            for (int i : messageNumb) {
                System.out.println(i);
                decrypt = decrypt + String.valueOf(word[(i ^ d) % n]);
            }
            decWrite.write(decrypt);
            decWrite.close();
            encRead.close();
            encrypt.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
