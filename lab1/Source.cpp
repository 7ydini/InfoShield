#include <iostream>
#include <fstream>

using namespace std;


int main(int argc, char* argv[]) {
	cout << "Input filename.\n";
	char simvol[128];
	int count[128];
	char str[1024];
	int size = 0;
	cin >> str;
	ifstream in(str, ios::in | ios::binary);
	if (!in) {
		cout << "Cannot open file.\n";
		return 1;
	}

	char ch;  int per[256];  int i;
	for (i = 0; i < 128; i++) {
		count[i] = 0;
		simvol[i] = '0';
	}
	for (i = 0; i < 256; i++) per[i] = i;

	while (in) {
		in.read(&ch, 1);

		if (in.fail()) {
			if (!in.eof()) {
				cout << "Input error\n";
				break;
			}
			else break;
		}
		//подсчёт частоты каждого байта и вывод содержимого файла
		size++;
		bool semaphore = false;
		int k = 1;
		
		while (!semaphore) {
			if (ch == '0') {
				count[0]++;
				semaphore;
			}
			if (simvol[k] == '0') {
				simvol[k] = ch;
				semaphore = true;
			}
			if (simvol[k] == ch){
				count[k]++;
				semaphore = true;
			}
			k++;
		}
		cout << ch;
	}

	char* arr = new char[size];
	cout << endl;
	cout << "Count byte: " << size << endl;
	for (i = 1; count[i] != 0; i++)
	{
		cout << "Count '" << simvol[i] << "' : " << count[i] << endl;
	}
	if (count[0] != 0) {
		cout << "Count " << "'0' : " << count[0] << endl;
	}
	// Открываем файл с ключом-подстановкой
	ifstream in2("key.txt", ios::in | ios::binary);
	int key[5];
	in2 >> key[0]; in2 >> key[1]; in2 >> key[2]; in2 >> key[3]; in2 >> key[4];
	in2.close();
	// Делаем массив с содержимым файла кратным 5
	in.seekg(0, ios::beg);
	int flag = 0;
	char* new_arr;
	if (size % 5 != 0) {
		flag = 1;
		new_arr = new char[size + (5 - size % 5)];
		in.read(new_arr, size);
		for (int i = 0; i < (5 - size % 5); i++) { 
			new_arr[size + (5 - size % 5) - 1 - i] = 'z'; 
		}
		for (int i = 0; i < size + (5 - size % 5); ++i) {
			cout << new_arr[i];
		}
		size = size + (5 - size % 5);
	}
	else {
		new_arr = new char[size];
		in.read(new_arr, size);
		for (int i = 0; i < size ; i++) {
			new_arr[size - 1 - i] = 'z';
		}
		for (int i = 0; i < size; ++i) {
			cout << new_arr[i];
		}
				
	}
	in.close();
	// Шифрование/Расшифровка и вывод результата
	int mode;
	do {
		cout << "Enter mode:\n1 >> encrypting\n2 >> decrypting\n3 >> Exit\n";
		cin >> mode;

		char* scfr_arr = new char[size];
		int b = 0, c = 0;
		switch (mode) {
		case 1:
			for (int i = 0; i < size; i++) {
				if (!flag) scfr_arr[((size / 5) * (key[i % 5] - 1)) + c] = arr[i];

				else scfr_arr[((size / 5) * (key[i % 5] - 1)) + c] = new_arr[i];
				b++;
				if (b == 5) {
					b = 0;
					c++;
				}
			}
			for (int i = 0; i < size; i++) {
				cout << scfr_arr[i];
			}
			break;
		case 2:
			for (int i = 0; i < size; i++) {
				if (!flag) {
					scfr_arr[i] = arr[((size / 5) * (key[i % 5] - 1)) + c];
				}
				else {
					scfr_arr[i] = new_arr[((size / 5) * (key[i % 5] - 1)) + c];
				}
				b++;
				if (b == 5) { b = 0; c++; }
			}
			for (int i = 0; i < size; i++) {
				cout << scfr_arr[i];
			}
			break;
		case(3):
			cout << "BB" << endl;
			break;
		}
	} while (mode != 3);
}