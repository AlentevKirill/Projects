const potion = {
    name: 'Название',
    expirationDays: 5,
    created: new Date(2023, 0, 1), // 1 Января 2023.
    use: function(){},
};



function makePotionsRoom() {
    return {
        // хранилище кладовки
        store: new Map(),

        // добавляет зелье на указанную полку, метод ничего не возвращает
        add: function (shelveName, potion) {
            this.store.set(potion, shelveName)
        },

        // Возвращает зелье, если оно есть на любой из полок. Зелье убирается из кладовки (с любой из полок, где есть зелье)
        takePotion: function (namePotion) {
            for (let key of this.store.keys()) {
                if (key.name === namePotion) {
                    const save = key;
                    this.store.delete(key);
                    return save;
                }
            }
            return null;
        },

        // Использует зелье (вызывая у него функцию "use"). Зелье убирается из кладовки (с любой из полок, где есть зелье).
        usePotion: function (namePotion) {
            for (let key of this.store.keys()) {
                if (key.name === namePotion) {
                    key.use();
                    this.store.delete(key);
                    return;
                }
            }
        },

        // Возвращает все зелья с полки. Содержимое полки не меняется
        getAllPotionsFromShelve: function (shelveName) {
            let arr = [];
            let i = 0;
            for (let [key, value] of this.store.entries()) {
                if (value === shelveName) {
                    arr[i] = key;
                    i++;
                }
            }
            return arr;
        },

        // Возвращает все зелья кладовки. Содержимое полок не меняется
        getAllPotions: function () {
            let arr = [];
            let i = 0;
            for (let key of this.store.keys()) {
                arr[i] = key;
                i++;
            }
            return arr;
        },

        // Возвращает все зелья с полки. Полка остается пустой
        takeAllPotionsFromShelve: function (shelveName) {
            let arr = [];
            let i = 0;
            for (let [key, value] of this.store.entries()) {
                if (value === shelveName) {
                    arr[i] = key;
                    i++;
                    this.store.delete(key); // ? Можно ли удалять во время прохода
                }
            }
            return arr;
        },

        // Использует все зелья с указанной полки. Полка остается пустой
        useAllPotionsFromShelve: function (shelveName) {
            for (let [key, value] of this.store.entries()) {
                if (value === shelveName) {
                    key.use();
                    this.store.delete(key); // ? Можно ли удалять во время прохода
                }
            }
        },

        // Возвращает зелья с истекшим сроком хранения. Метод убирает такие зелья из кладовки.
        // revisionDay - день (Date), в который происходит проверка сроков хранения
        clean: function (revisionDay) {
            let arr = [];
            let i = 0;
            for (let key of this.store.keys()) {
                if ((revisionDay.getTime() - key.created.getTime()) / (1000 * 3600 * 24) > key.expirationDays) {
                    arr[i] = key;
                    i++;
                    this.store.delete(key);
                }
            }
            return arr;
        },

        // возвращает число - сколько уникальных названий зелий находится в кладовке
        uniquePotionsCount() {
            let set = new Set();
            for (let key of this.store.keys()) {
                set.add(key.name);
            }
            return set.size;
        },
    };
}