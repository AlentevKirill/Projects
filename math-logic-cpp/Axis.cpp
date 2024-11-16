//
// Created by Kirill on 29.09.2023.
//

#include <complex>
#include <iostream>
#include <cmath>
#include <algorithm>
#include <vector>
#include <map>

using namespace std;

vector<string> vectorOfExpression;
vector<map<string, int>> vectorOfContext;
vector<pair<map<string, int>, string>> vectorOfDiductionForm;

string line;

int pos = 0;

string dij();

string con();

string nt();

bool skip(const string& i);

string e() {
    string x = dij();
    while (skip("->")) {
        x = "(->," + x + ',' + e() + ')';
    }
    return x;
}

string parse() {
    line = line + '#';
    return e();
}

string dij() {
    string x = con();
    while (skip("|")) {
        x = "(|," + x + ',' + con() + ')';
    }
    return x;
}

string con() {
    string x = nt();
    while (skip("&")) {
        x = "(&," + x + ',' + nt() + ')';
    }
    return x;
}

string nt() {
    if (skip("(")) {
        string x = e();
        skip(")");
        return x;
    }
    if (skip("!")) {
        return "(!" + nt() + ")";
    }
    string x = "";
    while (true) {
        bool flag = false;
        if (isdigit(line[pos]) || isalpha(line[pos]) || line[pos] == '\'') {
            flag = true;
            x += line[pos];
            pos += 1;
        }
        /*if (line.substr(pos, 3) == "'") {
            flag = true;
            x += line.substr(pos, 3);
            pos += 3;
        }*/
        if (!flag) {
            break;
        }
    }
    /*while (isdigit(line[pos]) || isalpha(line[pos]) || line.substr(pos, 3) == "’") {
        x += line[pos];
        pos += 1;
    }*/
    return x;
}

bool skip(const string& i) {
    if (line.rfind(i, pos) == pos) {
        pos += i.size();
        return true;
    }
    return false;
}

int skipMember(const string& parsingLine, int startPos) {
    int count = 0;
    int i = startPos;
    while (true) {
        if (parsingLine[i] == '(') {
            count++;
        }
        if (parsingLine[i] == ')') {
            count--;
        }
        if (count == 0) {
            break;
        }
        i++;
    }
    return i;
}

bool axis1(const string& parsingLine) { // A->B->A == (->,A,(->,B,A))
    if (parsingLine.substr(0, 4) != "(->,") {
        return false;
    }
    int posL = 4;
    int i = skipMember(parsingLine, posL);
    int posR = i + 1;
    string a = parsingLine.substr(posL, posR - posL);
    if (parsingLine.substr(posR + 1, 4) != "(->,") {
        return false;
    }
    posL = posR + 5;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    string b = parsingLine.substr(posL, posR - posL);
    posL = posR + 1;
    posR = (int) parsingLine.size() - 2;
    if (parsingLine.substr(posL, posR - posL) != a) {
        return false;
    }
    if (parsingLine[posR] != ')' || parsingLine[posR + 1] != ')') {
        return false;
    }
    return true;
}

bool axis2(const string& parsingLine) { // (A->B)->(A->B->C)->(A->C) == (->,(->,A,B),(->,(->,A,(->,B,C)),(->,A,C)))
    int posL = 0;
    if (parsingLine.substr(posL, 8) != "(->,(->,") {
        return false;
    }
    posL = 8;
    int i = skipMember(parsingLine, posL);
    int posR = i + 1;
    string a = parsingLine.substr(posL, posR - posL);
    posL = posR + 1; // Пропускаем запятую между A и B в выражении (->,A,B)
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    string b = parsingLine.substr(posL, posR - posL);
    if (parsingLine.substr(posR, 10) != "),(->,(->,") {
        return false;
    }
    posL = posR + 10;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    if (a != parsingLine.substr(posL, posR - posL)) {
        return false;
    }
    if (parsingLine.substr(posR, 5) != ",(->,") {
        return false;
    }
    posL = posR + 5;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    if (b != parsingLine.substr(posL, posR - posL)) {
        return false;
    }
    posL = posR + 1;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    string c = parsingLine.substr(posL, posR - posL);
    if (parsingLine.substr(posR, 7) != ")),(->,") {
        return false;
    }
    posL = posR + 7;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    if (a != parsingLine.substr(posL, posR - posL)) {
        return false;
    }
    posL = posR + 1;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    if (c != parsingLine.substr(posL, posR - posL)) {
        return false;
    }
    if (parsingLine.substr(posR, 3) != ")))") {
        return false;
    }
    return true;
}

