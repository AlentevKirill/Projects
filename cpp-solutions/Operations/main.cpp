//
// Created by Кирилл
//
#include <iostream>
#include <cstring>
#include <stack>
#include "LN.h"

using namespace std;

int* conv(const char* str, int size) { // Делает из строкового представления числа его представление в виде массива интов в обратном порядке
    int k = 0;
    if (str[0] == '-') { // учёт минуса в начале отрицательных констант
        k = 1;
    }
    int* res = (int*) calloc(size - k, sizeof(int));
    if (res == nullptr) {
        throw "Please, free up memory"; // бросаю исключение в виде строки, если не удалось выделить память
    }
    for (int i = k; i < size; i++) {
        res[size - i - 1] = str[i] - 48;
    }
    return res;
}

char* unConv(const int* number, int size, int mark) { // Делает из числа типа LN строковую запись этого числа
    int j = size - 1;
    while (true) { // Пропускаем все начальные нули
        if (number[j] == 0) {
            j--;
        } else {
            break;
        }
    }
    int k = 0;
    if (mark == -1) {
        k = 1;
    }
    char* res = (char*) calloc(j + 2 + k, sizeof(char));
    if (k == 1) {
        res[0] = '-';
    }
    if (res == nullptr) {
        throw "Please, free up memory"; // Бросаем исключение, если не хватило памяти
    }
    for (int i = 0; i < j + 1; i++) {
        res[j - i + k] = (char) (((int) '0') + number[i]);
    }
    res[j + 1 + k] = '\0'; // Ставим в конец "Ноль терминатор", чтобы программа видела где кончается строка
    return res;
}

int main(int argc, char* argv[]) {
    if (argc != 3) {
        puts("Please, enter three arguments\n");
        exit(1); // Вылетаем из программы, если не введено положенное количество аргументов (exit закрывает все процеесы и возвращет код 1)
    }
    FILE *fp = fopen(argv[1], "rt"); // Открытие файла только на чтение
    if (fp == nullptr) {
        puts("Uh oh, file could not be opened for reading!\n");
        exit(1); // Если файл для чтения не открылся, вылетаем
    }
    FILE *fw = fopen(argv[2], "w"); // Открытие файла для записи, при этом если такой файл уже существует, то он перезаписывается, а если нет, то создаётся новый
    if (fw == nullptr) {
        puts("Uh oh, file could not be opened for writing!\n");
        exit(1); // Если файл для записи не открылся, вылетаем
    }
    try {
        stack<LN> expr; // Создание стека с элементами LN
        int size = 1000000000;
        char *string = (char *) calloc(size, sizeof(char)); // Выделение памяти под число в строковом представлении
        if (string == nullptr) {
            throw "Please, free up memory"; // бросаем исключение если памяти на строчку не хватило
        }
        while (true) {
            int check = fscanf(fp, "%s\n", string); // Читаем построчно из файла, при этом check примет -1 если не удалось ничего считать
            size = strlen(string);
            if (check == -1) {
                break;
            }
            if (isdigit(string[0]) || (size > 1 && (string[0] == '-' && isdigit(string[1])))) { // Проверяем, что считали число
                if (string[0] == '-') {
                    expr.push(LN(conv(string, size), size - 1, -1));
                } else {
                    expr.push(LN(conv(string, size), size, 1));
                }
            } else if (string[0] == '+') {
                LN b = expr.top(); // top - возвращает верхний элемент, pop - удаляет верхний элемент, push - добавляет элемент в вершину стека
                expr.pop();
                LN a = expr.top();
                expr.pop();
                expr.push(a + b);
            } else if (string[0] == '-') {
                LN b = expr.top();
                expr.pop();
                LN a = expr.top();
                expr.pop();
                expr.push(a - b);
            } else if (string[0] == '_') {
                LN a = expr.top();
                expr.pop();
                expr.push(-a);
            } else if (string[0] == '*') {
                LN b = expr.top();
                expr.pop();
                LN a = expr.top();
                expr.pop();
                expr.push(a * b);
            } else if (string[0] == '<' && string[1] != '=') {
                LN b = expr.top();
                expr.pop();
                LN a = expr.top();
                expr.pop();
                int *y = (int *) calloc(1, sizeof(int));
                y[0] = a < b;
                expr.push(LN(y, 1, 1)); // Создание LN числа для констант 0, 1
            } else if (string[0] == '>' && string[1] != '=') {
                LN b = expr.top();
                expr.pop();
                LN a = expr.top();
                expr.pop();
                int *y = (int *) calloc(1, sizeof(int));
                y[0] = a > b;
                expr.push(LN(y, 1, 1));
            } else if (string[0] == '>' && string[1] == '=') {
                LN b = expr.top();
                expr.pop();
                LN a = expr.top();
                expr.pop();
                int *y = (int *) calloc(1, sizeof(int));
                y[0] = a >= b;
                expr.push(LN(y, 1, 1));
            } else if (string[0] == '<' && string[1] == '=') {
                LN b = expr.top();
                expr.pop();
                LN a = expr.top();
                expr.pop();
                int *y = (int *) calloc(1, sizeof(int));
                y[0] = a <= b;
                expr.push(LN(y, 1, 1));
            } else if (string[0] == '=' && string[1] == '=') {
                LN b = expr.top();
                expr.pop();
                LN a = expr.top();
                expr.pop();
                int *y = (int *) calloc(1, sizeof(int));
                y[0] = a == b;
                expr.push(LN(y, 1, 1));
            } else if (string[0] == '!' && string[1] == '=') {
                LN b = expr.top();
                expr.pop();
                LN a = expr.top();
                expr.pop();
                int *y = (int *) calloc(1, sizeof(int));
                y[0] = a != b;
                expr.push(LN(y, 1, 1));
            } else if (string[0] == '/') {
            LN a = expr.top();
            expr.pop();
            LN b = expr.top();
            expr.pop();
            expr.push(a / b);
        }
        }
        while (!expr.empty()) { // Вывод элементов стека на экран
            LN result = expr.top();
            expr.pop();
            fprintf(fw, "%s\n", unConv(result.number_, result.size_, result.mark_));
        }
    } catch (char *str) { // Ловим все брошенные исключения и выводим на экран сообщение об ошибке.
        printf("%s", str);
    }
    int check = fclose(fp); // Закрываем файлы, вылетая из программы с сообщением, если что-то не закрывалось. Если функция завершилась успехом то возвращает 0 значение
    if (check != 0) {
        puts("Error closing the input file\n");
        exit(1);
    }
    check = fclose(fw);
    if (check != 0) {
        puts("Error closing the output file\n");
        exit(1);
    }
}
