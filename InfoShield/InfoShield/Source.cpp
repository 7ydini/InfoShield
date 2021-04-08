#include <iostream>
#include <fstream>
#pragma warning (disable : 4703)
using namespace std;


int main(int argc, char* argv[]) {
	cout << "Input filename.\n";
	char simvol[256];
	int count[256];
	char str[1024];
	int size = 0;
	cin >> str;
	ifstream in(str, ios::in | ios::binary);
	if (!in) {
		cout << "Cannot open file.\n";
		return 1;
	}

	char ch;  int per[256];  int i;
	for (i = 0; i < 256; i++) {
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
		//������������ ������� ������
		size++;
		bool semaphore = false;
		int k = 1;

		while (!semaphore) {
			if (ch == '0') {
				count[0]++;
				semaphore = true;
			}
			else if (simvol[k] == '0') {
				simvol[k] = ch;
				semaphore = true;
			}
			if (simvol[k] == ch) {
				count[k]++;
				semaphore = true;
			}

			k++;
		}
		cout << ch;
	}
	char* doc = new char[size];
	ifstream in3("is.doc", ios::in | ios::binary);
	in3.read(doc, size);
	cout << endl;
	cout << "Count byte: " << size << endl;
	if (count[0] != 0) {
		cout << "Count " << "'0' : " << count[0] << endl;
	}
	for (i = 1; count[i] > 0; i++)
	{
		cout << "Count '" << simvol[i] << "' : " << count[i] << endl;
	}
	for (int i = 0; i < size; ++i) {
		cout << doc[i];
	}
	in.close();
	in3.close();

	size = 0;
	cout << "\n";
	ifstream in5("Message.txt", ios::in | ios::binary);
	if (!in5) {
		cout << "Cannot open file.\n";
		return 1;
	}
	while (in5) {
		in5.read(&ch, 1);
		if (in5.fail()) {
			if (!in5.eof()) {
				cout << "Input error\n";
				break;
			}
			else break;
		}
		size++;
		//cout << ch;
	}
	in5.close();
	ifstream inmes("Message.txt", ios::in | ios::binary);
	if (!inmes) {
		cout << "Cannot open file.\n";
		return 1;
	}
	char* arrMess = new char[size];
	//������� ���������
	cout << "Message: " << endl;
	for (int k = 0; k < size; k++) {
		inmes.read(&ch, 1);
		arrMess[k] = ch;
	}
	inmes.close();




	// ������ ������ � ���������� ����� ������� 5
	//in3.seekg(0, ios::beg);
	ifstream inMess("Message.txt", ios::in | ios::binary);
	int flag = 0;
	char* new_arr;
	if (size % 5 != 0) {
		flag = 1;
		new_arr = new char[size + (5 - size % 5)];
		inMess.read(new_arr, size);
		for (int i = 0; i < (5 - size % 5); i++) {
			new_arr[size + (5 - size % 5) - 1 - i] = 'z';
		}
		for (int i = 0; i < size + (5 - size % 5); ++i) {
			cout << new_arr[i];
		}
		size = size + (5 - size % 5);
	}
	

	// ��������� ���� � ������-������������
	ifstream inKey("key.txt", ios::in | ios::binary);
	int key[5];
	inKey >> key[0]; inKey >> key[1]; inKey >> key[2]; inKey >> key[3]; inKey >> key[4];
	inKey.close();
	// ����������/����������� � ����� ����������
	int mode;
	char* scfr_arr = new char[size];
	char* unScfr_arr = new char[size];
	int b = 0, c = 0;
	ofstream enc("encrypt.txt", ios::out);
	if (!enc) {
		cout << "Cannot open file.\n";
		return 1;
	}
	ofstream dec("decrypt.txt", ios::out);
	if (!dec) {
		cout << "Cannot open file.\n";
		return 1;
	}


	do {
		cout << "\nEnter mode: \n1 >> encrypting\n2 >> decrypting\n3 >> Exit.\n";
		cin >> mode;
		switch (mode) {
		case 1:
			for (int i = 0; i < size; i++) {
				if (!flag) {
					scfr_arr[((size / 5) * (key[i % 5] - 1)) + c] = arrMess[i];
				}
				else {
					scfr_arr[((size / 5) * (key[i % 5] - 1)) + c] = new_arr[i];
				}
				b++;
				if (b == 5) {
					b = 0;
					c++;
				}
			}
			for (int i = 0; i < size; i++) {
				cout << scfr_arr[i];
			}
			enc.write(scfr_arr, size);
			b = 0; 
			c = 0;
			break;

		case 2:
			for (int i = 0; i < size; i++) {
				if (!flag) {
					unScfr_arr[i] = scfr_arr[((size / 5) * (key[i % 5] - 1)) + c];
				}
				else {
					unScfr_arr[i] = scfr_arr[((size / 5) * (key[i % 5] - 1)) + c];
				}
				b++;
				if (b == 5) { b = 0; c++; }
			}
			for (int i = 0; i < size; i++) {
				cout << unScfr_arr[i];
			}
			dec.write(unScfr_arr, size);
			b = 0;
			c = 0;
			break;
		case(3):
			cout << "BB" << endl;
			break;
		}
	} while (mode != 3);
	enc.close();
	dec.close();
}