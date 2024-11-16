#pragma comment(linker, "/STACK:1000000000")
#include <iostream>
#include <fstream>
#include <cstdlib>
#include <string>
#include <ctime>
#include <omp.h>

int main() {

    using namespace std;

    int OMP_NUM_THREADS;
    cin >> OMP_NUM_THREADS;
    omp_set_dynamic(0);
    omp_set_num_threads(OMP_NUM_THREADS);
    string nameFile;
    cin >> nameFile;
    ifstream inf(nameFile);
    if (!inf) {
        cerr << "Uh oh, inputMatrix.txt could not be opened for reading!" << endl;
        exit(1);
    }
    string strInput;
    inf >> strInput;
    int n = stoi(strInput);
    double matrixx[n][n];
    double saveM[n][n];
    int l = 0;
    int r = 0;
    while (inf) {
        inf >> strInput;
        matrixx[l][r] = stod(strInput);
        saveM[l][r] = matrixx[l][r];
        r++;
        if (r == n) {
            l++;
            r = 0;
            if (l == n) {
                break;
            }
        }
    }
    unsigned long start_time = clock();
    int count = 0;
    double determinant = 1;
    double eps = 0.000001;
    double constant[n];
    for (int j = 0; j < n - 1; j++) {
        if (abs(matrixx[j][j]) < eps) {
            int f = j + 1;
            while (f < n) {
                if (abs(matrixx[f][j]) >= eps) {
                    break;
                }
                f++;
            }
            if (f != n) {
                for (int i = j; i < n; i++) {
                    double save;
                    save = matrixx[j][i];
                    matrixx[j][i] = matrixx[f][i];
                    matrixx[f][i] = save;
                }
                count++;
            } else {
                determinant = 0;
                break;
            }
        }
        int i;
        int k;
        #pragma omp parallel for num_threads(OMP_NUM_THREADS) private(i, k) shared (j)
        for (i = j + 1; i < n; i++) {
            if (abs(matrixx[i][j]) < eps) {
                continue;
            }
            constant[i] = -(matrixx[i][j] / matrixx[j][j]);
            matrixx[i][j] = 0;
            for (k = j + 1; k < n; k++) {
                matrixx[i][k] += (constant[i] * matrixx[j][k]);
            }
        }
    }
    int i;
    #pragma omp parallel for num_threads(OMP_NUM_THREADS) reduction(* : determinant) private (i)
    for (i = 0; i < n; i++) {
        determinant *= matrixx[i][i];
    }
    unsigned long end_time = clock();
    unsigned long search_time = end_time - start_time;
    cout << "\nTime (";
    printf("%i", OMP_NUM_THREADS);
    cout << " thread(s)): ";
    printf("%f", search_time);
    cout << " ms\n";
    determinant = count % 2 == 0 ? determinant : -determinant;
    cout << "Determinant: ";
    printf("%g\n", determinant);

    for (i = 0; i < n; i++) {
        for (int j = 0; j < n; j ++) {
            matrixx[i][j] = saveM[i][j];
        }
    }
    unsigned long start_time1 = clock();
    count = 0;
    double determinant1 = 1;
    for (int j = 0; j < n - 1; j++) {
        if (abs(matrixx[j][j]) < eps) {
            int f = j + 1;
            while (f < n) {
                if (abs(matrixx[f][j]) >= eps) {
                    break;
                }
                f++;
            }
            if (f != n) {
                for (i = j; i < n; i++) {
                    double save;
                    save = matrixx[j][i];
                    matrixx[j][i] = matrixx[f][i];
                    matrixx[f][i] = save;
                }
                count++;
            } else {
                determinant1 = 0;
                break;
            }
        }
        for (i = j + 1; i < n; i++) {
            if (abs(matrixx[i][j]) < eps) {
                continue;
            }
            constant[i] = -(matrixx[i][j] / matrixx[j][j]);
            matrixx[i][j] = 0;
            for (int k = j + 1; k < n; k++) {
                matrixx[i][k] += (constant[i] * matrixx[j][k]);
            }
        }
    }
    for (i = 0; i < n; i++) {
        determinant1 *= matrixx[i][i];
    }
    unsigned long end_time1 = clock();
    unsigned long search_time1 = end_time1 - start_time1;
    cout << "\nTime (";
    printf("%i", 0);
    cout << " thread(s)): ";
    printf("%f", search_time1);
    cout << " ms\n";
    determinant1 = count % 2 == 0 ? determinant1 : -determinant1;
    cout << "Determinant: ";
    printf("%g\n", determinant1);
    inf.close();
}