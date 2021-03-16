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

	/*ofstream out("test.txt", ios::out | ios::binary);
	if (!out) {
		cout << "Cannot open file.\n";
		return 1;
	}*/

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
		//out.put((char)per[ch]);
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
	cout << endl;
	cout << "Count byte: " << size << endl;
	for (i = 1; count[i] != 0; i++)
	{
		cout << "Count '" << simvol[i] << "' : " << count[i] << endl;
	}
	if (count[0] != 0) {
		cout << "Count " << "'0' : " << count[0] << endl;
	}
	in.close();
}