bool axis3(const string& parsingLine) { // A->B->A&B == (->,A,(->,B,(&,A,B)))
    int posL = 0;
    if (parsingLine.substr(posL, 4) != "(->,") {
        return false;
    }
    posL = 4;
    int i = skipMember(parsingLine, posL);
    int posR = i + 1;
    string a = parsingLine.substr(posL, posR - posL);
    if (parsingLine.substr(posR, 5) != ",(->,") {
        return false;
    }
    posL = posR + 5;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    string b = parsingLine.substr(posL, posR - posL);
    if (parsingLine.substr(posR, 4) != ",(&,") {
        return false;
    }
    posL = posR + 4;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    if (a != parsingLine.substr(posL, posR - posL)) {
        return false;
    }
    posL = posR + 1;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    if (b != parsingLine.substr(posL, posR - posL)) {
        return false;
    }
    if (parsingLine.substr(posR, 3) != ")))") {
        return false;
    }
    return true;
}

bool axis4(const string& parsingLine) { // A&B->A == (->,(&,A,B),A)
    int posL = 0;
    if (parsingLine.substr(posL, 7) != "(->,(&,") {
        return false;
    }
    posL = 7;
    int i = skipMember(parsingLine, posL);
    int posR = i + 1;
    string a = parsingLine.substr(posL, posR - posL);
    posL = posR + 1;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    string b = parsingLine.substr(posL, posR - posL);
    if (parsingLine.substr(posR, 2) != "),") {
        return false;
    }
    posL = posR + 2;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    if (a != parsingLine.substr(posL, posR - posL)) {
        return false;
    }
    if (parsingLine.substr(posR, 1) != ")") {
        return false;
    }
    return true;
}

bool axis5(const string& parsingLine) { // A&B->B == (->,(&,A,B),B)
    int posL = 0;
    if (parsingLine.substr(posL, 7) != "(->,(&,") {
        return false;
    }
    posL = 7;
    int i = skipMember(parsingLine, posL);
    int posR = i + 1;
    string a = parsingLine.substr(posL, posR - posL);
    posL = posR + 1;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    string b = parsingLine.substr(posL, posR - posL);
    if (parsingLine.substr(posR, 2) != "),") {
        return false;
    }
    posL = posR + 2;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    if (b != parsingLine.substr(posL, posR - posL)) {
        return false;
    }
    if (parsingLine.substr(posR, 1) != ")") {
        return false;
    }
    return true;
}

bool axis6(const string& parsingLine) { // A->A|B == (->,A,(|,A,B))
    int posL = 0;
    if (parsingLine.substr(posL, 4) != "(->,") {
        return false;
    }
    posL = 4;
    int i = skipMember(parsingLine, posL);
    int posR = i + 1;
    string a = parsingLine.substr(posL, posR - posL);
    if (parsingLine.substr(posR, 4) != ",(|,") {
        return false;
    }
    posL = posR + 4;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    if (a != parsingLine.substr(posL, posR - posL)) {
        return false;
    }
    posL = posR + 1;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    string b = parsingLine.substr(posL, posR - posL);
    if (parsingLine.substr(posR, 2) != "))") {
        return false;
    }
    return true;
}

