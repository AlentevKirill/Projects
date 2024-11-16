//
// Created by Кирилл on 27.05.2021.
//

#include <cstdlib>
#include <algorithm>
#include <cmath>
#include "LN.h"

using namespace std;

int* add(const int* a, int sizeA, const int* b, int sizeB) { // Складываем число по разрядам от меньшнго к большему.
    int size = max(sizeA, sizeB) + 1;
    int* c = (int*)calloc(size, sizeof(int));
    int k = 0;
    for (int i = 0; i < size; i++) {
        if (sizeA > i && sizeB > i) {
            c[i] = (a[i] + b[i] + k) % 10;
            k = (a[i] + b[i] + k) / 10;
        } else if (sizeA > i) {
            c[i] = (a[i] + k) % 10;
            k = (a[i] + k) / 10;
        } else if (sizeB > i) {
            c[i] = (b[i] + k) % 10;
            k = (b[i] + k) / 10;
        } else {
            c[i] = k;
        }
    }
    return c;
}

/*
 * 1 - первое число больше
 * 0 - оба числа равны
 * -1 - второе число больше
 */
int compare(const int* a, int sizeA, const int* b, int sizeB) { // Сравниваем по ци элементно от большего к меньшему разряду
    if (sizeA > sizeB) {
        return 1;
    }
    if (sizeB > sizeA) {
        return -1;
    }
    for (int i = sizeA - 1; i >= 0; i--) {
        if (a[i] > b[i]) {
            return 1;
        } else if (b[i] > a[i]) {
            return -1;
        }
    }
    return 0;
}

int* sub(const int* a, int sizeA, const int* b, int sizeB) { // Вычитаем поразрядно от меньшего к большему
    int *c = (int *) calloc(sizeA, sizeof(int));
    int k = 0;
    for (int i = 0; i < sizeA; i++) {
        if (sizeA > i && sizeB > i) {
            if ((a[i] + k) < b[i]) {
                c[i] = 10 + a[i] + k - b[i];
                k = -1;
            } else {
                c[i] = a[i] + k - b[i];
                k = 0;
            }
        } else if (sizeA > i) {
            c[i] = a[i] + k;
            k = 0;
        } else {
            c[i] = b[i] + k;
            k = 0;
        }
    }
    return c;
}

LN LN::operator+(const LN& b) const { // Реализация оператора + с учётом знака
    int* c;
    if (mark_ == b.mark_) {
        c = add(number_, size_, b.number_, b.size_);
        LN result(c, (max(size_, b.size_) + 1), mark_);
        return result;
    } else if (mark_ > b.mark_) {
        if (compare(number_, size_, b.number_, b.size_) >= 0) {
            c = sub(number_, size_, b.number_, b.size_);
            LN result(c, max(size_, b.size_), mark_);
            return result;
        } else {
            c = sub(b.number_, b.size_, number_, size_);
            LN result(c, max(size_, b.size_), b.mark_);
            return result;
        }
    } else {
        if (compare(b.number_, b.size_, number_, size_) >= 0) {
            c = sub(b.number_, b.size_, number_, size_);
            LN result(c, max(size_, b.size_), b.mark_);
            return result;
        } else {
            c = sub(number_, size_, b.number_, b.size_);
            LN result(c, max(size_, b.size_), mark_);
            return result;
        }
    }
}

LN LN::operator-(const LN& b) const {
    int* c;
    if (mark_ != b.mark_) {
        c = add(number_, size_, b.number_, b.size_);
        LN result(c, (max(size_, b.size_) + 1), mark_);
        return result;
    } else if (mark_ == 1 && b.mark_ == 1) {
        if (compare(number_, size_, b.number_, b.size_) >= 0) {
            c = sub(number_, size_, b.number_, b.size_);
            LN result(c, max(size_, b.size_), mark_);
            return result;
        } else {
            c = sub(b.number_, b.size_, number_, size_);
            LN result(c, max(size_, b.size_), -1);
            return result;
        }
    } else {
        if (compare(b.number_, b.size_, number_, size_) >= 0) {
            c = sub(b.number_, b.size_, number_, size_);
            LN result(c, max(size_, b.size_), 1);
            return result;
        } else {
            c = sub(number_, size_, b.number_, b.size_);
            LN result(c, max(size_, b.size_), -1);
            return result;
        }
    }
}

LN LN::operator-() const {
    return LN(number_, size_, -mark_);
}

LN LN::operator*(const LN& b) const {
    int* masNum = (int*)calloc(max(size_, b.size_) * 2 + 1, sizeof(int));
    //int* masKef = (int*)calloc(max(size_, b.size_) * 2 + 1, sizeof(int));
    int k = 0;
    for (int i = 0; i < b.size_; i++) { // берём последнюю цифру 2-го числа и перемножаем её на все цифры первого числа и так далее переходим к предпоследней цифре 2-го числа...
        for (int j = 0; j < size_; j++) {
            int temp = masNum[j + i] + number_[j] * b.number_[i] + k;
            masNum[j + i] = temp % 10;
            k = temp / 10;
        }
        masNum[size_ + i] += k;
        k = 0;
    }
    int p = 0;
    while (masNum[p] != 0) {
        masNum[p] = (masNum[p] + k) % 10;
        k = (masNum[p] + k) / 10;
        p++;
    }
    if (k != 0) {
        masNum[p] = k;
        return LN(masNum, p + 1, mark_ * b.mark_);
    }
    return LN(masNum, p, mark_ * b.mark_);
}

bool LN::operator<(const LN& b) const {
    if (compare(b.number_, b.size_, number_, size_) == 1) {
        return true;
    } else {
        return false;
    }
}

bool LN::operator<=(const LN& b) const {
    if (compare(number_, size_, b.number_, b.size_) == 1) {
        return false;
    } else {
        return true;
    }
}

bool LN::operator>(const LN& b) const {
    if (compare(number_, size_, b.number_, b.size_) == 1) {
        return true;
    } else {
        return false;
    }
}

bool LN::operator>=(const LN& b) const {
    if (compare(b.number_, b.size_, number_, size_) == 1) {
        return false;
    } else {
        return true;
    }
}

bool LN::operator==(const LN& b) const {
    if (compare(number_, size_, b.number_, b.size_) == 0) {
        return true;
    } else {
        return false;
    }
}

bool LN::operator!=(const LN& b) const {
    if (compare(number_, size_, b.number_, b.size_) == 0) {
        return false;
    } else {
        return true;
    }
}

LN::operator long long() const { // оператор приведения LN к long long
    if (size_ > 19) { // Я знаю что сдесь так неработает и должно быть сравнение с огроменным числом, но я поздно понял как нормально можно сделать
        throw "This number is too large for the type long long";
    }
    long long a = 0;
    for (int i = 0; i < size_; i++) {
        a += number_[i] * (long long) pow(10, i);
    }
    return a;
}

LN::operator bool() const { // оператор приведения LN к bool (сравнение с 0)
    if (size_ == 1 && number_[0] == 0) {
        return true;
    } else {
        return false;
    }
}

LN& LN::operator=(const LN &b) { // Оператор присваивания копированием
    if (&b == this) {
        return *this;
    }
    size_ = b.size_;
    mark_ = b.mark_;
    free(number_);
    number_ = (int*) calloc(size_, sizeof(int));
    if (number_ == nullptr) {
        throw "Please free up memory";
    }
    for (int i = 0; i < size_; i++) {
        number_[i] = b.number_[i];
    }
    return *this;
}

/*LN LN::operator ""_ln(const char *) {

    return LN();
}*/



