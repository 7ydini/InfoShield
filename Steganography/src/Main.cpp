#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include <vector>
#include <stdint.h>
#include <windows.h>

using namespace std;
BITMAPFILEHEADER BMP_File_header; // тип, размер и представление данных
BITMAPINFOHEADER BMP_Info_header; // размер и цветовой формат растрового изображения
RGBQUAD pixel_color; // цвет пикселя

void hide_byte_into_pixel(RGBQUAD* pixel_color, uint8_t hide_byte) // шифрование байта информации в пикселе
{
	pixel_color->rgbBlue &= (0xFC);
	pixel_color->rgbBlue |= (hide_byte >> 6) & 0x3;
	pixel_color->rgbGreen &= (0xFC);
	pixel_color->rgbGreen |= (hide_byte >> 4) & 0x3;
	pixel_color->rgbRed &= (0xFC);
	pixel_color->rgbRed |= (hide_byte >> 2) & 0x3;
	pixel_color->rgbReserved &= (0xFC);
	pixel_color->rgbReserved |= (hide_byte) & 0x3;
}

uint8_t unhide_byte_from_pixel(RGBQUAD pixel_color) { // дешифрование байта информации в пикселе

	uint8_t inf_byte;
	inf_byte = pixel_color.rgbBlue & 0x3;
	inf_byte = inf_byte << 2;
	inf_byte |= pixel_color.rgbGreen & 0x3;
	inf_byte = inf_byte << 2;
	inf_byte |= pixel_color.rgbRed & 0x3;
	inf_byte = inf_byte << 2;
	inf_byte |= pixel_color.rgbReserved & 0x3;
	return inf_byte;
}

int main() {
	while (true)
	{
		char enc_dec_flag; // флаг выбора шифрования/дешифрования
		string file_name; // название BMP файла для дешифрования
		cout << "Encrypting/Decrypting(e/d): ";
		fflush(stdin); // Сброс cin
		cin >> enc_dec_flag;
		if (enc_dec_flag == 'd') {
			cout << "BMP file name: ";
			fflush(stdin);
			cin >> file_name;
			FILE* bmp_file = fopen((file_name + ".bmp").c_str(), "rb"); // открытие bmp файла для чтения
			fread(&BMP_File_header, sizeof(BMP_File_header), 1, bmp_file); // Чтение | Без этого
			fread(&BMP_Info_header, sizeof(BMP_Info_header), 1, bmp_file); // заголовков файла | не работает
			//vector<uint8_t> decrypted_text; // текст из дешифрованного файла
			FILE* text = fopen("decode.txt", "wb");
			while (true) {
				fread(&pixel_color, sizeof(pixel_color), 1, bmp_file); // считывание цвета пикселя
				uint8_t decr_inf_byte = unhide_byte_from_pixel(pixel_color); // дешифрованный байт информации
				if (decr_inf_byte == 0xFF) break; // конец файла
				fwrite(&decr_inf_byte, 1, 1, text);
			}
			cout << endl;
			fclose(bmp_file); // закрытие bmp файла
			fclose(text); // закрытие bmp файла
		}
		else if (enc_dec_flag == 'e') {
			cout << "BMP file name: ";
			fflush(stdin);
			cin >> file_name;
			string text_for_encrypting;
			cout << "Text: ";
			cin >> text_for_encrypting;
			FILE* file_name_read = fopen((file_name + ".bmp").c_str(), "rb");
			fread(&BMP_File_header, sizeof(BMP_File_header), 1, file_name_read);
			fread(&BMP_Info_header, sizeof(BMP_Info_header), 1, file_name_read);
			int size_of_image = BMP_Info_header.biWidth * BMP_Info_header.biHeight; // размер изображения в пикселях
			vector<RGBQUAD> pixel_color_array; // массив цветов всех пикселей
			for (int i = 0; i < size_of_image; i++) { // заполнение массива
				fread(&pixel_color, sizeof(pixel_color), 1, file_name_read);
				pixel_color_array.insert(pixel_color_array.end(), pixel_color);
			}
			fclose(file_name_read);
			FILE* file_name_write = fopen((file_name + ".bmp").c_str(), "wb");
			fwrite(&BMP_File_header, sizeof(BMP_File_header), 1, file_name_write);
			fwrite(&BMP_Info_header, sizeof(BMP_Info_header), 1, file_name_write);
			int counter_of_text = 0;
			for (int i = 0; i < size_of_image; i++) {
				if (counter_of_text >= text_for_encrypting.size()) hide_byte_into_pixel(&pixel_color_array[i], 0x00);
				else hide_byte_into_pixel(&pixel_color_array[i], text_for_encrypting.at(counter_of_text));
				counter_of_text++;
				fwrite(&pixel_color_array[i], sizeof(pixel_color_array[i]), 1, file_name_write);
			}
			fclose(file_name_write);
		}
	}
	return 0;
}