bool axis7(const string& parsingLine) { // B->A|B == (->,B,(|,A,B))
    int posL = 0;
    if (parsingLine.substr(posL, 4) != "(->,") {
        return false;
    }
    posL = 4;
    int i = skipMember(parsingLine, posL);
    int posR = i + 1;
    string b = parsingLine.substr(posL, posR - posL);
    if (parsingLine.substr(posR, 4) != ",(|,") {
        return false;
    }
    posL = posR + 4;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    string a = parsingLine.substr(posL, posR - posL);
    posL = posR + 1;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    if (b != parsingLine.substr(posL, posR - posL)) {
        return false;
    }
    if (parsingLine.substr(posR, 2) != "))") {
        return false;
    }
    return true;
}

bool axis8(const string& parsingLine) { // (A->C)->(B->C)->(A|B->C) == (->,(->,A,C),(->,(->,B,C),(->,(|,A,B),C)))
    int posL = 0;
    if (parsingLine.substr(posL, 8) != "(->,(->,") {
        return false;
    }
    posL = 8;
    int i = skipMember(parsingLine, posL);
    int posR = i + 1;
    string a = parsingLine.substr(posL, posR - posL);
    posL = posR + 1; // Пропускаем запятую между A и B в выражении (->,A,B)
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    string c = parsingLine.substr(posL, posR - posL);
    if (parsingLine.substr(posR, 10) != "),(->,(->,") {
        return false;
    }
    posL = posR + 10;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    string b = parsingLine.substr(posL, posR - posL);
    posL = posR + 1;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    if (c != parsingLine.substr(posL, posR - posL)) {
        return false;
    }
    if (parsingLine.substr(posR, 9) != "),(->,(|,") {
        return false;
    }
    posL = posR + 9;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    if (a != parsingLine.substr(posL, posR - posL)) {
        return false;
    }
    posL = posR + 1;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    if (b != parsingLine.substr(posL, posR - posL)) {
        return false;
    }
    if (parsingLine.substr(posR, 2) != "),") {
        return false;
    }
    posL = posR + 2;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    if (c != parsingLine.substr(posL, posR - posL)) {
        return false;
    }
    if (parsingLine.substr(posR, 3) != ")))") {
        return false;
    }
    return true;
}

bool axis9(const string& parsingLine) { // (A->B)->(A->!B)->!A == (->,(->,A,B),(->,(->,A,(!B)),(!A)))
    int posL = 0;
    if (parsingLine.substr(posL, 8) != "(->,(->,") {
        return false;
    }
    posL = 8;
    int i = skipMember(parsingLine, posL);
    int posR = i + 1;
    string a = parsingLine.substr(posL, posR - posL);
    posL = posR + 1;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    string b = parsingLine.substr(posL, posR - posL);
    if (parsingLine.substr(posR, 10) != "),(->,(->,") {
        return false;
    }
    posL = posR + 10;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    if (a != parsingLine.substr(posL, posR - posL)) {
        return false;
    }
    if (parsingLine.substr(posR, 3) != ",(!") {
        return false;
    }
    posL = posR + 3;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    if (b != parsingLine.substr(posL, posR - posL)) {
        return false;
    }
    if (parsingLine.substr(posR, 5) != ")),(!") {
        return false;
    }
    posL = posR + 5;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    if (a != parsingLine.substr(posL, posR - posL)) {
        return false;
    }
    if (parsingLine.substr(posR, 3) != ")))") {
        return false;
    }
    return true;
}

bool axis10(const string& parsingLine) { // !!A->A == (->,(!(!A)),A)
    int posL = 0;
    if (parsingLine.substr(posL, 8) != "(->,(!(!") {
        return false;
    }
    posL = 8;
    int i = skipMember(parsingLine, posL);
    int posR = i + 1;
    string a = parsingLine.substr(posL, posR - posL);
    if (parsingLine.substr(posR, 3) != ")),") {
        return false;
    }
    posL = posR + 3;
    i = skipMember(parsingLine, posL);
    posR = i + 1;
    if (a != parsingLine.substr(posL, posR - posL)) {
        return false;
    }
    if (parsingLine.substr(posR, 1) != ")") {
        return false;
    }
    return true;
}
/*while(cin.get(c)) {
            if (c == '\n') {
                break;
            }
            inputString.push_back(c);
        }*/

