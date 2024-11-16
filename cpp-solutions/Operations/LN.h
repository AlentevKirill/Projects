//
// Created by Кирилл on 27.05.2021.
//

#include <cstdio>
#include <cstring>

#ifndef UNTITLED_LN_H
#define UNTITLED_LN_H // защита от повторного включения (многократное подключение эквивалентно однократному и не создаёт ошибки

class LN {

public: // Изначально все поля и методы класса приватные, поэтому приходится писать модификатор public
    int mark_; // Поля класса
    int* number_;
    int size_;

    LN() { // Конструктор по умолчанию
        this->mark_ = 1;
        this->number_ = nullptr;
        this->size_ = 0;
    }

    LN(const LN &ln) { // Конструктор копирование
        mark_ = ln.mark_;
        size_ = ln.size_;
        number_ = (int*) calloc(size_, sizeof(int));
        if (number_ == nullptr) {
            throw "Please free up memory"; // Бросаем исключение если не хватило памяти
        }
        for (int i = 0; i < size_; i++) { // Копируем в новый LN
            number_[i] = ln.number_[i];
        }
    }

    LN(LN&& x)  noexcept { // Конструктор перемещение
        number_ = x.number_;
        size_ = x.size_;
        mark_ = x.mark_;
        x.size_ = 0; // Перемещаем, зануляя предыдущий
        x.mark_ = 1;
        x.number_ = nullptr;
    }

    ~LN() { // Деструктор, выполняется всегда при завершении работы с Классом
        free(number_);
    }

    LN(int *number, int size, int mark) { // Обычный конструктор
        this->size_ = size;
        this->number_ = number;
        this->mark_ = mark;
    }

    explicit LN(long long x = 0) { // Конструктор long long -> LN
        number_ = (int*) calloc(20, sizeof(int));
        if (number_ == nullptr) {
            throw "Please free up memory";
        }
        int i = 0;
        if (x < 0) {
            mark_ = -1;
        } else {
            mark_ = 1;
        }
        while (x != 0) {
            number_[i] = x % 10;
            x = x / 10;
            i++;
        }
        size_ = i;
    }

    explicit LN(const char* str) { // Конструктор const char* -> LN
        int k = 0;
        if (str[0] == '-') {
            k = 1;
            mark_ = -1;
        } else {
            mark_ = 0;
        }
        int size = strlen(str);
        size_ = size - k;
        number_ = (int*) calloc(size - k, sizeof(int));
        if (number_ == nullptr) {
            throw "Please, free up memory";
        }
        for (int i = k; i < size; i++) {
            number_[size - i - 1] = str[i] - 48;
        }
    }

    /*explicit LN(std::string str) {
        int k = 0;
        if (str[0] == '-') {
            k = 1;
            mark_ = -1;
        } else {
            mark_ = 0;
        }
        int size = strlen(str);
        size_ = size - k;
        int* number_ = (int*) calloc(size - k, sizeof(int));
        if (number_ == nullptr) {
            throw "Please, free up memory";
        }
        for (int i = k; i < size; i++) {
            number_[size - i - 1] = str[i] - 48;
        }
    }*/

    LN operator+(const LN& b) const; // Перегрузка оператора, при этом первый параметр оператора + передаётся в качестве this, при этом оба слагаемых неизменяемые
    LN operator-(const LN& b) const;
    LN operator*(const LN& b) const;
//    LN operator/(LN b) const;
 //   LN operator%(LN b) const;
 //   LN operator~() const;
    LN operator-() const; // Унарный минус
    bool operator<(const LN& b) const;
    bool operator>(const LN& b) const;
    bool operator==(const LN& b) const;
    bool operator<=(const LN& b) const;
    bool operator>=(const LN& b) const;
    bool operator!=(const LN& b) const;
    LN& operator=(const LN &b); // Оператор присваивания, который возвращает то значение, которое присаивалось, чтобы были возможны такие конструкции x=y=z
    explicit operator long long() const; // оператор приведения LN к long long
    explicit operator bool() const; // оператор приведения LN к bool где проверяется 0 это или нет
    //LN operator""_ln(const char*);
};

#endif //UNTITLED_LN_H