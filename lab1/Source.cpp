#include <iostream>
#include <fstream>

using namespace std; 

int main(int argc, char* argv[]) {
	cout << "Input filename.\n";  
	char str[1024];  
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

	for (i = 0; i < 256; i++) per[i] = i;

	while (in) {
		in.read(&ch, 1);  
		if (in.fail()) { 
			if (!in.eof()) { 
				cout << "Input error\n"; 
				break; 
			} else break; 
		}   
	//out.put((char)per[ch]);  
	cout << ch; 
	}

	in.close(); 
	//out.close();

	getchar();  return 0;
}