pair<int, int> searchMP(const map<string, int>& context, const string& expr) {
    int mpNum1 = 0;
    int mpNum2 = 0;
    for (int i = 0; i < vectorOfExpression.size(); i++) {
        if (context == vectorOfContext[i]) {
            // ищем (->,_,expr)
            int indexBeginExpr = vectorOfExpression[i].size() - 2 - expr.size();
            if (vectorOfExpression[i].find("(->,") == 0
            && vectorOfExpression[i].rfind("," + expr + ")") == indexBeginExpr) {
                string a = vectorOfExpression[i].substr(4, indexBeginExpr - 4);
                //ищем _
                for (int j = 0; j < vectorOfExpression.size(); j++) {
                    if (context == vectorOfContext[i]) {
                        if (a == vectorOfExpression[j]) {
                            mpNum1 = i + 1;
                            mpNum2 = j + 1;
                            return make_pair(mpNum1, mpNum2);
                        }
                    }
                }
            }
        }
    }
    return make_pair(mpNum1, mpNum2);
}

void makeDeductionForm(const map<string, int>& context, const string& expr) {
    map<string, int> upgradeContext(context);
    /*for (auto & it : context) {
        upgradeContext.emplace(make_pair(it.first, it.second));
    }*/
    string upgradeExpr = expr;
    int posL;
    int posR;
    int i;
    string a;
    while(upgradeExpr.find("(->,") == 0) {
        // (->,A,...)
        posL = 4;
        i = skipMember(upgradeExpr, posL);
        posR = i + 1;
        a = upgradeExpr.substr(posL, posR - posL);
        upgradeExpr = upgradeExpr.substr(posR + 1,upgradeExpr.size() - 2 - posR);
        auto temp = upgradeContext.find(a);
        if (temp != upgradeContext.end()) {
            temp->second++;
        } else {
            upgradeContext.emplace(a, 1);
        }
    }
    vectorOfDiductionForm.emplace_back(upgradeContext, upgradeExpr);
}

int searchDeduction() {
    int length = vectorOfDiductionForm.size() - 1;
    for (int i = 0; i < length; i++) {
        if (vectorOfDiductionForm[i] == vectorOfDiductionForm[length]) {
            return i + 1;
        }
    }
    return 0;
}

