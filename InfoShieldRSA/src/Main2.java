import java.io.*;
import java.security.spec.EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int p, q, n, m, d = 0, e = 0;

        String message = "";
        Scanner scan = new Scanner(System.in);
        //Ввод чисел с клавиатуры через сканер.
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
     //Ищем случайное простое число d
     while(!simpleNum(d)) {
         d = (int) (1 + Math.random() * 100);
     }
       /* d = m - 1;
        for(int i = 2; i <= m; i++) {
           if (m % i == 0 && d % i == 0) {
                d--;
               i = 1;
           }
          }*/

     System.out.println("D = " + d);
     //Ищем e;
        while(!((e * d)%(m) == 1)){
            e = (int) (Math.random() * 1000);
        }
        System.out.println("E = " + e);
        System.out.println("E = " + e + "; and N =" + n + "; open key!");
        System.out.println("D = " + d + "; and N =" + n + "; close key!");
        while (message.length() == 0){
            System.out.println("Введите сообщение на русском>>");
            message = scan.nextLine();
        }
        scan.close();
        //while (message.strip().length() == 0);
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
        Encrypt(message, e, n);
        System.out.println("Сообщение успешно зашифровано!");

        System.out.println("Расшифровываем сообщение...");
        Decrypt(d, n);
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
    public static void Encrypt(String mess, int e, int n){

            char[] word = new char[] {
                    'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И',
                    'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т',
                    'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ь', 'Ы', 'Ъ',
                    'Э', 'Ю', 'Я', ' ', '0', '1', '2', '3', '4', '5',
                    '6', '7', '8', '9'
            };
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
    public static void Decrypt( int d, int n){

        char[] word = new char[] {
                'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И',
                'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т',
                'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ь', 'Ы', 'Ъ',
                'Э', 'Ю', 'Я', ' ', '0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9'
        };
        try {
            File enc = new File("src/encrypt.txt");
            FileReader encRead = new FileReader(enc);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader reader = new BufferedReader(encRead);
            File dec = new File("src/decrypt.txt");
            FileWriter decWrite = new FileWriter(dec);
            String message = "";
            String decrypt = "";
            int index = 0;
            String encMess = String.valueOf(reader.readLine());
            System.out.println("message :" + encMess);
            System.out.println(message);
            int[] messageNumb = Arrays.stream(encMess.split(" ")).mapToInt(Integer::parseInt).toArray();
            for (int i : messageNumb) {
                System.out.println(i);
                index = (i ^ d) % n;
                decWrite.append(word[index]);
                decWrite.append(" ");
                //decrypt = decrypt + String.valueOf(word[index]) + " ";
            }
            reader.close();
            decWrite.close();
            encRead.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