int main() {
    string inputString;
    int countString = 1;
    //cin >> inputString;
    getline(cin, inputString);
    while (!cin.eof()) {
        int iTurniket = inputString.find("|-");
        inputString.erase(remove_if(inputString.begin(), inputString.end(), ::isspace), inputString.end());
        line = inputString.substr(iTurniket + 2, inputString.size() - iTurniket - 2);
        string expr = line;
        pos = 0;
        int countHyp = 0;
        string resultOfParse = parse();
        vectorOfExpression.push_back(resultOfParse);
        map<string, int> context;
        string help;
        if (iTurniket == 0) {
        } else if (inputString.find(',', 0) > iTurniket) {
            help = inputString.substr(0, iTurniket);
            pos = 0;
            line = help;
            string parsingHyp = parse();
            if (help == expr) {
                countHyp = 1;
            }
            auto temp = context.find(parsingHyp);
            if (temp != context.end()) {
                temp->second++;
            } else {
                context.emplace(parsingHyp, 1);
            }
            context.emplace(parsingHyp, 1);
        } else {
            int j;
            int count = 1;
            for (int i = 0; i < iTurniket;) {
                j = inputString.find(',', i + 1);
                if (j == -1) {
                    help = inputString.substr(i, iTurniket - i);
                    pos = 0;
                    line = help;
                    string parsingHyp = parse();
                    if (help == expr) {
                        countHyp = count;
                    }
                    auto temp = context.find(parsingHyp);
                    if (temp != context.end()) {
                        temp->second++;
                    } else {
                        context.emplace(parsingHyp, 1);
                    }
                    break;
                }
                help = inputString.substr(i, j - i);
                pos = 0;
                line = help;
                string parsingHyp = parse();
                if (help == expr) {
                    countHyp = count;
                }
                auto temp = context.find(parsingHyp);
                if (temp != context.end()) {
                    temp->second++;
                } else {
                    context.emplace(parsingHyp, 1);
                }
                i = j + 1;
                count++;
            }
        }
        vectorOfContext.push_back(context);
        makeDeductionForm(context, resultOfParse);
        string result;
        // Проверяем гипотезы
        if (countHyp != 0) {
            result = "[" + to_string(countString) + "] " + inputString + " [Hyp. " + to_string(countHyp) + "]";
            cout << result << "\n";
        // Проверяем аксиомы
        } else if (axis1(resultOfParse)) {
            result = "[" + to_string(countString) + "] " + inputString + " [Ax. sch. 1]";
            cout << result << "\n";
        } else if (axis2(resultOfParse)) {
            result = "[" + to_string(countString) + "] " + inputString + " [Ax. sch. 2]";
            cout << result << "\n";
        } else if (axis3(resultOfParse)) {
            result = "[" + to_string(countString) + "] " + inputString + " [Ax. sch. 3]";
            cout << result << "\n";
        } else if (axis4(resultOfParse)) {
            result = "[" + to_string(countString) + "] " + inputString + " [Ax. sch. 4]";
            cout << result << "\n";
        } else if (axis5(resultOfParse)) {
            result = "[" + to_string(countString) + "] " + inputString + " [Ax. sch. 5]";
            cout << result << "\n";
        } else if (axis6(resultOfParse)) {
            result = "[" + to_string(countString) + "] " + inputString + " [Ax. sch. 6]";
            cout << result << "\n";
        } else if (axis7(resultOfParse)) {
            result = "[" + to_string(countString) + "] " + inputString + " [Ax. sch. 7]";
            cout << result << "\n";
        } else if (axis8(resultOfParse)) {
            result = "[" + to_string(countString) + "] " + inputString + " [Ax. sch. 8]";
            cout << result << "\n";
        } else if (axis9(resultOfParse)) {
            result = "[" + to_string(countString) + "] " + inputString + " [Ax. sch. 9]";
            cout << result << "\n";
        } else if (axis10(resultOfParse)) {
            result = "[" + to_string(countString) + "] " + inputString + " [Ax. sch. 10]";
            cout << result << "\n";
            // Проверяем Modus Ponens
        } else if (searchMP(context, resultOfParse) != make_pair(0, 0)) {
            pair<int, int> mpNum = searchMP(context, resultOfParse);
            result = "[" + to_string(countString) + "] " + inputString + " [M.P. " + to_string(mpNum.first) + ", " + to_string(mpNum.second) +  "]";
            cout << result << "\n";
            // Проверяем Дедукцию
        } else if (searchDeduction() != 0) {
            int numDed = searchDeduction();
            result = "[" + to_string(countString) + "] " + inputString + " [Ded. " + to_string(numDed) + "]";
            cout << result << "\n";
            // Выводим некорректную
        } else {
            result = "[" + to_string(countString) + "] " + inputString + " [Incorrect]";
            cout << result << "\n";
        }
        /*for (auto & it : context) {
            cout << it.first << ' ' << it.second << "\n";
        }*/
        //cout << resultOfParse << "\n";
        /*if (cin.eof()) {
            break;
        }*/
        getline(cin, inputString);
        countString++;
    }
    //cin.clear();
}